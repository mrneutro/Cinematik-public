package engine.accounts;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class UserTest {
    private User client;
    private User admin;

    @Before
    public void setUp() throws Exception {
        client = new User("TestName", "test@gmail.com", "password", 11, 0, null, null);
        admin = new User("TestName", "test@gmail.com", "password", 11, 1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmailException() throws Exception {
        new User("Test", "ssss", "password", 11, 0, null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameException() throws Exception {
        new User("", "test@gmail.com", "password", 11, 0, null, null);
    }

    @Test
    public void testIsAdmin() throws Exception {
        assertTrue(admin.isAdmin());
        assertFalse(client.isAdmin());
    }

    @Test
    public void testToString() throws Exception {
        assertThat(admin.toString().length(), greaterThan(0));
        assertThat(client.toString().length(), greaterThan(0));
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("TestName", admin.getName());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("test@gmail.com", admin.getEmail());
    }

    @Test
    public void testGetPass() throws Exception {
        assertEquals("password", admin.getPass());
    }

    @Test
    public void testGetAge() throws Exception {
        assertEquals(11, admin.getAge());
    }

}