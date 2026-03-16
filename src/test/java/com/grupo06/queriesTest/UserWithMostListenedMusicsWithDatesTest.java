package com.grupo06.queriesTest;

import com.grupo06.playable.Music;
import com.grupo06.queries.QueryData;
import com.grupo06.queries.UserWithMostListenedMusicsWithDates;
import com.grupo06.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserWithMostListenedMusicsWithDatesTest {

    static class DummyQueryData implements QueryData {
        private final Map<String, User> users;
        public DummyQueryData(Map<String, User> users) { this.users = users; }
        @Override
        public Map<String, User> getUsers() { return users; }
        @Override
        public Map<String, com.grupo06.playable.playlist.Playlist> getPlaylists() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Album> getAlbums() { return new HashMap<>(); }
        @Override
        public Map<String, com.grupo06.playable.Music> getMusics() { return new HashMap<>(); }
    }

    @Test
    void testSingleUserInRange() {
        User user = new User("john", "John Doe", "john@example.com", "Rua 1");
        Map<LocalDate, List<Music>> history = new HashMap<>();
        LocalDate d1 = LocalDate.of(2024, 5, 1);
        LocalDate d2 = LocalDate.of(2024, 5, 10);
        history.put(d1, Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        history.put(d2, Arrays.asList(
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user.setListenedMusics(history);

        Map<String, User> users = new HashMap<>();
        users.put("john", user);

        UserWithMostListenedMusicsWithDates query =
                new UserWithMostListenedMusicsWithDates(LocalDate.of(2024, 4, 30), LocalDate.of(2024, 5, 15));
        String result = query.execute(new DummyQueryData(users));
        assertEquals("john", result);
    }

    @Test
    void testMostListenedUserInRange() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        LocalDate d1 = LocalDate.of(2024, 5, 2);
        LocalDate d2 = LocalDate.of(2024, 5, 5);
        LocalDate d3 = LocalDate.of(2024, 5, 12);

        Map<LocalDate, List<Music>> history1 = new HashMap<>();
        history1.put(d1, Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user1.setListenedMusics(history1);

        Map<LocalDate, List<Music>> history2 = new HashMap<>();
        history2.put(d2, Arrays.asList(
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180),
                new Music("Song3", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        history2.put(d3, Arrays.asList(
                new Music("Song4", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user2.setListenedMusics(history2);

        Map<String, User> users = new HashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostListenedMusicsWithDates query =
                new UserWithMostListenedMusicsWithDates(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 11));
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result); // bob ouve 2 músicas no intervalo, alice ouve 1
    }

    @Test
    void testTieReturnsLast() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        LocalDate d = LocalDate.of(2024, 5, 5);

        Map<LocalDate, List<Music>> history1 = new HashMap<>();
        history1.put(d, Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user1.setListenedMusics(history1);

        Map<LocalDate, List<Music>> history2 = new HashMap<>();
        history2.put(d, Arrays.asList(
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user2.setListenedMusics(history2);

        Map<String, User> users = new LinkedHashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostListenedMusicsWithDates query =
                new UserWithMostListenedMusicsWithDates(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 10));
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }
}
