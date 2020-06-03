package com.herms.taskme;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.ConversationForListDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.Message;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.MessageRepository;

@Service
public class ConversationConverters{

	@Autowired
	private MessageRepository messageRepository;

    private ModelMapper modelMapper;

	public ConversationConverters() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toConversationForListDTOConv);
	}

    public List<ConversationForListDTO> toConversationForListDTO(List<Conversation> conversationList) {
    	return conversationList.stream()
				 				.map(conversation -> toConversationForListDTO(conversation)).collect(Collectors.toList());
    }
    
    public ConversationForListDTO toConversationForListDTO(Conversation conversation) {
    	ConversationForListDTO conversationDTO = new ConversationForListDTO();
    	modelMapper.map(conversation, conversationDTO);
    	return conversationDTO;
    }

	Converter<Conversation, ConversationForListDTO> toConversationForListDTOConv = new Converter<Conversation, ConversationForListDTO>() {

	      @Override
	      public ConversationForListDTO convert(MappingContext<Conversation, ConversationForListDTO> context) {	    	    
	         //This custom converter replaces the one automatically created by ModelMapper,
	         //So we have to map each of the contact fields as well.
	         context.getDestination().setId(context.getSource().getId());
	         context.getDestination().setHasUnreadMessages(context.getSource().getHasUnreadMessages());
	         context.getDestination().setMessagesList(context.getSource().getMessagesList());
	         context.getDestination().setCreatedOn(context.getSource().getCreatedOn());

	         for (User user : context.getSource().getUserList()) {
	            context.getDestination().getUserList().add(new UserDTO(user));
	         }

	         context.getDestination().setMessagesList(messageRepository.findFirst20ByConversationIdOrderBySentTime(context.getSource().getId()));
	         return context.getDestination();
	      }
	   };

}
