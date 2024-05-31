package com.ll.edubridge.domain.youtube;

import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.ll.edubridge.domain.openai.ChatResponse;
import com.ll.edubridge.domain.openai.ChatService;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rsData.RsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1/youtube")
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private YouTubeSubtitleService youTubeSubtitleService;

    @Autowired
    private YouTubeCaptionService youTubeCaptionService;

    @Autowired
    private PythonScriptService pythonScriptService;
    @Autowired
    private ChatService chatService;

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


    @GetMapping("/getKeywords")
    public RsData<String> runScript(@RequestParam String videoId) throws IOException {
        try {
            String input = pythonScriptService.runPythonScript(videoId);
            Future<String> chat = chatService.chat(input + "이 내용을 보고 가장 중요하다고 생각하는 키워드만 딱 5개를 추출해서 합친후 ,로 구분해서 알려줘.");
            return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), chat.get());
        } catch (IOException e) {
            e.printStackTrace();
            return RsData.of("500", "Internal Server Error", null);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
