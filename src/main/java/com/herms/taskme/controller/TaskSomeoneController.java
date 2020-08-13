package com.herms.taskme.controller;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.*;
import com.herms.taskme.converter.TaskSomeoneConverter;
import com.herms.taskme.dto.TaskSomeoneDetailsDTO;
import com.herms.taskme.dto.TaskSomeoneForListDTO;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.TaskSomeoneService;
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
public class TaskSomeoneController {
    @Autowired
    private TaskSomeoneService taskSomeoneService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TaskSomeoneConverter taskSomeoneConverter;

    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeone")
    public List<TaskSomeone> getAllTaskSomeone(){
        return taskSomeoneService.getAllTaskSomeone();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeona")
    public ResponseEntity<Page<TaskSomeone>> getTaskSomeonePages(
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) {

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskSomeone> pontoRotaList = taskSomeoneService.getAllTaskSomeonePaginated(pageRequest, searchTerm);

        return ResponseEntity.ok(pontoRotaList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/listtasks")
    public List<TaskSomeoneForListDTO> listTaskSomeone(){
        return taskSomeoneService.getAllTaskSomeone().stream().map(taskSomeone -> new TaskSomeoneForListDTO(taskSomeone)).collect(Collectors.toList());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeone/previousTasks/{userId}")
    public List<TaskSomeoneForListDTO> listPreviousTasks(@PathVariable Long userId){
        return taskSomeoneService.listPreviousTasks(userId).stream().map(taskSomeone -> new TaskSomeoneForListDTO(taskSomeone)).collect(Collectors.toList());
    }

    @RequestMapping("/tasksomeone/{id}")
    public ResponseEntity<TaskSomeoneDetailsDTO> getTaskSomeone(@PathVariable Long id) throws Exception{
    	return new ResponseEntity<>(taskSomeoneConverter.toDTO(taskSomeoneService.getTaskSomeone(id), TaskSomeoneDetailsDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasksomeone")
    public ResponseEntity<TaskSomeoneForListDTO> addTaskSomeone(@RequestBody TaskSomeone taskSomeone){
        return new ResponseEntity<>(new TaskSomeoneForListDTO(taskSomeoneService.addTaskSomeone(taskSomeone)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/tasksomeone/{id}")
    public ResponseEntity<TaskSomeoneDetailsDTO> updateTaskSomeone(@RequestBody TaskSomeone taskSomeone, @PathVariable Long id) throws Exception{
    	User principal = customUserDetailsService.getLoggedUser();
        TaskSomeone originalTaskSomeone = taskSomeoneService.getTaskSomeone(id);
        
        if(originalTaskSomeone == null || !originalTaskSomeone.getUser().equals(principal)){
            throw new Exception("You don't have access to this function");
        }
        
    	TaskSomeoneDetailsDTO dto = taskSomeoneConverter.toDTO(
    										taskSomeoneService.updateTaskSomeone(id, taskSomeone, originalTaskSomeone)
    										, TaskSomeoneDetailsDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tasksomeone/{id}")
    public void deleteTaskSomeone(@PathVariable Long id){
        taskSomeoneService.deleteTaskSomeone(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasksomeone/{id}/addMedia")
    public ResponseEntity<TaskSomeone> addMediaToTaskSomeone(@RequestParam("file") MultipartFile[] multipartFiles, @PathVariable Long id) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        TaskSomeone taskSomeone = taskSomeoneService.getTaskSomeone(id);
        if(taskSomeone == null || !taskSomeone.getUser().equals(principal)){
            throw new Exception("You don't have access to this function");
        }


        if(multipartFiles == null || multipartFiles.length == 0){
            throw new Exception("Invalid file size");
        }

        //if everything went fine, we can add media to the taskSomeone
        taskSomeone = taskSomeoneService.addMediaToTaskSomeone(multipartFiles, taskSomeone);
        return new ResponseEntity<>(taskSomeone, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeone/createdTasks")
    public ResponseEntity<Page<TaskSomeoneDetailsDTO>> getCurrentUserCreatedTasks(
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) throws Exception {
        User principal = customUserDetailsService.getLoggedUser();

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskSomeone> taskSomeoneCreatedBy = taskSomeoneService.getAllTaskSomeoneCreatedByUserIdPaginated(pageRequest, principal.getId(), searchTerm);
        Page<TaskSomeoneDetailsDTO> taskSomeoneForListDTOs = taskSomeoneCreatedBy.map((taskSomeone) -> taskSomeoneConverter.toDTO(taskSomeone, TaskSomeoneDetailsDTO.class));
        return new ResponseEntity<>(taskSomeoneForListDTOs, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeone/createdTasks/{userid}")
    public ResponseEntity<Page<TaskSomeone>> getCreatedTasksPaginated(@PathVariable Long userid,
                                                                       @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
                                                                       @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
                                                                       @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
                                                                       @RequestParam(value="direction", defaultValue = "DESC") String direction,
                                                                       @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        if(userid == null || !userid.equals(principal.getId())){
            throw new Exception("You don't have access to this function");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskSomeone> taskSomeoneCreatedBy = taskSomeoneService.getAllTaskSomeonePaginated(pageRequest, searchTerm);

        return new ResponseEntity<>(taskSomeoneCreatedBy, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tasksomeone/{taskId}/removeMedias/{toBeDeletedeMediaIdList}")
    public ResponseEntity<TaskSomeoneForListDTO> removeMediaFromTaskSomeone(@PathVariable Long taskId, @PathVariable List<Long> toBeDeletedeMediaIdList) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        TaskSomeone taskSomeone = taskSomeoneService.getTaskSomeone(taskId);

        if(taskSomeone.getUser() == null || !taskSomeone.getUser().getId().equals(principal.getId())){
            throw new Exception("You don't have access to this function");
        }

        taskSomeone = taskSomeoneService.removeMediasFromTaskSomeone(taskSomeone, toBeDeletedeMediaIdList);
        return new ResponseEntity<>(new TaskSomeoneForListDTO(taskSomeone), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasksomeone/applications")
    public ResponseEntity<Page<TaskSomeoneForListDTO>> getCurrentUserTaskApplications(
            @RequestParam(value="page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value="linesPerPage", defaultValue = "4") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC") String direction,
            @RequestParam(value="searchTerm", defaultValue = "") String searchTerm) throws Exception {
        User principal = customUserDetailsService.getLoggedUser();

        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<TaskSomeone> taskSomeoneCreatedBy = taskSomeoneService.getAllTaskSomeoneCreatedByUserIdPaginated(pageRequest, principal.getId(), searchTerm);

        Page<TaskSomeoneForListDTO> taskSomeoneForListDTOs = taskSomeoneCreatedBy.map(TaskSomeoneForListDTO::new);
        return new ResponseEntity<>(taskSomeoneForListDTOs, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/tasksomeone/terminateApplications")
    public ResponseEntity<TaskSomeoneDetailsDTO> terminateApplications(@RequestBody TaskSomeoneForListDTO taskSomeone){

        return new ResponseEntity<>(taskSomeoneConverter.toDTO(taskSomeoneService.terminateApplications(taskSomeoneConverter.fromDTO(taskSomeone)), TaskSomeoneDetailsDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasksomeone/openApplications")
    public ResponseEntity<TaskSomeoneDetailsDTO> openApplications(@RequestBody TaskSomeoneForListDTO taskSomeone){

        return new ResponseEntity<>(taskSomeoneConverter.toDTO(taskSomeoneService.openApplications(taskSomeoneConverter.fromDTO(taskSomeone)), TaskSomeoneDetailsDTO.class), HttpStatus.OK);
    }
}
