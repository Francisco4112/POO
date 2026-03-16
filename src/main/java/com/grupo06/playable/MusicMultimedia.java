package com.grupo06.playable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a music track with multimedia (video) content.
 */
public class MusicMultimedia extends Music {
  private String videoPath;
  private String resolution;

  /** Creates an empty multimedia music. */
  public MusicMultimedia() {
    super();
    this.videoPath = "";
    this.resolution = "";
  }

  /**
   * Creates a multimedia music track.
   * 
   * @param name          Track name
   * @param lyrics        Track lyrics
   * @param musicNotes    Musical notes
   * @param genre         Genre
   * @param durationInSec Duration in seconds
   * @param videoPath     Path to video file
   * @param resolution    Video resolution
   */
  public MusicMultimedia(String name, String lyrics, List<String> musicNotes, String genre, int durationInSec,
      String videoPath, String resolution) {
    super(name, lyrics, musicNotes, genre, durationInSec);
    this.videoPath = videoPath;
    this.resolution = resolution;
  }

  /**
   * Creates a multimedia music from a regular one.
   * 
   * @param m          Music to base on
   * @param videoPath  Video path
   * @param resolution Video resolution
   */
  public MusicMultimedia(Music m, String videoPath, String resolution) {
    super(m);
    this.videoPath = videoPath;
    this.resolution = resolution;
  }

  /**
   * Copy constructor.
   * 
   * @param m MusicMultimedia to copy
   */
  public MusicMultimedia(MusicMultimedia m) {
    super(m);
    this.videoPath = m.videoPath;
    this.resolution = m.resolution;
  }

  /** @return Video file path */
  public String getVideoPath() {
    return this.videoPath;
  }

  /** @param videoPath New video path */
  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

  /** @return Video resolution */
  public String getResolution() {
    return this.resolution;
  }

  /** @param resolution New resolution */
  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  /** @return A copy of this multimedia music */
  public MusicMultimedia clone() {
    return new MusicMultimedia(this);
  }

  /** @return String with video info included */
  @Override
  public String toString() {
    return super.toString() + "\nMusicMultimedia [videoPath=" + this.videoPath + " | " + "resolution=" + this.resolution
        + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(videoPath, resolution);
  }

  /**
   * Compares by videoPath and resolution.
   * 
   * @param o Object to compare
   * @return true if equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MusicMultimedia music = (MusicMultimedia) o;
    return this.videoPath.equals(music.getVideoPath()) &&
        this.resolution.equals(music.getResolution());
  }
}
