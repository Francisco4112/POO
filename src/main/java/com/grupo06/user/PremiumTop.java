package com.grupo06.user;

import java.io.Serializable;

/**
 * Represents the top-tier premium subscription plan.
 */
public class PremiumTop implements SubscriptionPlan, Serializable {
    /**
     * {@inheritDoc}
     */
    @Override
    public SubscriptionPlanType getType() {
        return SubscriptionPlanType.PREMIUM_TOP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculatePoints(double currentPoints) {
        return currentPoints + (0.025 * currentPoints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canSkipMusic() {
        return true;
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayMusic() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayAlbum() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlayPublicPlaylist() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHaveGeneratedPlaylists() {
        return true;
    }

    // Library Options

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canPlaySavedPlaylist() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canCreatePrivatePlaylist() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canModifyPrivatePlaylist() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddPublicPlaylistToLibrary() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddPlaylistToLibrary() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRemovePlaylistFromLibrary() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAddAlbumToLibrary() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRemoveAlbumFromLibrary() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canMakePlaylistPublic() {
        return true;
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
        return true; // PremiumTop é igual a qualquer outro plano Premium
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PremiumTop"; // Nome do plano
    }
}
