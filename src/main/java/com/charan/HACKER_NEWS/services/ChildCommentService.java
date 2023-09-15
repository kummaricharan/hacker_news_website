package com.charan.HACKER_NEWS.services;


import com.charan.HACKER_NEWS.entity.ChildComment;

import java.util.List;

public interface ChildCommentService {
    List<ChildComment> findAll();
    ChildComment findById(Long id);
    void save(ChildComment childComment);
    void deleteById(Long id);
}
