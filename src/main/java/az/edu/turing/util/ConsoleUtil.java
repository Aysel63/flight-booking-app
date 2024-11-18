package az.edu.turing.util;

import az.edu.turing.controller.BookingController;
import az.edu.turing.controller.FlightController;
import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.dao.impl.file.BookingFileDao;
import az.edu.turing.domain.dao.impl.file.FlightFileDao;
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
import java.util.List;
import java.util.Scanner;

public class ConsoleUtil {

    private final FlightDao flightDao =
//            new FlightInMemoryDao();
            new FlightFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    //            new FlightDatabaseDao();
    private final FlightMapper flightMapper = new FlightMapper();
    private final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
    private final FlightController flightController = new FlightController(flightService);

    private final BookingDao bookingDao =
//            new BookingInMemoryDao();
            new BookingFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    //            new BookingDatabaseDao();
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
                canLoop = handleMenuChoice(choice);
            } catch (NotFoundException | IllegalArgumentException e) {
                System.err.println(e.getMessage() + "\nrolling back to menu.");
            } catch (Exception e) {
                System.out.println("Something went wrong. " + e.getMessage() + "\nrolling back to menu.");
            }
        }
    }

    private boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> flightController.getAllFlightsWithin24Hours();
            case 2 -> handleFlightById();
            case 3 -> handleFlightSearchAndBooking();
            case 4 -> handleBookingCancellation();
            case 5 -> handlePassengerBookings();
            case 6 -> {
                System.out.println("Exiting....");
                return false;
            }
            default -> System.out.println("Wrong choice. Try Again");
        }
        return true;
    }

    private void handleFlightById() {
        System.out.print("Please enter flight ID: ");
        long flightId = Long.parseLong(scanner.nextLine().trim());
        System.out.println(flightController.getFlightById(flightId));
    }

    private void handleFlightSearchAndBooking() {
        String destination = getDestinationFromUser();
        LocalDate date = getDateFromUser();
        int ticketCount = getTicketCountFromUser();

        List<FlightDto> flights = flightController.findFlights(destination, date, ticketCount);
        if (flights.isEmpty()) {
            throw new NotFoundException("Flights with specified conditions couldn't be found.");
        }

        flights.forEach(System.out::println);
        displayMenuOfFlightSearch();

        int choiceOfFlightSearchMenu = Integer.parseInt(scanner.nextLine().trim());
        if (choiceOfFlightSearchMenu == 1) {
            handleFlightBooking(ticketCount);
        } else {
            System.out.println("Returning back to main menu...");
        }
    }

    private String getDestinationFromUser() {
        System.out.print("Please enter destination: ");
        return scanner.nextLine().trim();
    }

    private LocalDate getDateFromUser() {
        System.out.print("Please enter date\n");
        System.out.print("Day (1-31): ");
        int day = Integer.parseInt(scanner.nextLine().trim());
        validateDay(day);

        System.out.print("Month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine().trim());
        validateMonth(month);

        System.out.print("Year (YYYY): ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        return LocalDate.of(year, month, day);
    }

    private void validateDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
    }

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }
    }

    private int getTicketCountFromUser() {
        System.out.print("Please enter number of people (how many tickets): ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private void handleFlightBooking(int ticketCount) {
        System.out.print("Please enter flight ID: ");
        long flightId = Long.parseLong(scanner.nextLine().trim());

        for (int i = 1; i <= ticketCount; i++) {
            String passengerName = getPassengerName(i);
            String passengerSurname = getPassengerSurname(i);
            if (!isValidNameAndSurname(passengerName, passengerSurname)) {
                System.out.println("Name and surname must only contain alphabetic characters." +
                                   " Please try again.");
                i--;
                continue;
            }
            bookingController.createBooking(new CreateBookingRequest(flightId, passengerName, passengerSurname));
        }
    }

    private String getPassengerName(int i) {
        System.out.printf("Please enter passenger %d name: ", i);
        return scanner.nextLine().trim();
    }

    private String getPassengerSurname(int i) {
        System.out.printf("Please enter passenger %d surname: ", i);
        return scanner.nextLine().trim();
    }

    private void handleBookingCancellation() {
        System.out.print("Please enter booking ID: ");
        long bookingId = Long.parseLong(scanner.nextLine().trim());
        bookingController.cancelBooking(bookingId);
    }

    private void handlePassengerBookings() {
        System.out.print("Please enter your full name(name surname): ");
        String fullname = scanner.nextLine().trim();
        List<BookingDto> allBookingsByPassenger = bookingController.findAllBookingsByPassenger(fullname);
        if (allBookingsByPassenger.isEmpty()) {
            System.err.println(fullname + " hasn't booked a flight yet.");
        } else {
            allBookingsByPassenger.forEach(System.out::println);
        }
    }


    public void displayMenu() {
        System.out.print("""
                Make your choice:
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

    private boolean isValidNameAndSurname(String name, String surname) {
        return name.matches("[a-zA-Z]+") && surname.matches("[a-zA-Z]+");
    }
}
