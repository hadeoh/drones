package com.usmanadio.drone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table(name = "medications")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication extends AuditEntity implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "drone_id", referencedColumnName = "id", nullable = false)
    private Drone drone;
}
