package com.example.tenpaws.domain.user.entity;

import com.example.tenpaws.global.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(name = "preferred_size")
    private String preferredSize;

    @Column(name = "preferred_personality")
    private String preferredPersonality;

    @Column(name = "preferred_exercise_level")
    private Integer preferredExerciseLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    public void changePassword(String password) {
        this.password = password;
    }
    public void changeEmail(String email) { this.email = email; }
    public void changeUserRole(UserRole userRole) { this.userRole = userRole; }
}