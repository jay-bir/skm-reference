package pl.edu.pjatk.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.simulator.model.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
}
