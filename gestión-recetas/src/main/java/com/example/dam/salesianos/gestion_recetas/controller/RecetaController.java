package com.example.dam.salesianos.gestion_recetas.controller;

import com.example.dam.salesianos.gestion_recetas.dto.RecetaRequest;
import com.example.dam.salesianos.gestion_recetas.dto.RecetaResponse;
import com.example.dam.salesianos.gestion_recetas.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {
    private final RecetaService service;

    @GetMapping
    public List<RecetaResponse> findAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RecetaResponse findById(@PathVariable Long id) {
        return service.getRecetaById(id);
    }

    @PostMapping
    public ResponseEntity<RecetaResponse> create(@Valid @RequestBody RecetaRequest request) {
        RecetaResponse creada = service.addReceta(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RecetaRequest request) {
        RecetaResponse actualizada = service.updateReceta(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReceta(id);
        return ResponseEntity.noContent().build();
    }
}
