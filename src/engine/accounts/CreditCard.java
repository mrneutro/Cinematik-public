package engine.accounts;

import gui.Icons;

import javax.swing.*;
import java.util.Calendar;

/**
 * Credit card Checker
 */
public class CreditCard {

    public static final String REGEX_AMEX = "^3[47]\\d{14}$";
    public static final String REGEX_MAESTRO = "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$";
    private static final String REGEX_VISA = "^4\\d{15}$";
    private static final String REGEX_MASTERCARD = "^5\\d{15}$";

    /**
     * Return circuit type of card number or throws IllegalArgumentException
     *
     * @param number
     * @return card code
     * @throws java.lang.IllegalArgumentException
     */
    public static String verifyNumber(String number) {
        number = number.replaceAll("\\s+", "");
        if (number.matches(REGEX_VISA)) {
            return "Visa";
        } else if (number.matches((REGEX_AMEX))) {
            return "American Express";
        } else if (number.matches(REGEX_MAESTRO)) {
            return "Maestro";
        } else if (number.matches(REGEX_MASTERCARD)) {
            return "MasterCard";
        } else
            throw new IllegalArgumentException("Codice carta non riconosciuto");
    }

    /**
     * Date validation check
     *
     * @param date string like date rappresentation es 11/15
     * @throws java.lang.IllegalArgumentException if there is some trouble
     */
    public static void verifyDate(String date) {
        String[] parts = date.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR) - 2000;
        int currentMonth = c.get(Calendar.MONTH) + 1; //month starts from 0
        if (currentYear > year) {
            throw new IllegalArgumentException("Validità della carta errata");
        }
        if (currentYear == year && currentMonth > month) {
            throw new IllegalArgumentException("Validità della carta errata");
        }
        if (month > 12 || month <= 0 || year < currentYear) {
            throw new IllegalArgumentException("Validità della carta errata");
        }
    }

    /**
     * Return an image of circuit
     *
     * @param circuit circuit name MasterCard, Visa ecc
     * @return ImageIcon
     */
    public static ImageIcon getCardIcon(String circuit) {
        ImageIcon result = null;
        switch (circuit) {
            case "Visa":
                result = new ImageIcon(Icons.visaLogo);
                break;
            case "American Express":
                result = new ImageIcon(Icons.amexLogo);
                break;
            case "Maestro":
                result = new ImageIcon(Icons.maestroLogo);
                break;
            case "MasterCard":
                result = new ImageIcon(Icons.mastercardLogo);
                break;
            default:
                result = null;
        }
        return result;
    }
}
