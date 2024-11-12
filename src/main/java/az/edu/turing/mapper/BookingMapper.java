package az.edu.turing.mapper;

import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.model.dto.BookingDto;

public class BookingMapper implements EntityMapper<BookingEntity, BookingDto>{
    @Override
    public BookingEntity toEntity(BookingDto bookingDto) {
        return new BookingEntity(
                bookingDto.getBookingId(),
                bookingDto.getFirstName(),
                bookingDto.getLastName(),
                bookingDto.getFlight()
        );
    }

    @Override
    public BookingDto toDto(BookingEntity bookingEntity) {
        return new BookingDto(
                bookingEntity.getBookingId(),
                bookingEntity.getFirstName(),
                bookingEntity.getLastName(),
                bookingEntity.getFlight()
        );
    }
}
