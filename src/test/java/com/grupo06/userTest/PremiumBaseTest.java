package com.grupo06.userTest;

import com.grupo06.user.PremiumBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PremiumBaseTest {

    @Test
    public void testCalculatePoints() {
        PremiumBase premium = new PremiumBase();
        assertEquals(10, premium.calculatePoints(0));
        assertEquals(25, premium.calculatePoints(15));
    }

    @Test
    public void testCanSkipMusic() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canSkipMusic());
    }

    @Test
    public void testCanPlayRandomPlaylist() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canPlayRandomPlaylist());
    }

    @Test
    public void testCanHaveLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canHaveLibrary());
    }

    @Test
    public void testCanPlayMusic() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canPlayMusic());
    }

    @Test
    public void testCanPlayAlbum() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canPlayAlbum());
    }

    @Test
    public void testCanPlayPublicPlaylist() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canPlayPublicPlaylist());
    }

    @Test
    public void testCanHaveGeneratedPlaylists() {
        PremiumBase premium = new PremiumBase();
        assertFalse(premium.canHaveGeneratedPlaylists());
    }

    @Test
    public void testCanPlaySavedPlaylist() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canPlaySavedPlaylist());
    }

    @Test
    public void testCanCreatePrivatePlaylist() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canCreatePrivatePlaylist());
    }

    @Test
    public void testCanModifyPrivatePlaylist() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canModifyPrivatePlaylist());
    }

    @Test
    public void testCanAddPublicPlaylistToLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canAddPublicPlaylistToLibrary());
    }

    @Test
    public void testCanAddPlaylistToLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canAddPlaylistToLibrary());
    }

    @Test
    public void testCanRemovePlaylistFromLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canRemovePlaylistFromLibrary());
    }

    @Test
    public void testCanAddAlbumToLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canAddAlbumToLibrary());
    }

    @Test
    public void testCanRemoveAlbumFromLibrary() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canRemoveAlbumFromLibrary());
    }

    @Test
    public void testCanMakePlaylistPublic() {
        PremiumBase premium = new PremiumBase();
        assertTrue(premium.canMakePlaylistPublic());
    }

    @Test
    public void testEquals() {
        PremiumBase p1 = new PremiumBase();
        PremiumBase p2 = new PremiumBase();
        assertEquals(p1, p2);
        assertNotEquals(p1, null);
        assertNotEquals(p1, new Object());
    }

    @Test
    public void testToString() {
        PremiumBase premium = new PremiumBase();
        assertEquals("PremiumBase", premium.toString());
    }
}