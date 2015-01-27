package engine.custom_managers;


import db.ContentProvider;
import db.DefaultManager;
import engine.ObjectFilter;
import engine.Show;
import logger.InLogger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Custom ShowManager
 */
public class ShowManager extends DefaultManager<Show> {
    private ContentProvider cp;

    /**
     * Initialize ContentProvider and sets the source
     *
     * @throws IOException if there is problem with CP
     */
    public ShowManager() throws IOException {
        cp = new ContentProvider();
        this.setSource(cp.getShows());
    }

    /**
     * Initialize with CP
     *
     * @param cp ContentProvider
     */
    public ShowManager(ContentProvider cp) {
        this.cp = cp;
        this.setSource(cp.getShows());
    }

    /**
     * Adds new Show
     *
     * @param show Show to add
     * @throws IOException is there is time problems
     */
    public void add(Show show) throws IOException {
        objectList.add(show);
        ArrayList<Show> thisHall = new ArrayList<>();
        for (Show s : objectList) {
            if (s.getHall().equals(show.getHall()))
                thisHall.add(s);
        }
        Collections.sort(thisHall);
        int inx = thisHall.indexOf(show);
        Show prev = null;
        if (inx > 0) {
            prev = thisHall.get(inx - 1);
        }
        Show next = null;
        if (thisHall.size() != inx + 1) {
            next = thisHall.get(inx + 1);
        }
        LocalDateTime prevEndsAt = prev == null ? null : prev.getDate().plusMinutes(prev.getFilm().getLength());
        LocalDateTime thisEndsAt = show.getDate().plusMinutes(show.getFilm().getLength());

        if ((prev == null || prevEndsAt.isBefore(show.getDate())) && (next == null || next.getDate().isAfter(thisEndsAt))) {
            InLogger.info("Show added");
        } else {
            objectList.remove(show);
            throw new IOException("Questo evento non può essere inserito a causa di intersezione");
        }
    }

    /**
     * Return shows in interval
     *
     * @param from   Selection from
     * @param to     Selection TO
     * @param filter Filtering
     * @return List of shows
     */
    public ArrayList<Show> getInInterval(LocalDate from, LocalDate to, ObjectFilter filter) {
        ArrayList<Show> result = new ArrayList<>();
        for (Show s : objectList) {
            if (from.compareTo(s.getDate().toLocalDate()) <= 0 && to.compareTo(s.getDate().toLocalDate()) > 0) {
                if (filter.canAdd(s))
                    result.add(s);
            }
        }
        return result;
    }

}
