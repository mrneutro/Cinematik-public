package engine;

import db.ContentProvider;
import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import engine.custom_managers.ShowManagerTest;
import engine.discount.DiscountFactory;
import javafx.util.Pair;
import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnalyticsTest extends ShowManagerTest {

    @Test
    public void testGeneral() throws Exception {
        shows.add(show11);
        shows.add(show13);
        shows.add(show15);
        Client standart = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
        standart.makeTicket(show11, new Place(1, 1), true, DiscountFactory.getDiscount(standart, show11));
        standart.makeTicket(show13, new Place(1, 2), true, DiscountFactory.getDiscount(standart, show13));
        standart.makeTicket(show15, new Place(1, 3), true, null);
        standart.makeTicket(show15, new Place(1, 4), false, DiscountFactory.getDiscount(standart, show11));
        Pair<LocalDate, LocalDate> period = new Pair<>(LocalDate.of(2015, 01, 01), LocalDate.of(2015, 01, 30));
        Analytics a = new Analytics(new ContentProvider(), period, obj -> true);
        assertEquals(32.34, a.getTotalProfit());
        assertEquals(3, a.getTotalTickets());
        assertEquals(1, a.getTotalReservations());
        assertEquals(2, a.getTicketsWithDiscount());
        assertTrue(a.toString().length() > 0);
    }
}