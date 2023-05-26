package kz.car.iotapp.controller;

import kz.car.iotapp.model.dto.*;
import kz.car.iotapp.model.entity.Vehicle;
import kz.car.iotapp.service.DeviceService;
import kz.car.iotapp.service.IoTService;
import kz.car.iotapp.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final VehicleService vehicleService;
    private final IoTService ioTService;

    @GetMapping
    public String devicePage(Model model, Principal principal) {
        List<DeviceDto> devices = deviceService.getAll();
        DeviceDto device = new DeviceDto();
        List<VehicleReadDto> vehicles = vehicleService.getAllVehicle(principal);
        Vehicle vehicle = new Vehicle();
        model.addAttribute("devices", devices);
        model.addAttribute("device", device);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("form", new DeviceForm());
        return "device";
    }

    @PostMapping
    public String addDevice(@ModelAttribute("form") DeviceForm form, Principal principal) {
        ioTService.createIoT(principal, form);

        return "redirect:/welcome";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add")
    public String createDevicePage(Model model) {
        CreateDeviceDto createDeviceDto = new CreateDeviceDto();
        model.addAttribute("device", createDeviceDto);
        return "device/create";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String createDevice(@ModelAttribute("device") CreateDeviceDto createDeviceDto) {
        deviceService.addDevice(createDeviceDto);

        return "redirect:/welcome";
    }
}
