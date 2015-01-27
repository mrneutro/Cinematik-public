package engine.accounts;

import org.junit.Test;

import javax.swing.*;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class CreditCardTest {

    @Test
    public void testVerifyNumber() throws Exception {
        CreditCard cd = new CreditCard();
        String mastercard = "5964309212341234";
        String visa = "4444111144441111";
        String amex = "3444444411112222";
        String maestro = "5067111122223333";

        assertEquals("MasterCard", CreditCard.verifyNumber(mastercard));
        assertEquals("Visa", CreditCard.verifyNumber(visa));
        assertEquals("American Express", CreditCard.verifyNumber(amex));
        assertEquals("Maestro", CreditCard.verifyNumber(maestro));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNumberIllegal() throws Exception {
        assertEquals("MasterCard", CreditCard.verifyNumber("0000000000000000"));
    }

    @Test
    public void testVerifyDate() throws Exception {
        CreditCard.verifyDate("11/16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyDataExceptionSmall() throws Exception {
        CreditCard.verifyDate("10/14");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyDataExceptionErr() throws Exception {
        CreditCard.verifyDate("15/16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyDataExceptionErr2() throws Exception {
        CreditCard.verifyDate("00/15");
    }

    @Test
    public void testgetCardIcon() throws Exception {
        ImageIcon test = CreditCard.getCardIcon("Visa");
        assertThat(test, instanceOf(ImageIcon.class));
        test = CreditCard.getCardIcon("MasterCard");
        assertThat(test, instanceOf(ImageIcon.class));
        test = CreditCard.getCardIcon("Maestro");
        assertThat(test, instanceOf(ImageIcon.class));
        test = CreditCard.getCardIcon("American Express");
        assertThat(test, instanceOf(ImageIcon.class));
        ImageIcon test2 = CreditCard.getCardIcon("None");
        assertNull(test2);
    }
}