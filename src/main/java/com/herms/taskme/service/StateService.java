package com.herms.taskme.service;

import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.enums.TaskState;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private TaskApplicationService taskApplicationService;

    public void validateTaskStateChange(TaskSomeone taskSomeone, TaskState newState) throws Exception {
        if(TaskState.APPLICATIONS_CLOSED.equals(newState)){
            if(taskSomeone.getParticipants().size() == 0){
                throw new Exception("You can't terminate applications because you have no applicants for this task");
            }

            List<TaskApplication> approvedApplicantions = taskApplicationService.getAllTaskApplicationByTaskIdAndStatus(taskSomeone.getId(), ApplicationStatus.ACCEPTED);
            if(approvedApplicantions.size() == 0){
                throw new Exception("You can't terminate applications because you have no approved applicants for this task");
            }
        }
    }
}
