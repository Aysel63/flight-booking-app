package az.edu.turing.controller;

import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.impl.BookingServiceImpl;

import java.util.List;
import java.util.Optional;

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookingDto createBooking(CreateBookingRequest createBookingRequest) {
        return bookingService.createBooking(createBookingRequest);
    }

    public boolean cancelBooking(long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    public List<BookingDto> findAllBookingsByPassenger(String fullName) {
        return bookingService.findAllBookingByPassenger(fullName);
    }

    public Optional<BookingDto> getBookingDetails(long bookingId) {
        return bookingService.getBookingDetails(bookingId);
    }

    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
