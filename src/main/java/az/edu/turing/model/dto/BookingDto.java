package az.edu.turing.model.dto;

import az.edu.turing.domain.entities.FlightEntity;

import java.io.Serializable;

public class BookingDto implements Serializable {

    private final long bookingId;
    private final String firstName;
    private final String lastName;
    private final FlightEntity flight;

    public BookingDto(long bookingId, String firstName, String lastName, FlightEntity flight) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.flight = flight;
    }

    public long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
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
}
