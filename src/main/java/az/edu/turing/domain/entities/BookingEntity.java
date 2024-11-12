package az.edu.turing.domain.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong atomicCounter = new AtomicLong(0);

    private final long bookingId;
    private String bookerName;
    private String bookerSurName;
    private FlightEntity flight;


    public BookingEntity(long bookingId, String bookerName, String bookerSurName, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurName = bookerSurName;
        this.flight = flight;
    }

    public BookingEntity(String bookerName, String bookerSurName, FlightEntity flight) {
        this.bookingId = atomicCounter.incrementAndGet();
        this.bookerName = bookerName;
        this.bookerSurName = bookerSurName;
        this.flight = flight;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String firstName) {
        this.bookerName = bookerName;
    }

    public String getBookerSurName() {
        return bookerSurName;
    }

    public void setBookerSurName(String bookerSurName) {
        this.bookerSurName = bookerSurName;
    }

    public String getFullName() {
        return bookerName + " " + bookerSurName;
    }

    @Override
    public String toString() {
        return "BookingEntity{" +
                "bookingId=" + bookingId +
                ", flight=" + flight +
                ", bookerName='" + bookerName + '\'' +
                ", bookerSurName='" + bookerSurName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingId == that.bookingId &&
                Objects.equals(flight, that.flight) &&
                Objects.equals(bookerName, that.bookerName) &&
                Objects.equals(bookerSurName, that.bookerSurName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, flight, bookerName, bookerSurName);
    }
}
