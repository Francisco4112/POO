package com.grupo06.playableTest.playlistTest;

import com.grupo06.playable.Music;
import com.grupo06.playable.MusicExplicit;
import com.grupo06.playable.playlist.FavouriteList;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FavouriteListTest {

    @Test
    public void testEmptyConstructor() {
        FavouriteList fav = new FavouriteList();
        assertEquals("", fav.getName());
        assertTrue(fav.getMusics().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        FavouriteList fav = new FavouriteList("Favourites", musics);

        assertEquals("Favourites", fav.getName());
        assertEquals(1, fav.getMusics().size());
        assertEquals("Song 1", fav.getMusics().get(0).getName());
    }

    @Test
    public void testCopyConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        FavouriteList original = new FavouriteList("Favourites", musics);
        FavouriteList copy = new FavouriteList(original);

        assertNotSame(original, copy);
        assertNotSame(original.getMusics(), copy.getMusics());
        assertEquals(original.getMusics().get(0), copy.getMusics().get(0));
    }

    @Test
    public void testWithMaxDuration() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 100));
        musics.add(new Music("Song 2", "Lyrics 2", new ArrayList<>(), "Pop", 80));
        musics.add(new Music("Song 3", "Lyrics 3", new ArrayList<>(), "Pop", 50));
        FavouriteList fav = new FavouriteList("Favourites", musics);

        FavouriteList limited = fav.withMaxDuration(180);
        assertTrue(limited.getName().contains("max 180s"));
        int total = limited.getMusics().stream().mapToInt(Music::getDurationInSec).sum();
        assertTrue(total <= 180);
        // O shuffle pode alterar a ordem, mas as músicas devem ser subset das originais
        for (Music m : limited.getMusics()) {
            assertTrue(musics.contains(m));
        }
    }

    @Test
    public void testOnlyExplicit() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 100));
        musics.add(new MusicExplicit("Song 2", "Lyrics 2", new ArrayList<>(), "Pop", 80, 18));
        musics.add(new MusicExplicit("Song 3", "Lyrics 3", new ArrayList<>(), "Pop", 50, 16));
        FavouriteList fav = new FavouriteList("Favourites", musics);

        FavouriteList explicitOnly = fav.onlyExplicit();
        assertTrue(explicitOnly.getName().contains("explicit only"));
        for (Music m : explicitOnly.getMusics()) {
            assertTrue(m instanceof MusicExplicit);
        }
        // Deve conter apenas as músicas explicit
        assertEquals(2, explicitOnly.getMusics().size());
    }

    @Test
    public void testClone() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        FavouriteList fav = new FavouriteList("Favourites", musics);
        FavouriteList cloned = fav.clone();
        
        assertNotSame(fav, cloned);
        assertNotSame(fav.getMusics(), cloned.getMusics());
    }

    @Test
    public void testToString() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        FavouriteList fav = new FavouriteList("Favourites", musics);

        String str = fav.toString();
        assertTrue(str.contains("FavouriteList:"));
        assertTrue(str.contains("name=Favourites"));
        assertTrue(str.contains("Song 1"));
    }
}