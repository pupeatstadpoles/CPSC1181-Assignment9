import java.util.ArrayList;


/**
 * A class that represents a one room hotel and allows reservations to be made
 * and cancelled for the month of March.
 *
 * @author Chris Schmidt
 */
public class Hotel {
    private final int DAY_COUNT = 31;
    private ArrayList<String> reservations;

    /**
     * Constructor
     */
    public Hotel() {
        reservations = new ArrayList<String>(DAY_COUNT);
        for (int i = 0; i < DAY_COUNT; i++) {
            reservations.add(null);
        }
    }

    /**
     * Displays the full set of reservation information. Each day is marked with
     * either the user with the reservation or Available
     */
    public String toString() {
        String out = "Hotel Reservation Info\n__________________\n";

        for (int i = 0; i < DAY_COUNT; i++) {
            out += (i + 1) + ": ";
            if (reservations.get(i) == null) {
                out += "Available\n";
            } else {
                out += reservations.get(i) + "\n";
            }
        }
        return out;
    }

    /**
     * Requests a reservation.  The reservation is not made if the user already has a reservation
     * if the first and last days are invalid in any way
     * or if any of the requested days are already taken
     *
     * @param user:  The user requesting a reservation
     * @param first: The first day of the requested reservation
     * @param last:  The last day of the requested reservation
     * @return returns true if the reservation was made, false otherwise
     */
    public synchronized boolean requestReservation(String user, int first, int last) {
        if (reservations.indexOf(user) != -1) {
            return false;// "Only one reservation allowed per person";
        }

        if (first < 1 || last > DAY_COUNT) {
            return false;// "Incorrect Date Given";
        }

        return reserveRoom(reservations, user, first, last);

    }

    /**
     * Cancels the reservation for the user (if one exists)
     *
     * @param user: the user requesting the cancellation
     * @return: true if there was a reservation to cancel
     */
    public synchronized boolean cancelReservation(String user) {
        if (reservations.indexOf(user) == -1) {
            return false;// "You don't have a reservation to cancel.";
        }

        for (int i = 0; i < DAY_COUNT; i++) {
            if (user.equals(reservations.get(i))) {
                reservations.set(i, null);
            }
        }
        return true;// "Reservation Successfully Cancelled";
    }

    private boolean reserveRoom(ArrayList<String> room, String name, int first, int last) {
        for (int i = first - 1; i <= last - 1; i++) {
            if (room.get(i) != null) {
                return false;
            }
        }
        for (int i = first - 1; i <= last - 1; i++) {
            room.set(i, name);
        }
        return true;
    }


}
