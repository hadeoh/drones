package com.usmanadio.drone.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.services.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.usmanadio.drone.utils.Constants.API;
import static com.usmanadio.drone.utils.Constants.SUCCESS_MESSAGE;
import static com.usmanadio.drone.utils.validations.Routes.Drone.DRONES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DroneController.class)
@ContextConfiguration(classes = DroneController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
public class DroneControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DroneService droneService;

    @AfterEach
    void clear() {
        Mockito.reset(droneService);
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_DroneRegister() throws Exception {
        Drone drone = buildDroneModel();

        Response<Drone> response = new Response();
        response.setStatus(HttpStatus.CREATED);
        response.setData(drone);
        response.setErrors(null);
        response.setMessage(SUCCESS_MESSAGE);

        when(droneService.registerDrone(any())).thenReturn(response);
        DroneDto droneDto = DroneDto.builder().serialNumber("GHO126ST").weightLimit(300).build();
        mockMvc.perform(post(API + DRONES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(droneDto))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.weightLimit", Matchers.is(500.0)))
                .andExpect(jsonPath("errors", Matchers.blankOrNullString()));
    }

    @Test
    public void test_check_available_drones() throws Exception {
        List<Drone> droneList = new ArrayList<>();
        droneList.add(buildDroneModel());
        Pageable pageable = PageRequest.of(0,10, Sort.by(Sort.Order.desc("createdAt").ignoreCase()));
        Page<Drone> dronePage = new PageImpl<>(droneList,pageable, droneList.size());

        Response<Page<Drone>> response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setData(dronePage);
        response.setErrors(null);
        response.setMessage(SUCCESS_MESSAGE);

        when(droneService.checkAvailableDronesForLoading(1, 50)).thenReturn(response);
        mockMvc.perform(get(API + DRONES + "/availableForLoading"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", Matchers.notNullValue()))
                .andExpect(jsonPath("errors", Matchers.blankOrNullString()));
    }

    @Test
    public void test_drone_battery_capacity() throws Exception {

        Response<Drone> response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setData(buildDroneModel());
        response.setErrors(null);
        response.setMessage(SUCCESS_MESSAGE);

        when(droneService.checkDroneBatteryCapacity(1L)).thenReturn(response);
        mockMvc.perform(get(API + DRONES + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", Matchers.notNullValue()))
                .andExpect(jsonPath("errors", Matchers.blankOrNullString()));
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
