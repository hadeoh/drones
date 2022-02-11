package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;

public interface DroneService {
    Response<Drone> registerDrone(DroneDto droneDto);
}
