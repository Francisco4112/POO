package com.grupo06.userTest;

import com.grupo06.user.Free;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FreeTest {

    @Test
    public void testCalculatePoints() {
        Free free = new Free();
        assertEquals(5, free.calculatePoints(0));
        assertEquals(15, free.calculatePoints(10));
    }

    @Test
    public void testCanSkipMusic() {
        Free free = new Free();
        assertFalse(free.canSkipMusic());
    }

    @Test
    public void testCanPlayRandomPlaylist() {
        Free free = new Free();
        assertTrue(free.canPlayRandomPlaylist());
    }

    @Test
    public void testCanHaveLibrary() {
        Free free = new Free();
        assertFalse(free.canHaveLibrary());
    }

    @Test
    public void testCanPlayMusic() {
        Free free = new Free();
        assertFalse(free.canPlayMusic());
    }

    @Test
    public void testCanPlayAlbum() {
        Free free = new Free();
        assertFalse(free.canPlayAlbum());
    }

    @Test
    public void testCanPlayPublicPlaylist() {
        Free free = new Free();
        assertFalse(free.canPlayPublicPlaylist());
    }

    @Test
    public void testCanHaveGeneratedPlaylists() {
        Free free = new Free();
        assertFalse(free.canHaveGeneratedPlaylists());
    }

    @Test
    public void testCanPlaySavedPlaylist() {
        Free free = new Free();
        assertFalse(free.canPlaySavedPlaylist());
    }

    @Test
    public void testCanCreatePrivatePlaylist() {
        Free free = new Free();
        assertFalse(free.canCreatePrivatePlaylist());
    }

    @Test
    public void testCanModifyPrivatePlaylist() {
        Free free = new Free();
        assertFalse(free.canModifyPrivatePlaylist());
    }

    @Test
    public void testCanAddPublicPlaylistToLibrary() {
        Free free = new Free();
        assertFalse(free.canAddPublicPlaylistToLibrary());
    }

    @Test
    public void testCanAddPlaylistToLibrary() {
        Free free = new Free();
        assertFalse(free.canAddPlaylistToLibrary());
    }

    @Test
    public void testCanRemovePlaylistFromLibrary() {
        Free free = new Free();
        assertFalse(free.canRemovePlaylistFromLibrary());
    }

    @Test
    public void testCanAddAlbumToLibrary() {
        Free free = new Free();
        assertFalse(free.canAddAlbumToLibrary());
    }

    @Test
    public void testCanRemoveAlbumFromLibrary() {
        Free free = new Free();
        assertFalse(free.canRemoveAlbumFromLibrary());
    }

    @Test
    public void testCanMakePlaylistPublic() {
        Free free = new Free();
        assertFalse(free.canMakePlaylistPublic());
    }

    @Test
    public void testEquals() {
        Free free1 = new Free();
        Free free2 = new Free();
        assertEquals(free1, free2);
        assertNotEquals(free1, null);
        assertNotEquals(free1, new Object());
    }

    @Test
    public void testToString() {
        Free free = new Free();
        assertEquals("Free", free.toString());
    }
}