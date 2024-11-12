package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;

import java.util.*;

public class FlightInMemoryDao extends FlightDao {
    private static final Map<Long, FlightEntity> FLIGHTS = new HashMap<>();

    @Override
    public Collection<FlightEntity> getAll() {
        return List.copyOf(FLIGHTS.values());
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        return Optional.ofNullable(FLIGHTS.get(id));
    }

    @Override
    public FlightEntity save( final FlightEntity object) {
        return FLIGHTS.put(object.getFlightId(),object);
    }

    @Override
    public boolean deleteById(Long id) {
        return FLIGHTS.remove(id)!=null;
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        var flightEntity=FLIGHTS.get(flightId);
        if(flightEntity!=null){
            flightEntity.setAvailableSeats(newAvailableSeatCount);
        }
        return flightEntity;
    }
}
