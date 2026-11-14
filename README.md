# MédiLabo Solutions

Application en microservices d'aide à la détection du risque de diabète de type 2 chez les patients.

## Architecture

| Service              | Rôle                                         | Base de données | Port | État     |
|----------------------|----------------------------------------------|-----------------|------|----------|
| `patient-service`    | Gestion des informations personnelles        | MySQL           | 8081 | En cours |
| `notes-service`      | Notes des médecins sur les patients          | MongoDB         | 8082 | À venir  |
| `assessment-service` | Évaluation du risque de diabète              | Aucune          | 8083 | À venir  |
| `gateway`            | Point d'entrée unique et sécurité            | Aucune          | 8080 | En cours |
| `front`              | Interface utilisateur                        | Aucune          | 8084 | En cours |

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
| `POST` | `/api/patients`      | Ajouter un patient         | 201, 400 ou 409                  |
| `PUT`  | `/api/patients/{id}` | Modifier un patient        | 200, 400, 404 ou 409             |

Un patient est identifié par son prénom, son nom et sa date de naissance : deux patients ne peuvent pas avoir les trois identiques (contrainte `UNIQUE` en base, réponse 409).

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
| 409  | Patient déjà existant : même prénom, nom et date de naissance (majuscules et accents ignorés)         |

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

### Temps d'attente

Le gateway n'attend pas indéfiniment les services : 2 s maximum pour se connecter, 5 s maximum pour recevoir la réponse (`connect-timeout` et `response-timeout` dans `application.properties`). Au-delà, il répond **504 Gateway Timeout**.

## Lancer le front en local

Le front affiche les pages HTML (Thymeleaf) et appelle uniquement le gateway.

### Prérequis

- Java 21
- `patient-service` et `gateway` démarrés (voir ci-dessus)

### 1. Configurer les variables d'environnement

Copier `front/.env.example` en `front/.env`, puis renseigner les mêmes identifiants que dans `gateway/.env`.

| Variable           | Rôle                                | Valeur par défaut       |
|--------------------|-------------------------------------|-------------------------|
| `GATEWAY_URL`      | Adresse du gateway                  | `http://localhost:8080` |
| `GATEWAY_USERNAME` | Identifiant envoyé au gateway       | Aucune (obligatoire)    |
| `GATEWAY_PASSWORD` | Mot de passe envoyé au gateway      | Aucune (obligatoire)    |

### 2. Démarrer

```bash
cd front
./mvnw spring-boot:run
```

Le front est accessible sur http://localhost:8084. Depuis IntelliJ, régler le *Working directory* sur `$MODULE_WORKING_DIR$`.

### Pages

| URL                   | Rôle                                                            |
|-----------------------|-----------------------------------------------------------------|
| `/`                   | Redirige vers la liste des patients                             |
| `/patients`           | Liste des patients (lien « Modifier » sur chaque ligne)         |
| `/patients/new`       | Formulaire d'ajout d'un patient                                 |
| `/patients/{id}/edit` | Formulaire de modification d'un patient (404 s'il n'existe pas) |

### Pannes et temps d'attente

Le front n'attend pas indéfiniment le gateway : 2 s maximum pour se connecter, 10 s maximum pour recevoir la réponse (`gateway.connect-timeout` et `gateway.read-timeout` dans `application.properties`).

Si le gateway est arrêté, trop lent ou renvoie une erreur 5xx (par exemple parce que `patient-service` est arrêté), le front affiche la page « Service momentanément indisponible » (code **503**) au lieu d'une erreur 500.
