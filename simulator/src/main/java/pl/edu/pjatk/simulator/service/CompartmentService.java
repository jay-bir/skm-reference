package pl.edu.pjatk.simulator.service;

import org.springframework.stereotype.Service;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.repository.CompartmentRepository;
import pl.edu.pjatk.simulator.repository.TrainRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompartmentService {
    private CompartmentRepository compartmentRepository;
    private TrainRepository trainRepository;

    public CompartmentService(CompartmentRepository repository, TrainRepository trainRepository){
        this.compartmentRepository = repository;
        this.trainRepository = trainRepository;
    }

    public List<Compartment> getAll() {
        return compartmentRepository.findAll();
    }

    public Optional<Compartment> getById(Long id) {
        return compartmentRepository.findById(id);
    }

    public void create(Compartment compartment){
        if(compartment.getId() != null) throw new IllegalArgumentException("Id must be null");
        compartment.setTrain(null);
        compartmentRepository.save(compartment);
    }
    public void create(Compartment compartment, Long trainId){
        if(compartment.getId() != null) throw new IllegalArgumentException("Id must be null");
        Train train = trainRepository.findById(trainId).orElseThrow(() -> new NoSuchElementException("Train doesn't exist"));

        compartment.setTrain(train);
        compartmentRepository.save(compartment);

        train.addCompartment(compartment);
        trainRepository.save(train);
    }

    public void delete(Long id){
        if(compartmentRepository.findById(id).isEmpty()) throw new NoSuchElementException("Compartment doesn't exist");
        compartmentRepository.deleteById(id);
    }

    public void update(Compartment compartment){
        if(compartment.getId() != null) {
            Compartment toBeupdated = compartmentRepository.findById(compartment.getId()).orElseThrow(()-> new NoSuchElementException("Compartment doesn't exist"));
            toBeupdated.setCapacity(compartment.getCapacity());
            compartmentRepository.save(toBeupdated);
        }
        else{
            throw new IllegalArgumentException("Id can't be null");
        }
    }

    public List<Compartment> getAllNotAttached(){
        return compartmentRepository.findAll().stream()
                .filter(c -> c.getTrain() == null)
                .collect(Collectors.toList());
    }

}
