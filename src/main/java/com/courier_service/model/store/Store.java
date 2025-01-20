package com.courier_service.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store
{
    private Long id;
    private String name; 
    private double lat;  
    private double lng;  
}
