package com.herms.taskme.repository;

import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskSomeoneRepository extends CrudRepository<TaskSomeone, Long> {

    public List<TaskSomeone> findAllByOrderByCreatedOnDesc();

    public List<TaskSomeone> findAllByUserOrderByCreatedOnDesc(User user);
}
