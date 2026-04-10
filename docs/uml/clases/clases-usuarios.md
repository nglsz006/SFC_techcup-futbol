# Clases — Parte 1: Usuarios y Equipos

Aca se muestra quienes pueden usar el sistema y como estan organizados. Todos los actores del sistema comparten una base comun llamada `Usuario`, que tiene los datos basicos como nombre, correo y contrasena. Cada tipo de usuario agrega sus propias caracteristicas encima de esa base.

Un `Jugador` tiene numero de camiseta, posicion en la cancha y puede tener sanciones. Un `Capitan` es un jugador especial que ademas lidera un equipo. Un `Organizador` gestiona un torneo. Un `Arbitro` tiene asignados los partidos que va a dirigir. Un `Administrador` es quien registra a los organizadores y arbitros, y puede activarse o desactivarse. Un `UsuarioRegistrado` es quien se registra directamente en la plataforma o con Google.

El `Equipo` agrupa entre 7 y 12 jugadores bajo un capitan, y tiene nombre, escudo y colores. Cada jugador puede tener un `PerfilDeportivo` con informacion mas detallada como edad, dorsal, genero y semestre.

---

```mermaid
classDiagram

    class Usuario {
        <<abstract>>
        -String id
        -String name
        -String email
        -String password
        -TipoUsuario userType
        +getId() String
        +getName() String
        +getEmail() String
    }

    class TipoUsuario {
        <<enumeration>>
        ESTUDIANTE
        GRADUADO
        PROFESOR
        PERSONAL_ADMIN
        FAMILIAR
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
        +agregarSancion(sancion) void
        +tieneSancion(tipo) boolean
        +getSancionesPorTipo(tipo) List~Sancion~
    }

    class Posicion {
        <<enumeration>>
        PORTERO
        DEFENSA
        VOLANTE
        DELANTERO
    }

    class Capitan {
        -Equipo team
        +getTeam() Equipo
    }

    class Arbitro {
        -List~Partido~ assignedMatches
        +getAssignedMatches() List~Partido~
    }

    class Organizador {
        -Torneo currentTournament
        +getCurrentTournament() Torneo
    }

    class Administrador {
        -boolean activo
        +isActivo() boolean
    }

    class Equipo {
        -String id
        -String nombre
        -String escudo
        -String colorPrincipal
        -String colorSecundario
        -String capitanId
        -List~String~ jugadores
        +agregarJugador(jugadorId) void
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

    class Genero {
        <<enumeration>>
        MASCULINO
        FEMENINO
        OTRO
    }

    Usuario "1" --> "1" TipoUsuario : userType
    Usuario <|-- UsuarioRegistrado : extends
    Usuario <|-- Jugador : extends
    Usuario <|-- Arbitro : extends
    Usuario <|-- Organizador : extends
    Usuario <|-- Administrador : extends
    Jugador <|-- Capitan : extends
    Jugador "1" --> "1" Posicion : position
    Capitan "1" --> "0..1" Equipo : lidera
    Equipo "1" o-- "7..12" Jugador : contiene
    Jugador "1" --> "0..1" PerfilDeportivo : tiene
    PerfilDeportivo "1" --> "1" Genero : genero
```
