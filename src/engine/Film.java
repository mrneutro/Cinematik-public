package engine;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Neutro on 19/11/2014.
 */
public class Film implements Serializable, Cloneable, Comparable<Film> {
    private static final long serialVersionUID = 3L;
    private String name;
    private String description;
    private ImageIcon img;
    private String year;
    private LocalDateTime creation;
    private int length;

    /**
     * Film constructor
     *
     * @param name        Film name
     * @param description Description
     * @param length      Lengths in minutes
     * @param img         Buffered Image
     * @param year        Year
     * @throws java.lang.IllegalArgumentException in case of wrong contract data
     */
    public Film(String name, String description, String length, ImageIcon img, String year) {
        if (name.length() < 2)
            throw new IllegalArgumentException("Nome del film è troppo corto");
        if (description.length() < 10)
            throw new IllegalArgumentException("Lunghezza della descrizione troppo breve");
        int intYear = 0;
        try {
            intYear = Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Anno non valido");
        }
        if (intYear < 1900 || intYear > 2050) {
            throw new IllegalArgumentException("Anno non valido");
        }
        if (img == null) {
            throw new IllegalArgumentException("Immagine deve essere presente!");
        }
        int filmLength = 0;
        try {
            filmLength = Integer.parseInt(length);
            if (filmLength == 0) {
                throw new IllegalArgumentException("Lunghezza film non valida");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Lunghezza film non valida");
        }
        this.creation = LocalDateTime.now();
        this.name = name;
        this.description = description;
        this.img = img;
        this.year = year;
        this.length = filmLength;
    }

    /**
     * Return film lengths in minutes
     *
     * @return minutes
     */
    public int getLength() {
        return length;
    }

    /**
     * Film title
     *
     * @return Film title
     */
    public String getTitle() {
        return name;
    }

    /**
     * Creation date of this film
     *
     * @return date of creation
     */
    public LocalDateTime getDate() {
        return creation;
    }

    /**
     * Description
     *
     * @return film description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns an image icon of film
     *
     * @return film image
     */
    public ImageIcon getImg() {
        return img;
    }

    /**
     * Year
     *
     * @return Year
     */
    public String getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;

        Film film = (Film) o;

        if (creation != null ? !creation.equals(film.creation) : film.creation != null) return false;
        if (description != null ? !description.equals(film.description) : film.description != null) return false;
        if (img != null ? !img.equals(film.img) : film.img != null) return false;
        if (name != null ? !name.equals(film.name) : film.name != null) return false;
        if (year != null ? !year.equals(film.year) : film.year != null) return false;

        return true;
    }


    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (creation != null ? creation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img=" + img +
                ", year='" + year + '\'' +
                ", creation=" + creation +
                ", length=" + length +
                '}';
    }

    @Override
    public int compareTo(Film o) {
        return creation.compareTo(o.creation);
    }
}
