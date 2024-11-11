package az.edu.turing.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BookingEntity implements Serializable {
    private String bookingId;
    private FlightEntity flight;
    private List<PassengerEntity> passengers;

    public BookingEntity(String bookingId, FlightEntity flight, List<PassengerEntity> passengers) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passengers = passengers;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public FlightEntity getFlight() { return flight; }
    public void setFlight(FlightEntity flight) { this.flight = flight; }

    public List<PassengerEntity> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerEntity> passengers) { this.passengers = passengers; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", flight=" + flight +
                ", passengers=" + passengers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity booking = (BookingEntity) o;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}
