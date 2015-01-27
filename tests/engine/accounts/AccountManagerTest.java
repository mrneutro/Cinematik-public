package engine.accounts;

import db.DbObject;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class AccountManagerTest {
    private AccountManager a;
    private final String EMAIL = "testuser@gmail.com";
    private final String EMAIL2 = "testuser2@gmail.com";

    @Before
    public void setUp() throws Exception {
        DbObject.debug = true;
        InLogger.setShowConsole(false);
        a = new AccountManager();
        a.drop();
    }

    @Test
    public void testRegister() throws Exception {
        a.register("Sergio", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 0, 0);
        assertEquals(true, a.isPresent(EMAIL));
        a.register("Sergio", EMAIL2, "google", 21, "5964309212341234", "11/16", "062", 1, 0);
        assertEquals(true, a.isPresent(EMAIL2));
        assertThat(a.toString().length(), greaterThan(0));
    }

    @Test
    public void testGetUserById() throws Exception {
        a.register("Admin", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 1, 0); // Would be admin
        User user = a.getUserById(a.size() - 1);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(true, user.isAdmin());

        Admin admin = (Admin) user;

        a.register("Client", EMAIL + "c", "google", 21, "5964309212341234", "11/16", "062", 0, 0);
        a.forsedFlush();
        user = a.getUserById(a.size() - 1);
        Client client = (Client) user;
        assertNotEquals(null, client);
        assertEquals(false, client.isAdmin());
    }

    @Test
    public void testIsPresent() throws Exception {
        a.register("Admin", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 1, 0);
        a.forsedFlush();
        assertEquals(true, a.isPresent(EMAIL));
        assertEquals(false, a.isPresent("ssss@sss.ru"));
    }

    @Test
    public void testLogin() throws Exception {
        a.register("Admin", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 1, 0);
        User u = a.login(EMAIL, "google");
        assertEquals(EMAIL, u.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginNotFound() throws Exception {
        a.register("Admin", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 1, 0);
        User u = a.login(EMAIL, "googleaaa");
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginNotFoundEmail() throws Exception {
        User u = a.login(EMAIL + "spi", "googleaaa");
    }


    @Test(expected = UserExistException.class)
    public void testRegisterUserExists() throws Exception {
        a.register("Sergio", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 0, 0);
        a.register("Sergio", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterIllegal() throws Exception {
        a.register("Sergio", EMAIL, "", 21, "5964309212341234", "11/16", "062", 0, 0);
    }

    @Test
    public void testRemove() throws Exception {
        a.register("Sergio", EMAIL, "google", 21, "5964309212341234", "11/16", "062", 0, 0);
        assertTrue(a.isPresent(EMAIL));
        a.removeByEmail(EMAIL);
        assertFalse(a.isPresent(EMAIL));
        assertFalse(a.removeByEmail(EMAIL));
    }

}