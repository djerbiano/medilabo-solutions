package com.medilabo.front.controller;

import com.medilabo.front.client.PatientClient;
import com.medilabo.front.dto.PatientDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class PatientController {
    private final PatientClient patientClient;

    public PatientController(PatientClient patientClient) {
        this.patientClient = patientClient;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String list(Model model) {
        model.addAttribute("patients", patientClient.findAll());
        return "patients/list";
    }

    @GetMapping("/patients/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new PatientDto(null, null, null, null, null, null, null));
        return "patients/form";
    }

    @PostMapping("/patients")
    public String create(@Valid @ModelAttribute("patient") PatientDto patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "patients/form";
        }
        try {
            patientClient.create(patient);
        } catch (HttpClientErrorException.Conflict e) {
            bindingResult.reject("patient.duplicate",
                    "Ce patient existe déjà (même prénom, nom et date de naissance)");
            return "patients/form";
        }
        return "redirect:/patients";
    }
}
