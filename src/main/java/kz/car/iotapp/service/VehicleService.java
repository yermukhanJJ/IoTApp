package kz.car.iotapp.service;

import kz.car.iotapp.model.dto.VehicleDto;
import kz.car.iotapp.model.dto.VehicleReadDto;
import kz.car.iotapp.model.dto.VehicleType;

import java.security.Principal;
import java.util.List;

public interface VehicleService {
    void createVehicle(Principal principal, VehicleDto vehicleDto, VehicleType vehicleType);

    void updateVehicle(VehicleDto vehicleDto, VehicleType vehicleType);

    List<VehicleReadDto> getAllVehicle(Principal principal);

    VehicleDto getVehicle(Long id, Principal principal);

    void deleteVehicle(Long id);
}
