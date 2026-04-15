# Diagrama de Secuencia — Torneos

Aca se muestra todo el ciclo de vida de un torneo. El organizador lo crea con nombre, fechas, cantidad de equipos y costo. Al crearlo, el sistema notifica a los observers. Luego puede iniciarlo (pasa a EN_CURSO) o finalizarlo (pasa a FINALIZADO), y en cada cambio se notifica tambien. Ademas puede configurar el reglamento, canchas, horarios y cierre de inscripciones mientras el torneo no este en curso ni finalizado. Cualquier usuario autenticado puede consultar un torneo, ver la tabla de posiciones calculada con los partidos finalizados, ver la llave eliminatoria con todos los partidos y sus marcadores, y ver estadisticas generales del torneo.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant UsuarioController
    participant TorneoController
    participant OrganizadorService
    participant TorneoService
    participant PartidoService
    participant TorneoRepository
    participant Subject
    participant NotificadorTorneo

    %% Crear torneo via organizador
    Cliente->>UsuarioController: POST /api/users/organizers/{id}/tournament {nombre, fechaInicio, fechaFin, cantidadEquipos, costo}
    UsuarioController->>OrganizadorService: crearTorneo(id, torneo)
    OrganizadorService->>OrganizadorService: getOrThrow(id)
    OrganizadorService->>TorneoService: crear(torneo, Map.of())
    TorneoService->>TorneoRepository: save(torneo) → UUID generado
    TorneoRepository-->>TorneoService: Torneo guardado
    TorneoService->>Subject: notificar("TORNEO_CREADO", {id, nombre})
    Subject->>NotificadorTorneo: actualizar("TORNEO_CREADO", datos)
    TorneoService-->>OrganizadorService: Torneo
    OrganizadorService-->>UsuarioController: Torneo
    UsuarioController-->>Cliente: 200 OK Torneo

    %% Iniciar torneo
    Cliente->>UsuarioController: PATCH /api/users/organizers/{id}/tournament/start
    UsuarioController->>OrganizadorService: iniciarTorneo(id)
    OrganizadorService->>TorneoService: iniciar(torneoId)
    TorneoService->>TorneoRepository: findById(torneoId)
    TorneoRepository-->>TorneoService: Torneo
    TorneoService->>TorneoService: torneo.iniciar() → EN_CURSO
    TorneoService->>Subject: notificar("TORNEO_INICIADO", {id})
    TorneoService-->>UsuarioController: Torneo
    UsuarioController-->>Cliente: 200 OK Torneo

    %% Finalizar torneo
    Cliente->>UsuarioController: PATCH /api/users/organizers/{id}/tournament/end
    UsuarioController->>OrganizadorService: finalizarTorneo(id)
    OrganizadorService->>TorneoService: finalizar(torneoId)
    TorneoService->>TorneoRepository: findById(torneoId)
    TorneoRepository-->>TorneoService: Torneo
    TorneoService->>TorneoService: torneo.finalizar() → FINALIZADO
    TorneoService->>Subject: notificar("TORNEO_FINALIZADO", {id})
    TorneoService-->>UsuarioController: Torneo
    UsuarioController-->>Cliente: 200 OK Torneo

    %% Configurar torneo
    Cliente->>UsuarioController: PATCH /api/users/organizers/{id}/tournament/configure {reglamento, canchas, horarios, sanciones, cierreInscripciones}
    UsuarioController->>TorneoService: configurar(torneoId, reglamento, canchas, horarios, sanciones, cierre)
    TorneoService->>TorneoRepository: findById(torneoId)
    TorneoRepository-->>TorneoService: Torneo
    TorneoService->>TorneoService: validar estado != EN_CURSO ni FINALIZADO
    TorneoService->>TorneoRepository: save(torneo)
    TorneoService-->>UsuarioController: Torneo configurado
    UsuarioController-->>Cliente: 200 OK Torneo

    %% Consultar torneo
    Cliente->>UsuarioController: GET /api/tournaments/{id}
    UsuarioController->>TorneoService: obtener(id)
    TorneoService->>TorneoRepository: findById(id)
    alt no encontrado
        TorneoRepository-->>TorneoService: Optional.empty()
        TorneoService-->>UsuarioController: IllegalArgumentException
        UsuarioController-->>Cliente: 400 Bad Request
    end
    TorneoRepository-->>TorneoService: Torneo
    TorneoService-->>UsuarioController: Torneo
    UsuarioController-->>Cliente: 200 OK Torneo

    %% Tabla de posiciones
    Cliente->>TorneoController: GET /api/tournaments/{id}/positions
    TorneoController->>PartidoService: consultarPartidosPorTorneo(id)
    PartidoService-->>TorneoController: List~Partido~
    TorneoController->>TorneoController: calcular puntos por partido FINALIZADO
    TorneoController-->>Cliente: 200 OK List tabla posiciones ordenada por puntos

    %% Llave eliminatoria
    Cliente->>TorneoController: GET /api/tournaments/{id}/bracket
    TorneoController->>PartidoService: consultarPartidosPorTorneo(id)
    PartidoService-->>TorneoController: List~Partido~
    TorneoController-->>Cliente: 200 OK List partidos con marcador y estado

    %% Llave eliminatoria
    Cliente->>TorneoController: GET /api/tournaments/{id}/bracket
    TorneoController->>PartidoService: consultarPartidosPorTorneo(id)
    PartidoService-->>TorneoController: List~Partido~
    TorneoController->>TorneoController: agrupar por fase (CUARTOS, SEMIFINAL, FINAL)
    TorneoController-->>Cliente: 200 OK Map fase -> List partidos

    %% Generar llaves
    Cliente->>TorneoController: POST /api/tournaments/{id}/generate-bracket
    TorneoController->>PartidoService: generarLlaves(id)
    PartidoService->>PartidoService: calcularTabla(torneoId) -> top 8
    PartidoService->>PartidoService: shuffle(equipos)
    PartidoService->>PartidoRepository: save(partidos CUARTOS)
    PartidoService-->>TorneoController: List~Partido~ cuartos
    TorneoController-->>Cliente: 200 OK List~Partido~

    %% Maximos goleadores
    Cliente->>TorneoController: GET /api/tournaments/{id}/top-scorers
    TorneoController->>PartidoService: maximosGoleadores(id)
    PartidoService->>PartidoService: contar goles por jugador
    PartidoService-->>TorneoController: List jugadorId, nombre, goles
    TorneoController-->>Cliente: 200 OK List goleadores

    %% Estadisticas
    Cliente->>TorneoController: GET /api/tournaments/{id}/statistics
    TorneoController->>PartidoService: consultarPartidosPorTorneo(id)
    PartidoService-->>TorneoController: List~Partido~
    TorneoController->>TorneoController: calcular totales y promedios
    TorneoController-->>Cliente: 200 OK totalPartidos, goles, promedios, estados

    %% Eliminar torneo
    Cliente->>TorneoController: DELETE /api/tournaments/{id}
    TorneoController->>TorneoService: eliminar(id)
    TorneoService->>TorneoRepository: deleteById(id)
    TorneoService-->>TorneoController: void
    TorneoController-->>Cliente: 200 OK mensaje
```
