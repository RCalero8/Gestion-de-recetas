package com.example.dam.salesianos.gestion_recetas.controller;


import com.example.dam.salesianos.gestion_recetas.service.RecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Recetas", description = "CRUD completo de recetas y gestión de sus ingredientes (relación M:M)")
@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService service;

    @Operation(summary = "Listar recetas",
            description = "Devuelve todas las recetas con su categoría y sus ingredientes. Si no hay ninguna, devuelve una lista vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de recetas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GetRecetaDetail.class)),
                            examples = @ExampleObject(name = "lista", value = ApiExamples.RECETAS_LISTA)))
    })
    @GetMapping
    public List<GetRecetaDetail> findAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener una receta por id",
            description = "Devuelve la receta incluyendo su categoría y la lista de sus ingredientes (con cantidad y unidad).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetRecetaDetail.class),
                            examples = @ExampleObject(name = "recetaConIngredientes", value = ApiExamples.RECETA_CON_INGREDIENTES))),
            @ApiResponse(responseCode = "404", description = "La receta no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrada", value = ApiExamples.ERROR_RECETA_NO_ENCONTRADA)))
    })
    @GetMapping("/{id}")
    public GetRecetaDetail findById(
            @Parameter(description = "Id de la receta", example = "1") @PathVariable Long id) {
        return service.getRecetaById(id);
    }

    @Operation(summary = "Crear una receta",
            description = "Crea una receta en una categoría que ya existe (se indica con categoriaId). "
                    + "El nombre no puede repetirse y el tiempo de preparación debe ser mayor que cero.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receta creada",
                    headers = @Header(name = "Location", description = "URL de la receta creada",
                            schema = @Schema(type = "string", example = "http://localhost:8080/api/v1/recetas/1")),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetRecetaDetail.class),
                            examples = @ExampleObject(name = "receta", value = ApiExamples.RECETA_SIN_INGREDIENTES))),
            @ApiResponse(responseCode = "400", description = "Datos no válidos, JSON ilegible o tiempo de preparación menor o igual a cero",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION),
                                    @ExampleObject(name = "tiempoInvalido", value = ApiExamples.ERROR_TIEMPO_INVALIDO),
                                    @ExampleObject(name = "jsonIlegible", value = ApiExamples.ERROR_JSON_ILEGIBLE)
                            })),
            @ApiResponse(responseCode = "404", description = "La categoría indicada no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "categoriaNoEncontrada", value = ApiExamples.ERROR_CATEGORIA_NO_ENCONTRADA)))
    })
    @PostMapping
    public ResponseEntity<GetRecetaDetail> create(@Valid @RequestBody EditRecetaDto request) {
        GetRecetaDetail creada = service.addReceta(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar una receta",
            description = "Reemplaza los datos de una receta existente. No modifica sus ingredientes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta actualizada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetRecetaDetail.class),
                            examples = @ExampleObject(name = "receta", value = ApiExamples.RECETA_CON_INGREDIENTES))),
            @ApiResponse(responseCode = "400", description = "Datos no válidos, JSON ilegible o tiempo de preparación menor o igual a cero",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION),
                                    @ExampleObject(name = "tiempoInvalido", value = ApiExamples.ERROR_TIEMPO_INVALIDO)
                            })),
            @ApiResponse(responseCode = "404", description = "La receta o la categoría indicada no existen",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "recetaNoEncontrada", value = ApiExamples.ERROR_RECETA_NO_ENCONTRADA),
                                    @ExampleObject(name = "categoriaNoEncontrada", value = ApiExamples.ERROR_CATEGORIA_NO_ENCONTRADA)
                            }))
    })
    @PutMapping("/{id}")
    public GetRecetaDetail update(
            @Parameter(description = "Id de la receta", example = "1") @PathVariable Long id,
            @Valid @RequestBody EditRecetaDto request) {
        return service.updateReceta(id, request);
    }

    @Operation(summary = "Borrar una receta",
            description = "Borra la receta y sus relaciones con los ingredientes. Los ingredientes y la categoría no se borran.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receta borrada (sin contenido)"),
            @ApiResponse(responseCode = "404", description = "La receta no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrada", value = ApiExamples.ERROR_RECETA_NO_ENCONTRADA)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id de la receta", example = "1") @PathVariable Long id) {
        service.deleteReceta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Añadir un ingrediente a una receta (M:M)",
            description = "Añade a la receta un ingrediente que ya existe, indicando opcionalmente la cantidad y la unidad "
                    + "de medida (por ejemplo 200 gramos o 3 cucharadas soperas). Un ingrediente no puede añadirse dos veces "
                    + "a la misma receta. Devuelve la receta completa con su lista de ingredientes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ingrediente añadido; devuelve la receta actualizada",
                    headers = @Header(name = "Location", description = "URL de la receta",
                            schema = @Schema(type = "string", example = "http://localhost:8080/api/v1/recetas/1")),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetRecetaDetail.class),
                            examples = @ExampleObject(name = "recetaConIngredientes", value = ApiExamples.RECETA_CON_INGREDIENTES))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION))),
            @ApiResponse(responseCode = "404", description = "La receta o el ingrediente no existen",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "recetaNoEncontrada", value = ApiExamples.ERROR_RECETA_NO_ENCONTRADA),
                                    @ExampleObject(name = "ingredienteNoEncontrado", value = ApiExamples.ERROR_INGREDIENTE_NO_ENCONTRADO)
                            })),
            @ApiResponse(responseCode = "409", description = "El ingrediente ya estaba añadido a la receta",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "yaAnadido", value = ApiExamples.ERROR_INGREDIENTE_YA_ANADIDO)))
    })
    @PostMapping("/{recetaId}/ingredientes")
    public ResponseEntity<GetRecetaDetail> addIngrediente(
            @Parameter(description = "Id de la receta", example = "1") @PathVariable Long recetaId,
            @Valid @RequestBody EditRecetaIngredienteDto request) {
        GetRecetaDetail receta = service.addIngrediente(recetaId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/recetas/{id}").buildAndExpand(recetaId).toUri();
        return ResponseEntity.created(location).body(receta);
    }
}

