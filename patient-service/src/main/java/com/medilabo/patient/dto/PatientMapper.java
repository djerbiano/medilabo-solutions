package com.medilabo.patient.dto;

import com.medilabo.patient.entity.Gender;
import com.medilabo.patient.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDto toDto(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getGender().getCode(),
                patient.getAddress(),
                patient.getPhone());
    }

    public void updateEntity(Patient patient, PatientDto dto, Gender gender) {
        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setBirthDate(dto.birthDate());
        patient.setGender(gender);
        patient.setAddress(dto.address());
        patient.setPhone(dto.phone());
    }
}
