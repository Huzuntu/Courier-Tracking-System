package com.courier_service.model.store;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreEntry {
  private String storeName;
  private LocalDateTime entryTime;
}
