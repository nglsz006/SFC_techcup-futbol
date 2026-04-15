# Diagrama de Secuencia — Jugadores

Aca se muestra como se gestionan los jugadores. Un jugador se registra con sus datos basicos. Luego puede crear su perfil deportivo con posiciones, dorsal, edad, genero e identificacion. Puede editar su perfil en cualquier momento. Cuando un capitan lo invita, el jugador puede aceptar la invitacion, lo que lo marca como no disponible. Si quiere volver a estar disponible para otro equipo, puede marcarse como disponible siempre que no este ya en un equipo.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant UsuarioController
    participant JugadorService
    participant JugadorRepository
    participant PerfilDeportivoService
    participant PerfilDeportivoRepository
    participant JugadorValidator

    %% Crear jugador
    Cliente->>UsuarioController: POST /api/users/players {nombre, email, password, tipoUsuario, numeroCamiseta, posicion}
    UsuarioController->>JugadorRepository: save(jugador) → UUID generado
    JugadorRepository-->>UsuarioController: Jugador guardado
    UsuarioController-->>Cliente: 200 OK Jugador

    %% Crear perfil deportivo
    Cliente->>UsuarioController: POST /api/users/players/{id}/profile {posiciones, dorsal, foto, edad, genero, identificacion, semestre}
    UsuarioController->>PerfilDeportivoService: crearPerfil(id, posiciones, dorsal, foto, edad, genero, identificacion, semestre)
    PerfilDeportivoService->>JugadorRepository: findById(id)
    alt jugador no encontrado
        JugadorRepository-->>PerfilDeportivoService: Optional.empty()
        PerfilDeportivoService-->>UsuarioController: IllegalArgumentException
        UsuarioController-->>Cliente: 400 Bad Request
    end
    PerfilDeportivoService->>PerfilDeportivoRepository: findByJugadorId(id)
    alt ya tiene perfil
        PerfilDeportivoRepository-->>PerfilDeportivoService: Optional.of(perfil)
        PerfilDeportivoService-->>UsuarioController: IllegalStateException
        UsuarioController-->>Cliente: 409 Conflict
    end
    PerfilDeportivoService->>PerfilDeportivoRepository: save(perfil) → UUID generado
    PerfilDeportivoRepository-->>PerfilDeportivoService: PerfilDeportivo guardado
    PerfilDeportivoService-->>UsuarioController: PerfilDeportivo
    UsuarioController-->>Cliente: 200 OK PerfilDeportivo

    %% Editar perfil
    Cliente->>UsuarioController: PATCH /api/users/players/{id}/profile {nombre, numeroCamiseta, posicion, foto}
    UsuarioController->>JugadorService: editarPerfil(id, nombre, numeroCamiseta, posicion, foto)
    JugadorService->>JugadorRepository: findById(id)
    JugadorRepository-->>JugadorService: Jugador
    JugadorService->>JugadorRepository: save(jugador actualizado)
    JugadorService-->>UsuarioController: Jugador
    UsuarioController-->>Cliente: 200 OK Jugador

    %% Aceptar invitacion
    Cliente->>UsuarioController: PATCH /api/users/players/{id}/accept-invitation
    UsuarioController->>JugadorService: aceptarInvitacion(id)
    JugadorService->>JugadorRepository: findById(id)
    JugadorRepository-->>JugadorService: Jugador
    JugadorService->>JugadorService: jugador.setAvailable(false)
    JugadorService->>JugadorRepository: save(jugador)
    JugadorService-->>UsuarioController: void
    UsuarioController-->>Cliente: 200 OK "Invitacion aceptada correctamente"

    %% Marcar disponible
    Cliente->>UsuarioController: PATCH /api/users/players/{id}/availability
    UsuarioController->>JugadorService: marcarDisponible(id)
    JugadorService->>JugadorRepository: findById(id)
    JugadorRepository-->>JugadorService: Jugador
    JugadorService->>JugadorValidator: jugadorDisponibleParaEquipo(jugador)
    alt ya en equipo
        JugadorValidator-->>JugadorService: false
        JugadorService-->>UsuarioController: IllegalStateException
        UsuarioController-->>Cliente: 409 Conflict
    end
    JugadorService->>JugadorRepository: save(jugador)
    UsuarioController-->>Cliente: 200 OK "Jugador marcado como disponible"
    %% Busqueda avanzada de jugadores
    Cliente->>UsuarioController: GET /api/users/captains/{id}/search-players/advanced?posicion=X&semestre=Y&edad=Z&genero=G&nombre=N&identificacion=I
    UsuarioController->>PerfilDeportivoService: buscarJugadores(posicion, semestre, edad, genero, nombre, identificacion)
    PerfilDeportivoService->>PerfilDeportivoRepository: findAll()
    PerfilDeportivoService->>PerfilDeportivoService: filtrar por cada parametro opcional
    PerfilDeportivoService-->>UsuarioController: List~PerfilDeportivo~
    UsuarioController-->>Cliente: 200 OK List~PerfilDeportivo~
```