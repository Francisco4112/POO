package com.grupo06.playable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single music track with lyrics, notes, and metadata.
 */
public class Music implements Playable, Serializable {
    private String name;
    private String lyrics;
    private List<String> musicNotes;
    private String genre;
    private int durationInSec;
    private int playCount;

    /** Creates an empty music track. */
    public Music() {
        this.name = "";
        this.lyrics = "";
        this.musicNotes = new ArrayList<>();
        this.genre = "";
        this.durationInSec = 0;
        this.playCount = 0;
    }

    /**
     * Creates a music track without initial play count.
     * 
     * @param name          Track name
     * @param lyrics        Track lyrics
     * @param musicNotes    Musical notes
     * @param genre         Music genre
     * @param durationInSec Duration in seconds
     */
    public Music(String name, String lyrics, List<String> musicNotes, String genre, int durationInSec) {
        this.name = name;
        this.lyrics = lyrics;
        this.musicNotes = new ArrayList<>(musicNotes);
        this.genre = genre;
        this.durationInSec = durationInSec;
        this.playCount = 0;
    }

    /**
     * Creates a music track with play count.
     * 
     * @param name          Track name
     * @param lyrics        Track lyrics
     * @param musicNotes    Musical notes
     * @param genre         Music genre
     * @param durationInSec Duration in seconds
     * @param playCount     Initial play count
     */
    public Music(String name, String lyrics, List<String> musicNotes, String genre, int durationInSec,
            int playCount) {
        this.name = name;
        this.lyrics = lyrics;
        this.musicNotes = new ArrayList<>(musicNotes);
        this.genre = genre;
        this.durationInSec = durationInSec;
        this.playCount = playCount;
    }

    /**
     * Copy constructor.
     * 
     * @param music Music to copy
     */
    public Music(Music music) {
        this.name = music.name;
        this.lyrics = music.lyrics;
        this.musicNotes = new ArrayList<>(music.musicNotes);
        this.genre = music.genre;
        this.durationInSec = music.durationInSec;
        this.playCount = music.playCount;
    }

    /** @return Track name */
    public String getName() {
        return name;
    }

    /** @param name New track name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return Track lyrics */
    public String getLyrics() {
        return lyrics;
    }

    /** @param lyrics New lyrics */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /** @return Musical notes list */
    public List<String> getMusicNotes() {
        return musicNotes;
    }

    /** @param musicNotes New musical notes */
    public void setMusicNotes(List<String> musicNotes) {
        this.musicNotes = musicNotes;
    }

    /** @return Music genre */
    public String getGenre() {
        return genre;
    }

    /** @param genre New genre */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /** @return Duration in seconds */
    public int getDurationInSec() {
        return durationInSec;
    }

    /** @param durationInSec New duration */
    public void setDurationInSec(int durationInSec) {
        this.durationInSec = durationInSec;
    }

    /** @return Play count */
    public int getPlayCount() {
        return playCount;
    }

    /** @param playCount New play count */
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    /** Increments the play count by one. */
    public void incrementPlayCount() {
        this.playCount++;
    }

    /** @return A copy of this music */
    @Override
    public Music clone() {
        return new Music(this);
    }

    /** @return Textual representation of the music */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== Music ===\n")
                .append("Name: ").append(this.name).append("\n")
                .append("Genre: ").append(this.genre).append("\n")
                .append("Duration: ").append(formatDuration(this.durationInSec)).append("\n")
                .append("Play count: ").append(this.playCount).append("\n")
                .append("\n=== Lyrics ===\n")
                .append(this.lyrics.isEmpty() ? "[Lyrics unavailable]" : this.lyrics)
                .append("\n\n=== Musical notes ===\n");

        if (this.musicNotes.isEmpty()) {
            sb.append("[No musical notes]");
        } else {
            for (int i = 0; i < this.musicNotes.size(); i++) {
                sb.append(i + 1).append(". ").append(this.musicNotes.get(i)).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Formats seconds into mm:ss.
     * 
     * @param totalSeconds Duration in seconds
     * @return Formatted duration
     */
    private String formatDuration(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lyrics, musicNotes, genre, durationInSec, playCount);
    }

    /**
     * Compares music tracks by all fields.
     * 
     * @param obj Music to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Music))
            return false;
        Music music = (Music) obj;
        return this.name.equals(music.name) &&
                this.lyrics.equals(music.lyrics) &&
                this.musicNotes.equals(music.musicNotes) &&
                this.genre.equals(music.genre) &&
                this.durationInSec == music.durationInSec &&
                this.playCount == music.playCount;
    }
}
