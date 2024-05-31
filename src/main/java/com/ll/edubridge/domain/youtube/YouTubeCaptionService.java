package com.ll.edubridge.domain.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CaptionListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class YouTubeCaptionService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final String APPLICATION_NAME = "youtube-captions-downloader";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private YouTube getService() throws GeneralSecurityException, IOException {
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public CaptionListResponse getCaptions(String videoId) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        YouTube.Captions.List request = youtubeService.captions()
                .list("snippet", videoId)
                .setKey(apiKey);
        return request.execute();

    }

    public String downloadCaption(String captionId) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        YouTube.Captions.Download request = youtubeService.captions().download(captionId)
                .setKey(apiKey);
        return request.executeMediaAsInputStream().toString(); // You may want to handle the InputStream properly.
    }
}
