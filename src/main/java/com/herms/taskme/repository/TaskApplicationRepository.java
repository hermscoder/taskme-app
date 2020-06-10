package com.herms.taskme.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;

@Repository
public interface TaskApplicationRepository extends CrudRepository<TaskApplication, Long>, PagingAndSortingRepository<TaskApplication, Long> {
	
	@Query("select ta from TaskApplication ta where ta.taskSomeone.id = :taskSomeoneId and UPPER(ta.taskSomeone.title) like CONCAT('%', UPPER(:term), '%') order by createdOn")
	public Page<TaskApplication> findAllByTaskSomeoneAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(Pageable pageable, @Param("taskSomeoneId") Long taskSomeoneId, @Param("term") String term);
	@Query("select ta from TaskApplication ta where ta.user.id = :userId and UPPER(ta.taskSomeone.title) like CONCAT('%', UPPER(:term), '%') order by createdOn")
    public Page<TaskApplication> findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(Pageable pageable, @Param("userId") Long userId, @Param("term") String term);
}
