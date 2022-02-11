package com.usmanadio.drone.repositories;

import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.models.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Page<Medication> findAllByDrone_Id(Long id, Pageable pageable);
}
