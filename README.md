
# 🌿 Air Quality Monitoring API – Coding Challenge

Ce POC est une API REST développée en Java avec Spring et permettant de recevoir, stocker, interroger et agréger des mesures de capteurs de qualité de l'air.

## 📌 Objectifs Fonctionnels

L'application permet de :

- 📥 Recevoir de nouvelles **mesures** envoyées par des **capteurs**.
- 🔍 Interroger les mesures :
  - Par un ou plusieurs capteurs.
  - En sélectionnant les **métriques** souhaitées (ex : CO₂, PM2.5).
  - En spécifiant une **statistique** par métrique : `min`, `max`, `sum`, `avg`.
  - En définissant une **plage de dates** (`from`, `to`). Si aucune date n’est fournie, la mesure la plus récente est utilisée.

### 🧪 Exemples de requêtes

- 📄 *Donne-moi la mesure de Benzène pour le capteur 1*
- 📊 *Donne-moi la moyenne de CO₂ et PM2.5 pour le capteur 2 durant la dernière semaine*

---

## ⚙️ Technologies utilisées

- Java 17
- Spring Boot
- Spring Data JPA
- H2
- Maven
- JUnit / Mockito

---

## 🔧 Endpoints REST principaux

L'application sera disponible sur : `http://localhost:8080`

### 📥 Ajouter une mesure

`POST /measurements`

```json
{
  "sensorId": 1,
  "capturedOn": "2025-04-14T16:30:00",
  "metrics": [
    { "name": "CO2", "value": 12.4 },
    { "name": "PM2.5", "value": 30.0 }
  ]
}
```

---

### 🔍 Requête simple

`GET /measurements?sensorIds=1,2&metrics=CO2,PM2.5`

> Retourne les dernières mesures de chaque capteur pour les métriques demandées.

---

### 📊 Requête avec statistiques et plages de dates

`GET /measurements?sensorIds=1&metrics=CO2,PM2.5&stat=avg&from=2025-04-01T00:00:00&to=2025-04-10T23:59:59`

> Retourne les statistiques agrégées (avg/min/max/sum) pour les métriques demandées entre deux dates.

---

## 🛑 Gestion des erreurs

- `404 NOT FOUND` : Capteur non trouvé
- `400 BAD REQUEST` : Valeurs invalides

---

## 🗂️ Notes techniques & Choix

- 📦 Le modèle de données suit une structure : `Sensor -> Measurement -> Metric`
- 📐 La logique métier (filtrage, agrégation) est centralisée dans le service `MeasurementServiceImpl`
- 🔄 La validation est gérée par des annotations (ex: `@NotNull`) + un `@RestControllerAdvice`
- 🧪 Des tests unitaires sont disponibles pour valider les comportements (Mock + JUnit)

---
## 👨‍💻 Auteur

Challege proposé par : Ticatag.

Développé par Soufiane CHAIEB.

Lien vers le repo GitHub : [github.com/ton-utilisateur/air-quality-api](https://github.com/soufChb/air-quality-app)


