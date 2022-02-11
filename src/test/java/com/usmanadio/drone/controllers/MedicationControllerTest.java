package com.usmanadio.drone.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.repositories.DroneRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.usmanadio.drone.utils.Constants.API;
import static com.usmanadio.drone.utils.validations.Routes.Drone.DRONES;
import static com.usmanadio.drone.utils.validations.Routes.Medication.MEDICATIONS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MedicationControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_LoadDroneWithMedication() throws Exception {
        MedicationDto medicationDto = MedicationDto.builder().imageUrl("https://www.med.com")
                .code("ASDD_334AS").droneId(1L).weight(300).name("meDiCAtion12").build();
        mockMvc.perform(post(API + DRONES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(DroneDto.builder().serialNumber("ABCDEF").weightLimit(500).build()))).andDo(print());
        mockMvc.perform(post(API + MEDICATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(medicationDto))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message", Matchers.is("Operation successful")))
                .andExpect(jsonPath("data.weight", Matchers.is(300.0)))
                .andExpect(jsonPath("errors", Matchers.blankOrNullString()));
    }

    @Test
    public void test_AllowedCodeValidations() throws Exception {
        MedicationDto medicationDto = MedicationDto.builder().imageUrl("https://www.med.com")
                .code("ASaDD_334AS").droneId(1L).weight(300).name("meDiCAtion12").build();
        mockMvc.perform(post(API + MEDICATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(medicationDto))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is("Validation Error")))
                .andExpect(jsonPath("errors", Matchers.notNullValue()));
    }

    @Test
    public void test_AllowedNameValidations() throws Exception {
        MedicationDto medicationDto = MedicationDto.builder().imageUrl("https://www.med.com")
                .code("ASDD_334AS").droneId(1L).weight(300).name("meDi@CAtion12").build();
        mockMvc.perform(post(API + MEDICATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(medicationDto))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is("Validation Error")))
                .andExpect(jsonPath("errors", Matchers.notNullValue()));
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
}
