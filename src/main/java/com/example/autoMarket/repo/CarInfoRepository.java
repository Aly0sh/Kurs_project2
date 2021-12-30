package com.example.autoMarket.repo;

import com.example.autoMarket.models.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarInfoRepository extends JpaRepository<CarInfo, Long> {
    List<CarInfo> findByUserUsername(String username);
    List<CarInfo> findByBrand(String brand);
}