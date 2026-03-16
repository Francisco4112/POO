package com.grupo06.queries;

import com.grupo06.user.User;
import java.util.Map;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.grupo06.playable.Music;

public class UserWithMostListenedMusicsEver implements Query<String> {
    /**
     * This method finds the user who has listened to the most music tracks in a
     * given map of users.
     *
     * @param users A map where the key is a string (username) and the value is a
     *              User object.
     * @return The User object of the user who has listened to the most music
     *         tracks.
     */
    public String execute(QueryData model) {
        Map<String, User> users = model.getUsers();
        Map<String, Integer> userReps = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userReps.put(entry.getKey(), 0);
            for (Map.Entry<LocalDate, List<Music>> musics : entry.getValue().getListenedMusics().entrySet()) {
                for (Music music : musics.getValue()) {
                    userReps.put(entry.getKey(), userReps.get(entry.getKey()) + 1);
                }
            }
        }
        String usernameMostListened = userReps.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        User user = users.get(usernameMostListened);
        return user.getUsername();
    }
}