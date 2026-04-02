# 🚀 Héliodyssée

**Héliodyssée** est une application web de réservation de vols spatiaux, développée en Java avec Spring Boot. Elle permet aux utilisateurs de rechercher des vols interplanétaires, de gérer leurs réservations et de procéder au paiement en ligne.

---

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Stack technique](#-stack-technique)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation et démarrage](#-installation-et-démarrage)
- [Variables d'environnement](#-variables-denvironnement)
- [Base de données](#-base-de-données)
- [Documentation API](#-documentation-api)
- [Structure du projet](#-structure-du-projet)

---

## ✨ Fonctionnalités

- Exploration des planètes et spatioports dans une **vue 3D interactive** (développée from scratch en JS natif)
- Recherche et consultation de vols spatiaux
- Gestion des planètes et des spatioports de départ/arrivée
- Réservation de vols avec génération de facture PDF
- Paiement en ligne via **Stripe**
- Authentification et gestion des utilisateurs (Spring Security)
- Envoi d'e-mails de confirmation
- API REST documentée (Swagger UI)

---

## 🛠 Stack technique

| Technologie | Rôle |
|---|---|
| Java 17 | Langage principal |
| Spring Boot 3.4.5 | Framework applicatif |
| Spring Security | Authentification & autorisation |
| Spring Data JPA | Persistance relationnelle |
| Spring Data MongoDB | Persistance NoSQL |
| MySQL 8 | Base de données relationnelle |
| MongoDB | Base de données NoSQL |
| Liquibase | Gestion des migrations de schéma |
| Stripe | Paiement en ligne |
| OpenPDF | Génération de factures PDF |
| MapStruct | Mapping entités ↔ DTOs |
| Lombok | Réduction du boilerplate Java |
| Thymeleaf + JS natif | Templating HTML & vue 3D interactive |
| SpringDoc / Swagger UI | Documentation de l'API REST |
| Docker / Docker Compose | Conteneurisation |

---

## 🏛 Architecture

Le projet suit une **architecture hexagonale** (Ports & Adapters) :

```
heliodysse/
├── adapter/
│   ├── in/
│   │   └── controller/         # Contrôleurs REST (entrées HTTP)
│   └── out/
│       ├── jpaEntity/          # Entités JPA (MySQL)
│       ├── mongoEntity/        # Entités MongoDB
│       ├── mapper/             # MapStruct : entités ↔ domaine
│       ├── repoImpl/           # Implémentations des ports de sortie
│       └── repository/         # Interfaces Spring Data
├── application/
│   └── service/
│       └── UserApplicationService.java   # Orchestration des cas d'usage
├── domain/
│   ├── model/
│   │   ├── entity/             # Entités métier
│   │   └── enums/              # Énumérations métier
│   ├── port/
│   │   ├── in/                 # Interfaces des cas d'usage (use cases)
│   │   └── out/                # Interfaces des dépôts (repositories)
│   └── service/                # Logique métier
│       ├── payment/            # Logique de paiement Stripe
│       ├── BookingService.java
│       ├── FlightService.java
│       ├── PlanetService.java
│       ├── SpacePortService.java
│       └── UserService.java
└── ServletInitializer.java
```

---

## 📦 Prérequis

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Un compte [Stripe](https://stripe.com/) (clés API)
- Un serveur SMTP (pour l'envoi d'e-mails)

---

## 🚀 Installation et démarrage

### 1. Cloner le dépôt

```bash
git clone https://github.com/cecil-sa-thomas/Heliodyssee.git
cd Heliodyssee
```

### 2. Configurer les credentials

> ⚠️ La configuration des variables d'environnement avec Spring peut varier selon l'IDE. Si vous rencontrez des difficultés, le plus simple reste de renseigner les credentials directement dans `application.properties` — **en veillant à ne jamais committer ce fichier** (vérifiez qu'il est bien dans votre `.gitignore`).

**Windows (PowerShell) :**
```powershell
$env:MYSQL_PASSWORD = "votre_mot_de_passe"
# Ou pour persister la variable après redémarrage de l'IDE :
[Environment]::SetEnvironmentVariable("MYSQL_PASSWORD", "votre_mot_de_passe", "User")
```

> ⚠️ Pensez à redémarrer votre IDE ou votre fenêtre PowerShell après avoir défini les variables.

**Linux :**
```bash
export MYSQL_PASSWORD="votre_mot_de_passe"
```

### 3. Initialiser le projet

Ce script orchestre l'intégralité du lancement en local : démarrage des conteneurs Docker, initialisation de la base de données, migrations Liquibase et lancement de l'application. Il suffit de démarrer Docker, puis d'exécuter le script et de suivre les instructions.

```bash
bash init_db.sh
```

L'application est accessible sur `http://localhost:8080`.

---

## 🔐 Variables d'environnement

| Variable | Description |
|---|---|
| `MYSQL_ROOT_PASSWORD` | Mot de passe root MySQL |
| `MYSQL_DATABASE` | Nom de la base de données |
| `MYSQL_USERNAME` | Utilisateur MySQL |
| `MYSQL_PASSWORD` | Mot de passe utilisateur MySQL |

Les autres secrets (clés Stripe, configuration SMTP, MongoDB URI) sont à configurer dans `src/main/resources/application.properties` ou via des variables d'environnement dédiées.

---

## 🗄 Base de données

L'application utilise deux bases de données :

- **MySQL** — données relationnelles (utilisateurs, réservations, vols, spatioports…)
- **MongoDB** — données non-relationnelles (logs, documents dynamiques…)

Les migrations MySQL sont gérées automatiquement par **Liquibase** au démarrage (fichier maître : `src/main/resources/db/changelog/changelog-master.yml`).

Il est également possible de les exécuter manuellement via le script `init_db.sh` ou directement avec Maven :

```bash
./mvnw liquibase:update -Pmysql
```

---

## 📖 Documentation API

La documentation Swagger UI est accessible après démarrage de l'application, pour tout utilisateur authentifié :

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📁 Structure du projet

```
Heliodyssee/
├── src/
│   └── main/
│       ├── java/fr/thomascecil/heliodysse/   # Code source Java
│       └── resources/
│           ├── db/changelog/                  # Scripts Liquibase
│           ├── application.properties         # Configuration Spring Boot
│           └── liquibase-mysql.properties     # Config Liquibase MySQL
├── docker-compose.yml                         # Infrastructure Docker
├── init_db.sh                                 # Script d'initialisation et de lancement
├── pom.xml                                    # Dépendances Maven
└── mvnw / mvnw.cmd                            # Maven Wrapper
```

---

## 🧪 Note importante pour effectuer des tests

Le jeu de données fourni concernant les utilisateurs a pour seul but de peupler la base de données afin de permettre des tests. Les mots de passe présents en base ne sont pas utilisables pour s'authentifier (ils ne correspondent pas à des hash valides).

La procédure recommandée pour tester l'application est la suivante :

1. Créer un compte via l'interface
2. L'activer en récupérant le lien de confirmation affiché dans le terminal (simulation d'envoi d'e-mail)
3. Utiliser un outil d'administration de base de données (ex : [DBeaver](https://dbeaver.io/)) pour passer manuellement le rôle du compte créé à `ADMIN`

---

## 👤 Auteur

**Thomas Cecil** — [GitHub](https://github.com/cecil-sa-thomas)
