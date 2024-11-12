package az.edu.turing.service;

import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.dto.request.CreateFlightRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightService {

    Optional<FlightDto> getFlightById(long flightId);

    List<FlightDto> getAllFlights();

    List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople);
}
