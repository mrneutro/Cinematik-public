package engine;

import db.DbObject;
import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import gui.Icons;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDateTime;

import static junit.framework.Assert.*;

public class ShowTest {
    public ImageIcon imageIcon;
    public final String DESCRIPTION = "Here we a have a long long description";
    public final String TITLE = "Small Title";
    public final String LENGTH = "75";
    Film f;
    Client client;

    @Before
    public void setUp() throws Exception {
        InLogger.setShowConsole(false);
        DbObject.debug = true;
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
        client = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
    }

    @Test
    public void testClone() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        Show clone = (Show) show.clone();
        assertTrue(show.equals(clone));
        assertTrue(show.hashCode() == clone.hashCode());
    }

    @Test
    public void testOutDated() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        assertTrue(show.isOutDated());
        Show not = new Show(f, LocalDateTime.of(2016, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        assertFalse(not.isOutDated());
        assertFalse(show.getDiscount());
    }

    @Test
    public void testRemoveAllOut() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        Place p = new Place(1, 1);
        Place p2 = new Place(1, 2);
        show.addReservation(new Reservation(show, client, false, 10.0, null, p), p);
        show.addReservation(new Reservation(show, client, true, 10.0, null, p2), p2);
        assertEquals(2, show.getReservations().size());
        show.removeOutDatedReservations();
        assertEquals(1, show.getReservations().size());
    }


    @Test
    public void testConfirm() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        Place p = new Place(1, 1);
        Reservation res = new Reservation(show, client, false, 10.0, null, p);
        show.addReservation(res, p);
        show.confirmReservation(res, p);
    }

    @Test
    public void testCompare() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        Show show2 = new Show(f, LocalDateTime.of(2015, 01, 16, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        assertEquals(0, show.compareTo(show));
        assertEquals(1, show2.compareTo(show));
        assertEquals(-1, show.compareTo(show2));
    }

    @Test
    public void testSort() throws Exception {
        Show show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, new Hall(0, 10, 10));
        show.setSortElement("postsbusy");
        assertEquals(show.getSortElement(), 0);
        show.setSortElement("title");
        show.setSortElement("hall");
        show.setSortElement("date");
        show.setSortElement("postsfree");
        show.setSortElement("po");

    }
}