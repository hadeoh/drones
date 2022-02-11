package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.exceptions.CustomException;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.repositories.DroneRepository;
import com.usmanadio.drone.services.impl.DroneServiceImpl;
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

public class DroneServiceTest {

    @Mock
    DroneRepository droneRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    DroneServiceImpl droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(droneRepository.findBySerialNumber("ASVG6U7I")).thenReturn(Optional.of(buildDroneModel()));
        when(droneRepository.save(any())).thenReturn(buildDroneModel());
    }

    @Test
    void testDroneSerialNumber_AlreadyExists() {
        CustomException exception = assertThrows(CustomException.class, () -> droneService.registerDrone(DroneDto.builder()
                .serialNumber("ASVG6U7I").weightLimit(400).build()));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getMessage()).isEqualTo("A drone with ASVG6U7I already exists");
    }

    @Test
    void test_RegisterDrone() {
        Response<Drone> response = droneService.registerDrone(DroneDto.builder().serialNumber("ASVG6U7IFR")
                .weightLimit(400).build());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getMessage()).isEqualTo("Operation successful");
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    private Drone buildDroneModel() {
        Drone drone = new Drone();
        drone.setModel(DroneModel.Middleweight);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);
        drone.setSerialNumber("ASVG6U7I");
        drone.setWeightLimit(500);
        return drone;
    }
}
