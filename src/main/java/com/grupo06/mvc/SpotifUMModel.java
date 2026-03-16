package com.grupo06.mvc;

import com.grupo06.playable.*;
import com.grupo06.playable.playlist.*;
import com.grupo06.exceptions.*;
import com.grupo06.user.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * Model class managing users, playlists, albums, and musics.
 * Uses composite keys for albums and musics (musician + name).
 */

public class SpotifUMModel implements Serializable {
    /** Map of username to User. */
    private Map<String, User> users;

    /** Map of playlist name to Playlist. */
    private Map<String, Playlist> playlists;

    /** Map of composite album key (musician:name) to Album. */
    private Map<String, Album> albums;

    /** Map of composite music key (albumKey:name) to Music. */
    private Map<String, Music> musics;

    /**
     * Default constructor initializing empty collections and sets current time.
     */
    public SpotifUMModel() {
        this.users = new HashMap<>();
        this.playlists = new HashMap<>();
        this.albums = new HashMap<>();
        this.musics = new HashMap<>();
    }

    /**
     * Constructor with given collections.
     */
    public SpotifUMModel(Map<String, User> users, Map<String, Playlist> playlists,
            Map<String, Album> albums, Map<String, Music> musics) {
        this.users = users;
        this.playlists = playlists;
        this.albums = new HashMap<>(albums);
        this.musics = musics;
    }

    /**
     * Copy constructor.
     */
    public SpotifUMModel(SpotifUMModel model) {
        this.users = model.getUsers();
        this.playlists = model.getPlaylists();
        this.albums = model.getAlbums();
        this.musics = model.getMusics();
    }

    /**
     * Generates a composite key for an album from musician and album name.
     * 
     * @param album the album
     * @return composite key "musician:name"
     */
    private String generateAlbumKey(Album album) {
        return album.getMusician() + ":" + album.getName();
    }

    /**
     * Generates a composite key for music from album key and music name.
     * 
     * @param music    the music
     * @param albumKey the album key
     * @return composite key "albumKey:musicName"
     */
    private String generateMusicKey(Music music, String albumKey) {
        return albumKey + ":" + music.getName();
    }

    // Getters e Setters
    public Map<String, User> getUsers() {
        return this.users.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Playlist> getPlaylists() {
        return this.playlists.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Album> getAlbums() {
        return this.albums.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Music> getMusics() {
        return this.musics.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setUsers(Map<String, User> users) {
        this.users = users.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setPlaylists(Map<String, Playlist> playlists) {
        this.playlists = playlists.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setAlbums(Map<String, Album> albums) {
        this.albums = albums.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setMusics(Map<String, Music> musics) {
        this.musics = musics.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    /**
     * Adds a user if username is not already present.
     * 
     * @param user the User to add
     * @throws UserAlreadyExistsException if username exists
     */
    public void addUser(User user) throws UserAlreadyExistsException {
        if (this.users.containsKey(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        this.users.put(user.getUsername(), user.clone());
    }

    /**
     * Adds a public playlist if it doesn't exist.
     * 
     * @param playlist the Playlist to add
     * @throws PlaylistAlreadyExistsException if playlist exists
     */
    public void addPublicPlaylist(Playlist playlist) throws PlaylistAlreadyExistsException {
        if (this.playlists.containsKey(playlist.getName())) {
            throw new PlaylistAlreadyExistsException(playlist.getName());
        }

        this.playlists.put(playlist.getName(), playlist);
    }

    /**
     * Adds an album identified by composite key.
     * 
     * @param album the Album to add
     * @throws AlbumAlreadyExistsException if album key exists
     */
    public void addAlbum(Album album) throws AlbumAlreadyExistsException {
        String key = generateAlbumKey(album);
        if (this.albums.containsKey(key)) {
            throw new AlbumAlreadyExistsException(key);
        }

        this.albums.put(key, album);
    }

    /**
     * Adds a music track to an existing album.
     * 
     * @param album    album name
     * @param musician musician name
     * @param music    Music object to add
     * @throws MusicAlreadyExistsException if music key exists
     * @throws AlbumDoesNotExistException  if album not found
     */
    public void addMusic(String album, String musician, Music music)
            throws MusicAlreadyExistsException, AlbumDoesNotExistException {
        String albumKey = musician + ":" + album;
        String musicKey = generateMusicKey(music, albumKey);

        if (this.musics.containsKey(musicKey)) {
            throw new MusicAlreadyExistsException(musicKey);
        }
        this.musics.put(musicKey, music);

        Album a = this.albums.get(albumKey);
        if (a == null) {
            throw new AlbumDoesNotExistException(albumKey);
        }
        a.addMusic(music);
    }

    /**
     * Makes a user's playlist public.
     * 
     * @param playlistName playlist to publish
     * @param username     owner username
     * @param user         User object (usually ignored)
     * @return playlist name
     * @throws UserDoesNotExistException        if user not found
     * @throws PlaylistDoesNotExistException    if playlist not found
     * @throws PlaylistAlreadyIsPublicException if already public
     */
    public String turnPlaylistPublic(String playlistName, String username, User user)
            throws UserDoesNotExistException, PlaylistDoesNotExistException, PlaylistAlreadyIsPublicException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        CreatedPlaylist playlist = user.getCreatePlayListFromLibrary(playlistName);
        if (playlist == null) {
            throw new PlaylistDoesNotExistException(playlistName);
        }
        if (!playlist.isPrivate()) {
            throw new PlaylistAlreadyIsPublicException(playlistName);
        }
        playlist.setPrivate(false);
        this.playlists.put(playlistName, playlist);

        return playlistName;
    }

    /**
     * Adds a private playlist to user's library.
     * 
     * @param username username
     * @param playlist Playlist to add
     * @param user     User object (usually ignored)
     * @throws UserDoesNotExistException if user not found
     */
    public void addPrivatePlaylist(String username, Playlist playlist, User user) throws UserDoesNotExistException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }

        user.addMusicToLibrary(playlist);
    }

    /**
     * Returns the subscription plan of a user.
     * 
     * @param username username
     * @param user     User object (usually ignored)
     * @return SubscriptionPlan of the user
     */
    public SubscriptionPlan getUserPlan(String username, User user) {
        user = this.users.get(username);
        return user.getPlan();
    }

    /**
     * Returns the User object for a username.
     * 
     * @param username username
     * @return User object
     * @throws UserDoesNotExistException if user not found
     */
    public User getUser(String username) throws UserDoesNotExistException {
        if (!this.users.containsKey(username)) {
            throw new UserDoesNotExistException(username);
        }
        return this.users.get(username);
    }

    /**
     * Generates a playlist with the user's music preferences (unique tracks they've
     * listened to).
     * 
     * @param username The user's username.
     * @return A playlist with a shuffled list of unique Music objects the user has
     *         listened to.
     * @throws PlaylistException if the user hasn't listened to any music or user
     *                           doesn't exist.
     */
    public FavouriteList generatePreferencesPlaylist(String username)
            throws PlaylistException {
        User u = this.users.get(username);
        if (u == null) {
            throw new PlaylistException("User does not exist.");
        }

        Set<Music> uniqueMusics = u.getListenedMusics().values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        if (uniqueMusics.isEmpty()) {
            throw new PlaylistException("User hasn't listened to any music.");
        }

        ArrayList<Music> musics = new ArrayList<>(uniqueMusics);
        Collections.shuffle(musics);
        return new FavouriteList("Favourite Playlist", musics);
    }

    /**
     * Generates a playlist with the user's music preferences, limited to a maximum
     * duration.
     * 
     * @param username The user's username.
     * @param duration The maximum duration in seconds for the playlist.
     * @return A playlist with a shuffled list of Music objects that fits within the
     *         specified
     *         duration.
     * @throws PlaylistException if no music fits the duration or user hasn't
     *                           listened to any music.
     */
    public FavouriteList generatePreferencesPlaylistWithMaxTime(String username, int duration)
            throws PlaylistException {
        FavouriteList base = generatePreferencesPlaylist(username);
        FavouriteList filtered = base.withMaxDuration(duration);

        if (filtered.getMusics().isEmpty()) {
            throw new PlaylistException("No music fits the specified duration.");
        }

        return filtered;
    }

    /**
     * Generates a playlist containing only explicit music from the user's
     * preferences.
     * 
     * @param username The user's username.
     * @return A playlist with a shuffled list of Explicit Music objects the user
     *         has listened to.
     * @throws PlaylistException if the user hasn't listened to any explicit music.
     */
    public FavouriteList generatePreferencesPlaylistWithOnlyExplicit(String username)
            throws PlaylistException {
        FavouriteList base = generatePreferencesPlaylist(username);
        FavouriteList explicitOnly = base.onlyExplicit();

        if (explicitOnly.getMusics().isEmpty()) {
            throw new PlaylistException("User hasn't listened to any explicit music.");
        }

        return explicitOnly;
    }

    /**
     * Retrieves a public playlist by name.
     * 
     * @param playListName the name of the playlist to retrieve
     * @return the Playlist object
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public Playlist getPlaylist(String playListName) throws PlaylistDoesNotExistException {
        if (!this.playlists.containsKey(playListName)) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        return this.playlists.get(playListName);
    }

    /**
     * Retrieves a music track by its composite key.
     * 
     * @param keyMusic the composite key of the music (e.g.,
     *                 "musician:album:trackName")
     * @return the Music object
     * @throws MusicDoesNotExistException if the music does not exist
     */
    public Music getMusic(String keyMusic) throws MusicDoesNotExistException {
        if (!this.musics.containsKey(keyMusic)) {
            throw new MusicDoesNotExistException(keyMusic);
        }
        return this.musics.get(keyMusic);
    }

    /**
     * Retrieves an album by musician and album name.
     * 
     * @param musician  the musician's name
     * @param albumName the album name
     * @return the Album object
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public Album getAlbum(String musician, String albumName) throws AlbumDoesNotExistException {
        String key = musician + ":" + albumName;
        if (!this.albums.containsKey(key)) {
            throw new AlbumDoesNotExistException(key);
        }
        return this.albums.get(key);
    }

    /**
     * Changes the subscription plan of the user identified by username.
     * 
     * @param u        User object (ignored, re-fetched inside)
     * @param plan     the new SubscriptionPlan
     * @param username the username of the user to update
     */
    public void changePlanUser(User u, SubscriptionPlan plan, String username) {
        u = this.users.get(username);
        u.setPlan(plan);
    }

    /**
     * Creates a random playlist of 5 musics and sets it on the given
     * RandomPlaylist.
     * 
     * @param p the RandomPlaylist to populate
     */
    public void createRandomPlaylist(RandomPlaylist p) {
        List<Music> randomMusics = RandomPlaylist.getRandomItems(this.musics, 5);
        p.setMusics(randomMusics);
    }

    /**
     * Removes an album identified by AlbumKey, and removes all its musics.
     * 
     * @param AlbumKey composite album key
     * @throws AlbumDoesNotExistException if the album does not exist
     * @throws MusicDoesNotExistException if any music to remove does not exist
     */
    public void removeAlbum(String AlbumKey) throws AlbumDoesNotExistException, MusicDoesNotExistException {
        if (this.albums.containsKey(AlbumKey)) {
            Album album = this.albums.get(AlbumKey);
            for (Music m : album.getMusics()) {
                String musicKey = generateMusicKey(m, AlbumKey);
                removeMusic(musicKey, AlbumKey);
            }
            this.albums.remove(AlbumKey);
        } else {
            throw new AlbumDoesNotExistException(AlbumKey);
        }
    }

    /**
     * Removes a given music from all public playlists.
     * 
     * @param music the Music to remove
     */
    public void removeMusicFromPublicPlaylists(Music music) {
        for (Playlist playlist : this.playlists.values()) {
            playlist.removeMusic(music);
        }
    }

    /**
     * Removes a given music from all users' libraries.
     * 
     * @param music the Music to remove
     */
    public void removeMusicFromLibrary(Music music) {
        for (User user : this.users.values()) {
            user.removeMusicFromLibrary(music);
        }
    }

    /**
     * Removes a given music from all private playlists of all users.
     * 
     * @param music the Music to remove
     */
    public void removeMusicFromPrivatePlaylists(Music music) {
        for (User user : this.users.values()) {
            for (Playlist playlist : user.getPlaylists()) {
                if (playlist instanceof CreatedPlaylist) {
                    ((CreatedPlaylist) playlist).removeMusic(music);
                }
            }
        }
    }

    /**
     * Removes a music identified by MusicKey from the model, including all
     * references.
     * 
     * @param MusicKey composite music key
     * @param AlbumKey composite album key
     * @throws MusicDoesNotExistException if the music does not exist
     */
    public void removeMusic(String MusicKey, String AlbumKey) throws MusicDoesNotExistException {
        if (this.musics.containsKey(MusicKey)) {
            Music music = this.musics.get(MusicKey);
            Album a = this.albums.get(AlbumKey);
            a.removeMusic(music);
            removeMusicFromPublicPlaylists(music);
            removeMusicFromPrivatePlaylists(music);
            removeMusicFromLibrary(music);
            this.musics.remove(MusicKey);
        } else {
            throw new MusicDoesNotExistException(MusicKey);
        }
    }

    /**
     * Removes a user by username.
     * 
     * @param username the username of the user to remove
     * @throws UserDoesNotExistException if the user does not exist
     */
    public void removeUser(String username) throws UserDoesNotExistException {
        if (this.users.containsKey(username)) {
            this.users.remove(username);
        } else {
            throw new UserDoesNotExistException(username);
        }
    }

    /**
     * Removes a playlist identified by key from all users libraries.
     * 
     * @param key the playlist key/name
     */
    public void removePlayListFromUsers(String key) {
        for (User user : this.users.values()) {
            user.removeMusicFromLibrary(this.playlists.get(key));
        }
    }

    /**
     * Removes a playlist from a specific user's library.
     * 
     * @param playListName name of the playlist
     * @param username     username of the user
     * @param user         User object (ignored, re-fetched internally)
     * @throws PlaylistDoesNotExistException if the playlist does not exist for the
     *                                       user
     */
    public void removePlayListFromLibrary(String playListName, String username, User user)
            throws PlaylistDoesNotExistException {
        user = this.users.get(username);
        if (!user.existPlayList(playListName)) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        user.removeMusicFromLibrary(
                user.getPlayableFromLibrary(playListName, "Playlist"));
    }

    /**
     * Removes a public playlist by name.
     * 
     * @param playListName the name of the playlist to remove
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void removePlayListPublic(String playListName) throws PlaylistDoesNotExistException {
        if (this.playlists.containsKey(playListName)) {
            this.playlists.remove(playListName);
        } else {
            throw new PlaylistDoesNotExistException(playListName);
        }
    }

    /**
     * Removes a playlist identified by key from users, library, and public store.
     * 
     * @param key          the playlist key/name
     * @param playListName the playlist name (used for exception message)
     * @param username     the user who owns the playlist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void removePlayList(String key, String playListName, String username) throws PlaylistDoesNotExistException {
        if (!this.playlists.containsKey(key)) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        removePlayListFromUsers(key);
        User u = this.users.get(username);
        removePlayListFromLibrary(key, username, u);
        removePlayListPublic(key);
    }

    /**
     * Checks if an album with the given composite key exists.
     * 
     * @param AlbumKey composite album key
     * @return true if the album exists, false otherwise
     */
    public boolean existAlbum(String AlbumKey) {
        return this.albums.containsKey(AlbumKey);
    }

    /**
     * Returns a list of string representations of all playlists.
     *
     * @return ArrayList of playlist strings
     */
    public ArrayList<String> listPlaylists() {
        return this.playlists.values().stream()
                .map(Playlist::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a list of string representations of the music library of a user.
     *
     * @param username the username whose library will be listed
     * @return ArrayList of music strings in the user's library
     */
    public ArrayList<String> listUserLibrary(String username) {
        User u = this.users.get(username);
        return u.listLibrary();
    }

    /**
     * Returns a list of string representations of all albums.
     *
     * @return ArrayList of album strings
     */
    public ArrayList<String> listAlbums() {
        return this.albums.values().stream()
                .map(Album::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a list of string representations of all musics.
     *
     * @return ArrayList of music strings
     */
    public ArrayList<String> listMusics() {
        return this.musics.values().stream()
                .map(Music::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns all musics from a specific playlist of a user.
     *
     * @param playListName the name of the playlist
     * @param username     the username of the owner
     * @param playlist     unused parameter (overwritten internally)
     * @param user         unused parameter (overwritten internally)
     * @return ArrayList of Music objects in the playlist
     * @throws PlaylistDoesNotExistException if playlist does not exist for the user
     */
    public ArrayList<Music> getMusicsFromPlayList(String playListName, String username, Playlist playlist, User user)
            throws PlaylistDoesNotExistException {
        user = this.users.get(username);
        playlist = user.getPlaylist(playListName);
        if (playlist == null) {
            throw new PlaylistDoesNotExistException(playListName);
        }

        return playlist.getMusics();
    }

    /**
     * Returns all musics from a public playlist.
     *
     * @param playListName the name of the public playlist
     * @param playlist     unused parameter (overwritten internally)
     * @return ArrayList of Music objects in the playlist
     * @throws PlaylistDoesNotExistException if playlist does not exist
     */
    public ArrayList<Music> getMusicsFromPublicPlayList(String playListName, Playlist playlist)
            throws PlaylistDoesNotExistException {
        ArrayList<Music> musics = new ArrayList<>();
        playlist = this.playlists.get(playListName);
        if (playlist == null) {
            throw new PlaylistDoesNotExistException(playListName);
        }

        musics = playlist.getMusics();

        return musics;
    }

    /**
     * Returns all musics from an album.
     *
     * @param albumKey the key of the album
     * @param album    unused parameter (overwritten internally)
     * @return ArrayList of Music objects in the album
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public ArrayList<Music> getAlbumMusics(String albumKey, Album album) throws AlbumDoesNotExistException {
        ArrayList<Music> musics = new ArrayList<>();
        album = this.albums.get(albumKey);
        if (album == null) {
            throw new AlbumDoesNotExistException(albumKey);
        }

        for (Music music : album.getMusics()) {
            musics.add(music);
        }

        return musics;
    }

    /**
     * Returns all musics from an album present in a user's library.
     *
     * @param albumKey the key of the album
     * @param username the username
     * @param user     unused parameter (overwritten internally)
     * @param album    unused parameter (overwritten internally)
     * @return ArrayList of Music objects in the album in the user's library
     * @throws AlbumDoesNotExistException if the album does not exist in user's
     *                                    library
     */
    public ArrayList<Music> getAlbumMusicsFromLibrary(String albumKey, String username, User user, Album album)
            throws AlbumDoesNotExistException {
        ArrayList<Music> musics = new ArrayList<>();
        user = this.users.get(username);
        album = user.getAlbumFromLibrary(albumKey);
        if (album == null) {
            throw new AlbumDoesNotExistException(albumKey);
        }
        for (Music music : album.getMusics()) {
            musics.add(music);
        }

        return musics;
    }

    /**
     * Removes a music from a user's private playlist.
     *
     * @param playListName the name of the private playlist
     * @param musicKey     the key of the music to remove
     * @param user         unused parameter (overwritten internally)
     * @param username     the username
     * @param music        unused parameter (overwritten internally)
     * @throws UserDoesNotExistException        if the user does not exist
     * @throws MusicDoesNotExistException       if the music does not exist
     * @throws PlaylistDoesNotExistException    if the playlist does not exist
     * @throws PlaylistAlreadyIsPublicException if the playlist is not private
     */
    public void removeMusicFromPrivatePlayList(String playListName, String musicKey, User user, String username,
            Music music)
            throws UserDoesNotExistException, MusicDoesNotExistException, PlaylistDoesNotExistException,
            PlaylistAlreadyIsPublicException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        CreatedPlaylist playlist = user.getCreatePlayListFromLibrary(playListName);
        if (playlist == null) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        music = this.musics.get(musicKey);
        if (music == null) {
            throw new MusicDoesNotExistException(musicKey);
        }
        if (!playlist.isPrivate()) {
            throw new PlaylistAlreadyIsPublicException(playListName);
        }
        playlist.removeMusic(music);
    }

    /**
     * Adds a music to a user's private playlist.
     *
     * @param playListName the name of the private playlist
     * @param musicKey     the key of the music to add
     * @param user         unused parameter (overwritten internally)
     * @param username     the username
     * @param music        unused parameter (overwritten internally)
     * @throws UserDoesNotExistException        if the user does not exist
     * @throws MusicDoesNotExistException       if the music does not exist
     * @throws PlaylistDoesNotExistException    if the playlist does not exist
     * @throws PlaylistAlreadyIsPublicException if the playlist is not private
     */
    public void addMusicToPrivatePlayList(String playListName, String musicKey, User user, String username,
            Music music)
            throws UserDoesNotExistException, MusicDoesNotExistException, PlaylistDoesNotExistException,
            PlaylistAlreadyIsPublicException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        music = this.musics.get(musicKey);
        if (music == null) {
            throw new MusicDoesNotExistException(musicKey);
        }
        CreatedPlaylist playlist = user.getCreatePlayListFromLibrary(playListName);
        if (playlist == null) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        if (!playlist.isPrivate()) {
            throw new PlaylistAlreadyIsPublicException(playListName);
        }
        playlist.addMusic(music);
    }

    /**
     * Adds a public playlist to a user's library.
     *
     * @param playListName the name of the playlist
     * @param username     the username
     * @param user         unused parameter (overwritten internally)
     * @throws UserDoesNotExistException     if the user does not exist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void addPlayListToLibrary(String playListName, String username, User user)
            throws UserDoesNotExistException, PlaylistDoesNotExistException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        if (this.playlists.get(playListName) == null) {
            throw new PlaylistDoesNotExistException(playListName);
        }
        user.addMusicToLibrary(this.playlists.get(playListName).clone());
    }

    /**
     * Adds an album to a user's library.
     *
     * @param albumKey the key of the album
     * @param username the username
     * @param user     unused parameter (overwritten internally)
     * @throws UserDoesNotExistException  if the user does not exist
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public void addAlbumToLibrary(String albumKey, String username, User user)
            throws UserDoesNotExistException, AlbumDoesNotExistException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        if (this.albums.get(albumKey) == null) {
            throw new AlbumDoesNotExistException(albumKey);
        }
        user.addMusicToLibrary(this.albums.get(albumKey));
    }

    /**
     * Removes an album from a user's library.
     *
     * @param albumKey the key of the album
     * @param username the username
     * @param user     unused parameter (overwritten internally)
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public void removeAlbumFromLibrary(String albumKey, String username, User user)
            throws AlbumDoesNotExistException {
        user = this.users.get(username);
        if (this.albums.get(albumKey) == null) {
            throw new AlbumDoesNotExistException(albumKey);
        }
        user.removeMusicFromLibrary(user.getPlayableFromLibrary(albumKey, "Album"));
    }

    /**
     * Updates the system when a user listens to a music.
     *
     * @param username the username of the user
     * @param user     unused parameter (overwritten internally)
     * @param music    the music listened to
     * @throws UserDoesNotExistException if the user does not exist
     */
    public void atualizaWhenUserListenMusic(String username, User user, Music music)
            throws UserDoesNotExistException {
        user = this.users.get(username);
        if (user == null) {
            throw new UserDoesNotExistException(username);
        }
        music.incrementPlayCount();
        user.addListenedMusic(LocalDate.now(), music);
        user.atualizaPontos();
    }

    /**
     * Loads the model state from a file.
     *
     * @param path the path of the file to load from
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if the class cannot be found
     * @throws ClassCastException     if the cast fails
     */
    public void loadFromFile(String path)
            throws IOException, ClassNotFoundException, ClassCastException {
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        SpotifUMModel model = (SpotifUMModel) objectStream.readObject();
        objectStream.close();

        // Copies the model data to the new model
        this.albums = model.getAlbums();
        this.musics = model.getMusics();
        this.playlists = model.getPlaylists();
        this.users = model.getUsers();
    }

    /**
     * Saves the model state to a file.
     *
     * @param path the path of the file to save to
     * @throws IOException if an I/O error occurs
     */
    public void saveToFile(String path) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(path);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(this);
        objectStream.close();
        fileStream.close();
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, playlists, albums, musics);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        SpotifUMModel that = (SpotifUMModel) obj;
        return users.equals(that.users) &&
                playlists.equals(that.playlists) &&
                albums.equals(that.albums) &&
                musics.equals(that.musics);
    }

    @Override
    public SpotifUMModel clone() {
        return new SpotifUMModel(this);
    }

    @Override
    public String toString() {
        return "SpotifUMModel{" +
                "usersCount=" + users.size() +
                ", playlistsCount=" + playlists.size() +
                ", albumsCount=" + albums.size() +
                ", musicsCount=" + musics.size() +
                '}';
    }

}