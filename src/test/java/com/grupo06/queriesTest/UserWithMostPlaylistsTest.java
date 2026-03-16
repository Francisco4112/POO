package com.grupo06.queriesTest;

import com.grupo06.playable.Playable;
import com.grupo06.playable.playlist.FavouriteList;

import com.grupo06.queries.QueryData;
import com.grupo06.queries.UserWithMostPlaylists;
import com.grupo06.user.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserWithMostPlaylistsTest {

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
    void testSingleUserWithPlaylists() {
        User user = new User("john", "John Doe", "john@example.com", "Rua 1");
        List<Playable> library = new ArrayList<>();
        library.add(new FavouriteList("Fav1", new ArrayList<>()));
        library.add(new FavouriteList("Fav2", new ArrayList<>()));
        user.setLibrary(library);

        Map<String, User> users = new HashMap<>();
        users.put("john", user);

        UserWithMostPlaylists query = new UserWithMostPlaylists();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("john", result);
    }

    @Test
    void testMostPlaylistsAmongUsers() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        List<Playable> library1 = new ArrayList<>();
        library1.add(new FavouriteList("Fav1", new ArrayList<>()));
        user1.setLibrary(library1);

        List<Playable> library2 = new ArrayList<>();
        library2.add(new FavouriteList("Fav2", new ArrayList<>()));
        library2.add(new FavouriteList("Fav3", new ArrayList<>()));
        user2.setLibrary(library2);

        Map<String, User> users = new HashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostPlaylists query = new UserWithMostPlaylists();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }

    @Test
    void testTieReturnsLast() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        List<Playable> library1 = new ArrayList<>();
        library1.add(new FavouriteList("Fav1", new ArrayList<>()));
        user1.setLibrary(library1);

        List<Playable> library2 = new ArrayList<>();
        library2.add(new FavouriteList("Fav2", new ArrayList<>()));
        user2.setLibrary(library2);

        Map<String, User> users = new LinkedHashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostPlaylists query = new UserWithMostPlaylists();
        String result = query.execute(new DummyQueryData(users));
        assertEquals("bob", result);
    }

    @Test
    void testNoPlaylistsReturnsDefaultUser() {
        User user1 = new User("alice", "Alice", "alice@example.com", "Rua 2");
        User user2 = new User("bob", "Bob", "bob@example.com", "Rua 3");

        user1.setLibrary(new ArrayList<>());
        user2.setLibrary(new ArrayList<>());

        Map<String, User> users = new LinkedHashMap<>();
        users.put("alice", user1);
        users.put("bob", user2);

        UserWithMostPlaylists query = new UserWithMostPlaylists();
        String result = query.execute(new DummyQueryData(users));
        // O método retorna o último user do laço (bob)
        assertEquals("bob", result);
    }
}
