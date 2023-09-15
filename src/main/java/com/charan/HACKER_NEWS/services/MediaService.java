//package com.charan.HACKER_NEWS.services;
//import com.charan.HACKER_NEWS.entity.Media;
//
//import java.util.*;
//
//import com.charan.HACKER_NEWS.repository.MediaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MediaService {
//    private MediaRepository mediaRepository;
//    public MediaService(MediaRepository mediaRepository) {
//        this.mediaRepository = mediaRepository;
//    }
//
//    public Media create(Media media) {
//        return mediaRepository.save(media);
//    }
//
//    public Media update(Media media) {
//        return mediaRepository.save(media);
//    }
//    public List<Media> viewAll() {
//        return (List<Media>) mediaRepository.findAll();
//    }
//
//
//    public Media viewById(Integer id) {
//        return mediaRepository.findById(id).get();
//    }
//}
