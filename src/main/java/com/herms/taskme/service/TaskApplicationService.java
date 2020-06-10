package com.herms.taskme.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.repository.TaskApplicationRepository;

@Service
public class TaskApplicationService {
    @Autowired
    private TaskApplicationRepository taskApplicationRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private MediaService mediaService;

    public TaskApplicationService() {
    }

    public TaskApplication getTaskApplication(Long id){
        return taskApplicationRepository.findById(id).get();
    }

    
    public Page<TaskApplication> getAllTaskApplicationByTaskIdPaginated(Pageable pageable, Long taskSomeoneId, String term){
        return taskApplicationRepository.findAllByTaskSomeoneAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, taskSomeoneId, term);
    }
    
    public Page<TaskApplication> getAllTaskApplicationCreatedByUserIdPaginated(Pageable pageable, Long userId, String term){
        return taskApplicationRepository.findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, userId, term);
    }

    public TaskApplication addTaskApplication(TaskApplication taskApplication){
    	taskApplication.setCreatedOn(new Date());
    	taskApplication.setUser(customUserDetailsService.getLoggedUser());
    	taskApplication.setStatus(ApplicationStatus.PENDING);
        return taskApplicationRepository.save(taskApplication);
    }
    
    public TaskApplication updateTaskApplication(Long id, TaskApplication taskApplication) {
    	return updateTaskApplication(id, taskApplication, null);
    }
    
    public TaskApplication updateTaskApplication(Long id, TaskApplication taskApplication, TaskApplication originalTaskApplication) {        
        TaskApplication original = originalTaskApplication;
    	if(original == null) {
    		original = getTaskApplication(taskApplication.getId());
    	}

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(taskApplication, original);
        return taskApplicationRepository.save(original);
    }
    
    public void deleteTaskApplication(Long id){
    	taskApplicationRepository.deleteById(id);
    }
}
