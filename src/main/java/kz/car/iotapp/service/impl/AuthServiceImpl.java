package kz.car.iotapp.service.impl;

import kz.car.iotapp.model.dto.CreateProfileDto;
import kz.car.iotapp.model.entity.Profile;
import kz.car.iotapp.model.entity.Role;
import kz.car.iotapp.repository.ProfileRepository;
import kz.car.iotapp.repository.RoleRepository;
import kz.car.iotapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void signUp(CreateProfileDto createProfileDto) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .get();
        roles.add(userRole);

        Profile profile = new Profile(createProfileDto.getUsername(), createProfileDto.getEmail(), encoder.encode(createProfileDto.getPassword()),
                createProfileDto.getFirstname(), createProfileDto.getFirstname());
        profile.setRoles(roles);

        profileRepository.save(profile);
        log.info("Create new account with role: " + userRole.getName());
    }
}
