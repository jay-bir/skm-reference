package pl.edu.pjatk.simulator.model;

import pl.edu.pjatk.simulator.service.Identifiable;
import pl.edu.pjatk.simulator.util.PersonGenerator;

import java.util.Collection;
import java.util.List;

public class Train implements Identifiable {
    private final int id;
    private final List<Compartment> compartments;
    private Station currentStation;
    private boolean goingToGdansk;
    private int currentPauseTime;

    public Train(int id, List<Compartment> compartments, Station currentStation, boolean goingToGdansk) {
        this.id = id;
        this.compartments = compartments;
        this.currentStation = currentStation;
        this.goingToGdansk = goingToGdansk;
        this.currentPauseTime = 0;
    }

    public Collection<Compartment> getCompartments() {
        return compartments;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public boolean isGoingToGdansk() {
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

            compartments.forEach(c -> c.disembark(this.currentStation));
            compartments.forEach(c -> {
                List<Person> people = PersonGenerator.generatePeople(this.currentStation);
                people.forEach(c::embark);
            });
        }
    }

    @Override
    public long getId() {
        return id;
    }
}
