package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BookingFileDao extends BookingDao {

    private static final String RESOURCE_PATH = "src/main/java/az/edu/turing/resource";
    private static final String BOOKINGS_FILE_PATH = RESOURCE_PATH.concat("/bookings.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, BookingEntity> bookings = new HashMap<>();

    public BookingFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadOnStartup();
    }

    private void loadOnStartup() {
        Path path = Paths.get(BOOKINGS_FILE_PATH);
        if (Files.notExists(path)) {
            System.out.println("No existing bookings file found.");
            return;
        }
        try {
            byte[] fileData = Files.readAllBytes(path);
            List<BookingEntity> loadedBookings = objectMapper.readValue(fileData, new TypeReference<>() {
            });
            loadedBookings.forEach(booking -> bookings.put(booking.getBookingId(), booking));
        } catch (IOException e) {
            System.err.println("Error loading bookings from file");
            e.printStackTrace();
        }
    }

    public void saveOnShutdown() {
        try {
            List<BookingEntity> bookingList = new ArrayList<>(bookings.values());
            byte[] data = objectMapper.writeValueAsBytes(bookingList);
            Files.write(Paths.get(BOOKINGS_FILE_PATH), data);
        } catch (IOException e) {
            System.err.println("Error saving bookings to file");
            e.printStackTrace();
        }
    }

    @Override
    public Collection<BookingEntity> getAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public BookingEntity save(BookingEntity bookingEntity) {
        bookings.put(bookingEntity.getBookingId(), bookingEntity);
        return bookingEntity;
    }

    @Override
    public boolean deleteById(Long id) {
        return bookings.remove(id) != null;
    }
}
