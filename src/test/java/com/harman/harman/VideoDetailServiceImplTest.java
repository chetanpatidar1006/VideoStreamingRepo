package com.harman.harman;

import com.harman.harman.exception.ResourceNotFound;
import com.harman.harman.model.VideoDetail;
import com.harman.harman.repository.VideoDetailRepo;
import com.harman.harman.service.impl.VideoDetailServiceImpl;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class VideoDetailServiceImplTest {

    @Mock
    private VideoDetailRepo videoDetailRepo;

    @InjectMocks
    private VideoDetailServiceImpl videoDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void publishVideoTest() {
        VideoDetail video = new VideoDetail();
        video.setTitle("Title");
        when(videoDetailRepo.save(any(VideoDetail.class))).thenReturn(video);
        videoDetailService.publishVideo(video);
        verify(videoDetailRepo, times(1)).save(any(VideoDetail.class));
    }

    @Test
    void updateMetadataTest() {
        VideoDetail video = new VideoDetail();
        video.setTitle("Updated Title");
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.of(video));
        when(videoDetailRepo.save(any(VideoDetail.class))).thenReturn(video);
        VideoDetail updatedVideo = videoDetailService.updateMetadata(1, video);
        assertEquals("Updated Title", updatedVideo.getTitle());
        verify(videoDetailRepo, times(1)).findById(anyInt());
        verify(videoDetailRepo, times(1)).save(any(VideoDetail.class));
    }

    @Test
    void updateMetadataNotFoundTest() {
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, () -> videoDetailService.updateMetadata(1, new VideoDetail()));
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }

    @Test
    void delistVideoTest() {
        VideoDetail video = new VideoDetail();
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.of(video));
        when(videoDetailRepo.save(any(VideoDetail.class))).thenReturn(video);
        videoDetailService.delistVideo(1);
        assertTrue(video.getDeListed());
        verify(videoDetailRepo, times(1)).findById(anyInt());
        verify(videoDetailRepo, times(1)).save(any(VideoDetail.class));
    }

    @Test
    void delistVideoTest1() {

        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(ResourceNotFound.class, () -> videoDetailService.delistVideo(1));
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }

    @Test
    void loadVideoTest() {
        VideoDetail video = new VideoDetail();
        video.setImpressions(0);
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.of(video));
        when(videoDetailRepo.save(any(VideoDetail.class))).thenReturn(video);

        VideoDetail loadedVideo = videoDetailService.loadVideo(1);

        assertEquals(1, loadedVideo.getImpressions());
        verify(videoDetailRepo, times(1)).findById(anyInt());
        verify(videoDetailRepo, times(1)).save(any(VideoDetail.class));
    }

    @Test
    void loadVideoTest1() {
        VideoDetail video = new VideoDetail();
        video.setImpressions(0);
        video.setId(1);
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(ResourceNotFound.class, () -> videoDetailService.loadVideo(1));
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }

    @Test
    void playVideoTest() {
        VideoDetail video = new VideoDetail();
        video.setTitle("Title");
        video.setDirector("Director");
        video.setViews(0);
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.of(video));
        when(videoDetailRepo.save(any(VideoDetail.class))).thenReturn(video);
        String result = videoDetailService.playVideo(1);
        assertEquals("Video content for: Title and Director is playing And the video has been viewed 1 times", result);
        verify(videoDetailRepo, times(1)).findById(anyInt());
        verify(videoDetailRepo, times(1)).save(any(VideoDetail.class));
    }

    @Test
    void playVideoTest1() {
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(ResourceNotFound.class, () -> videoDetailService.playVideo(1));
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }

    @Test
    void listVideosTest() {
        VideoDetail video = new VideoDetail();
        video.setDeListed(false);
        video.setRunningTime(100);
        video.setCastName("test");
        video.setDirector("test");
        video.setGenre("test");
        video.setYearOfRelease("test");
        video.setSynopsis("test");
        List<VideoDetail> videos = Collections.singletonList(video);
        when(videoDetailRepo.findAll()).thenReturn(videos);

        List<VideoDetailWrapper> result = videoDetailService.listVideos();

        assertEquals(1, result.size());
        verify(videoDetailRepo, times(1)).findAll();
    }

    @Test
    void searchVideosTest() {
        VideoDetail video = new VideoDetail();
        video.setCastName("test");
        video.setDirector("Director");
        video.setGenre("test");
        video.setCastName("test");
        video.setYearOfRelease("test");
        video.setSynopsis("test");
        video.setTitle("Title");
        video.setRunningTime(130);
        video.setDeListed(Boolean.FALSE);
        List<VideoDetail> videos = new ArrayList<>();
        videos.add(video);
        when(videoDetailRepo.findAll(any(Specification.class))).thenReturn(videos);
        List<VideoDetailWrapper> result = videoDetailService.searchVideos("Director", "Title", "test", "test");
        assertEquals(1, result.size());
        verify(videoDetailRepo, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getEngagementStatsTest() {
        VideoDetail video = new VideoDetail();
        video.setImpressions(100);
        video.setViews(50);
        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.of(video));
        EngagementStatsWrapper stats = videoDetailService.getEngagementStats(1);
        assertEquals(100, stats.getImpressions());
        assertEquals(50, stats.getViews());
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }

    @Test
    void getEngagementStatsTest1() {

        when(videoDetailRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(ResourceNotFound.class, () -> videoDetailService.getEngagementStats(1));
        verify(videoDetailRepo, times(1)).findById(anyInt());
    }
}
