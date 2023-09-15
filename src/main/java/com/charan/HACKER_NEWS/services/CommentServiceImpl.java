package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.Comment;
import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import com.charan.HACKER_NEWS.repository.CommentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    public List<Comment> findAllByParentStory(Story story){
        return commentRepository.findAllByParentStory(story);
    }
    @Override
    public Comment findById(Long id) {
        Optional<Comment> result = Optional.ofNullable(commentRepository.findById(id));
        Comment comment = null;
        if(result.isPresent()){
            comment = result.get();
        }
        else{
            throw new RuntimeException("Did not find storyId "+id);
        }
        return comment;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Comment comment = commentRepository.findById(id);

        List<User> users = commentRepository.findUsersByUpvotedCommentsContains(comment);
        for (User user : users) {
            user.removeUpvoteComment(comment);
        }

        commentRepository.delete(comment);
    }

    @Override
    public void deleteByCommentIdDownVote(Long commentId) {
        commentRepository.deleteByCommentIdDownVote(commentId);
    }

    @Override
    public void deleteByCommentIdUpVote(Long commentId) {
        commentRepository.deleteByCommentIdUpVote(commentId);
    }
}
