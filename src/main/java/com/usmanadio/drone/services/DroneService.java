package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;
import org.springframework.data.domain.Page;

public interface DroneService {
    Response<Drone> registerDrone(DroneDto droneDto);
    Response<Page<Drone>> checkAvailableDronesForLoading(int pageNumber, int pageSize);
}
