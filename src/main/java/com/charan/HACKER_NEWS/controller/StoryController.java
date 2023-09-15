package com.charan.HACKER_NEWS.controller;

import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import com.charan.HACKER_NEWS.services.StoryService;
import com.charan.HACKER_NEWS.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.*;


@Controller
@RequestMapping("/posts")
public class StoryController {
    private StoryService storyService;
    private UserService userService;
    private
    @Autowired StoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @RequestMapping("/list")
    public String listOfStories(@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "15") int size,
                                @RequestParam(name = "ask", required = false, defaultValue = "") String ask,
                                @RequestParam(name = "show", required = false, defaultValue = "") String show,
                                @RequestParam(name = "job", required = false, defaultValue = "") String jobs,
                                @RequestParam(name = "new", required = false, defaultValue = "") String news,
                                @RequestParam(name = "past", required = false, defaultValue = "") String past,
                                @RequestParam(name = "date", defaultValue = "") String dateStr,
                                @RequestParam(name = "search", defaultValue = "") String search,
                                @RequestParam(name = "submission",defaultValue = "") String submission,
                                @RequestParam(name = "upvoted_submission" , defaultValue = "") String upvoted_submission,
                                @RequestParam(name = "favorite" , defaultValue = "") String favorite,
                                Model model, HttpSession session) {

        Page<Story> postsPage;
        Pageable pageable = PageRequest.of(page, size);

        StringBuilder paginationUrl = new StringBuilder("/posts/list?")
                .append("size=").append(size);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;  // Initialize the date variable
//
//        if (dateStr!=null) {
//            try {
//                date = dateFormat.parse(dateStr);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

        if (!ask.isEmpty()) {
            postsPage = storyService.findByAsk(ask, pageable);
            paginationUrl.append("&ask=").append(ask);
        } else if (!show.isEmpty()) {
            postsPage = storyService.findByShow(show, pageable);
            paginationUrl.append("&show=").append(show);
        } else if (!jobs.isEmpty()) {
            postsPage = storyService.findByJobs(jobs, pageable);
            paginationUrl.append("&jobs=").append(jobs);
        }else if(! news.isEmpty()){
            postsPage = storyService.findByNew(news,pageable);
            paginationUrl.append("&new=").append(news);
        }
        else if(!past.isEmpty()){
            postsPage = storyService.findByPost(pageable);
        }
        else {
            postsPage = storyService.findAllStories(pageable);
        }

        for (Story story : postsPage.getContent()) {
            story.setFormattedTime(story.getSubmissionTime().getTime());
        }
        if(!past.isEmpty()){
            paginationUrl.append("&past=").append(past);
        }
        if(!search.isEmpty()){
            paginationUrl.append("&search=").append(search);
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            user = userService.findByName(userDetails.getUsername());
            model.addAttribute("user",user);
        }
//        if (!past.isEmpty() && date != null) {
//            Page<Story> stories = storyService.findBySubmissionDate(date, PageRequest.of(page, size));
//            if (stories != null) {
//                model.addAttribute("stories", stories);
//                model.addAttribute("date", date);
//            }
//        }
        if(!submission.isEmpty()){
            postsPage = storyService.findStoriesByUser(user.getUsername(),pageable);
            model.addAttribute("submission",submission);
        }
        else if(!upvoted_submission.isEmpty()){
            postsPage = storyService.findUpvotedStories(user.getUsername(),pageable);
            System.out.println(upvoted_submission);
        }
        else if(!favorite.isEmpty()){
            postsPage = storyService.findStoriesByFavorite(user.getUsername(),pageable);
        }
        model.addAttribute("stories", postsPage);

        model.addAttribute("past",past);
        model.addAttribute("paginationUrl", paginationUrl.toString());

        return "posts/stories_home_page";
    }
    @PostMapping("/{id}/upvote")
    public String upvoteStory(@PathVariable("id") Long storyId, HttpSession session) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(userDetails.getUsername());

        if (user != null) {
            Story story = storyService.findById(storyId);

            if (story != null && !user.hasUpvoted(story)) {
                user.upvote(story);
                story.setScore(story.getScore() + 1);
                userService.save(user);
                storyService.save(story);

                // Add the story ID to the set of upvoted stories in the session
                Set<Long> upvotedStories = (Set<Long>) session.getAttribute("upvotedStories");
                if (upvotedStories == null) {
                    upvotedStories = new HashSet<>();
                    session.setAttribute("upvotedStories", upvotedStories);
                }
                upvotedStories.add(storyId);
                Set<Long> downvotedStories = (Set<Long>) session.getAttribute("downvotedStories");
                if (downvotedStories != null && downvotedStories.contains(storyId)) {
                    downvotedStories.remove(storyId);
                }
            }
        }
        return "redirect:/posts/list";
    }

    @PostMapping("/{id}/downvote")
    public String downvoteStory(@PathVariable("id") Long storyId, HttpSession session) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(userDetails.getUsername());

        if (user != null) {
            Story story = storyService.findById(storyId);

            if (story != null && !user.hasDownvoted(story)) {
                if (user.hasUpvoted(story)) {
                    user.removeUpvote(story);
                    story.setScore(story.getScore() - 1);
                }

                user.downvote(story);
                userService.save(user);
                storyService.save(story);

                // Add the story ID to the set of downvoted stories in the session
                Set<Long> downvotedStories = (Set<Long>) session.getAttribute("downvotedStories");
                if (downvotedStories == null) {
                    downvotedStories = new HashSet<>();
                    session.setAttribute("downvotedStories", downvotedStories);
                }
                downvotedStories.add(storyId);

                // Remove the story ID from the set of upvoted stories in the session
                Set<Long> upvotedStories = (Set<Long>) session.getAttribute("upvotedStories");
                if (upvotedStories != null) {
                    upvotedStories.remove(storyId);
                }
            }
        }
        return "redirect:/posts/list";
    }



    @RequestMapping("/createstory")
    public String createStory(Model model){
        Story story = new Story();
        model.addAttribute(story);
        return "posts/create_post";
    }

    @PostMapping("/save")
    public String saveStory(@ModelAttribute("story") Story story) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            story.setSubmissionTime(currentTimestamp);

            User user = userService.findByName(userDetails.getUsername());

            story.setAuthor(user);
            storyService.save(story);

            System.out.println("Author: " + story.getAuthor().getUsername());
        } else {
            return "redirect:/user/LoginPage";
        }

        return "redirect:/posts/list";
    }


    @RequestMapping("/welcome")
    public String welocme(){
        return "posts/welcome";
    }
    @RequestMapping("/ask")
    public String ask(Model model){

        return "posts/ask";
    }

//    @GetMapping("/search")
//    public String searchStories(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "15") int size,
//            @RequestParam(name = "keyword", required = false) String keyword,
//            @RequestParam(name = "type", required = false) String type,
//            @RequestParam(name = "sortBy", required = false) String sortBy,
//            @RequestParam(name = "timeRange", required = false) String timeRange,
//            Model model
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Story> searchResults = storyService.searchByKeyword(keyword, pageable);
//
//
//        if (type != null) {
//            searchResults = storyService.filterByType(type, pageable);
//        }
//
//        if ("popularity".equals(sortBy)) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("score")));
//        } else if ("date".equals(sortBy)) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("timestamp")));
//        }
//
//        if (timeRange != null) {
//            Timestamp toDate = Timestamp.from(Instant.now());
//            Timestamp fromDate = calculateFromDate(timeRange, toDate);
//
//            searchResults = storyService.filterByTimeRange(fromDate, toDate, pageable);
//        }
//
//        StringBuilder paginationUrl = new StringBuilder("/posts/search?")
//                .append("size=").append(size)
//                .append("&keyword=").append(keyword != null ? keyword : "")
//                .append("&type=").append(type != null ? type : "")
//                .append("&sortBy=").append(sortBy != null ? sortBy : "")
//                .append("&timeRange=").append(timeRange != null ? timeRange : "");
//
//        model.addAttribute("searchResults", searchResults);
//        model.addAttribute("paginationUrl", paginationUrl.toString());
//        return "posts/search_page";
//    }
//
//    private Timestamp calculateFromDate(String timeRange, Timestamp toDate) {
//        Instant toInstant = toDate.toInstant();
//        Instant fromInstant = null;
//
//        if ("last24hours".equals(timeRange)) {
//            fromInstant = toInstant.minus(24, ChronoUnit.HOURS);
//        } else if ("pastWeek".equals(timeRange)) {
//            fromInstant = toInstant.minus(7, ChronoUnit.DAYS);
//        } else if ("pastMonth".equals(timeRange)) {
//            fromInstant = toInstant.minus(30, ChronoUnit.DAYS); // Approximate, adjust as needed
//        } else if ("pastYear".equals(timeRange)) {
//            fromInstant = toInstant.minus(365, ChronoUnit.DAYS); // Approximate, adjust as needed
//        }
//
//        if (fromInstant != null) {
//            return Timestamp.from(fromInstant);
//        } else {
//            return null;
//        }
//    }
    @GetMapping("/search")
    public String searchStories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "search", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "popularity") String sortBy,
            @RequestParam(name = "timeRange", defaultValue = "alltime") String timeRange,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        StringBuilder paginationUrl = new StringBuilder("/posts/search?");

        Page<Story> repositoryQueryResult;
        if ("popularity".equals(sortBy)) {
            Sort sort = Sort.by(Sort.Order.desc("score"));
            pageable = PageRequest.of(page, size, sort.descending());
        }
        else if ("date".equals(sortBy)) {
            Sort sort = Sort.by(Sort.Order.asc("submissionTime"));
            pageable = PageRequest.of(page, size, sort.descending());
        }

        Timestamp toDate = Timestamp.from(Instant.now());
        Timestamp fromDate = null;
        if ("lastday".equals(timeRange)) {
            Instant instantTo = toDate.toInstant();
            Instant instantFrom = instantTo.minus(1, ChronoUnit.DAYS);
            fromDate = Timestamp.from(instantFrom);
        }
         else if ("pastweek".equals(timeRange)) {
            Instant instantTo = toDate.toInstant();
            Instant instantFrom = instantTo.minus(7, ChronoUnit.DAYS);
            fromDate = Timestamp.from(instantFrom);
        } else if ("pastmonth".equals(timeRange)) {
            Instant instant = toDate.toInstant().minus(30, ChronoUnit.DAYS);
            fromDate = Timestamp.from(instant);
        } else if ("pastyear".equals(timeRange)) {
            Instant instant = toDate.toInstant().minus(365, ChronoUnit.DAYS);
            fromDate = Timestamp.from(instant);
        }
        repositoryQueryResult = storyService.searchByKeywordAndTimeRange(keyword, fromDate, toDate, pageable);

        if (keyword != null & !keyword.equals("")) {
            paginationUrl.append("&search=").append(keyword);
        }
        if (sortBy != null ) {
            paginationUrl.append("&sortBy=").append(sortBy);
        }
        if (timeRange != null) {
            paginationUrl.append("&timeRange=").append(timeRange);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            User user = userService.findByName(userDetails.getUsername());
            model.addAttribute("user",user);
        }
        model.addAttribute("stories", repositoryQueryResult);
        model.addAttribute("selectedSortBy", sortBy);
        model.addAttribute("selectedTimeRange", timeRange);
        model.addAttribute("paginationUrl", paginationUrl.toString());
        model.addAttribute("search",keyword);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("timeRange",timeRange);

        return "posts/search_page";
    }
    @GetMapping("/comments")
    public String comments(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "15") int size,
                           @RequestParam(name = "comments", defaultValue = "") String comments,
                           Model model){

        Page<Story> postsPage;
        Pageable pageable = PageRequest.of(page, size);
        postsPage = storyService.findAllStories(pageable);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            user = userService.findByName(userDetails.getUsername());
            model.addAttribute("user",user);
        }
        if(!comments.isEmpty()){
            postsPage = storyService.findStoriesByUser(user.getUsername(),pageable);
            model.addAttribute("comments",comments);

        }
        model.addAttribute("stories",postsPage);
        return "posts/only_comments";
    }
    @GetMapping("/profile")
    public String profile(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Story> story = new ArrayList<>();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            User user = userService.findByName(userDetails.getUsername());
            model.addAttribute("user",user);
            story = storyService.findStoriesByUserUsername(user.getUsername());
        }
        Long karma = 0L;
        for(Story story1 :story){
            karma+=story1.getScore();
        }
        karma=karma/10;
        model.addAttribute("karma",karma);
        return "posts/profile_page";
    }
    @RequestMapping("/guide")
    public String guidelines(){
        return "posts/guidelines";
    }
    @RequestMapping("/faq")
    public String faq(){
        return "posts/faq";
    }
    @RequestMapping("/security")
    public String security(){
        return "posts/security";
    }
}
