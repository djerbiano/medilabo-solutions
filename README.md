# MédiLabo Solutions

Application en microservices d'aide à la détection du risque de diabète de type 2 chez les patients.

## Architecture

| Service              | Rôle                                         | Base de données | Port | État     |
|----------------------|----------------------------------------------|-----------------|------|----------|
| `patient-service`    | Gestion des informations personnelles        | MySQL           | 8081 | En cours |
| `notes-service`      | Notes des médecins sur les patients          | MongoDB         | 8082 | À venir  |
| `assessment-service` | Évaluation du risque de diabète              | Aucune          | 8083 | À venir  |
| `gateway`            | Point d'entrée unique et sécurité            | Aucune          | 8080 | En cours |
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

Depuis IntelliJ, régler le *Working directory* de la configuration de lancement sur `$MODULE_WORKING_DIR$` : sinon le fichier `.env` n'est pas trouvé.

Au démarrage, les tables (`schema.sql`) et les 4 patients de test du sujet (`data.sql`) sont créés automatiquement. Les scripts peuvent être relancés sans risque : les tables existantes et les patients déjà présents sont conservés, sans doublon.

### API

| Verbe  | URL                  | Rôle                       | Réponse                          |
|--------|----------------------|----------------------------|----------------------------------|
| `GET`  | `/api/patients`      | Liste des patients         | 200                              |
| `GET`  | `/api/patients/{id}` | Informations d'un patient  | 200, 400 ou 404                  |
| `POST` | `/api/patients`      | Ajouter un patient         | 201 ou 400                       |
| `PUT`  | `/api/patients/{id}` | Modifier un patient        | 200, 400 ou 404                  |

Exemple de corps pour `POST` / `PUT` (`address` et `phone` sont facultatifs, `gender` vaut `F` ou `M`) :

```json
{
  "firstName": "Test",
  "lastName": "TestNone",
  "birthDate": "1966-12-31",
  "gender": "F",
  "address": "1 Brookside St",
  "phone": "100-222-3333"
}
```

### Erreurs

Toutes les erreurs sont renvoyées au format standard `ProblemDetail` (RFC 9457, `Content-Type: application/problem+json`) :

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Données invalides",
  "instance": "/api/patients",
  "errors": {
    "lastName": "Le nom est obligatoire",
    "birthDate": "La date de naissance ne peut pas être dans le futur"
  }
}
```

| Code | Cas                                                                                                   |
|------|-------------------------------------------------------------------------------------------------------|
| 400  | Données invalides (détail par champ dans `errors`), genre inconnu, id non numérique, JSON illisible   |
| 404  | Patient introuvable, URL inexistante                                                                  |
| 405  | Méthode HTTP non autorisée (par exemple `DELETE`)                                                     |

## Lancer le gateway en local

Le gateway est le point d'entrée unique de l'application (port **8080**) : il vérifie l'authentification puis transmet chaque requête au bon microservice.

### Prérequis

- Java 21
- `patient-service` démarré (voir ci-dessus)

### 1. Configurer les variables d'environnement

Copier `gateway/.env.example` en `gateway/.env`, puis choisir l'identifiant et le mot de passe de l'utilisateur autorisé.

| Variable              | Rôle                                   | Valeur par défaut       |
|-----------------------|----------------------------------------|-------------------------|
| `GATEWAY_USERNAME`    | Identifiant de l'utilisateur           | Aucune (obligatoire)    |
| `GATEWAY_PASSWORD`    | Mot de passe de l'utilisateur          | Aucune (obligatoire)    |
| `PATIENT_SERVICE_URL` | Adresse du `patient-service`           | `http://localhost:8081` |

### 2. Démarrer

```bash
cd gateway
./mvnw spring-boot:run
```

Depuis IntelliJ, régler le *Working directory* sur `$MODULE_WORKING_DIR$`, comme pour `patient-service`.

### Routes et sécurité

| Chemin             | Service cible     |
|--------------------|-------------------|
| `/api/patients/**` | `patient-service` |

Toutes les requêtes doivent être authentifiées en HTTP Basic, sinon le gateway répond **401** :

```bash
curl -u medecin:votre_mot_de_passe http://localhost:8080/api/patients
```
