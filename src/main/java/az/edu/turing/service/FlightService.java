package az.edu.turing.service;

import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.dto.request.CreateFlightRequest;

import java.util.List;

public interface FlightService {

    FlightDto getFlightById(long flightId);
    List<FlightDto> getAllFlights();
    List<FlightDto> findFlights(String destination);
}
