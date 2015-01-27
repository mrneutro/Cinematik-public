package engine;

import gui.Icons;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class FilmTest {

    private Film f;
    private ImageIcon imageIcon;
    private final String DESCRIPTION = "Here we a have a long long description";
    private final String TITLE = "Small Title";
    private final String LENGTH = "75";

    @Before
    public void setUp() throws Exception {
        imageIcon = new ImageIcon(Icons.addIcon);
        f = new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "2014");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilmNameException() throws Exception {
        new Film("", DESCRIPTION, LENGTH, imageIcon, "2014");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDescriptionException() throws Exception {
        new Film(TITLE, "", LENGTH, imageIcon, "2014");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearIncompatibleError() throws Exception {
        new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "abs");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearIntervalException() throws Exception {
        new Film(TITLE, DESCRIPTION, LENGTH, imageIcon, "0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageNullException() throws Exception {
        new Film(TITLE, DESCRIPTION, LENGTH, null, "2014");
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(TITLE, f.getTitle());
    }

    @Test
    public void testGetTimestamp() throws Exception {
        assertNotNull(f.getDate());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(DESCRIPTION, f.getDescription());
    }

    @Test
    public void testGetImg() throws Exception {
        assertEquals(imageIcon, f.getImg());
    }

    @Test
    public void testGetYear() throws Exception {
        assertEquals("2014", f.getYear());
    }

    @Test
    public void testToString() throws Exception {
        assertThat(f.toString().length(), greaterThan(0));
    }

    @Test
    public void testClone() throws Exception {
        Film clone = (Film) f.clone();
        Assert.assertEquals(clone, f);
        Assert.assertEquals(clone.hashCode(), f.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilmLengthException() throws Exception {
        new Film(TITLE, DESCRIPTION, "as", imageIcon, "2014");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilmLengthNullException() throws Exception {
        new Film(TITLE, DESCRIPTION, "0", imageIcon, "2014");
    }

    @Test
    public void testCompare() throws Exception {
        Film f1 = new Film(TITLE, DESCRIPTION, "117", imageIcon, "2014");
        Thread.sleep(5);
        Film f2 = new Film(TITLE, DESCRIPTION, "117", imageIcon, "2014");
        assertEquals(0, f1.compareTo(f1));
        assertEquals(-1, f1.compareTo(f2));
        assertEquals(1, f2.compareTo(f1));
    }
}