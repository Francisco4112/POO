package com.grupo06.playable;

import java.util.List;
import java.util.Objects;

/**
 * Represents an explicit music track with an age restriction.
 */
public class MusicExplicit extends Music {
  private int ageRestriction;

  /** Creates an empty explicit music. */
  public MusicExplicit() {
    super();
    this.ageRestriction = 0;
  }

  /**
   * Creates a new explicit music track.
   * 
   * @param name           Track name
   * @param lyrics         Track lyrics
   * @param musicNotes     Musical notes
   * @param genre          Genre
   * @param durationInSec  Duration in seconds
   * @param ageRestriction Minimum age required
   */
  public MusicExplicit(String name, String lyrics, List<String> musicNotes, String genre, int durationInSec,
      int ageRestriction) {
    super(name, lyrics, musicNotes, genre, durationInSec);
    this.ageRestriction = ageRestriction;
  }

  /**
   * Creates an explicit music from a regular one.
   * 
   * @param m              Music to base on
   * @param ageRestriction Age restriction
   */
  public MusicExplicit(Music m, int ageRestriction) {
    super(m);
    this.ageRestriction = ageRestriction;
  }

  /**
   * Copy constructor.
   * 
   * @param m MusicExplicit to copy
   */
  public MusicExplicit(MusicExplicit m) {
    super(m);
  }

  /** @return Age restriction */
  public int getAgeRestriction() {
    return this.ageRestriction;
  }

  /** @param ageRestriction New age restriction */
  public void setAgeRestriction(int ageRestriction) {
    this.ageRestriction = ageRestriction;
  }

  /** @return A copy of this explicit music */
  public MusicExplicit clone() {
    return new MusicExplicit(this);
  }

  /** @return Textual representation including age restriction */
  @Override
  public String toString() {
    return super.toString() + "\nMusicExplicit [ageRestriction=" + ageRestriction + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(ageRestriction);
  }


  /**
   * Compares by age restriction.
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
    MusicExplicit music = (MusicExplicit) o;
    return ageRestriction == music.getAgeRestriction();
  }
}
