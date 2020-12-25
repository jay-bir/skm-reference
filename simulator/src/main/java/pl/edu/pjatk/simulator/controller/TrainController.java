package pl.edu.pjatk.simulator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.service.TrainService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trains")
public class TrainController {
    private TrainService trainService;

    public TrainController(TrainService service){
        this.trainService = service;
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getAllTrains() {
        try {
            List<Train> all = trainService.getAll();
            List<Map<String, Object>> payload = all.stream()
                    .map(obj -> transformToTrainDTO().apply(obj))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTrainById(@PathVariable("id") Long id) {
        try {
            Train obj = trainService.getById(id).orElseThrow(() -> new NoSuchElementException("Train doesn't exist"));
            Map<String, Object> payload = transformToTrainDTO().apply(obj);

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<Void> add(@RequestBody Train train) {
        try {
            trainService.create(train);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody Train train){
        try {
            trainService.update(train);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        try {
            trainService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/attach")
    public ResponseEntity<Void> attach(
            @RequestParam(name = "trainId") Long trainId,
            @RequestParam(name = "compartments") List<Long> compartments){
        try{
            trainService.attach(trainId, compartments);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/detach")
    public ResponseEntity<Void> detach(
            @RequestParam(name = "trainId") Long trainId,
            @RequestParam(name = "compartments") List<Long> compartments){
        try{
            trainService.detach(trainId, compartments);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Function<Train, Map<String, Object>> transformToTrainDTO() {
        return train -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", train.getId());
            payload.put("currentStation", train.getCurrentStation());
            payload.put("goingToGdansk", train.getGoingToGdansk());
            payload.put("currentPauseTime", train.getCurrentPauseTime());
            payload.put("compartmentIds", train.getCompartments().stream().map(Compartment::getId));

            return payload;
        };
    }

}
