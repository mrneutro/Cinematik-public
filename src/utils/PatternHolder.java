package utils;

/**
 * Pattern holder
 */
public class PatternHolder {
    public static final String EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String NAME = "^[A-Za-zèùàòé][a-zA-Z'èùàòé ]*$";
    public static final String PASS = "^[\\S]{4,}$";
    public static final String CVV = "^[\\d]{3}$";
    public static final String YEAR = "^[\\d]{4}$";
    public static final String DESCR = "^[\\s\\S]+$";
    public static final String NUMBER = "^[\\d]+$";
    public static final String FLENGTH = "^[\\d]{1,3}$";
    public static final String PRICE = "^[\\d.]+$";
}
