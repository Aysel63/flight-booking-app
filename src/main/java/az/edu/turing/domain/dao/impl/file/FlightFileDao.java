package az.edu.turing.domain.dao.impl.file;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlightFileDao extends FlightDao {

    private static final String FLIGHTS_RESOURCE_PATH = System.getenv("FLIGHTS_RESOURCE_PATH");
    private static final String FILE_EXTENSION = System.getenv("FLIGHTS_FILE_EXTENSION");
    private final ObjectMapper objectMapper;

    public FlightFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFolderExists();
        createMockFlight();
    }

    @Override
    public List<FlightEntity> getAll() {
        final Path directoryPath = Paths.get(FLIGHTS_RESOURCE_PATH);
        try (Stream<Path> filesStream = Files.walk(directoryPath)) {
            return filesStream
                    .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                    .map(this::readFlightFromFile)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading files in FlightFileDao.getAll() method");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<FlightEntity> getAllFlightsWithin24Hours() {
        final Path directoryPath = Paths.get(FLIGHTS_RESOURCE_PATH);
        try (Stream<Path> filesStream = Files.walk(directoryPath)) {
            return filesStream
                    .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                    .map(this::readFlightFromFile).filter(Objects::nonNull)
                    .filter(this::isFlightWithin24Hours)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading files in FlightFileDao.getAllFlightsWithin24Hours() method");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        final Path filePath = Paths.get(FLIGHTS_RESOURCE_PATH, id + FILE_EXTENSION);
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        FlightEntity flight = readFlightFromFile(filePath);
        return Optional.ofNullable(flight);
    }

    @Override
    public FlightEntity save(FlightEntity flightEntity) {
        try {
            final Path filePath = Paths.get(FLIGHTS_RESOURCE_PATH, flightEntity.getFlightId() + FILE_EXTENSION);
            Files.write(filePath, objectMapper.writeValueAsBytes(flightEntity));
        } catch (IOException e) {
            System.err.println("Error saving flight in FlightFileDao.save() method");
            e.printStackTrace();
        }
        return flightEntity;
    }

    @Override
    public boolean deleteById(Long id) {
        final Path filePath = Paths.get(FLIGHTS_RESOURCE_PATH, id + FILE_EXTENSION);
        if (!Files.exists(filePath)) {
            throw new FlightNotFoundException("Couldn't find flight with id: " + id);
        }

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file in FlightFileDao.deleteById() method");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        FlightEntity flightById = getById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        flightById.setAvailableSeats(newAvailableSeatCount);
        return save(flightById);
    }

    private void ensureFolderExists() {
        if (FLIGHTS_RESOURCE_PATH == null || FLIGHTS_RESOURCE_PATH.isBlank()) {
            throw new IllegalArgumentException("FLIGHTS_RESOURCE_PATH environment variable is not set or is empty.");
        }

        final Path folderPath = Paths.get(FLIGHTS_RESOURCE_PATH);

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                System.err.println("Error creating folder in ensureFolderExists method of FlightFileDao");
                e.printStackTrace();
            }
        }
    }

    public boolean isFlightWithin24Hours(FlightEntity flight) {
        final LocalDateTime currentTime = LocalDateTime.now();
        final LocalDateTime flightDateTime = flight.getDepartureTime();

        long hoursDifference = Math.abs(ChronoUnit.HOURS.between(currentTime, flightDateTime));
        return (hoursDifference >= 0 && hoursDifference <= 24);
    }

    private FlightEntity readFlightFromFile(Path filePath) {
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            return objectMapper.readValue(bytes, FlightEntity.class);
        } catch (IOException e) {
            System.err.println("Error reading file in FlightFileDao.readFlightFromFile() method");
            e.printStackTrace();
        }
        return null;
    }

    public void createMockFlight() {
        FlightEntity mockFlight = new FlightEntity(
                1L,
                "New York",
                "Kiev",
                LocalDateTime.now().plusHours(4),
                150
        );
        try {
            save(mockFlight);
        } catch (RuntimeException e) {
            System.err.println("Error creating mock flight in createMockFlight() method");
            e.printStackTrace();
        }
    }
}
