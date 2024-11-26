package com.example.tenpaws.domain.user.dto;

import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.global.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDTO {

    @NotBlank(message = "이메일 정보는 필수입니다")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "유효하지 않은 이메일 형식입니다"
    )
    private String email;

    @NotBlank(message = "사용자 이름은 필수입니다")
    private String username;

    @NotBlank(message = "사용자 비밀번호는 필수입니다")
    @Size(min = 7, max = 20, message = "사용자 비밀번호는 7글자 이상, 20글자 이하로 제한됩니다")
    private String password;

    @NotNull(message = "생년월일 정보는 필수입니다")
    @Past(message = "생년월일은 과거의 날짜여야 합니다")
    private LocalDate birthDate;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phoneNumber;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String address;

    private String preferredSize;
    private String preferredPersonality;
    private Integer preferredExerciseLevel;

    @NotNull(message = "사용자 역할은 필수 항목입니다.")
    private UserRole userRole;

    // DTO -> Entity 변환 메서드
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .birthDate(this.birthDate)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .preferredSize(this.preferredSize)
                .preferredPersonality(this.preferredPersonality)
                .preferredExerciseLevel(this.preferredExerciseLevel)
                .userRole(this.userRole)
                .build();
    }
}

