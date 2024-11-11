package az.edu.turing.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BookingDto implements Serializable {
    private long bookingId;
    private long flightId;
    private String firstName;
    private String lastName;

    // Constructor, getters, setters
    public BookingDto(long flightId, String firstName, String lastName) {
        this.flightId = flightId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public BookingDto(long bookingId, long flightId, String firstName, String lastName, String from, String destination, LocalDateTime departureTime) {

    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
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
}
