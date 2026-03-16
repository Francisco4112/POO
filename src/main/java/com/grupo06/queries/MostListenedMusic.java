package com.grupo06.queries;

import java.util.Map;
import java.util.Comparator;

import com.grupo06.playable.*;

public class MostListenedMusic implements Query<String> {

  /**
   * Executes the query to return the name of the most played music.
   *
   * @param model the data context
   * @return the name of the most listened music
   */
  @Override
  public String execute(QueryData model) {
    Map<String, Music> musics = model.getMusics();
    Music music = musics.values().stream().max(Comparator.comparingInt(Music::getPlayCount)).orElse(null);
    return music.getName();
  }
}
