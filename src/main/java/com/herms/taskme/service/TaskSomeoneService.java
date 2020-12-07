package com.herms.taskme.service;

import com.herms.taskme.dto.MediaForCreationDTO;
import com.herms.taskme.dto.MessageDTO;
import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.enums.FrequencyEnum;
import com.herms.taskme.enums.TaskState;
import com.herms.taskme.model.Media;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.TaskSomeoneRepository;
import com.herms.taskme.util.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskSomeoneService {
    @Autowired
    private TaskSomeoneRepository taskSomeoneRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private TaskApplicationService taskApplicationService;
    @Autowired
    private StateService stateService;
    @Autowired
    private MessageService messageService;

    public TaskSomeoneService() {
    }

    public List<TaskSomeone> getAllTaskSomeone(){
        List<TaskSomeone> taskSomeoneList = new ArrayList<>();
        taskSomeoneRepository.findAllByOrderByCreatedOnDesc().forEach(taskSomeoneList::add);
        return taskSomeoneList;
    }

    public List<TaskSomeone> listPreviousTasks(Long userId){
		List<TaskSomeone> taskSomeoneList = new ArrayList<>();
        taskSomeoneRepository.findAllPreviousTasksFromUserIdOrderByCreatedOnDesc(userId).forEach(taskSomeoneList::add);
        return taskSomeoneList;
    }
    
    public Page<TaskSomeone> getAllTaskSomeonePaginated(Pageable pageable, String searchTerm){
        return taskSomeoneRepository.findByTitleContainingIgnoreCaseAndStateAndParentTaskIsNullOrderByCreatedOnDesc(pageable, searchTerm, TaskState.APPLICATIONS_OPEN);
    }

    public TaskSomeone getTaskSomeone(Long id){
        return taskSomeoneRepository.findById(id).get();
    }

    public Long getNumberOfTasksCreatedByUserId(Long userId) {
        return taskSomeoneRepository.findNumberOfTasksCreatedByuser(userId);
    }

    public Page<TaskSomeone> getAllTaskSomeoneCreatedByUserIdPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseAndParentTaskIsNullOrderByCreatedOnDesc(pageable, userId, term);
    }

    public Page<TaskSomeone> getAllTasksThatUserIsParticipatingPaginated(Pageable pageable, Long userId, String term) {
        return taskSomeoneRepository.findAllTasksThatUserIsParticipatingPaginated(pageable, userId, term);
    }

    private Page<TaskSomeone> getAllUserTaskApplicationsPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseAndParentTaskIsNullOrderByCreatedOnDesc(pageable, userId, term);
    }

    public Page<TaskSomeone> findAllSubTasksPaginated(Pageable pageable, Long id) {
        return taskSomeoneRepository.findAllByParentTask_IdOrderByEndDateAsc(pageable, id);
    }

    public Page<TaskSomeone> getAllSubTasksThatUserIsParticipatingPaginated(Pageable pageable, Long userId, String term, Date fromDate, Date toDate){
        return taskSomeoneRepository.findAllSubTasksThatUserIsParticipatingInAPeriodPaginated(pageable, userId, term, fromDate, toDate);
    }

    public TaskSomeone addTaskSomeone(TaskSomeone taskSomeone){
        taskSomeone.setCreatedOn(new Date());
        taskSomeone.setState(TaskState.APPLICATIONS_OPEN);
        taskSomeone.setUser(customUserDetailsService.getLoggedUser());
        return taskSomeoneRepository.save(taskSomeone);
    }

    public TaskSomeone updateTaskSomeone(Long id, TaskSomeone taskSomeone) {
    	return updateTaskSomeone(id, taskSomeone, null);
    }

    public TaskSomeone updateTaskSomeone(Long id, TaskSomeone taskSomeone, TaskSomeone originalTaskSomeone) {
    	TaskSomeone original = originalTaskSomeone;
    	if(original == null) {
    		original = getTaskSomeone(taskSomeone.getId());
    	}

    	List<TaskSomeone> subtasks = original.getSubTasksList();
    	TaskSomeone parent = original.getParentTask();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(taskSomeone, original);

        original.setSubTasksList(subtasks);
        original.setParentTask(parent);
        return taskSomeoneRepository.save(original);
    }

    public void deleteTaskSomeone(Long id){
        taskSomeoneRepository.deleteById(id);
    }

    public TaskSomeone addMediaToTaskSomeone(MultipartFile[] multipartFiles, TaskSomeone taskSomeone) throws Exception {
        for(MultipartFile file : multipartFiles) {
            MediaForCreationDTO media = new MediaForCreationDTO();
            media.setFileByteArray(file.getBytes());
            media.setCreatedOn(new Date());
            media.setDescription("TaskSomeone media");
            media.setType(Media.MEDIA_TYPE_IMG);
            media.setTaskSomeone(taskSomeone);
            taskSomeone.getMediaList().add(mediaService.addMedia(media));
        }
        updateTaskSomeone(taskSomeone.getId(), taskSomeone);
        return taskSomeone;
    }

    public TaskSomeone removeMediasFromTaskSomeone(TaskSomeone taskSomeone, List<Long> toBeDeletedMediaIdList) throws IOException, InterruptedException {
        for(Long mediaId : toBeDeletedMediaIdList){
            taskSomeone.getMediaList().removeIf(x -> mediaId.equals(x.getId()));
        }
        System.out.println("After remove media from tasksomeone");
        mediaService.deleteMediaIdListAsync(toBeDeletedMediaIdList);
        System.out.println("After call delete Media List");
        return taskSomeoneRepository.save(taskSomeone);
    }

	public TaskSomeone openApplications(TaskSomeone taskSomeone) {
        taskSomeone.getParticipants().clear();
        taskSomeone.setState(TaskState.APPLICATIONS_OPEN);
        return taskSomeoneRepository.save(taskSomeone);
    }

    public TaskSomeone changeTaskToNextState(TaskSomeone taskSomeone) throws Exception {
        TaskState nextState = stateService.getNextTaskState(taskSomeone);
        if(nextState != null) {
            stateService.validateTaskStateChange(taskSomeone, nextState);
            taskSomeone.setState(nextState);
            stateService.executeAfterStateSetProcedures(taskSomeone, nextState);
        }
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public TaskSomeone changeTaskToPreviousState(TaskSomeone taskSomeone) throws Exception {
        TaskState previousState = stateService.getPreviousTaskState(taskSomeone);
        if(previousState != null) {
            stateService.validateTaskStateChange(taskSomeone, previousState);
            taskSomeone.setState(previousState);
            stateService.executeAfterStateSetProcedures(taskSomeone, previousState);
        }
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public TaskSomeone changeTaskToCancelled(TaskSomeone taskSomeone) {
        taskSomeone.setState(TaskState.CANCELLED);
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public void generateOrDeleteSubTasks(TaskSomeone taskSomeone) {
        if(!taskSomeone.getSubTasksList().isEmpty()){
            removeNecessaryOldOrFutureSubTasksForTask(taskSomeone);
        }
        generateSubTasksForTask(taskSomeone);
    }
    private void generateSubTasksForTask(TaskSomeone taskSomeone) {
        Date taskDate = taskSomeone.getStartDate();
        List<Date> existingSubTasksDate = taskSomeone.getSubTasksList().stream()
                                                                        .map(TaskSomeone::getEndDate)
                                                                        .collect(Collectors.toList());
        while(taskDate.before(taskSomeone.getEndDate())) {
            //if there is already a subtask of this task in this date, we don't create.
            if(existingSubTasksDate.contains(taskDate)){
                taskDate = DateUtils.nextDateAccordingToFrequency(taskDate, taskSomeone.getFrequency());
                continue;
            }
            TaskSomeone subTask = new TaskSomeone();
            subTask.setEndDate(taskDate);
            subTask.setDescription(taskSomeone.getDescription());
            subTask.setCreatedOn(new Date());
            subTask.setTitle(taskSomeone.getTitle());
            subTask.setLocation(taskSomeone.getLocation());
            subTask.setUser(taskSomeone.getUser());
//            subTask.setMediaList();
            subTask.setState(TaskState.CREATED);
            subTask.setParentTask(taskSomeone);
            taskSomeone.getSubTasksList().add(subTask);
            System.out.println("Generating subtask: " + subTask.getEndDate());
            taskDate = DateUtils.nextDateAccordingToFrequency(taskDate, taskSomeone.getFrequency()) ;
        }
    }

    private void removeNecessaryOldOrFutureSubTasksForTask(TaskSomeone taskSomeone) {
        List<TaskSomeone> toBeRemovedTasks = new ArrayList<>();

        for(TaskSomeone subtask : taskSomeone.getSubTasksList()){
            boolean beforeStartDateTask = subtask.getEndDate().before(taskSomeone.getStartDate());
            boolean beforeTodayTask = subtask.getEndDate().before(new Date());
            //if the subtask was before the parent task start date or before today, and its on the DONE state,
            // we DONT remove, because it means that the task was touched, and maybe there is important
            //information there. But the user can delete if those old subtasks if he wants to
            if( (beforeStartDateTask || beforeTodayTask) && subtask.getState().equals(TaskState.DONE) ) {
                continue;
            }
            subtask.setParentTask(null);
            toBeRemovedTasks.add(subtask);
        }
        taskSomeone.getSubTasksList().removeAll(toBeRemovedTasks);
    }


    public void notifyPartificpantsThatTaskStarted(TaskSomeone taskSomeone) {
        MessageDTO message = new MessageDTO();
        message.setUserSenderId(taskSomeone.getUser().getId());
        message.setSentTime(new Date());
        for(User participant : taskSomeone.getParticipants()) {
            message.setContent(String.format("Hello %s! I'd like to inform you that the task '%s' has started.",
                    participant.getGivenName(), taskSomeone.getTitle()));
            messageService.sendMessageTo(participant.getId(), message);
        }
    }

    public void validateTaskConclusion(TaskSomeone taskSomeone) throws Exception {
        if(taskSomeone.isPeriodic()) {
            List<TaskSomeone> notDoneSubTask = taskSomeone.getSubTasksList().stream()
                    .filter(st -> st.getState().equals(TaskState.CREATED))
                    .collect(Collectors.toList());
            if(!notDoneSubTask.isEmpty()) {
                throw new Exception("There is unfinished subtasks! In order to conclude this task, all the subtasks must be DONE or CANCELLED");
            }
        }
    }
}
