package com.charan.HACKER_NEWS.repository;

import com.charan.HACKER_NEWS.entity.Comment;
import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.beans.JavaBean;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT c FROM Comment c WHERE c.parentStory = :story")
    List<Comment> findAllByParentStory(Story story);

    Comment findById(Long id);

    void deleteById(Long id);
    @Query("SELECT DISTINCT u FROM User u WHERE :comment IN (SELECT c FROM User u2 JOIN u2.upvotedComments c WHERE u2 = u)")
    List<User> findUsersByUpvotedCommentsContains(@Param("comment") Comment comment);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_downvoted_comments WHERE comment_id = :commentId", nativeQuery = true)
    void deleteByCommentIdDownVote(@Param("commentId") Long commentId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_upvoted_comments WHERE comment_id = :commentId", nativeQuery = true)
    void deleteByCommentIdUpVote(@Param("commentId") Long commentId);

}
