package com.grupo06.playable.playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.grupo06.playable.Music;
import com.grupo06.playable.MusicExplicit;

/**
 * A playlist for the user's favorite music selections.
 */
public class FavouriteList extends Playlist {

    /**
     * Creates an empty favourite list.
     */
    public FavouriteList() {
        super();
    }

    /**
     * Creates a favourite list with a name and music list.
     *
     * @param name   the name of the playlist
     * @param musics the list of music
     */
    public FavouriteList(String name, List<Music> musics) {
        super(name, musics);
    }

    /**
     * Copy constructor.
     *
     * @param playlist the playlist to copy
     */
    public FavouriteList(FavouriteList playlist) {
        super(playlist);
    }

    /**
     * Creates a shuffled playlist limited to a maximum duration.
     *
     * @param durationInSec the max total duration in seconds
     * @return new FavouriteList with duration limit
     */
    public FavouriteList withMaxDuration(int durationInSec) {
        ArrayList<Music> selected = new ArrayList<>();
        int total = 0;

        for (Music m : this.getMusics()) {
            if (total + m.getDurationInSec() <= durationInSec) {
                selected.add(m);
                total += m.getDurationInSec();
            } else {
                break;
            }
        }

        Collections.shuffle(selected);
        return new FavouriteList(this.getName() + " (max " + durationInSec + "s)", selected);
    }

    /**
     * Filters and shuffles only explicit music from the list.
     *
     * @return new FavouriteList with explicit music only
     */
    public FavouriteList onlyExplicit() {
        ArrayList<Music> filtered = this.getMusics().stream()
                .filter(m -> m instanceof MusicExplicit)
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(filtered);
        return new FavouriteList(this.getName() + " (explicit only)", filtered);
    }

    /**
     * Returns a string representation of the favourite list.
     *
     * @return formatted string with name and musics
     */
    @Override
    public String toString() {
        return "FavouriteList: " +
                "\nname=" + getName() +
                "\nmusics=" + getMusics();
    }

    /**
     * Creates a copy of this playlist.
     *
     * @return cloned FavouriteList
     */
    @Override
    public FavouriteList clone() {
        return new FavouriteList(this);
    }
}
