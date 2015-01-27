package db;

import engine.accounts.Client;
import logger.InLogger;
import org.junit.Before;
import org.junit.Test;
import utils.Time;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;

import static junit.framework.TestCase.assertEquals;

public class DbObjectTest {
    Time testObject1;
    protected final String TEST_CONTAINER = "testWrite.bin";
    protected String testPath;

    @Before
    public void setUp() throws Exception {
        DbObject.debug = true;
        testObject1 = new Time(11, 12);
        InLogger.setShowConsole(false);
        removeTestData();
    }

    private void removeTestData() {
        File dbDir = new File("dbDataTest");
        if (dbDir.exists()) {
            File container = new File(dbDir.getAbsolutePath() + "/" + TEST_CONTAINER);
            if (container.exists()) container.delete();
        }
    }

    @Test
    public void testReadWrite() throws Exception {
        DbObject.write(testObject1, TEST_CONTAINER);
        Time testObjectRes = DbObject.read(TEST_CONTAINER);
        assertEquals(true, testObject1.equals(testObjectRes));
    }

    @Test(expected = ClassCastException.class)
    public void testReadException() throws Exception {
        Client failRead = DbObject.read(TEST_CONTAINER);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadFileNotFound() throws Exception {
        Client failRead = DbObject.read("null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadIllegal() throws Exception {
        Client failRead = DbObject.read("");
    }

    @Test(expected = NotSerializableException.class)
    public void testWriteNotSerializable() throws Exception {
        Object o = new Object();
        DbObject.write(o, TEST_CONTAINER);
    }

    @Test(expected = IOException.class)
    public void testWriteIOException() throws Exception {
        DbObject.write(testObject1, "?s");
    }
}