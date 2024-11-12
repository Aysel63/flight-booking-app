package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;

import java.util.*;

public class BookingInMemoryDao extends BookingDao {
    private static final Map<Long, BookingEntity> BOOKINGS = new HashMap<>();

    @Override
    public Collection<BookingEntity> getAll() {
        return List.copyOf(BOOKINGS.values());
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        return Optional.ofNullable(BOOKINGS.get(id));

    }

    @Override
    public BookingEntity save(final BookingEntity object) {
        return BOOKINGS.put(object.getBookingId(), object);
    }

    @Override
    public boolean deleteById(Long id) {
        return BOOKINGS.remove(id) != null;
    }
}
