package com.charan.HACKER_NEWS.repository;

import com.charan.HACKER_NEWS.entity.ChildComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildCommentRepository extends JpaRepository<ChildComment,Integer> {

    void deleteById(Long id);

    Optional<ChildComment> findById(Long id);
}
