package com.grupo06.queries;

import com.grupo06.user.User;
import java.util.Comparator;
import java.util.Map;

public class UserWithMostPoints implements Query<String> {
  /**
   * This method finds the user with the most points in a given map of users.
   *
   * @param users A map where the key is a string (username) and the value is a
   *              User object.
   * @return The User object of the user with the most points.
   *         If no user exists, it returns null.
   */
  public String execute(QueryData model) {
    Map<String, User> users = model.getUsers();
    User user = users.entrySet().stream()
        .max(Map.Entry.comparingByValue(Comparator.comparingDouble(User::getPoints)))
        .map(Map.Entry::getValue)
        .orElse(null);
    return user.getUsername();
  }
}
