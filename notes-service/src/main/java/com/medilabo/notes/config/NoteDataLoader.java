package com.medilabo.notes.config;

import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

// si la collection est vide, charge les notes de test au démarrage
@Component
@RequiredArgsConstructor
public class NoteDataLoader implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Override
    public void run(String... args) {
        if (noteRepository.count() > 0) {
            return;
        }

        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 21, 0);

        List<Note> notes = List.of(
                new Note(1L, "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé", start),
                new Note(2L, "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement", start.plusDays(1)),
                new Note(2L, "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale", start.plusDays(2)),
                new Note(3L, "Le patient déclare qu'il fume depuis peu", start.plusDays(3)),
                new Note(3L, "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", start.plusDays(4)),
                new Note(4L, "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments", start.plusDays(5)),
                new Note(4L, "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", start.plusDays(6)),
                new Note(4L, "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé", start.plusDays(7)),
                new Note(4L, "Taille, Poids, Cholestérol, Vertige et Réaction", start.plusDays(8))
        );

        noteRepository.saveAll(notes);
    }
}
