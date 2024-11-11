package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.entities.FlightEntity;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.FlightService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private FlightDao flightDao;

    public FlightServiceImpl() {
        this.flightDao = flightDao;
    }



    @Override
    public FlightDto getFlightById(long flightId) {
        Optional<FlightEntity> flightEntity = flightDao.getById(flightId);
        if (!flightEntity.isPresent()) {
            return null;
        }

        FlightEntity flight = flightEntity.get();
        return new FlightDto(
                flight.getFlightId(),
                flight.getFrom(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getAvailableSeats()
        );
    }

    @Override
    public List<FlightDto> getAllFlights() {
        List<FlightEntity> flights = (List<FlightEntity>) flightDao.getAll();
        return flights.stream()
                .map(flight -> new FlightDto(
                        flight.getFlightId(),
                        flight.getFrom(),
                        flight.getDestination(),
                        flight.getDepartureTime(),
                        flight.getAvailableSeats()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDto> findFlights(String destination ) {
        List<FlightEntity> flights = flightDao.getAll().stream()
                .filter(f -> f.getDestination().equals(destination) && f.getFrom().equals(f))
                .collect(Collectors.toList());

        return flights.stream()
                .map(flight -> new FlightDto(
                        flight.getFlightId(),
                        flight.getFrom(),
                        flight.getDestination(),
                        flight.getDepartureTime(),
                        flight.getAvailableSeats()
                ))
                .collect(Collectors.toList());
    }

    }


