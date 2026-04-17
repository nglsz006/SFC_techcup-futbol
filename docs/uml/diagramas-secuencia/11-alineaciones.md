# Diagrama de Secuencia — Alineaciones

Aca se muestra como se gestionan las alineaciones. El capitan crea la alineacion para su equipo en un partido especifico, con la formacion tactica, los jugadores titulares y los reservas. El sistema valida los datos antes de guardarla y notifica a los observers al crearla. Tambien se puede consultar una alineacion por ID o listar todas.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant AlineacionController
    participant AlineacionService
    participant AlineacionRepository
    participant ValidacionAlineacion
    participant Subject

    %% Crear alineacion
    Cliente->>AlineacionController: POST /api/lineups {equipoId, partidoId, formacion, titulares, reservas}
    AlineacionController->>AlineacionService: crear(alineacion, datos)
    AlineacionService->>ValidacionAlineacion: validar(datos)
    alt validacion falla
        ValidacionAlineacion-->>AlineacionService: IllegalArgumentException
        AlineacionService-->>AlineacionController: IllegalArgumentException
        AlineacionController-->>Cliente: 400 Bad Request
    end
    AlineacionService->>AlineacionRepository: save(alineacion) → UUID generado
    AlineacionRepository-->>AlineacionService: Alineacion guardada
    AlineacionService->>Subject: notificar("ALINEACION_CREADA", {id, equipoId})
    AlineacionService-->>AlineacionController: Alineacion
    AlineacionController-->>Cliente: 200 OK Alineacion

    %% Obtener alineacion
    Cliente->>AlineacionController: GET /api/lineups/{id}
    AlineacionController->>AlineacionService: obtener(id)
    AlineacionService->>AlineacionRepository: findById(id)
    alt no encontrada
        AlineacionRepository-->>AlineacionService: Optional.empty()
        AlineacionService-->>AlineacionController: IllegalArgumentException
        AlineacionController-->>Cliente: 400 Bad Request
    end
    AlineacionRepository-->>AlineacionService: Alineacion
    AlineacionService-->>AlineacionController: Alineacion
    AlineacionController-->>Cliente: 200 OK Alineacion

    %% Listar alineaciones
    Cliente->>AlineacionController: GET /api/lineups
    AlineacionController->>AlineacionService: listar()
    AlineacionService->>AlineacionRepository: findAll()
    AlineacionRepository-->>AlineacionService: List~Alineacion~
    AlineacionService-->>AlineacionController: List~Alineacion~
    AlineacionController-->>Cliente: 200 OK List~Alineacion~
```
