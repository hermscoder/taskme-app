package com.herms.taskme.repository;

import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where UPPER(u.username) = UPPER(:username)")
    public User findByUsername(@Param("username") String username);
    
    @Query("select ta.user from TaskApplication ta where ta.taskSomeone.id = :taskSomeoneId order by created_on")
	public List<User> findAllTaskApplicantsByTaskSomeone_IdOrderByCreatedOnDesc(@Param("taskSomeoneId") Long taskSomeoneId);
}
