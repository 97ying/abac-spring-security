package com.example.permission.controller;

import com.example.permission.authority.AccessPolicyEnforcement;
import com.example.permission.model.Device;
import com.example.permission.model.Vehicle;
import com.example.permission.service.InMemoryDeviceService;
import com.example.permission.service.InMemoryVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class OrgController {

    @Autowired
    private AccessPolicyEnforcement policy;

    @Autowired
    private InMemoryVehicleService vehicleService;

    @Autowired
    private InMemoryDeviceService deviceService;

    @RequestMapping("/organizations/{orgId}/vehicles")
    @ResponseStatus(HttpStatus.OK)
    public Set<Vehicle> getAllVehiclesByOrgId(@PathVariable String orgId, @RequestHeader("userId") String userId) {
        policy.checkOrgIdPermission(orgId, "vehicle:read");

        //service to get the vehicles and return
        return vehicleService.getAllVehicles();
    }

    @DeleteMapping("/organizations/{orgId}/vehicles/{vin}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVehiclesByOrgId(@PathVariable String orgId, @RequestHeader("userId") String userId,@PathVariable String vin) {
        policy.checkRolePermission("partner_admin", "vehicle:delete");
        //service to update the vehicles and return
        vehicleService.deleteVehicle(vin);
    }

    @PostMapping("/organizations/{orgId}/vehicles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createVehiclesByOrgId(@PathVariable String orgId, @RequestHeader("userId") String userId, @RequestBody Vehicle vehicle) {
        policy.checkOrgIdPermission(orgId, "vehicle:create");
        //service to create the vehicles and return
        vehicleService.createVehicle(vehicle);
    }

    @RequestMapping("/organizations/{orgId}/devices")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#orgId, 'device:read')")
    public Set<Device> getAllDevicesByOrgId(@PathVariable String orgId, @RequestHeader("userId") String userId) {
        //service to get the devices and return
        return deviceService.getAllDevices();
    }
}
