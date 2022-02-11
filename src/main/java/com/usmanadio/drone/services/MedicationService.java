package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;
import org.springframework.data.domain.Page;

public interface MedicationService {
    Response<Medication> loadDroneWithMedication(MedicationDto medicationDto);
    Response<Page<Medication>> checkLoadedMedicationsForDrone(Long droneId, int pageNumber, int pageSize);
}
