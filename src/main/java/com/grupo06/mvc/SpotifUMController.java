package com.grupo06.mvc;

import com.grupo06.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.Serializable;
import com.grupo06.user.SubscriptionPlan;
import com.grupo06.user.SubscriptionPlanType;
import com.grupo06.playable.Music;
import com.grupo06.playable.MusicExplicit;
import com.grupo06.playable.MusicMultimedia;
import com.grupo06.playable.Playable;
import com.grupo06.playable.playlist.*;
import com.grupo06.queries.*;
import com.grupo06.exceptions.*;
import com.grupo06.playable.Album;
import java.time.LocalDate;

/**
 * Controller class for the SpotifUM application, handling user, album, music,
 * and playlist operations by delegating to the underlying model.
 */

public class SpotifUMController implements Serializable {
    /** The model instance used by this controller. */
    private SpotifUMModel model;

    /**
     * Creates a new SpotifUMController with a new model instance.
     */
    public SpotifUMController() {
        this.model = new SpotifUMModel();
    }

    /**
     * Creates a new SpotifUMController with the specified model.
     *
     * @param model the SpotifUMModel instance to use
     */
    public SpotifUMController(SpotifUMModel model) {
        this.model = model;
    }

    /**
     * Creates a new SpotifUMController copying the model from another controller.
     *
     * @param controller the controller to copy the model from
     */
    public SpotifUMController(SpotifUMController controller) {
        this.model = controller.getModel();
    }

    /**
     * Returns a deep copy of the model instance used by this controller.
     *
     * @return the SpotifUMModel instance
     */
    public SpotifUMModel getModel() {
        return this.model.clone();
    }

    /**
     * Inner class implementing the QueryData interface to provide access to model
     * data
     * for executing queries.
     */
    private class ControllerQuery implements QueryData {
        @Override
        public Map<String, User> getUsers() {
            return model.getUsers();
        }

        @Override
        public Map<String, Album> getAlbums() {
            return model.getAlbums();
        }

        @Override
        public Map<String, Music> getMusics() {
            return model.getMusics();
        }

        @Override
        public Map<String, Playlist> getPlaylists() {
            return model.getPlaylists();
        }

    }

    /**
     * Runs a query on the data model.
     *
     * @param <T>   the type of the query result
     * @param query the query to execute
     * @return the result of the query execution
     */
    public <T> T runQuery(Query<T> query) throws UserDoesNotExistException, PlaylistDoesNotExistException,
            AlbumDoesNotExistException, MusicDoesNotExistException {
        if (model.getUsers().size() == 0) {
            throw new UserDoesNotExistException("The users list is empty");
        } else if (model.getPlaylists().size() == 0) {
            throw new PlaylistDoesNotExistException("The playlist list is empty");
        } else if (model.getAlbums().size() == 0) {
            throw new AlbumDoesNotExistException("The album list is empty");
        } else if (model.getMusics().size() == 0) {
            throw new MusicDoesNotExistException("The music list is empty");
        } else {
            return query.execute(new ControllerQuery());
        }
    }

    /**
     * Creates a new user and adds it to the model.
     *
     * @param username the user's username
     * @param name     the user's name
     * @param email    the user's email
     * @param address  the user's address
     * @throws UserAlreadyExistsException if a user with the same username already
     *                                    exists
     */
    public void createUser(String username, String name, String email, String address)
            throws UserAlreadyExistsException {
        User u = new User(username, name, email, address);

        model.addUser(u);
    }

    /**
     * Creates a new album and adds it to the model.
     *
     * @param name          the album's name
     * @param musician      the musician or band name
     * @param publisherName the publisher's name
     * @return zero (placeholder return value)
     * @throws AlbumAlreadyExistsException if an album with the same name already
     *                                     exists
     */
    public int createAlbum(String name, String musician, String publisherName) throws AlbumAlreadyExistsException {
        Album a = new Album(name, musician, publisherName);
        model.addAlbum(a);

        return 0;
    }

    /**
     * Creates a new music track of a specified type and adds it to an album.
     *
     * @param album          the album name
     * @param musician       the musician name
     * @param name           the music track name
     * @param lyrics         the lyrics of the track
     * @param musicNotes     list of music notes
     * @param genre          the genre of the music
     * @param durationInSec  duration in seconds
     * @param type           type of music (1 = explicit, 2 = multimedia, 3 =
     *                       simple)
     * @param ageRestriction age restriction for explicit music
     * @param videoPath      path to the video (for multimedia type)
     * @param resolution     video resolution (for multimedia type)
     * @throws MusicAlreadyExistsException if the music track already exists
     * @throws AlbumDoesNotExistException  if the specified album does not exist
     * @throws IllegalArgumentException    if an invalid type is specified
     */
    public void createMusic(String album, String musician, String name, String lyrics, List<String> musicNotes,
            String genre,
            int durationInSec, int type, int ageRestriction, String videoPath, String resolution)
            throws MusicAlreadyExistsException, AlbumDoesNotExistException {
        if (type == 1) {
            MusicExplicit musicExplicit = new MusicExplicit(name, lyrics, musicNotes, genre, durationInSec,
                    ageRestriction);
            model.addMusic(album, musician, musicExplicit);
        } else if (type == 2) {
            MusicMultimedia musicMultimedia = new MusicMultimedia(name, lyrics, musicNotes, genre, durationInSec,
                    videoPath, resolution);
            model.addMusic(album, musician, musicMultimedia);
        } else if (type == 3) {
            Music musica = new Music(name, lyrics, musicNotes, genre, durationInSec);
            model.addMusic(album, musician, musica);
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    /**
     * Removes a music track from a user's private playlist.
     *
     * @param PlayListName the playlist name
     * @param username     the username
     * @param MusicName    the music track name
     * @param MusicianName the musician name
     * @param AlbumName    the album name
     * @throws UserDoesNotExistException        if the user does not exist
     * @throws MusicDoesNotExistException       if the music does not exist
     * @throws PlaylistDoesNotExistException    if the playlist does not exist
     * @throws PlaylistAlreadyIsPublicException if the playlist is already public
     */
    public void removeMusicFromPrivatePlayList(String PlayListName, String username, String MusicName,
            String MusicianName, String AlbumName)
            throws UserDoesNotExistException, MusicDoesNotExistException, PlaylistDoesNotExistException,
            PlaylistAlreadyIsPublicException {
        String keyMusic = MusicianName + ":" + AlbumName + ":" + MusicName;
        User user = new User();
        Music music = new Music();
        model.removeMusicFromPrivatePlayList(PlayListName, keyMusic, user, username, music);
    }

    /**
     * Returns a list of subscription plans available to the user except the current
     * plan.
     *
     * @param currentPlan the user's current subscription plan
     * @return list of available subscription plan types
     */
    public List<SubscriptionPlanType> getAvailablePlans(SubscriptionPlan currentPlan) {
        return Arrays.stream(SubscriptionPlanType.values())
                .filter(planType -> planType != currentPlan.getType()) // Remove o plano atual
                .collect(Collectors.toList());
    }

    /**
     * Changes the subscription plan of a user.
     *
     * @param username the username
     * @param planType the new subscription plan type
     */
    public void changeUserPlan(String username, SubscriptionPlanType planType) {
        changePlanUser(username, planType.createInstance());
    }

    /**
     * Retrieves the list of music tracks in a specific album.
     *
     * @param albumName    the album name
     * @param musicianName the musician name
     * @return list of music tracks in the album
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public ArrayList<Music> getAlbumsMusics(String albumName, String musicianName) throws AlbumDoesNotExistException {
        String keyAlbum = musicianName + ":" + albumName;
        Album a = new Album();
        return model.getAlbumMusics(keyAlbum, a);
    }

    /**
     * Retrieves the list of music tracks in a specific album from a user's library.
     *
     * @param albumName    the album name
     * @param musicianName the musician name
     * @param username     the username
     * @return list of music tracks in the album from the user's library
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public ArrayList<Music> getAlbumsMusicsFromLibrary(String albumName, String musicianName, String username)
            throws AlbumDoesNotExistException {
        String keyAlbum = musicianName + ":" + albumName;
        Album a = new Album();
        User u = new User();
        return model.getAlbumMusicsFromLibrary(keyAlbum, username, u, a);
    }

    /**
     * Creates a new private playlist for a user.
     *
     * @param playListName the name of the playlist
     * @param username     the username
     * @return true if playlist was created successfully
     * @throws UserDoesNotExistException      if the user does not exist
     * @throws PlaylistAlreadyExistsException if the playlist already exists
     */
    public boolean createPlaylist(String playListName, String username) throws UserDoesNotExistException,
            PlaylistAlreadyExistsException {
        List<Music> musics = new ArrayList<>();
        User u = model.getUser(username);
        playListName = username + ":" + playListName;
        if (!u.existPlayList(playListName)) {
            CreatedPlaylist p = new CreatedPlaylist(playListName, true, musics);
            model.addPrivatePlaylist(username, p, u);
            return true;
        } else {
            throw new PlaylistAlreadyExistsException(playListName);
        }
    }

    /**
     * Generates a playlist based on user preferences and criteria.
     *
     * @param username the username
     * @param type     the type of playlist to generate (1, 2, or 3)
     * @param duration the max duration in seconds (used for type 2)
     * @return a FavouriteList representing the generated playlist
     * @throws PlaylistException if there is an error generating the playlist
     */
    public FavouriteList generatePlaylist(String username, int type, int duration) throws PlaylistException {
        FavouriteList playlist = new FavouriteList();
        switch (type) {
            case 1:
                try {
                    playlist = model.generatePreferencesPlaylist(username);
                } catch (PlaylistException e) {
                    throw new PlaylistException(e.getMessage());
                }
                break;
            case 2:
                try {
                    playlist = model.generatePreferencesPlaylistWithMaxTime(username, duration);
                } catch (PlaylistException e) {
                    throw new PlaylistException(e.getMessage());
                }
                break;
            case 3:
                try {
                    playlist = model.generatePreferencesPlaylistWithOnlyExplicit(username);
                } catch (PlaylistException e) {
                    throw new PlaylistException(e.getMessage());
                }
                break;
            default:
                break;
        }
        return playlist;
    }

    /**
     * Returns a random playlist.
     *
     * @return list of musics in the random playlist
     */
    public ArrayList<Music> getRandomPlaylist() {
        List<Music> musics = new ArrayList<>();
        RandomPlaylist p = new RandomPlaylist("random", musics);

        model.createRandomPlaylist(p);

        return p.getMusics();
    }

    /**
     * Retrieves the list of musics from a user's playlist.
     *
     * @param playListName the playlist name
     * @param username     the username
     * @return list of musics from the playlist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public ArrayList<Music> getPlaylistMusics(String playListName, String username)
            throws PlaylistDoesNotExistException {
        Playlist p = new CreatedPlaylist();
        User u = new User();
        return model.getMusicsFromPlayList(playListName, username, p, u);
    }

    /**
     * Retrieves the list of musics from a public playlist.
     *
     * @param playListName the playlist name
     * @return list of musics from the public playlist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public ArrayList<Music> getPublicPlaylistMusics(String playListName) throws PlaylistDoesNotExistException {
        Playlist p = new CreatedPlaylist();
        return model.getMusicsFromPublicPlayList(playListName, p);
    }

    /**
     * Makes a user's playlist public.
     *
     * @param username     the username
     * @param playListName the playlist name
     * @return the playlist name if successful
     * @throws UserDoesNotExistException        if the user does not exist
     * @throws PlaylistDoesNotExistException    if the playlist does not exist
     * @throws PlaylistAlreadyIsPublicException if the playlist is already public
     */
    public String turnPlaylistPublic(String username, String playListName)
            throws UserDoesNotExistException, PlaylistDoesNotExistException, PlaylistAlreadyIsPublicException {
        User u = new User();
        return model.turnPlaylistPublic(playListName, username, u);
    }

    /**
     * Creates a new public playlist.
     *
     * @param playListName the playlist name
     * @return the created playlist name
     * @throws PlaylistAlreadyExistsException if the playlist already exists
     */
    public String createPublicPlayList(String playListName) throws PlaylistAlreadyExistsException {
        List<Music> musics = new ArrayList<>();
        Playlist p = new CreatedPlaylist(playListName, false, musics);
        model.addPublicPlaylist(p);
        return playListName;
    }

    /**
     * Retrieves the subscription plan of a user.
     *
     * @param username the username
     * @return the user's subscription plan
     */
    public SubscriptionPlan getUserPlan(String username) {
        User u = new User();
        return model.getUserPlan(username, u);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username
     * @return the User object
     * @throws UserDoesNotExistException if the user does not exist
     */
    public User getUser(String username) throws UserDoesNotExistException {
        // If the user does not exist
        User u = model.getUser(username);
        if (u == null) {
            throw new UserDoesNotExistException(username);
        }

        return u;
    }

    /**
     * Changes a user's subscription plan.
     *
     * @param username the username
     * @param plan     the new subscription plan instance
     */
    public void changePlanUser(String username, SubscriptionPlan plan) {
        User u = new User();
        model.changePlanUser(u, plan, username);
    }

    /**
     * Removes a public playlist by name.
     *
     * @param playListName the name of the playlist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void removePlayListPublic(String playListName) throws PlaylistDoesNotExistException {
        model.removePlayListPublic(playListName);
    }

    /**
     * Adds a music track to a playlist.
     *
     * @param playListName the playlist name
     * @param albumName    the album name
     * @param MusicianName the musician name
     * @param MusicName    the music name
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     * @throws MusicDoesNotExistException    if the music does not exist
     */
    public void addMusicToPlayList(String playListName, String albumName, String MusicianName, String MusicName)
            throws PlaylistDoesNotExistException, MusicDoesNotExistException {
        String keyMusic = MusicianName + ":" + albumName + ":" + MusicName;
        Playlist p = model.getPlaylist(playListName);
        Music m = model.getMusic(keyMusic);

        if (p == null) {
            throw new PlaylistDoesNotExistException(playListName);
        } else if (m == null) {
            throw new MusicDoesNotExistException(keyMusic);
        }
        p.addMusic(m);
    }

    /**
     * Adds a music track to a user's private playlist.
     *
     * @param playListName the playlist name
     * @param albumName    the album name
     * @param MusicianName the musician name
     * @param MusicName    the music name
     * @param username     the username
     * @throws UserDoesNotExistException        if the user does not exist
     * @throws MusicDoesNotExistException       if the music does not exist
     * @throws PlaylistDoesNotExistException    if the playlist does not exist
     * @throws PlaylistAlreadyIsPublicException if the playlist is already public
     */
    public void addMusicToPrivatePlayList(String playListName, String albumName, String MusicianName,
            String MusicName, String username)
            throws UserDoesNotExistException, MusicDoesNotExistException, PlaylistDoesNotExistException,
            PlaylistAlreadyIsPublicException {
        String keyMusic = MusicianName + ":" + albumName + ":" + MusicName;
        User u = new User();
        Playlist p = new CreatedPlaylist();
        Music m = new Music();

        model.addMusicToPrivatePlayList(playListName, keyMusic, u, username, m);

        p.addMusic(m);
    }

    /**
     * Adds a playlist to a user's library.
     *
     * @param playListName the playlist name
     * @param username     the username
     * @throws UserDoesNotExistException     if the user does not exist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void addPlayListToLibrary(String playListName, String username)
            throws UserDoesNotExistException, PlaylistDoesNotExistException {
        User u = new User();
        model.addPlayListToLibrary(playListName, username, u);
    }

    /**
     * Adds an album to a user's library.
     *
     * @param albumName    the album name
     * @param musicianName the musician name
     * @param username     the username
     * @throws UserDoesNotExistException  if the user does not exist
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public void addAlbumToLibrary(String albumName, String musicianName, String username)
            throws UserDoesNotExistException, AlbumDoesNotExistException {
        User u = new User();
        String AlbumKey = musicianName + ":" + albumName;
        model.addAlbumToLibrary(AlbumKey, username, u);
    }

    /**
     * Removes an album from a user's library.
     *
     * @param albumName    the album name
     * @param musicianName the musician name
     * @param username     the username
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public void removeAlbumFromLibrary(String albumName, String musicianName, String username)
            throws AlbumDoesNotExistException {
        User u = new User();
        String AlbumKey = musicianName + ":" + albumName;
        model.removeAlbumFromLibrary(AlbumKey, username, u);
    }

    /**
     * Removes a playlist from a user's library.
     *
     * @param playListName the playlist name
     * @param username     the username
     * @throws UserDoesNotExistException     if the user does not exist
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void removePlayListFromLibrary(String playListName, String username)
            throws UserDoesNotExistException, PlaylistDoesNotExistException {
        User u = new User();
        model.removePlayListFromLibrary(playListName, username, u);
    }

    /**
     * Removes an album by musician and album name.
     *
     * @param MusicianName the musician name
     * @param AlbumName    the album name
     * @throws AlbumDoesNotExistException if the album does not exist
     * @throws MusicDoesNotExistException if related music does not exist
     */
    public void removeAlbum(String MusicianName, String AlbumName)
            throws AlbumDoesNotExistException, MusicDoesNotExistException {
        String AlbumKey = MusicianName + ":" + AlbumName;
        model.removeAlbum(AlbumKey);
    }

    /**
     * Removes a music track from an album.
     *
     * @param MusicianName the musician name
     * @param AlbumName    the album name
     * @param MusicName    the music name
     * @throws MusicDoesNotExistException if the music does not exist
     */
    public void removeMusic(String MusicianName, String AlbumName, String MusicName)
            throws MusicDoesNotExistException {
        String AlbumKey = MusicianName + ":" + AlbumName;
        String MusicKey = MusicianName + ":" + AlbumName + ":" + MusicName;
        model.removeMusic(MusicKey, AlbumKey);
    }

    /**
     * Removes a user by username.
     *
     * @param username the username
     * @throws UserDoesNotExistException if the user does not exist
     */
    public void removeUser(String username) throws UserDoesNotExistException {
        model.removeUser(username);
    }

    /**
     * Removes a user's playlist.
     *
     * @param playListName the playlist name
     * @param username     the username
     * @throws PlaylistDoesNotExistException if the playlist does not exist
     */
    public void removePlaylist(String playListName, String username) throws PlaylistDoesNotExistException {
        String key = username + ":" + playListName;
        model.removePlayList(key, playListName, username);
    }

    /**
     * Checks if an album exists.
     *
     * @param Album    the album name
     * @param Musician the musician name
     * @return true if the album exists, false otherwise
     */
    public boolean existAlbum(String Album, String Musician) {
        String albumKey = Musician + ":" + Album;
        return model.existAlbum(albumKey);
    }

    /**
     * Prints a formatted table of all albums.
     *
     * @return the albums table as a string
     */
    public String printAlbumsTable() {
        Map<String, Album> albums = model.getAlbums();
        StringBuilder sb = new StringBuilder();

        String separator = "+----------------------+----------------------+----------------------+------------------+";
        sb.append(separator).append("\n");
        sb.append(String.format("| %-20s | %-20s | %-20s | %-16s |\n",
                "Album", "Musician", "Publisher", "Music Count"));
        sb.append(separator).append("\n");

        for (Album album : albums.values()) {
            String name = truncate(album.getName(), 20);
            String musician = truncate(album.getMusician(), 20);
            String publisher = truncate(album.getPublisherName(), 20);
            int musicCount = album.getMusics() != null ? album.getMusics().size() : 0;

            sb.append(String.format("| %-20s | %-20s | %-20s | %-16d |\n",
                    name, musician, publisher, musicCount));
        }

        sb.append(separator);
        return sb.toString();
    }

    /**
     * Prints a formatted table of all music tracks.
     *
     * @return the musics table as a string
     */
    public String printMusicsTable() {
        Map<String, Music> musicMap = model.getMusics();

        StringBuilder sb = new StringBuilder();

        String separator = "+----------------------+----------------+----------+--------+---------------+";
        sb.append(separator).append("\n");
        sb.append(String.format("| %-20s | %-14s | %-8s | %-6s | %-13s |\n",
                "Name", "Genre", "Duration", "Plays", "Type"));
        sb.append(separator).append("\n");

        for (Music music : musicMap.values()) {
            System.out.println(music.toString());
            String name = truncate(music.getName(), 20);
            String genre = truncate(music.getGenre(), 14);
            String duration = formatDuration(music.getDurationInSec());
            int plays = music.getPlayCount();
            String type = "Simple";

            if (music instanceof MusicExplicit) {
                type = "Explicit";
            } else if (music instanceof MusicMultimedia) {
                type = "Multimedia";
            }

            sb.append(String.format("| %-20s | %-14s | %-8s | %-6d | %-13s |\n",
                    name, genre, duration, plays, type));
        }

        sb.append(separator);
        return sb.toString();
    }

    private static String formatDuration(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    /**
     * Prints a formatted list of playlist names.
     *
     * @return the playlist names table
     */
    public String printPlaylistNames() {
        Map<String, Playlist> playlists = model.getPlaylists();
        StringBuilder sb = new StringBuilder();

        String separator = "+----------------------+";
        sb.append(separator).append("\n");
        sb.append(String.format("| %-20s |\n", "Playlist Name"));
        sb.append(separator).append("\n");

        for (Playlist playlist : playlists.values()) {
            sb.append(String.format("| %-20s |\n", truncate(playlist.getName(), 20)));
        }

        sb.append(separator);
        return sb.toString();
    }

    /**
     * Prints a formatted table of all users.
     *
     * @return the users table as a string
     */
    public String printUsersTable() {
        Map<String, User> users = model.getUsers();
        StringBuilder sb = new StringBuilder();

        String separator = "+".repeat(171);
        sb.append(separator).append("\n");
        sb.append(String.format("| %-15s | %-20s | %-25s | %-20s | %-8s | %-10s | %-20s | %-30s |\n",
                "Username", "Name", "Email", "Address", "Points", "Plan", "Library", "Listened Musics"));
        sb.append(separator).append("\n");

        for (User user : users.values()) {
            // Format library
            String libraryStr = user.getLibrary() != null && !user.getLibrary().isEmpty()
                    ? joinNames(user.getLibrary(), 3) // show up to 3
                    : "Empty";

            // Format listened musics
            String listenedStr = user.getListenedMusics() != null && !user.getListenedMusics().isEmpty()
                    ? summarizeHistory(user.getListenedMusics(), 2)
                    : "None";

            sb.append(String.format("| %-15s | %-20s | %-25s | %-20s | %-8.2f | %-10s | %-20s | %-30s |\n",
                    user.getUsername(),
                    truncate(user.getName(), 20),
                    truncate(user.getEmail(), 25),
                    truncate(user.getAddress(), 20),
                    user.getPoints(),
                    user.getPlan().toString(),
                    truncate(libraryStr, 20),
                    truncate(listenedStr, 30)));
        }

        sb.append(separator);
        return sb.toString();
    }

    // Helper to truncate long strings
    private static String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }

    // Helper to join music names or IDs from Playable list
    private static String joinNames(List<Playable> playables, int maxItems) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < Math.min(playables.size(), maxItems); i++) {
            names.add(playables.get(i).getName());
        }
        if (playables.size() > maxItems)
            names.add("...");
        return String.join(", ", names);
    }

    // Helper to summarize listening history
    private static String summarizeHistory(Map<LocalDate, List<Music>> history, int maxDays) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Map.Entry<LocalDate, List<Music>> entry : history.entrySet()) {
            if (count++ >= maxDays) {
                sb.append("...");
                break;
            }
            String dateStr = entry.getKey().toString();
            String musicList = entry.getValue().stream()
                    .limit(2)
                    .map(Music::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            sb.append(dateStr).append(": ").append(musicList).append(" | ");
        }
        return sb.toString();
    }

    /**
     * Lists all music names.
     *
     * @return list of music names
     */
    public ArrayList<String> listMusics() {
        return model.listMusics();
    }

    /**
     * Lists all album names.
     *
     * @return list of album names
     */
    public ArrayList<String> listAlbums() {
        return model.listAlbums();
    }

    /**
     * Lists all playlist names.
     *
     * @return list of playlist names
     */
    public ArrayList<String> listPlaylists() {
        return model.listPlaylists();
    }

    /**
     * Lists the contents of a user's library.
     *
     * @param username the username
     * @return list of user's library items
     */
    public ArrayList<String> listUserLibrary(String username) {
        return model.listUserLibrary(username);
    }

    /**
     * Retrieves a music object by name, album, and musician.
     *
     * @param MusicName    the music name
     * @param AlbumName    the album name
     * @param MusicianName the musician name
     * @return the music object
     * @throws MusicDoesNotExistException if the music does not exist
     */
    public Music getMusic(String MusicName, String AlbumName, String MusicianName) throws MusicDoesNotExistException {
        String musicKey = MusicianName + ":" + AlbumName + ":" + MusicName;
        return model.getMusic(musicKey);
    }

    /**
     * Retrieves an album by musician and album name.
     *
     * @param MusicianName the musician name
     * @param AlbumName    the album name
     * @return the album object
     * @throws AlbumDoesNotExistException if the album does not exist
     */
    public Album getAlbum(String MusicianName, String AlbumName) throws AlbumDoesNotExistException {
        return model.getAlbum(MusicianName, AlbumName);
    }

    /**
     * Updates user data when they listen to a music track.
     *
     * @param username the username
     * @param music    the music track
     * @throws UserDoesNotExistException if the user does not exist
     */
    public void atualizaWhenUserListenMusic(String username, Music music) throws UserDoesNotExistException {
        User u = new User();
        model.atualizaWhenUserListenMusic(username, u, music);
    }

    /**
     * Loads the data of the application from a file.
     *
     * @param path Path to the file.
     * @throws SpotifUMControllerException Failure to load from file.
     */
    public void loadFromFile(String path) throws SpotifUMControllerException {
        try {
            this.model.loadFromFile(path);
        } catch (IOException e) {
            throw new SpotifUMControllerException("Failed to read file contents!");
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new SpotifUMControllerException("Invalid file contents!");
        }
    }

    /**
     * Saves the data of the application to a file.
     *
     * @param path Path to the file.
     * @throws SpotifUMControllerException Failure to write to file.
     */
    public void saveToFile(String path) throws SpotifUMControllerException {
        try {
            this.model.saveToFile(path);
        } catch (IOException e) {
            throw new SpotifUMControllerException("Failed to write to file!");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    /**
     * Checks if this SpotifUM controller is equal to another object.
     *
     * @param obj Object to be compared with this SpotifUM controller.
     * @return Whether this is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        SpotifUMController that = (SpotifUMController) obj;
        return this.model.equals(that.getModel());
    }

    /**
     * Creates a deep copy of this SpotifUM controller.
     *
     * @return A deep copy of this SpotifUM controller.
     */
    @Override
    public SpotifUMController clone() {
        return new SpotifUMController(this);
    }

    /**
     * Creates a debug string representation of this SpotifUM controller.
     *
     * @return A debug string representation of this SpotifUM controller.
     */
    @Override
    public String toString() {
        return "SpotifUMController" +
                "\nmodel = " + this.model.toString();
    }
}
