# API REST - Gestion de Recetas y Categorias

API RESTful desarrollada en Spring Boot para la gestion completa de recetas de cocina, sus categorias correspondientes e ingredientes asociados, permitiendo establecer relaciones Many-to-Many personalizadas y validaciones avanzadas de negocio.

## Arquitectura del Proyecto

El proyecto sigue un patron de Arquitectura en Capas (Layered Architecture), promoviendo la separacion de responsabilidades, alta mantenibilidad y desacoplamiento mediante DTOs (Data Transfer Objects).

```
com.example.dam.salesianos.gestion_recetas
├── config/          --> Configuraciones del framework (Validacion, I18n)
├── controller/      --> Capa de Controladores REST (Endpoints & Swagger)
├── docs/            --> Ejemplos y documentacion OpenApi / Swagger
├── dto/             --> Objetos de Transferencia de Datos (Request/Response)
│   ├── Edit/        --> DTOs para solicitudes de creacion/modificacion
│   └── Getter/      --> DTOs para respuestas formateadas y anidadas
├── error/           --> Manejo global de excepciones (ProblemDetail) y excepciones custom
│   └── notFound/    --> Excepciones especificas de recursos no encontrados
├── model/           --> Entidades JPA (Persistencia de datos)
├── repo/            --> Repositorios de datos (Spring Data JPA)
├── service/         --> Logica de negocio de la aplicacion
├── utils/           --> Componentes auxiliares (DataSeed / Semillero de datos)
└── validation/      --> Anotaciones y Validadores personalizados de Bean Validation
```

## Modelo de Datos (Entidades)

El modelo de dominio esta compuesto por tres entidades principales y una tabla de asociacion interconectadas mediante JPA:

1. **Category (Categoria):**
   * id (Long, PK Autogenerado)
   * nombre (String, Unico, NotBlank)
   * descripcion (String)
   * Relacion: Una Categoria tiene muchas Recetas (1:N).

2. **Receta (Receta):**
   * id (Long, PK Autogenerado)
   * nombre (String, Unico, NotBlank)
   * tiempoPreparacionMin (Integer, Mayor a 0)
   * dificultad (Enum: FACIL, MEDIA, DIFICIL)
   * categoria (Category, FK)
   * Relacion: Una Receta contiene multiples Ingredientes a traves de RecetaIngrediente (N:M).

3. **Ingredient (Ingrediente):**
   * id (Long, PK Autogenerado)
   * nombre (String, Unico, NotBlank)

4. **RecetaIngrediente (Relacion Intermedia):**
   * Mapea la relacion Many-to-Many entre Receta e Ingredient.
   * Permite agregar atributos contextuales como cantidad y unidadMedida (ej: 200g, 3 cucharadas).

## Endpoints Principales

### Categorias (/api/v1/categorias)

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | /api/v1/categorias | Listar todas las categorias |
| GET | /api/v1/categorias/{id} | Obtener detalle de una categoria por ID |
| POST | /api/v1/categorias | Crear una nueva categoria |
| PUT | /api/v1/categorias/{id} | Actualizar una categoria existente |
| DELETE | /api/v1/categorias/{id} | Eliminar una categoria |

### Ingredientes (/api/v1/ingredientes)

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | /api/v1/ingredientes | Listar todos los ingredientes |
| GET | /api/v1/ingredientes/{id} | Obtener detalle de un ingrediente |
| POST | /api/v1/ingredientes | Crear un nuevo ingrediente |
| DELETE | /api/v1/ingredientes/{id} | Eliminar un ingrediente |

### Recetas (/api/v1/recetas)

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | /api/v1/recetas | Listar todas las recetas |
| GET | /api/v1/recetas/{id} | Obtener receta por ID (incluye DTO anidado) |
| POST | /api/v1/recetas | Crear receta (requiere ID de categoria existente) |
| PUT | /api/v1/recetas/{id} | Actualizar una receta |
| DELETE | /api/v1/recetas/{id} | Eliminar receta |
| POST | /api/v1/recetas/{recetaId}/ingredientes | Anadir un ingrediente a una receta existente |

## Validaciones y Gestion de Errores

El proyecto utiliza un GlobalExceptionHandler centralizado basado en el estandar RFC 7807 (ProblemDetail) para brindar respuestas claras y estructuradas ante errores:

* **Validacion personalizada:** `@NombreRecetaUnico` garantiza que no se creen o editen recetas con un nombre duplicado.
* **TiempoInvalidoException (400 Bad Request):** Se lanza si el tiempo de preparacion expresado en minutos es menor o igual a cero.
* **IngredienteYaAnidadoException (400 / 409 Conflict):** Se lanza al intentar agregar un ingrediente que ya esta asociado a la receta.
* **CategoryNotFoundException / RecetaNotFoundException / IngredientNotFoundException (404 Not Found):** Producidas cuando no existe el recurso buscado.
* **Internacionalizacion (i18n):** Los mensajes de error y validacion estan externalizados en archivos `.properties` (soporte multi-idioma ES/EN).

## Requisitos Previos y Ejecucion

### Requisitos:
* Java SDK: 17 o superior.
* Maven: 3.8+ (o utilizar el wrapper `./mvnw` incluido).

### Pasos para ejecutar la aplicacion:

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/gestion-recetas.git
   cd gestion-recetas
   ```

2. Compilar e instalar dependencias:
   ```bash
   ./mvnw clean compile
   ```

3. Iniciar la aplicacion:
   ```bash
   ./mvnw spring-boot:run
   ```
   La aplicacion iniciara en `http://localhost:8080` por defecto.

## Documentacion OpenAPI & Swagger UI

Una vez en ejecucion la aplicacion, la documentacion interactiva OpenAPI 3.0 estara disponible en las siguientes URLs:

* Swagger UI: http://localhost:8080/swagger-ui.html
* Especificacion OpenAPI (JSON): http://localhost:8080/v3/api-docs

## Pruebas con Postman

Se incluye en el repositorio la coleccion exportada de Postman para probar todos los casos de uso:

* Archivo: `Gestion_Recetas.postman_collection.json` (ubicado en la raiz).