package az.edu.turing.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDto implements Serializable {
    private final long flightId;
    private final String destination;
    private final String from;
    private final LocalDateTime departureTime;
    private final int availableSeats;


    public FlightDto(long flightId, String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getDestination() {
        return destination;
    }

    public String getFrom() {
        return from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    @Override
    public String toString() {
        return "Flight ID=%d, Destination='%s', From='%s', Departure Time=%s, Available Seats=%d"
                .formatted(flightId, destination, from, departureTime, availableSeats);
    }
}