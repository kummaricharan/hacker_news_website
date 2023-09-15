package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.ChildComment;
import com.charan.HACKER_NEWS.repository.ChildCommentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChildCommentServiceImpl implements ChildCommentService {
    private ChildCommentRepository childCommentRepository;

    public ChildCommentServiceImpl(ChildCommentRepository childCommentRepository) {
        this.childCommentRepository = childCommentRepository;
    }

    @Override
    public List<ChildComment> findAll() {
        return childCommentRepository.findAll();
    }

    @Override
    public ChildComment findById(Long id) {
        Optional<ChildComment> result = childCommentRepository.findById(id);
        ChildComment childComment = null;
        if(result.isPresent()){
            childComment = result.get();
        }
        else{
            throw new RuntimeException("Did not find storyId "+id);
        }
        return childComment;
    }

    @Override
    public void save(ChildComment childComment) {
        childCommentRepository.save(childComment);
    }

    @Override
    public void deleteById(Long id) {
        childCommentRepository.deleteById(id);
    }
}
