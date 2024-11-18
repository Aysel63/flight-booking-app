package az.edu.turing.domain.dao.impl.file;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.exception.BookingNotFoundException;
import az.edu.turing.exception.FileOperationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BookingFileDao extends BookingDao {

    private static final String BOOKINGS_RESOURCE_PATH = System.getenv("BOOKINGS_RESOURCE_PATH");
    private static final String FILE_EXTENSION = System.getenv("BOOKINGS_FILE_EXTENSION");

    private final ObjectMapper objectMapper;

    public BookingFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFolderExists();
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        final Path directoryPath = Paths.get(BOOKINGS_RESOURCE_PATH);
        try (Stream<Path> stream = Files.list(directoryPath)) {
            stream.filter(file -> file.toString().endsWith(FILE_EXTENSION))
                    .forEach(file -> bookings.add(readBookingFromFile(file)));
        } catch (IOException e) {
            throw new FileOperationException("Error reading directory or files in BookingFileDao.getAll()");
        }
        return bookings;
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        final Path filePath = Paths.get(BOOKINGS_RESOURCE_PATH, id + FILE_EXTENSION);

        if (Files.exists(filePath)) {
            return Optional.of(readBookingFromFile(filePath));
        }
        return Optional.empty();
    }

    @Override
    public BookingEntity save(BookingEntity bookingEntity) {
        final Path filePath = Paths.get(BOOKINGS_RESOURCE_PATH, bookingEntity.getBookingId() + FILE_EXTENSION);
        try {
            Files.write(filePath, objectMapper.writeValueAsBytes(bookingEntity));
        } catch (IOException e) {
            System.err.println("Error saving booking with ID: " + bookingEntity.getBookingId());
            e.printStackTrace();
        }
        return bookingEntity;
    }

    @Override
    public boolean deleteById(Long id) {
        final Path filePath = Paths.get(BOOKINGS_RESOURCE_PATH, id + FILE_EXTENSION);
        if (!Files.exists(filePath)) {
            throw new BookingNotFoundException("Couldn't find booking with id: " + id);
        }
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileOperationException("Error deleting file " + filePath + " in BookingFileDao.deleteById()");
        }
    }

    private void ensureFolderExists() {
        if (BOOKINGS_RESOURCE_PATH == null || BOOKINGS_RESOURCE_PATH.isBlank()) {
            throw new IllegalArgumentException("BOOKINGS_RESOURCE_PATH environment variable is not set or is empty.");
        }

        Path folderPath = Paths.get(BOOKINGS_RESOURCE_PATH);

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                throw new FileOperationException(
                        "Error creating folder in ensureFolderExists method of BookingFileDao");
            }
        }
    }

    private BookingEntity readBookingFromFile(Path filePath) {
        try {
            return objectMapper.readValue(filePath.toFile(), BookingEntity.class);
        } catch (IOException e) {
            throw new FileOperationException("Error reading file " + filePath + " in BookingFileDao");
        }
    }
}
