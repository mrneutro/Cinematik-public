package engine.custom_managers;

import db.ContentProvider;
import db.DbObject;
import engine.Film;
import engine.Hall;
import engine.Show;
import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import gui.Icons;
import junit.framework.Assert;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class ShowManagerTest {
    public Show show11;
    public Show show12;
    public Show show13;
    public Show show15;
    public Show show17;
    public Show show18;
    public ImageIcon imageIcon;
    public final String DESCRIPTION = "Here we a have a long long description";
    public final String TITLE = "Small Title";
    public final String LENGTH = "75";
    public Film f;
    public ShowManager shows;
    public Client client;

    @Before
    public void setUp() throws Exception {
        DbObject.debug = true;
        InLogger.setShowConsole(false);
        removeTestData();
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
        shows = new ShowManager();
        Hall h = new Hall(0, 10, 10);
        show11 = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, h);
        show12 = new Show(f, LocalDateTime.of(2015, 01, 15, 12, 00, 00), 11, false, h);
        show13 = new Show(f, LocalDateTime.of(2015, 01, 15, 13, 00, 00), 11, false, h);
        show15 = new Show(f, LocalDateTime.of(2015, 01, 15, 15, 00, 00), 11, false, h);
        show17 = new Show(f, LocalDateTime.of(2015, 01, 15, 17, 00, 00), 11, false, h);
        show18 = new Show(f, LocalDateTime.of(2016, 01, 15, 17, 00, 00), 11, false, h);
        client = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
    }

    private void removeTestData() throws IOException {
        ContentProvider cp = new ContentProvider();
        cp.getShows().clear();
        File dbDir = new File("dbDataTest");
        if (dbDir.exists()) {
            File container = new File(dbDir.getAbsolutePath() + "/" + "content.bin");
            if (container.exists()) container.delete();
        }
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(0, shows.size());
        shows.add(show11);
        shows.add(show13);
        assertEquals(2, shows.size());
        shows.remove(show11);
        assertEquals(1, shows.size());
        assertTrue(shows.get(0).equals(show13));
        shows.add(show11);
        assertEquals(2, shows.size());
    }

    @Test(expected = IOException.class)
    public void testAddException() throws Exception {
        shows.add(show11);
        shows.add(show12);
    }

    @Test
    public void testGetInInterval() throws Exception {
        shows = new ShowManager(new ContentProvider());
        shows.add(show11);
        shows.add(show13);
        shows.add(show15);
        shows.add(show17);
        ArrayList<Show> myShow = shows.getInInterval(LocalDate.of(2015, 01, 14), LocalDate.of(2015, 01, 16), filter -> true);
        assertEquals(4, myShow.size());
        ArrayList<Show> myShowNo = shows.getInInterval(LocalDate.of(2015, 01, 17), LocalDate.of(2015, 01, 19), filter -> true);
        Assert.assertEquals(0, myShowNo.size());
    }
}