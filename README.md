# MédiLabo Solutions

Application en microservices d'aide à la détection du risque de diabète de type 2 chez les patients.

## Architecture

| Service              | Rôle                                         | Base de données | Port | État     |
|----------------------|----------------------------------------------|-----------------|------|----------|
| `patient-service`    | Gestion des informations personnelles        | MySQL           | 8081 | En cours |
| `notes-service`      | Notes des médecins sur les patients          | MongoDB         | 8082 | À venir  |
| `assessment-service` | Évaluation du risque de diabète              | Aucune          | 8083 | À venir  |
| `gateway`            | Point d'entrée unique et sécurité            | Aucune          | 8080 | À venir  |
| `front`              | Interface utilisateur                        | Aucune          | -    | À venir  |

Stack : Java 21, Spring Boot 4.1.2, Maven.

## Lancer le patient-service en local

### Prérequis

- Java 21
- MySQL 8

### 1. Créer la base et l'utilisateur

Dans MySQL (Workbench ou `mysql -u root -p`) :

```sql
CREATE DATABASE medilabo_patient
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'medilabo_patient'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON medilabo_patient.* TO 'medilabo_patient'@'localhost';
FLUSH PRIVILEGES;
```

`utf8mb4` permet de stocker correctement tous les caractères (accents, etc.).

### 2. Configurer les variables d'environnement

Copier `patient-service/.env.example` en `patient-service/.env`, puis renseigner le mot de passe choisi à l'étape précédente.

Le fichier `.env` contient des secrets : il est ignoré par Git et ne doit jamais être commité.

### 3. Démarrer

```bash
cd patient-service
./mvnw spring-boot:run
```

Le service écoute sur le port **8081**.

Au démarrage, les tables (`schema.sql`) et les 4 patients de test du sujet (`data.sql`) sont créés automatiquement. Les scripts peuvent être relancés sans risque : les tables existantes et les patients déjà présents sont conservés, sans doublon.
