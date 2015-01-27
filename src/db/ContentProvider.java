package db;

import engine.Film;
import engine.Hall;
import engine.Show;
import engine.accounts.User;
import logger.InLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.ArrayList;

/**
 * Layer between fisical file and object graph
 */
public class ContentProvider {
    private static final String CONTAINER = "content.bin";
    protected static ContentHolder ch;

    /**
     * Content provider for all data in Cinematik
     *
     * @throws IOException
     */
    public ContentProvider() throws IOException {
        if (ch == null) {
            InLogger.info("Loading DB from file");
            try {
                ch = DbObject.read(CONTAINER);
            } catch (FileNotFoundException ex) {
                InLogger.exception(ex.getMessage());
                ch = new ContentHolder();
                flush();
            } catch (ClassNotFoundException ex) {
                InLogger.exception("Corrupted container: " + CONTAINER);
                flush();
            } catch (IOException ex) {
                InLogger.exception(ex.getMessage());
                flush();
            }
        }
    }

    /**
     * Users arrayList
     *
     * @return
     */
    public ArrayList<User> getUsers() {
        return ch.getUsers();
    }

    /**
     * Halls arrayList
     *
     * @return
     */
    public ArrayList<Hall> getHalls() {
        return ch.getHalls();
    }

    /**
     * Shows arrayList
     *
     * @return
     */
    public ArrayList<Show> getShows() {
        return ch.getShows();
    }

    /**
     * Films arrayList
     *
     * @return
     */
    public ArrayList<Film> getFilms() {
        return ch.getFilms();
    }

    /**
     * Flush data to disk
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        try {
            DbObject.write(ch, CONTAINER);
        } catch (NotSerializableException ex) {
            InLogger.exception(ex.getMessage());
            throw new RuntimeException("Error saving data to file");
        }
    }
}
