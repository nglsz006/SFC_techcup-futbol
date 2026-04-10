# Componentes — Torneo y Partido

Aca se muestra como funciona la gestion del torneo y los partidos. El organizador crea y gestiona el torneo, y el arbitro maneja los partidos.

Cuando se crea un torneo, el `TorneoService` lo guarda y notifica a los observers usando el patron Observer. El torneo pasa por estados: creado, en curso y finalizado, controlados por el patron State. Lo mismo pasa con los partidos: programado, en curso y finalizado. El `PartidoValidator` se asegura de que los datos sean correctos antes de crear o modificar un partido.

El `TorneoController` expone ademas endpoints para consultar la tabla de posiciones, la llave eliminatoria y las estadisticas del torneo, calculadas a partir de los partidos finalizados.

---

```mermaid
graph TD
    subgraph Controllers["Controllers"]
        UC[UsuarioController]
        TC[TorneoController]
        PC[PartidoController]
    end

    subgraph Services["Services"]
        OS[OrganizadorService]
        TS[TorneoService]
        PS[PartidoServiceImpl]
    end

    subgraph Validators["Validators"]
        TV[ValidacionTorneo]
        PV[PartidoValidator]
        UV[UsuarioValidator]
    end

    subgraph Patterns["Patrones"]
        SUB[Subject]
        NT[NotificadorTorneo]
        LO[LoggerObserver]
        ST[State - Torneo]
        SP[State - Partido]
    end

    subgraph Repos["Repositorios"]
        TR[TorneoRepository]
        PR[PartidoRepository]
        ER[EquipoRepository]
        JR[JugadorRepository]
        OR[OrganizadorRepository]
    end

    UC -->|crear torneo| OS
    UC -->|crear partido| PS
    TC -->|consultar torneo| TS
    TC -->|tabla posiciones / bracket / estadisticas| PS
    PC -->|consultar partido| PS
    OS --> OR
    OS --> TS
    OS --> UV
    TS --> TR
    TS --> TV
    TS --> SUB
    TS --> ST
    SUB --> NT
    SUB --> LO
    PS --> PR
    PS --> TR
    PS --> ER
    PS --> JR
    PS --> PV
    PS --> SP
```
