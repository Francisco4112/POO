package com.grupo06.user;

import java.io.Serializable;

/**
 * Represents the free subscription plan with limited features.
 */
public class Free implements SubscriptionPlan, Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public SubscriptionPlanType getType() {
        return SubscriptionPlanType.FREE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculatePoints(double currentPoints) {
        return currentPoints + 5; // 5 pontos por música
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canSkipMusic() {
        return false; // Free não pode pular músicas
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayRandomPlaylist() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHaveLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayMusic() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayAlbum() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayPublicPlaylist() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHaveGeneratedPlaylists() {
        return false;
    }

    // Library Options

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlaySavedPlaylist() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canCreatePrivatePlaylist() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canModifyPrivatePlaylist() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddPublicPlaylistToLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddPlaylistToLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRemovePlaylistFromLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddAlbumToLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRemoveAlbumFromLibrary() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canMakePlaylistPublic() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Free"; // Nome do plano
    }
}
