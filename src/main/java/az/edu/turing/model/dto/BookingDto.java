package az.edu.turing.model.dto;

import az.edu.turing.domain.entities.FlightEntity;

import java.io.Serializable;

public class BookingDto implements Serializable {

    private final long bookingId;
    private final String bookerName;
    private final String bookerSurName;
    private final FlightEntity flight;

    public BookingDto(long bookingId, String bookerName, String bookerSurName, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurName = bookerSurName;
        this.flight = flight;
    }

    public long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public String getFullName() {
        return bookerName + " " + bookerSurName;
    }

    public String getBookerName() {
        return bookerName;
    }

    public String getBookerSurName() {
        return bookerSurName;
    }
}
