package kz.car.iotapp.controller;

import kz.car.iotapp.model.dto.IoTDto;
import kz.car.iotapp.model.dto.VehicleDto;
import kz.car.iotapp.model.dto.VehicleReadDto;
import kz.car.iotapp.model.dto.VehicleType;
import kz.car.iotapp.model.entity.Vehicle;
import kz.car.iotapp.service.IoTService;
import kz.car.iotapp.service.VehicleService;
import kz.car.iotapp.util.validator.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleValidator vehicleValidator;
    private final IoTService ioTService;

    @GetMapping
    public String getAll(Model model, Principal principal) {
        List<VehicleReadDto> vehicles = vehicleService.getAllVehicle(principal);
        Vehicle vehicle = new Vehicle();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("vehicles", vehicles);
        return "vehicle/vehicles";
    }

    @GetMapping("/devices/{id}")
    public String getAllDevicesByVehicle(@PathVariable(value = "id") Long id, Principal principal,Model model) {
        List<IoTDto> ioTDtos = ioTService.getAllIoTByVehicle(id);
        VehicleDto vehicle = vehicleService.getVehicle(id, principal);
        IoTDto ioTDto = new IoTDto();
        model.addAttribute("ioT", ioTDto);
        model.addAttribute("ioTs", ioTDtos);
        model.addAttribute("vehicle", vehicle);

        return "device/view";
    }

    @GetMapping("/add")
    public String addVehiclePage(Model model) {
        VehicleDto vehicleDto = new VehicleDto();
        model.addAttribute("vehicle", vehicleDto);

        return "vehicle/add-vehicle";
    }

    @PostMapping("/add")
    public String addVehicle(@ModelAttribute("vehicle") @Valid VehicleDto vehicleDto,
                             @RequestParam("vehicleType") String vehicleType,
                             BindingResult result,
                             Principal principal) {
        vehicleValidator.validate(vehicleDto, result);

        if (result.hasErrors())
            return "vehicle/add-vehicle";

        VehicleType type = VehicleType.valueOf(vehicleType);
        vehicleService.createVehicle(principal, vehicleDto, type);
        return "redirect:/welcome";
    }
}
