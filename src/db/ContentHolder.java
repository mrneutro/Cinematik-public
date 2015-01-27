package db;

import engine.Film;
import engine.Hall;
import engine.Show;
import engine.accounts.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Main holder of objects
 */
public class ContentHolder implements Serializable {
    private static final long serialVersionUID = 1L;
    protected ArrayList<User> users;
    protected ArrayList<Film> films;
    protected ArrayList<Hall> halls;
    protected ArrayList<Show> shows;

    public ContentHolder() {
        users = new ArrayList<>();
        films = new ArrayList<>();
        halls = new ArrayList<>();
        shows = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return (users == null ? users = new ArrayList<>() : users);
    }

    public ArrayList<Film> getFilms() {
        return (films == null ? films = new ArrayList<>() : films);
    }

    public ArrayList<Hall> getHalls() {
        return (halls == null ? halls = new ArrayList<>() : halls);
    }

    public ArrayList<Show> getShows() {
        return (shows == null ? shows = new ArrayList<>() : shows);
    }
}
