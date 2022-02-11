package com.usmanadio.drone.dtos;

import com.usmanadio.drone.utils.validations.MedicationCode;
import com.usmanadio.drone.utils.validations.MedicationName;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class MedicationDto {

    @NotNull(message = "name must not be null")
    @MedicationName
    private String name;

    @NotNull(message = "weight must not be null")
    private double weight;

    @NotNull(message = "name must not be null")
    @MedicationCode
    private String code;

    @NotNull(message = "imageUrl must not be null")
    @NotBlank(message = "imageUrl must not be blank")
    private String imageUrl;

    @NotNull(message = "droneId must not be null")
    private Long droneId;
}
