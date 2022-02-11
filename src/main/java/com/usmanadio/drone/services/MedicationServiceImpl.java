package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.exceptions.CustomException;
import com.usmanadio.drone.models.Drone;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;
import com.usmanadio.drone.repositories.DroneRepository;
import com.usmanadio.drone.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.usmanadio.drone.utils.Constants.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService{

    private final MedicationRepository medicationRepository;
    private final DroneRepository droneRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<Medication> loadDroneWithMedication(MedicationDto medicationDto) {
        Optional<Drone> drone = droneRepository.findById(medicationDto.getDroneId());
        if (drone.isEmpty())
            throw new CustomException("There is no such drone with id " + medicationDto.getDroneId(), HttpStatus.BAD_REQUEST);
        if (drone.get().getBatteryCapacity() < 25)
            throw new CustomException("Drone cannot load medications with battery capacity less than 25%", HttpStatus.BAD_REQUEST);
        double weightLimit = drone.get().getWeightLimit();
        if (medicationDto.getWeight() > weightLimit)
            throw new CustomException("Drone cannot exceed its weight limit of " + weightLimit, HttpStatus.BAD_REQUEST);
        if (drone.get().getState() != DroneState.IDLE)
            throw new CustomException("Drone is already in " + drone.get().getState() + " state", HttpStatus.BAD_REQUEST);
        drone.get().setState(DroneState.LOADING);
        droneRepository.save(drone.get());
        Medication medication = medicationRepository.save(modelMapper.map(medicationDto, Medication.class));
        Response<Medication> response = new Response<>();
        response.setMessage(SUCCESS_MESSAGE);
        response.setStatus(HttpStatus.CREATED);
        response.setData(medication);
        return response;
    }
}
