package engine;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class HallTest {
    private Reservation validPayedReservation;
    private Reservation validUnPayedReservation;
    private Reservation unValidPayedReservation;
    private Reservation validUnPayedReservationDiffPlace;
    private Hall hall;

    @Before
    public void setUp() throws Exception {
        validPayedReservation = new Reservation(null, null, true, 10, null, new Place(5, 10));
        validUnPayedReservationDiffPlace = new Reservation(null, null, false, 10, null, new Place(6, 10));
        validUnPayedReservation = new Reservation(null, null, true, 10, null, new Place(5, 10));
        unValidPayedReservation = new Reservation(null, null, true, 10, null, new Place(500, 10));
        hall = new Hall(0, 50, 50);
    }

    @Test
    public void testGetters() throws Exception {
        assertEquals(0, hall.getNumber());
        assertEquals(2500, hall.getFree());
        assertEquals(0, hall.getBusy());
        assertEquals(0, hall.getReserved());
        assertEquals(50, hall.getCols());
        assertEquals(50, hall.getRows());
    }

    @Test
    public void testReservation() throws Exception {
        hall.setReservation(validPayedReservation, validPayedReservation.getPosition());
        assertEquals(2499, hall.getFree());
        assertEquals(1, hall.getBusy());
        assertEquals(0, hall.getReserved());
        hall.setReservation(validUnPayedReservationDiffPlace, validUnPayedReservationDiffPlace.getPosition());
        assertEquals(2498, hall.getFree());
        assertEquals(1, hall.getBusy());
        assertEquals(1, hall.getReserved());
        Position[][] a = hall.getPositions();
    }

    @Test(expected = UnExistentPlaceException.class)
    public void testUnValidPlace() throws Exception {
        hall.setReservation(unValidPayedReservation, unValidPayedReservation.getPosition());
    }

    @Test
    public void testRemoveReserv() throws Exception {
        hall.setReservation(validPayedReservation, validPayedReservation.getPosition());
        hall.removeReservation(validPayedReservation.getPosition());
        assertEquals(2500, hall.getFree());
        assertEquals(0, hall.getBusy());
        assertEquals(0, hall.getReserved());
    }

    @Test
    public void testDisable() throws Exception {
        hall.setDisabled(validPayedReservation.getPosition());
        Assert.assertEquals(Position.Status.DISABLED, hall.getState(validPayedReservation.getPosition()));
        assertEquals(2499, hall.getFree());
        assertEquals(0, hall.getBusy());
        assertEquals(0, hall.getReserved());
        assertEquals(1, hall.getDisabled());
        hall.setEnabled(validPayedReservation.getPosition());

        assertEquals(2500, hall.getFree());
        assertEquals(0, hall.getBusy());
        assertEquals(0, hall.getReserved());
        assertEquals(0, hall.getDisabled());
        hall.setDisabled(validPayedReservation.getPosition());
        hall.removeReservation(validPayedReservation.getPosition());
    }

    @Test(expected = UnExistentPlaceException.class)
    public void testDisableException() throws Exception {
        hall.setDisabled(new Place(1500, 10));
    }

    @Test(expected = BusyPlaceException.class)
    public void testDisableExceptionBusy() throws Exception {
        hall.setReservation(validPayedReservation, validPayedReservation.getPosition());
        hall.setDisabled(validPayedReservation.getPosition());
    }

    @Test(expected = UnExistentPlaceException.class)
    public void testEnableExceptionOut() throws Exception {
        hall.setEnabled(new Place(1500, 10));
    }

    @Test(expected = NotChangedException.class)
    public void testEnableException() throws Exception {
        hall.setEnabled(validPayedReservation.getPosition());
    }

    @Test
    public void testToString() throws Exception {
        String a = hall.toString();
    }

    @Test
    public void testEqualsCloneCompare() throws Exception {
        Hall clone = (Hall) hall.clone();
        assertEquals(true, hall.equals(clone));
        assertEquals(clone.hashCode(), hall.hashCode());
        Hall testHall = new Hall(1, 2, 10);
        assertEquals(1, testHall.compareTo(hall));
    }

    @Test
    public void testHallCreation() throws Exception {
        Hall testHall = new Hall(1, 50, 10);
        assertEquals(1, testHall.getNumber());
        assertEquals(500, testHall.getFree());
        assertEquals(0, testHall.getBusy());
        assertEquals(0, testHall.getReserved());
        assertEquals(10, testHall.getCols());
        assertEquals(50, testHall.getRows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationException() throws Exception {
        Hall testHall = new Hall(1, 0, 10);
    }

    @Test(expected = UnExistentPlaceException.class)
    public void testRemoveExc() throws Exception {
        hall.removeReservation(new Place(1000, 1500));
    }

    @Test(expected = UnExistentPlaceException.class)
    public void testGetStateExeption() throws Exception {
        hall.getState(new Place(1000, 1500));
    }
}