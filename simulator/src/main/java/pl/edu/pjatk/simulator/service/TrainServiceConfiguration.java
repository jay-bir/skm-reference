package pl.edu.pjatk.simulator.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "skm")
public class TrainServiceConfiguration {
    private int numberOfTrains;
    private int numberOfCompartments;
    private int compartmentCapacity;

    public int getNumberOfTrains() {
        return numberOfTrains;
    }

    public void setNumberOfTrains(int numberOfTrains) {
        this.numberOfTrains = numberOfTrains;
    }

    public int getNumberOfCompartments() {
        return numberOfCompartments;
    }

    public void setNumberOfCompartments(int numberOfCompartments) {
        this.numberOfCompartments = numberOfCompartments;
    }

    public int getCompartmentCapacity() {
        return compartmentCapacity;
    }

    public void setCompartmentCapacity(int compartmentCapacity) {
        this.compartmentCapacity = compartmentCapacity;
    }
}
