# Proyecto_IW

## Integrantes

- Joel Choquepata
- Joaquin Torres
- Sebastian David
- Franco Rojas

##  Carpeta .devcontainer

- La carpeta .devcontainer la usamos porque estamos trabajando en el codespace de github y y por cosas de la página tenemos que borrar el codespace y crearlo de nuevo cada vez que trabajemos en el y para no tener que reinstalar siempre el jdk 25 en el codespace usamos la esta carpeta que lo instala apenas se crea el codespace.

## Carpeta Collectiverse

- Esta carpeta contiene el entorno virtual de spring que usamos entidades que seran nuestras tablas para el modelo de datos y con su repositorio.

```
src/
├── main/
│   ├── java/
│   │   └── com/collectiverse/collectiverse/
│   │       ├── CollectiverseApplication.java      # Clase principal (Spring Boot)
│   │       └── dominio/
│   │           ├── entity/                        # Entidades del modelo de datos
│   │           │   ├── Category.java
│   │           │   ├── CollectionEntity.java
│   │           │   ├── Favorite.java
│   │           │   ├── ProductImage.java
│   │           │   ├── Product.java
│   │           │   ├── PublicationExchange.java
│   │           │   ├── Publication.java
│   │           │   ├── Rating.java
│   │           │   └── Report.java
│   │           └── repository/                    # Repositorios (acceso a datos)
│   │               ├── RepoCategory.java
│   │               ├── RepoCollection.java
│   │               ├── RepoFavorite.java
│   │               ├── RepoProductImage.java
│   │               ├── RepoProduct.java
│   │               ├── RepoPublicationExchange.java
│   │               ├── RepoPublication.java
│   │               ├── RepoRating.java
│   │               └── RepoReport.java
│   └── resources/
│       └── application.properties                 # Configuración de la aplicación
└── test/
    └── java/
        └── com/collectiverse/collectiverse/
            └── CollectiverseApplicationTests.java 
```
