# GLOW Menu Service

The GLOW Menu Service is a backend microservice that manages restaurant menus within the GLOW platform. It is independently deployable and forms part of the platform's distributed microservice architecture.

Each menu item belongs to a restaurant and has a name, description, price, list of ingredients and a food category (burger, pizza, pasta, salad, sushi, dessert, drink, side or other). The service exposes REST endpoints to create, update and delete menu items and to list a restaurant's full menu. For bulk access, other services can page through menu item IDs, optionally filtered by restaurant, and then fetch the matching items in a single request.

The service is built with **Java 25** and **Quarkus**, using **PostgreSQL** for persistence and **Liquibase** for database migrations.

Technologies:
* Java 25 & Quarkus
* PostgreSQL & Liquibase
* REST API
* OpenID Connect (OIDC)
* MapStruct
* Kubernetes
* Gradle
