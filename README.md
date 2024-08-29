# ConstructionXpert Services

## Description
ConstructionXpert Services est une application de gestion de projets de construction, développée pour l'entreprise "ConstructionXpert Services". L'application est construite en utilisant une architecture microservices pour assurer une meilleure évolutivité, maintenabilité, et résilience. Chaque microservice gère une partie spécifique des fonctionnalités de l'application, avec une communication via des API RESTful sécurisées.

## Table des matières
- [Description](#description)
- [Architecture](#architecture)
- [Microservices](#microservices)
  - [Service de Gestion des Projets](#service-de-gestion-des-projets)
  - [Service de Gestion des Tâches](#service-de-gestion-des-tâches)
  - [Service de Gestion des Ressources](#service-de-gestion-des-ressources)
- [Communication entre les Services](#communication-entre-les-services)
- [Base de Données](#base-de-données)
- [Gestion des Migrations](#gestion-des-migrations)
- [Démarrage](#démarrage)
- [Technologies Utilisées](#technologies-utilisées)
- [Contact](#contact)

## Architecture
L'application utilise une architecture microservices, où chaque service est isolé dans son propre module Maven et gère ses propres fonctionnalités ainsi que sa base de données. 

## Microservices

### Service de Gestion des Projets
Ce service permet de gérer les projets de construction avec les fonctionnalités suivantes :
- **Créer un projet** avec un nom, une description, une date de début, une date de fin, et un budget.
- **Afficher la liste des projets** existants avec leurs détails.
- **Mettre à jour les détails d'un projet** existant.
- **Supprimer un projet** existant.

### Service de Gestion des Tâches
Ce service gère les tâches associées à chaque projet :
- **Créer une nouvelle tâche** en spécifiant sa description, sa date de début, sa date de fin, son statut (à faire, en cours, terminé) et les ressources nécessaires.
- **Afficher la liste des tâches** associées à un projet avec leurs détails.
- **Mettre à jour les détails d'une tâche** existante.
- **Supprimer une tâche** existante.

### Service de Gestion des Ressources
Ce service gère les ressources nécessaires aux projets :
- **Ajouter de nouvelles ressources** avec leurs détails tels que le nom, le type, la quantité, et les informations du fournisseur.
- **Afficher la liste des ressources** disponibles avec leurs détails.
- **Mettre à jour les détails d'une ressource** existante.
- **Supprimer une ressource** existante.

## Communication entre les Services
Les microservices communiquent entre eux en utilisant le pattern client HTTP via `RestTemplate`. Chaque service peut envoyer des requêtes HTTP (GET, POST, PUT, DELETE) à un autre service pour récupérer ou modifier des données.

### Exemple de Communication
Par exemple, le **Service de Gestion des Tâches** peut avoir besoin de vérifier la disponibilité d'une ressource avant d'assigner une tâche. Pour ce faire, il enverra une requête au **Service de Gestion des Ressources** pour obtenir les informations nécessaires.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {

    @Autowired
    private RestTemplate restTemplate;

    public Resource getResourceDetails(Long resourceId) {
        String resourceServiceUrl = "http://resource-service/api/resources/" + resourceId;
        return restTemplate.getForObject(resourceServiceUrl, Resource.class);
    }
}
```

### Configuration de RestTemplate
Dans chaque service qui nécessite une communication avec d'autres microservices, `RestTemplate` est configuré en tant que bean Spring :

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

Cette configuration permet à Spring de gérer l'injection de dépendances et d'utiliser `RestTemplate` dans les services pour effectuer des appels inter-services.

### Utilisation de Eureka pour la Résolution de Noms
Grâce à l'intégration avec `Spring Cloud Netflix Eureka`, les services peuvent se découvrir mutuellement par leur nom. Par exemple, dans l'exemple précédent, resource-service est le nom logique du service de gestion des ressources enregistré dans Eureka. `RestTemplate` utilisera ce nom pour résoudre l'adresse IP et le port corrects du service.

```java
String resourceServiceUrl = "http://resource-service/api/resources/" + resourceId;
```

Cela simplifie la communication entre les microservices en évitant de hardcoder les adresses des services, ce qui est particulièrement utile dans des environnements dynamiques tels que le cloud.

## Base de Données
Chaque microservice dispose de sa propre base de données, assurant ainsi une isolation complète des données et facilitant les migrations et évolutions indépendantes.

## Gestion des Migrations
Le projet utilise Flyway pour gérer les migrations de schéma de la base de données. Chaque changement de schéma est versionné et appliqué de manière ordonnée lors des déploiements.

## Démarrage
### Prérequis

Java 22
Maven 3.8+
MySQL 8.0+

### Installation
1. Clonez le dépôt :
   
```bash
git clone https://github.com/YassineOularbi/CONSTRUCTXPERT-MICROSERVICES.git
cd CONSTRUCTXPERT-MICROSERVICES
```

2. Lancez les services microservices :

```bash
mvn clean install
mvn spring-boot:run -pl eureka-server
mvn spring-boot:run -pl project-service
mvn spring-boot:run -pl task-service
mvn spring-boot:run -pl resource-service
```

3. Accédez à l'application via les points d'entrée API.

## Technologies Utilisées

- **Spring Boot 3.3.3**
- **Spring Cloud Netflix Eureka 2023.0.3**
- **Spring Data JPA**
- **Flyway 9.14.1**
- **MySQL 8.0.30**
- **MapStruct 1.5.5.Final**
- **Lombok 1.18.32**
- **RESTful APIs** (via `RestTemplate` pour la communication inter-services)

## Contact

Pour toute question ou information supplémentaire, vous pouvez me contacter à l'adresse suivante :

- **Email :** [yassineoularbi4@gmail.com](mailto:yassineoularbi4@gmail.com)
- **LinkedIn :** [@YassineOularbi](www.linkedin.com/in/yassine-oularbi-79730424b)




