/**
 * Created by Neutro on 24/11/2014.
 */
package engine.accounts;

import utils.PatternHolder;

import java.io.Serializable;

/**
 * User main class
 */
public class User implements Serializable, Cloneable {
    private static final long serialVersionUID = 11L;
    private String name;
    private String email;
    private String pass;
    private int age;
    private int role = 0;
    private BankAccount bankAccount;
    private EmployHolder employ;

    /**
     * User constuction
     *
     * @param name        Name
     * @param email       Email - must be unique
     * @param pass        Password - look at regex for other data
     * @param age         Age
     * @param role        By default 0 for user, admin 1
     * @param bankAccount Bank account
     * @param employ      Profession
     */
    public User(String name, String email, String pass, int age, int role, BankAccount bankAccount, EmployHolder employ) {
        if (!email.matches(PatternHolder.EMAIL)) {
            throw new IllegalArgumentException("E-mail non è valido");
        }
        if (!name.matches(PatternHolder.NAME)) {
            throw new IllegalArgumentException("Il nome deve essere corretto");
        }
        if (!pass.matches(PatternHolder.PASS)) {
            throw new IllegalArgumentException("Password troppo corta");
        }
        this.name = name;
        this.email = email;
        this.age = age;
        this.pass = pass;
        this.role = role;
        this.bankAccount = bankAccount;
        this.employ = employ;
    }

    public boolean isAdmin() {
        return role == 0 ? false : true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", age=" + age +
                ", role=" + role +
                ", bankAccount=" + bankAccount +
                ", employ=" + employ +
                '}';
    }

    @Override
    protected Object clone() {
        User clone = null;
        try {
            clone = (User) super.clone();
            clone.bankAccount = bankAccount.clone();
            clone.employ = employ.clone();
        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    /**
     * Username
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * User email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * User password
     *
     * @return password
     */
    public String getPass() {
        return pass;
    }

    /**
     * User age
     *
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * User employ
     *
     * @return profession
     */
    public EmployHolder getEmploy() {
        return employ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;

        if (age != user.age) return false;
        if (role != user.role) return false;
        if (!bankAccount.equals(user.bankAccount)) return false;
        if (!email.equals(user.email)) return false;
        if (!name.equals(user.name)) return false;
        if (!pass.equals(user.pass)) return false;
        if (!employ.equals(user.employ)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + pass.hashCode();
        result = 31 * result + age;
        result = 31 * result + role;
        result = 31 * result + bankAccount.hashCode();
        result = 31 * result + employ.hashCode();
        return result;
    }
}
