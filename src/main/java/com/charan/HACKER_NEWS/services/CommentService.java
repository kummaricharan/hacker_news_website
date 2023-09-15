package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.Comment;
import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentService {

    void save(Comment comment);
    List<Comment> findAllByParentStory(Story story);
    Comment findById(Long id);
    void deleteById(Long id);
    void deleteByCommentIdDownVote(Long commentId);
    void deleteByCommentIdUpVote( Long commentId);
}
