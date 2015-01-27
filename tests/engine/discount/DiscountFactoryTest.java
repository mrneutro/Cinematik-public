package engine.discount;

import engine.accounts.BankAccount;
import engine.accounts.Client;
import engine.accounts.EmployHolder;
import engine.Film;
import engine.Hall;
import engine.Place;
import engine.Show;
import gui.Icons;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

public class DiscountFactoryTest {
    private final String DESCRIPTION = "Here we a have a long long description";
    private final String TITLE = "Small Title";
    private final String LENGTH = "200";

    private Film testFilm;
    private LocalDateTime morning;
    private LocalDateTime evening;
    private Client standart;
    private Client student;
    private Client pensioner;
    private Client social;
    private Client baby;
    private Hall hall;

    @Before
    public void setUp() throws Exception {
        testFilm = new Film(TITLE, DESCRIPTION, LENGTH, new ImageIcon(Icons.addIcon), "2014");

        morning = LocalDateTime.of(2015, 1, 1, 10, 00);
        evening = LocalDateTime.of(2015, 1, 1, 20, 00);

        standart = new Client("Sergio", "she@gmail.com", "google", 18, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));
        student = new Client("Sergio", "she@gmail.com", "google", 21, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(0));
        pensioner = new Client("Andrea", "she@gmail.com", "google", 72, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(1));
        social = new Client("Sergio", "she@gmail.com", "google", 21, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(2));
        baby = new Client("Sergio", "she@gmail.com", "google", 7, 0, new BankAccount("5964309212341234", "Sherg Aaron", "069", "11/15"), new EmployHolder(3));

        hall = new Hall(0, 10, 12);
    }

    @Test
    public void testStandartDiscount() throws Exception {
        Show eveningShow = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, eveningShow);
        assertEquals(100.0, discount.getPrice(100));
    }

    @Test
    public void testMorningDiscount() throws Exception {
        Show morningShow = new Show(testFilm, morning, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(standart, morningShow);
        assertEquals(97.0, discount.getPrice(100));
    }

    @Test
    public void testPensioneer() throws Exception {
        Show morningShow = new Show(testFilm, morning, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(pensioner, morningShow);
        assertEquals(95.0, discount.getPrice(100));
    }

    @Test
    public void testStudent() throws Exception {
        Show morningShow = new Show(testFilm, morning, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(student, morningShow);
        assertEquals(97.0, discount.getPrice(100)); //Will be applied MORNING discount, NOT STUDENT
        assertEquals("Mattina", discount.getName());
        assertEquals(3.0, discount.getPercentage());
    }

    @Test
    public void testBaby() throws Exception {
        Show show = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(baby, show);
        assertEquals(90.0, discount.getPrice(100));
        assertEquals("Baby", discount.getName());
        assertEquals(10.0, discount.getPercentage());
    }

    @Test
    public void testFiveTickets() throws Exception {
        for (int i = 0; i < 5; i++) {
            Show show = new Show(testFilm, evening, 10, true, hall);
            student.makeTicket(show, new Place(1, i), true, null);
        }
        Show show = new Show(testFilm, evening, 10, true, hall);
        AbstractDiscount discount = DiscountFactory.getDiscount(student, show);
        assertEquals(50.0, discount.getPrice(100));

    }
}