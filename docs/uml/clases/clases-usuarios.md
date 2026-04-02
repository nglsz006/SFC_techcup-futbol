# Clases — Parte 1: Usuarios y Equipos

Acá se muestra quiénes pueden usar el sistema y cómo están organizados. Todos los actores del sistema — jugadores, capitanes, árbitros, organizadores y administradores — comparten una base común llamada `Usuario`, que tiene los datos básicos como nombre, correo y contraseña. Cada uno agrega sus propias características encima de esa base.

Por ejemplo, un `Jugador` tiene número de camiseta y posición en la cancha. Un `Capitán` es un jugador especial que además lidera un equipo. Un `Organizador` gestiona un torneo. Un `Árbitro` tiene asignados los partidos que va a dirigir.

El `Equipo` agrupa entre 7 y 12 jugadores bajo un capitán, y tiene nombre, escudo y colores. Cada jugador puede tener un `PerfilDeportivo` con información más detallada como edad, dorsal, género y semestre.

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
