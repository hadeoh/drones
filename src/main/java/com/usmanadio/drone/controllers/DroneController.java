package com.usmanadio.drone.controllers;

import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.services.DroneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.usmanadio.drone.utils.Constants.API;
import static com.usmanadio.drone.utils.validations.Routes.Drone.DRONES;

@RestController
@RequiredArgsConstructor
@Api(value = API + DRONES, tags = "drone-controller")
@RequestMapping(API + DRONES)
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    @ApiOperation(value = "Register a drone",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            response = Response.class)
    public ResponseEntity<Response<Drone>> registerDrone(@RequestBody @Valid DroneDto droneDto) {
        Response<Drone> response = droneService.registerDrone(droneDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/availableForLoading")
    @ApiOperation(value = "Check drones available for loading",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            response = Response.class)
    public ResponseEntity<Response<Page<Drone>>> checkAvailableDrones(
            @RequestParam(required = false, name = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(required = false, name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Response<Page<Drone>> response = droneService.checkAvailableDronesForLoading(pageNumber, pageSize);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
