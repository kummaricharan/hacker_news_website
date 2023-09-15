package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.*;

public interface StoryService {

    List<Story> findAll();
    Story findById(Long id);
    void save(Story story);
    void deleteById(Long id);
    Page<Story> findAll(Pageable pageable);
    Page<Story> findAllStories(Pageable pageable);

    Page<Story> findByAsk(String ask ,Pageable pageable);
    Page<Story> findByShow(String show ,Pageable pageable);
    Page<Story> findByJobs(String Job ,Pageable pageable);
    Page<Story> findByNew(String news, Pageable pageable);
//    Page<Story> searchByKeyword( String keyword, Pageable pageable);
//    Page<Story> filterByType(String type,Pageable pageable);
//    Page<Story> filterByTimeRange(Timestamp fromDate, @Param("toDate") Timestamp toDate, Pageable pageable);

      Page<Story> searchByKeywordAndTimeRange (String keyword, Timestamp fromDate, Timestamp toDate, Pageable pageable);
    Page<Story> findBySubmissionDate( Date date, Pageable pageable);
    List<Story> findStoriesByUserUsername(String username);
    Page<Story> findStoriesByUser(String username,Pageable pageable);
    Page<Story> findUpvotedStories(String username,Pageable pageable);
    Page<Story> findStoriesByFavorite(String username,Pageable pageable);
    Page<Story> findByPost(Pageable pageable);
    void removeCommentReferencesByStoryIdDownvote(@Param("story") Story story);
    void removeCommentReferencesByStoryIdUpvote(@Param("story") Story story);

}
