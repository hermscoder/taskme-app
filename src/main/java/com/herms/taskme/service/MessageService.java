package com.herms.taskme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.herms.taskme.model.Message;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageService() {
    }

    public Message addMessage(Message msg) {
    	return messageRepository.save(msg);
    }

    public Page<Message> getMessagesFromConvPaginated(Pageable pageable, Long conversationId){
        return messageRepository.findAllByConversationIdOrderBySentTime(pageable, conversationId);
    }

}
