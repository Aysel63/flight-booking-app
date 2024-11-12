package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.BookingService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final FlightDao flightDao;
    private final BookingMapper mapper;

    public BookingServiceImpl(BookingDao bookingDao, FlightDao flightDao, BookingMapper mapper) {
        this.bookingDao = bookingDao;
        this.flightDao = flightDao;
        this.mapper = mapper;
    }

    @Override
    public BookingDto createBooking(CreateBookingRequest request) {
        FlightEntity flight = flightDao.getById(request.getFlightId()).get();

        BookingEntity savedBooking = bookingDao.save(
                new BookingEntity(request.getFirstName(), request.getLastName(), flight)
        );

        flightDao.updateAvailableSeats(flight.getFlightId(), flight.getAvailableSeats() - 1);

        return mapper.toDto(savedBooking);
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        BookingEntity bookingEntity = bookingDao.getById(bookingId).get();
        FlightEntity flight = bookingEntity.getFlight();

        flightDao.updateAvailableSeats(flight.getFlightId(), flight.getAvailableSeats() + 1);

        return bookingDao.deleteById(bookingId);
    }

    @Override
    public List<BookingDto> findAllBookingByPassenger(String fullName) {
        return bookingDao.getAll().stream()
                .filter(b -> b.getFullName().equalsIgnoreCase(fullName))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> getBookingDetails(long bookingId) {
        return bookingDao.getById(bookingId).map(mapper::toDto);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingDao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
