package engine.accounts;

import utils.PatternHolder;

import java.io.Serializable;

/**
 * BankAccount data holder
 */
public class BankAccount implements Serializable, Cloneable {
    private String number;
    private String name;
    private String circuit;
    private String cvv;

    /**
     * Creates and checks all data
     *
     * @param number CardNumber
     * @param name   Name on card
     * @param cvv    CVV code
     * @param date   Date like 11/14
     * @throws java.lang.IllegalArgumentException in case of errored data
     */
    public BankAccount(String number, String name, String cvv, String date) {
        circuit = CreditCard.verifyNumber(number);
        CreditCard.verifyDate(date);
        if (!cvv.matches(PatternHolder.CVV)) {
            throw new IllegalArgumentException("Codice CVV non valido");
        }
        this.number = number;
        this.name = name;
        this.cvv = cvv;
    }

    /**
     * Returns a number
     *
     * @return card number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Return a name
     *
     * @return card holder name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns name of circuit
     *
     * @return MasterCard Visa ecc.
     */
    public String getCircuit() {
        return circuit;
    }

    /**
     * CVV code
     *
     * @return CVV
     */
    public String getCvv() {
        return cvv;
    }

    @Override
    protected BankAccount clone() {
        BankAccount clone = null;
        try {
            clone = (BankAccount) super.clone();
        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", circuit='" + circuit + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;

        BankAccount that = (BankAccount) o;

        if (!circuit.equals(that.circuit)) return false;
        if (!cvv.equals(that.cvv)) return false;
        if (!name.equals(that.name)) return false;
        if (!number.equals(that.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + circuit.hashCode();
        result = 31 * result + cvv.hashCode();
        return result;
    }
}
