package com.herms.taskme.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.herms.taskme.ConversationConverters;
import com.herms.taskme.dto.ConversationForListDTO;
import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.User;
import com.herms.taskme.service.ConversationService;
import com.herms.taskme.service.CustomUserDetailsService;

@RestController
@RequestMapping("/logged")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ConversationConverters conversationConverters;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/conversation")
    public ResponseEntity<List<ConversationForListDTO>> getUserConversations() throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        if(principal == null){
            throw new Exception("You don't have access to this function");
        }

        List<Conversation> userConversationsList = conversationService.getAllUserConversations(principal);
        return new ResponseEntity<>(conversationConverters.toConversationForListDTO(userConversationsList), HttpStatus.OK);

    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/conversation")
    public ResponseEntity<Conversation> createConversationWith(@RequestBody Conversation conversation) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        
        if(principal == null ||  !conversation.getUserList().stream().map(User::getId).collect(Collectors.toList()).contains(principal.getId())){
            throw new Exception("You don't have access to this function");
        }
        
        return new ResponseEntity<>(conversationService.addConversation(conversation), HttpStatus.OK);

    }
}
