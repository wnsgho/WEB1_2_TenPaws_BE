//package com.example.tenpaws.domain.facility.controller;
//
//import com.example.tenpaws.domain.facility.entity.Facility;
//import com.example.tenpaws.domain.facility.service.FacilityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/facilities")
//public class FacilityController {
//
//    private final FacilityService facilityService;
//
//    @Autowired
//    public FacilityController(FacilityService facilityService) {
//        this.facilityService = facilityService;
//    }
//
//    // CREATE (새로운 시설 등록)
//    @PostMapping
//    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
//        Facility savedFacility = facilityService.createFacility(facility);
//        return ResponseEntity.ok(savedFacility);
//    }
//
//    // READ (모든 시설 조회)
//    @GetMapping
//    public ResponseEntity<List<Facility>> getAllFacilities() {
//        List<Facility> facilities = facilityService.getAllFacilities();
//        return ResponseEntity.ok(facilities);
//    }
//
//    // READ (특정 시설 조회)
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<Facility>> getFacilityById(@PathVariable Long id) {
//        Optional<Facility> facility = facilityService.getFacilityById(id);
//        return facility.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // UPDATE (시설 정보 업데이트)
//    @PutMapping("/{id}")
//    public ResponseEntity<Facility> updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
//        facility.setFacilityId(id);
//        Facility updatedFacility = facilityService.updateFacility(facility);
//        return ResponseEntity.ok(updatedFacility);
//    }
//
//    // DELETE (시설 삭제)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
//        facilityService.deleteFacility(id);
//        return ResponseEntity.noContent().build();
//    }
//}