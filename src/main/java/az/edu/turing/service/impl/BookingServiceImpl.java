package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.entities.BookingEntity;
import az.edu.turing.entities.FlightEntity;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.service.BookingService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final FlightDao flightDao;

    public BookingServiceImpl(BookingDao bookingDao, FlightDao flightDao) {
        this.bookingDao = bookingDao;
        this.flightDao = flightDao;
    }

    private BookingDto toDto(BookingEntity booking) {
        return new BookingDto(
                booking.getBookingId(),
                booking.getFlight().getFlightId(),
                booking.getFirstName(),
                booking.getLastName(),
                booking.getFlight().getFrom(),
                booking.getFlight().getDestination(),
                booking.getFlight().getDepartureTime()
        );
    }

    @Override
    public BookingDto createBooking(long flightId, String firstName, String lastName) {
        Optional<FlightEntity> flight = flightDao.getById(flightId);
        if (!flight.isPresent()) {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }

        BookingEntity bookingEntity = new BookingEntity(0, flight.get(), firstName, lastName);
        bookingDao.save(bookingEntity);
        return toDto(bookingEntity);
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        Optional<BookingEntity> booking = bookingDao.getById(bookingId);
        return booking.map(b -> bookingDao.deleteById(bookingId)).orElse(false);
    }

    @Override
    public List<BookingDto> findAllBookingByPassenger(String fullName) {
        return bookingDao.getAll().stream()
                .filter(b -> (b.getFirstName() + " " + b.getLastName()).equalsIgnoreCase(fullName))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> getBookingDetails(long bookingId) {
        return bookingDao.getById(bookingId).map(this::toDto);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingDao.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
