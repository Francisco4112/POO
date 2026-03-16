package com.grupo06.playableTest.playlistTest;

import com.grupo06.playable.playlist.GenrePlaylist;
import com.grupo06.playable.Music;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenrePlaylistTest {

    @Test
    public void testEmptyConstructor() {
        GenrePlaylist playlist = new GenrePlaylist();
        assertEquals("", playlist.getName());
        assertTrue(playlist.getMusics().isEmpty());
        assertEquals("", playlist.getGenre());
    }

    @Test
    public void testParameterizedConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        GenrePlaylist playlist = new GenrePlaylist("Pop Hits", musics, "Pop");

        assertEquals("Pop Hits", playlist.getName());
        assertEquals(1, playlist.getMusics().size());
        assertEquals("Song 1", playlist.getMusics().get(0).getName());
        assertEquals("Pop", playlist.getGenre());
    }

    @Test
    public void testCopyConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        GenrePlaylist original = new GenrePlaylist("Pop Hits", musics, "Pop");
        GenrePlaylist copy = new GenrePlaylist(original);
        assertNotSame(original.getMusics(), copy.getMusics());
        assertEquals(original.getGenre(), copy.getGenre());
    }

    @Test
    public void testSetGenre() {
        GenrePlaylist playlist = new GenrePlaylist();
        playlist.setGenre("Rock");
        assertEquals("Rock", playlist.getGenre());
    }

    @Test
    public void testClone() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        GenrePlaylist playlist = new GenrePlaylist("Pop Hits", musics, "Pop");
        GenrePlaylist cloned = playlist.clone();

        assertNotSame(playlist, cloned);
        assertNotSame(playlist.getMusics(), cloned.getMusics());
        assertEquals(playlist.getGenre(), cloned.getGenre());
    }

    @Test
    public void testToString() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        GenrePlaylist playlist = new GenrePlaylist("Pop Hits", musics, "Pop");

        String str = playlist.toString();
        assertTrue(str.contains("GenrePlaylist:"));
        assertTrue(str.contains("name=Pop Hits"));
        assertTrue(str.contains("Song 1"));
        assertTrue(str.contains("genre='Pop"));
    }
}