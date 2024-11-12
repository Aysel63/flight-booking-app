package az.edu.turing.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.entities.BookingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BookingInMemoryDao extends BookingDao {
    private static final List<BookingEntity>BOOKINGS = new ArrayList<>();

    @Override
    public Collection<BookingEntity> getAll() {
        return BOOKINGS;
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        return BOOKINGS.stream()
                .filter(booking ->booking.getBookingId().equals(id))
                .findFirst();
    }

    @Override
    public BookingEntity save(BookingEntity object) {
        BOOKINGS.add(object);
        return object;
    }

    @Override
    public boolean deleteById(Long id) {
        return BOOKINGS.removeIf(booking ->booking.getBookingId().equals(id));
    }
}
