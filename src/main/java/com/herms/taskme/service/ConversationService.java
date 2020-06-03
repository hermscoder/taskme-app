package com.herms.taskme.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.ConversationForListDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.ConversationRepository;
import com.herms.taskme.repository.UserRepository;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;
    public ConversationService() {
    }
    
    public List<Conversation> getAllUserConversations(User user){
        return conversationRepository.findAllByUserListContaining(user);
    }

	public Conversation getConversation(Long conversationId) {
		return conversationRepository.findById(conversationId).orElse(null);
	}

	public Conversation addConversation(Conversation conversation) {
		conversation.setCreatedOn(new Date());
		List<User> lazyFetchedUsers = conversation.getUserList().stream()
																.map(user -> userRepository.getOne(user.getId()))
																.collect(Collectors.toList());
		conversation.setUserList(lazyFetchedUsers);

		return conversationRepository.save(conversation);
	}

}
