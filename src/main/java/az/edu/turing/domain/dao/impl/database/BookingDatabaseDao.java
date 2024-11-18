package az.edu.turing.domain.dao.impl.database;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDatabaseDao extends BookingDao {

    private final Connection connection;

    public BookingDatabaseDao(Connection connection) {
        this.connection = connection;
        createBBookingTableIfNotExists();
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, b.booker_name, b.booker_surname, " +
                "f.flight_id, f.destination, f.from_location, f.departure_time, f.available_seats " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id";

        try (PreparedStatement statement = connection.prepareStatement(query);
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
        String query = "SELECT b.booking_id, b.booker_name, b.booker_surname, " +
                "f.flight_id, f.destination, f.from_location, f.departure_time, f.available_seats " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "WHERE b.booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
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
    public BookingEntity save(BookingEntity bookingEntity) {
        String checkFlightQuery = "SELECT COUNT(*) FROM flights WHERE flight_id = ?";
        String insertBookingQuery = "INSERT INTO bookings (booker_name, booker_surname, flight_id) VALUES (?, ?, ?)";

        try {
            // Transaksiyanı başlat
            connection.setAutoCommit(false); // Transaksiyanı başlat

            // Savepoint təyin et
            Savepoint savepoint = connection.setSavepoint("Savepoint1");

            try (PreparedStatement checkStatement = connection.prepareStatement(checkFlightQuery)) {
                checkStatement.setLong(1, bookingEntity.getFlight().getFlightId());
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Booking əlavə et
                    BookingEntity savedBooking = insertBooking(bookingEntity, insertBookingQuery);

                    if (savedBooking != null) {
                        // Əgər əməliyyat uğurludursa, commit et
                        connection.commit();
                        return savedBooking;
                    }
                } else {
                    // Flight mövcud deyil, səhv qaytar
                    throw new SQLException("Flight with ID " + bookingEntity.getFlight().getFlightId() + " does not exist.");
                }
            } catch (SQLException e) {
                // Əgər səhv baş verərsə, savepoint-a geri dön
                connection.rollback(savepoint);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Transaksiyanı sıfırla
            try {
                connection.setAutoCommit(true); // Auto commit rejimini yenidən aç
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    private BookingEntity insertBooking(BookingEntity bookingEntity, String insertBookingQuery) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(insertBookingQuery)) {
            insertStatement.setString(1, bookingEntity.getBookerName());
            insertStatement.setString(2, bookingEntity.getBookerSurname());
            insertStatement.setLong(3, bookingEntity.getFlight().getFlightId());

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                return bookingEntity;
            }
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM bookings WHERE booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
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

        FlightEntity flight = new FlightEntity(
                resultSet.getLong("flight_id"),
                resultSet.getString("destination"),
                resultSet.getString("from_location"),
                resultSet.getTimestamp("departure_time") != null
                        ? resultSet.getTimestamp("departure_time").toLocalDateTime()
                        : null,
                resultSet.getInt("available_seats")
        );

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookingId(resultSet.getLong("booking_id"));
        bookingEntity.setBookerName(bookerName);
        bookingEntity.setBookerSurname(bookerSurname);
        bookingEntity.setFlight(flight);

        return bookingEntity;
    }

    private boolean createBBookingTableIfNotExists() {
        boolean result = false;
        String query = """
                CREATE TABLE IF NOT EXISTS bookings (
                    booking_id SERIAL PRIMARY KEY,
                    booker_name VARCHAR(100) NOT NULL,
                    booker_surname VARCHAR(100) NOT NULL,
                    flight_id INTEGER NOT NULL,
                    CONSTRAINT bookings_flight_id_fkey FOREIGN KEY (flight_id)
                        REFERENCES flights(flight_id) ON DELETE CASCADE
                );
                """;

        try (Statement statement = connection.createStatement()) {
            result = statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
