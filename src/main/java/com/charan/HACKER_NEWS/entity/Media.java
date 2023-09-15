package com.charan.HACKER_NEWS.entity;

import jakarta.persistence.*;

import java.sql.Blob;

@Entity
@Table(name="medias")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mediaId;
    private Blob media;
    private String contentType;

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Blob getMedia() {
        return media;
    }

    public void setMedia(Blob media) {
        this.media = media;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
