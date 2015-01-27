package engine.custom_managers;

import db.ContentProvider;
import db.DefaultManager;
import engine.Film;
import engine.Show;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Custom film manager for improved event verification
 */
public class FilmManager extends DefaultManager<Film> {
    /**
     * Safe removing of film
     *
     * @param film film to delete
     * @throws IOException in case is this film is used in some shows
     */
    public void remove(Film film) throws IOException {
        ContentProvider cp = new ContentProvider();
        ArrayList<Show> showsList = cp.getShows();
        for (Show s : showsList)
            if (s.getFilm().equals(film)) {
                throw new IOException("Impossibile cancellare film perché è usato nel programma del " + s.getDate());
            }
        super.remove(film);
    }
}
