package com.example.tenpaws.shelter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder //일단 여기 넣어두고
@Table(name = "shelters")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String pw;

    @Column(name = "shelter_name", nullable = false, length = 255)
    private String shelterName;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

}

