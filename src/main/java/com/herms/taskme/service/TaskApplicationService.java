package com.herms.taskme.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.model.Message;
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
    private MessageService messageService;

    public TaskApplicationService() {
    }

    public TaskApplication getTaskApplication(Long id){
        return taskApplicationRepository.findById(id).get();
    }

    public List<TaskApplication> getAllTaskApplicationByTaskId(Long taskSomeoneId){
        return taskApplicationRepository.findAllByTaskSomeone_IdOrderByCreatedOnDesc(taskSomeoneId);
    }

    public Page<TaskApplication> getAllTaskApplicationByTaskIdAndStatusPaginated(Pageable pageable, Long taskSomeoneId, String term, ApplicationStatus status){
        return taskApplicationRepository.findAllByTaskSomeoneAndTitleContainingIgnoreCaseAndStatusOrderByCreatedOnDesc(pageable, taskSomeoneId, term, status);
    }
    
    public List<TaskApplication> getAllTaskApplicationCreatedByUserId(Long userId){
        return taskApplicationRepository.findAllByUser_IdOrderByCreatedOnDesc(userId);
    }

    public Page<TaskApplication> getAllTaskApplicationCreatedByUserIdAndStatusPaginated(Pageable pageable, Long userId, String term, ApplicationStatus status){
        return taskApplicationRepository.findAllByUser_IdAndTitleContainingIgnoreCaseAndStatusOrderByCreatedOnDesc(pageable, userId, term, status);
    }

    public TaskApplication addTaskApplication(TaskApplication taskApplication){
    	taskApplication.setCreatedOn(new Date());
    	taskApplication.setUser(customUserDetailsService.getLoggedUser());
    	if(taskApplication.getStatus() == null) {
    		taskApplication.setStatus(ApplicationStatus.PENDING);    		
    	}
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

	public TaskApplication sendMsgAndApply(TaskSomeone taskSomeone, MessageDTO messageDTO) {
		Message message = messageService.sendMessageTo(taskSomeone.getUser().getId(), messageDTO);
		messageDTO = new MessageDTO(message);

		TaskApplication taskApplication = new TaskApplication();
		taskApplication.setCreatedOn(new Date());
		taskApplication.setUser(customUserDetailsService.getLoggedUser());
		taskApplication.setTaskSomeone(taskSomeone);
		taskApplication.setApplyingMessage(message);

		return addTaskApplication(taskApplication);
	}

	public TaskApplication sendAndSetAcceptationMsgToApplicant(TaskApplication taskApplication, MessageDTO messageDTO) {
		Message message = messageService.sendMessageTo(taskApplication.getUser().getId(), messageDTO);
		
		taskApplication.setStatus(ApplicationStatus.ACCEPTED);
		return updateTaskApplication(taskApplication.getId(), taskApplication);
	}
	
	public TaskApplication cancelApplication(TaskApplication application) {
		application.setStatus(ApplicationStatus.CANCELLED_BY_APPLICANT);
		return updateTaskApplication(application.getId(), application);
	}
	
	public TaskApplication changeApplicationStatusAndSendMsgToApplicant(TaskApplication application, String newStatus, MessageDTO messageDTO) {
		Message message = messageService.sendMessageTo(application.getUser().getId(), messageDTO);

		application.setStatus(ApplicationStatus.fromCode(newStatus));
		return updateTaskApplication(application.getId(), application);
	}
	
}
