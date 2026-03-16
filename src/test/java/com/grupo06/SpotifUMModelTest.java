package com.grupo06;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo06.exceptions.*;
import com.grupo06.mvc.SpotifUMModel;
import com.grupo06.playable.*;
import com.grupo06.playable.playlist.*;
import com.grupo06.user.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

public class SpotifUMModelTest {

  private SpotifUMModel model;
  private User testUser;
  private Music testMusic;
  private Album testAlbum;
  private CreatedPlaylist testPlaylist;

  @BeforeEach
  public void setUp() {
    model = new SpotifUMModel();
    testUser = new User("testUser", "Test Name", "Test Email", "Test Address");
    testMusic = new Music("Test Song", "Test Lyrics", new ArrayList<String>(),
                          "Genre Test", 180);
    testAlbum = new Album("Test Album", "Test Artist", "Test Publisher");
    testPlaylist =
        new CreatedPlaylist("Test Playlist", true, new ArrayList<Music>());
  }

  @Test
  public void testDefaultConstructor() {
    assertNotNull(model);
    assertTrue(model.getUsers().isEmpty());
    assertTrue(model.getPlaylists().isEmpty());
    assertTrue(model.getAlbums().isEmpty());
    assertTrue(model.getMusics().isEmpty());
  }

  @Test
  public void testParameterizedConstructor() {
    Map<String, User> users = new HashMap<>();
    users.put("user1", new User("user1", "name", "email", "adress"));

    Map<String, Playlist> playlists = new HashMap<>();
    playlists.put("playlist1",
                  new FavouriteList("playlist1", new ArrayList<>()));

    Map<String, Album> albums = new HashMap<>();
    albums.put("artist1:album1", new Album("album1", "artist1", "publisher1"));

    Map<String, Music> musics = new HashMap<>();
    musics.put(
        "artist1:album1:song1",
        new Music("song1", "lyrics1", new ArrayList<String>(), "Rock", 200));

    SpotifUMModel paramModel =
        new SpotifUMModel(users, playlists, albums, musics);

    assertEquals(1, paramModel.getUsers().size());
    assertEquals(1, paramModel.getPlaylists().size());
    assertEquals(1, paramModel.getAlbums().size());
    assertEquals(1, paramModel.getMusics().size());
  }

  @Test
  public void testClone() {
    SpotifUMModel clonedModel = model.clone();
    assertNotSame(model, clonedModel);
  }

  @Test
  public void testAddUser() throws UserAlreadyExistsException {
    model.addUser(testUser);
    assertEquals(1, model.getUsers().size());
    assertTrue(model.getUsers().containsKey("testUser"));
  }

  @Test
  public void testAddUserAlreadyExists() throws UserAlreadyExistsException {
    model.addUser(testUser);
    assertThrows(UserAlreadyExistsException.class,
                 () -> model.addUser(testUser));
  }

  @Test
  public void testAddAlbum() throws AlbumAlreadyExistsException {
    model.addAlbum(testAlbum);
    assertEquals(1, model.getAlbums().size());
    assertTrue(model.getAlbums().containsKey("Test Artist:Test Album"));
  }

  @Test
  public void testAddAlbumAlreadyExists() throws AlbumAlreadyExistsException {
    model.addAlbum(testAlbum);
    assertThrows(AlbumAlreadyExistsException.class,
                 () -> model.addAlbum(testAlbum));
  }

  @Test
  public void testAddMusic()
      throws AlbumAlreadyExistsException, MusicAlreadyExistsException,
             AlbumDoesNotExistException {
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);
    assertEquals(1, model.getMusics().size());
    assertTrue(
        model.getMusics().containsKey("Test Artist:Test Album:Test Song"));
  }

  @Test
  public void testAddMusicToNonExistentAlbum() {
    assertThrows(AlbumDoesNotExistException.class,
                 () -> model.addMusic("NonExistent", "Artist", testMusic));
  }

  @Test
  public void testAddPublicPlaylist() throws PlaylistAlreadyExistsException {
    model.addPublicPlaylist(testPlaylist);
    assertEquals(1, model.getPlaylists().size());
    assertTrue(model.getPlaylists().containsKey("Test Playlist"));
  }

  @Test
  public void testAddPublicPlaylistAlreadyExists()
      throws PlaylistAlreadyExistsException {
    model.addPublicPlaylist(testPlaylist);
    assertThrows(PlaylistAlreadyExistsException.class,
                 () -> model.addPublicPlaylist(testPlaylist));
  }

  @Test
  public void testGetUser()
      throws UserAlreadyExistsException, UserDoesNotExistException {
    model.addUser(testUser);
    User retrieved = model.getUser("testUser");
    assertEquals(testUser, retrieved);
  }

  @Test
  public void testGetNonExistentUser() {
    assertThrows(UserDoesNotExistException.class,
                 () -> model.getUser("nonExistent"));
  }

  @Test
  public void testGetAlbum()
      throws AlbumAlreadyExistsException, AlbumDoesNotExistException {
    model.addAlbum(testAlbum);
    Album retrieved = model.getAlbum("Test Artist", "Test Album");
    assertEquals(testAlbum, retrieved);
  }

  @Test
  public void testGetNonExistentAlbum() {
    assertThrows(AlbumDoesNotExistException.class,
                 () -> model.getAlbum("Artist", "NonExistent"));
  }

  @Test
  public void testGetMusic()
      throws AlbumAlreadyExistsException, MusicAlreadyExistsException,
             AlbumDoesNotExistException, MusicDoesNotExistException {
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);
    String key = "Test Artist:Test Album:Test Song";
    Music retrieved = model.getMusic(key);
    assertEquals(testMusic, retrieved);
  }

  @Test
  public void testGetNonExistentMusic() {
    assertThrows(MusicDoesNotExistException.class,
                 () -> model.getMusic("non:existent:key"));
  }

  @Test
  public void testGetPlaylist()
      throws PlaylistAlreadyExistsException, PlaylistDoesNotExistException {
    model.addPublicPlaylist(testPlaylist);
    Playlist retrieved = model.getPlaylist("Test Playlist");
    assertEquals(testPlaylist, retrieved);
  }

  @Test
  public void testGetNonExistentPlaylist() {
    assertThrows(PlaylistDoesNotExistException.class,
                 () -> model.getPlaylist("NonExistent"));
  }

  @Test
  public void testRemoveUser()
      throws UserAlreadyExistsException, UserDoesNotExistException {
    model.addUser(testUser);
    model.removeUser("testUser");
    assertFalse(model.getUsers().containsKey("testUser"));
  }

  @Test
  public void testRemoveNonExistentUser() {
    assertThrows(UserDoesNotExistException.class,
                 () -> model.removeUser("nonExistent"));
  }

  @Test
  public void testRemoveAlbum()
      throws AlbumAlreadyExistsException, AlbumDoesNotExistException,
             MusicDoesNotExistException {
    model.addAlbum(testAlbum);
    model.removeAlbum("Test Artist:Test Album");
    assertFalse(model.getAlbums().containsKey("Test Artist:Test Album"));
  }

  @Test
  public void testRemoveNonExistentAlbum() {
    assertThrows(AlbumDoesNotExistException.class,
                 () -> model.removeAlbum("Artist:NonExistent"));
  }

  @Test
  public void testRemoveMusic()
      throws AlbumAlreadyExistsException, MusicAlreadyExistsException,
             AlbumDoesNotExistException, MusicDoesNotExistException {
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);
    String key = "Test Artist:Test Album:Test Song";
    model.removeMusic(key, "Test Artist:Test Album");
    assertFalse(model.getMusics().containsKey(key));
  }

  @Test
  public void testRemoveNonExistentMusic() {
    assertThrows(MusicDoesNotExistException.class,
                 () -> model.removeMusic("non:existent:key", "albumKey"));
  }

  @Test
  public void testRemovePlaylist()
      throws PlaylistAlreadyExistsException, PlaylistDoesNotExistException {
    model.addPublicPlaylist(testPlaylist);
    model.removePlayListPublic("Test Playlist");
    assertFalse(model.getPlaylists().containsKey("Test Playlist"));
  }

  @Test
  public void testRemoveNonExistentPlaylist() {
    assertThrows(PlaylistDoesNotExistException.class,
                 () -> model.removePlayListPublic("NonExistent"));
  }

  @Test
  public void testGeneratePreferencesPlaylist()
      throws UserAlreadyExistsException, PlaylistException,
             AlbumAlreadyExistsException {

    testUser.addListenedMusic(LocalDate.now(), testMusic);
    model.addUser(testUser);
    FavouriteList playlist = model.generatePreferencesPlaylist("testUser");
    assertFalse(playlist.getMusics().isEmpty());
  }

  @Test
  public void testGeneratePreferencesPlaylistNoMusic()
      throws UserAlreadyExistsException {
    model.addUser(testUser);
    assertThrows(PlaylistException.class,
                 () -> model.generatePreferencesPlaylist("testUser"));
  }

  @Test
  public void testGeneratePreferencesPlaylistWithMaxTime()
      throws UserAlreadyExistsException, PlaylistException {
    testUser.addListenedMusic(LocalDate.now(), testMusic);
    model.addUser(testUser);
    FavouriteList playlist =
        model.generatePreferencesPlaylistWithMaxTime("testUser", 200);
    assertFalse(playlist.getMusics().isEmpty());
  }

  @Test
  public void testGeneratePreferencesPlaylistWithMaxTimeTooShort()
      throws UserAlreadyExistsException {
    model.addUser(testUser);
    testUser.addListenedMusic(LocalDate.now(), testMusic);
    assertThrows(
        PlaylistException.class,
        () -> model.generatePreferencesPlaylistWithMaxTime("testUser", 10));
  }

  @Test
  public void testGeneratePreferencesPlaylistWithOnlyExplicit()
      throws UserAlreadyExistsException, PlaylistException {
    Music explicitMusic = new MusicExplicit(
        "Song", "Lyrics", new ArrayList<String>(), "Rock", 180, 18);
    testUser.addListenedMusic(LocalDate.now(), explicitMusic);
    model.addUser(testUser);
    FavouriteList playlist =
        model.generatePreferencesPlaylistWithOnlyExplicit("testUser");
    assertFalse(playlist.getMusics().isEmpty());
  }

  @Test
  public void testGeneratePreferencesPlaylistWithOnlyExplicitNoExplicitMusic()
      throws UserAlreadyExistsException {
    model.addUser(testUser);
    testUser.addListenedMusic(LocalDate.now(), testMusic);
    assertThrows(
        PlaylistException.class,
        () -> model.generatePreferencesPlaylistWithOnlyExplicit("testUser"));
  }

  @Test
  public void testAddAndRemoveMusicFromPrivatePlaylist()
      throws UserAlreadyExistsException, UserDoesNotExistException,
             MusicDoesNotExistException, PlaylistDoesNotExistException,
             PlaylistAlreadyIsPublicException, MusicAlreadyExistsException, AlbumDoesNotExistException, AlbumAlreadyExistsException {
    testUser.addMusicToLibrary(testPlaylist);
    model.addUser(testUser);
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);
    // Add music to private playlist
    model.addMusicToPrivatePlayList("Test Playlist",
                                    "Test Artist:Test Album:Test Song",
                                    testUser, "testUser", testMusic);



    // Remove music from private playlist
    model.removeMusicFromPrivatePlayList("Test Playlist",
                                         "Test Artist:Test Album:Test Song",
                                         testUser, "testUser", testMusic);

    // Verify playlist is empty
    CreatedPlaylist playlist =
        testUser.getCreatePlayListFromLibrary("Test Playlist");
    assertTrue(playlist.getMusics().isEmpty());
  }

  @Test
  public void testAddAlbumToLibrary()
      throws UserAlreadyExistsException, AlbumAlreadyExistsException,
             UserDoesNotExistException, AlbumDoesNotExistException {
    model.addUser(testUser);
    model.addAlbum(testAlbum);
    model.addAlbumToLibrary("Test Artist:Test Album", "testUser", testUser);
    assertTrue(model.getUser("testUser").getPlayableFromLibrary("Test Artist:Test Album",
                                               "Album") != null);
  }

  @Test
  public void testSaveAndLoadModel()
      throws IOException, ClassNotFoundException, UserAlreadyExistsException {
    model.addUser(testUser);
    String testFile = "testModel.ser";

    // Save model
    model.saveToFile(testFile);

    // Load model
    SpotifUMModel loadedModel = new SpotifUMModel();
    loadedModel.loadFromFile(testFile);

    // Verify loaded model
    assertTrue(loadedModel.getUsers().containsKey("testUser"));
  }

  @Test
  public void testEquals() throws UserAlreadyExistsException {
    model.addUser(testUser);
    SpotifUMModel sameModel = new SpotifUMModel();
    sameModel.addUser(testUser);

    SpotifUMModel differentModel = new SpotifUMModel();

    assertNotEquals(model, differentModel);
  }

  @Test
  public void testToString() {
    String str = model.toString();
    assertTrue(str.contains("SpotifUMModel"));
    assertTrue(str.contains("usersCount=0"));
    assertTrue(str.contains("playlistsCount=0"));
    assertTrue(str.contains("albumsCount=0"));
    assertTrue(str.contains("musicsCount=0"));
  }

  @Test
  public void testListPlaylists() throws PlaylistAlreadyExistsException {
    model.addPublicPlaylist(testPlaylist);
    List<String> playlists = model.listPlaylists();
    assertEquals(1, playlists.size());
    assertTrue(playlists.get(0).contains("Test Playlist"));
  }

  @Test
  public void testListAlbums() throws AlbumAlreadyExistsException {
    model.addAlbum(testAlbum);
    List<String> albums = model.listAlbums();
    assertEquals(1, albums.size());
    assertTrue(albums.get(0).contains("Test Album"));
  }

  @Test
  public void testListMusics()
      throws AlbumAlreadyExistsException, MusicAlreadyExistsException,
             AlbumDoesNotExistException {
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);
    List<String> musics = model.listMusics();
    assertEquals(1, musics.size());
    assertTrue(musics.get(0).contains("Test Song"));
  }

  @Test
  public void testGetUserPlan() throws UserAlreadyExistsException {
    model.addUser(testUser);
    SubscriptionPlan plan = model.getUserPlan("testUser", testUser);
    assertEquals(new Free(), plan);
  }

  @Test
  public void testChangePlanUser() throws UserAlreadyExistsException, UserDoesNotExistException {
    model.addUser(testUser);
    model.changePlanUser(testUser, new PremiumTop(), "testUser");
    assertEquals(new PremiumTop(), model.getUser("testUser").getPlan());
  }

  @Test
  public void testAtualizaWhenUserListenMusic()
      throws UserAlreadyExistsException, UserDoesNotExistException,
             AlbumDoesNotExistException, AlbumAlreadyExistsException,
             MusicAlreadyExistsException {
    model.addUser(testUser);
    model.addAlbum(testAlbum);
    model.addMusic("Test Album", "Test Artist", testMusic);

    model.atualizaWhenUserListenMusic("testUser", testUser, testMusic);

    assertEquals(1, testMusic.getPlayCount());
    assertFalse(model.getUser("testUser").getListenedMusics().isEmpty());
  }
}
