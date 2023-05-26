package kz.car.iotapp.service.impl;

import kz.car.iotapp.model.dto.CreateDeviceDto;
import kz.car.iotapp.model.dto.DeviceDto;
import kz.car.iotapp.model.entity.Device;
import kz.car.iotapp.repository.DeviceRepository;
import kz.car.iotapp.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @CacheEvict("devices")
    @Override
    public void addDevice(CreateDeviceDto createDeviceDto) {
        Device device = new Device();
        device.setTitle(createDeviceDto.getTitle());
        device.setAttribute(createDeviceDto.getAttribute());
        device.setDescription(createDeviceDto.getDescription());
        deviceRepository.save(device);
    }

    @Cacheable(value = "device", key = "#id")
    @Override
    public DeviceDto getDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));

        return new DeviceDto(
                device.getId(),
                device.getTitle(),
                device.getAttribute(),
                device.getDescription()
        );
    }

    @Cacheable("devices")
    @Override
    public List<DeviceDto> getAll() {
        List<Device> deviceList = deviceRepository.findAll();

        return deviceList.stream()
                .map(device -> (
                        new DeviceDto(
                                device.getId(),
                                device.getTitle(),
                                device.getAttribute(),
                                device.getDescription()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Caching(
            evict = {@CacheEvict("device")},
            put = {@CachePut(value = "device", key = "#id")}
    )
    @Override
    public void upDevice(CreateDeviceDto createDeviceDto, Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        device.setTitle(createDeviceDto.getTitle());
        device.setAttribute(createDeviceDto.getAttribute());
        device.setDescription(createDeviceDto.getDescription());
        deviceRepository.save(device);
    }

    @Caching(
            evict = {@CacheEvict("devices"),
                    @CacheEvict(value = "device", key = "#id")}
    )
    @Override
    public void delDevice(Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
        }
    }
}
