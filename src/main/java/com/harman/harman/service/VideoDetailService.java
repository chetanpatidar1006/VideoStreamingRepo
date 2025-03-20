package com.harman.harman.service;

import com.harman.harman.model.VideoDetail;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;

import java.util.List;

public interface VideoDetailService {


    void publishVideo(VideoDetail video);

    VideoDetail updateMetadata(Integer id, VideoDetail video);

    void delistVideo(Integer id);

    VideoDetail loadVideo(Integer id);

    List<VideoDetailWrapper> listVideos();

    String playVideo(Integer id);

    List<VideoDetailWrapper> searchVideos(String directorName, String title, String cast, String genre);

    EngagementStatsWrapper getEngagementStats(Integer id);
}
