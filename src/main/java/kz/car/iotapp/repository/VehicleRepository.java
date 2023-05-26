package kz.car.iotapp.repository;

import kz.car.iotapp.model.entity.Profile;
import kz.car.iotapp.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Boolean existsByTitleAndProfile(String title, Profile profile);

    List<Vehicle> findAllByProfile(Profile profile);

    Optional<Vehicle> findByTitle(String title);
}
