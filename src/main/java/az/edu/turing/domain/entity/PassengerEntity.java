package az.edu.turing.domain.entity;

import java.io.Serializable;
import java.util.Objects;

public class PassengerEntity implements Serializable {
    private String firstName;
    private String lastName;

    public PassengerEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerEntity passenger = (PassengerEntity) o;
        return Objects.equals(firstName, passenger.firstName) &&
                Objects.equals(lastName, passenger.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
