package com.grupo06.queries;

import java.util.Map;

import com.grupo06.user.User;
import com.grupo06.playable.Playable;
import com.grupo06.playable.playlist.Playlist;

public class UserWithMostPlaylists implements Query<String> {
  /**
   * This method finds the user who has the most playlists in a given map of
   * users.
   * 
   * @param users A map where the key is a string (username) and the value is a
   *              User object.
   * @return The User object of the user who has the most playlists.
   *         If no user has playlists, it returns null.
   *         If there are multiple users with the same number of playlists, it
   *         returns the first one found.
   */
  public String execute(QueryData model) {
    Map<String, User> users = model.getUsers();
    User userWithMostPlaylists = new User();
    int maxPlaylists = 0;
    for (User user : users.values()) {
      int numberOfPlaylists = 0;
      for (Playable p : user.getLibrary()) {
        if (p instanceof Playlist) {
          numberOfPlaylists++;
        }
      }
      if (numberOfPlaylists >= maxPlaylists) {
        maxPlaylists = numberOfPlaylists;
        userWithMostPlaylists = user;
      }
    }
    return userWithMostPlaylists.getUsername();
  }
}
