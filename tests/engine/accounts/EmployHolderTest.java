package engine.accounts;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class EmployHolderTest {
    EmployHolder e;
    String[] vals;

    @Before
    public void setUp() throws Exception {
        e = new EmployHolder(0);
    }

    @Test
    public void testGetValues() throws Exception {
        vals = EmployHolder.getValues();
        assertThat(vals.length, greaterThan(0));
        for (int i = 0; i < vals.length; i++) {
            EmployHolder employHolder = new EmployHolder(i);
            assertEquals(vals[i], employHolder.getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue() throws Exception {
        new EmployHolder(5);
    }

    @Test
    public void testClone() throws Exception {
        EmployHolder clone = e.clone();
        assertTrue(e.equals(clone));
        assertEquals(e.hashCode(), clone.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        String data = e.toString();

    }
}