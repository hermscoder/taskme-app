package com.herms.taskme.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.herms.taskme.converter.MessageConverter;
import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.Message;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private ConversationService conversationService;
    
    public MessageService() {
    }

    public Message addMessage(MessageDTO messageDTO) {
    	Message msg = messageConverter.fromMessagetDTO(messageDTO);
    	
    	Conversation conversation = conversationService.getConversation(messageDTO.getConversationId());
    	conversation.getMessagesList().add(msg);
    	msg.setConversation(conversation);
    	conversationService.updateConversation(conversation);
//    	return addMessage(msg);
		return msg;
    }
    
    public Message addMessage(Message msg) {
    	return messageRepository.save(msg);
    }

    public Message sendMessageTo(Long targetUserId, MessageDTO msgDTO) {
    	List<Conversation> conversationList = conversationService.findConversationByParticipants(Arrays.asList(targetUserId, msgDTO.getUserSenderId()));
    	Message msg = messageConverter.fromMessagetDTO(msgDTO);
    	msg.setSentTime(new Date());
    	
    	Conversation conversation;
    	if(conversationList != null && !conversationList.isEmpty()) {
    		conversation = conversationList.get(0);
    		conversation.getMessagesList().add(msg);
        	msg.setConversation(conversation);
        	conversationService.updateConversation(conversation);
    	} else {
    		conversation = new Conversation();
    		conversation.setUserList(Arrays.asList(new User(targetUserId), msg.getSender()));
    		conversation.getMessagesList().add(msg);
        	msg.setConversation(conversation);
    		conversationService.addConversation(conversation);
    	}
    	
    	return msg;
    }
    
    public Page<Message> getMessagesFromConvPaginated(Pageable pageable, Long conversationId){
        return messageRepository.findAllByConversationIdOrderBySentTime(pageable, conversationId);
    }

}
