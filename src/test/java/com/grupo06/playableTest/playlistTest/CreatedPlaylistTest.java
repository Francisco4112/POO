package com.grupo06.playableTest.playlistTest;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.playable.Music;
import com.grupo06.playable.playlist.CreatedPlaylist;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CreatedPlaylistTest {

  @Test
  public void testDefaultConstructor() {
    CreatedPlaylist playlist = new CreatedPlaylist();
    assertEquals("", playlist.getName());
    assertTrue(playlist.getMusics().isEmpty());
    assertFalse(playlist.isPrivate());
  }

  @Test
  public void testParameterizedConstructor() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    CreatedPlaylist playlist = new CreatedPlaylist("My Playlist", true, musics);

    assertEquals("My Playlist", playlist.getName());
    assertEquals(1, playlist.getMusics().size());
    assertTrue(playlist.isPrivate());
  }

  @Test
  public void testCopyConstructor() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    CreatedPlaylist original = new CreatedPlaylist("My Playlist", true, musics);
    CreatedPlaylist copy = new CreatedPlaylist(original);

    assertNotSame(original, copy);
    assertNotSame(original.getMusics(), copy.getMusics());
    for (int i = 0; i < original.getMusics().size(); i++) {
      assertSame(original.getMusics().get(i), copy.getMusics().get(i));
    }
  }

  @Test
  public void testSetPrivate() {
    CreatedPlaylist playlist = new CreatedPlaylist();
    assertFalse(playlist.isPrivate());

    playlist.setPrivate(true);
    assertTrue(playlist.isPrivate());
  }

  @Test
  public void testAddMusic() {
    CreatedPlaylist playlist = new CreatedPlaylist();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    playlist.addMusic(music);
    assertEquals(1, playlist.getMusics().size());
    assertTrue(playlist.getMusics().contains(music));
  }

  @Test
  public void testRemoveMusic() {
    CreatedPlaylist playlist = new CreatedPlaylist();
    Music music =
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180);

    playlist.addMusic(music);
    playlist.removeMusic(music);
    assertTrue(playlist.getMusics().isEmpty());
  }

  @Test
  public void testClone() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    CreatedPlaylist playlist = new CreatedPlaylist("My Playlist", true, musics);

    CreatedPlaylist clonedPlaylist = playlist.clone();
    assertNotSame(playlist, clonedPlaylist);
    assertNotSame(playlist.getMusics(), clonedPlaylist.getMusics());
    for (int i = 0; i < playlist.getMusics().size(); i++) {
      assertSame(playlist.getMusics().get(i),
                 clonedPlaylist.getMusics().get(i));
    }
  }

  @Test
  public void testToString() {
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    CreatedPlaylist playlist = new CreatedPlaylist("My Playlist", true, musics);

    String result = playlist.toString();
    assertTrue(result.contains("My Playlist"));
    assertTrue(result.contains("true")); // isPrivate
    assertTrue(result.contains("Song 1"));
  }
}