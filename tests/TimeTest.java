import utils.Time;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class TimeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEquals() throws Exception {
        Time time1 = new Time(9, 12);
        Time time2 = new Time(9, 12);
        Time time3 = new Time(8, 12);
        assertEquals(true, time1.equals(time2));
        assertEquals(true, time1.equals(time1));
        assertEquals(false, time1.equals(time3));
        assertEquals(false, time2.equals(time3));
        assertEquals(false, time1.equals(null));
    }

    @Test
    public void testCompareTo() throws Exception {
        Time time1 = new Time(12, 10);
        Time time2 = new Time(12, 11);
        Time time3 = new Time(11, 11);
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time1));
        assertEquals(1, time1.compareTo(time3));
        assertEquals(-1, time3.compareTo(time1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateException() {
        Time time = new Time(24, 00);
    }

    @Test
    public void testGetter() throws Exception {
        Time time1 = new Time(12, 10);
        assertEquals(12, time1.getHour());
        assertEquals(10, time1.getMinute());
        assertEquals("utils.Time{hour=12, minute=10}", time1.toString());
    }

    @Test
    public void testGetHours() throws Exception {
        String[] hours = Time.getHours();
        assertEquals("0", hours[0]);
        assertEquals("23", hours[23]);
    }

    @Test
    public void testGetMinutes() throws Exception {
        String[] hours = Time.getMinutes();
        assertEquals("0", hours[0]);
        assertEquals("59", hours[59]);
    }

    @Test
    public void testGetHumanTime() throws Exception {
        Time time1 = new Time(9, 12);
        Time time2 = new Time(0, 1);
        assertEquals("09:12", time1.getHumanTime());
        assertEquals("00:01", time2.getHumanTime());
    }
}