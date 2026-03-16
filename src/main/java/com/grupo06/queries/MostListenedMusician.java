package com.grupo06.queries;

import java.util.HashMap;
import java.util.Map;

import com.grupo06.playable.*;;

public class MostListenedMusician implements Query<String> {
  /**
   * This method finds the most listened musician in a given map of albums.
   *
   * @param albums A map where the key is a string (album name + musician name)
   *               and the value is an Album object.
   * @return The name of the most listened musician.
   */
  public String execute(QueryData model) {
    Map<String, Album> albums = model.getAlbums();
    // Will map musician name to his playcount
    Map<String, Integer> musicianReproductions = new HashMap<String, Integer>();
    int maxPlayCount = 0;
    String mostListenedMusician = "";

    for (Album a : albums.values()) {
      int playCount = 0;
      for (Music m : a.getMusics()) {
        playCount += m.getPlayCount();
      }

      // If we already have seen the musician, just updates his play count
      if (musicianReproductions.containsKey(a.getMusician())) {
        int prevPlayCount = musicianReproductions.get(a.getMusician());
        musicianReproductions.put(a.getMusician(), playCount + prevPlayCount);
      }
      // Else, just adds the key-value pair to the hashmap
      else {
        musicianReproductions.put(a.getMusician(), playCount);
      }

      // If the playcount we just added was bigger than the biggest playcount, we
      // update it and the the correspoding musician
      if (musicianReproductions.get(a.getMusician()) > maxPlayCount) {
        maxPlayCount = musicianReproductions.get(a.getMusician());
        mostListenedMusician = a.getMusician();
      }
    }

    return mostListenedMusician;
  }
}
