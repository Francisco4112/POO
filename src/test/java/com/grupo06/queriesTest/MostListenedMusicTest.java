package com.grupo06.queriesTest;

import com.grupo06.playable.Music;
import org.junit.jupiter.api.Test;

import java.util.*;
import com.grupo06.queries.QueryData;
import com.grupo06.queries.MostListenedMusic;

import static org.junit.jupiter.api.Assertions.*;

public class MostListenedMusicTest {

    static class DummyQueryData implements QueryData {
        private final Map<String, Music> musics;
        public DummyQueryData(Map<String, Music> musics) { this.musics = musics; }
        @Override
        public Map<String, Music> getMusics() { return musics; }
        @Override
        public Map<String, com.grupo06.user.User> getUsers() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.playlist.Playlist> getPlaylists() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Album> getAlbums() { return new HashMap<>(); }
    }

    @Test
    void testMostListenedMusic() {
        Map<String, Music> musics = new HashMap<>();
        Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
        m1.setPlayCount(10);
        Music m2 = new Music("Song2", "Lyrics", new ArrayList<>(), "Rock", 200);
        m2.setPlayCount(15);
        musics.put("1", m1);
        musics.put("2", m2);

        MostListenedMusic query = new MostListenedMusic();
        String result = query.execute(new DummyQueryData(musics));
        assertEquals("Song2", result);
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

        MostListenedMusic query = new MostListenedMusic();
        String result = query.execute(new DummyQueryData(musics));
        // No critério de desempate, retorna o primeiro encontrado (Song1)
        assertEquals("Song1", result);
    }

    @Test
    void testEmptyMapThrowsNPE() {
        MostListenedMusic query = new MostListenedMusic();
        assertThrows(NullPointerException.class, () -> query.execute(new DummyQueryData(new HashMap<>())));
    }
}