package com.usmanadio.drone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table(name = "drone_battery_logs")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneBatteryLog extends AuditEntity implements Serializable {

    @Column(name = "battery_level", nullable = false)
    private double batteryLevel;

    @ManyToOne
    @JoinColumn(name = "drone_id", referencedColumnName = "id")
    private Drone drone;
}
