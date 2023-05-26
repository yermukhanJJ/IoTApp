package kz.car.iotapp.util.validator;

import kz.car.iotapp.model.dto.CreateProfileDto;
import kz.car.iotapp.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthValidator implements Validator {

    private final ProfileRepository profileRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateProfileDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateProfileDto createProfileDto = (CreateProfileDto) target;
        String regexEmail = "^[\\w]+(?:\\.[\\w]+)*@(?:[a-z0-9-]+\\.)+[\\\\a-zA-Z]{2,6}";
        Pattern patternEmail = Pattern.compile(regexEmail);

        if (!patternEmail.matcher(createProfileDto.getEmail()).matches())
            errors.rejectValue("email","","Email address is invalid! Format:example@example.com");
        if (profileRepository.existsByEmail(createProfileDto.getEmail()))
            errors.rejectValue("email","","Email is already taken!");
        if (profileRepository.existsByUsername(createProfileDto.getUsername()))
            errors.rejectValue("username","","Username is already taken!");
    }
}
