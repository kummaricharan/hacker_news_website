package com.charan.HACKER_NEWS.repository;

import com.charan.HACKER_NEWS.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.*;

public interface StoryRepository extends JpaRepository<Story,Integer> {
    Optional<Story> findById(Long id);

    @Query("SELECT s FROM Story s ORDER BY s.submissionTime DESC")
    Page<Story> findAll(Pageable pageable);

    @Query("SELECT s FROM Story s")
    Page<Story> findAllStories(Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.category =:category")
    Page<Story> findByAsk(@Param("category") String ask, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.category =:category")
    Page<Story> findByShow(@Param("category") String show, Pageable pageable);
    @Query("SELECT s FROM Story s ORDER BY s.submissionTime DESC")
    Page<Story> findByNew(@Param("news") String news, Pageable pageable);
    @Query("SELECT s FROM Story s ORDER BY s.submissionTime ASC")
    Page<Story> findByPost(Pageable pageable);



    @Query("SELECT s FROM Story s WHERE s.category =:category")
    Page<Story> findByJobs(@Param("category") String Jobs, Pageable pageable);

//    @Query("SELECT s FROM Story s WHERE s.type = :type")
//    Page<Story> filterByType(@Param("type") String type, Pageable pageable);
//
//
//    @Query("SELECT s FROM Story s WHERE s.timestamp BETWEEN :fromDate AND :toDate")
//    Page<Story> filterByTimeRange(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate, Pageable pageable);
//
//    @Query("SELECT s FROM Story s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.url) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<Story> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT s FROM Story s " +
            "WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.url) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.author.username) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "AND (:isDate = 0 OR s.submissionTime BETWEEN :fromDate AND :toDate)")
    Page<Story> searchByKeywordAndTimeRange(
            @Param("keyword") String keyword,
            @Param("isDate") int isDate,
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate,
            Pageable pageable);
    @Query("SELECT s FROM Story s WHERE DATE(s.submissionTime) = DATE(:date)")
    Page<Story> findBySubmissionDate(@Param("date") Date date, Pageable pageable);
    @Query("SELECT s FROM Story s JOIN s.author u WHERE u.username = :username")
    List<Story> findStoriesByUserUsername(@Param("username") String username);
    @Query("SELECT s FROM Story s JOIN s.author u WHERE u.username = :username")
    Page<Story> findStoriesByUser(@Param("username") String username,Pageable pageable);
    @Query("SELECT us.upvotedStories FROM User us WHERE us.username = :username")
    Page<Story> findUpvotedStories(@Param("username") String username,Pageable pageable);
    @Query("SELECT us.favoriteStories FROM User us WHERE us.username = :username")
    Page<Story> findStoriesByFavorite(@Param("username") String username,Pageable pageable);

}






