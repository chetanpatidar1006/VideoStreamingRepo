package com.harman.harman.service.impl;

import com.harman.harman.exception.ResourceNotFound;
import com.harman.harman.model.VideoDetail;
import com.harman.harman.repository.VideoDetailRepo;
import com.harman.harman.service.VideoDetailService;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.harman.harman.constant.Constants.ZERO;
import static com.harman.harman.utils.VideoSpacification.*;

@Service
public class VideoDetailServiceImpl implements VideoDetailService {

    private static final Logger logger = LoggerFactory.getLogger(VideoDetailServiceImpl.class);

    @Autowired
    private VideoDetailRepo videoDetailRepo;

    public void publishVideo(VideoDetail video) {
        logger.info("Going to publish video: {}", video.getTitle());
        video.setDeListed(Boolean.FALSE);
        video.setImpressions(ZERO);
        video.setViews(ZERO);
        videoDetailRepo.save(video);
        logger.info("Video published successfully: {}", video.getTitle());
    }

    @Override
    public VideoDetail updateMetadata(Integer id, VideoDetail video) {
        logger.info("Going to update video: {}", video.getTitle());
        VideoDetail existingVideo = videoDetailRepo.findById(id).orElseThrow(() -> {
            logger.error("Data not found for id: {}", id);
            return new ResourceNotFound("Data not found for id: " + id);
        });
        existingVideo.setTitle(video.getTitle());
        existingVideo.setSynopsis(video.getSynopsis());
        existingVideo.setDirector(video.getDirector());
        existingVideo.setCastName(video.getCastName());
        existingVideo.setYearOfRelease(video.getYearOfRelease());
        existingVideo.setGenre(video.getGenre());
        existingVideo.setRunningTime(video.getRunningTime());
        VideoDetail updatedVideo = videoDetailRepo.save(existingVideo);
        logger.info("Video updated successfully: {}", updatedVideo.getTitle());
        return updatedVideo;
    }

    @Override
    public void delistVideo(Integer id) {
        logger.info("Going to delist video with id: {}", id);
        VideoDetail existingVideo = videoDetailRepo.findById(id).orElseThrow(() -> {
            logger.error("Data not found for id: {}", id);
            return new ResourceNotFound("Data not found for id: " + id);
        });
        existingVideo.setDeListed(Boolean.TRUE);
        videoDetailRepo.save(existingVideo);
        logger.info("Video delisted successfully with id: {}", id);
    }

    @Override
    public VideoDetail loadVideo(Integer id) {
        logger.info("Loading video with id: {}", id);
        VideoDetail video = videoDetailRepo.findById(id).orElseThrow(() -> {
            logger.error("Data not found for id: {}", id);
            return new ResourceNotFound("Data not found for id: " + id);
        });
        video.setImpressions(video.getImpressions() + 1);
        VideoDetail updatedVideo = videoDetailRepo.save(video);
        logger.info("Video loaded successfully with id: {}", id);
        return updatedVideo;
    }

    @Override
    public String playVideo(Integer id) {
        logger.info("Playing video with id: {}", id);
        VideoDetail video = videoDetailRepo.findById(id).orElseThrow(() -> {
            logger.error("Data not found for id: {}", id);
            return new ResourceNotFound("Data not found for id: " + id);
        });
        video.setViews(video.getViews() + 1);
        videoDetailRepo.save(video);
        String result = "Video content for: " + video.getTitle() + " and " + video.getDirector() + " is playing And the video has been viewed " + video.getViews() + " times";
        logger.info("Video played successfully with id: {}", id);
        return result;
    }

    @Override
    public List<VideoDetailWrapper> listVideos() {
        logger.info("Listing all videos");
        List<VideoDetailWrapper> result = videoDetailRepo.findAll().stream()
                .filter(x -> x.getDeListed().equals(Boolean.FALSE))
                .map(video -> new VideoDetailWrapper(
                        video.getTitle(),
                        video.getDirector(),
                        video.getCastName(),
                        video.getGenre(),
                        video.getRunningTime()
                )).collect(Collectors.toList());
        logger.info("Listed all videos successfully");
        return result;
    }

    @Override
    public List<VideoDetailWrapper> searchVideos(String directorName, String title, String cast, String genre) {
        logger.info("Searching videos with filters - directorName: {}, title: {}, cast: {}, genre: {}", directorName, title, cast, genre);
        Specification<VideoDetail> specification = Specification.where(null);
        if (StringUtils.isNotBlank(directorName)) {
            specification = specification.and(derector(directorName));
        }
        if (StringUtils.isNotBlank(title)) {
            specification = specification.and(title(title));
        }
        if (StringUtils.isNotBlank(genre)) {
            specification = specification.and(genre(genre));
        }
        if (StringUtils.isNotBlank(cast)) {
            specification = specification.and(castName(cast));
        }
        specification = specification.and(deListed());
        List<VideoDetailWrapper> result= videoDetailRepo.findAll(specification).stream().map(video -> new VideoDetailWrapper(
                video.getTitle(),
                video.getDirector(),
                video.getCastName(),
                video.getGenre(),
                video.getRunningTime()
        )).collect(Collectors.toList());
        logger.info("Searched videos successfully with filters - directorName: {}, title: {}, cast: {}, genre: {} ", directorName, title, cast, genre );
        return result;
    }

    @Override
    public EngagementStatsWrapper getEngagementStats(Integer id) {
        logger.info("Getting engagement stats for video with id: {}", id);
        VideoDetail data = videoDetailRepo.findById(id).orElseThrow(() -> {
            logger.error("Data not found for id: {}", id);
            return new ResourceNotFound("Data not found for id: " + id);
        });
        EngagementStatsWrapper engagementStatsWrapper = new EngagementStatsWrapper(data.getImpressions(), data.getViews());
        logger.info("Got engagement stats successfully for video with id: {}", id);
        return engagementStatsWrapper;
    }
}