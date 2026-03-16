package com.grupo06.queriesTest;

import com.grupo06.playable.Music;
import com.grupo06.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import com.grupo06.queries.QueryData;
import com.grupo06.queries.UserWithMostListenedMusicsEver;

import static org.junit.jupiter.api.Assertions.*;

public class UserWithMostListenedMusicsEverTest {

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
    void testSingleUser() {
        User user = new User("john", "John Doe", "john@example.com", "Rua 1");
        Map<LocalDate, List<Music>> history = new HashMap<>();
        List<Music> musics = Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180),
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180)
        );
        history.put(LocalDate.now(), musics);
        user.setListenedMusics(history);

        Map<String, User> users = new HashMap<>();
        users.put("john", user);

        UserWithMostListenedMusicsEver query = new UserWithMostListenedMusicsEver();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("john", result);
    }

    @Test
    void testMostListenedUser() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        Map<LocalDate, List<Music>> history1 = new HashMap<>();
        history1.put(LocalDate.now(), Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user1.setListenedMusics(history1);

        Map<LocalDate, List<Music>> history2 = new HashMap<>();
        history2.put(LocalDate.now(), Arrays.asList(
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180),
                new Music("Song3", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user2.setListenedMusics(history2);

        Map<String, User> users = new HashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostListenedMusicsEver query = new UserWithMostListenedMusicsEver();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }

    @Test
    void testTieReturnsLast() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        Map<LocalDate, List<Music>> history1 = new HashMap<>();
        history1.put(LocalDate.now(), Arrays.asList(
                new Music("Song1", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user1.setListenedMusics(history1);

        Map<LocalDate, List<Music>> history2 = new HashMap<>();
        history2.put(LocalDate.now(), Arrays.asList(
                new Music("Song2", "Lyrics", new ArrayList<>(), "Pop", 180)
        ));
        user2.setListenedMusics(history2);

        Map<String, User> users = new HashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostListenedMusicsEver query = new UserWithMostListenedMusicsEver();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }
}