package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FlightFileDao extends FlightDao {

    private static final String FLIGHTS_RESOURCE_PATH = System.getenv("FLIGHTS_RESOURCE_PATH");
    private final ObjectMapper objectMapper;
    private final Map<Long, FlightEntity> flightCache = new ConcurrentHashMap<>();

    public FlightFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFolderExists();
        loadFlightsIntoCache();
    }

    @Override
    public List<FlightEntity> getAll() {
        return new ArrayList<>(flightCache.values());
    }

    @Override
    public List<FlightEntity> getFlightsWithin24Hours(LocalDateTime now, LocalDateTime twentyFourHoursLater) {
        return flightCache.values().stream()
                .filter(flight -> isFlightWithinTimeRange(flight.getDepartureTime(), now, twentyFourHoursLater))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        return Optional.ofNullable(flightCache.get(id));
    }

    @Override
    public FlightEntity save(FlightEntity flightEntity) {
        File file = new File(FLIGHTS_RESOURCE_PATH, flightEntity.getFlightId() + ".json");
        try {
            objectMapper.writeValue(file, flightEntity);
            flightCache.put(flightEntity.getFlightId(), flightEntity);
        } catch (IOException e) {
            handleIOException(e, "Error saving flight data");
        }
        return flightEntity;
    }

    @Override
    public boolean deleteById(Long id) {
        File file = getFlightFileById(id);
        if (!file.exists()) {
            throw new FlightNotFoundException("Couldn't find flight with id: " + id);
        }
        boolean deleted = file.delete();
        if (deleted) {
            flightCache.remove(id);
        }
        return deleted;
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        FlightEntity flightById = flightCache.get(flightId);
        if (flightById == null) {
            throw new FlightNotFoundException("Flight not found");
        }
        flightById.setAvailableSeats(newAvailableSeatCount);
        return save(flightById);
    }

    private void ensureFolderExists() {
        if (FLIGHTS_RESOURCE_PATH == null || FLIGHTS_RESOURCE_PATH.isBlank()) {
            throw new IllegalArgumentException("FLIGHTS_RESOURCE_PATH environment variable is not set or is empty.");
        }

        Path folderPath = Paths.get(FLIGHTS_RESOURCE_PATH);
        if (Files.notExists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                handleIOException(e, "Error creating directory in ensureFolderExists method");
            }
        }
    }

    private void loadFlightsIntoCache() {
        File[] files = getFlightFiles();
        if (files != null) {
            for (File file : files) {
                Optional<FlightEntity> flight = readFlightFromFile(file);
                flight.ifPresent(f -> flightCache.put(f.getFlightId(), f));
            }
        }
    }

    private Optional<FlightEntity> readFlightFromFile(File file) {
        try {
            return Optional.of(objectMapper.readValue(file, FlightEntity.class));
        } catch (IOException e) {
            handleIOException(e, "Error reading file: " + file.getName());
        }
        return Optional.empty();
    }

    private File[] getFlightFiles() {
        File directory = new File(FLIGHTS_RESOURCE_PATH);
        return directory.listFiles((dir, name) -> name.endsWith(".json"));
    }

    private File getFlightFileById(Long id) {
        return new File(FLIGHTS_RESOURCE_PATH, id + ".json");
    }

    private boolean isFlightWithinTimeRange(LocalDateTime flightDepartureTime, LocalDateTime now, LocalDateTime twentyFourHoursLater) {
        return !flightDepartureTime.isBefore(now) && !flightDepartureTime.isAfter(twentyFourHoursLater);
    }

    private void handleIOException(IOException e, String message) {
        System.err.println(message);
        e.printStackTrace();
        throw new RuntimeException(message, e);
    }
}