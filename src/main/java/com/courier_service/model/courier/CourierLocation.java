package com.courier_service.model.courier;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierLocation {
  private Integer courierId;
  private Double lat;
  private Double lng;
  private LocalDateTime timestamp;
}
