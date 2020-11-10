package com.herms.taskme.service;

import com.herms.taskme.dto.MediaForCreationDTO;
import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.enums.TaskState;
import com.herms.taskme.model.Media;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.repository.TaskSomeoneRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        return taskSomeoneRepository.findByTitleContainingIgnoreCaseAndStateOrderByCreatedOnDesc(pageable, searchTerm, TaskState.APPLICATIONS_OPEN);
    }

    public TaskSomeone getTaskSomeone(Long id){
        return taskSomeoneRepository.findById(id).get();
    }

    public Page<TaskSomeone> getAllTaskSomeoneCreatedByUserIdPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, userId, term);
    }

    public Page<TaskSomeone> getAllTasksThatUserIsParticipatingPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllTasksThatUserIsParticipatingPaginated(pageable, userId, term);
    }

    public Page<TaskSomeone> getAllUserTaskApplicationsPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, userId, term);
    }

    public TaskSomeone addTaskSomeone(TaskSomeone taskSomeone){
        taskSomeone.setCreatedOn(new Date());
        taskSomeone.setState(TaskState.CREATED);
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

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(taskSomeone, original);
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

	public TaskSomeone terminateApplications(TaskSomeone taskSomeone) {
		List<TaskApplication> approvedApplications = taskApplicationService.getAllTaskApplicationByTaskIdAndStatus(taskSomeone.getId(), ApplicationStatus.ACCEPTED);
        taskSomeone.setParticipants(approvedApplications.stream()
                                        .map(TaskApplication::getUser)
                                        .collect(Collectors.toList()));
        taskSomeone.setState(TaskState.APPLICATIONS_CLOSED);
		return taskSomeoneRepository.save(taskSomeone);
	}

	public TaskSomeone openApplications(TaskSomeone taskSomeone) {
        taskSomeone.getParticipants().clear();
        taskSomeone.setState(TaskState.APPLICATIONS_OPEN);
        return taskSomeoneRepository.save(taskSomeone);
    }

    public TaskSomeone changeTaskToNextState(TaskSomeone taskSomeone) throws Exception {
        TaskState nextState = getNextTaskState(taskSomeone.getState());
        if(nextState != null) {
            stateService.validateTaskStateChange(taskSomeone, nextState);
            taskSomeone.setState(nextState);
        }
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public TaskSomeone changeTaskToPreviousState(TaskSomeone taskSomeone) throws Exception {
        TaskState previousState = getPreviousTaskState(taskSomeone.getState());
        if(previousState != null) {
            stateService.validateTaskStateChange(taskSomeone, previousState);
            taskSomeone.setState(previousState);
        }
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public TaskSomeone changeTaskToCancelled(TaskSomeone taskSomeone) {
        taskSomeone.setState(TaskState.CANCELLED);
        taskSomeoneRepository.save(taskSomeone);
        return taskSomeone;
    }

    public Integer getNextTaskState(int stateCode) {
        TaskState nextTaskState = getNextTaskState(TaskState.toEnum(stateCode));
        if(nextTaskState != null) {
            return nextTaskState.getCode();
        }
        return null;
    }

    public TaskState getNextTaskState(TaskState state) {
        if (state == TaskState.CREATED) {
            return TaskState.APPLICATIONS_OPEN;
        } else if (state == TaskState.APPLICATIONS_OPEN) {
            return  TaskState.APPLICATIONS_CLOSED;
        } else if (state == TaskState.APPLICATIONS_CLOSED) {
            return  TaskState.STARTED;
        } else if (state == TaskState.STARTED) {
            return  TaskState.DONE;
        }
        return null;
    }

    public Integer getPreviousTaskState(int stateCode) {
        TaskState previousState = getPreviousTaskState(TaskState.toEnum(stateCode));
        if(previousState != null) {
            return previousState.getCode();
        }
        return null;
    }

    public TaskState getPreviousTaskState(TaskState state) {
        if (state == TaskState.APPLICATIONS_OPEN) {
            return TaskState.CREATED;
        } else if (state == TaskState.APPLICATIONS_CLOSED) {
            return TaskState.APPLICATIONS_OPEN;
        } else if (state == TaskState.STARTED) {
            return TaskState.APPLICATIONS_CLOSED;
        } else if (state == TaskState.DONE) {
            return TaskState.STARTED;
        }
        return null;
    }
}
