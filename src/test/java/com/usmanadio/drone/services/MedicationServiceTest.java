package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.exceptions.CustomException;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.repositories.DroneRepository;
import com.usmanadio.drone.repositories.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MedicationServiceTest {

    @Mock
    MedicationRepository medicationRepository;

    @Mock
    DroneRepository droneRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    MedicationServiceImpl medicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(droneRepository.findById(1L)).thenReturn(Optional.of(buildDroneModel()));
        when(droneRepository.findById(2L)).thenReturn(Optional.of(build25PercentDroneModel()));
        when(droneRepository.findById(4L)).thenReturn(Optional.of(buildLoadedDroneModel()));
        when(medicationRepository.save(any())).thenReturn(buildMedicationModel());
    }

    @Test
    public void test_LoadDroneWithMedication() {
        Response<Medication> response = medicationService.loadDroneWithMedication(MedicationDto.builder()
                .droneId(1L).code("CODE_1234").name("bigGram_-").weight(200)
                .imageUrl("https://www.medicationdrone.com").build());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getMessage()).isEqualTo("Operation successful");
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    public void test_DroneShouldNotAcceptWeightMoreThanLimit() {
        CustomException exception = assertThrows(CustomException.class, () -> medicationService
                .loadDroneWithMedication(MedicationDto.builder()
                .droneId(1L).code("CODE_1234").name("bigGram_-").weight(450)
                .imageUrl("https://www.medicationdrone.com").build()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("Drone cannot exceed its weight limit of 400.0");
    }

    @Test
    public void test_DroneShouldNotLoad_WithBatteryCapacity_LessThan25Percent() {
        CustomException exception = assertThrows(CustomException.class, () -> medicationService
                .loadDroneWithMedication(MedicationDto.builder()
                        .droneId(2L).code("CODE_1234").name("bigGram_-").weight(450)
                        .imageUrl("https://www.medicationdrone.com").build()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("Drone cannot load medications with battery capacity less than 25%");
    }

    @Test
    public void test_DroneShouldNotLoad_InOtherStatesExcept_IdleState() {
        CustomException exception = assertThrows(CustomException.class, () -> medicationService
                .loadDroneWithMedication(MedicationDto.builder()
                        .droneId(4L).code("CODE_1234").name("bigGram_-").weight(50)
                        .imageUrl("https://www.medicationdrone.com").build()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("Drone is already in LOADED state");
    }

    @Test
    public void test_ThrowError_DroneDoesNotExist() {
        CustomException exception = assertThrows(CustomException.class, () -> medicationService
                .loadDroneWithMedication(MedicationDto.builder()
                        .droneId(3L).code("CODE_1234").name("bigGram_-").weight(300)
                        .imageUrl("https://www.medicationdrone.com").build()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("There is no such drone with id 3");
    }

    private Drone buildDroneModel() {
        Drone drone = new Drone();
        drone.setModel(DroneModel.Middleweight);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);
        drone.setSerialNumber("ASVG6U7I");
        drone.setWeightLimit(400);
        drone.setId(1L);
        return drone;
    }

    private Drone build25PercentDroneModel() {
        Drone drone = new Drone();
        drone.setModel(DroneModel.Middleweight);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(24);
        drone.setSerialNumber("ASVG6U7I");
        drone.setWeightLimit(400);
        drone.setId(2L);
        return drone;
    }

    private Drone buildLoadedDroneModel() {
        Drone drone = new Drone();
        drone.setModel(DroneModel.Middleweight);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(80);
        drone.setSerialNumber("ASVG6U7I");
        drone.setWeightLimit(400);
        drone.setId(4L);
        drone.setState(DroneState.LOADED);
        return drone;
    }

    private Medication buildMedicationModel() {
        Medication medication = new Medication();
        medication.setCode("CODE_1234");
        medication.setName("bigGram_-");
        medication.setDrone(buildDroneModel());
        medication.setWeight(200);
        medication.setImageUrl("https://www.medicationdrone.com");
        medication.setId(1L);
        return medication;
    }


}
