package com.grupo06.playableTest;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.playable.Music;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MusicTest {

  @Test
  public void testDefaultConstructor() {
    Music music = new Music();
    assertEquals("", music.getName());
    assertEquals("", music.getLyrics());
    assertTrue(music.getMusicNotes().isEmpty());
    assertEquals("", music.getGenre());
    assertEquals(0, music.getDurationInSec());
    assertEquals(0, music.getPlayCount());
  }

  @Test
  public void testParameterizedConstructor() {
    List<String> notes = new ArrayList<>();
    notes.add("C");
    notes.add("D");
    notes.add("E");

    Music music = new Music("Song 1", "Some lyrics", notes, "Pop", 180, 5);

    assertEquals("Song 1", music.getName());
    assertEquals("Some lyrics", music.getLyrics());
    assertEquals(notes, music.getMusicNotes());
    assertEquals("Pop", music.getGenre());
    assertEquals(180, music.getDurationInSec());
    assertEquals(5, music.getPlayCount());
  }

  @Test
  public void testCopyConstructor() {
    List<String> notes = new ArrayList<>();
    notes.add("C");
    notes.add("D");
    notes.add("E");

    Music original = new Music("Song 1", "Some lyrics", notes, "Pop", 180, 5);
    Music copy = new Music(original);

    assertEquals(original.getName(), copy.getName());
    assertEquals(original.getLyrics(), copy.getLyrics());
    assertEquals(original.getMusicNotes(), copy.getMusicNotes());
    assertEquals(original.getGenre(), copy.getGenre());
    assertEquals(original.getDurationInSec(), copy.getDurationInSec());
    assertEquals(original.getPlayCount(), copy.getPlayCount());

    assertNotSame(original.getMusicNotes(),
                  copy.getMusicNotes()); // Lista deve ser diferente
    for (int i = 0; i < original.getMusicNotes().size(); i++) {
      assertEquals(original.getMusicNotes().get(i),
                   copy.getMusicNotes().get(i)); // Conteúdo igual
    }
  }

  @Test
  public void testSettersAndGetters() {
    Music music = new Music();

    music.setName("Song 1");
    music.setLyrics("Some lyrics");
    List<String> notes = new ArrayList<>();
    notes.add("C");
    notes.add("D");
    music.setMusicNotes(notes);
    music.setGenre("Pop");
    music.setDurationInSec(200);
    music.setPlayCount(10);

    assertEquals("Song 1", music.getName());
    assertEquals("Some lyrics", music.getLyrics());
    assertEquals(notes, music.getMusicNotes());
    assertEquals("Pop", music.getGenre());
    assertEquals(200, music.getDurationInSec());
    assertEquals(10, music.getPlayCount());
  }

  @Test
  public void testIncrementPlayCount() {
    Music music = new Music();
    assertEquals(0, music.getPlayCount());

    music.incrementPlayCount();
    assertEquals(1, music.getPlayCount());

    music.incrementPlayCount();
    assertEquals(2, music.getPlayCount());
  }

  @Test
  public void testToString() {
    List<String> notes = new ArrayList<>();
    notes.add("C");
    notes.add("D");
    notes.add("E");

    Music music = new Music("Song 1", "Some lyrics", notes, "Pop", 180, 5);

    String expected = "\n=== Music ===\n"
                      + "Name: Song 1\n"
                      + "Genre: Pop\n"
                      + "Duration: 3:00\n"
                      + "Play count: 5\n"
                      + "\n=== Lyrics ===\n"
                      + "Some lyrics"
                      + "\n\n=== Musical notes ===\n"
                      + "1. C\n"
                      + "2. D\n"
                      + "3. E\n";

    assertEquals(expected, music.toString());
  }

  @Test
  public void testClone() {
    List<String> notes = new ArrayList<>();
    notes.add("C");
    notes.add("D");
    notes.add("E");

    Music original = new Music("Song 1", "Some lyrics", notes, "Pop", 180, 5);
    Music clone = original.clone();

    assertNotSame(original, clone);
    assertNotSame(original.getMusicNotes(),
                  clone.getMusicNotes()); // Ensure shallow copy
  }

  @Test
  public void testEquals() {
    List<String> notes1 = new ArrayList<>();
    notes1.add("C");
    notes1.add("D");

    List<String> notes2 = new ArrayList<>();
    notes2.add("C");
    notes2.add("D");

    Music music1 = new Music("Song 1", "Some lyrics", notes1, "Pop", 180, 5);
    Music music2 = new Music("Song 1", "Some lyrics", notes2, "Pop", 180, 5);

    assertEquals(music1, music2);
  }
}
