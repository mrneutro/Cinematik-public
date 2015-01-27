package db;

import engine.Hall;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class DefaultManagerTest {
    private DefaultManager<Hall> dm;
    private ArrayList<Hall> arrayList;

    @Before
    public void setUp() throws Exception {
        dm = new DefaultManager<>();
        arrayList = new ArrayList<>();
        for (int i = 0; i < 100; i++) arrayList.add(new Hall(i, i % 10 + 1, i % 12 + 1));
    }

    @Test
    public void testGet() throws Exception {
        dm.setSource(arrayList);
        assertEquals(1, dm.get(1).getNumber());
        Hall nh = new Hall(1000, 100, 100);
        dm.set(10, nh);
        Assert.assertEquals(nh, dm.get(10));
        dm.remove(10);
        Assert.assertEquals(99, dm.size());
        dm.add(nh);
        Assert.assertEquals(100, dm.size());
        dm.remove(nh);
        Assert.assertEquals(99, dm.size());
        dm.add(nh);
        Assert.assertEquals(99, dm.indexOf(nh));
        dm.drop();
        Assert.assertEquals(0, dm.size());
    }

    @Test
    public void testIterator() throws Exception {
        dm.setSource(arrayList);
        int i = 0;
        for (Hall h : dm) {
            i++;
        }
        assertEquals(100, i);
    }
}