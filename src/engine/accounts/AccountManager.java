package engine.accounts;

import db.ContentProvider;
import db.DefaultManager;
import logger.InLogger;

import java.io.IOException;

/**
 * Main user manager, registration and login
 */
public class AccountManager extends DefaultManager<User> {
    private ContentProvider cp;

    /**
     * Initializes Content Provider and loads users
     *
     * @throws IOException
     */
    public AccountManager() throws IOException {
        cp = new ContentProvider();
        this.setSource(cp.getUsers());
    }

    /**
     * Forsed flush data on disk
     *
     * @throws IOException
     */
    public void forsedFlush() throws IOException {
        cp.flush();
    }

    /**
     * Gets user by ID, null returned if not exists
     *
     * @param id used index
     * @return User object
     */
    public User getUserById(int id) {
        return objectList.get(id);
    }

    /**
     * Registration of new user
     *
     * @param name       Name of user
     * @param email      Email
     * @param pass       Password
     * @param age        Age
     * @param cardNumber Card number
     * @param cardDate   Card date
     * @param cardCvv    CVV code of the card
     * @param role       Role 0 - if user 1 if admin
     * @param employment Employment index
     * @throws UserExistException If user just registered
     * @throws IOException
     */
    public void register(String name, String email, String pass, int age, String cardNumber, String cardDate, String cardCvv, int role, int employment) throws UserExistException, IOException {
        try {
            BankAccount bankAccount = new BankAccount(cardNumber, name, cardCvv, cardDate);
            EmployHolder employHolder = new EmployHolder(employment);
            if (isPresent(email)) {
                throw new UserExistException("Utente con email " + email + " già presente nel sistema");
            }
            if (objectList.size() == 0) {//First registration
                Admin user = new Admin(name, email, pass, age, 1, bankAccount, employHolder);
                objectList.add(user);
            } else {
                Client user = new Client(name, email, pass, age, role, bankAccount, employHolder);
                objectList.add(user);
            }
            forsedFlush();
        } catch (IllegalArgumentException ex) {
            InLogger.exception(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Removes user form getList
     *
     * @param email user email
     * @return boolean true if existed, false othervise
     */
    public boolean removeByEmail(String email) {
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i).getEmail().equals(email)) {
                objectList.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if user just exists
     *
     * @param email user email
     * @return boolean true if exists, false otherwise
     */
    public boolean isPresent(String email) {
        for (User u : objectList) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Login of user into system, return User object
     *
     * @param email user email
     * @param pass  password
     * @return User object
     * @throws UserNotFoundException if user not found
     */
    public User login(String email, String pass) throws UserNotFoundException {
        for (User u : objectList)
            if (u.getEmail().equals(email)) {
                if (u.getPass().equals(pass)) {
                    return u;
                } else {
                    throw new UserNotFoundException("Email esiste, pero password non valida");
                }
            }
        throw new UserNotFoundException("Utente non tovato");
    }
}
