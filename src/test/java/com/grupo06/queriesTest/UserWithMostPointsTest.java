package com.grupo06.queriesTest;

import com.grupo06.queries.QueryData;
import com.grupo06.queries.UserWithMostPoints;
import com.grupo06.user.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserWithMostPointsTest {

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
        user.setPoints(42.0);

        Map<String, User> users = new HashMap<>();
        users.put("john", user);

        UserWithMostPoints query = new UserWithMostPoints();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("john", result);
    }

    @Test
    void testMostPointsAmongUsers() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        user1.setPoints(10.0);
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");
        user2.setPoints(50.0);

        Map<String, User> users = new HashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostPoints query = new UserWithMostPoints();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }

    @Test
    void testTieReturnsFirst() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        user1.setPoints(20.0);
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");
        user2.setPoints(20.0);

        Map<String, User> users = new LinkedHashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostPoints query = new UserWithMostPoints();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("alice", result);
    }
}
