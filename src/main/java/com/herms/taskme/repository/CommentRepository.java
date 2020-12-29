package com.herms.taskme.repository;


import com.herms.taskme.model.Comment;
import com.herms.taskme.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
    public Page<Comment> findAllByTaskSomeoneIdOrderBySentTimeAsc(Pageable pageable, Long taskSomeoneId);
}
