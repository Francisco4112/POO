package com.grupo06.playableTest;

import com.grupo06.playable.Music;
import com.grupo06.playable.MusicMultimedia;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MusicMultimediaTest {

    @Test
    public void testEmptyConstructor() {
        MusicMultimedia music = new MusicMultimedia();
        assertEquals("", music.getName());
        assertEquals("", music.getLyrics());
        assertEquals("", music.getGenre());
        assertEquals(0, music.getDurationInSec());
        assertEquals("", music.getVideoPath());
        assertEquals("", music.getResolution());
    }

    @Test
    public void testParameterizedConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        notes.add("D");
        MusicMultimedia music = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/path/video.mp4", "1080p");

        assertEquals("Song", music.getName());
        assertEquals("Lyrics", music.getLyrics());
        assertEquals(notes, music.getMusicNotes());
        assertEquals("Pop", music.getGenre());
        assertEquals(180, music.getDurationInSec());
        assertEquals("/path/video.mp4", music.getVideoPath());
        assertEquals("1080p", music.getResolution());
    }

    @Test
    public void testCopyConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicMultimedia original = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/path/video.mp4", "720p");
        MusicMultimedia copy = new MusicMultimedia(original);

        assertNotSame(original, copy);
    }

    @Test
    public void testMusicConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        Music m = new Music("Song", "Lyrics", notes, "Pop", 180);
        MusicMultimedia multimedia = new MusicMultimedia(m, "/path/video.mp4", "4K");

        assertEquals("Song", multimedia.getName());
        assertEquals("Lyrics", multimedia.getLyrics());
        assertEquals(notes, multimedia.getMusicNotes());
        assertEquals("Pop", multimedia.getGenre());
        assertEquals(180, multimedia.getDurationInSec());
        assertEquals("/path/video.mp4", multimedia.getVideoPath());
        assertEquals("4K", multimedia.getResolution());
    }

    @Test
    public void testSettersAndGetters() {
        MusicMultimedia music = new MusicMultimedia();
        music.setVideoPath("/video/path");
        music.setResolution("8K");
        assertEquals("/video/path", music.getVideoPath());
        assertEquals("8K", music.getResolution());
    }

    @Test
    public void testClone() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicMultimedia original = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/path/video.mp4", "720p");
        MusicMultimedia clone = original.clone();

        assertNotSame(original, clone);
    }

    @Test
    public void testToString() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicMultimedia music = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/path/video.mp4", "1080p");

        String str = music.toString();
        assertTrue(str.contains("Song"));
        assertTrue(str.contains("Lyrics"));
        assertTrue(str.contains("Pop"));
        assertTrue(str.contains("videoPath=/path/video.mp4"));
        assertTrue(str.contains("resolution=1080p"));
    }

    @Test
    public void testEquals() {
        List<String> notes = new ArrayList<>();
        MusicMultimedia m1 = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/a", "HD");
        MusicMultimedia m2 = new MusicMultimedia("Other", "Other", notes, "Rock", 200, "/a", "HD");
        MusicMultimedia m3 = new MusicMultimedia("Song", "Lyrics", notes, "Pop", 180, "/b", "HD");

        assertEquals(m1, m2); // videoPath and resolution are the same
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, new Object());
    }
}
