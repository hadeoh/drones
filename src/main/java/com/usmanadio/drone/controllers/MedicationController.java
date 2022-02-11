package com.usmanadio.drone.controllers;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.services.MedicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{droneId}")
    @ApiOperation(value = "Load a drone with medications",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            response = Response.class)
    public ResponseEntity<Response<Page<Medication>>> checkLoadedMedicationsForDrone(
            @PathVariable("droneId") Long droneId,
            @RequestParam(required = false, name = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(required = false, name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Response<Page<Medication>> response = medicationService.checkLoadedMedicationsForDrone(droneId, pageNumber, pageSize);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
