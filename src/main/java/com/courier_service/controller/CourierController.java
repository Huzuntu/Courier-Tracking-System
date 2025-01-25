package com.courier_service.controller;

import com.courier_service.model.courier.CourierLocation;
import com.courier_service.service.StateStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {
  private KafkaTemplate<Integer, CourierLocation> kafkaTemplate;
  private final StateStoreService stateStoreService;

  /**
   * Constructs a CourierController with dependencies injected.
   *
   * @param kafkaTemplate KafkaTemplate for sending courier locations.
   * @param stateStoreService Service to retrieve aggregated courier data.
   */
  public CourierController(
      KafkaTemplate<Integer, CourierLocation> kafkaTemplate, StateStoreService stateStoreService) {
    this.kafkaTemplate = kafkaTemplate;
    this.stateStoreService = stateStoreService;
  }

  @Operation(
      summary = "Log a courier's location",
      description = "Logs the courier's location by sending it to Kafka.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Location logged successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
      })
  @PostMapping("/locations")
  public ResponseEntity<Void> logLocation(@RequestBody CourierLocation location) {
    location.setTimestamp(LocalDateTime.now());
    kafkaTemplate.send("input", location.getCourierId(), location);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Get total travel distance",
      description = "Retrieves the total distance traveled by a courier.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Distance retrieved successfully",
            content = @Content(schema = @Schema(implementation = Double.class))),
        @ApiResponse(responseCode = "404", description = "Courier not found", content = @Content)
      })
  @GetMapping("/{id}/distance-travelled/")
  public ResponseEntity<Double> getTotalTravelDistance(@PathVariable Integer courierId) {
    return ResponseEntity.ok(stateStoreService.calculateDistanceForCourier(courierId));
  }
}
