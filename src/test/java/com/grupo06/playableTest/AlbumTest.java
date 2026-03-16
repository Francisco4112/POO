package com.grupo06.playableTest;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.playable.Album;
import com.grupo06.playable.Music;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AlbumTest {

  @Test
  public void testEmptyConstructor() {
    Album album = new Album();
    assertEquals("", album.getName());
    assertEquals("", album.getMusician());
    assertEquals("", album.getPublisherName());
    assertTrue(album.getMusics().isEmpty());
  }

  @Test
  public void testParameterizedConstructor() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    Album album =
        new Album("Album Name", "Musician Name", "Publisher Name", musics);

    assertEquals("Album Name", album.getName());
    assertEquals("Musician Name", album.getMusician());
    assertEquals("Publisher Name", album.getPublisherName());
    assertEquals(1, album.getMusics().size());
  }

  @Test
  public void testCopyConstructor() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    Album original =
        new Album("Album Name", "Musician Name", "Publisher Name", musics);
    Album copy = new Album(original);

    assertEquals(original, copy);
    assertNotSame(original.getMusics(),
                  copy.getMusics()); // Deep copy verification
  }

  @Test
  public void testAddMusic() {
    Album album = new Album();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    album.addMusic(music);
    assertEquals(1, album.getMusics().size());
    assertTrue(album.getMusics().contains(music));
  }

  @Test
  public void testAddDuplicateMusic() {
    Album album = new Album();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    album.addMusic(music);
    album.addMusic(music); // Attempt to add duplicate
    assertEquals(1, album.getMusics().size());
  }

  @Test
  public void testRemoveMusic() {
    Album album = new Album();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    album.addMusic(music);
    album.removeMusic(music);
    assertTrue(album.getMusics().isEmpty());
  }

  @Test
  public void testRemoveNonExistentMusic() {
    Album album = new Album();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    album.removeMusic(music); // Attempt to remove music not in the album
    assertTrue(album.getMusics().isEmpty());
  }

  @Test
  public void testSetMusics() {
    Album album = new Album();
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    musics.add(
        new Music("Song 2", "Lyrics 2", new ArrayList<>(), "Genre 2", 200));

    album.setMusics(musics);
    assertEquals(2, album.getMusics().size());
    assertNotSame(musics, album.getMusics()); // Ensure defensive copy
  }

  @Test
  public void testEquals() {
    Album album1 = new Album("Album Name", "Musician Name", "Publisher Name");
    Album album2 = new Album("Album Name", "Musician Name", "Publisher Name");
    Album album3 =
        new Album("Different Album", "Musician Name", "Publisher Name");

    assertEquals(album1, album2);
    assertNotEquals(album1, album3);
  }

  @Test
  public void testClone() {
    Album album = new Album("Album Name", "Musician Name", "Publisher Name");
    album.addMusic(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));

    Album clonedAlbum = album.clone();
    assertEquals(album, clonedAlbum);
    assertNotSame(album.getMusics(),
                  clonedAlbum.getMusics()); // Deep copy verification
  }

  @Test
  public void testToString() {
    Album album = new Album("Album Name", "Musician Name", "Publisher Name");
    album.addMusic(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));

    String result = album.toString();
    assertTrue(result.contains("Album Name"));
    assertTrue(result.contains("Musician Name"));
    assertTrue(result.contains("Publisher Name"));
    assertTrue(result.contains("Song 1"));
  }
}