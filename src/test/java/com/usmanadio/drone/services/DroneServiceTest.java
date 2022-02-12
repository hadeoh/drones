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
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(droneRepository.findById(1L)).thenReturn(Optional.of(buildDroneModel()));
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

    @Test
    void test_check_available_drones_for_loading() {
        List<Drone> droneList = new ArrayList<>();
        droneList.add(buildDroneModel());
        Pageable pageable = PageRequest.of(0,10, Sort.by(Sort.Order.desc("createdAt").ignoreCase()));
        Page<Drone> dronePage = new PageImpl<>(droneList,pageable, droneList.size());
        when(droneRepository.findAllByState(DroneState.IDLE, pageable)).thenReturn(dronePage);
        Response<Page<Drone>> response = droneService.checkAvailableDronesForLoading(1, 10);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getTotalElements()).isEqualTo(1);
        verify(droneRepository, times(1)).findAllByState(DroneState.IDLE, pageable);
    }

    @Test
    void test_check_drone_battery_level() {
        Response<Drone> response = droneService.checkDroneBatteryCapacity(1L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getMessage()).isEqualTo("Operation successful");
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getBatteryCapacity()).isEqualTo(100);
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
