package com.grupo06.queries;

import java.util.Map;

import com.grupo06.playable.playlist.Playlist;

public class NumberOfPublicPlaylists implements Query<String> {
  /**
   * This method counts the number of public playlists in a given map of
   * playlists.
   *
   * @param playlists A map where the key is a string and the value is a Playlist
   *                  object.
   * @return The number of public playlists in the map.
   */
  public String execute(QueryData model) {
    Map<String, Playlist> playlists = model.getPlaylists();
    return Integer.toString(playlists.size());
  }
}
