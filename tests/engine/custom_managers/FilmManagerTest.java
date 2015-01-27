package engine.custom_managers;

import db.ContentProvider;
import db.DbObject;
import engine.Film;
import engine.Hall;
import engine.Show;
import gui.Icons;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class FilmManagerTest {
    private ImageIcon imageIcon;
    private final String DESCRIPTION = "Here we a have a long long description";
    private final String TITLE = "Small Title";
    private final String LENGTH = "200";
    private Film f;
    FilmManager filmManager;

    @Before
    public void setUp() throws Exception {
        DbObject.debug = true;
        InLogger.setShowConsole(false);
        removeTestData();
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
        ArrayList<Film> testData = new ArrayList<>();
        filmManager = new FilmManager();
        filmManager.setSource(testData);
    }

    private void removeTestData() {
        File dbDir = new File("dbDataTest");
        if (dbDir.exists()) {
            File container = new File(dbDir.getAbsolutePath() + "/" + "content.bin");
            if (container.exists()) container.delete();
        }
    }

    @Test(expected = IOException.class)
    public void testRemoveErr() throws Exception {
        ContentProvider cp = new ContentProvider();
        Show s = new Show(f, LocalDateTime.now(), 11.22, false, new Hall(1, 10, 10));
        cp.getShows().add(s);
        filmManager.add(f);
        filmManager.remove(f);
    }

    @Test
    public void testRemove() throws Exception {
        assertEquals(0, filmManager.size());
        filmManager.add(f);
        assertEquals(1, filmManager.size());
        filmManager.remove(f);
        assertEquals(0, filmManager.size());
    }
}