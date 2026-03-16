package com.grupo06.playable.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.grupo06.playable.Music;

/**
 * A playlist that contains randomly selected music.
 */
public class RandomPlaylist extends Playlist {

    /**
     * Creates an empty random playlist.
     */
    public RandomPlaylist() {
        super();
    }

    /**
     * Creates a random playlist with a name and a list of music.
     *
     * @param name   the playlist name
     * @param musics the list of music
     */
    public RandomPlaylist(String name, List<Music> musics) {
        super(name, musics);
    }

    /**
     * Copy constructor.
     *
     * @param playlist the playlist to copy
     */
    public RandomPlaylist(RandomPlaylist playlist) {
        super(playlist);
    }

    /**
     * Selects a random set of music from the given map.
     *
     * @param map   the music map
     * @param count number of random items to select
     * @return list of randomly selected music
     */
    public static List<Music> getRandomItems(Map<String, Music> map, int count) {
        List<Music> entries = new ArrayList<>(map.values());
        List<Music> result = new ArrayList<>();

        if (count >= entries.size()) {
            return entries;
        }

        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(entries.size());
            result.add(entries.get(randomIndex));
            entries.remove(randomIndex);
        }

        return result;
    }

    /**
     * Returns a string representation of the random playlist.
     *
     * @return formatted string with name and musics
     */
    @Override
    public String toString() {
        return "RandomPlaylist: " +
                "\nname=" + getName() +
                "\nmusics=" + getMusics();
    }

    /**
     * Creates a copy of this playlist.
     *
     * @return cloned RandomPlaylist
     */
    @Override
    public RandomPlaylist clone() {
        return new RandomPlaylist(this);
    }

}
