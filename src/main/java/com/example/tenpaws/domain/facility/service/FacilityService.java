//package com.example.tenpaws.domain.facility.service;
//
//import com.example.tenpaws.domain.facility.entity.Facility;
//import com.example.tenpaws.domain.facility.repository.FacilityRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class FacilityService {
//
//    private final FacilityRepository facilityRepository;
//
//    public FacilityService(FacilityRepository facilityRepository) {
//        this.facilityRepository = facilityRepository;
//    }
//
//    // CREATE (새로운 시설 등록)
//    public Facility createFacility(Facility facility) {
//        return facilityRepository.save(facility);
//    }
//
//    // READ (모든 시설 조회)
//    public List<Facility> getAllFacilities() {
//        return facilityRepository.findAll();
//    }
//
//    // READ (특정 시설 조회)
//    public Optional<Facility> getFacilityById(Long facilityId) {
//        return facilityRepository.findById(facilityId);
//    }
//
//    // UPDATE (시설 정보 업데이트)
//    public Facility updateFacility(Facility facility) {
//        if (!facilityRepository.existsById(facility.getFacilityId())) {
//            throw new RuntimeException("존재하지 않는 시설입니다.");
//        }
//        return facilityRepository.save(facility);
//    }
//
//    // DELETE (시설 삭제)
//    public void deleteFacility(Long facilityId) {
//        if (!facilityRepository.existsById(facilityId)) {
//            throw new RuntimeException("존재하지 않는 시설입니다.");
//        }
//        facilityRepository.deleteById(facilityId);
//    }
//}