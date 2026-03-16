package com.grupo06.userTest;

import com.grupo06.user.PremiumTop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PremiumTopTest {

    @Test
    public void testCalculatePoints() {
        PremiumTop premium = new PremiumTop();
        assertEquals(0, premium.calculatePoints(0), 0.0001);
        assertEquals(102.5, premium.calculatePoints(100), 0.0001);
        assertEquals(205, premium.calculatePoints(200), 0.0001);
    }

    @Test
    public void testCanSkipMusic() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canSkipMusic());
    }

    @Test
    public void testCanPlayRandomPlaylist() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canPlayRandomPlaylist());
    }

    @Test
    public void testCanHaveLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canHaveLibrary());
    }

    @Test
    public void testCanPlayMusic() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canPlayMusic());
    }

    @Test
    public void testCanPlayAlbum() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canPlayAlbum());
    }

    @Test
    public void testCanPlayPublicPlaylist() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canPlayPublicPlaylist());
    }

    @Test
    public void testCanHaveGeneratedPlaylists() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canHaveGeneratedPlaylists());
    }

    @Test
    public void testCanPlaySavedPlaylist() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canPlaySavedPlaylist());
    }

    @Test
    public void testCanCreatePrivatePlaylist() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canCreatePrivatePlaylist());
    }

    @Test
    public void testCanModifyPrivatePlaylist() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canModifyPrivatePlaylist());
    }

    @Test
    public void testCanAddPublicPlaylistToLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canAddPublicPlaylistToLibrary());
    }

    @Test
    public void testCanAddPlaylistToLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canAddPlaylistToLibrary());
    }

    @Test
    public void testCanRemovePlaylistFromLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canRemovePlaylistFromLibrary());
    }

    @Test
    public void testCanAddAlbumToLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canAddAlbumToLibrary());
    }

    @Test
    public void testCanRemoveAlbumFromLibrary() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canRemoveAlbumFromLibrary());
    }

    @Test
    public void testCanMakePlaylistPublic() {
        PremiumTop premium = new PremiumTop();
        assertTrue(premium.canMakePlaylistPublic());
    }

    @Test
    public void testEquals() {
        PremiumTop p1 = new PremiumTop();
        PremiumTop p2 = new PremiumTop();
        assertEquals(p1, p2);
        assertNotEquals(p1, null);
        assertNotEquals(p1, new Object());
    }

    @Test
    public void testToString() {
        PremiumTop premium = new PremiumTop();
        assertEquals("PremiumTop", premium.toString());
    }
}