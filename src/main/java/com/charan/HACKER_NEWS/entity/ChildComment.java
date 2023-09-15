package com.charan.HACKER_NEWS.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "child_comment")
public class ChildComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Comment parentComment;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "score")
    private Long score;

    @Column(name = "submissionTime", columnDefinition = "TIMESTAMP")
    private Timestamp submissionTime;
    @Transient
    private Long formattedTime;

    public ChildComment(){

    }
    public ChildComment(Long id, User author, Comment parentComment, String text, Long score, Timestamp submissionTime,Long formattedTime) {
        this.id = id;
        this.author = author;
        this.parentComment = parentComment;
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

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
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
        return "ChildComment{" +
                "id=" + id +
                ", author=" + author +
                ", parentComment=" + parentComment +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", submissionTime=" + submissionTime +
                '}';
    }
}

