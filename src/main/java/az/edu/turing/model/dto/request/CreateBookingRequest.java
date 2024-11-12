package az.edu.turing.model.dto.request;

public class CreateBookingRequest {
    private long flightId;
    private String firstName;
    private String lastName;

    public CreateBookingRequest(long flightId, String firstName, String lastName) {
        this.flightId = flightId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
