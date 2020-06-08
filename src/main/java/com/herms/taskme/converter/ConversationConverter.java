package com.herms.taskme.converter;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.ConversationDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.Message;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.MessageRepository;
import com.herms.taskme.repository.UserRepository;

@Service
public class ConversationConverter{
	@Autowired
	private MessageConverter messageConverters;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserRepository userRepository;
	
    private ModelMapper modelMapper;

	public ConversationConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toConversationDTOConv);
	}

	public List<Conversation> fromConversationtDTO(List<ConversationDTO> conversationDTOsList) {
		return conversationDTOsList.stream()
 				.map(conversation -> fromConversationtDTO(conversation)).collect(Collectors.toList());
	}

	public Conversation fromConversationtDTO(ConversationDTO conversationDTO) {
		Conversation conversation = new Conversation();
    	modelMapper.map(conversationDTO, conversation);
    	return conversation;
	}
	
	public List<ConversationDTO> toConversationtDTO(List<Conversation> conversationList) {
		return toConversationtDTO(conversationList, false);
	}
	
    public List<ConversationDTO> toConversationtDTO(List<Conversation> conversationList, boolean loadLastMessages) {
    	return conversationList.stream()
				 				.map(conversation -> toConversationtDTO(conversation,loadLastMessages)).collect(Collectors.toList());
    }
    
    public ConversationDTO toConversationtDTO(Conversation conversation) {
    	return toConversationtDTO(conversation, false);
    }
    /**
     * This method is used to return a DTO of the Conversation model, and it has the option if the user 
     * wants a DTO with the last 20 messages of the conversation (for quick presentation purposes).
     * @param conversation original object to be DTOrized
     * @param loadLastMessages[false] loads 20 last messages of the conversation
     * @return ConversationDTO
     */
    public ConversationDTO toConversationtDTO(Conversation conversation, boolean loadLastMessages) {
    	ConversationDTO conversationDTO = new ConversationDTO();
    	modelMapper.map(conversation, conversationDTO);
    	return conversationDTO;
    }

	Converter<Conversation, ConversationDTO> toConversationDTOConv = new Converter<Conversation, ConversationDTO>() {

	      @Override
	      public ConversationDTO convert(MappingContext<Conversation, ConversationDTO> context) {	    	    
	         //This custom converter replaces the one automatically created by ModelMapper,
	         //So we have to map each of the contact fields as well.
	         context.getDestination().setId(context.getSource().getId());
	         context.getDestination().setHasUnreadMessages(context.getSource().getHasUnreadMessages());
	         context.getDestination().setCreatedOn(context.getSource().getCreatedOn());
	         for (User user : context.getSource().getUserList()) {
	            context.getDestination().getUserMap().put(user.getId(), new UserDTO(user));
	         }
	         List<Message> lastMsgs = messageRepository.findFirst20ByConversationIdOrderBySentTime(context.getSource().getId());
	         context.getDestination().setMessagesList(messageConverters.toMessagetDTO(lastMsgs));
	         
	         return context.getDestination();
	      }
	   };
	   
	   Converter<ConversationDTO, Conversation> fromConversationDTOConv = new Converter<ConversationDTO, Conversation>() {

		      @Override
		      public Conversation convert(MappingContext<ConversationDTO, Conversation> context) {	    	    
		         //This custom converter replaces the one automatically created by ModelMapper,
		         //So we have to map each of the contact fields as well.
		         context.getDestination().setId(context.getSource().getId());
		         context.getDestination().setHasUnreadMessages(context.getSource().getHasUnreadMessages());
		         context.getDestination().setCreatedOn(context.getSource().getCreatedOn());

		         for (Entry<Long, UserDTO> entry : context.getSource().getUserMap().entrySet()) {
		            context.getDestination().getUserList().add(userRepository.getOne(entry.getKey()));
		         }
		         
		         context.getDestination().setMessagesList(messageConverters.fromMessagetDTO(context.getSource().getMessagesList()));
		         
		         return context.getDestination();
		      }
		   };

}
