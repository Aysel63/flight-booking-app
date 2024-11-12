package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.FlightService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    private FlightDto toDto(FlightEntity flight) {
        return new FlightDto(
                flight.getFlightId(),
                flight.getDestination(),
                flight.getFrom(),
                flight.getDepartureTime(),
                flight.getAvailableSeats()
        );
    }

    @Override
    public FlightDto getFlightById(long flightId) {
        return flightDao.getById(flightId)
                .map(this::toDto)
                .orElseThrow(()-> new FlightNotFoundException("Flight not found with ID "+ flightId));
    }

    @Override
    public List<FlightDto> getAllFlights() {
        return flightDao.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople) {
        return flightDao.getAll().stream()
                .filter(f -> destination == null || f.getDestination().equals(destination))
                .filter(f -> date == null || f.getDepartureTime().toLocalDate().equals(date))
                .filter(f -> numberOfPeople <= 0 || f.getAvailableSeats() >= numberOfPeople)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}

