package com.grupo06.queriesTest;

import com.grupo06.playable.Album;
import com.grupo06.playable.Music;
import org.junit.jupiter.api.Test;

import java.util.*;
import com.grupo06.queries.QueryData;
import com.grupo06.queries.MostListenedMusician;

import static org.junit.jupiter.api.Assertions.*;

class MostListenedMusicianTest {

    static class DummyQueryData implements QueryData {
        private final Map<String, Album> albums;
        public DummyQueryData(Map<String, Album> albums) { this.albums = albums; }
        @Override
        public Map<String, Album> getAlbums() { return albums; }
        @Override
        public Map<String, com.grupo06.user.User> getUsers() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.playlist.Playlist> getPlaylists() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Music> getMusics() { return new HashMap<>(); }
    }

    @Test
    void testSingleMusician() {
        List<Music> musics = new ArrayList<>();
        Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
        m1.setPlayCount(10);
        musics.add(m1);
        Album album = new Album("Album1", "Artist1", "Publisher", musics);

        Map<String, Album> albums = new HashMap<>();
        albums.put("1", album);

        MostListenedMusician query = new MostListenedMusician();
        String result = query.execute(new DummyQueryData(albums));
        assertEquals("Artist1", result);
    }

    @Test
    void testMostListenedMusician() {
        List<Music> musics1 = new ArrayList<>();
        Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
        m1.setPlayCount(10);
        musics1.add(m1);
        Album album1 = new Album("Album1", "Artist1", "Publisher", musics1);

        List<Music> musics2 = new ArrayList<>();
        Music m2 = new Music("Song2", "Lyrics", new ArrayList<>(), "Rock", 200);
        m2.setPlayCount(20);
        musics2.add(m2);
        Album album2 = new Album("Album2", "Artist2", "Publisher", musics2);

        List<Music> musics3 = new ArrayList<>();
        Music m3 = new Music("Song3", "Lyrics", new ArrayList<>(), "Pop", 150);
        m3.setPlayCount(5);
        musics3.add(m3);
        Album album3 = new Album("Album3", "Artist1", "Publisher", musics3);

        Map<String, Album> albums = new HashMap<>();
        albums.put("1", album1);
        albums.put("2", album2);
        albums.put("3", album3);

        MostListenedMusician query = new MostListenedMusician();
        String result = query.execute(new DummyQueryData(albums));
        // Artist1: 10 + 5 = 15, Artist2: 20
        assertEquals("Artist2", result);
    }

    @Test
    void testTieReturnsFirst() {
        List<Music> musics1 = new ArrayList<>();
        Music m1 = new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180);
        m1.setPlayCount(10);
        musics1.add(m1);
        Album album1 = new Album("Album1", "Artist1", "Publisher", musics1);

        List<Music> musics2 = new ArrayList<>();
        Music m2 = new Music("Song2", "Lyrics", new ArrayList<>(), "Rock", 200);
        m2.setPlayCount(10);
        musics2.add(m2);
        Album album2 = new Album("Album2", "Artist2", "Publisher", musics2);

        Map<String, Album> albums = new LinkedHashMap<>();
        albums.put("1", album1);
        albums.put("2", album2);

        MostListenedMusician query = new MostListenedMusician();
        String result = query.execute(new DummyQueryData(albums));
        // No critério de desempate, retorna o primeiro encontrado (Artist1)
        assertEquals("Artist1", result);
    }

    @Test
    void testEmptyMapReturnsEmptyString() {
        MostListenedMusician query = new MostListenedMusician();
        String result = query.execute(new DummyQueryData(new HashMap<>()));
        assertEquals("", result);
    }
}