//package com.charan.HACKER_NEWS.controller;
//
//import com.charan.HACKER_NEWS.entity.ChildComment;
//import com.charan.HACKER_NEWS.entity.Comment;
//import com.charan.HACKER_NEWS.services.ChildCommentService;
//import com.charan.HACKER_NEWS.services.CommentService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.sql.Timestamp;
//import java.util.*;
//
//@Controller
//@RequestMapping("/childcomments")
//public class ChildCommentController {
//    private ChildCommentService childCommentService;
//    private CommentService commentService;
//
//    public ChildCommentController(ChildCommentService childCommentService, CommentService commentService) {
//        this.childCommentService = childCommentService;
//        this.commentService = commentService;
//    }
//
//    @PostMapping("/saveChildComment")
//    public String saveChildComment(@ModelAttribute("childComment") ChildComment childComment, @RequestParam("parentCommentId") Long parentCommentId) {
//        Comment parentComment = commentService.findById(parentCommentId);
//
//        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
//        childComment.setSubmissionTime(currentTimestamp);
//
//        childComment.setParentComment(parentComment);
//
//        childCommentService.save(childComment);
//
//        parentComment.getReplies().add(childComment);
//
//        // Save the parentComment after adding the childComment to its replies
//        commentService.save(parentComment);
//
//        // You can set formattedTime for the childComment here
//        childComment.setFormattedTime(childComment.getSubmissionTime().getTime());
//        childCommentService.save(childComment);
//
//        return "redirect:/comments?storyId=" + parentComment.getParentStory().getId();
//    }
//
//}
