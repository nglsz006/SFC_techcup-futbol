# Diagrama de Secuencia — Arbitros

Aca se muestra todo lo que puede hacer un arbitro. El administrador lo registra en el sistema. Luego se le puede asignar un partido. El arbitro puede consultar sus partidos asignados, iniciarlos y finalizarlos.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant UsuarioController
    participant ArbitroService
    participant ArbitroRepository
    participant PartidoRepository
    participant PartidoServiceImpl

    %% Crear arbitro via admin
    Cliente->>UsuarioController: POST /api/users/referees {nombre, email, password, tipoUsuario}
    UsuarioController->>ArbitroService: save(arbitro)
    ArbitroService->>ArbitroRepository: save(arbitro) → UUID generado
    ArbitroRepository-->>ArbitroService: Arbitro guardado
    ArbitroService-->>UsuarioController: Arbitro
    UsuarioController-->>Cliente: 200 OK Arbitro

    %% Asignar partido a arbitro
    Cliente->>UsuarioController: POST /api/users/referees/{id}/matches/{matchId}
    UsuarioController->>ArbitroRepository: findById(id)
    UsuarioController->>PartidoRepository: findById(matchId)
    alt partido no encontrado
        PartidoRepository-->>UsuarioController: Optional.empty()
        UsuarioController-->>Cliente: 400 Bad Request
    end
    UsuarioController->>ArbitroService: save(arbitro con partido asignado)
    ArbitroService->>ArbitroRepository: save(arbitro)
    ArbitroService-->>UsuarioController: void
    UsuarioController-->>Cliente: 200 OK Arbitro asignado al partido correctamente

    %% Consultar partidos asignados
    Cliente->>UsuarioController: GET /api/users/referees/{id}/matches
    UsuarioController->>ArbitroService: consultarPartidosAsignados(id)
    ArbitroService->>ArbitroRepository: findById(id)
    alt arbitro no encontrado
        ArbitroRepository-->>ArbitroService: Optional.empty()
        ArbitroService-->>UsuarioController: List vacia
        UsuarioController-->>Cliente: 200 OK []
    end
    ArbitroService->>ArbitroService: arbitro.getAssignedMatches()
    ArbitroService-->>UsuarioController: List~Partido~
    UsuarioController-->>Cliente: 200 OK List~Partido~

    %% Iniciar partido
    Cliente->>UsuarioController: PUT /api/users/referees/{id}/matches/{matchId}/start
    UsuarioController->>PartidoServiceImpl: iniciarPartido(matchId)
    PartidoServiceImpl->>PartidoRepository: findById(matchId)
    PartidoServiceImpl->>PartidoServiceImpl: partido.iniciar() → EN_CURSO
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
    note right of PartidoServiceImpl: Si fase=CUARTOS crea SEMIFINAL, Si fase=SEMIFINAL crea FINAL, Si fase=FINAL registra campeon
    PartidoServiceImpl-->>UsuarioController: Partido
    UsuarioController-->>Cliente: 200 OK Partido
```
