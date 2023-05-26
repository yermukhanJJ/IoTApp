package kz.car.iotapp.service;

import kz.car.iotapp.model.dto.CreateProfileDto;

public interface AuthService {
    void signUp(CreateProfileDto createProfileDto);
}
