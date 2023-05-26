package kz.car.iotapp.util.validator;

import kz.car.iotapp.model.dto.VehicleDto;
import kz.car.iotapp.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.security.Principal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VehicleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return VehicleDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VehicleDto vehicleDto = (VehicleDto) target;

        if (vehicleDto.getGeneration() < 1885 && vehicleDto.getGeneration() > LocalDateTime.now().getYear() + 1)
            errors.rejectValue("generation","","Enter the natural year of manufacture of the car");
        if (vehicleDto.getTitle().equals("") || vehicleDto.getTitle().length() < 2)
            errors.rejectValue("title","","The name of the vehicle must consist of at least two characters");
    }
}
