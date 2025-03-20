package com.harman.harman.controller;

import com.harman.harman.constant.Constants;
import com.harman.harman.model.VideoDetail;
import com.harman.harman.wrapper.EngagementStatsWrapper;
import com.harman.harman.wrapper.VideoDetailWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.harman.harman.constant.Constants.*;

@RestController
@RequestMapping(path = Constants.BASE_PATH + Constants.BASE_PATH_V1)
public interface VideoDetailController {

    @PostMapping
    public ResponseEntity<String> publishVideo(@RequestBody VideoDetail video) ;

    @PutMapping(ID_PATH)
    public ResponseEntity<VideoDetail> updateMetadata(@PathVariable Integer id, @RequestBody VideoDetail video) ;

    @DeleteMapping(ID_PATH)
    public ResponseEntity<String> delistVideo(@PathVariable Integer id) ;

    @GetMapping(ID_PATH)
    public ResponseEntity<VideoDetail> loadVideo(@PathVariable Integer id) ;

    @PostMapping(PLAY_ID_PATH)
    public ResponseEntity<String> playVideo(@PathVariable Integer id) ;

    @GetMapping
    public ResponseEntity<List<VideoDetailWrapper>> listVideos();

    @GetMapping(SEARCH_PATH)
    public ResponseEntity<List<VideoDetailWrapper>> searchVideos(  @RequestParam(required = false, name = DIRECTOR_NAME) String directorName,
                                                                   @RequestParam(required = false, name = TITLE) String title,
                                                                   @RequestParam(required = false, name = CAST) String Cast,
                                                                   @RequestParam(required = false, name = GENRE) String genre);
    @GetMapping(ENGAGEMENT_ID_PATH)
    public ResponseEntity<EngagementStatsWrapper> getEngagementStats(@PathVariable Integer id) ;
}
