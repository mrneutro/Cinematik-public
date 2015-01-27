package engine.accounts;

/**
 * Admin class
 */
public class Admin extends User {
    /**
     * Admin constuction
     *
     * @param name        Name
     * @param email       Email - must be unique
     * @param pass        Password - look at regex for other data
     * @param age         Age
     * @param role        By default 0, but for admin is 1
     * @param bankAccount Bank account
     * @param employ      Profession
     */
    public Admin(String name, String email, String pass, int age, int role, BankAccount bankAccount, EmployHolder employ) {
        super(name, email, pass, age, role, bankAccount, employ);
    }
}
