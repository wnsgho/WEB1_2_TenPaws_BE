package com.example.tenpaws.domain.facility.controller;

import com.example.tenpaws.domain.facility.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/facilities")
    public List<FacilityDTO> getNearbyFacilities(@RequestParam Double lat, @RequestParam Double lng) {
        return facilityService.getNearbyFacilities(lat, lng);
    }
}