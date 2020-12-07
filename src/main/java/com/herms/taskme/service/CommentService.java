package com.herms.taskme.service;

import com.herms.taskme.dto.CommentDTO;
import com.herms.taskme.model.Comment;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskSomeoneService taskSomeoneService;

    public CommentService() {
    }

    public Page<Comment> getAllCommentsFromTask(Pageable pageable, Long tasksomeoneId) {
        return commentRepository.findAllByTaskSomeoneIdOrderBySentTimeDesc(pageable, tasksomeoneId);
    }

    public Comment addComment(CommentDTO commentDTO) throws Exception {
        User principal = customUserDetailsService.getLoggedUser();
        Comment entity = new Comment();
        entity.setContent(commentDTO.getContent());
        User user = userService.getUser(principal.getId());
        entity.setUserSender(user);
        TaskSomeone task = taskSomeoneService.getTaskSomeone(commentDTO.getTaskSomeoneId());
        entity.setTaskSomeone(task);
        entity.setSentTime(commentDTO.getSentTime());
        task.getCommentList().add(entity);

        boolean isRelatedToSubtask = task.isSubTask() && (task.getParentTask().getParticipants().contains(user)
                                    || task.getUser().getId().equals(user.getId()));
        if(!isRelatedToSubtask){
            throw new Exception("You can not add comments to a task that is not related to you!");
        }
        return commentRepository.save(entity);
    }

    public void deleteComment(Long commentId) throws Exception {
        User currentUser = customUserDetailsService.getLoggedUser();
        Comment comment = commentRepository.findById(commentId).orElse(null);
        //if the user is trying to delete other peoples comments without being the creator of the task
        //we will throw an exception
        boolean isRelatedToSubtask = comment.getTaskSomeone().isSubTask() && (comment.getTaskSomeone().getParentTask().getParticipants().contains(currentUser)
                || comment.getTaskSomeone().getUser().getId().equals(currentUser.getId()));
        if(!isRelatedToSubtask){
            throw new Exception("You must be the task creator in order to delete other people's comments");
        }
        commentRepository.deleteById(commentId);
    }
}
