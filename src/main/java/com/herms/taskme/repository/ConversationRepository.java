package com.herms.taskme.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.herms.taskme.model.Conversation;
import com.herms.taskme.model.User;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    public List<Conversation> findAllByUserListContaining(User user);
    
    @Query("SELECT c FROM Conversation c inner join c.userList ul WHERE ul.id in :users GROUP BY c HAVING count(c) > 1")
    public List<Conversation> findByParticipants(@Param("users")Collection<Long> userIds);
}
