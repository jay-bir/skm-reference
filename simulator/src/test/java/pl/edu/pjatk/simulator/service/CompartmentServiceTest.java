package pl.edu.pjatk.simulator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.repository.CompartmentRepository;
import pl.edu.pjatk.simulator.repository.TrainRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CompartmentServiceTest {

    private CompartmentService compartmentService;

    @Mock
    private CompartmentRepository compartmentRepository;

    @Mock
    private TrainRepository trainRepository;

    @BeforeEach
    public void prepare(){
        this.compartmentService = new CompartmentService(compartmentRepository,trainRepository);
    }

    @Test
    public void getAllNotAttachedWithAttachedCompartmentsShouldReturnEmptyList(){
        Compartment compartment = new Compartment();
        Train train = Mockito.mock(Train.class);
        compartment.setTrain(train);

        when(compartmentRepository.findAll()).thenReturn(List.of(compartment));
        List<Compartment> returnedCompartments = compartmentService.getAllNotAttached();

        Assertions.assertEquals(returnedCompartments.size(), 0);
    }

    @Test
    public void getAllNotAttachedWithNotAttachedCompartmentsShouldReturnNotEmptyList(){
        Compartment compartment = new Compartment();

        when(compartmentRepository.findAll()).thenReturn(List.of(compartment));
        List<Compartment> returnedCompartments = compartmentService.getAllNotAttached();

        Assertions.assertEquals(returnedCompartments.size(), 1);
        Assertions.assertEquals(returnedCompartments.get(0), compartment);
    }
}
