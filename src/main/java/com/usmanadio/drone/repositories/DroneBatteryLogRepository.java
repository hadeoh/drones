package com.usmanadio.drone.repositories;

import com.usmanadio.drone.models.DroneBatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatteryLogRepository extends JpaRepository<DroneBatteryLog, Long> {
}
