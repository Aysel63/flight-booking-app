package az.edu.turing.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BookingDto implements Serializable {
    private final long bookingId;
    private final long flightId;
    private final String firstName;
    private final String lastName;
    private final String from;
    private final String destination;
    private final LocalDateTime departureTime;

    public BookingDto(long bookingId, long flightId, String firstName, String lastName, String from, String destination, LocalDateTime departureTime) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.from = from;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public long getBookingId() {
        return bookingId;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFrom() {
        return from;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
}
