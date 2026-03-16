package com.grupo06.playable.playlist;

import java.util.List;

import com.grupo06.playable.Music;

/**
 * A playlist containing music of a specific genre.
 */
public class GenrePlaylist extends Playlist {
    private String genre;

    /**
     * Creates an empty genre playlist.
     */
    public GenrePlaylist() {
        super();
        this.genre = "";
    }

    /**
     * Creates a genre playlist with a name, music list, and genre.
     *
     * @param name   the playlist name
     * @param musics the list of music
     * @param genre  the music genre
     */
    public GenrePlaylist(String name, List<Music> musics, String genre) {
        super(name, musics);
        this.genre = genre;
    }

    /**
     * Copy constructor.
     *
     * @param playlist the playlist to copy
     */
    public GenrePlaylist(GenrePlaylist playlist) {
        super(playlist);
        this.genre = playlist.getGenre();
    }

    /**
     * Returns the genre of the playlist.
     *
     * @return genre string
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the playlist.
     *
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Returns a string representation of the genre playlist.
     *
     * @return formatted string with name, musics, and genre
     */
    @Override
    public String toString() {
        return "GenrePlaylist: " +
                "\nname=" + getName() +
                "\nmusics=" + getMusics() +
                "\ngenre='" + getGenre();
    }

    /**
     * Creates a copy of this playlist.
     *
     * @return cloned GenrePlaylist
     */
    @Override
    public GenrePlaylist clone() {
        return new GenrePlaylist(this);
    }
}
