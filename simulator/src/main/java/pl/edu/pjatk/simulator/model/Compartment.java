package pl.edu.pjatk.simulator.model;

import pl.edu.pjatk.simulator.service.Identifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Compartment implements Identifiable {
    private final int id;
    private final int capacity;
    private final List<Person> occupants;

    public Compartment(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        occupants = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Collection<Person> getOccupants() {
        return occupants;
    }

    public void embark(Person person) {
        if (occupants.size() < capacity) {
            occupants.add(person);
        }
    }

    public void disembark(Station station) {
        List<Person> leaving = occupants.stream()
                .filter(p -> p.getDestination().equals(station))
                .collect(Collectors.toList());

        occupants.removeAll(leaving);
    }

}
