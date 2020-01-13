package com.herms.taskme.repository;

import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskSomeoneRepository extends CrudRepository<TaskSomeone, Long>, PagingAndSortingRepository<TaskSomeone, Long> {

    public List<TaskSomeone> findAllByOrderByCreatedOnDesc();
    public Page<TaskSomeone> findAllByOrderByCreatedOnDesc(Pageable pageable);

    public List<TaskSomeone> findAllByUserOrderByCreatedOnDesc(User user);
    public Page<TaskSomeone> findAllByUserOrderByCreatedOnDesc(User user, Pageable pageable);
}
