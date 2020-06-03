package com.herms.taskme.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.herms.taskme.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>, PagingAndSortingRepository<Message, Long> {
    public Page<Message> findAllByConversationIdOrderBySentTime(Pageable pageable, Long conversationId);
    
    public List<Message> findFirst20ByConversationIdOrderBySentTime(Long conversationId);
}
