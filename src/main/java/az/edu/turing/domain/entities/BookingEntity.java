package az.edu.turing.domain.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong atomicCounter = new AtomicLong(0);

    private final long bookingId;
    private String firstName;
    private String lastName;
    private FlightEntity flight;


    public BookingEntity(long bookingId, String firstName, String lastName, FlightEntity flight) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.flight = flight;
    }

    public BookingEntity(String firstName, String lastName, FlightEntity flight) {
        this.bookingId = atomicCounter.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
        this.flight = flight;
    }

    public Long getBookingId() {
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

    public String getFullName() {
        return firstName + " " + lastName;
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
