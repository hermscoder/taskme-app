package com.herms.taskme.controller;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.model.*;
import com.herms.taskme.converter.TaskApplicationConverter;
import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.dto.MsgAndNewApplicationStatus;
import com.herms.taskme.dto.TaskApplicationDetailsDTO;
import com.herms.taskme.dto.TaskApplicationForListDTO;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.MessageService;
import com.herms.taskme.service.TaskApplicationService;
import com.herms.taskme.service.TaskSomeoneService;
import com.herms.taskme.service.TaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logged")
public class TaskApplicationController {
    @Autowired
    private TaskApplicationService taskApplicationService;
    @Autowired
    private TaskApplicationConverter taskApplicationConverter;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TaskSomeoneService taskSomeoneService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/taskapplication/{taskSomeoneId}")
    public ResponseEntity<Page<TaskApplicationDetailsDTO>> getTaskApplicationPages(
    		@PathVariable Long taskSomeoneId,
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value="status", defaultValue = "", required = false) String status) {

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskApplication> applicationsList = taskApplicationService.getAllTaskApplicationByTaskIdAndStatusPaginated(pageRequest, taskSomeoneId, searchTerm, ApplicationStatus.fromCode(status));
        applicationsList.map(application -> taskApplicationConverter.toDTO(application, TaskApplicationDetailsDTO.class));
        return ResponseEntity.ok(applicationsList.map(application -> taskApplicationConverter.toDTO(application, TaskApplicationDetailsDTO.class)));
    }

    @RequestMapping("/taskapplication/{id}")
    public ResponseEntity<TaskApplicationDetailsDTO> getTaskApplication(@PathVariable Long id) throws Exception{
    	return new ResponseEntity<>(taskApplicationConverter.toDTO(taskApplicationService.getTaskApplication(id), TaskApplicationDetailsDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/taskapplication")
    public ResponseEntity<TaskApplicationForListDTO> addTaskApplication(@RequestBody TaskApplication taskSomeone) throws Exception{
    	TaskApplicationForListDTO dto = taskApplicationConverter.toDTO(taskApplicationService.addTaskApplication(taskSomeone), TaskApplicationForListDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/taskapplication/{id}")
    public ResponseEntity<TaskApplicationDetailsDTO> updateTaskApplication(@RequestBody TaskApplication taskSomeone, @PathVariable Long id) throws Exception{
    	User principal = customUserDetailsService.getLoggedUser();
        TaskApplication originalTaskApplication = taskApplicationService.getTaskApplication(id);
        
        if(originalTaskApplication == null || !originalTaskApplication.getUser().equals(principal)){
            throw new Exception("You don't have access to this function");
        }
        
    	TaskApplicationDetailsDTO dto = taskApplicationConverter.toDTO(
    										taskApplicationService.updateTaskApplication(id, taskSomeone, originalTaskApplication)
    										, TaskApplicationDetailsDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/taskapplication/{id}")
    public void deleteTaskApplication(@PathVariable Long id){
        taskApplicationService.deleteTaskApplication(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/taskapplication/cancel")
    public ResponseEntity<TaskApplicationDetailsDTO> cancelApplication(@RequestBody TaskApplicationDetailsDTO taskApplicationDTO) throws Exception{
    	TaskApplication application = taskApplicationConverter.fromDTO(taskApplicationDTO);
    	User principal = customUserDetailsService.getLoggedUser();
    	
    	if(principal == null || !principal.equals(application.getUser())) {
    		throw new Exception("You can not cancel applications from another users!");
    	}
    	TaskApplicationDetailsDTO dto = taskApplicationConverter.toDTO(taskApplicationService.cancelApplication(application), TaskApplicationDetailsDTO.class);
        return new ResponseEntity<TaskApplicationDetailsDTO>(dto, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/taskapplication")
    public ResponseEntity<Page<TaskApplicationDetailsDTO>> getCurrentUserTaskApplications(
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value="status", defaultValue = "", required = false) String status) throws Exception {
        User principal = customUserDetailsService.getLoggedUser();

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskApplication> taskApplicationsCreatedBy = taskApplicationService.getAllTaskApplicationCreatedByUserIdAndStatusPaginated(pageRequest, principal.getId(), searchTerm, ApplicationStatus.fromCode(status));
        Page<TaskApplicationDetailsDTO> taskApplicationDTOs = taskApplicationsCreatedBy.map((taskApplication) -> {
			try {
				return taskApplicationConverter.toDTO(taskApplication, TaskApplicationDetailsDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
        return new ResponseEntity<>(taskApplicationDTOs, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/taskapplication/sendApplyingMsg/{taskSomeoneId}")
    public ResponseEntity<TaskApplicationDetailsDTO> sendMsgToOwnerOfTaskAndApply(@PathVariable Long taskSomeoneId, @RequestBody MessageDTO message) throws Exception {

    	TaskSomeone taskSomeone = taskSomeoneService.getTaskSomeone(taskSomeoneId);
    	
        User principal = customUserDetailsService.getLoggedUser();
        if(principal == null ||  principal.getId().equals(taskSomeone.getUser())){
            throw new Exception("You don't have access to this function");
        }
        message.setUserSenderId(principal.getId());
        
        TaskApplicationDetailsDTO dto = taskApplicationConverter.toDTO(taskApplicationService.sendMsgAndApply(taskSomeone, message), TaskApplicationDetailsDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/taskapplication/changeStatusAndSendMsgToApplicant")
    public ResponseEntity<TaskApplicationDetailsDTO> changeApplicationStatus(@RequestBody MsgAndNewApplicationStatus msgAndNewStatus) throws Exception{
    	TaskApplication application = taskApplicationService.getTaskApplication(msgAndNewStatus.getTaskApplicationId());
    	User principal = customUserDetailsService.getLoggedUser();
    	
    	if(principal == null || !principal.equals(application.getTaskSomeone().getUser())) {
    		throw new Exception("You can not change applications related to other users tasks!");
    	}

    	msgAndNewStatus.getUpdateStatusMsg().setUserSenderId(principal.getId());
    	TaskApplicationDetailsDTO dto = taskApplicationConverter.toDTO(taskApplicationService.changeApplicationStatusAndSendMsgToApplicant(application, msgAndNewStatus.getNewStatusCode(), msgAndNewStatus.getUpdateStatusMsg()), TaskApplicationDetailsDTO.class);
        return new ResponseEntity<TaskApplicationDetailsDTO>(dto, HttpStatus.OK);
    }
}
