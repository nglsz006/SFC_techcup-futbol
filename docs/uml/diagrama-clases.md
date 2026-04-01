# Diagrama de Clases

Organización completa de las clases del sistema, sus atributos, métodos y relaciones.

---

```mermaid
classDiagram

    %% ── Jerarquía de usuarios ──────────────────────────────────────────────

    class Usuario {
        <<abstract>>
        -String id
        -String name
        -String email
        -String password
        -TipoUsuario userType
        +getId() String
        +getEmail() String
    }

    class UsuarioRegistrado {
        +UsuarioRegistrado(id, nombre, email, password, tipoUsuario)
    }

    class Jugador {
        -int jerseyNumber
        -Posicion position
        -boolean available
        -String photo
        -String equipoId
        -ArrayList~Sancion~ sanciones
        +agregarSancion(sancion)
        +tieneSancion(tipo) boolean
        +getSancionesPorTipo(tipo) List
    }

    class Capitan {
        -Equipo team
        +getTeam() Equipo
    }

    class Arbitro {
        -List~Partido~ assignedMatches
        +getAssignedMatches() List
    }

    class Organizador {
        -Torneo currentTournament
        +getCurrentTournament() Torneo
    }

    class Administrador {
        -boolean activo
        +isActivo() boolean
    }

    Usuario <|-- UsuarioRegistrado
    Usuario <|-- Jugador
    Usuario <|-- Arbitro
    Usuario <|-- Organizador
    Usuario <|-- Administrador
    Jugador <|-- Capitan

    %% ── Equipo y Torneo ────────────────────────────────────────────────────

    class Equipo {
        -String id
        -String nombre
        -String escudo
        -String colorPrincipal
        -String colorSecundario
        -String capitanId
        -List~String~ jugadores
        +agregarJugador(jugadorId)
    }

    class Torneo {
        -String id
        -String nombre
        -LocalDateTime fechaInicio
        -LocalDateTime fechaFin
        -int cantidadEquipos
        -double costo
        -EstadoTorneo estado
        -String reglamento
        -LocalDateTime cierreInscripciones
        -String canchas
        -String horarios
        -String sanciones
        +iniciar()
        +finalizar()
    }

    class PerfilDeportivo {
        -String id
        -String jugadorId
        -List~Posicion~ posiciones
        -int dorsal
        -String foto
        -int edad
        -Genero genero
        -String identificacion
        -Integer semestre
    }

    Capitan --> Equipo
    Organizador --> Torneo
    Jugador --> PerfilDeportivo

    %% ── Partido ────────────────────────────────────────────────────────────

    class Partido {
        -String id
        -LocalDateTime fecha
        -String cancha
        -int marcadorLocal
        -int marcadorVisitante
        -PartidoEstado estado
        -List~Gol~ goles
        -List~Sancion~ sanciones
        +iniciar()
        +registrarResultado(golesLocal, golesVisitante)
        +finalizar()
    }

    class Gol {
        -String id
        -int minuto
        -Jugador jugador
    }

    class Sancion {
        -String id
        -TipoSancion tipoSancion
        -String descripcion
        -Jugador jugador
    }

    class Alineacion {
        -String id
        -String equipoId
        -String partidoId
        -Formacion formacion
        -List~String~ titulares
        -List~String~ reservas
    }

    Partido --> Equipo : equipoLocal
    Partido --> Equipo : equipoVisitante
    Partido --> Torneo
    Partido "1" --> "*" Gol
    Partido "1" --> "*" Sancion
    Alineacion --> Equipo
    Alineacion --> Partido

    %% ── Pago ───────────────────────────────────────────────────────────────

    class Pago {
        -String id
        -String comprobante
        -LocalDate fechaSubida
        -PagoEstado estado
        -Equipo equipo
        +avanzar()
        +rechazar()
    }

    Pago --> Equipo

    %% ── Auditoría ──────────────────────────────────────────────────────────

    class RegistroAuditoria {
        -String id
        -String administradorId
        -String usuario
        -TipoAccionAuditoria tipoAccion
        -String descripcion
        -LocalDateTime fecha
    }

    %% ── Patrones de diseño ─────────────────────────────────────────────────

    class Subject {
        -List~Observer~ observers
        +agregarObserver(observer)
        +notificar(evento, datos)
    }

    class Observer {
        <<interface>>
        +actualizar(evento, datos)
    }

    class NotificadorTorneo {
        +actualizar(evento, datos)
    }

    class LoggerObserver {
        +actualizar(evento, datos)
    }

    Subject --> Observer
    Observer <|.. NotificadorTorneo
    Observer <|.. LoggerObserver

    %% ── Seguridad ──────────────────────────────────────────────────────────

    class JwtService {
        +generarToken(email, rol) String
        +extraerEmail(token) String
        +extraerRol(token) String
        +esValido(token) boolean
    }

    class JwtFilter {
        +doFilterInternal(request, response, chain)
    }

    class OAuth2Service {
        +procesarCallback(authentication) OAuth2Response
    }

    JwtFilter --> JwtService

    %% ── Enums ──────────────────────────────────────────────────────────────

    class RolFuncional {
        <<enumeration>>
        ADMINISTRADOR
        ORGANIZADOR
        ARBITRO
        CAPITAN
        JUGADOR
    }

    class TipoAccionAuditoria {
        <<enumeration>>
        LOGIN_ADMIN
        REGISTRO_ORGANIZADOR
        REGISTRO_ARBITRO
    }
```
