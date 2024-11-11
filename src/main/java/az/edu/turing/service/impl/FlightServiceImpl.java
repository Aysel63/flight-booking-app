package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.dto.request.CreateFlightRequest;
import az.edu.turing.service.FlightService;

public class FlightServiceImpl implements FlightService {

    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    @Override
    public FlightDto create(CreateFlightRequest request) {
        return null;
    }
}
