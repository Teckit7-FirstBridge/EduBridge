package com.ll.edubridge.domain.youtube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeService.class);

    @Value("${youtube.api.key}")
    private String apiKey;


    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public YouTubeService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/youtube/v3").build();
        this.objectMapper = objectMapper;

    }


    public Mono<List<PlaylistResult>> searchPlaylistsByName(String query, int maxResults) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", query)
                        .queryParam("maxResults", maxResults)
                        .queryParam("type", "playlist")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Playlist search response: {}", response)) // 응답 로깅
                .flatMapMany(this::extractPlaylistIds)
                .flatMap(this::getPlaylistVideos)
                .collectList()
                .doOnError(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        logger.error("Client error: {}", ex.getResponseBodyAsString());
                    } else if (ex.getStatusCode().is5xxServerError()) {
                        logger.error("Server error: {}", ex.getResponseBodyAsString());
                    }
                });
    }

    private Flux<String> extractPlaylistIds(String response) {
        logger.info("Extracting playlist IDs from response");
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("items");

            List<String> playlistIds = new ArrayList<>();
            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String playlistId = itemNode.path("id").path("playlistId").asText();
                    playlistIds.add(playlistId);
                }
            }

            return Flux.fromIterable(playlistIds);
        } catch (Exception e) {
            logger.error("Failed to extract playlist IDs: {}", e.getMessage());
            return Flux.error(e);
        }
    }

    public Mono<PlaylistResult> getPlaylistVideos(String playlistId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/playlistItems")
                        .queryParam("part", "snippet,contentDetails")
                        .queryParam("playlistId", playlistId)
                        .queryParam("maxResults", 50)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Playlist items response: {}", response)) // 응답 로깅
                .flatMap(response -> extractVideoLinks(playlistId, response))
                .doOnError(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        logger.error("Client error: {}", ex.getResponseBodyAsString());
                    } else if (ex.getStatusCode().is5xxServerError()) {
                        logger.error("Server error: {}", ex.getResponseBodyAsString());
                    }
                });
    }

    private Mono<PlaylistResult> extractVideoLinks(String playlistId, String response) {
        logger.info("Extracting video links from response for playlist ID: {}", playlistId);
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("items");

            List<VideoLink> videoLinks = new ArrayList<>();

            for (JsonNode itemNode : itemsNode) {
                try {
                    JsonNode contentDetailsNode = itemNode.path("contentDetails");
                    String videoId = contentDetailsNode.path("videoId").asText();

                    if (videoId != null && !videoId.isEmpty()) {
                        String videoLink = "https://www.youtube.com/watch?v=" + videoId;
                        videoLinks.add(new VideoLink(videoLink));
                    } else {
                        logger.warn("Video ID is missing for an item: {}", itemNode);
                    }
                } catch (Exception e) {
                    logger.error("Failed to extract video link from item: {}", itemNode, e);
                }
            }

            return Mono.just(new PlaylistResult(playlistId, videoLinks));
        } catch (Exception e) {
            logger.error("Failed to extract video links: {}", e.getMessage());
            return Mono.error(e);
        }
    }
}
