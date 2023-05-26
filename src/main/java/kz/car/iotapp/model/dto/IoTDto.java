package kz.car.iotapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IoTDto {
    private Long id;
    private String thingName;
    private String thingAttribute;
    private String attributeValue;
}
