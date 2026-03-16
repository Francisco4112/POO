package com.grupo06.playableTest;

import com.grupo06.playable.Music;
import com.grupo06.playable.MusicExplicit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MusicExplicitTest {

    @Test
    public void testEmptyConstructor() {
        MusicExplicit music = new MusicExplicit();
        assertEquals("", music.getName());
        assertEquals("", music.getLyrics());
        assertEquals("", music.getGenre());
        assertEquals(0, music.getDurationInSec());
        assertEquals(0, music.getAgeRestriction());
    }

    @Test
    public void testParameterizedConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        notes.add("D");
        MusicExplicit music = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 18);

        assertEquals("Song", music.getName());
        assertEquals("Lyrics", music.getLyrics());
        assertEquals(notes, music.getMusicNotes());
        assertEquals("Pop", music.getGenre());
        assertEquals(180, music.getDurationInSec());
        assertEquals(18, music.getAgeRestriction());
    }

    @Test
    public void testCopyConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicExplicit original = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 16);
        MusicExplicit copy = new MusicExplicit(original);

        assertNotSame(original, copy);
    }

    @Test
    public void testMusicConstructor() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        Music m = new Music("Song", "Lyrics", notes, "Pop", 180);
        MusicExplicit explicit = new MusicExplicit(m, 21);

        assertEquals("Song", explicit.getName());
        assertEquals("Lyrics", explicit.getLyrics());
        assertEquals(notes, explicit.getMusicNotes());
        assertEquals("Pop", explicit.getGenre());
        assertEquals(180, explicit.getDurationInSec());
        assertEquals(21, explicit.getAgeRestriction());
    }

    @Test
    public void testSetAndGetAgeRestriction() {
        MusicExplicit music = new MusicExplicit();
        music.setAgeRestriction(12);
        assertEquals(12, music.getAgeRestriction());
    }

    @Test
    public void testClone() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicExplicit original = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 16);
        MusicExplicit clone = original.clone();

        assertNotSame(original, clone);
    }

    @Test
    public void testToString() {
        List<String> notes = new ArrayList<>();
        notes.add("C");
        MusicExplicit music = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 18);

        String str = music.toString();
        assertTrue(str.contains("Song"));
        assertTrue(str.contains("Lyrics"));
        assertTrue(str.contains("Pop"));
        assertTrue(str.contains("ageRestriction=18"));
    }

    @Test
    public void testEquals() {
        List<String> notes = new ArrayList<>();
        MusicExplicit m1 = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 18);
        MusicExplicit m2 = new MusicExplicit("Other", "Other", notes, "Rock", 200, 18);
        MusicExplicit m3 = new MusicExplicit("Song", "Lyrics", notes, "Pop", 180, 21);

        assertEquals(m1, m2); // ageRestriction is the same
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, new Object());
    }
}