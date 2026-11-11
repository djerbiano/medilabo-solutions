package com.medilabo.patient.repository;

import com.medilabo.patient.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByCode(Character code);
}
