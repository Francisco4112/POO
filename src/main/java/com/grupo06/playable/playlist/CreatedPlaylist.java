package com.grupo06.playable.playlist;

import java.util.List;

import com.grupo06.playable.Music;

/**
 * Playlist created by the user, with a privacy setting.
 */
public class CreatedPlaylist extends Playlist {
    private boolean isPrivate;

    /**
     * Creates an empty public playlist.
     */
    public CreatedPlaylist() {
        super();
        this.isPrivate = false;
    }

    /**
     * Creates a playlist with name, privacy, and musics.
     *
     * @param name      the playlist name
     * @param isPrivate privacy status
     * @param musics    list of musics
     */
    public CreatedPlaylist(String name, Boolean isPrivate, List<Music> musics) {
        super(name, musics);
        this.isPrivate = isPrivate;
    }

    /**
     * Copy constructor.
     *
     * @param playlist playlist to copy
     */
    public CreatedPlaylist(CreatedPlaylist playlist) {
        super(playlist);
        this.isPrivate = playlist.isPrivate;
    }

    /**
     * Returns if the playlist is private.
     *
     * @return true if private, false otherwise
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Sets the playlist privacy.
     *
     * @param isPrivate true to set private, false otherwise
     */
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * String representation including privacy.
     *
     * @return string with playlist info and privacy
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nisPrivate = ").append(isPrivate);
        return sb.toString();
    }

    /**
     * Clones this playlist.
     *
     * @return copy of the playlist
     */
    @Override
    public CreatedPlaylist clone() {
        return new CreatedPlaylist(this);
    }
}
