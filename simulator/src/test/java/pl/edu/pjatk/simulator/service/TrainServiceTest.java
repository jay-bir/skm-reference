package pl.edu.pjatk.simulator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.edu.pjatk.simulator.model.Compartment;
import pl.edu.pjatk.simulator.model.Station;
import pl.edu.pjatk.simulator.model.Train;
import pl.edu.pjatk.simulator.repository.CompartmentRepository;
import pl.edu.pjatk.simulator.repository.TrainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
public class TrainServiceTest {

    private TrainService trainService;

    @MockBean
    private TrainRepository trainRepository;

    @Mock
    private CompartmentService compartmentService;

    @Mock
    private CompartmentRepository compartmentRepository;

    @Mock
    private List<Compartment> compartments;

    @BeforeEach
    public void prepare(){
        this.trainService = new TrainService(trainRepository,compartmentService,compartmentRepository);
    }

    @Test
    public void moveTimeForwardTest(){
        Train train_1 = Mockito.mock(Train.class);
        Train train_2 = Mockito.mock(Train.class);
        List<Train> trains = List.of(train_1,train_2);

        when(trainRepository.findAll()).thenReturn(trains);
        trainService.moveTimeForward();

        verify(train_1).move();
        verify(train_2).move();
        verify(trainRepository).save(train_1);
        verify(trainRepository).save(train_2);
    }

    @Test
    public void createWithNotNullIdShouldThrowException(){
        Train train = new Train();
        train.setId(1L);
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            trainService.create(train);
        },"Id must be null");
    }

    @Test
    public void createWithIdNullShouldSaveToRepository(){
        Train train = spy(Train.class);

        trainService.create(train);

        verify(train).setCompartments(new ArrayList<>());
        verify(trainRepository).save(train);
        Assertions.assertNull(train.getId());
    }

    @Test
    public void updateWithNullIdShouldThrowAnException(){
        Train train = new Train();
        Assertions.assertThrows(IllegalArgumentException.class, () -> trainService.update(train),"Id can't be null");
    }

    @Test
    public void updateShouldSetNewValuesToTheSavedTrain(){
        Train passedTrain = new Train(1L,new ArrayList<>(),Station.GDANSK_STOCZNIA,false);
        Train savedTrain = new Train(1L,compartments,Station.GDYNIA_GLOWNA, true);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(savedTrain));

        trainService.update(passedTrain);

        verify(trainRepository).save(savedTrain);
        verifyNoInteractions(compartments);

        Assertions.assertEquals(savedTrain.getCurrentStation(), passedTrain.getCurrentStation());
        Assertions.assertEquals(savedTrain.getCurrentPauseTime(), passedTrain.getCurrentPauseTime());
        Assertions.assertEquals(savedTrain.getId(), 1L);
    }

    @Test
    public void updateWhenTrainNotPresentInDbShouldThrowAnException(){
        Train passedTrain = new Train();
        passedTrain.setId(1L);

        when(trainRepository.findById(1L)).thenReturn(Optional.empty());
    }


    @Test
    public void attachWithExistingCompartmentShouldAddItToTheTrain(){
        Long trainId = 1L;
        Train train = new Train();
        train.setId(trainId);
        train.setCompartments(new ArrayList<>());

        Long compartmentId = 1L;
        Compartment compartment = new Compartment();
        compartment.setId(compartmentId);

        List<Long> compartments = List.of(compartmentId);

        when(trainRepository.findById(trainId)).thenReturn(Optional.of(train));
        when(compartmentService.getAllNotAttached()).thenReturn(List.of(compartment));

        trainService.attach(trainId,compartments);

        verify(compartmentService).getAllNotAttached();
        verify(compartmentRepository).save(compartment);
        verify(trainRepository).save(train);

        Assertions.assertEquals(compartment.getTrain(),train);
        Assertions.assertEquals(train.getCompartments().size(), 1);
        Assertions.assertEquals(train.getCompartments().get(0), compartment);
    }

    @Test
    public void detachWithExistingCompartmentShouldRemoveItFromTrain(){
        Long trainId = 1L;
        Train train = new Train();
        train.setId(trainId);
        train.setCompartments(new ArrayList<>());

        Long compartmentId = 1L;
        Compartment compartment = new Compartment();
        compartment.setId(compartmentId);
        train.addCompartment(compartment);

        List<Long> compartments = List.of(compartmentId);

        when(trainRepository.findById(trainId)).thenReturn(Optional.of(train));

        trainService.detach(trainId,compartments);

        verify(compartmentRepository).save(compartment);
        verify(trainRepository).save(train);

        Assertions.assertNull(compartment.getTrain());
        Assertions.assertEquals(train.getCompartments().size(), 0);
    }
}
