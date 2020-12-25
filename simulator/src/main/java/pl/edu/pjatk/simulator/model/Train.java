package pl.edu.pjatk.simulator.model;

import pl.edu.pjatk.simulator.service.Identifiable;
import pl.edu.pjatk.simulator.util.PersonGenerator;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "trains")
public class Train implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @OneToMany(mappedBy = "train")
    private  List<Compartment> compartments;

    @Column(name = "current_station")
    private Station currentStation;

    @Column(name = "going_to_gdansk")
    private boolean goingToGdansk;

    @Column(name = "current_pause_time")
    private int currentPauseTime;

    public Train(Long id, List<Compartment> compartments, Station currentStation, boolean goingToGdansk) {
        this.id = id;
        this.compartments = compartments;
        this.currentStation = currentStation;
        this.goingToGdansk = goingToGdansk;
        this.currentPauseTime = 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public void setGoingToGdansk(boolean goingToGdansk) {
        this.goingToGdansk = goingToGdansk;
    }

    public void setCurrentPauseTime(int currentPauseTime) {
        this.currentPauseTime = currentPauseTime;
    }

    public Train(){}

    public List<Compartment> getCompartments() {
        return compartments;
    }

    public void setCompartments(List<Compartment> compartments) {
        this.compartments = compartments;
    }

    public void addCompartment(Compartment compartment){
        this.compartments.add(compartment);
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public boolean getGoingToGdansk() {
        return goingToGdansk;
    }

    public int getCurrentPauseTime() {
        return currentPauseTime;
    }

    public void move() {
        if (currentPauseTime > 0) {
            currentPauseTime--;
        } else {
            int nextStationModifier = goingToGdansk ? 1 : -1;
            currentStation = Station.values()[currentStation.ordinal() + nextStationModifier];
            currentPauseTime = currentStation.getPauseTime();

            if (currentStation.getPauseTime() > 0) {
                goingToGdansk = !goingToGdansk;
            }

            //For now cannot be done with passengers as transient
//            compartments.forEach(c -> c.disembark(this.currentStation));
//            compartments.forEach(c -> {
//                List<Person> people = PersonGenerator.generatePeople(this.currentStation);
//                people.forEach(c::embark);
//            });
        }
    }

    @Override
    public Long getId() {
        return id;
    }
}
