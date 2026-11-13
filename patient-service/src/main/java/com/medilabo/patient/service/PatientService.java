package com.medilabo.patient.service;

import com.medilabo.patient.dto.PatientDto;
import com.medilabo.patient.dto.PatientMapper;
import com.medilabo.patient.entity.Gender;
import com.medilabo.patient.entity.Patient;
import com.medilabo.patient.exception.GenderNotFoundException;
import com.medilabo.patient.exception.PatientAlreadyExistsException;
import com.medilabo.patient.exception.PatientNotFoundException;
import com.medilabo.patient.repository.GenderRepository;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final GenderRepository genderRepository;
    private final PatientMapper patientMapper;


    private Gender findGender(Character code) {
        return genderRepository.findByCode(code)
                .orElseThrow(() -> new GenderNotFoundException(code));
    }

    @Transactional(readOnly = true)
    public List<PatientDto> findAll() {
        return patientRepository.findAll().stream()
                .map(patient -> patientMapper.toDto(patient))
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientDto findById(Long id) {
        return patientRepository.findById(id)
                .map(patient -> patientMapper.toDto(patient))
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public PatientDto create(PatientDto dto) {
        if (patientRepository.existsByFirstNameAndLastNameAndBirthDate(
                dto.firstName(), dto.lastName(), dto.birthDate())) {
            throw new PatientAlreadyExistsException(dto.firstName(), dto.lastName(), dto.birthDate());
        }
        Patient patient = new Patient();
        patientMapper.updateEntity(patient, dto, findGender(dto.gender()));
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Transactional
    public PatientDto update(Long id, PatientDto dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        if (patientRepository.existsByFirstNameAndLastNameAndBirthDateAndIdNot(
                dto.firstName(), dto.lastName(), dto.birthDate(), id)) {
            throw new PatientAlreadyExistsException(dto.firstName(), dto.lastName(), dto.birthDate());
        }
        patientMapper.updateEntity(patient, dto, findGender(dto.gender()));
        return patientMapper.toDto(patientRepository.save(patient));
    }
}
