package com.ll.edubridge.domain.youtube;

import java.util.List;

public class PlaylistResult {
    private String playlistId;
    private List<VideoLink> videoLinks;

    public PlaylistResult() {
    }

    public PlaylistResult(String playlistId, List<VideoLink> videoLinks) {
        this.playlistId = playlistId;
        this.videoLinks = videoLinks;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public List<VideoLink> getVideoLinks() {
        return videoLinks;
    }

    public void setVideoLinks(List<VideoLink> videoLinks) {
        this.videoLinks = videoLinks;
    }
}
