package db;

import engine.Hall;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class ContentProviderTest {
    private ContentProvider cp;

    @Before
    public void setUp() throws Exception {
        DbObject.debug = true;
        InLogger.setShowConsole(false);
        removeTestData();
        cp = new ContentProvider();
    }

    private void removeTestData() {
        File dbDir = new File("dbDataTest");
        if (dbDir.exists()) {
            File container = new File(dbDir.getAbsolutePath() + "/" + "content.bin");
            if (container.exists()) container.delete();
        }
    }

    @Test
    public void testGetUsers() throws Exception {
        cp = new ContentProvider();
        assertEquals(0, cp.getUsers().size());
        assertEquals(0, cp.getFilms().size());
        assertEquals(0, cp.getHalls().size());
        assertEquals(0, cp.getShows().size());

        assertEquals(0, cp.getUsers().size());
        assertEquals(0, cp.getFilms().size());
        assertEquals(0, cp.getHalls().size());
        assertEquals(0, cp.getShows().size());
    }

    @Test
    public void testFlush() throws Exception {
        ArrayList<Hall> halls = cp.getHalls();
        halls.add(new Hall(0, 10, 10));
        cp.flush();
        assertEquals(1, cp.getHalls().size());
    }
}