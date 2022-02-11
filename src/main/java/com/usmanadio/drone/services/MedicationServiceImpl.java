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
public class MedicationServiceImpl implements MedicationService{

    private final MedicationRepository medicationRepository;
    private final DroneRepository droneRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<Medication> loadDroneWithMedication(MedicationDto medicationDto) {
        Optional<Drone> droneOptional = droneRepository.findById(medicationDto.getDroneId());
        if (droneOptional.isEmpty())
            throw new CustomException("There is no such drone with id " + medicationDto.getDroneId(), HttpStatus.BAD_REQUEST);
        Drone drone = droneOptional.get();
        if (drone.getBatteryCapacity() < 25)
            throw new CustomException("Drone cannot load medications with battery capacity less than 25%", HttpStatus.BAD_REQUEST);
        double weightLimit = drone.getWeightLimit();
        if (medicationDto.getWeight() > weightLimit)
            throw new CustomException("Drone cannot exceed its weight limit of " + weightLimit, HttpStatus.BAD_REQUEST);
        if (drone.getState() != DroneState.IDLE)
            throw new CustomException("Drone is already in " + drone.getState() + " state", HttpStatus.BAD_REQUEST);
        Medication medication = medicationRepository.save(modelMapper.map(medicationDto, Medication.class));
        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);
        Response<Medication> response = new Response<>();
        response.setMessage(SUCCESS_MESSAGE);
        response.setStatus(HttpStatus.CREATED);
        response.setData(medication);
        return response;
    }

    @Override
    public Response<Page<Medication>> checkLoadedMedicationsForDrone(Long droneId, int pageNumber, int pageSize) {
        Optional<Drone> droneOptional = droneRepository.findById(droneId);
        if (droneOptional.isEmpty())
            throw new CustomException("There is no such drone with id " + droneId, HttpStatus.BAD_REQUEST);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt").ignoreCase()));
        Page<Medication> medications = medicationRepository.findAllByDrone_Id(droneId, pageable);
        Response<Page<Medication>> response = new Response<>();
        response.setData(medications);
        response.setStatus(HttpStatus.OK);
        response.setMessage(SUCCESS_MESSAGE);
        return response;
    }
}
