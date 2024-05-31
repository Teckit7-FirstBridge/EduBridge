package com.ll.edubridge.domain.youtube;

import java.util.List;

public class YouTubeResponse {
    private List<PlaylistResult> playlists;

    public YouTubeResponse() {
    }

    public YouTubeResponse(List<PlaylistResult> playlists) {
        this.playlists = playlists;
    }

    public List<PlaylistResult> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistResult> playlists) {
        this.playlists = playlists;
    }
}
