package com.example.tenpaws.domain.facility.repository;

import com.example.tenpaws.domain.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("SELECT f FROM Facility f WHERE ST_Distance_Sphere(f.location, POINT(:lat, :lng)) <= 1000")
    List<Facility> findByLocation(@Param("lat") Double lat, @Param("lng") Double lng);
}