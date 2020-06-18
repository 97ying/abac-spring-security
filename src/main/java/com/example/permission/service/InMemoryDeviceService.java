package com.example.permission.service;

import com.example.permission.model.Device;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryDeviceService {

    private final Map<String, Device> deviceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Device device = new Device("device-1", "device-tem-1");
        deviceMap.put(device.getImei(), device);
    }

    public Set<Device> getAllDevices() {
        return deviceMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toSet());
    }

    public boolean createDevice(Device device) {
        if (deviceMap.get(device.getImei()) != null) {
            return true;
        }

        deviceMap.put(device.getImei(), device);
        return true;
    }

    public boolean deleteDevice(String imei) {
        deviceMap.remove(imei);
        return true;
    }
}
