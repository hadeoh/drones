package com.usmanadio.drone.services.impl;

import com.usmanadio.drone.dtos.DroneDto;
import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.exceptions.CustomException;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.repositories.DroneRepository;
import com.usmanadio.drone.services.DroneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.usmanadio.drone.utils.Constants.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<Drone> registerDrone(DroneDto droneDto) {
        if (droneRepository.findBySerialNumber(droneDto.getSerialNumber()).isPresent())
            throw new CustomException("A drone with " + droneDto.getSerialNumber() + " already exists", HttpStatus.CONFLICT);
        Drone drone = new Drone();
        modelMapper.map(droneDto, drone);
        drone.setBatteryCapacity(100);
        drone.setState(DroneState.IDLE);
        if (droneDto.getWeightLimit() >= 1 && droneDto.getWeightLimit() < 200) {
            drone.setModel(DroneModel.Lightweight);
        } else if (droneDto.getWeightLimit() >= 200 && droneDto.getWeightLimit() < 300) {
            drone.setModel(DroneModel.Middleweight);
        } else if (droneDto.getWeightLimit() >= 300 && droneDto.getWeightLimit() < 400) {
            drone.setModel(DroneModel.Cruiserweight);
        } else {
            drone.setModel(DroneModel.Heavyweight);
        }
        drone = droneRepository.save(drone);

        Response<Drone> response = new Response<>();
        response.setData(drone);
        response.setErrors(null);
        response.setStatus(HttpStatus.CREATED);
        response.setMessage(SUCCESS_MESSAGE);
        return response;
    }

    @Override
    public Response<Page<Drone>> checkAvailableDronesForLoading(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt").ignoreCase()));
        Page<Drone> drones = droneRepository.findAllByState(DroneState.IDLE, pageable);
        Response<Page<Drone>> response = new Response<>();
        response.setMessage(SUCCESS_MESSAGE);
        response.setData(drones);
        response.setStatus(HttpStatus.OK);
        return response;
    }

    @Override
    public Response<Drone> checkDroneBatteryCapacity(Long droneId) {
        Optional<Drone> droneOptional = droneRepository.findById(droneId);
        if (droneOptional.isEmpty())
            throw new CustomException("There is no such drone with id " + droneId, HttpStatus.BAD_REQUEST);
        Response<Drone> response = new Response<>();
        response.setData(droneOptional.get());
        response.setErrors(null);
        response.setStatus(HttpStatus.OK);
        response.setMessage(SUCCESS_MESSAGE);
        return response;
    }
}
