package com.grupo06.queriesTest;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.playable.Music;
import java.util.*;
import org.junit.jupiter.api.Test;
import com.grupo06.queries.MostListenedGenre;
import com.grupo06.queries.QueryData;

public class MostListenedGenreTest {

  // Dummy QueryData implementation for testing
  static class DummyQueryData implements QueryData {
    private final Map<String, Music> musics;
    public DummyQueryData(Map<String, Music> musics) { this.musics = musics; }
    @Override
    public Map<String, Music> getMusics() {
      return musics;
    }
    @Override
    public Map<String, com.grupo06.user.User> getUsers() {
      return new HashMap<>();
    }
    @Override
    public Map<String, com.grupo06.playable.playlist.Playlist> getPlaylists() {
      return new HashMap<>();
    }
    @Override
    public Map<String, com.grupo06.playable.Album> getAlbums() {
      return new HashMap<>();
    }
  }

  @Test
  void testSingleGenre() {
    Map<String, Music> musics = new HashMap<>();
    Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
    m1.setPlayCount(10);
    musics.put("1", m1);

    MostListenedGenre query = new MostListenedGenre();
    String result = query.execute(new DummyQueryData(musics));
    assertEquals("Pop", result);
  }

  @Test
  void testMostListenedGenre() {
    Map<String, Music> musics = new HashMap<>();
    Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
    m1.setPlayCount(10);
    Music m2 = new Music("Song2", "Lyrics", new ArrayList<>(), "Rock", 200);
    m2.setPlayCount(15);
    Music m3 = new Music("Song3", "Lyrics", new ArrayList<>(), "Pop", 150);
    m3.setPlayCount(5);
    musics.put("1", m1);
    musics.put("2", m2);
    musics.put("3", m3);

    MostListenedGenre query = new MostListenedGenre();
    String result = query.execute(new DummyQueryData(musics));
    assertEquals("Pop",
                 result); // Rock: 15, Pop: 15 (empate, mas Pop aparece antes)
  }

  @Test
  void testTieReturnsFirst() {
    Map<String, Music> musics = new LinkedHashMap<>();
    Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
    m1.setPlayCount(10);
    Music m2 = new Music("Song2", "Lyrics", new ArrayList<>(), "Rock", 200);
    m2.setPlayCount(10);
    musics.put("1", m1);
    musics.put("2", m2);

    MostListenedGenre query = new MostListenedGenre();
    String result = query.execute(new DummyQueryData(musics));
    // No critério de desempate, retorna o primeiro encontrado (Pop)
    assertEquals("Pop", result);
  }

  @Test
  void testEmptyMapThrowsNPE() {
    MostListenedGenre query = new MostListenedGenre();
    assertThrows(NullPointerException.class,
                 () -> query.execute(new DummyQueryData(new HashMap<>())));
  }
}