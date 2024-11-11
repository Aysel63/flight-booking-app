package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.BookingService;

public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;

    public BookingServiceImpl(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    @Override
    public BookingDto create(CreateBookingRequest request) {
        return null;
    }
}
