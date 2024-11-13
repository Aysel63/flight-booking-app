package az.edu.turing;

import az.edu.turing.domain.dao.impl.BookingDatabaseDao;
import az.edu.turing.domain.dao.impl.FlightDatabaseDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;

import java.time.LocalDateTime;

public class BookingApp {

    public static void main(String[] args) {


        // Flight test

        FlightDatabaseDao flightDatabaseDao = new FlightDatabaseDao();
        FlightEntity flight = new FlightEntity(1L, "New York", "London", LocalDateTime.now(), 150);
        System.out.println(flightDatabaseDao.save(flight));
        System.out.println(flightDatabaseDao.getById(1L));
        System.out.println(flightDatabaseDao.getAll());
        System.out.println(flightDatabaseDao.deleteById(1L));


        // Booking test

        BookingDatabaseDao bookingDatabaseDao = new BookingDatabaseDao();
        BookingEntity booking = new BookingEntity(1L, "John", "Doe", flight);
        System.out.println(bookingDatabaseDao.save(booking));
        System.out.println(bookingDatabaseDao.getById(1L));
        System.out.println(bookingDatabaseDao.deleteById(1L));
    }
}
