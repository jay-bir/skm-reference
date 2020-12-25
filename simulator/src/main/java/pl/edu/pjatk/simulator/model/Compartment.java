package pl.edu.pjatk.simulator.model;

import pl.edu.pjatk.simulator.service.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "compartments")
public class Compartment implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private Train train;

    public void setId(Long id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Transient
    private List<Person> occupants;

    public Compartment(Long id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        occupants = new ArrayList<>();
    }

    public Compartment(){}

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
    public Long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Collection<Person> getOccupants() {
        return occupants;
    }

    public void setOccupants(List<Person> occupants){
        this.occupants = occupants;
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
