package engine.accounts;

import engine.*;
import engine.discount.AbstractDiscount;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Client
 */
public class Client extends User {
    private ArrayList<Reservation> myTickets;

    /**
     * Clients constuction
     *
     * @param name        Name
     * @param email       Email - must be unique
     * @param pass        Password - look at regex for other data
     * @param age         Age
     * @param role        By default 0 for user, admin 1
     * @param bankAccount Bank account
     * @param employ      Profession
     */
    public Client(String name, String email, String pass, int age, int role, BankAccount bankAccount, EmployHolder employ) {
        super(name, email, pass, age, role, bankAccount, employ);
        myTickets = new ArrayList<>();
    }

    /**
     * Creates new ticket
     *
     * @param show     Show
     * @param position Position
     * @param buy      true if payed - false if is obly reserved
     * @param discount Discount if is, otherwise - null
     * @throws UnExistentPlaceException If place not exists
     * @throws BusyPlaceException       If place is busy
     */
    public void makeTicket(Show show, Place position, boolean buy, AbstractDiscount discount) throws UnExistentPlaceException, BusyPlaceException {
        double totalSum = show.getRegularCost();
        Reservation reservation = new Reservation(show, this, buy, totalSum, discount, position);
        myTickets.add(reservation);
        show.addReservation(reservation, position);
    }

    /**
     * List of tickets
     *
     * @return Reservation list
     */
    public ArrayList<Reservation> getMyTickets() {
        return myTickets;
    }

    /**
     * Confirms reservation of tickets
     *
     * @param reservation Reservation
     * @throws NotChangedException if there is an error in confirming
     */
    public void confirmReservation(Reservation reservation) throws NotChangedException {
        if (!myTickets.contains(reservation))
            throw new IllegalArgumentException("Reservation not exists");
        reservation.getShow().confirmReservation(reservation, reservation.getPosition());
    }

    /**
     * Removes reservation
     *
     * @param reservation Reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        try {
            if (!myTickets.contains(reservation))
                throw new IllegalArgumentException("Reservation not exists");
            Show show = reservation.getShow();
            show.removeReservation(reservation, reservation.getPosition());
            myTickets.remove(reservation);
        } catch (NotChangedException ignore) {
            //NOP
        }
    }


    @Override
    public Object clone() {
        Client clone = (Client) super.clone();
        ArrayList<Reservation> clonedList = new ArrayList<>(myTickets.size());
        for (Reservation item : myTickets) clonedList.add((Reservation) item.clone());
        clone.myTickets = clonedList;
        return super.clone();
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + myTickets.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Client client = (Client) o;
        Collections.sort(myTickets);
        Collections.sort(client.myTickets);
        return super.equals(o) && client.myTickets.equals(myTickets);
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "myTickets=" + myTickets +
                '}';
    }
}
