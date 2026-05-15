package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;
import java.util.Date;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO) {
        return Patient.builder()
                .name(patientRequestDTO.getName())
                .email(patientRequestDTO.getEmail())
                .address(patientRequestDTO.getAddress())
                .dateOfBirth(patientRequestDTO.getDateOfBirth())
                .registeredDate(LocalDate.now())
                .build();
    }
}
