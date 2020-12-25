package pl.edu.pjatk.simulator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.service.CompartmentService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compartments")
public class CompartmentController {
    private CompartmentService compartmentService;

    public CompartmentController(CompartmentService service) {
        this.compartmentService = service;
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getAll(){
        try{
            List<Compartment> all = compartmentService.getAll();
            List<Map<String, Object>> payload = all.stream()
                    .map(obj -> transformToCompartmentDTO().apply(obj))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(payload, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable("id") Long id){
        try{
            Compartment obj = compartmentService.getById(id).orElseThrow(() -> new NoSuchElementException("Compartment doesn't exist"));
            Map<String, Object> payload = transformToCompartmentDTO().apply(obj);
            return new ResponseEntity<>(payload,HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<Void> create(
            @RequestBody Compartment compartment,
            @RequestParam(required = false, name = "trainId") Long trainId){
        try{
            if(trainId != null){
                compartmentService.create(compartment, trainId);
            }
            else{
                compartmentService.create(compartment);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody Compartment compartment){
        try{
            compartmentService.update(compartment);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        try{
            compartmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//its better for it to ride than have passengers i think
    private Function<Compartment, Map<String, Object>> transformToCompartmentDTO() {
        return compartment -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", compartment.getId());
            payload.put("capacity", compartment.getCapacity());
//            payload.put("spaceUsed", compartment.getOccupants().size());
//            payload.put("occupants", compartment.getOccupants());
            payload.put("train", compartment.getTrain().getId());

            return payload;
        };
    }
}
