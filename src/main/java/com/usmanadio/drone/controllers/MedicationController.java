package com.usmanadio.drone.controllers;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.services.MedicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.usmanadio.drone.utils.Constants.API;
import static com.usmanadio.drone.utils.validations.Routes.Medication.MEDICATIONS;

@RestController
@RequiredArgsConstructor
@Api(value = API + MEDICATIONS, tags = "medication-controller")
@RequestMapping(API + MEDICATIONS)
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    @ApiOperation(value = "Load a drone with medications",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            response = Response.class)
    public ResponseEntity<Response<Medication>> loadDroneWithMedication(@RequestBody @Valid MedicationDto medicationDto) {
        Response<Medication> response = medicationService.loadDroneWithMedication(medicationDto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
