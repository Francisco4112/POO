package com.grupo06.queriesTest;

import com.grupo06.playable.playlist.FavouriteList;
import com.grupo06.playable.playlist.Playlist;
import org.junit.jupiter.api.Test;

import java.util.*;
import com.grupo06.queries.QueryData;
import com.grupo06.queries.NumberOfPublicPlaylists;

import static org.junit.jupiter.api.Assertions.*;

public class NumberOfPublicPlaylistsTest {

    static class DummyQueryData implements QueryData {
        private final Map<String, Playlist> playlists;
        public DummyQueryData(Map<String, Playlist> playlists) { this.playlists = playlists; }
        @Override
        public Map<String, Playlist> getPlaylists() { return playlists; }
        @Override
        public Map<String, com.grupo06.user.User> getUsers() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Album> getAlbums() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Music> getMusics() { return new HashMap<>(); }
    }

    @Test
    void testNoPlaylists() {
        Map<String, Playlist> playlists = new HashMap<>();
        NumberOfPublicPlaylists query = new NumberOfPublicPlaylists();
        String result = query.execute(new DummyQueryData(playlists));
        assertEquals("0", result);
    }

    @Test
    void testOnePlaylist() {
        Map<String, Playlist> playlists = new HashMap<>();
        playlists.put("1", new FavouriteList());
        NumberOfPublicPlaylists query = new NumberOfPublicPlaylists();
        String result = query.execute(new DummyQueryData(playlists));
        assertEquals("1", result);
    }

    @Test
    void testMultiplePlaylists() {
        Map<String, Playlist> playlists = new HashMap<>();
        playlists.put("1", new FavouriteList());
        playlists.put("2", new FavouriteList());
        playlists.put("3", new FavouriteList());
        NumberOfPublicPlaylists query = new NumberOfPublicPlaylists();
        String result = query.execute(new DummyQueryData(playlists));
        assertEquals("3", result);
    }
}