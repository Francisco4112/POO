package com.grupo06.queries;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grupo06.playable.Music;
import com.grupo06.user.User;

public class UserWithMostListenedMusicsWithDates implements Query<String> {
    private LocalDate startDate;
    private LocalDate endDate;

    public UserWithMostListenedMusicsWithDates(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
    }

    /**
     * This method finds the user who has listened to the most music tracks within a
     * specified date range in a given map of users.
     *
     * @param users     A map where the key is a string (username) and the value is
     *                  a User object.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return The User object of the user who has listened to the most music tracks
     *         within the specified date range.
     */
    public String execute(QueryData model) {
        System.out.println(startDate);
        System.out.println(endDate);
        Map<String, User> users = model.getUsers();
        Map<String, Integer> userReps = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            userReps.put(entry.getKey(), 0);
            for (Map.Entry<LocalDate, List<Music>> musics : entry.getValue().getListenedMusics().entrySet()) {
                if (!musics.getKey().isBefore(startDate) && !musics.getKey().isAfter(endDate)) {
                    for (Music music : musics.getValue()) {
                        userReps.put(entry.getKey(), userReps.get(entry.getKey()) + 1);
                    }
                }
            }
        }
        String usernameMostListened = userReps.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        User user = users.get(usernameMostListened);
        return user.getUsername();
    }
}
