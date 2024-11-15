package az.edu.turing.ServiceTests;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.BookingNotFoundException;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private BookingDao bookingDao;
    private FlightDao flightDao;
    private BookingMapper mapper;


    @Test
    void testCreateBooking_Success() {
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flightDao.save(flight);

        CreateBookingRequest request = new CreateBookingRequest(4L, "Ron", "Weasley");

        BookingDto result = bookingService.createBooking(request);

        assertNotNull(result);
        assertEquals("John", result.getBookerName());
        assertEquals("Doe", result.getBookerSurname());
        assertNotNull(result.getFlight());
        assertEquals(1L, result.getFlight().getFlightId());
    }

    @Test
    void testCreateBooking_FlightNotFound() {
        CreateBookingRequest request = new CreateBookingRequest(1L, "Harry", "Potter");

        assertThrows(FlightNotFoundException.class, () -> bookingService.createBooking(request));
    }

    @Test
    void testCancelBooking_Success() {
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flightDao.save(flight);

        BookingEntity booking = new BookingEntity("Hermione", "Granger", flight);
        bookingDao.save(booking);

        boolean isCancelled = bookingService.cancelBooking(booking.getBookingId());

        assertTrue(isCancelled);
        assertEquals(101, flightDao.getById(1L).get().getAvailableSeats());
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(999L));
    }

    @Test
    void testGetAllBookings() {
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flightDao.save(flight);

        bookingDao.save(new BookingEntity("Severus", "Snape", flight));
        bookingDao.save(new BookingEntity("Minevra", "McGonagall", flight));

        List<BookingDto> bookings = bookingService.getAllBookings();

        assertEquals(2, bookings.size());
    }
}
