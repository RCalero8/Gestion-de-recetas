package com.example.dam.salesianos.gestion_recetas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info =
@Info(description = """
			API REST para gestionar **categorías**, **recetas** e **ingredientes**.

			- Una categoría tiene muchas recetas.
			- Una receta tiene muchos ingredientes y un ingrediente se usa en muchas recetas (M:M), con cantidad y unidad de medida opcionales.
			- Los errores se devuelven en formato **ProblemDetail** (RFC 9457).
			- Los mensajes de error están internacionalizados: envía la cabecera `Accept-Language` con `es` o `en`.
			""",
		version = "1.0",
		contact = @Contact(email = "calero.loroc26@triana.salesianos.edu", name = "Rocio Calero"),
		license = @License(name = "CC BY"),
		title = "Api de Gestión de Recetas"
)
)

@SpringBootApplication
public class GestionRecetasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionRecetasApplication.class, args);
	}

}
