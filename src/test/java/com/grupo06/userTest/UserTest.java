package com.grupo06.userTest;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.playable.Album;
import com.grupo06.playable.Music;
import com.grupo06.playable.Playable;
import com.grupo06.playable.playlist.CreatedPlaylist;
import com.grupo06.playable.playlist.Playlist;
import com.grupo06.user.Free;
import com.grupo06.user.PremiumBase;
import com.grupo06.user.PremiumTop;
import com.grupo06.user.SubscriptionPlan;
import com.grupo06.user.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  public void testDefaultConstructor() {
    User user = new User();
    assertEquals("", user.getUsername());
    assertEquals("", user.getName());
    assertEquals("", user.getEmail());
    assertEquals("", user.getAddress());
    assertEquals(0, user.getPoints());
    assertTrue(user.getLibrary().isEmpty());
    assertTrue(user.getListenedMusics().isEmpty());
    assertTrue(user.getPlan() instanceof Free);
  }

  @Test
  public void testParameterizedConstructor() {
    List<Playable> library = new ArrayList<>();
    Map<LocalDate, List<Music>> listenedMusics = new HashMap<>();
    SubscriptionPlan plan = new PremiumBase();

    User user = new User("john_doe", "John Doe", "john@example.com",
                         "123 Street", plan, 100, library, listenedMusics);

    assertEquals("john_doe", user.getUsername());
    assertEquals("John Doe", user.getName());
    assertEquals("john@example.com", user.getEmail());
    assertEquals("123 Street", user.getAddress());
    assertEquals(100, user.getPoints());
    assertTrue(user.getLibrary().isEmpty());
    assertTrue(user.getListenedMusics().isEmpty());
    assertTrue(user.getPlan() instanceof PremiumBase);
  }

  @Test
  public void testClone() {
    User user =
        new User("john_doe", "John Doe", "john@example.com", "123 Street",
                 new Free(), 50, new ArrayList<>(), new HashMap<>());
    User clonedUser = user.clone();

    assertEquals(user, clonedUser);
    assertNotSame(user, clonedUser);
  }

  @Test
  public void testAddMusicToLibrary() {
    User user = new User();
    Playable music =
        new Music("Song 1", "Lyrics", new ArrayList<>(), "Pop", 180, 0);

    user.addMusicToLibrary(music);
    assertEquals(1, user.getLibrary().size());
    assertTrue(user.getLibrary().contains(music));
  }

  @Test
  public void testRemoveMusicFromLibrary() {
    User user = new User();
    Playable music =
        new Music("Song 1", "Lyrics", new ArrayList<>(), "Pop", 180, 0);

    user.addMusicToLibrary(music);
    user.removeMusicFromLibrary(music);
    assertTrue(user.getLibrary().isEmpty());
  }

  @Test
  public void testAddListenedMusic() {
    User user = new User();
    Music music =
        new Music("Song 1", "Lyrics", new ArrayList<>(), "Pop", 180, 0);
    LocalDate date = LocalDate.now();

    user.addListenedMusic(date, music);
    assertTrue(user.getListenedMusics().containsKey(date));
    assertEquals(1, user.getListenedMusics().get(date).size());
    assertTrue(user.getListenedMusics().get(date).contains(music));
  }

  @Test
  public void testAtualizaPontos() {
    User user =
        new User("john_doe", "John Doe", "john@example.com", "123 Street",
                 new PremiumBase(), 50, new ArrayList<>(), new HashMap<>());

    user.atualizaPontos();
    assertEquals(60, user.getPoints()); // PremiumBase adds 10 points
  }

  @Test
  public void testSettersAndGetters() {
    User user = new User();
    user.setUsername("u");
    user.setName("n");
    user.setEmail("e");
    user.setAddress("a");
    user.setPoints(42.5);
    user.setPlan(new PremiumBase());

    assertEquals("u", user.getUsername());
    assertEquals("n", user.getName());
    assertEquals("e", user.getEmail());
    assertEquals("a", user.getAddress());
    assertEquals(42.5, user.getPoints());
    assertTrue(user.getPlan() instanceof PremiumBase);
  }

  @Test
  public void testSetPlanPremiumTopAddsPoints() {
    User user = new User();
    user.setPoints(10);
    user.setPlan(new PremiumTop());
    assertTrue(user.getPlan() instanceof PremiumTop);
    assertEquals(110, user.getPoints()); // +100 pontos
  }

  @Test
  public void testSetLibraryAndGetLibrary() {
    User user = new User();
    List<Playable> library = new ArrayList<>();
    library.add(new Music("Song", "Lyrics", new ArrayList<>(), "Pop", 100));
    user.setLibrary(library);
    assertEquals(1, user.getLibrary().size());
    assertNotSame(library, user.getLibrary());
  }

  @Test
  public void testSetListenedMusicsAndGetListenedMusics() {
    User user = new User();
    Map<LocalDate, List<Music>> listened = new HashMap<>();
    List<Music> musics = new ArrayList<>();
    musics.add(new Music("S", "L", new ArrayList<>(), "G", 100));
    listened.put(LocalDate.of(2024, 1, 1), musics);
    user.setListenedMusics(listened);
    Map<LocalDate, List<Music>> result = user.getListenedMusics();
    assertEquals(1, result.size());
    assertEquals("S", result.get(LocalDate.of(2024, 1, 1)).get(0).getName());
    assertNotSame(listened, result);
  }

  @Test
  public void testGetPlaylistAndExistPlayList() {
    User user = new User();
    CreatedPlaylist playlist =
        new CreatedPlaylist("MyPlaylist", true, new ArrayList<>());
    user.addMusicToLibrary(playlist);
    assertNotNull(user.getPlaylist("MyPlaylist"));
    assertTrue(user.existPlayList("MyPlaylist"));
    assertNull(user.getPlaylist("Other"));
    assertFalse(user.existPlayList("Other"));
  }

  @Test
  public void testGetCreatePlayListFromLibrary() {
    User user = new User();
    CreatedPlaylist playlist =
        new CreatedPlaylist("MyPlaylist", true, new ArrayList<>());
    user.addMusicToLibrary(playlist);
    assertNotNull(user.getCreatePlayListFromLibrary("MyPlaylist"));
    assertNull(user.getCreatePlayListFromLibrary("Other"));
  }

  @Test
  public void testGetPlaylists() {
    User user = new User();
    CreatedPlaylist playlist =
        new CreatedPlaylist("MyPlaylist", true, new ArrayList<>());
    user.addMusicToLibrary(playlist);
    List<Playlist> playlists = user.getPlaylists();
    assertEquals(1, playlists.size());
    assertEquals("MyPlaylist", playlists.get(0).getName());
  }

  @Test
  public void testListLibrary() {
    User user = new User();
    Music music = new Music("Song", "Lyrics", new ArrayList<>(), "Pop", 100);
    user.addMusicToLibrary(music);
    List<String> list = user.listLibrary();
    assertEquals(1, list.size());
    assertTrue(list.get(0).contains("Song"));
  }

  @Test
  public void testGetAlbumFromLibrary() {
    User user = new User();
    Album album = new Album("Album1", "Artist1", "Publisher1");
    user.addMusicToLibrary(album);
    assertNotNull(user.getAlbumFromLibrary("Artist1:Album1"));
    assertNull(user.getAlbumFromLibrary("Other:Album2"));
  }

  @Test
  public void testGetPlayableFromLibrary() {
    User user = new User();
    Album album = new Album("Album1", "Artist1", "Publisher1");
    CreatedPlaylist playlist =
        new CreatedPlaylist("MyPlaylist", true, new ArrayList<>());
    user.addMusicToLibrary(album);
    user.addMusicToLibrary(playlist);
    assertEquals(album, user.getPlayableFromLibrary("Artist1:Album1", "Album"));
    assertEquals(playlist,
                 user.getPlayableFromLibrary("MyPlaylist", "Playlist"));
    assertNull(user.getPlayableFromLibrary("Other", "Album"));
    assertNull(user.getPlayableFromLibrary("Other", "Playlist"));
    assertNull(user.getPlayableFromLibrary("Any", "OtherType"));
  }

  @Test
  public void testEquals() {
    User user1 =
        new User("john_doe", "John Doe", "john@example.com", "123 Street",
                 new Free(), 50, new ArrayList<>(), new HashMap<>());
    User user2 =
        new User("john_doe", "John Doe", "john@example.com", "123 Street",
                 new Free(), 50, new ArrayList<>(), new HashMap<>());

    assertEquals(user1, user2);
  }

  @Test
  public void testToString() {
    // Configuração do objeto User
    List<Playable> library = new ArrayList<>();
    library.add(
        new Music("Song 1", "Lyrics 1", new ArrayList<>(), "Genre 1", 180));
    Map<LocalDate, List<Music>> listenedMusics = new HashMap<>();
    List<Music> musics = new ArrayList<>();
    musics.add(
        new Music("Song 2", "Lyrics 2", new ArrayList<>(), "Genre 2", 200));
    listenedMusics.put(LocalDate.of(2025, 5, 6), musics);

    User user = new User("john_doe", "John Doe", "john@example.com",
                         "123 Street", new Free(), 50, library, listenedMusics);

    String expected = "\n=== User profile ===\n"
                      + "Username: john_doe\n"
                      + "Full name: John Doe\n"
                      + "Email: john@example.com\n"
                      + "Address: 123 Street\n"
                      + "Points: 50,00\n"
                      + "Subscription plan: Free\n"
                      + "\n=== Library ===\n"
                      + "Items in library: 1\n"
                      + "\n=== History ===\n"
                      + "\n06/05/2025:\n"
                      + "  1. Song 2\n";

    assertEquals(expected, user.toString());
  }
}