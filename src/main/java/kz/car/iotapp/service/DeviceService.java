package kz.car.iotapp.service;

import kz.car.iotapp.model.dto.CreateDeviceDto;
import kz.car.iotapp.model.dto.DeviceDto;

import java.util.List;

public interface DeviceService {

    void addDevice(CreateDeviceDto createDeviceDto);

    DeviceDto getDevice(Long id);

    List<DeviceDto> getAll();

    void upDevice(CreateDeviceDto createDeviceDto, Long id);

    void delDevice(Long id);
}
