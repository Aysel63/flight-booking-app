package az.edu.turing.util;

import az.edu.turing.controller.BookingController;
import az.edu.turing.controller.FlightController;
import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.dao.impl.BookingFileDao;
import az.edu.turing.domain.dao.impl.BookingInMemoryDao;
import az.edu.turing.domain.dao.impl.FlightFileDao;
import az.edu.turing.domain.dao.impl.FlightInMemoryDao;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.FlightService;
import az.edu.turing.service.impl.BookingServiceImpl;
import az.edu.turing.service.impl.FlightServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUtil {

    private final FlightDao flightDao =
//            new FlightInMemoryDao();
            new FlightFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    private final FlightMapper flightMapper = new FlightMapper();
    private final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
    private final FlightController flightController = new FlightController(flightService);

    private final BookingDao bookingDao =
//            new BookingInMemoryDao();
            new BookingFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    private final BookingMapper bookingMapper = new BookingMapper();
    private final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao, bookingMapper);
    private final BookingController bookingController = new BookingController(bookingService);

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean canLoop = true;
        while (canLoop) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> displayAllFlightsWithing24Hours();
                    case 2 -> {
                        System.out.print("Please enter flight ID: ");
                        long flightId = Long.parseLong(scanner.nextLine().trim());
                        System.out.println(flightController.getFlightById(flightId));
                    }
                    case 3 -> {
                        System.out.print("Please enter destination: ");
                        String destination = scanner.nextLine().trim();

                        System.out.print("Please enter date\n");
                        System.out.print("Day (1-31): ");
                        int day = Integer.parseInt(scanner.nextLine().trim());
                        if (day < 1 || day > 31) {
                            throw new IllegalArgumentException("Day must be between 1 and 31");
                        }

                        System.out.print("Month (1-12): ");
                        int month = Integer.parseInt(scanner.nextLine().trim());
                        if (month < 1 || month > 12) {
                            throw new IllegalArgumentException("Month must be between 1 and 12.");
                        }

                        System.out.print("Year (YYYY): ");
                        int year = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Please enter number of people (how many tickets): ");
                        int ticketCount = Integer.parseInt(scanner.nextLine().trim());

                        List<FlightDto> flights =
                                flightController.findFlights(destination, LocalDate.of(year, month, day), ticketCount);
                        if (flights.isEmpty()) {
                            throw new NotFoundException("Flights with specified conditions couldn't be found.");
                        }
                        flights.forEach(System.out::println);
                        displayMenuOfFlightSearch();
                        int choiceOfFlightSearchMenu = Integer.parseInt(scanner.nextLine().trim());
                        switch (choiceOfFlightSearchMenu) {
                            case 1 -> {
                                System.out.print("Please enter flight ID: ");
                                long flightId = Long.parseLong(scanner.nextLine().trim());

                                for (int i = 1; i <= ticketCount; i++) {
                                    System.out.printf("Please enter passenger %d name: ", i);
                                    String passengerName = scanner.nextLine().trim();
                                    System.out.printf("Please enter passenger %d surname: ", i);
                                    String passengerSurname = scanner.nextLine().trim();
                                    if (!isValidNameAndSurname(passengerName, passengerSurname)) {
                                        System.out.println("Name and surname must only contain alphabetic characters." +
                                                " Please try again.");
                                        i--;
                                        continue;
                                    }

                                    bookingController.createBooking(
                                            new CreateBookingRequest(flightId, passengerName, passengerSurname)
                                    );
                                }
                            }
                            case 0 -> System.out.println("Returning back to main menu...");
                        }
                    }
                    case 4 -> {
                        System.out.print("Please enter booking ID: ");
                        long bookingId = Long.parseLong(scanner.nextLine().trim());
                        bookingController.cancelBooking(bookingId);
                    }
                    case 5 -> {
                        System.out.print("Please enter your full name(name surname): ");
                        String fullname = scanner.nextLine().trim();
                        List<BookingDto> allBookingsByPassenger = bookingController.findAllBookingsByPassenger(fullname);
                        if (allBookingsByPassenger.isEmpty()) {
                            System.err.println(fullname + " hasn't booked a flight yet.");
                        } else {
                            allBookingsByPassenger.forEach(System.out::println);
                        }
                    }
                    case 6 -> {
                        System.out.println("Exiting....");
                        if ((flightDao instanceof FlightFileDao) && (bookingDao instanceof BookingFileDao)) {
                            ((FlightFileDao) flightDao).saveOnShutdown();
                            ((BookingFileDao) bookingDao).saveOnShutdown();
                        }
                        canLoop = false;
                    }
                    default -> System.out.println("Wrong choice. Try Again");
                }

            } catch (NotFoundException | IllegalArgumentException e) {
                System.err.println(e.getMessage() + "\nrolling back to menu.");
            } catch (Exception e) {
                System.out.println("Something went wrong. " + e.getMessage() + "\nrolling back to menu.");
            }
        }
    }

    public void displayMenu() {
        System.out.print("""
                \nMake your choice:
                1. Online-Board
                2. Show flight information
                3. Search and book a flight
                4. Cancel the booking
                5. My Flights
                6. Exit
                """);
    }

    public void displayMenuOfFlightSearch() {
        System.out.print("""
                Make your choice:
                0. Return back to main menu
                1. Select one of the flights
                """);
    }

    public void displayAllFlightsWithing24Hours() {
        List<FlightDto> flightsWithin24Hours = flightController.getAllFlights().stream()
                .filter(this::checkIfFlightIsWithin24Hours)
                .collect(Collectors.toList());


        if (flightsWithin24Hours.isEmpty()) {
            System.err.println("No flights within 24 hours found");
        } else {
            flightsWithin24Hours.forEach(System.out::println);
        }
    }

    public boolean checkIfFlightIsWithin24Hours(FlightDto flight) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime flightDateTime = flight.getDepartureTime();

        if (currentTime.getYear() != flightDateTime.getYear()) {
            return false;
        }

        if (currentTime.getMonthValue() != flightDateTime.getMonthValue()) {
            return false;
        }

        long hoursDifference = Math.abs(ChronoUnit.HOURS.between(currentTime, flightDateTime));

        if (hoursDifference > 24 || hoursDifference < 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidNameAndSurname(String name, String surname) {
        return name.matches("[a-zA-Z]+") && surname.matches("[a-zA-Z]+");
    }
}
