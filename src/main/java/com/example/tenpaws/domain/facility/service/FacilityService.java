package com.example.tenpaws.domain.facility.service;

import com.example.tenpaws.domain.facility.repository.FacilityRepository;
import com.example.tenpaws.facility.Facility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public List<FacilityDTO> getNearbyFacilities(Double lat, Double lng) {
        return facilityRepository.findByLocation(lat, lng).stream()
                .map(this::toFacilityDTO)
                .collect(Collectors.toList());
    }

    private FacilityDTO toFacilityDTO(Facility facility) {
        FacilityDTO dto = new FacilityDTO();
        dto.setId(facility.getId());
        dto.setName(facility.getName());
        dto.setLat(facility.getLat());
        dto.setLng(facility.getLng());
        return dto;
    }
}