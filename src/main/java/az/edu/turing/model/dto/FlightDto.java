package az.edu.turing.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDto implements Serializable {
    private long flightId;
    private String destination;
    private String from;
    private String departureTime;
    private int availableSeats;

    // Constructor, getters, setters
    public FlightDto(long flightId, String destination, String from, String departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public FlightDto(long flightId, String from, String destination, LocalDateTime departureTime, int availableSeats) {

    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;

    }  }