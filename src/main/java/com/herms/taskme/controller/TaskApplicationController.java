package com.herms.taskme.controller;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.*;
import com.herms.taskme.converter.TaskApplicationConverter;
import com.herms.taskme.dto.TaskApplicationDetailsDTO;
import com.herms.taskme.dto.TaskApplicationForListDTO;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.TaskApplicationService;
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

    @RequestMapping(method = RequestMethod.GET, value = "/taskapplication/{taskSomeoneId}")
    public ResponseEntity<Page<TaskApplication>> getTaskApplicationPages(
    		@PathVariable Long taskSomeoneId,
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) {

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskApplication> pontoRotaList = taskApplicationService.getAllTaskApplicationByTaskIdPaginated(pageRequest, taskSomeoneId, searchTerm);

        return ResponseEntity.ok(pontoRotaList);
    }

    @RequestMapping("/taskapplication/{id}")
    public ResponseEntity<TaskApplicationDetailsDTO> getTaskApplication(@PathVariable Long id) throws Exception{
    	return new ResponseEntity<>(taskApplicationConverter.toDTO(taskApplicationService.getTaskApplication(id), TaskApplicationDetailsDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/taskapplication")
    public ResponseEntity<TaskApplicationForListDTO> addTaskApplication(@RequestBody TaskApplication taskSomeone){
        return new ResponseEntity<>(new TaskApplicationForListDTO(taskApplicationService.addTaskApplication(taskSomeone)), HttpStatus.OK);
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

    @RequestMapping(method = RequestMethod.GET, value = "/taskapplication/applications")
    public ResponseEntity<Page<TaskApplicationForListDTO>> getCurrentUserTaskApplications(
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) throws Exception {
        User principal = customUserDetailsService.getLoggedUser();

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskApplication> taskSomeoneCreatedBy = taskApplicationService.getAllTaskApplicationCreatedByUserIdPaginated(pageRequest, principal.getId(), searchTerm);

        Page<TaskApplicationForListDTO> taskSomeoneForListDTOs = taskSomeoneCreatedBy.map(TaskApplicationForListDTO::new);
        return new ResponseEntity<>(taskSomeoneForListDTOs, HttpStatus.OK);
    }

}
