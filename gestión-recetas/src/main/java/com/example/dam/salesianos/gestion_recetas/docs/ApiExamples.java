package com.example.dam.salesianos.gestion_recetas.docs;

public final class ApiExamples {

    private ApiExamples() {
    }

    // Respuestas correctas

    public static final String CATEGORIA = """
            {
              "id": 1,
              "nombre": "Postres",
              "descripcion": "Dulces y repostería"
            }
            """;

    public static final String CATEGORIAS_LISTA = """
            [
              { "id": 1, "nombre": "Postres", "descripcion": "Dulces y repostería" },
              { "id": 2, "nombre": "Arroces", "descripcion": "Platos de arroz" }
            ]
            """;

    public static final String INGREDIENTE = """
            {
              "id": 1,
              "nombre": "Harina"
            }
            """;

    public static final String INGREDIENTES_LISTA = """
            [
              { "id": 1, "nombre": "Harina" },
              { "id": 2, "nombre": "Huevos" }
            ]
            """;

    public static final String RECETA_SIN_INGREDIENTES = """
            {
              "id": 1,
              "nombre": "Paella valenciana",
              "tiempoPreparacionMin": 45,
              "dificultad": "MEDIA",
              "categoria": { "id": 2, "nombre": "Arroces", "descripcion": "Platos de arroz" },
              "ingredientes": []
            }
            """;

    public static final String RECETA_CON_INGREDIENTES = """
            {
              "id": 1,
              "nombre": "Paella valenciana",
              "tiempoPreparacionMin": 45,
              "dificultad": "MEDIA",
              "categoria": { "id": 2, "nombre": "Arroces", "descripcion": "Platos de arroz" },
              "ingredientes": [
                { "ingrediente": { "id": 1, "nombre": "Arroz" }, "cantidad": 400, "unidad": "gramos" },
                { "ingrediente": { "id": 2, "nombre": "Aceite de oliva" }, "cantidad": 3, "unidad": "cucharadas soperas" }
              ]
            }
            """;

    public static final String RECETAS_LISTA = """
            [
              {
                "id": 1,
                "nombre": "Paella valenciana",
                "tiempoPreparacionMin": 45,
                "dificultad": "MEDIA",
                "categoria": { "id": 2, "nombre": "Arroces", "descripcion": "Platos de arroz" },
                "ingredientes": [
                  { "ingrediente": { "id": 1, "nombre": "Arroz" }, "cantidad": 400, "unidad": "gramos" }
                ]
              }
            ]
            """;

    // Errores

    public static final String ERROR_CATEGORIA_NO_ENCONTRADA = """
            {
              "type": "about:blank",
              "title": "Categoría no encontrada",
              "status": 404,
              "detail": "No se encontró la categoría con id 99",
              "instance": "/api/v1/categorias/99"
            }
            """;

    public static final String ERROR_RECETA_NO_ENCONTRADA = """
            {
              "type": "about:blank",
              "title": "Receta no encontrada",
              "status": 404,
              "detail": "No se encontró la receta con id 99",
              "instance": "/api/v1/recetas/99"
            }
            """;

    public static final String ERROR_INGREDIENTE_NO_ENCONTRADO = """
            {
              "type": "about:blank",
              "title": "Ingrediente no encontrado",
              "status": 404,
              "detail": "No se encontró el ingrediente con id 99",
              "instance": "/api/v1/recetas/1/ingredientes"
            }
            """;

    public static final String ERROR_VALIDACION = """
            {
              "type": "about:blank",
              "title": "Error de validación",
              "status": 400,
              "detail": "Hay uno o más campos con valores no válidos",
              "instance": "/api/v1/recetas",
              "invalid-params": [
                {
                  "object": "editRecetaDto",
                  "message": "El nombre de la receta es obligatorio",
                  "field": "nombre",
                  "rejectedValue": ""
                }
              ]
            }
            """;

    public static final String ERROR_TIEMPO_INVALIDO = """
            {
              "type": "about:blank",
              "title": "Tiempo de preparación inválido",
              "status": 400,
              "detail": "El tiempo de preparación debe ser mayor que cero, pero se recibió -10",
              "instance": "/api/v1/recetas"
            }
            """;

    public static final String ERROR_JSON_ILEGIBLE = """
            {
              "type": "about:blank",
              "title": "Petición no legible",
              "status": 400,
              "detail": "El cuerpo de la petición no es un JSON válido o contiene valores no permitidos, por ejemplo una dificultad desconocida",
              "instance": "/api/v1/recetas"
            }
            """;

    public static final String ERROR_INGREDIENTE_YA_ANADIDO = """
            {
              "type": "about:blank",
              "title": "Ingrediente ya añadido",
              "status": 409,
              "detail": "El ingrediente Arroz ya forma parte de la receta Paella valenciana",
              "instance": "/api/v1/recetas/1/ingredientes"
            }
            """;

    public static final String ERROR_CONFLICTO = """
            {
              "type": "about:blank",
              "title": "Conflicto con los datos existentes",
              "status": 409,
              "detail": "No se puede completar la operación: ya existe un registro con ese nombre o el recurso está siendo utilizado por otros datos",
              "instance": "/api/v1/categorias"
            }
            """;
}
