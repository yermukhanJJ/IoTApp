package kz.car.iotapp.repository;

import kz.car.iotapp.model.entity.IoT;
import kz.car.iotapp.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IoTRepository extends JpaRepository<IoT, Long> {
    Optional<IoT> findByVehicleAndId(Vehicle vehicle, Long id);
    List<IoT> findAllByVehicle(Vehicle vehicle);
}
