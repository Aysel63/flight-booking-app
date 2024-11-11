package az.edu.turing.controller;

import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.FlightService;
import az.edu.turing.service.impl.FlightServiceImpl;

import java.util.List;

public class FlightController {
    private FlightService flightService = new FlightServiceImpl();

    public FlightDto getFlightById(long flightId) {
        return flightService.getFlightById(flightId);
    }

    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    public List<FlightDto> findFlights(String destination) {
        return flightService.findFlights(destination);
    }
}
