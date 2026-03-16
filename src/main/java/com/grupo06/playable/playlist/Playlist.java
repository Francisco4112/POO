package com.grupo06.playable.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.grupo06.playable.Music;
import com.grupo06.playable.Playable;

import java.io.Serializable;

/**
 * Abstract class representing a playlist composed of music tracks.
 */
public abstract class Playlist implements Playable, Serializable {
    private String name;
    private List<Music> musics;

    /**
     * Creates an empty playlist with no name and no music.
     */
    public Playlist() {
        this.name = "";
        this.musics = new ArrayList<>();
    }

    /**
     * Creates a playlist with the given name and list of music.
     *
     * @param name   the name of the playlist
     * @param musics the list of music tracks
     */
    public Playlist(String name, List<Music> musics) {
        this.name = name;
        this.musics = new ArrayList<>(musics);
    }

    /**
     * Copy constructor for a playlist.
     *
     * @param playlist the playlist to copy
     */
    public Playlist(Playlist playlist) {
        this.name = playlist.getName();
        this.musics = new ArrayList<>();
        for (Music m : playlist.musics) {
            this.musics.add(m);
        }
    }

    /**
     * Returns the playlist name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the playlist name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a copy of the list of musics.
     *
     * @return list of music tracks
     */
    public ArrayList<Music> getMusics() {
        ArrayList<Music> copy = new ArrayList<>();
        for (Music music : this.musics) {
            copy.add(music);
        }
        return copy;
    }

    /**
     * Sets the list of musics in the playlist.
     *
     * @param musics the list to set
     */
    public void setMusics(List<Music> musics) {
        List<Music> copy = new ArrayList<>();
        for (Music music : musics) {
            copy.add(music);
        }
        this.musics = copy;
    }

    /**
     * Adds a music to the playlist.
     *
     * @param music the music to add
     */
    public void addMusic(Music music) {
        this.musics.add(music);
    }

    /**
     * Removes a music from the playlist.
     *
     * @param music the music to remove
     */
    public void removeMusic(Music music) {
        this.musics.remove(music);
    }

    /**
     * Creates a clone of the playlist.
     *
     * @return a copy of the playlist
     */
    @Override
    public abstract Playlist clone();

    /**
     * Returns a string representation of the playlist.
     *
     * @return string with name and music titles
     */
    @Override
    public String toString() {
        String musicTitles = getMusics().stream()
                .map(Music::getName)
                .collect(Collectors.joining(", "));

        return "Playlist:\nname=" + getName() + "\nmusics=" + musicTitles;
    }
}
