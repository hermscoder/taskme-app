package com.herms.taskme.service;

import com.herms.taskme.dto.MediaForCreationDTO;
import com.herms.taskme.enums.ApplicationStatus;
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
        return taskSomeoneRepository.findByTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, searchTerm);
    }

    public TaskSomeone getTaskSomeone(Long id){
        return taskSomeoneRepository.findById(id).get();
    }

    public Page<TaskSomeone> getAllTaskSomeoneCreatedByUserIdPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, userId, term);
    }
    
    public Page<TaskSomeone> getAllUserTaskApplicationsPaginated(Pageable pageable, Long userId, String term){
        return taskSomeoneRepository.findAllByUser_IdAndTitleContainingIgnoreCaseOrderByCreatedOnDesc(pageable, userId, term);
    }

    public TaskSomeone addTaskSomeone(TaskSomeone taskSomeone){
        taskSomeone.setCreatedOn(new Date());
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
		return taskSomeoneRepository.save(taskSomeone);
	}

	public TaskSomeone openApplications(TaskSomeone taskSomeone) {
        taskSomeone.getParticipants().clear();
        return taskSomeoneRepository.save(taskSomeone);
    }
}
