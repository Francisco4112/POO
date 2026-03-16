package com.grupo06.playable;

import java.util.List;
import java.util.Objects;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a music album containing multiple tracks.
 */
public class Album implements Playable, Serializable {
  private String name;
  private String musician;
  private String publisherName;
  private List<Music> musics;

  /** Creates an empty album. */
  public Album() {
    this.name = "";
    this.musician = "";
    this.publisherName = "";
    this.musics = new ArrayList<>();
  }

  /**
   * Creates an album with basic info.
   * 
   * @param name          Album name
   * @param musician      Musician's name
   * @param publisherName Publisher's name
   */
  public Album(String name, String musician, String publisherName) {
    this.name = name;
    this.musician = musician;
    this.publisherName = publisherName;
    this.musics = new ArrayList<>();
  }

  /**
   * Creates an album with music list.
   * 
   * @param name          Album name
   * @param musician      Musician's name
   * @param publisherName Publisher's name
   * @param musics        List of musics
   */
  public Album(String name, String musician, String publisherName, List<Music> musics) {
    this.name = name;
    this.musician = musician;
    this.publisherName = publisherName;
    this.musics = new ArrayList<>(musics);
  }

  /**
   * Copy constructor (deep clone of musics).
   * 
   * @param umAlbum Album to copy
   */
  public Album(Album umAlbum) {
    this.name = umAlbum.name;
    this.musician = umAlbum.musician;
    this.publisherName = umAlbum.publisherName;
    this.musics = new ArrayList<>();
    for (Music m : umAlbum.musics) {
      this.musics.add(m.clone());
    }
  }

  /** @return Album name */
  public String getName() {
    return this.name;
  }

  /** @param name New album name */
  public void setName(String name) {
    this.name = name;
  }

  /** @return Musician's name */
  public String getMusician() {
    return musician;
  }

  /** @param musician New musician name */
  public void setMusician(String musician) {
    this.musician = musician;
  }

  /** @return Publisher's name */
  public String getPublisherName() {
    return publisherName;
  }

  /** @param publisherName New publisher name */
  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }

  /** @return List of musics (copied) */
  public List<Music> getMusics() {
    return new ArrayList<>(this.musics);
  }

  /**
   * Sets the list of musics (shallow copy).
   * 
   * @param musics New list of musics
   */
  public void setMusics(List<Music> musics) {
    this.musics = new ArrayList<>(musics);
  }

  /**
   * Adds a music if not null or duplicate.
   * 
   * @param music Music to add
   */
  public void addMusic(Music music) {
    if (music == null || this.musics.contains(music))
      return;
    this.musics.add(music);
  }

  /**
   * Removes a music by object.
   * 
   * @param music Music to remove
   */
  public void removeMusic(Music music) {
    if (music == null)
      return;
    this.musics.remove(music);
  }

  /**
   * Removes a music by name.
   * 
   * @param MusicName Name of the music to remove
   */
  public void removeMusic(String MusicName) {
    for (int i = 0; i < this.musics.size(); i++) {
      Music m = this.musics.get(i);
      if (m.getName().equals(MusicName)) {
        this.musics.remove(i);
        break;
      }
    }
  }

  /** @return Deep copy of this album */
  @Override
  public Album clone() {
    return new Album(this);
  }

  /** @return Text representation of the album */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n=== Album ===\n")
        .append("Name: ").append(this.name).append("\n")
        .append("Musician: ").append(this.musician).append("\n")
        .append("Publisher: ").append(this.publisherName).append("\n")
        .append("Musics (").append(this.musics.size()).append("):\n");

    if (this.musics.isEmpty()) {
      sb.append("No musics in the album\n");
    } else {
      for (int i = 0; i < this.musics.size(); i++) {
        sb.append("  ").append(i + 1).append(". ")
            .append(this.musics.get(i).getName()).append("\n");
      }
    }

    return sb.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, musician, publisherName);
  }

  /**
   * Compares albums by name, musician, and publisher.
   * 
   * @param obj Album to compare
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Album))
      return false;
    Album album = (Album) obj;
    return this.name.equals(album.name) &&
        this.musician.equals(album.musician) &&
        this.publisherName.equals(album.publisherName);
  }
}
