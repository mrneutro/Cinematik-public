package engine.accounts;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankAccountTest {
    private BankAccount b;

    @Before
    public void setUp() throws Exception {
        b = new BankAccount("5964309212345698", "Sergio Ss", "062", "11/16");
    }

    @Test
    public void testBankAccount() throws Exception {
        assertEquals("engine.accounts.BankAccount{number='5964309212345698', name='Sergio Ss', circuit='MasterCard', cvv='062'}", b.toString());
    }

    @Test
    public void testGetters() throws Exception {
        assertEquals("5964309212345698", b.getNumber());
        assertEquals("Sergio Ss", b.getName());
        assertEquals("MasterCard", b.getCircuit());
        assertEquals("062", b.getCvv());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongCVV() throws Exception {
        new BankAccount("5964309212345698", "Sergio Ss", "06", "11/16");
    }

    @Test
    public void testClone() throws Exception {
        BankAccount clone = b.clone();
        assertEquals(clone, b);
        assertEquals(b.hashCode(),clone.hashCode());
    }
}