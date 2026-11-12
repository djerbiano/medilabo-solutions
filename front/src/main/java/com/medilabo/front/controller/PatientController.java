package com.medilabo.front.controller;

import com.medilabo.front.client.PatientClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
