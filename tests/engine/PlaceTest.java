package engine;

import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class PlaceTest {
    @Test
    public void testClone() throws Exception {
        Place p1 = new Place(10, 10);
        Place cloned = (Place) p1.clone();
        assertEquals(p1, cloned);
        assertEquals(p1.hashCode(), cloned.hashCode());
    }

    @Test
    public void testGetters() throws Exception {
        Place p1 = new Place(10, 12);
        Assert.assertEquals(10, p1.getRow());
        assertEquals(12, p1.getCol());
        assertTrue(p1.toString().length() > 0);
    }

    @Test
    public void testCompare() throws Exception {
        Place p1 = new Place(10, 12);
        Place p2 = new Place(10, 10);
        assertEquals(0, p1.compareTo(p1));
        assertEquals(1, p1.compareTo(p2));
        assertEquals(-1, p2.compareTo(p1));
    }
}