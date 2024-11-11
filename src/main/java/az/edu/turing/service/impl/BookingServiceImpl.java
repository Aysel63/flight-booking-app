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

    private BookingDao bookingDao;
    private FlightDao flightDao;

    public BookingServiceImpl() {
        this.bookingDao = bookingDao;
        this.flightDao = flightDao;
    }

    @Override
    public BookingDto createBooking(long flightId, String firstName, String lastName) {
        Optional<FlightEntity> flight = flightDao.getById(flightId);
        if (!flight.isPresent()) {
            return null; // və ya xüsusi istisna
        }

        BookingEntity bookingEntity = new BookingEntity(0, flight.get(), firstName, lastName);
        bookingDao.save(bookingEntity);

        return new BookingDto(
                bookingEntity.getBookingId(),
                flightId,
                firstName,
                lastName,
                flight.get().getFrom(),
                flight.get().getDestination(),
                flight.get().getDepartureTime()
        );
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        Optional<BookingEntity> booking = bookingDao.getById(bookingId);
        if (!booking.isPresent()) {
            return false;
        }
        return bookingDao.deleteById(bookingId);
    }

    @Override
    public List<BookingDto> findBookingByPassenger(String fullName) {
        List<BookingEntity> bookings = bookingDao.getAll().stream()
                .filter(b -> (b.getFirstName() + " " + b.getLastName()).equals(fullName))
                .collect(Collectors.toList());

        return bookings.stream()
                .map(booking -> new BookingDto(
                        booking.getBookingId(),
                        booking.getFlight().getFlightId(),
                        booking.getFirstName(),
                        booking.getLastName(),
                        booking.getFlight().getFrom(),
                        booking.getFlight().getDestination(),
                        booking.getFlight().getDepartureTime()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> getBookingDetails(long bookingId) {
        Optional<BookingEntity> booking = bookingDao.getById(bookingId);
        return booking.map(b -> new BookingDto(
                b.getBookingId(),
                b.getFlight().getFlightId(),
                b.getFirstName(),
                b.getLastName(),
                b.getFlight().getFrom(),
                b.getFlight().getDestination(),
                b.getFlight().getDepartureTime()
        ));
    }

    @Override
    public List<BookingDto> getAllBookings() {
        List<BookingEntity> bookings = (List<BookingEntity>) bookingDao.getAll();
        return bookings.stream()
                .map(booking -> new BookingDto(
                        booking.getBookingId(),
                        booking.getFlight().getFlightId(),
                        booking.getFirstName(),
                        booking.getLastName(),
                        booking.getFlight().getFrom(),
                        booking.getFlight().getDestination(),
                        booking.getFlight().getDepartureTime()
                ))
                .collect(Collectors.toList());
    }
}
