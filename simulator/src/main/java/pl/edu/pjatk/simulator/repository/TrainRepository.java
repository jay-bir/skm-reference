package pl.edu.pjatk.simulator.repository;

import org.springframework.stereotype.Component;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Station;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.service.TrainServiceConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TrainRepository {
    private final Collection<Train> trainRepository;

    public TrainRepository(TrainServiceConfiguration config) {
        Random random = new Random();

        trainRepository = IntStream.rangeClosed(1, config.getNumberOfTrains())
                .mapToObj(trainId -> {
                    List<Compartment> compartments = IntStream.rangeClosed(1, config.getNumberOfCompartments())
                            .mapToObj(compartmentId -> new Compartment(compartmentId, config.getCompartmentCapacity()))
                            .collect(Collectors.toList());

                    Station[] stations = Station.values();
                    int lastStationOrdinalBound = stations[stations.length - 1].ordinal();

                    return new Train(trainId, compartments, stations[random.nextInt(lastStationOrdinalBound)], random.nextBoolean());
                }).collect(Collectors.toList());
    }

    public Collection<Train> getAllTrains() {
        return trainRepository;
    }
}
