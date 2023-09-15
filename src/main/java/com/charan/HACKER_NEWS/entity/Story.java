package com.charan.HACKER_NEWS.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "story")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "text", length = 2500)
    private String text;

    @Column(name = "score")
    private int score;

    @Column(name = "category")
    private String category;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "parentStory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "parentStory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> kids;

    @Column(name = "submission_time", columnDefinition = "TIMESTAMP")
    private Timestamp submissionTime;

    @Transient
    private Long formattedTime;

    public Story() {
    }

    public Story(Long id, String title, String url, String text, int score, String category, User author,
                 List<Comment> comments, List<Comment> kids, Timestamp submissionTime, Long formattedTime) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.text = text;
        this.score = score;
        this.category = category;
        this.author = author;
        this.comments = comments;
        this.kids = kids;
        this.submissionTime = submissionTime;
        this.formattedTime = formattedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getKids() {
        return kids;
    }

    public void setKids(List<Comment> kids) {
        this.kids = kids;
    }

    public Long getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(Long formattedTime) {
        this.formattedTime = formattedTime;
    }

    public Timestamp getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Timestamp submissionTime) {
        this.submissionTime = submissionTime;
    }



    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", category='" + category + '\'' +
                ", submissionTime=" + submissionTime +
                '}';
    }
}
