package com.herms.taskme.controller;

import com.herms.taskme.dto.CommentDTO;
import com.herms.taskme.dto.TaskApplicationDetailsDTO;
import com.herms.taskme.dto.TaskSomeoneForListDTO;
import com.herms.taskme.model.Comment;
import com.herms.taskme.model.Media;
import com.herms.taskme.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logged")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.GET, value = "/comments")
    public ResponseEntity<Page<CommentDTO>> getAllCommentsFromTask( @RequestParam(value = "taskSomeoneId", required = true) Long taskSomeoneId,
                                                                   @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                                                   @RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
                                                                   @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                                                   @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        PageRequest pageRequest = PageRequest.of(pageNumber, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<Comment> commentsPage = commentService.getAllCommentsFromTask(pageRequest, taskSomeoneId);

        Page<CommentDTO> commentDTOPage = commentsPage.map(CommentDTO::new);

        return new ResponseEntity<>(commentDTOPage, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/comments")
    public ResponseEntity<CommentDTO> addCommentToTask(@RequestBody CommentDTO commentDTO) throws Exception {

        return new ResponseEntity<CommentDTO>(new CommentDTO(commentService.addComment(commentDTO)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) throws Exception {
        commentService.deleteComment(commentId);
    }
}
