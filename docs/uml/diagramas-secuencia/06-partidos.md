# Diagrama de Secuencia — Partidos

Aca se muestra el ciclo de vida de un partido. El organizador lo crea asignando torneo, equipos, fecha y cancha. El arbitro lo inicia (pasa a EN_CURSO), registra goles con el jugador y el minuto, registra sanciones con el tipo y descripcion, registra el resultado final con los goles de cada equipo, y lo finaliza (pasa a FINALIZADO). Solo se pueden registrar goles y sanciones en partidos que esten EN_CURSO.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant UsuarioController
    participant PartidoValidator
    participant PartidoServiceImpl
    participant PartidoRepository
    participant TorneoRepository
    participant EquipoRepository
    participant JugadorRepository

    %% Crear partido
    Cliente->>UsuarioController: POST /api/users/organizers/{id}/matches {torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha}
    UsuarioController->>PartidoServiceImpl: crearPartido(torneoId, localId, visitanteId, fecha, cancha)
    PartidoServiceImpl->>TorneoRepository: findById(torneoId)
    PartidoServiceImpl->>EquipoRepository: findById(equipoLocalId)
    PartidoServiceImpl->>EquipoRepository: findById(equipoVisitanteId)
    PartidoServiceImpl->>PartidoServiceImpl: validar local != visitante
    PartidoServiceImpl->>PartidoRepository: save(partido) → UUID generado
    PartidoRepository-->>PartidoServiceImpl: Partido guardado
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido

    %% Iniciar partido
    Cliente->>UsuarioController: PUT /api/users/referees/{id}/matches/{matchId}/start
    UsuarioController->>PartidoServiceImpl: iniciarPartido(matchId)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoRepository-->>PartidoServiceImpl: Partido
    PartidoServiceImpl->>PartidoServiceImpl: partido.iniciar() → EN_CURSO
    PartidoServiceImpl->>PartidoRepository: save(partido)
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido

    %% Registrar goleador
    Cliente->>UsuarioController: POST /api/users/referees/{id}/matches/{matchId}/goals {jugadorId, minuto}
    UsuarioController->>PartidoServiceImpl: registrarGoleador(matchId, jugadorId, minuto)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoServiceImpl->>PartidoServiceImpl: validarPartidoEnCurso(partido)
    PartidoServiceImpl->>JugadorRepository: findById(jugadorId)
    PartidoServiceImpl->>PartidoServiceImpl: new Gol con UUID → partido.getGoles().add(gol)
    PartidoServiceImpl->>PartidoServiceImpl: actualizar marcador
    PartidoServiceImpl->>PartidoRepository: save(partido)
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido

    %% Registrar sancion
    Cliente->>UsuarioController: POST /api/users/referees/{id}/matches/{matchId}/sanctions {jugadorId, tipoSancion, descripcion}
    UsuarioController->>PartidoValidator: validarSancion(sancion)
    UsuarioController->>PartidoServiceImpl: registrarSancion(matchId, jugadorId, tipoSancion, descripcion)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoServiceImpl->>PartidoServiceImpl: validarPartidoEnCurso(partido)
    PartidoServiceImpl->>JugadorRepository: findById(jugadorId)
    PartidoServiceImpl->>PartidoServiceImpl: new Sancion con UUID → partido y jugador
    PartidoServiceImpl->>PartidoRepository: save(partido)
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido

    %% Registrar resultado
    Cliente->>UsuarioController: PUT /api/users/referees/{id}/matches/{matchId}/result {golesLocal, golesVisitante}
    UsuarioController->>PartidoValidator: validarResultado(golesLocal, golesVisitante)
    UsuarioController->>PartidoServiceImpl: registrarResultado(matchId, golesLocal, golesVisitante)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoServiceImpl->>PartidoServiceImpl: partido.registrarResultado(golesLocal, golesVisitante)
    PartidoServiceImpl->>PartidoRepository: save(partido)
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido

    %% Finalizar partido
    Cliente->>UsuarioController: PUT /api/users/referees/{id}/matches/{matchId}/end
    UsuarioController->>PartidoServiceImpl: finalizarPartido(matchId)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoServiceImpl->>PartidoServiceImpl: partido.finalizar() → FINALIZADO
    PartidoServiceImpl->>PartidoRepository: save(partido)
    PartidoServiceImpl->>PartidoServiceImpl: avanzarGanador(partido)
    note right of PartidoServiceImpl: Si fase=CUARTOS crea SEMIFINAL\nSi fase=SEMIFINAL crea FINAL\nSi fase=FINAL registra campeon en torneo
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido
```
