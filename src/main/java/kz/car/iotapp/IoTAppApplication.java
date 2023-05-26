package kz.car.iotapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IoTAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoTAppApplication.class, args);
    }

}
