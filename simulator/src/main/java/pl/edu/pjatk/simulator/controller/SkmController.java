package pl.edu.pjatk.simulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.service.SkmService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skm")
public class SkmController {

    @Autowired
    SkmService skmService;

    @GetMapping()
    @RequestMapping("/go")
    public ResponseEntity<List<Map<String, Object>>> moveTime() {
        try {
            skmService.moveTimeForward();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
