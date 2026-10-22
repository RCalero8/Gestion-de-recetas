package com.example.dam.salesianos.gestion_recetas.controller;

import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditCategoryDto;
import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetCategoryDetail;
import com.example.dam.salesianos.gestion_recetas.service.CategoryService;
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

@Tag(name = "Categorías", description = "CRUD completo de categorías de recetas")
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Listar categorías",
            description = "Devuelve todas las categorías. Si no hay ninguna, devuelve una lista vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de categorías",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GetCategoryDetail.class)),
                            examples = @ExampleObject(name = "lista", value = ApiExamples.CATEGORIAS_LISTA)))
    })
    @GetMapping
    public List<GetCategoryDetail> findAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener una categoría por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetCategoryDetail.class),
                            examples = @ExampleObject(name = "categoria", value = ApiExamples.CATEGORIA))),
            @ApiResponse(responseCode = "404", description = "La categoría no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrada", value = ApiExamples.ERROR_CATEGORIA_NO_ENCONTRADA)))
    })
    @GetMapping("/{id}")
    public GetCategoryDetail findById(
            @Parameter(description = "Id de la categoría", example = "1") @PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Crear una categoría",
            description = "Crea una categoría. El nombre es obligatorio y no puede repetirse.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada",
                    headers = @Header(name = "Location", description = "URL de la categoría creada",
                            schema = @Schema(type = "string", example = "http://localhost:8080/api/v1/categorias/1")),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetCategoryDetail.class),
                            examples = @ExampleObject(name = "categoria", value = ApiExamples.CATEGORIA))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION))),
            @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "conflicto", value = ApiExamples.ERROR_CONFLICTO)))
    })
    @PostMapping
    public ResponseEntity<GetCategoryDetail> create(@Valid @RequestBody EditCategoryDto request) {
        GetCategoryDetail creada = service.addCategory(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @Operation(summary = "Actualizar una categoría",
            description = "Reemplaza el nombre y la descripción de una categoría existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetCategoryDetail.class),
                            examples = @ExampleObject(name = "categoria", value = ApiExamples.CATEGORIA))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "validacion", value = ApiExamples.ERROR_VALIDACION))),
            @ApiResponse(responseCode = "404", description = "La categoría no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrada", value = ApiExamples.ERROR_CATEGORIA_NO_ENCONTRADA))),
            @ApiResponse(responseCode = "409", description = "Ya existe otra categoría con ese nombre",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "conflicto", value = ApiExamples.ERROR_CONFLICTO)))
    })
    @PutMapping("/{id}")
    public GetCategoryDetail update(
            @Parameter(description = "Id de la categoría", example = "1") @PathVariable Long id,
            @Valid @RequestBody EditCategoryDto request) {
        return service.updateCategory(id, request);
    }

    @Operation(summary = "Borrar una categoría",
            description = "Borra una categoría. No se puede borrar si tiene recetas asociadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría borrada (sin contenido)"),
            @ApiResponse(responseCode = "404", description = "La categoría no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "noEncontrada", value = ApiExamples.ERROR_CATEGORIA_NO_ENCONTRADA))),
            @ApiResponse(responseCode = "409", description = "La categoría tiene recetas asociadas",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "conflicto", value = ApiExamples.ERROR_CONFLICTO)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id de la categoría", example = "1") @PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
