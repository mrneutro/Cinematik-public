package engine;

import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import engine.discount.AbstractDiscount;
import engine.discount.DiscountFactory;
import gui.Icons;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDateTime;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class ReservationTest {
    public ImageIcon imageIcon;
    public final String DESCRIPTION = "Here we a have a long long description";
    public final String TITLE = "Small Title";
    public final String LENGTH = "75";
    Film f;
    Show show;
    Client client;
    Client client2;

    @Before
    public void setUp() throws Exception {
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
        Hall h = new Hall(0, 10, 10);
        show = new Show(f, LocalDateTime.of(2015, 01, 15, 11, 00, 00), 11, false, h);
        client = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
        client2 = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(0));
    }

    @Test
    public void testCreate() throws Exception {
        AbstractDiscount disc = DiscountFactory.getDiscount(client, show);
        Reservation res = new Reservation(show, client, false, 10, disc, new Place(10, 10));
        assertEquals(show, res.getShow());
        assertEquals(client, res.getReservedBy());
        assertEquals(10.0, res.getTotalSum());
        assertEquals(disc, res.getAbstractDiscount());
        assertTrue(res.getTime().isAfter(LocalDateTime.now().minusSeconds(10)));
        assertTrue(res.getTime().isBefore(LocalDateTime.now().plusSeconds(10)));
        assertTrue(res.toString().length() > 0);
    }

    @Test
    public void testClone() throws Exception {
        Reservation res = new Reservation(show, client, false, 10, null, new Place(10, 10));
        Reservation clone = (Reservation) res.clone();
        assertEquals(clone, res);
        assertTrue(clone.hashCode() == res.hashCode());
    }

    @Test
    public void testEqualsFalse() throws Exception {
        AbstractDiscount disc = DiscountFactory.getDiscount(client, show);
        Reservation res = new Reservation(show, client, false, 10, disc, new Place(10, 10));
        AbstractDiscount disc2 = DiscountFactory.getDiscount(client2, show);
        Reservation res2 = new Reservation(show, client2, false, 10, disc2, new Place(10, 10));
        assertFalse(res.equals(res2));
    }

    @Test
    public void testCompareTo() throws Exception {
        Reservation res = new Reservation(show, client, false, 10, null, new Place(10, 10));
        Thread.sleep(100);
        Reservation res2 = new Reservation(show, client, false, 10, null, new Place(10, 10));
        Assert.assertEquals(0, res.compareTo(res));
        Assert.assertEquals(-1, res.compareTo(res2));
        Assert.assertEquals(1, res2.compareTo(res));
    }
}