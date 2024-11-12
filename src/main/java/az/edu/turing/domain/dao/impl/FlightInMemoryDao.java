package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FlightInMemoryDao extends FlightDao {
    private static final List<FlightEntity> FLIGHTS = new ArrayList<>();

    @Override
    public Collection<FlightEntity> getAll() {
        return List.copyOf(FLIGHTS);
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        return FLIGHTS.stream()
                .filter(flights -> flights.getFlightId().equals(id))
                .findFirst();
    }

    @Override
    public FlightEntity save(FlightEntity object) {
        FLIGHTS.add(object);
        return FLIGHTS.get(FLIGHTS.size() - 1); //Avoided using .getLast() method for compatibility matters.
    }

    @Override
    public boolean deleteById(Long id) {
        return FLIGHTS.removeIf(flights -> flights.getFlightId().equals(id));
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        FlightEntity updatedFlight = null;
        for (FlightEntity flight : FLIGHTS) {
            if (flight.getFlightId().equals(flightId)) {
                flight.setAvailableSeats(newAvailableSeatCount);
                updatedFlight = flight;
                break;
            }
        }

        return updatedFlight;
    }
}
