package pl.edu.pjatk.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.simulator.model.Compartment;

public interface CompartmentRepository extends JpaRepository<Compartment, Long> {
}
