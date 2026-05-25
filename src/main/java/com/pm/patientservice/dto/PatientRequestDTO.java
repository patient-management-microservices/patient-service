package com.pm.patientservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

//    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Address is required")
//    In the controller add @Validated({Default.class, CreatePatientValidationGroup.class})
    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of Birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
