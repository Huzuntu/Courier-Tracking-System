package com.courier_service.model.courier;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CourierLocation
{
    private Integer courierId;
    private Double lat;
    private Double lng;
    private LocalDateTime timestamp;
}
