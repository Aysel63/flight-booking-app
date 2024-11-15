package az.edu.turing.ServiceTests;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class FlightServiceImplTest {

    private FlightServiceImpl flightService;
    private FlightDao flightDao;
    private FlightMapper mapper;

    @Test
    void testGetFlightById_Success() {
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flightDao.save(flight);

        FlightDto flightDto = flightService.getFlightById(1L);

        assertNotNull(flightDto);
        assertEquals("NYC", flightDto.getFrom());
        assertEquals("LA", flightDto.getDestination());
    }

    @Test
    void testGetFlightById_NotFound() {
        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightById(999L));
    }

    @Test
    void testGetAllFlights() {
        flightDao.save(new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100));
        flightDao.save(new FlightEntity("Kiev", "LA", LocalDateTime.of(2024, 12, 25, 10, 30), 150));

        List<FlightDto> flights = flightService.getAllFlights();

        assertEquals(2, flights.size());
    }

    @Test
    void testFindFlights() {
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flight.setDepartureTime(LocalDateTime.of(2024, 12, 25, 10, 30));
        flightDao.save(flight);

        List<FlightDto> results = flightService.findFlights("LA", LocalDate.of(2024, 12, 25), 1);

        assertEquals(1, results.size());
        assertEquals("LA", results.get(0).getDestination());
    }
}
