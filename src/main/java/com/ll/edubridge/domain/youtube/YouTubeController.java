package com.ll.edubridge.domain.youtube;

import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private YouTubeSubtitleService youTubeSubtitleService;

    @Autowired
    private YouTubeCaptionService youTubeCaptionService;

    @Autowired
    private PythonScriptService pythonScriptService;

    @GetMapping("/search")
    public Mono<YouTubeResponse> search(@RequestParam String query, @RequestParam(defaultValue = "10") int maxResults) {
        return youTubeService.searchPlaylistsByName(query, maxResults)
                .map(YouTubeResponse::new);
    }
    @GetMapping("/has-subtitles")
    public Mono<Boolean> hasSubtitles(@RequestParam String videoId) {
        return youTubeSubtitleService.hasSubtitles(videoId);
    }

    @GetMapping("/get-subtitle")
    public Mono<String> getSubtitle(@RequestParam String videoId) {
        return youTubeSubtitleService.getSubtitleUrl(videoId)
                .flatMap(subtitleUrl -> {
                    try {
                        String srtContent = youTubeSubtitleService.downloadSubtitle(subtitleUrl);
                        String parsedText = youTubeSubtitleService.parseSrt(srtContent);
                        return Mono.just(parsedText);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }


    @GetMapping("/captions")
    public List<Caption> getCaptions(@RequestParam String videoId) throws GeneralSecurityException, IOException {
        CaptionListResponse response = youTubeCaptionService.getCaptions(videoId);
        return response.getItems();
    }

    @GetMapping("/downloadCaption")
    public String downloadCaption(@RequestParam String captionId) throws GeneralSecurityException, IOException {
        return youTubeCaptionService.downloadCaption(captionId);
    }


    @GetMapping("/run-script")
    public String runScript() {
        try {
            pythonScriptService.runPythonScript();
            return "Python script executed successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Python script execution failed!";
        }
    }
}
