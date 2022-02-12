package com.usmanadio.drone.models;

import com.usmanadio.drone.enums.DroneModel;
import com.usmanadio.drone.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table(name = "drones")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drone extends AuditEntity implements Serializable {

    @Column(name = "serial_number", unique = true, length = 100, nullable = false)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneModel model;

    @Column(name = "weight_limit", precision = 500, scale = 2)
    private double weightLimit;

    @Column(name = "battery_capacity", precision = 100, scale = 2)
    private double batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneState state;
}
