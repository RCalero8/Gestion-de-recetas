package com.example.dam.salesianos.gestion_recetas.repo;

import com.example.dam.salesianos.gestion_recetas.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepositoy extends JpaRepository<Receta, Long> {
    //Existe el nombre
    boolean existsByNombreIgnoreCase(String nombre);

    //Existe ya esta receta
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
