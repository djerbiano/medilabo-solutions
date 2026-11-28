package com.medilabo.front.controller;

import com.medilabo.front.client.NoteClient;
import com.medilabo.front.client.PatientClient;
import com.medilabo.front.dto.PatientDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PatientController {
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientClient patientClient;
    private final NoteClient noteClient;

    public PatientController(PatientClient patientClient, NoteClient noteClient) {
        this.patientClient = patientClient;
        this.noteClient = noteClient;
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

    @GetMapping("/patients/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("patient", patientClient.findById(id));
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            model.addAttribute("notes", noteClient.findByPatId(id));
        } catch (HttpServerErrorException | ResourceAccessException e) {
            // le notes-service ne répond pas : on affiche quand même la fiche, avec un message à la place des notes
            log.warn("Notes indisponibles pour le patient {} : {}", id, e.getMessage());
            model.addAttribute("notesUnavailable", true);
        }
        return "patients/details";
    }

    @GetMapping("/patients/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("patient", patientClient.findById(id));
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "patients/form";
    }

    @PostMapping("/patients/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patient") PatientDto patient,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "patients/form";
        }
        try {
            patientClient.update(id, patient);
        } catch (HttpClientErrorException.Conflict e) {
            bindingResult.reject("patient.duplicate",
                    "Ce patient existe déjà (même prénom, nom et date de naissance)");
            return "patients/form";
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/patients";
    }
}
