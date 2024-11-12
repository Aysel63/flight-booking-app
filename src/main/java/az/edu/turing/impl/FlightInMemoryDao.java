package az.edu.turing.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.entities.FlightEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FlightInMemoryDao extends FlightDao {
    private static final List<FlightEntity> FLIGHTS = new ArrayList<>();

    @Override
    public Collection<FlightEntity> getAll() {
        return FLIGHTS;
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
        return object;
    }

    @Override
    public boolean deleteById(Long id) {
        return FLIGHTS.removeIf(flights -> flights.getFlightId().equals(id));
    }
}
