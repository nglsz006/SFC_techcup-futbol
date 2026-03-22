package edu.dosw.project.SFC_TechUp_Futbol;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup Fútbol API")
                        .description("""
                                Plataforma web para la gestión integral del torneo interno de fútbol de los programas
                                de Ingeniería de Sistemas, IA, Ciberseguridad y Estadística.

                                **Actores del sistema:**
                                - **Jugador**: estudiante que se registra, marca disponibilidad y participa en equipos.
                                - **Capitán**: jugador que crea y gestiona su equipo, invita jugadores, define alineaciones y sube comprobantes de pago.
                                - **Árbitro**: usuario asignado a partidos para registrar resultados, goles y tarjetas.
                                - **Organizador**: administrador del torneo; crea torneos, verifica pagos y gestiona el estado general.

                                **Equipo de desarrollo:** Shawarma F.C — DOSW, Universidad.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shawarma F.C")
                                .email("techcup@dosw.edu.co")))
                .tags(List.of(
                        new Tag().name("Acceso").description("Registro e inicio de sesión para todos los actores del sistema."),
                        new Tag().name("Usuarios").description("Gestión de los actores: Jugadores, Capitanes, Árbitros y Organizadores."),
                        new Tag().name("Torneos").description("Creación, consulta, tabla de posiciones, llave eliminatoria y estadísticas del torneo. Accesible por Organizadores."),
                        new Tag().name("Equipos").description("Gestión de equipos: creación, consulta y asignación de jugadores. Gestionado por el Capitán."),
                        new Tag().name("Partidos").description("Programación y gestión de partidos: resultados, goles y tarjetas. Registrado por el Árbitro."),
                        new Tag().name("Alineaciones").description("Definición y consulta de alineaciones por partido. Gestionado por el Capitán."),
                        new Tag().name("Pagos").description("Subida de comprobantes de pago por el Capitán y verificación/aprobación por el Organizador.")
                ));
    }
}