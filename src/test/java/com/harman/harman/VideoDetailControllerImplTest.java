package com.harman.harman;

import com.harman.harman.controller.impl.VideoDetailControllerImpl;
import com.harman.harman.model.VideoDetail;
import com.harman.harman.service.VideoDetailService;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class VideoDetailControllerImplTest {

	@Mock
	private VideoDetailService videoService;

	@InjectMocks
	private VideoDetailControllerImpl videoDetailController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void publishVideoTest() {
		VideoDetail video = new VideoDetail();
		video.setTitle("Title");
//		when(videoService.publishVideo(any(VideoDetail.class))).thenReturn(null);

		ResponseEntity<String> response = videoDetailController.publishVideo(video);

		assertEquals("Video published successfully", response.getBody());
		verify(videoService, times(1)).publishVideo(any(VideoDetail.class));
	}

	@Test
	void updateMetadataTest() {
		VideoDetail video = new VideoDetail();
		video.setTitle("Updated Title");
		when(videoService.updateMetadata(anyInt(), any(VideoDetail.class))).thenReturn(video);

		ResponseEntity<VideoDetail> response = videoDetailController.updateMetadata(1, video);

		assertEquals("Updated Title", response.getBody().getTitle());
		verify(videoService, times(1)).updateMetadata(anyInt(), any(VideoDetail.class));
	}

	@Test
	void delistVideoTest() {
		doNothing().when(videoService).delistVideo(anyInt());

		ResponseEntity<String> response = videoDetailController.delistVideo(1);

		assertEquals("Video delisted successfully", response.getBody());
		verify(videoService, times(1)).delistVideo(anyInt());
	}

	@Test
	void loadVideoTest() {
		VideoDetail video = new VideoDetail();
		video.setTitle("Title");
		when(videoService.loadVideo(anyInt())).thenReturn(video);

		ResponseEntity<VideoDetail> response = videoDetailController.loadVideo(1);

		assertEquals("Title", response.getBody().getTitle());
		verify(videoService, times(1)).loadVideo(anyInt());
	}

	@Test
	void playVideoTest() {
		when(videoService.playVideo(anyInt())).thenReturn("Video content for: Title and Director is playing And the video has been viewed 1 times");

		ResponseEntity<String> response = videoDetailController.playVideo(1);

		assertEquals("Video content for: Title and Director is playing And the video has been viewed 1 times", response.getBody());
		verify(videoService, times(1)).playVideo(anyInt());
	}

	@Test
	void listVideosTest() {
		List<VideoDetailWrapper> videos = Collections.singletonList(new VideoDetailWrapper("Title", "Director", "Cast", "Genre", 120));
		when(videoService.listVideos()).thenReturn(videos);

		ResponseEntity<List<VideoDetailWrapper>> response = videoDetailController.listVideos();

		assertEquals(1, response.getBody().size());
		verify(videoService, times(1)).listVideos();
	}

	@Test
	void searchVideosTest() {
		List<VideoDetailWrapper> videos = Collections.singletonList(new VideoDetailWrapper("Title", "Director", "Cast", "Genre", 120));
		when(videoService.searchVideos(anyString(), anyString(), anyString(), anyString())).thenReturn(videos);
		ResponseEntity<List<VideoDetailWrapper>> response = videoDetailController.searchVideos("Director", "Title", "Cast", "Genre");
		assertEquals(1, response.getBody().size());
		verify(videoService, times(1)).searchVideos(anyString(), anyString(), anyString(), anyString());
	}

	@Test
	void getEngagementStatsTest() {
		EngagementStatsWrapper stats = new EngagementStatsWrapper(100, 50);
		when(videoService.getEngagementStats(anyInt())).thenReturn(stats);

		ResponseEntity<EngagementStatsWrapper> response = videoDetailController.getEngagementStats(1);

		assertEquals(100, response.getBody().getImpressions());
		assertEquals(50, response.getBody().getViews());
		verify(videoService, times(1)).getEngagementStats(anyInt());
	}
}