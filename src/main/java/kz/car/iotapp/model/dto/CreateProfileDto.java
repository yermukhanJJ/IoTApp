package kz.car.iotapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileDto {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String password;

}
