package pl.edu.pjatk.simulator.service;

import org.springframework.stereotype.Service;

import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.repository.CompartmentRepository;
import pl.edu.pjatk.simulator.repository.TrainRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private TrainRepository trainRepository;
    private CompartmentService compartmentService;
    private CompartmentRepository compartmentRepository;

    public TrainService(TrainRepository repository, CompartmentService compartmentService, CompartmentRepository compartmentRepository) {
        this.trainRepository = repository;
        this.compartmentService = compartmentService;
        this.compartmentRepository = compartmentRepository;
    }

    public void moveTimeForward() {
        trainRepository.findAll().forEach(t ->{
            t.move();
            trainRepository.save(t);
        });
    }

    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    public Optional<Train> getById(Long id) {
        return trainRepository.findById(id);
    }

    public void create(Train train){
        if(train.getId() != null) throw new IllegalArgumentException("Id must be null");
        train.setCompartments(new ArrayList<>());
        trainRepository.save(train);
    }

    public void delete(Long id){
        if(trainRepository.findById(id).isEmpty()) throw new NoSuchElementException("Train doesn't exist");
        trainRepository.deleteById(id);
    }

    public void update(Train train){
        if(train.getId() != null) {
            Train toBeupdated = trainRepository.findById(train.getId()).orElseThrow(()-> new NoSuchElementException("Train doesn't exist"));
            toBeupdated.setCurrentStation(train.getCurrentStation());
            toBeupdated.setGoingToGdansk(train.getGoingToGdansk());
            toBeupdated.setCurrentPauseTime(train.getCurrentPauseTime());
            trainRepository.save(toBeupdated);
        }
        else{
            throw new IllegalArgumentException("Id can't be null");
        }
    }
    public void attach(Long trainId, List<Long> compartments){
        Train train = trainRepository.findById(trainId).orElseThrow(() -> new NoSuchElementException("Train doesn't exist"));
        compartmentService.getAllNotAttached().stream()
                .filter(c -> compartments.contains(c.getId()))
                .forEach(c -> {
                    if(c.getTrain() != null) throw new IllegalArgumentException("Compartment already attached");
                    train.addCompartment(c);
                    c.setTrain(train);
                    compartmentRepository.save(c);
                });
        trainRepository.save(train);
    }

    public void detach(Long trainId, List<Long> compartments){
        Train train = trainRepository.findById(trainId).orElseThrow(() -> new NoSuchElementException("Train doesn't exist"));
        train.getCompartments().stream()
                .filter(c -> compartments.contains(c.getId()))
                .forEach(c -> {
                    c.setTrain(null);
                    compartmentRepository.save(c);
                });
        List<Compartment> leftCompartments = train.getCompartments().stream()
                .filter(c -> c.getTrain() == train)
                .collect(Collectors.toList());
        train.setCompartments(leftCompartments);
        trainRepository.save(train);
    }
}
