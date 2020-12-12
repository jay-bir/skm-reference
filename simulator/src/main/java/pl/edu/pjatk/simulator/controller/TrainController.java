package pl.edu.pjatk.simulator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.service.TrainService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getAllTrains() {
        try {
            Collection<Train> all = trainService.getAll();
            List<Map<String, Object>> payload = all.stream()
                    .map(obj -> transformToTrainDTO().apply(obj))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTrainById(@PathVariable("id") Long id) {
        try {
            Train obj = trainService.getById(id);
            Map<String, Object> payload = transformToTrainDTO().apply(obj);

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{trainId}/compartments")
    public ResponseEntity<List<Map<String, Object>>> getAllCompartmentsForTrain(@PathVariable("trainId") Long trainId) {
        try {
            Collection<Compartment> all = trainService.getById(trainId).getCompartments();
            List<Map<String, Object>> payload = all.stream()
                    .map(obj -> transformToCompartmentDTO().apply(obj))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{trainId}/compartments/{id}")
    public ResponseEntity<Map<String, Object>> getCompartmentById(@PathVariable("trainId") Long trainId,
                                                                  @PathVariable("id") Long id) {
        try {
            Compartment compartment = trainService.getById(trainId).getCompartments().stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);
            Map<String, Object> payload = transformToCompartmentDTO().apply(compartment);

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Function<Compartment, Map<String, Object>> transformToCompartmentDTO() {
        return compartment -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", compartment.getId());
            payload.put("capacity", compartment.getCapacity());
            payload.put("spaceUsed", compartment.getOccupants().size());
            payload.put("occupants", compartment.getOccupants());

            return payload;
        };
    }

    private Function<Train, Map<String, Object>> transformToTrainDTO() {
        return train -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", train.getId());
            payload.put("currentStation", train.getCurrentStation());
            payload.put("goingToGdansk", train.isGoingToGdansk());
            payload.put("currentPauseTime", train.getCurrentPauseTime());
            payload.put("compartmentIds", train.getCompartments().stream().map(Compartment::getId));

            return payload;
        };
    }
}
