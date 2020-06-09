package com.herms.taskme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.herms.taskme.converter.MessageConverter;
import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.Message;
import com.herms.taskme.model.User;
import com.herms.taskme.service.ConversationService;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.MessageService;

@Controller
public class ChatController {
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageConverter messageConverter;
	@Autowired
	private ConversationService conversationService;
	
	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public MessageDTO sendMessage(@Payload MessageDTO chatMessage) {
		return chatMessage;
	}

	@MessageMapping("/chat.send/{chatRoomId}")
	public MessageDTO sendMessage(@Payload MessageDTO chatMessage, @DestinationVariable("chatRoomId") String chatRoomId) throws Exception {
		Message msg = messageService.addMessage(chatMessage);
		if(msg == null) {
			throw new Exception("Unable to send the message");
		}
		MessageDTO msgDTO = messageConverter.toMessagetDTO(msg);
		simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, chatMessage);
		
		return msgDTO;
	}
}
