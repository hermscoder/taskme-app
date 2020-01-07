package com.herms.taskme.repository;

import com.herms.taskme.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "select u from User u where u.username = :username")
    public User findByUsername(@Param("username") String username);
}
