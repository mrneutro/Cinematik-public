package db;

import java.io.*;

/**
 * Objects writes/reads data from file, creates path
 */
public class DbObject {
    private static String DB_FOLDER = "";
    public static boolean debug = false;

    /**
     * Ensures that folder for dbData files exists, if not - creates it
     *
     * @throws java.io.IOException
     */
    private static void ensureFolder() throws IOException {
        File dbDir;
        if (debug) {
            dbDir = new File("dbDataTest");
        } else {
            dbDir = new File("dbData");
        }
        if (!dbDir.exists()) {
            if (!dbDir.mkdir()) {
                throw new FileNotFoundException("Impossibile create dbData dir");
            }
        }
        DB_FOLDER = dbDir.getAbsolutePath(); //Crazy constant creating, I know XD
        dbDir = null;
    }

    /**
     * Writes generic object into file
     *
     * @param obj       Generic object
     * @param container Container, generally - name of file or subclass
     * @throws IOException
     */
    public static <T> void write(T obj, String container) throws IOException {
        ensureFolder();
        if (container.length() == 0) throw new IllegalArgumentException("Empty container value");
        container = DB_FOLDER + File.separator + container;
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(container)));
        out.writeObject(obj);
        out.flush();
    }

    /**
     * Reads generic object from file
     *
     * @param container Container, generally - name of file or subclass
     * @return T Read object
     * @throws java.io.IOException, ClassCastException, ClassNotFoundException
     */
    public static <T> T read(String container) throws IOException, ClassNotFoundException, ClassCastException {
        ensureFolder();
        if (container.length() == 0) throw new IllegalArgumentException("Empty container value");
        container = DB_FOLDER + File.separator + container;
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(container)))) {
            T read = (T) in.readObject();
            if (read == null) {
                throw new FileNotFoundException("File not found or corrupted");
            }
            return read;
        } catch (ClassNotFoundException ex) { //Probably corrupted file
            File f = new File(container);
            f.delete();
            throw ex;
        }
    }
}
