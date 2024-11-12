package az.edu.turing.model.dto.request;

public class CreateBookingRequest {
    private long flightId;
    private String bookerName;
    private String bookerSurName;

    public CreateBookingRequest(long flightId, String bookerName, String bookerSurName) {
        this.flightId = flightId;
        this.bookerName = bookerName;
        this.bookerSurName = bookerSurName;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName=bookerName;
    }

    public String getBookerSurName() {
        return bookerSurName;
    }

    public void setBookerSurName(String bookerSurName) {
        this.bookerSurName = bookerSurName;
    }
}
