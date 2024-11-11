package az.edu.turing.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class FlightEntity implements Serializable {

    private int flightId;
    private String destination;
    private String from = "Kiev";
    private LocalDateTime departureTime;
    private int availableSeats;

    public FlightEntity(int flightId, String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }


    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", destination='" + destination + '\'' +
                ", from='" + from + '\'' +
                ", departureTime=" + departureTime +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightEntity flight = (FlightEntity) o;
        return Objects.equals(flightId, flight.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }
}
