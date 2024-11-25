//package com.example.tenpaws.domain.facility.entity;
//
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import lombok.ToString;
//
//@Entity
//@Table(name = "Facilities") // 테이블 이름 매핑
//@Getter
//@NoArgsConstructor // 기본 생성자 자동 생성
//@AllArgsConstructor // 모든 필드를 초기화하는 생성자 자동 생성
//@ToString
//public class Facility {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 매핑
//    private Long facilityId;
//
//    private String facilityType;
//
//    private String location;
//}