package com.herms.taskme.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.Message;
import com.herms.taskme.repository.ConversationRepository;
import com.herms.taskme.repository.UserRepository;

@Service
public class MessageConverter{

	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private UserRepository userRepository;
	
    private ModelMapper modelMapper;

	public MessageConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toMessageDTOConv);
	}

	public List<Message> fromMessagetDTO(List<MessageDTO> messageDTOsList) {
		return messageDTOsList.stream()
 				.map(msg -> fromMessagetDTO(msg)).collect(Collectors.toList());
	}

	public Message fromMessagetDTO(MessageDTO messageDTO) {
		Message msg = new Message();
    	modelMapper.map(messageDTO, msg);
    	return msg;
	}
	
    public List<MessageDTO> toMessagetDTO(List<Message> messageList) {
    	return messageList.stream()
				 				.map(msg -> toMessagetDTO(msg)).collect(Collectors.toList());
    }
    
    /**
     * This method is used to return a DTO of the Message model, 
     * @param message original object to be DTOrized
     * @return MessageDTO
     */
    public MessageDTO toMessagetDTO(Message message) {
    	MessageDTO messageDTO = new MessageDTO();
    	modelMapper.map(message, messageDTO);
    	return messageDTO;
    }

	Converter<Message, MessageDTO> toMessageDTOConv = new Converter<Message, MessageDTO>() {

	      @Override
	      public MessageDTO convert(MappingContext<Message, MessageDTO> context) {	   
	    	context.getDestination().setId(context.getSource().getId());
	    	context.getDestination().setContent(context.getSource().getContent());
	    	context.getDestination().setSenderName(context.getSource().getSender().getGivenName() + " " + context.getSource().getSender().getFamilyName());
	    	context.getDestination().setUserSenderId(context.getSource().getSender().getId());
	    	context.getDestination().setSentTime(context.getSource().getSentTime());
	    	context.getDestination().setConversationId(context.getSource().getConversation().getId());

	    	return context.getDestination();
	      }
	   };
	   
   Converter<MessageDTO, Message> fromMessageDTOConv = new Converter<MessageDTO, Message>() {

	      @Override
	      public Message convert(MappingContext<MessageDTO, Message> context) {	    	  
	    	context.getDestination().setId(context.getSource().getId());
		    context.getDestination().setContent(context.getSource().getContent());
		    context.getDestination().setSender(userRepository.getOne(context.getSource().getUserSenderId()));
		    context.getDestination().setSentTime(context.getSource().getSentTime());
		    context.getDestination().setConversation(conversationRepository.getOne(context.getSource().getConversationId()));

		    return context.getDestination(); 
	      }
	   };

}
