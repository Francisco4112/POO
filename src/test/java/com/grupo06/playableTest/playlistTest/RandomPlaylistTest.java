package com.grupo06.playableTest.playlistTest;

import com.grupo06.playable.playlist.RandomPlaylist;
import com.grupo06.playable.Music;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RandomPlaylistTest {

    @Test
    public void testEmptyConstructor() {
        RandomPlaylist playlist = new RandomPlaylist();
        assertEquals("", playlist.getName());
        assertTrue(playlist.getMusics().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        RandomPlaylist playlist = new RandomPlaylist("Random", musics);

        assertEquals("Random", playlist.getName());
        assertEquals(1, playlist.getMusics().size());
        assertEquals("Song 1", playlist.getMusics().get(0).getName());
    }

    @Test
    public void testCopyConstructor() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        RandomPlaylist original = new RandomPlaylist("Random", musics);
        RandomPlaylist copy = new RandomPlaylist(original);

        assertNotSame(original, copy);
        assertNotSame(original.getMusics(), copy.getMusics());
    }

    @Test
    public void testClone() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        RandomPlaylist playlist = new RandomPlaylist("Random", musics);
        RandomPlaylist cloned = playlist.clone();

        assertNotSame(playlist, cloned);
        assertNotSame(playlist.getMusics(), cloned.getMusics());
    }

    @Test
    public void testToString() {
        List<Music> musics = new ArrayList<>();
        musics.add(new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Pop", 180));
        RandomPlaylist playlist = new RandomPlaylist("Random", musics);

        String str = playlist.toString();
        assertTrue(str.contains("RandomPlaylist:"));
        assertTrue(str.contains("name=Random"));
        assertTrue(str.contains("Song 1"));
    }

    @Test
    public void testGetRandomItemsReturnsAllIfCountGreaterThanSize() {
        Map<String, Music> map = new HashMap<>();
        map.put("a", new Music("A", "L", new ArrayList<>(), "Pop", 100));
        map.put("b", new Music("B", "L", new ArrayList<>(), "Pop", 100));
        List<Music> result = RandomPlaylist.getRandomItems(map, 5);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("A")));
        assertTrue(result.stream().anyMatch(m -> m.getName().equals("B")));
    }

    @Test
    public void testGetRandomItemsReturnsRandomSubset() {
        Map<String, Music> map = new HashMap<>();
        map.put("a", new Music("A", "L", new ArrayList<>(), "Pop", 100));
        map.put("b", new Music("B", "L", new ArrayList<>(), "Pop", 100));
        map.put("c", new Music("C", "L", new ArrayList<>(), "Pop", 100));
        List<Music> result = RandomPlaylist.getRandomItems(map, 2);
        assertEquals(2, result.size());
        Set<String> names = new HashSet<>();
        for (Music m : result) names.add(m.getName());
        assertTrue(names.size() == 2);
        for (Music m : result) {
            assertTrue(map.containsValue(m));
        }
    }
}
