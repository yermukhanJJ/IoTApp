package kz.car.iotapp.repository;

import kz.car.iotapp.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByTitle(String title);
    List<Device> findByTitleIn(List<String> deviceName);
}
