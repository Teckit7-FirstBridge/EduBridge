package com.ll.edubridge.domain.youtube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class YouTubeSubtitleService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public YouTubeSubtitleService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/youtube/v3").build();
        this.objectMapper = objectMapper;
    }

    public Mono<Boolean> hasSubtitles(String videoId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/captions")
                        .queryParam("part", "snippet")
                        .queryParam("videoId", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(response);
                        JsonNode itemsNode = rootNode.path("items");
                        return itemsNode.size() > 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }

    public Mono<String> getSubtitleUrl(String videoId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/captions")
                        .queryParam("part", "snippet")
                        .queryParam("videoId", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractSubtitleUrl);
    }

    private String extractSubtitleUrl(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("items");

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    JsonNode snippetNode = itemNode.path("snippet");
                    String language = snippetNode.path("language").asText();

                    // Check for Korean captions
                    if ("ko".equals(language)) {
                        String subtitleId = itemNode.path("id").asText();
                        return "https://www.googleapis.com/youtube/v3/captions/" + subtitleId + "?tfmt=srt&key=" + apiKey;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadSubtitle(String subtitleUrl) throws Exception {
        if (subtitleUrl == null) {
            throw new Exception("Subtitle URL is null");
        }

        URL url = new URL(subtitleUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }
        in.close();
        connection.disconnect();

        return content.toString();
    }

    public String parseSrt(String srtContent) {
        String[] lines = srtContent.split("\n");
        StringBuilder parsedText = new StringBuilder();
        for (String line : lines) {
            if (!line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}") && !line.matches("\\d+")) {
                parsedText.append(line).append(" ");
            }
        }
        return parsedText.toString().trim();
    }
}
