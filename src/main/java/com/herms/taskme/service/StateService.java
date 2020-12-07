package com.herms.taskme.service;

import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.enums.TaskState;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateService {

    @Autowired
    private TaskApplicationService taskApplicationService;
    @Autowired
    private TaskSomeoneService taskSomeoneService;

    public void validateTaskStateChange(TaskSomeone taskSomeone, TaskState newState) throws Exception {
        if(taskSomeone.isSubTask()){
            if(!Arrays.asList(TaskState.CREATED, TaskState.STARTED, TaskState.DONE, TaskState.CANCELLED).contains(newState)){
                throw new Exception("Subtask can not have the state " + newState+ ". The allowed states are: CREATED, DONE and CANCELLED");
            }
        } else {
            if(TaskState.APPLICATIONS_CLOSED.equals(newState)){
                List<TaskApplication> approvedApplicantions = taskApplicationService.getAllTaskApplicationByTaskIdAndStatus(taskSomeone.getId(), ApplicationStatus.ACCEPTED);

                if(approvedApplicantions.size() == 0){
                    throw new Exception("You can't terminate applications because you have no approved applicants for this task");
                }
            }
        }
    }

    public void executeAfterStateSetProcedures(TaskSomeone taskSomeone, TaskState newState) {
        //if it's a parent task
        if(!taskSomeone.isSubTask()){
            if(TaskState.APPLICATIONS_OPEN.equals(newState)){
                taskSomeone.getParticipants().clear();
            } else if(TaskState.APPLICATIONS_CLOSED.equals(newState)){
                List<TaskApplication> approvedApplications = taskApplicationService.getAllTaskApplicationByTaskIdAndStatus(taskSomeone.getId(), ApplicationStatus.ACCEPTED);
                taskSomeone.setParticipants(approvedApplications.stream()
                        .map(TaskApplication::getUser)
                        .collect(Collectors.toList()));
            } else if (TaskState.STARTED.equals(newState)){
                if(taskSomeone.isPeriodic()){
                    taskSomeoneService.generateOrDeleteSubTasks(taskSomeone);
                }
                taskSomeoneService.notifyPartificpantsThatTaskStarted(taskSomeone);
            }
        }
    }
    public Integer getNextTaskStateCode(TaskSomeone taskSomeone) {
        TaskState nextTaskState = getNextTaskState(taskSomeone);
        if(nextTaskState != null) {
            return nextTaskState.getCode();
        }
        return null;
    }

    public TaskState getNextTaskState(TaskSomeone taskSomeone) {
        TaskState state = taskSomeone.getState();

        if (state == TaskState.CREATED) {
            if(taskSomeone.isSubTask()){
                return TaskState.STARTED;
            } else {
                return TaskState.APPLICATIONS_OPEN;
            }
        } else if (state == TaskState.APPLICATIONS_OPEN) {
            return  TaskState.APPLICATIONS_CLOSED;
        } else if (state == TaskState.APPLICATIONS_CLOSED) {
            return  TaskState.STARTED;
        } else if (state == TaskState.STARTED) {
            return  TaskState.DONE;
        }

        return null;
    }

    public Integer getPreviousTaskStateCode(TaskSomeone taskSomeone) {
        TaskState previousState = getPreviousTaskState(taskSomeone);
        if(previousState != null) {
            return previousState.getCode();
        }
        return null;
    }

    public TaskState getPreviousTaskState(TaskSomeone taskSomeone) {
        TaskState state = taskSomeone.getState();
        if (state == TaskState.APPLICATIONS_OPEN) {
            return TaskState.CREATED;
        } else if (state == TaskState.APPLICATIONS_CLOSED) {
            return TaskState.APPLICATIONS_OPEN;
        } else if (state == TaskState.STARTED) {
            if(taskSomeone.isSubTask()){
                return TaskState.CREATED;
            } else {
                return TaskState.APPLICATIONS_CLOSED;
            }
        } else if (state == TaskState.DONE) {
            return TaskState.STARTED;
        }
        return null;
    }
}
