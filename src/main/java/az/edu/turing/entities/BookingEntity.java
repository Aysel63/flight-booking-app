package az.edu.turing.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong atomicCounter = new AtomicLong(0);

    private final long bookingId;
    private FlightEntity flight;
    private String firstName;
    private String lastName;

    // BookingId ilə constructor
    public BookingEntity(long bookingId, FlightEntity flight, String firstName, String lastName) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public BookingEntity(FlightEntity flight, String firstName, String lastName) {
        this.bookingId = atomicCounter.incrementAndGet();
        this.flight = flight;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getBookingId() {
        return bookingId;
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
        return "BookingEntity{" +
                "bookingId=" + bookingId +
                ", flight=" + flight +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingId == that.bookingId &&
                Objects.equals(flight, that.flight) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, flight, firstName, lastName);
    }
}
