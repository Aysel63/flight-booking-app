package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.database.DatabaseConfig;
import az.edu.turing.domain.entities.FlightEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.edu.turing.domain.entities.database.DatabaseConfig.getConnection;

public class BookingDatabaseDao extends BookingDao {

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BookingEntity booking = mapRowToBookingEntity(resultSet);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        String query = "SELECT * FROM bookings WHERE booking_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BookingEntity booking = mapRowToBookingEntity(resultSet);
                    return Optional.of(booking);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public BookingEntity save(BookingEntity object) {
        String checkFlightQuery = "SELECT COUNT(*) FROM flights WHERE flight_id = ?";
        String insertBookingQuery = "INSERT INTO bookings (booking_id, booker_name, booker_surname, flight_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkFlightQuery)) {

            checkStatement.setLong(1, object.getFlight().getFlightId());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertBookingQuery)) {
                    insertStatement.setLong(1, object.getBookingId());
                    insertStatement.setString(2, object.getBookerName());
                    insertStatement.setString(3, object.getBookerSurname());
                    insertStatement.setLong(4, object.getFlight().getFlightId());

                    int rowsAffected = insertStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        return object;
                    }
                }
            } else {
                throw new SQLException("Flight with ID " + object.getFlight().getFlightId() + " does not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private BookingEntity mapRowToBookingEntity(ResultSet resultSet) throws SQLException {
        String bookerName = resultSet.getString("booker_name");
        String bookerSurname = resultSet.getString("booker_surname");

        FlightEntity flight = new FlightEntity();

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookerName(bookerName);
        bookingEntity.setBookerSurname(bookerSurname);
        bookingEntity.setFlight(flight);

        return bookingEntity;
    }
}
