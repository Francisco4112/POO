package com.grupo06.user;

/**
 * Represents a subscription plan with various capabilities.
 */
public interface SubscriptionPlan {
    // General options

    /**
     * Calculates user points based on current points.
     *
     * @param currentPoints current user points
     * @return updated points
     */
    public double calculatePoints(double currentPoints);

    /**
     * Gets the type of this subscription plan.
     *
     * @return the plan type
     */
    SubscriptionPlanType getType();

    /**
     * Checks if music can be skipped.
     *
     * @return true if skipping is allowed
     */
    public boolean canSkipMusic();

    /**
     * Checks if random playlists can be played.
     *
     * @return true if allowed
     */
    public boolean canPlayRandomPlaylist();

    /**
     * Checks if user can have a library.
     *
     * @return true if allowed
     */
    public boolean canHaveLibrary();

    /**
     * Checks if music can be played.
     *
     * @return true if allowed
     */
    public boolean canPlayMusic();

    /**
     * Checks if albums can be played.
     *
     * @return true if allowed
     */
    public boolean canPlayAlbum();

    /**
     * Checks if public playlists can be played.
     *
     * @return true if allowed
     */
    public boolean canPlayPublicPlaylist();

    /**
     * Checks if generated playlists are available.
     *
     * @return true if allowed
     */
    public boolean canHaveGeneratedPlaylists();

    // Library Options

    /**
     * Checks if saved playlists can be played.
     *
     * @return true if allowed
     */
    public boolean canPlaySavedPlaylist();

    /**
     * Checks if private playlists can be created.
     *
     * @return true if allowed
     */
    public boolean canCreatePrivatePlaylist();

    /**
     * Checks if private playlists can be modified.
     *
     * @return true if allowed
     */
    public boolean canModifyPrivatePlaylist();

    /**
     * Checks if public playlists can be added to the library.
     *
     * @return true if allowed
     */
    public boolean canAddPublicPlaylistToLibrary();

    /**
     * Checks if any playlist can be added to the library.
     *
     * @return true if allowed
     */
    public boolean canAddPlaylistToLibrary();

    /**
     * Checks if playlists can be removed from the library.
     *
     * @return true if allowed
     */
    public boolean canRemovePlaylistFromLibrary();

    /**
     * Checks if albums can be added to the library.
     *
     * @return true if allowed
     */
    public boolean canAddAlbumToLibrary();

    /**
     * Checks if albums can be removed from the library.
     *
     * @return true if allowed
     */
    public boolean canRemoveAlbumFromLibrary();

    /**
     * Checks if playlists can be made public.
     *
     * @return true if allowed
     */
    public boolean canMakePlaylistPublic();

    /**
     * Checks if this plan is equal to another object.
     *
     * @param obj the object to compare
     * @return true if equal
     */
    public boolean equals(Object obj);

    /**
     * Returns a string representation of the plan.
     *
     * @return plan as a string
     */
    public String toString();
}
