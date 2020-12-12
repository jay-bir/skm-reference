package pl.edu.pjatk.simulator.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "skm")
public class SkmService {
    TrainService trainService;

    public SkmService(TrainService trainService) {
        this.trainService = trainService;
    }

    public void moveTimeForward() {
        trainService.moveTimeForward();
    }
}
