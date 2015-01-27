package db;

import logger.InLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Default object manager
 */
public class DefaultManager<T> implements Iterable<T> {
    protected ArrayList<T> objectList;

    /**
     * Sets the source of data
     *
     * @param list ArrayList of the source
     */
    public void setSource(ArrayList<T> list) {
        objectList = list;
    }

    /**
     * Drops everything
     *
     * @throws IOException
     */
    public void drop() throws IOException {
        objectList.clear();
        InLogger.info("DB dropped");
    }

    /**
     * Returns object by it index
     *
     * @param inx index
     * @return T object
     */
    public T get(int inx) {
        return objectList.get(inx);
    }

    /**
     * Sets object into index
     *
     * @param inx index where to set
     * @param o   object to set
     * @throws IOException
     */
    public void set(int inx, T o) throws IOException {
        objectList.set(inx, o);
    }

    /**
     * Adds object in the end of arrayList
     *
     * @param o object
     * @throws IOException
     */
    public void add(T o) throws IOException {
        objectList.add(o);
    }

    /**
     * Removes object from arrayList
     *
     * @param o object to remove
     * @throws IOException
     */
    public void remove(T o) throws IOException {
        objectList.remove(o);
    }

    /**
     * Remove by index
     *
     * @param inx index to remove
     * @throws IOException
     */
    public void remove(int inx) throws IOException {
        objectList.remove(inx);
    }

    /**
     * Returns index by object
     *
     * @param o searched object
     * @return
     */
    public int indexOf(T o) {
        return objectList.indexOf(o);
    }

    /**
     * List size
     *
     * @return
     */
    public int size() {
        return objectList.size();
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> iter = objectList.iterator();
        return iter;
    }
}
