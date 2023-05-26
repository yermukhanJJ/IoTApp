package kz.car.iotapp.service;

import kz.car.iotapp.model.dto.DeviceForm;
import kz.car.iotapp.model.dto.IoTDto;

import java.security.Principal;
import java.util.List;

public interface IoTService {
    void createIoT(Principal principal, DeviceForm form);
    List<IoTDto> getAllIoTByVehicle(Long idVehicle);
}
