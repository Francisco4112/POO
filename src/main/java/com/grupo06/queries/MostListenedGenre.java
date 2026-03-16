package com.grupo06.queries;

import com.grupo06.playable.*;

import java.util.HashMap;
import java.util.Map;

public class MostListenedGenre implements Query<String> {
  /**
   * This method finds the most listened genre in a given map of music tracks.
   *
   * @param musics A map where the key is a string (music name + album name) and
   *               the value is a Music object.
   * @return The genre that has been listened to the most.
   */
  public String execute(QueryData model) {
    Map<String, Music> musics = model.getMusics();
    // Hashmap that will hold genre -> playCountPerGenre
    Map<String, Integer> playCountPerGenre = new HashMap<String, Integer>();

    // Loops through the musics hashmap
    for (Music m : musics.values()) {
      String genre = m.getGenre();
      int playCount = m.getPlayCount();

      if (!playCountPerGenre.containsKey(genre)) {
        playCountPerGenre.put(genre, playCount);
      } else {
        playCountPerGenre.put(genre, playCount + playCountPerGenre.get(genre));
      }
    }

    // Gets the most listened genre
    Map.Entry<String, Integer> maxEntry = null;
    for (Map.Entry<String, Integer> e : playCountPerGenre.entrySet()) {
      if (maxEntry == null || e.getValue() > maxEntry.getValue()) {
        maxEntry = e;
      }
    }

    return maxEntry.getKey();
  }
}
