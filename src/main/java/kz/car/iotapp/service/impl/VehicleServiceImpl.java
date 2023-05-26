package kz.car.iotapp.service.impl;

import kz.car.iotapp.model.dto.VehicleDto;
import kz.car.iotapp.model.dto.VehicleReadDto;
import kz.car.iotapp.model.dto.VehicleType;
import kz.car.iotapp.model.entity.Profile;
import kz.car.iotapp.model.entity.Vehicle;
import kz.car.iotapp.repository.ProfileRepository;
import kz.car.iotapp.repository.VehicleRepository;
import kz.car.iotapp.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ProfileRepository profileRepository;

    @Override
    public void createVehicle(Principal principal, VehicleDto vehicleDto, VehicleType vehicleType) {
        Profile profile = profileRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User " + principal.getName() + " not found"));
        Vehicle vehicle = new Vehicle();
        vehicle.setProfile(profile);
        vehicle.setTitle(vehicleDto.getTitle());
        vehicle.setGeneration(vehicleDto.getGeneration());
        switch (vehicleType) {
            case SUV -> vehicle.setType("suv");
            case SEDAN -> vehicle.setType("sedan");
            case TRUCK -> vehicle.setType("truck");
            case SPORTCAR -> vehicle.setType("sports car");
            case CROSSOVER -> vehicle.setType("crossover");
            case UNIVERSAL -> vehicle.setType("universal");
            default -> vehicle.setType("another type");
        }

        vehicleRepository.save(vehicle);
    }

    @Override
    public void updateVehicle(VehicleDto vehicleDto, VehicleType vehicleType) {

    }

    @Override
    public List<VehicleReadDto> getAllVehicle(Principal principal) {
        Profile profile = profileRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Vehicle> vehicleList = vehicleRepository.findAllByProfile(profile);
        return vehicleList.stream()
                .map(vehicle -> new VehicleReadDto(
                        vehicle.getId(),
                        vehicle.getTitle(),
                        vehicle.getGeneration(),
                        vehicle.getType()
                )).toList();
    }

    @Override
    public VehicleDto getVehicle(Long id, Principal principal) {
        return null;
    }

    @Override
    public void deleteVehicle(Long id) {

    }
}
