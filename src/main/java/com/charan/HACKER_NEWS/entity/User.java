package com.charan.HACKER_NEWS.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.*;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "profilelink")
    private String profilePictureUrl;
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Story> submittedStories;
    @ManyToMany
    @JoinTable(name="user_upvoted_stories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "story_id"))
    private List<Story> upvotedStories;
    @ManyToMany
    @JoinTable(name="user_favorite_stories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "story_id"))
    private List<Story> favoriteStories;
    @ManyToMany
    @JoinTable(
            name = "user_downvoted_stories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "story_id")
    )
    private List<Story> downvotedStories;
    @ManyToMany
    @JoinTable(
            name = "user_upvoted_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private List<Comment> upvotedComments;
    @ManyToMany
    @JoinTable(
            name = "user_downvoted_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private List<Comment> downvotedComments;

    @ManyToMany
    @JoinTable(
            name = "user_saved_stories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "story_id")
    )
    private List<Story> savedStories;

    @Column(name="role")
    private String role;
    @Column(name = "enabled")
    private int enabled;
    @Column(name = "submission_time", columnDefinition = "TIMESTAMP")
    private Timestamp submissionTime;
    @Transient
    private Long formattedTime;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "media_id")
    private Media media;

    public User(){

    }

    public User(Long id, String username, String email, String password, String profilePictureUrl, List<Story> submittedStories,
                List<Story> upvotedStories, List<Story> favoriteStories, List<Story> downvotedStories,
                List<Comment> upvotedComments, List<Comment> downvotedComments,
                List<Story> savedStories, String role, int enabled, Timestamp submissionTime, Long formattedTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.submittedStories = submittedStories;
        this.upvotedStories = upvotedStories;
        this.favoriteStories = favoriteStories;
        this.downvotedStories = downvotedStories;
        this.upvotedComments = upvotedComments;
        this.downvotedComments = downvotedComments;
        this.savedStories = savedStories;
        this.role = role;
        this.enabled = enabled;
        this.submissionTime = submissionTime;
        this.formattedTime = formattedTime;
    }

    public User(Media media) {
        this.media = media;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<Story> getSubmittedStories() {
        return submittedStories;
    }

    public void setSubmittedStories(List<Story> submittedStories) {
        this.submittedStories = submittedStories;
    }

    public List<Story> getUpvotedStories() {
        return upvotedStories;
    }

    public void setUpvotedStories(List<Story> upvotedStories) {
        this.upvotedStories = upvotedStories;
    }

    public List<Comment> getUpvotedComments() {
        return upvotedComments;
    }

    public void setUpvotedComments(List<Comment> upvotedComments) {
        this.upvotedComments = upvotedComments;
    }

    public List<Story> getSavedStories() {
        return savedStories;
    }

    public void setSavedStories(List<Story> savedStories) {
        this.savedStories = savedStories;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<Story> getDownvotedStories() {
        return downvotedStories;
    }

    public void setDownvotedStories(List<Story> downvotedStories) {
        this.downvotedStories = downvotedStories;
    }

    public boolean hasUpvoted(Story story) {
        return upvotedStories.contains(story);
    }

    public boolean hasDownvoted(Story story) {
        return downvotedStories.contains(story);
    }

    public void upvote(Story story) {
        upvotedStories.add(story);
        downvotedStories.remove(story);
    }

    public void downvote(Story story) {
        downvotedStories.add(story);
        upvotedStories.remove(story);
    }
    public void upvoteComment(Comment comment) {
        upvotedComments.add(comment);
        downvotedComments.remove(comment);
    }

    public void downvoteComment(Comment comment) {
        downvotedComments.add(comment);
        upvotedComments.remove(comment);
    }
    public void removeUpvote(Story story) {
        upvotedStories.remove(story);
    }
    public void removeUpvoteComment(Comment comment) {
        upvotedComments.remove(comment);
    }
    public boolean hasUpvotedComment(Comment comment) {
        return upvotedComments.contains(comment);
    }

    public boolean hasDownvotedComment(Comment comment) {
        return downvotedComments.contains(comment);
    }

    public List<Comment> getDownvotedComments() {
        return downvotedComments;
    }

    public void setDownvotedComments(List<Comment> downvotedComments) {
        this.downvotedComments = downvotedComments;
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

    public List<Story> getFavoriteStories() {
        return favoriteStories;
    }

    public void setFavoriteStories(List<Story> favoriteStories) {
        this.favoriteStories = favoriteStories;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                '}';
    }

}
