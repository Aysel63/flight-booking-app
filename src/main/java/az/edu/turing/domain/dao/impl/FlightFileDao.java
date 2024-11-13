package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FlightFileDao extends FlightDao {

    private static final String RESOURCE_PATH = "src/main/java/az/edu/turing/resource";
    private static final String FLIGHTS_FILE_PATH = RESOURCE_PATH.concat("/flights.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, FlightEntity> flights = new HashMap<>();

    public FlightFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadOnStartup();
    }

    private void loadOnStartup() {
        Path path = Paths.get(FLIGHTS_FILE_PATH);
        if (Files.notExists(path)) {
            System.out.println("No existing flights file found.");
            return;
        }
        try {
            byte[] fileData = Files.readAllBytes(path);
            List<FlightEntity> loadedFlights = objectMapper.readValue(fileData, new TypeReference<>() {
            });
            loadedFlights.forEach(flight -> flights.put(flight.getFlightId(), flight));
        } catch (IOException e) {
            System.err.println("Error loading flights from file");
            e.printStackTrace();
        }
    }

    public void saveOnShutdown() {
        try {
            List<FlightEntity> flightList = new ArrayList<>(flights.values());
            byte[] data = objectMapper.writeValueAsBytes(flightList);
            Files.write(Paths.get(FLIGHTS_FILE_PATH), data);
        } catch (IOException e) {
            System.err.println("Error saving flights to file");
            e.printStackTrace();
        }
    }

    @Override
    public Collection<FlightEntity> getAll() {
        return new ArrayList<>(flights.values());
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        return Optional.ofNullable(flights.get(id));
    }

    @Override
    public FlightEntity save(FlightEntity flightEntity) {
        flights.put(flightEntity.getFlightId(), flightEntity);
        return flightEntity;
    }

    @Override
    public boolean deleteById(Long id) {
        return flights.remove(id) != null;
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        return Optional.ofNullable(flights.get(flightId))
                .map(flight -> {
                    flight.setAvailableSeats(newAvailableSeatCount);
                    return flight;
                })
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
    }
}
