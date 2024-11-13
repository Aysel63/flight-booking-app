package az.edu.turing.domain.dao.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.domain.entities.database.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import static az.edu.turing.domain.entities.database.DatabaseConfig.getConnection;

public class FlightDatabaseDao extends FlightDao {

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FlightEntity flight = mapRowToFlightEntity(resultSet);
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    FlightEntity flight = mapRowToFlightEntity(resultSet);
                    return Optional.of(flight);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public FlightEntity save(FlightEntity object) {
        String insertFlightQuery = "INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertFlightQuery)) {

            statement.setLong(1, object.getFlightId());
            statement.setString(2, object.getDestination());
            statement.setString(3, object.getFrom());
            statement.setTimestamp(4, Timestamp.valueOf(object.getDepartureTime()));
            statement.setInt(5, object.getAvailableSeats());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return object;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM flights WHERE flight_id = ?";

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

    private FlightEntity mapRowToFlightEntity(ResultSet resultSet) throws SQLException {
        long flightId = resultSet.getLong("flight_id");
        String destination = resultSet.getString("destination");
        String fromLocation = resultSet.getString("from_location");
        LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
        int availableSeats = resultSet.getInt("available_seats");

        return new FlightEntity(flightId, destination, fromLocation, departureTime, availableSeats);
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        String updateSeatsQuery = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSeatsQuery)) {

            statement.setInt(1, newAvailableSeatCount);
            statement.setLong(2, flightId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return getById(flightId).orElse(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
