package az.edu.turing.entities;

import java.io.Serializable;
import java.util.Objects;

public class BookingEntity implements Serializable {

    private int bookingId;
    private FlightEntity flight;
    private String firstName;
    private String lastName;

    public BookingEntity(int bookingId, FlightEntity flight, String firstName, String lastName) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", flight=" + flight +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity booking = (BookingEntity) o;
        return Objects.equals(bookingId, booking.bookingId) &&
                Objects.equals(flight, booking.flight) &&
                Objects.equals(firstName, booking.firstName) &&
                Objects.equals(lastName, booking.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, flight, firstName, lastName);
    }
}
