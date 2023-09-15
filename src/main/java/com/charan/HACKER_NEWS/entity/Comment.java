package com.charan.HACKER_NEWS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Story parentStory;

    @ManyToOne
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> replies = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String text;
    @Column(name ="score")
    private int score;
    @Column(name = "submissionTime",columnDefinition = "TIMESTAMP")
    private Timestamp submissionTime;

    @Transient
    private Long formattedTime;
    public Comment(){

    }

    public Comment(Long id, User author, Story parentStory, Comment parentComment, List<Comment> replies, String text, int score, Timestamp submissionTime,Long formattedTime) {
        this.id = id;
        this.author = author;
        this.parentStory = parentStory;
        this.parentComment = parentComment;
        this.replies = replies;
        this.text = text;
        this.score = score;
        this.submissionTime = submissionTime;
        this.formattedTime = formattedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Story getParentStory() {
        return parentStory;
    }

    public void setParentStory(Story parentStory) {
        this.parentStory = parentStory;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Timestamp getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Timestamp submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Long getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(Long formattedTime) {
        this.formattedTime = formattedTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", submissionTime=" + submissionTime +
                '}';
    }
}
