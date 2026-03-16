package com.grupo06.mvc;

import com.grupo06.exceptions.*;
import com.grupo06.playable.*;
import com.grupo06.playable.playlist.*;
import com.grupo06.queries.MostListenedGenre;
import com.grupo06.queries.MostListenedMusic;
import com.grupo06.queries.MostListenedMusician;
import com.grupo06.queries.NumberOfPublicPlaylists;
import com.grupo06.queries.UserWithMostListenedMusicsEver;
import com.grupo06.queries.UserWithMostListenedMusicsWithDates;
import com.grupo06.queries.UserWithMostPlaylists;
import com.grupo06.queries.UserWithMostPoints;
import com.grupo06.user.PremiumBase;
import com.grupo06.user.PremiumTop;
import com.grupo06.user.SubscriptionPlan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** The user interaction layer of the SpotifUM application. */
public class SpotifUMView {
    /** The controller that this view interacts with. */
    private SpotifUMController controller;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    /** Creates a new SpotifUM view. */
    public SpotifUMView() {
        this.controller = new SpotifUMController();
    }

    /**
     * Creates a SpotifUM view from the value of its fields.
     *
     * @param controller SpotifUM controller.
     */
    public SpotifUMView(SpotifUMController controller) {
        this.controller = controller;
    }

    /**
     * Copy constructor of a SpotifUM view.
     *
     * @param view SpotifUM view to be copied.
     */
    public SpotifUMView(SpotifUMView view) {
        this.controller = view.getController();
    }

    /**
     * Gets the controller that this view interacts with.
     *
     * @return The controller that this view interacts with.
     */
    private SpotifUMController getController() {
        return this.controller;
    }

    /**
     * Builds a menu entry to allow the user to change their subscription plan.
     *
     * @param input    the UserInput instance to handle user input
     * @param username the username of the user whose plan is being changed
     * @return a MenuOption object representing the "Change subscription plan"
     *         option
     */
    private MenuOption buildPlanChangeMenu(UserInput input, String username) {
        SubscriptionPlan currentPlan = controller.getUserPlan(username);

        return new MenuOption("Change subscription plan", i -> {
            List<MenuOption> planEntries = controller.getAvailablePlans(currentPlan).stream()
                    .map(planType -> new MenuOption(
                            planType.getDisplayName(),
                            j -> controller.changeUserPlan(username, planType)))
                    .collect(Collectors.toList());

            planEntries.add(new MenuOption("Go back", j -> {
            }));
            new Menu(planEntries.toArray(new MenuOption[0])).run();
        });
    }

    /**
     * Builds and runs the main menu entries for the user based on their
     * subscription plan.
     * The menu includes options to play playlists, music, albums, and manage the
     * library,
     * depending on the user's plan capabilities.
     *
     * @param input      the UserInput instance to read user inputs
     * @param username   the username of the current user
     * @param controller the SpotifUMController instance for backend operations
     * @param plan       the SubscriptionPlan of the user, which determines
     *                   available features
     * @return a list of MenuOption objects representing the available menu options
     */
    public List<MenuOption> buildMenuEntries(UserInput input, String username,
            SpotifUMController controller, SubscriptionPlan plan) {
        List<MenuOption> entries = new ArrayList<>();

        if (plan.canPlayRandomPlaylist()) {
            entries.add(new MenuOption("Play a random playlist",
                    i -> {
                        ArrayList<Music> musics = controller.getRandomPlaylist();
                        playMusics(musics, new PremiumBase(), username);
                        buildMenuEntries(input, username, controller, plan);
                    }));
        }

        if (plan.canPlayMusic()) {
            entries.add(new MenuOption("Play music",
                    i -> {
                        String musicName = input.readString("Music name: ");
                        String musicianName = input.readString("Musician name: ");
                        String albumName = input.readString("Album name: ");
                        try {
                            controller.getAlbum(musicianName, albumName);
                        } catch (AlbumDoesNotExistException e) {
                            System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
                            buildMenuEntries(input, username, controller, plan);
                            return;
                        }
                        try {
                            Music music = controller.getMusic(musicName, albumName, musicianName);
                            playMusic(music, username);
                        } catch (MusicDoesNotExistException e) {
                            System.err.println(RED + "Music does not exist: " + e.getMessage() + RESET);
                            buildMenuEntries(input, username, controller, plan);
                            return;
                        }
                        buildMenuEntries(input, username, controller, plan);
                    }));
        }

        if (plan.canPlayAlbum()) {
            entries.add(new MenuOption("Reproduce album",
                    i -> {
                        String albumName = input.readString("Album name: ");
                        String musicianName = input.readString("Musician name: ");
                        try {
                            ArrayList<Music> musics = controller.getAlbumsMusics(albumName, musicianName);
                            playMusics(musics, new PremiumBase(), username);
                        } catch (AlbumDoesNotExistException e) {
                            System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
                            buildMenuEntries(input, username, controller, plan);
                            return;
                        }
                        buildMenuEntries(input, username, controller, plan);
                    }));
        }

        if (plan.canPlayPublicPlaylist()) {
            entries.add(new MenuOption("Play public playlist",
                    i -> {
                        String playlistName = input.readString("Playlist name: ");
                        ArrayList<Music> musics = new ArrayList<>();
                        try {
                            musics = controller.getPublicPlaylistMusics(playlistName);
                        } catch (PlaylistDoesNotExistException e) {
                            System.err.println(RED + "Playlist does not exist: " + e.getMessage() + RESET);
                            buildMenuEntries(input, username, controller, plan);
                            return;
                        }
                        playMusics(musics, new PremiumBase(), username);
                        buildMenuEntries(input, username, controller, plan);
                    }));
        }

        if (plan.canHaveGeneratedPlaylists()) {
            entries.add(new MenuOption("SpotifUM generated playlist", i -> {
                MenuOption[] genPlaylist = {
                        new MenuOption("Generate a playlist based on your preferences",
                                j -> {
                                    try {
                                        FavouriteList l = controller.generatePlaylist(username, 1, 0);
                                        playMusics(l.getMusics(), new PremiumTop(), username);
                                    } catch (PlaylistException e) {
                                        System.err.println(RED + "The playlist is empty" + RESET);
                                    }
                                }),
                        new MenuOption("Generate a playlist based on your preferences with a max duration", j -> {
                            try {
                                int duration = input.readInt(
                                        "Duration: ", "Duration must be a positive integer!", d -> d > 0);
                                FavouriteList l = controller.generatePlaylist(username, 2, duration);
                                playMusics(l.getMusics(), new PremiumTop(), username);
                            } catch (PlaylistException e) {
                                System.err.println(RED + "The playlist is empty" + RESET);
                            }
                        }),
                        new MenuOption("Generate a playlist based on your preferences with only explicit musics", j -> {
                            try {
                                FavouriteList l = controller.generatePlaylist(username, 3, 0);
                                playMusics(l.getMusics(), new PremiumTop(), username);
                            } catch (PlaylistException e) {
                                System.err.println(RED + "The playlist is empty" + RESET);
                            }
                        })
                };
                new Menu(genPlaylist).run();
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canHaveLibrary()) {
            entries.add(new MenuOption("Library", i -> {
                MenuOption[] library = buildLibraryMenu(input, username, controller, plan);
                new Menu(library).run();
            }));
        }

        entries.add(buildPlanChangeMenu(input, username));
        entries.add(new MenuOption("Go back", i -> run()));
        new Menu(entries.toArray(new MenuOption[0])).run();
        return entries;
    }

    /**
     * Builds the library submenu entries for the user, providing options for
     * playing and managing playlists, albums, and library content based on
     * the user's subscription plan permissions.
     *
     * @param input      the UserInput instance to handle user input
     * @param username   the username of the current user
     * @param controller the SpotifUMController instance for backend interactions
     * @param plan       the SubscriptionPlan that determines available library
     *                   features
     * @return an array of MenuOption objects representing the library submenu
     *         options
     */
    private MenuOption[] buildLibraryMenu(UserInput input, String username,
            SpotifUMController controller, SubscriptionPlan plan) {
        List<MenuOption> library = new ArrayList<>();

        if (plan.canPlaySavedPlaylist()) {
            library.add(new MenuOption("Play playlist", j -> {
                String name = input.readString("Playlist name: ");
                try {
                    ArrayList<Music> musics = controller.getPlaylistMusics(name, username);
                    playMusics(musics, new PremiumBase(), username);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                    buildMenuEntries(input, username, controller, plan);
                    return;
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canPlayAlbum()) {
            library.add(new MenuOption("Play album", j -> {
                String name = input.readString("Album name: ");
                String musician = input.readString("Musician name: ");
                try {
                    ArrayList<Music> musics = controller.getAlbumsMusicsFromLibrary(name,
                            musician, username);
                    playMusics(musics, new PremiumBase(), username);
                } catch (AlbumDoesNotExistException e) {
                    System.err.println(
                            RED + "Album does not exist in library: " + e.getMessage() + RESET);
                    buildMenuEntries(input, username, controller, plan);
                    return;
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canCreatePrivatePlaylist()) {
            library.add(new MenuOption("Create new playlist", j -> {
                String playlistName = input.readString("Playlist name: ");
                try {
                    controller.createPlaylist(playlistName, username);
                    System.out.println(GREEN + "Playlist created successfully!" + RESET);
                } catch (PlaylistAlreadyExistsException e) {
                    System.err.println(
                            RED + "Playlist already exists: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));

            library.add(new MenuOption("Remove music from playlist", j -> {
                String playlistName = input.readString("Playlist name: ");
                String albumName = input.readString("Album name: ");
                String musicianName = input.readString("Musician name: ");
                String musicName = input.readString("Music name: ");
                try {
                    controller.removeMusicFromPrivatePlayList(playlistName, username, musicName,
                            musicianName, albumName);
                    System.out.println(
                            GREEN + "Music removed from playlist successfully!" + RESET);
                } catch (MusicDoesNotExistException e) {
                    System.err.println(RED + "Music does not exist: " + e.getMessage() + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                } catch (PlaylistAlreadyIsPublicException e) {
                    System.err.println(RED + "Playlist is public, you cannot remove musics: "
                            + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));

            library.add(new MenuOption("Add music to playlist", j -> {
                String playlistName = input.readString("Playlist name: ");
                String albumName = input.readString("Album name: ");
                String musicianName = input.readString("Musician name: ");
                String musicName = input.readString("Music name: ");
                try {
                    controller.addMusicToPrivatePlayList(playlistName, albumName, musicianName,
                            musicName, username);
                    System.out.println(GREEN + "Music added to playlist successfully!" + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                } catch (MusicDoesNotExistException e) {
                    System.err.println(RED + "Music does not exist: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                } catch (PlaylistAlreadyIsPublicException e) {
                    System.err.println(RED + "Playlist is public, you cannot add musics: "
                            + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canAddPublicPlaylistToLibrary()) {
            library.add(new MenuOption("Add Public Playlist to library", j -> {
                String playlistName = input.readString("Playlist name: ");
                try {
                    controller.addPlayListToLibrary(playlistName, username);
                    System.out.println(GREEN + "Public playlist added to library!" + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canAddAlbumToLibrary()) {
            library.add(new MenuOption("Add Album to library", j -> {
                String albumName = input.readString("Album name: ");
                String musicianName = input.readString("Musician name: ");
                try {
                    controller.addAlbumToLibrary(albumName, musicianName, username);
                    System.out.println(GREEN + "Album added to library!" + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                } catch (AlbumDoesNotExistException e) {
                    System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canRemovePlaylistFromLibrary()) {
            library.add(new MenuOption("Remove playlist from library", j -> {
                String playlistName = input.readString("Playlist name: ");
                try {
                    controller.removePlayListFromLibrary(playlistName, username);
                    System.out.println(GREEN + "Playlist removed from library!" + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canRemoveAlbumFromLibrary()) {
            library.add(new MenuOption("Remove album from library", j -> {
                String albumName = input.readString("Album name: ");
                String musicianName = input.readString("Musician name: ");
                try {
                    controller.removeAlbumFromLibrary(albumName, musicianName, username);
                    System.out.println(GREEN + "Album removed from library!" + RESET);
                } catch (AlbumDoesNotExistException e) {
                    System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        if (plan.canMakePlaylistPublic()) {
            library.add(new MenuOption("Make playlist public", j -> {
                String playlistName = input.readString("Playlist name: ");
                try {
                    controller.turnPlaylistPublic(username, playlistName);
                    System.out.println(GREEN + "Playlist is now public!" + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(
                            RED + "Playlist does not exist: " + e.getMessage() + RESET);
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                } catch (PlaylistAlreadyIsPublicException e) {
                    System.err.println(
                            RED + "Playlist already is public: " + e.getMessage() + RESET);
                }
                buildMenuEntries(input, username, controller, plan);
            }));
        }

        library.add(new MenuOption("List Library", j -> {
            ArrayList<String> Library = controller.listUserLibrary(username);
            for (String s : Library) {
                System.out.println(s);
            }
            buildMenuEntries(input, username, controller, plan);
        }));

        library.add(new MenuOption("Go Back", j -> {
            buildMenuEntries(input, username, controller, plan);
        }));

        return library.toArray(new MenuOption[0]);
    }

    /**
     * Clears the console screen by printing the ANSI escape codes.
     * This method works in terminals that support ANSI codes.
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Displays a welcome banner with colored ASCII art and a delay between
     * characters.
     * 
     * @throws InterruptedException if the thread is interrupted during the sleep.
     */
    public void welcome() throws InterruptedException {
        String[] lines = {
                "============================================",
                " ▗▄▄▖▗▄▄▖  ▗▄▖▗▄▄▄▖▗▄▄▄▖▗▄▄▄▖▗▖ ▗▖▗▖  ▗▖",
                "▐▌   ▐▌ ▐▌▐▌ ▐▌ █    █  ▐▌   ▐▌ ▐▌▐▛▚▞▜▌",
                " ▝▀▚▖▐▛▀▘ ▐▌ ▐▌ █    █  ▐▛▀▀▘▐▌ ▐▌▐▌  ▐▌",
                "▗▄▄▞▘▐▌   ▝▚▄▞▘ █  ▗▄█▄▖▐▌   ▝▚▄▞▘▐▌  ▐▌",
                "============================================"
        };

        for (String line : lines) {
            boolean isBar = line.contains("=");
            String color = isBar ? BLUE : GREEN;

            for (char c : line.toCharArray()) {
                System.out.print(color + c + RESET);
                Thread.sleep(10);
            }
            System.out.println();
        }
    }

    /**
     * Runs the welcome banner display and catches any InterruptedException.
     * Prints the exception message to the error output if interrupted.
     */
    public void runWelcome() {
        try {
            welcome();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns a Consumer that prompts the user to input details for creating a new
     * user.
     * It collects username, full name, email, and address.
     * Attempts to create the user via the controller and handles
     * UserAlreadyExistsException.
     * Calls addEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs user
     *         creation.
     */
    private Consumer<Integer> createUser() {
        return i -> {
            UserInput input = new UserInput();
            String username = input.readString("Username: ");
            String fullname = input.readString("Full name: ");
            String email = input.readString("Email: ");
            String address = input.readString("Address: ");
            try {
                this.controller.createUser(username, fullname, email, address);
                System.out.println(GREEN + "User added successfully!\n" + RESET);
            } catch (UserAlreadyExistsException e) {
                System.err.println(RED + "User already exists: " + e.getMessage() + RESET);
            }
            addEntity();
        };
    }

    /**
     * Returns a Consumer that prompts the user to input a username to remove.
     * Attempts to remove the user via the controller and handles
     * UserDoesNotExistException.
     * Calls removeEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs user
     *         removal.
     */
    private Consumer<Integer> removeUser() {
        return i -> {
            UserInput input = new UserInput();
            String username = input.readString("Username: ");
            try {
                this.controller.removeUser(username);
                System.out.println(GREEN + "User removed successfully!\n" + RESET);
            } catch (UserDoesNotExistException e) {
                System.err.println(RED + "User doesn't exists: " + e.getMessage() + RESET);
            }
            removeEntity();
        };
    }

    /**
     * Returns a Consumer that prompts the user to create a new playlist.
     * It reads the playlist name and tries to create it, handling
     * PlaylistAlreadyExistsException.
     * Then allows the user to add multiple musics to the playlist until they press
     * Enter.
     * Calls addEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs
     *         playlist creation.
     */
    private Consumer<Integer> createPlaylist() {
        return i -> {
            UserInput input = new UserInput();
            String playlist = input.readString("Playlist: ");
            try {
                controller.createPublicPlayList(playlist);
            } catch (PlaylistAlreadyExistsException e) {
                System.err.println(RED + "Playlist already exists: " + e.getMessage() + RESET);
            }
            System.out.println(GREEN + "Playlist added successfully!\n" + RESET);
            System.out.println("Let's add some musics to the new playlist");
            while (true) {
                System.out.println("If you want to stop, press Enter");
                String musicName = input.readString("Music name: ");
                if (musicName.isEmpty()) {
                    System.out.println("Stopping...");
                    break;
                }
                String albumName = input.readString("Album name: ");
                String musicianName = input.readString("Musician name: ");
                try {
                    this.controller.addMusicToPlayList(playlist, albumName, musicianName,
                            musicName);
                    System.out.println(GREEN + "Music added successfully!" + RESET);
                } catch (MusicDoesNotExistException e) {
                    System.err.println(RED + "Error adding music: " + musicName +
                            " doesn't exists: " + RESET);
                } catch (PlaylistDoesNotExistException e) {
                    System.err.println(RED + "Error adding music: " + playlist +
                            " doesn't exists" + RESET);
                }
            }
            addEntity();
        };
    }

    /**
     * Returns a Consumer that prompts the user to input a playlist name to remove.
     * Attempts to remove the playlist via the controller and handles
     * PlaylistDoesNotExistException.
     * Calls removeEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs
     *         playlist removal.
     */
    private Consumer<Integer> removePlaylist() {
        return i -> {
            UserInput input = new UserInput();
            String playlist = input.readString("Playlist: ");
            try {
                this.controller.removePlayListPublic(playlist);
                System.out.println(GREEN + "Playlist removed successfully!\n" + RESET);
            } catch (PlaylistDoesNotExistException e) {
                System.err.println(RED + "Playlist doesn't exists: " + e.getMessage() + RESET);
            }
            removeEntity();
        };
    }

    /**
     * Returns a Consumer that prompts the user to input details for creating a new
     * album.
     * Then allows the user to add multiple musics with detailed metadata including
     * lyrics,
     * notes, genre, duration, age restriction, multimedia info, and type.
     * Handles AlbumAlreadyExistsException, MusicAlreadyExistsException, and
     * AlbumDoesNotExistException.
     * Calls addEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs
     *         album creation.
     */
    private Consumer<Integer> createAlbum() {
        return i -> {
            UserInput input = new UserInput();
            String albumName = input.readString("Album name: ");
            String musician = input.readString("Musician: ");
            String publisher = input.readString("Publisher: ");
            try {
                this.controller.createAlbum(albumName, musician, publisher);
                System.out.println(GREEN + "Album added successfully!\n" + RESET);
            } catch (AlbumAlreadyExistsException e) {
                System.err.println(RED + "Album already exists:" + e.getMessage() + RESET);
            }

            System.out.println("Let's add some musics to the new album");
            while (true) {
                System.out.println("If you want to stop, press Enter");
                String musicName = input.readString("Music name: ");
                if (musicName.isEmpty()) {
                    System.out.println("Stopping...");
                    break;
                }

                System.out.println("\nLyrics (Enter in an empty line to stop): ");
                StringBuilder lyricsBuilder = new StringBuilder();
                String line;
                while (!(line = input.readString("")).isEmpty()) {
                    lyricsBuilder.append(line).append("\n");
                }
                String lyrics = lyricsBuilder.toString().trim();

                System.out.println("Notes (one per line, Enter to stop): ");
                List<String> musicNotes = new ArrayList<>();
                while (true) {
                    System.out.print("> ");
                    String note = input.readString("").trim();
                    if (note.isEmpty()) {
                        if (musicNotes.isEmpty()) {
                            System.err.println(RED + "É necessário pelo menos uma nota musical." + RESET);
                            continue;
                        }
                        break;
                    }
                    musicNotes.add(note);
                }

                String genre = input.readString("Genre: ");
                int durationInSec = input.readInt(
                        "Duration (in seconds): ", RED + "Must be a positive integer!" + RESET, d -> d > 0);
                int ageRestriction = 0;
                String videoPath = "";
                String resolution = "";
                String isExplicitStr = input.readString("Is explicit? (Y/N): ");
                while (!isExplicitStr.equalsIgnoreCase("Y") && !isExplicitStr.equalsIgnoreCase("N")) {
                    isExplicitStr = input.readString(RED + "Invalid input. Please enter Y or N." + RESET);
                }
                int type = 3;
                if (isExplicitStr.equalsIgnoreCase("Y")) {
                    ageRestriction = input.readInt("Age Restriction: ",
                            RED + "Age restriction must be bigger than 0!" + RESET,
                            a -> a > 0);
                    type = 1;
                } else {
                    String isMultimediaStr = input.readString("Is Multimedia? (Y/N): ");
                    while (!isMultimediaStr.equalsIgnoreCase("Y") && !isMultimediaStr.equalsIgnoreCase("N")) {
                        isMultimediaStr = input.readString(RED + "Invalid input. Please enter Y or N." + RESET);
                    }
                    if (isMultimediaStr.equalsIgnoreCase("Y")) {
                        videoPath = input.readString("Video path: ");
                        resolution = input.readString("Resolution: ");
                        type = 2;
                    }
                }
                try {
                    this.controller.createMusic(albumName, musician, musicName, lyrics,
                            musicNotes, genre, durationInSec, type, ageRestriction, videoPath, resolution);
                    System.out.println(GREEN + "Music added successfully!\n" + RESET);
                } catch (MusicAlreadyExistsException e) {
                    System.err.println(RED + "Music already exists: " + e.getMessage() + RESET);
                } catch (AlbumDoesNotExistException e) {
                    System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
                }
            }
            addEntity();
        };
    }

    /**
     * Returns a Consumer that prompts the user to input an album and musician name
     * to remove an album.
     * Attempts to remove the album via the controller and handles
     * AlbumDoesNotExistException and MusicDoesNotExistException.
     * Calls removeEntity() after completion.
     * 
     * @return a Consumer accepting an Integer (unused parameter) that performs
     *         album removal.
     */
    private Consumer<Integer> removeAlbum() {
        return i -> {
            UserInput input = new UserInput();
            String albumName = input.readString("Album name: ");
            String musician = input.readString("Musician: ");
            try {
                this.controller.removeAlbum(musician, albumName);
                System.out.println(GREEN + "Album removed successfully!\n" + RESET);
            } catch (AlbumDoesNotExistException e) {
                System.err.println(RED + "Album doesn't exist: " + e.getMessage() + RESET);
            } catch (MusicDoesNotExistException e) {
                System.err.println(RED + "Music doesn't exist: " + e.getMessage() + RESET);
            }
            removeEntity();
        };
    }

    /**
     * Returns a Consumer that handles the creation of a new music entry through
     * user interaction.
     *
     * @return a Consumer<Integer> that executes the music creation flow based on
     *         user input.
     */
    private Consumer<Integer> createMusic() {
        return i -> {
            UserInput input = new UserInput();
            String albumName = input.readString("Album to add: ");
            String musician = input.readString("Album Musician: ");
            if (!controller.existAlbum(albumName, musician)) {
                System.err.println(RED + "Album does not exist. Please create the album first." + RESET);
                return;
            }

            String musicName = input.readString("Music name: ");
            System.out.println("\nLyrics (Enter in an empty line to stop): ");
            StringBuilder lyricsBuilder = new StringBuilder();
            String line;
            while (!(line = input.readString("")).isEmpty()) {
                lyricsBuilder.append(line).append("\n");
            }
            String lyrics = lyricsBuilder.toString().trim();

            System.out.println("Notes (one per line, Enter to stop): ");
            List<String> musicNotes = new ArrayList<>();
            while (true) {
                System.out.print("> ");
                String note = input.readString("").trim();
                if (note.isEmpty()) {
                    if (musicNotes.isEmpty()) {
                        System.err.println(RED + "You need to enter at least one musical note." + RESET);
                        continue;
                    }
                    break;
                }
                musicNotes.add(note);
            }

            String genre = input.readString("Genre: ");
            int durationInSec = input.readInt("Duration (in seconds): ", "Must be a positive integer!", d -> d > 0);
            int ageRestriction = 0;
            String videoPath = "";
            String resolution = "";
            String isExplicitStr = input.readString("Is explicit? (Y/N): ");
            while (!isExplicitStr.equalsIgnoreCase("Y") && !isExplicitStr.equalsIgnoreCase("N")) {
                isExplicitStr = input.readString(RED + "Invalid input. Please enter Y or N." + RESET);
            }

            int type = 3;
            if (isExplicitStr.equalsIgnoreCase("Y")) {
                ageRestriction = input.readInt("Age Restriction: ",
                        RED + "Age restriction must be bigger than 0!" + RESET,
                        a -> a > 0);
                type = 1;
            } else {
                String isMultimediaStr = input.readString("Is Multimedia? (Y/N): ");
                while (!isMultimediaStr.equalsIgnoreCase("Y") && !isMultimediaStr.equalsIgnoreCase("N")) {
                    isMultimediaStr = input.readString(RED + "Invalid input. Please enter Y or N." + RESET);
                }
                if (isMultimediaStr.equalsIgnoreCase("Y")) {
                    videoPath = input.readString("Video path: ");
                    resolution = input.readString("Resolution: ");
                    type = 2;
                }
            }

            try {
                this.controller.createMusic(
                        albumName, musician, musicName, lyrics,
                        musicNotes, genre, durationInSec, type,
                        ageRestriction, videoPath, resolution);
                System.out.println(GREEN + "Music added successfully!\n" + RESET);
            } catch (MusicAlreadyExistsException e) {
                System.err.println(RED + "Music already exists: " + e.getMessage() + RESET);
            } catch (AlbumDoesNotExistException e) {
                System.err.println(RED + "Album does not exist: " + e.getMessage() + RESET);
            }
            addEntity();
        };
    }

    /**
     * Returns a Consumer that removes a music entry based on user input.
     * 
     * Prompts for album name, musician, and music name, then attempts to remove the
     * music.
     * Displays success or error messages accordingly.
     * After removal, shows the entity removal menu.
     *
     * @return a Consumer<Integer> that performs the music removal process.
     */
    private Consumer<Integer> removeMusic() {
        return i -> {
            UserInput input = new UserInput();
            String albumName = input.readString("Album name: ");
            String musician = input.readString("Musician: ");
            String musicName = input.readString("Music name: ");
            try {
                this.controller.removeMusic(musician, albumName, musicName);
                System.out.println(GREEN + "Music removed successfully!\n" + RESET);
            } catch (MusicDoesNotExistException e) {
                System.err.println(RED + "Music doesn't exist: " + e.getMessage() + RESET);
            }
            removeEntity();
        };
    }

    /**
     * Handles user login by prompting for a username.
     * 
     * Validates if the user exists; if not, shows an error and restarts.
     * Retrieves the user's subscription plan and builds the menu accordingly.
     */
    private void handleLogin() {
        UserInput input = new UserInput();
        String username = input.readString("Username: ");

        try {
            controller.getUser(username);
        } catch (UserDoesNotExistException e) {
            System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
            run();
        }
        SubscriptionPlan plan = controller.getUserPlan(username);
        buildMenuEntries(input, username, controller, plan);
    }

    /**
     * Displays a menu for removing entities: users, albums, playlists, or music.
     * Allows navigation back to the main menu.
     */
    private void removeEntity() {
        // faz da mesma maneira que as outras
        MenuOption[] entries = {
                new MenuOption("Remove user", i -> this.removeUser().accept(i)),
                new MenuOption("Remove album", i -> this.removeAlbum().accept(i)),
                new MenuOption("Remove playlist", i -> this.removePlaylist().accept(i)),
                new MenuOption("Remove music", i -> this.removeMusic().accept(i)),

                new MenuOption("Go back", i -> {
                    run();
                }),
        };
        new Menu(entries).run();
    }

    /**
     * Displays a menu listing different entity types (Users, Musics, Albums,
     * Playlists).
     * Shows the corresponding table and redisplays the menu until the user chooses
     * to go back.
     */
    private void menuListEntities() {
        MenuOption[] differentEntities = {
                new MenuOption("Users", j -> {
                    String s = controller.printUsersTable();
                    System.out.println(s);
                    menuListEntities();
                }), new MenuOption("Musics", j -> {
                    String s = controller.printMusicsTable();
                    System.out.println(s);
                    menuListEntities();
                }), new MenuOption("Albums", j -> {
                    String s = controller.printAlbumsTable();
                    System.out.println(s);
                    menuListEntities();
                }), new MenuOption("Playlists", j -> {
                    String s = controller.printPlaylistNames();
                    System.out.println(s);
                    menuListEntities();
                }), new MenuOption("Go Back", j -> {
                    run();
                }) };
        new Menu(differentEntities).run();
    }

    /**
     * Displays a menu for various predefined queries related to music statistics
     * and user data.
     * Executes the selected query, prints the result, and redisplays the menu until
     * the user chooses to go back.
     */
    private void handleQueries() {
        UserInput input = new UserInput();
        MenuOption[] entries = {
                new MenuOption("Most listened music",
                        i -> {
                            try {
                                String ans = controller.runQuery(new MostListenedMusic());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("Most listened musician",
                        i -> {
                            try {
                                String ans = controller.runQuery(new MostListenedMusician());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption(
                        "User with the most listened musics with dates",
                        i -> {
                            LocalDate start = input.readDate("Invalid date", d -> !d.isAfter(LocalDate.now()));
                            LocalDate end = input.readDate("Invalid date",
                                    d -> !d.isAfter(LocalDate.now()) && !d.isBefore(start));
                            try {
                                String ans = controller.runQuery(new UserWithMostListenedMusicsWithDates(start, end));
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("User with the most listened musics ever",
                        i -> {
                            try {
                                String ans = controller.runQuery(new UserWithMostListenedMusicsEver());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("User with the most points",
                        i -> {
                            try {
                                String ans = controller.runQuery(new UserWithMostPoints());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("Most listened genre",
                        i -> {
                            try {
                                String ans = controller.runQuery(new MostListenedGenre());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("Number of public playlists",
                        i -> {
                            try {
                                String ans = controller.runQuery(new NumberOfPublicPlaylists());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("User with the most playlists",
                        i -> {
                            try {
                                String ans = controller.runQuery(new UserWithMostPlaylists());
                                System.out.println(ans);
                                handleQueries();
                            } catch (UserDoesNotExistException e) {
                                System.out.println(e);
                            } catch (PlaylistDoesNotExistException e) {
                                System.out.println(e);
                            } catch (AlbumDoesNotExistException e) {
                                System.out.println(e);
                            } catch (MusicDoesNotExistException e) {
                                System.out.println(e);
                            }
                            handleQueries();
                        }),
                new MenuOption("Go back", i -> {
                    run();
                }),
        };
        new Menu(entries).run();
    }

    /**
     * Plays a list of musics for a user, allowing interaction based on the user's
     * subscription plan.
     * Users with skip privileges can skip, go back, play, or quit. Others can only
     * play sequentially.
     *
     * @param musics   List of Music objects to play.
     * @param plan     The user's subscription plan, determining playback options.
     * @param username The username of the user listening to the music.
     */
    private void playMusics(ArrayList<Music> musics, SubscriptionPlan plan, String username) {
        for (int j = 0; j < musics.size(); j++) {
            System.out.println("Music Name: " + musics.get(j).getName());
            if (plan.canSkipMusic()) {
                System.out.println("0 -> Skip Music");
                System.out.println("1 -> Previous Music");
                System.out.println("2 -> Play Music");
                System.out.println("3 -> Shuffle Musics");
                System.out.println("4 -> Quit");
                int resposta = new UserInput().readInt("Option: ", "Invalid option",
                        i -> i >= 0 && i <= 4);
                switch (resposta) {
                    case 0:
                        break;
                    case 1:
                        if (j == 0)
                            j = -1;
                        else
                            j -= 2;
                        break;
                    case 2:
                        try {
                            System.out.println(musics.get(j).getLyrics());
                            controller.atualizaWhenUserListenMusic(username, musics.get(j));
                        } catch (UserDoesNotExistException e) {
                            System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                        }
                        break;
                    case 3:
                        Collections.shuffle(musics);
                        j = -1;
                        break;
                    case 4:
                        return;
                    default:
                        break;
                }
            } else if (!plan.canSkipMusic()) {
                try {
                    System.out.println(musics.get(j).getLyrics());
                    controller.atualizaWhenUserListenMusic(username, musics.get(j));
                } catch (UserDoesNotExistException e) {
                    System.err.println(RED + "User does not exist: " + e.getMessage() + RESET);
                }
            }
        }
        System.out.println("\nLeaving...");
    }

    /**
     * Plays a single music for the given user and updates the user's listening
     * history.
     *
     * @param music    The Music object to play.
     * @param username The username of the user listening to the music.
     */
    private void playMusic(Music music, String username) {
        try {
            controller.atualizaWhenUserListenMusic(username, music);
            System.out.println("Lyrics: " + music.getLyrics());
        } catch (UserDoesNotExistException e) {
            System.out.println(RED + "User does not exist: " + e.getMessage() + RESET);
        }
    }

    /**
     * Displays a menu to add a new user, album, playlist, or music, or go back.
     */
    private void addEntity() {
        MenuOption[] entries = {
                new MenuOption("Add new user", i -> this.createUser().accept(i)),
                new MenuOption("Add new album", i -> this.createAlbum().accept(i)),
                new MenuOption("Add new playlist", i -> this.createPlaylist().accept(i)),
                new MenuOption("Add new music", i -> this.createMusic().accept(i)),

                new MenuOption("Go back", i -> {
                    run();
                }),
        };
        new Menu(entries).run();
    }

    /**
     * Displays a menu for file operations: load or save state from/to a file.
     */
    private void fileOperations() {
        Consumer<Integer> doOperation = (i) -> {
            UserInput input = new UserInput();
            String path = input.readString("File Name > ");
            try {
                if (i == 0) {
                    this.controller.loadFromFile(path);
                } else {
                    this.controller.saveToFile(path);
                }
                System.out.println(GREEN + "Operation successful!" + RESET);
            } catch (SpotifUMControllerException e) {
                System.err.println(e.getMessage());
            }
        };

        MenuOption[] entries = {
                new MenuOption("Load state from file", doOperation),
                new MenuOption("Save state to file", doOperation),
                new MenuOption("Go back", i -> {
                    run();
                }),
        };
        new Menu(entries).run();
    }

    /**
     * Starts the main menu loop of the application.
     */
    public void run() {
        boolean[] exitRequest = { false }; // Array wrapper to allow for lambda modification
        MenuOption[] entries = {
                new MenuOption("Login", i -> this.handleLogin()),
                new MenuOption("Add entity", i -> this.addEntity()),
                new MenuOption("Remove entity", i -> this.removeEntity()),
                new MenuOption("Queries", i -> this.handleQueries()),
                new MenuOption("List entities", i -> this.menuListEntities()),
                new MenuOption("File operations", i -> this.fileOperations()),
                new MenuOption("Exit", i -> System.exit(0)),
        };

        do {
            new Menu(entries).run();
        } while (!exitRequest[0]);
    }

    /**
     * Creates a deep copy of this SpotifUM view.
     *
     * @return A deep copy of this SpotifUM view.
     */
    @Override
    public SpotifUMView clone() {
        return new SpotifUMView(this);
    }

    /**
     * Creates a string representation of this SpotifUM view.
     *
     * @return A string representation of this SpotifUM view.
     */
    @Override
    public String toString() {
        return String.format("NewView(controller = %s)",
                this.controller.toString());
    }
}
