package az.edu.turing.service;

import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.dto.request.CreateFlightRequest;

public interface FlightService {

    FlightDto create(CreateFlightRequest request);
}
