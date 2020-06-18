package com.example.permission.service;

import com.example.permission.model.Vehicle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryVehicleService {

    private final Map<String, Vehicle> vehicleMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Vehicle vehicle = new Vehicle("vin-1", "vrn-1", "model-vw");
        vehicleMap.put(vehicle.getVin(), vehicle);
    }

    public Set<Vehicle> getAllVehicles() {
        return vehicleMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toSet());
    }

    public boolean createVehicle(Vehicle vehicle) {
        if (vehicleMap.get(vehicle.getVin()) != null) {
            return true;
        }

        vehicleMap.put(vehicle.getVin(), vehicle);
        return true;
    }

    public boolean deleteVehicle(String vin) {
        vehicleMap.remove(vin);
        return true;
    }
}
