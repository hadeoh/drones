package com.usmanadio.drone.repositories;

import com.usmanadio.drone.enums.DroneState;
import com.usmanadio.drone.models.Drone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
    Page<Drone> findAllByState(DroneState state, Pageable pageable);
}
