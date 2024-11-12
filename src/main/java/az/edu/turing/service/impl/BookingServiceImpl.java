package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.BookingNotFoundException;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
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
    public BookingDto createBooking(CreateBookingRequest createBookingRequest) {
        FlightEntity flight = flightDao.getById(createBookingRequest.getFlightId())
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + createBookingRequest.getFlightId()));
        {
        }

        BookingEntity bookingEntity = new BookingEntity(flight, createBookingRequest.getFirstName(), createBookingRequest.getLastName());
        bookingDao.save(bookingEntity);
        return toDto(bookingEntity);
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        BookingEntity bookingEntity = bookingDao.getById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));
        return bookingDao.deleteById(bookingId);
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
