package com.usmanadio.drone.services;

import com.usmanadio.drone.dtos.MedicationDto;
import com.usmanadio.drone.models.Medication;
import com.usmanadio.drone.pojos.Response;

public interface MedicationService {
    Response<Medication> loadDroneWithMedication(MedicationDto medicationDto);
}
