package pl.edu.pjatk.simulator.model;

public class Person {
    private final String firstName;
    private final String lastName;
    private final Station destination;

    public Person(String firstName, String lastName, Station destination) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.destination = destination;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Station getDestination() {
        return destination;
    }
}
