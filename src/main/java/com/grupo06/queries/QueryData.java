package com.grupo06.queries;

import java.util.Map;

import com.grupo06.playable.Album;
import com.grupo06.playable.Music;
import com.grupo06.playable.playlist.Playlist;
import com.grupo06.user.User;

/**
 * Provides access to application data such as users, playlists, albums, and
 * musics.
 */
public interface QueryData {

    /**
     * Returns a map of all users.
     * 
     * @return map of users by ID
     */
    public Map<String, User> getUsers();

    /**
     * Returns a map of all playlists.
     * 
     * @return map of playlists by ID
     */
    public Map<String, Playlist> getPlaylists();

    /**
     * Returns a map of all albums.
     * 
     * @return map of albums by ID
     */
    public Map<String, Album> getAlbums();

    /**
     * Returns a map of all musics.
     * 
     * @return map of musics by ID
     */
    public Map<String, Music> getMusics();
}
