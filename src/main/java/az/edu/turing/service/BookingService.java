package az.edu.turing.service;

import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;

public interface BookingService {

    BookingDto create(CreateBookingRequest request);
}
