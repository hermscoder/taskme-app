package com.herms.taskme.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.herms.taskme.converter.ConversationConverter;
import com.herms.taskme.dto.ConversationDTO;
import com.herms.taskme.dto.UserDTO;
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
    private ConversationConverter conversationConverters;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/conversation")
    public ResponseEntity<List<ConversationDTO>> getUserConversations() throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        if(principal == null){
            throw new Exception("You don't have access to this function");
        }

        List<Conversation> userConversationsList = conversationService.getAllUserConversations(principal);
        return new ResponseEntity<>(conversationConverters.toConversationtDTO(userConversationsList), HttpStatus.OK);

    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/conversation")
    public ResponseEntity<ConversationDTO> createConversationWith(@RequestBody ConversationDTO conversationDTO) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        
        if( principal == null){
            throw new Exception("You don't have access to this function");
        }
        
        if(conversationDTO.getParticipants()
        					.stream().noneMatch(user -> principal.getId().equals(user.getId()))) {
        	conversationDTO.getParticipants().add(new UserDTO(principal));
        }
        Conversation conversation = conversationConverters.fromConversationtDTO(conversationDTO);
        
        conversationDTO = conversationConverters.toConversationtDTO(conversationService.addConversation(conversation));
        return new ResponseEntity<>(conversationDTO, HttpStatus.OK);

    }
}
