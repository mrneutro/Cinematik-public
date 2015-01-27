package engine.accounts;

import engine.*;
import engine.discount.DiscountFactory;
import engine.discount.AbstractDiscount;
import gui.Icons;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDateTime;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class ClientTest {
    private final String DESCRIPTION = "Here we a have a long long description";
    private final String TITLE = "Small Title";
    private final String LENGTH = "200";
    private Film testFilm;
    private LocalDateTime morning;
    private LocalDateTime evening;
    private Client standart;
    private Hall hall;

    @Before
    public void setUp() throws Exception {
        testFilm = new Film(TITLE, DESCRIPTION, LENGTH, new ImageIcon(Icons.addIcon), "2014");

        morning = LocalDateTime.of(2015, 1, 1, 10, 00);
        evening = LocalDateTime.of(2015, 1, 1, 20, 00);

        standart = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));

        hall = new Hall(0, 10, 12);
    }

    @Test
    public void testNewTicket() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), true, discount);

        assertEquals(1, standart.getMyTickets().size());
        Reservation rem = eveningShow.getHall().getPositions()[1][1].getReservation();
        Assert.assertEquals(rem, standart.getMyTickets().get(0));
    }

    @Test
    public void testConfirming() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), false, discount);
        Reservation res = standart.getMyTickets().get(0);
        assertEquals(false, res.isPayed());

        standart.confirmReservation(res);
        Assert.assertEquals(true, res.isPayed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfirmingNotExists() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), false, discount);
        Reservation res = standart.getMyTickets().get(0);
        standart.removeReservation(res);
        standart.confirmReservation(res);
    }

    @Test
    public void testNotChanged() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), false, discount);
        Reservation res = standart.getMyTickets().get(0);
        res.getShow().removeReservation(res, res.getPosition());
        standart.removeReservation(res);
    }

    @Test
    public void testRemoving() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), false, discount);
        Reservation res = standart.getMyTickets().get(0);
        assertEquals(res, res.getShow().getHall().getPositions()[1][1].getReservation());

        standart.removeReservation(res);

        assertEquals(0, standart.getMyTickets().size());
        assertEquals(null, res.getShow().getHall().getPositions()[1][1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTestException() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);

        standart.makeTicket(eveningShow, new Place(1, 1), false, discount);
        Reservation res = standart.getMyTickets().get(0);
        standart.removeReservation(res);
        standart.removeReservation(res);

    }

    @Test
    public void testCloning() throws Exception {
        Client clone = (Client) standart.clone();
        assertTrue(standart.equals(clone));
        Assert.assertEquals(standart.hashCode(), clone.hashCode());
        TestCase.assertTrue(standart.toString().length() > 0);
    }
}