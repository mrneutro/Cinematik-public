package engine;

import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import gui.Icons;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDateTime;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class PositionTest {
    public ImageIcon imageIcon;
    public final String DESCRIPTION = "Here we a have a long long description";
    public final String TITLE = "Small Title";
    public final String LENGTH = "75";
    Film f;
    Show show;
    Client client;

    @Before
    public void setUp() throws Exception {
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
        Hall h = new Hall(0, 10, 10);
        show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, h);
        client = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
    }

    @Test
    public void testCreate() throws Exception {
        Reservation r = new Reservation(null, null, true, 0.00, null, null);
        Place place = new Place(10, 10);
        Position pos1 = new Position(Position.Status.FREE, r, place);

        assertEquals(r, pos1.getReservation());
        assertEquals(place, pos1.getPlace());
        assertEquals(Position.Status.FREE, pos1.getStatus());

        TestCase.assertTrue(pos1.toString().length() > 0);
    }

    @Test
    public void testClone() throws Exception {
        Place place = new Place(10, 10);
        Reservation reservation = new Reservation(show, client, true, 10, null, place);
        Position pos1 = new Position(Position.Status.FREE, reservation, place);
        Position clone = (Position) pos1.clone();
        assertTrue(clone.equals(pos1));
        TestCase.assertTrue(clone.hashCode() == pos1.hashCode());
    }

    @Test
    public void testCompare() throws Exception {
        Reservation r = new Reservation(null, null, true, 0.00, null, null);
        Place place = new Place(10, 10);
        Position pos1 = new Position(Position.Status.FREE, r, place);

        Place place2 = new Place(10, 12);
        Position pos2 = new Position(Position.Status.FREE, r, place2);
        assertEquals(-1, pos1.compareTo(pos2));
        assertEquals(1, pos2.compareTo(pos1));
        assertEquals(0, pos1.compareTo(pos1));
    }
}