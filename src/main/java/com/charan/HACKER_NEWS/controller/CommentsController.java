package com.charan.HACKER_NEWS.controller;

import com.charan.HACKER_NEWS.entity.Comment;
import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import com.charan.HACKER_NEWS.services.CommentService;
import com.charan.HACKER_NEWS.services.StoryService;
import com.charan.HACKER_NEWS.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/comments")
public class CommentsController {
    private final CommentService commentService;
    private final StoryService storyService;
    private UserService userService;

    public CommentsController(CommentService commentService, StoryService storyService,UserService userService) {
        this.commentService = commentService;
        this.storyService = storyService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String goToCommentsPage(@RequestParam("storyId") Long storyId, Model model) {
        Story story = storyService.findById(storyId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        List<Comment> comments = commentService.findAllByParentStory(story);

        for (Comment comment : comments) {
            comment.setFormattedTime(comment.getSubmissionTime().getTime());
        }

        Comment comment = new Comment();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            user = userService.findByName(userDetails.getUsername());

        }
        comment.setAuthor(user);
        model.addAttribute("user",user);
        model.addAttribute("comment", comment);
        model.addAttribute("story", story);
        model.addAttribute("comments", comments);

        return "posts/comments_page";
    }

//    @PostMapping("/save")
//    public String saveComment(@RequestParam("postId") Long storyId, @ModelAttribute("newComment")  Comment comment, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "redirect:/comments/list?storyId=" + storyId;
//        }
//
//        Story story = storyService.findById(storyId);
//        comment.setParentStory(story);
//
//        comment.setSubmissionTime(new Timestamp(System.currentTimeMillis()));
//
//
//        commentService.save(comment);
//
//        return "redirect:/comments/list?storyId=" + storyId;
//    }
@PostMapping("/save")
public String saveComment(@RequestParam("postId") Long storyId,
                          @ModelAttribute("newComment") Comment comment) {

    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
    comment.setSubmissionTime(currentTimestamp);

    Story story = storyService.findById(storyId);
    comment.setParentStory(story);
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;

    if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;

        user = userService.findByName(userDetails.getUsername());

    }
    comment.setAuthor(user);
    commentService.save(comment);

    return "redirect:/comments/list?storyId=" + storyId;
}

    @PostMapping("/saveChild")
    public String saveChildComment(
            @RequestParam("storyId") Long storyId,
            @RequestParam("commentId") Long parentCommentId,
            @ModelAttribute("newComment") Comment comment) {

        Comment parentComment = commentService.findById(parentCommentId);

        if (parentComment != null) {
            Timestamp currentTimestamp = new Timestamp(new Date().getTime());

            comment.setSubmissionTime(currentTimestamp);

            comment.setParentComment(parentComment);

            comment.setFormattedTime(comment.getSubmissionTime().getTime());

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = null;

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                user = userService.findByName(userDetails.getUsername());

            }
            comment.setAuthor(user);

            parentComment.getReplies().add(comment);

            commentService.save(comment);
            commentService.save(parentComment);

            return "redirect:/comments/list?storyId=" + storyId;
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/Comment")
    public String Comment( @RequestParam("storyId") Long storyId,@RequestParam("commentId") Long commentId, Model model) {
        Comment comment = commentService.findById(commentId);
        Story story = storyService.findById(storyId);
        model.addAttribute("comment", comment);
        model.addAttribute("storyId", storyId);
        model.addAttribute("story",story);
        return "posts/child_comments";
    }
    @GetMapping("/GrandchildComment")
    public String displayGrandchildComment(@RequestParam("commentId") Long commentId,
                                           @RequestParam("storyId") Long storyId,
                                           Model model) {
        Comment grandchildComment = commentService.findById(commentId);
        grandchildComment.setFormattedTime(grandchildComment.getSubmissionTime().getTime());
        model.addAttribute("comment", grandchildComment);
        model.addAttribute("storyId", storyId);
        return "posts/child_comments";
    }
    @GetMapping("/replyOnComment/{commentId}")
    public String replyComment(@PathVariable Long commentId, @RequestParam Integer postId, Model model) {
        Comment comment = commentService.findById(commentId);
        System.out.println(commentId);
        Comment replyComment = new Comment();
        model.addAttribute("comment", comment);
        model.addAttribute("replyComment", replyComment);

        return "reply_comment";
    }

    @PostMapping("/replyOnComment/{commentId}")
    public String saveReplyComment(@PathVariable Long commentId, @ModelAttribute("replyComment") Comment replyComment) {
        Comment parentComment = commentService.findById(commentId);

        if (parentComment != null) {
            Comment newReplyComment = new Comment();
            newReplyComment.setText(replyComment.getText());
            newReplyComment.setParentComment(parentComment);
            newReplyComment.setParentStory(parentComment.getParentStory());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByName(authentication.getName());
            newReplyComment.setAuthor(user);

            commentService.save(newReplyComment);
        }

        //return "redirect:/" + parentComment.getPost().getPostId();
        return "redirect:/" + parentComment.getParentStory().getId();
    }
    @PostMapping("/delete")
    public String delete(@RequestParam("storyId") Long storyId,@RequestParam("commentId") Long commentId) {
        commentService.deleteByCommentIdDownVote(commentId);
        commentService.deleteByCommentIdUpVote(commentId);
        commentService.deleteById(commentId);
        return "redirect:/comments/list?storyId=" + storyId;
    }
    @PostMapping("/{id}/upvote")
    public String upvoteStory(@PathVariable("id") Long commentId,@RequestParam("storyId") Long storyId, HttpSession session,Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(userDetails.getUsername());

        if (user != null) {
            Comment comment = commentService.findById(commentId);

            if (comment != null && !user.hasUpvotedComment(comment)) {
                user.upvoteComment(comment);

                comment.setScore(comment.getScore() + 1);

                userService.save(user);
                commentService.save(comment);

                Set<Long> upvotedComments = (Set<Long>) session.getAttribute("upvotedComments");
                if (upvotedComments == null) {
                    upvotedComments = new HashSet<>();
                    session.setAttribute("upvotedComments", upvotedComments);
                }
                upvotedComments.add(commentId);
                Set<Long> downvotedStories = (Set<Long>) session.getAttribute("downvotedStories");
                if (downvotedStories != null && downvotedStories.contains(commentId)) {
                    downvotedStories.remove(commentId);
                }
            }
        }

        model.addAttribute("storyId",storyId);
        return "redirect:/comments/list?storyId=" + storyId;
    }

    @PostMapping("/{id}/downvote")
    public String downvoteStory(@PathVariable("id") Long commentId,@RequestParam("storyId") Long storyId, HttpSession session,Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(userDetails.getUsername());

        if (user != null) {
            Comment comment = commentService.findById(commentId);

            if (comment!= null && !user.hasDownvotedComment(comment)) {
                if (user.hasUpvotedComment(comment)) {
                    user.removeUpvoteComment(comment);
                    comment.setScore(comment.getScore() - 1);

                }

                user.downvoteComment(comment);
                userService.save(user);
                commentService.save(comment);

                // Add the story ID to the set of downvoted stories in the session
                Set<Long> downvotedComments = (Set<Long>) session.getAttribute("downvotedComments");
                if (downvotedComments == null) {
                    downvotedComments= new HashSet<>();
                    session.setAttribute("downvotedComments", downvotedComments);
                }
                downvotedComments.add(commentId);

                // Remove the story ID from the set of upvoted stories in the session
                Set<Long> upvotedComments = (Set<Long>) session.getAttribute("upvotedComments");
                if (upvotedComments != null) {
                    upvotedComments.remove(commentId);
                }
            }
        }
        model.addAttribute("storyId",storyId);
        return "redirect:/comments/list?storyId=" + storyId;
    }
    @RequestMapping("/favorite")
    public String favorite(@RequestParam("storyId") Long storyId,Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Story story = storyService.findById(storyId);
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userService.findByName(userDetails.getUsername());
            user.getFavoriteStories().add(story);
            userService.save(user);
            model.addAttribute("user",user);
        }
        return "redirect:/comments/list?storyId=" + storyId;
    }
    @RequestMapping("/unfavorite")
    public String unfavorite(@RequestParam("storyId") Long storyId,Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Story story = storyService.findById(storyId);
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userService.findByName(userDetails.getUsername());
            user.getFavoriteStories().remove(story);
            userService.save(user);
            model.addAttribute("user",user);
        }
        return "redirect:/comments/list?storyId=" + storyId;
    }

}
