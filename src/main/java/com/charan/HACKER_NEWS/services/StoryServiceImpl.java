package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.Comment;
import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.repository.StoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class StoryServiceImpl implements StoryService{
    private StoryRepository storyRepository;

    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Override
    public List<Story> findAll() {
        return storyRepository.findAll();
    }

    @Override
    public Story findById(Long id) {
        Optional<Story> result = storyRepository.findById(id);
        Story story = null;
        if(result.isPresent()){
            story = result.get();
        }
        else{
            throw new RuntimeException("Did not find storyId "+id);
        }
        return story;
    }

    @Override
    public void save(Story story) {
        storyRepository.save(story);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        storyRepository.deleteById(id);
    }

    @Override
    public Page<Story> findAll(Pageable pageable) {
        return storyRepository.findAll(pageable);
    }
    public Page<Story> findAllStories(Pageable pageable){
        return storyRepository.findAllStories(pageable);
    }

    @Override
    public Page<Story> findByAsk(String ask, Pageable pageable) {
        return storyRepository.findByAsk(ask,pageable);
    }

    @Override
    public Page<Story> findByShow(String show, Pageable pageable) {
        return storyRepository.findByShow(show,pageable);
    }
    @Override
    public Page<Story> findByJobs(String Job, Pageable pageable) {
        return storyRepository.findByJobs(Job,pageable);
    }
    @Override
    public Page<Story> findByNew(String news, Pageable pageable) {
        return storyRepository.findByNew(news,pageable);
    }

    @Override
    public Page<Story> searchByKeywordAndTimeRange(String keyword, Timestamp fromDate, Timestamp toDate, Pageable pageable) {
        int isDateTime = (fromDate== null || toDate==null) ? 0:1;
        return storyRepository.searchByKeywordAndTimeRange(keyword,isDateTime,fromDate,toDate,pageable);
    }
    @Override
    public Page<Story> findBySubmissionDate( Date date, Pageable pageable){
        return storyRepository.findBySubmissionDate(date,pageable);
    }
    @Override
    public List<Story> findStoriesByUserUsername(String username){
        return storyRepository.findStoriesByUserUsername(username);
    }

    @Override
    public Page<Story> findStoriesByUser(String username, Pageable pageable) {
        return storyRepository.findStoriesByUser(username,pageable);
    }

    @Override
    public Page<Story> findUpvotedStories(String username, Pageable pageable) {
        return storyRepository.findUpvotedStories(username,pageable);
    }
    public Page<Story> findStoriesByFavorite(@Param("username") String username,Pageable pageable){
        return storyRepository.findStoriesByFavorite(username,pageable);
    }

    @Override
    public Page<Story> findByPost(Pageable pageable) {
        return storyRepository.findByPost(pageable);
    }
    @Transactional
    @Override
    public void removeCommentReferencesByStoryIdDownvote(Story story) {
        storyRepository.removeCommentReferencesByStoryIdDownvote(story);
    }
    @Transactional
    @Override
    public void removeCommentReferencesByStoryIdUpvote(Story story) {
         storyRepository.removeCommentReferencesByStoryIdUpvote(story);
    }


}

//    @Override
//    public Page<Story> findByJobs(String Jobs, Pageable pageable) {
//        return storyRepository.findByJobs(Jobs,pageable);
//    }
//
//    @Override
//    public Page<Story> searchByKeyword(String keyword, Pageable pageable) {
//        return storyRepository.searchByKeyword(keyword,pageable);
//    }
//
//    @Override
//    public Page<Story> filterByType(String type, Pageable pageable) {
//        return storyRepository.filterByType(type,pageable);
//    }
//
//    @Override
//    public Page<Story> filterByTimeRange(Timestamp fromDate, Timestamp toDate, Pageable pageable) {
//        return storyRepository.filterByTimeRange(fromDate,toDate,pageable);
//    }
