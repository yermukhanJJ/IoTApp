package kz.car.iotapp.controller;

import kz.car.iotapp.model.dto.CreateProfileDto;
import kz.car.iotapp.service.AuthService;
import kz.car.iotapp.util.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthValidator authValidator;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String createAccountPage(@ModelAttribute("userRequest")CreateProfileDto createProfileDto) {
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String createAccount(@ModelAttribute("userRequest") @Valid CreateProfileDto createProfileDto,
                                BindingResult result) {
        authValidator.validate(createProfileDto, result);

        if (result.hasErrors()) {
            return "/auth/sign-up";
        }

        authService.signUp(createProfileDto);
        return "redirect:/auth/login";
    }

}
