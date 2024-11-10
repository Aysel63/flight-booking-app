package az.edu.turing.service;

import az.edu.turing.model.dto.request.CreateFlightRequest;
import az.edu.turing.model.dto.response.CreateFlightResponse;

public interface FlightService {
CreateFlightResponse create(CreateFlightRequest request);
}
