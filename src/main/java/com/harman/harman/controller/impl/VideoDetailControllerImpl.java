package com.harman.harman.controller.impl;

import com.harman.harman.controller.VideoDetailController;
import com.harman.harman.model.VideoDetail;
import com.harman.harman.service.VideoDetailService;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.harman.harman.constant.Constants.VIDEO_DELISTED_SUCCESSFULLY;
import static com.harman.harman.constant.Constants.VIDEO_PUBLISHED_SUCCESSFULLY;

@Component
public class VideoDetailControllerImpl implements VideoDetailController {

    private static final Logger logger = LoggerFactory.getLogger(VideoDetailControllerImpl.class);

    @Autowired
    private VideoDetailService videoService;

    public ResponseEntity<String> publishVideo(VideoDetail video) {
        logger.info("Request received to publish video: {}", video.getTitle());
        videoService.publishVideo(video);
        logger.info("Video published successfully.");
        return ResponseEntity.accepted().body(VIDEO_PUBLISHED_SUCCESSFULLY);
    }

    public ResponseEntity<VideoDetail> updateMetadata(Integer id, VideoDetail video) {
        logger.info("Request received to update metadata for video with id: {}", id);
        VideoDetail updatedVideo = videoService.updateMetadata(id, video);
        logger.info("Metadata updated successfully for video.");
        return ResponseEntity.ok(updatedVideo);
    }

    public ResponseEntity<String> delistVideo(Integer id) {
        logger.info("Request received to delist video with id: {}", id);
        videoService.delistVideo(id);
        logger.info("Video delisted successfully.");
        return ResponseEntity.accepted().body(VIDEO_DELISTED_SUCCESSFULLY);
    }

    public ResponseEntity<VideoDetail> loadVideo(@PathVariable Integer id) {
        logger.info("Request received to load video with id: {}", id);
        VideoDetail video = videoService.loadVideo(id);
        logger.info("Video loaded successfully.");
        return ResponseEntity.ok(video);
    }

    public ResponseEntity<String> playVideo(@PathVariable Integer id) {
        logger.info("Request received to play video with id: {}", id);
        String result = videoService.playVideo(id);
        logger.info("Video played successfully.");
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<List<VideoDetailWrapper>> listVideos() {
        logger.info("Request received to list all videos");
        List<VideoDetailWrapper> videos = videoService.listVideos();
        logger.info("Listed all videos successfully.");
        return ResponseEntity.ok(videos);
    }

    public ResponseEntity<List<VideoDetailWrapper>> searchVideos(String directorName, String title, String cast, String genre) {
        logger.info("Request received to search videos with filters - directorName: {}, title: {}, cast: {}, genre: {}", directorName, title, cast, genre);
        List<VideoDetailWrapper> videos = videoService.searchVideos(directorName, title, cast, genre);
        logger.info("Searched videos successfully with filters.");
        return ResponseEntity.ok(videos);
    }

    public ResponseEntity<EngagementStatsWrapper> getEngagementStats(Integer id) {
        logger.info("Request received to get engagement stats for video with id: {}", id);
        EngagementStatsWrapper stats = videoService.getEngagementStats(id);
        logger.info("Got engagement stats successfully for video.");
        return ResponseEntity.ok(stats);
    }
}