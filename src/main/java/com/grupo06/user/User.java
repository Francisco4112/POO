package com.grupo06.user;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.grupo06.playable.Music;
import com.grupo06.playable.Playable;
import com.grupo06.playable.playlist.CreatedPlaylist;
import com.grupo06.playable.playlist.Playlist;
import com.grupo06.playable.Album;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Represents a user in the music streaming system.
 */

public class User implements Serializable {
    private String username;
    private String name;
    private String email;
    private String address;
    private double points;
    private List<Playable> library;
    private Map<LocalDate, List<Music>> listenedMusics;
    private SubscriptionPlan plan;

    /**
     * Constructs an empty User with default values.
     */
    public User() {
        this.username = "";
        this.name = "";
        this.email = "";
        this.address = "";
        this.points = 0;
        this.library = new ArrayList<>();
        this.listenedMusics = new HashMap<>();
        this.plan = new Free();
    }

    /**
     * Constructs a User with basic information.
     * 
     * @param username the unique username identifier
     * @param name     the full name of the user
     * @param email    the user's email address
     * @param address  the user's physical address
     */
    public User(String username, String name, String email, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.points = 0;
        this.library = new ArrayList<>();
        this.listenedMusics = new HashMap<>();
        this.plan = new Free();
    }

    /**
     * Constructs a User with complete information.
     * 
     * @param username       the unique username identifier
     * @param name           the full name of the user
     * @param email          the user's email address
     * @param address        the user's physical address
     * @param plan           the user's subscription plan
     * @param pontos         the user's current points
     * @param library        the user's music library
     * @param listenedMusics the user's listening history
     */
    public User(String username, String name, String email, String address, SubscriptionPlan plan, double pontos,
            List<Playable> library, Map<LocalDate, List<Music>> listenedMusics) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.points = pontos;
        this.library = new ArrayList<>(library);
        this.listenedMusics = new HashMap<>();
        for (Map.Entry<LocalDate, List<Music>> entry : listenedMusics.entrySet()) {
            this.listenedMusics.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        this.plan = plan;
    }

    /**
     * Copy constructor that creates a deep copy of another User.
     * 
     * @param user the User to copy
     */
    public User(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.plan = user.getPlan();
        this.points = user.getPoints();
        this.library = new ArrayList<>(user.getLibrary());
        setListenedMusics(user.getListenedMusics());
    }

    /**
     * Returns the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * 
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the user's full name.
     * 
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     * 
     * @param name the new full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's email address.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's physical address.
     * 
     * @return the physical address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the user's physical address.
     * 
     * @param address the new physical address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the user's subscription plan.
     * 
     * @return the subscription plan
     */
    public SubscriptionPlan getPlan() {
        return plan;
    }

    /**
     * Sets the user's subscription plan.
     * <p>
     * Automatically awards 100 points if upgrading to PremiumTop plan.
     * 
     * @param plan the new subscription plan
     */
    public void setPlan(SubscriptionPlan plan) {
        if (plan instanceof PremiumTop) {
            this.points += 100;
        }
        this.plan = plan;
    }

    /**
     * Returns the user's current points.
     * 
     * @return the current points
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the user's points.
     * 
     * @param points the new points value
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /**
     * Returns a copy of the user's library.
     * 
     * @return a new ArrayList containing all library items
     */
    public List<Playable> getLibrary() {
        return new ArrayList<>(this.library);
    }

    /**
     * Replaces the user's library with a copy of the provided list.
     * 
     * @param library the new library contents
     */
    public void setLibrary(List<Playable> library) {
        this.library = new ArrayList<>(library);
    }

    /**
     * Returns a copy of the user's listening history.
     * 
     * @return a new HashMap containing the listening history
     */
    public Map<LocalDate, List<Music>> getListenedMusics() {
        Map<LocalDate, List<Music>> res = new HashMap<>();
        for (Map.Entry<LocalDate, List<Music>> entry : this.listenedMusics.entrySet()) {
            res.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return res;
    }

    /**
     * Finds and returns a playlist by name from the user's library.
     * 
     * @param name the name of the playlist to find
     * @return the found playlist, or null if not found
     */
    public Playlist getPlaylist(String name) {
        for (Playable p : this.library) {
            if (p instanceof Playlist) {
                Playlist playlist = (Playlist) p;
                if (playlist.getName().equals(name)) {
                    return playlist;
                }
            }
        }
        return null;
    }

    /**
     * Replaces the user's listening history with a copy of the provided map.
     * 
     * @param listenedMusics the new listening history
     */
    public void setListenedMusics(Map<LocalDate, List<Music>> listenedMusics) {
        this.listenedMusics = new HashMap<>();
        for (Map.Entry<LocalDate, List<Music>> entry : listenedMusics.entrySet()) {
            this.listenedMusics.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    /**
     * Adds a playable item to the user's library.
     * 
     * @param music the playable item to add
     */
    public void addMusicToLibrary(Playable music) {
        this.library.add(music);
    }

    /**
     * Removes a playable item from the user's library.
     * 
     * @param music the playable item to remove
     */
    public void removeMusicFromLibrary(Playable music) {
        this.library.remove(music);
    }

    /**
     * Finds and returns a created playlist by name from the user's library.
     * 
     * @param name the name of the playlist to find
     * @return the found created playlist, or null if not found
     */
    public CreatedPlaylist getCreatePlayListFromLibrary(String name) {
        CreatedPlaylist foundPlaylist = null;
        for (Playable p : this.library) {
            if (p instanceof CreatedPlaylist) {
                CreatedPlaylist playlist = (CreatedPlaylist) p;
                if (playlist.getName().equals(name)) {
                    foundPlaylist = playlist;
                    break;
                }
            }
        }
        return foundPlaylist;
    }

    /**
     * Returns all playlists from the user's library.
     * 
     * @return an ArrayList of all playlists
     */
    public ArrayList<Playlist> getPlaylists() {
        ArrayList<Playlist> playLists = new ArrayList<>();
        for (Playable p : this.library) {
            if (p instanceof Playlist) {
                playLists.add((Playlist) p);
            }
        }
        return playLists;
    }

    /**
     * Returns a list of string representations of all items in the library.
     * 
     * @return an ArrayList of string representations
     */
    public ArrayList<String> listLibrary() {
        return this.library.stream()
                .map(Playable::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Finds and returns an album by name from the user's library.
     * 
     * @param name the name of the album to find in format "musician:albumName"
     * @return the found album, or null if not found
     */
    public Album getAlbumFromLibrary(String name) {
        Album foundAlbum = null;
        for (Playable p : this.library) {
            if (p instanceof Album) {
                Album album = (Album) p;
                if ((album.getMusician() + ":" + album.getName()).equals(name)) {
                    foundAlbum = album;
                    break;
                }
            }
        }
        return foundAlbum;
    }

    /**
     * Finds and returns a playable item by name and type from the user's library.
     * 
     * @param name the name of the item to find
     * @param type the type of item ("Album" or "Playlist")
     * @return the found playable item, or null if not found
     */
    public Playable getPlayableFromLibrary(String name, String type) {
        Playable foundPlayable = null;
        if (type.equals("Album")) {
            for (Playable p : this.library) {
                if (p instanceof Album) {
                    Album album = (Album) p;
                    if ((album.getMusician() + ":" + album.getName()).equals(name)) {
                        foundPlayable = album;
                        break;
                    }
                }
            }
        } else if (type.equals("Playlist")) {
            for (Playable p : this.library) {
                if (p instanceof Playlist) {
                    Playlist playlist = (Playlist) p;
                    if (playlist.getName().equals(name)) {
                        foundPlayable = playlist;
                        break;
                    }
                }
            }
        }
        return foundPlayable;
    }

    /**
     * Checks if a playlist exists in the user's library.
     * 
     * @param playListName the name of the playlist to check
     * @return true if the playlist exists, false otherwise
     */
    public boolean existPlayList(String playListName) {
        for (Playable p : this.library) {
            if (p instanceof Playlist) {
                Playlist playlist = (Playlist) p;
                if (playlist.getName().equals(playListName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a music track to the listening history for a specific date.
     * 
     * @param date  the date when the music was listened to
     * @param music the music track that was listened to
     */
    public void addListenedMusic(LocalDate date, Music music) {
        if (!this.listenedMusics.containsKey(date)) {
            this.listenedMusics.put(date, new ArrayList<>());
        }
        this.listenedMusics.get(date).add(music);
    }

    /**
     * Updates the user's points based on the current subscription plan.
     */
    public void atualizaPontos() {
        this.points = this.plan.calculatePoints(this.points);
    }

    /**
     * Returns a string representation of the user including profile information,
     * library contents, and listening history.
     * 
     * @return a formatted string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== User profile ===\n")
                .append("Username: ").append(this.username).append("\n")
                .append("Full name: ").append(this.name).append("\n")
                .append("Email: ").append(this.email).append("\n")
                .append("Address: ").append(this.address).append("\n")
                .append("Points: ").append(String.format("%.2f", this.points)).append("\n")
                .append("Subscription plan: ").append(this.plan != null ? this.plan.toString() : "No plan")
                .append("\n\n=== Library ===\n")
                .append("Items in library: ").append(this.library != null ? this.library.size() : 0).append("\n")
                .append("\n=== History ===\n");

        if (this.listenedMusics == null || this.listenedMusics.isEmpty()) {
            sb.append("History is empty.\n");
        } else {
            this.listenedMusics.forEach((date, musics) -> {
                sb.append("\n").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(":\n");
                if (musics != null && !musics.isEmpty()) {
                    for (int i = 0; i < musics.size(); i++) {
                        sb.append("  ").append(i + 1).append(". ")
                                .append(musics.get(i).getName()).append("\n");
                    }
                }
            });
        }
        return sb.toString();
    }

    /**
     * Creates and returns a deep copy of this user.
     * 
     * @return a new User that is a copy of this user
     */
    @Override
    public User clone() {
        return new User(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points, username, name, email, address, plan, library, listenedMusics);
    }

    /**
     * Compares this user to another object for equality.
     * 
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Double.compare(user.points, points) == 0 &&
                username.equals(user.username) &&
                name.equals(user.name) &&
                email.equals(user.email) &&
                address.equals(user.address) &&
                plan.equals(user.plan) &&
                library.equals(user.library) &&
                listenedMusics.equals(user.listenedMusics);
    }

}