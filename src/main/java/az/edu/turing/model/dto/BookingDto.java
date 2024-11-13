package az.edu.turing.model.dto;

import az.edu.turing.domain.entities.FlightEntity;

import java.io.Serializable;

public class BookingDto implements Serializable {

    private final long bookingId;
    private final String bookerName;
    private final String bookerSurname;
    private final FlightEntity flight;

    public BookingDto(long bookingId, String bookerName, String bookerSurname, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
        this.flight = flight;
    }

    public long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public String getFullName() {
        return bookerName + " " + bookerSurname;
    }

    public String getBookerName() {
        return bookerName;
    }

    public String getBookerSurname() {
        return bookerSurname;
    }


    @Override
    public String toString() {
        return "Booking ID=%d, Booker's Name='%s', Booker's Surname='%s', Flight=%d}"
                .formatted(bookingId, bookerName, bookerSurname, flight.getFlightId());
    }
}