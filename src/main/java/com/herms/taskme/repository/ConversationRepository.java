package com.herms.taskme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.User;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    public List<Conversation> findAllByUserListContaining(User user);
}
