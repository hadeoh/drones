package com.usmanadio.drone.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.drone.dtos.DroneDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.usmanadio.drone.utils.Constants.API;
import static com.usmanadio.drone.utils.Routes.Drone.DRONES;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DroneControllerTest {

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
    public void test_DroneRegister() throws Exception {
        DroneDto droneDto = DroneDto.builder().serialNumber("GHO126ST").weightLimit(300).build();
        mockMvc.perform(post(API + DRONES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(droneDto))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.weightLimit", Matchers.is(300.0)))
                .andExpect(jsonPath("errors", Matchers.blankOrNullString()));
    }

    @Test
    public void test_WeightLimit_NotMoreThan500() throws Exception {
        DroneDto droneDto = DroneDto.builder().serialNumber("GHO126ST").weightLimit(600).build();
        mockMvc.perform(post(API + DRONES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(droneDto))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is("Validation Error")))
                .andExpect(jsonPath("errors", Matchers.notNullValue()));
    }

}
