package com.example.dam.salesianos.gestion_recetas.controller;

import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditIngredientDto;
import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetIngredientDetail;
import com.example.dam.salesianos.gestion_recetas.service.IngredientService;
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

@Tag(name = "Ingredientes", description = "CRUD básico de ingredientes (crear y listar)")
@RestController
@RequestMapping("/api/v1/ingredientes")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService service;

    @Operation(summary = "Listar ingredientes",
            description = "Devuelve todos los ingredientes. Si no hay ninguno, devuelve una lista vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de ingredientes",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GetIngredientDetail.class)),
                            examples = @ExampleObject(name = "lista", value = ApiExamples.INGREDIENTES_LISTA)))
    })
    @GetMapping
    public List<GetIngredientDetail> findAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener un ingrediente por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingrediente encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetIngredientDetail.class),
                            examples = @ExampleObject(name = "ingrediente", value = ApiExamples.INGREDIENTE))),
            @ApiResponse(responseCode = "404", description = "El ingrediente no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrado", value = ApiExamples.ERROR_INGREDIENTE_NO_ENCONTRADO)))
    })
    @GetMapping("/{id}")
    public GetIngredientDetail findById(
            @Parameter(description = "Id del ingrediente", example = "1") @PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Crear un ingrediente",
            description = "Crea un ingrediente. El nombre es obligatorio y no puede repetirse.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ingrediente creado",
                    headers = @Header(name = "Location", description = "URL del ingrediente creado",
                            schema = @Schema(type = "string", example = "http://localhost:8080/api/v1/ingredientes/1")),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetIngredientDetail.class),
                            examples = @ExampleObject(name = "ingrediente", value = ApiExamples.INGREDIENTE))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION))),
            @ApiResponse(responseCode = "409", description = "Ya existe un ingrediente con ese nombre",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "conflicto", value = ApiExamples.ERROR_CONFLICTO)))
    })
    @PostMapping
    public ResponseEntity<GetIngredientDetail> create(@Valid @RequestBody EditIngredientDto request) {
        GetIngredientDetail creado = service.addIngredient(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }
}

