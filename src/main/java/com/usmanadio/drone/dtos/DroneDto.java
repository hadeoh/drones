package com.usmanadio.drone.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Builder
public class DroneDto {
    @Size(max = 100, message = "The length of serial number cannot be more than 100")
    private String serialNumber;

    @Max(value = 500, message = "weightLimit cannot be more than 500gr")
    @Min(value = 1, message = "weightLimit cannot be less that 1gr")
    private double weightLimit;
}
