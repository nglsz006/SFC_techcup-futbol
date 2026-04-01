# Clases — Parte 2: Torneo, Partido, Pago y Alineación

Acá se muestra el corazón del torneo: cómo se organiza la competencia, cómo se juegan los partidos, cómo se manejan los pagos y cómo se registran las alineaciones.

Un `Torneo` puede estar en tres momentos: recién creado, en curso o finalizado. Dentro del torneo se juegan `Partido`s, cada uno entre dos equipos (local y visitante). Durante un partido se pueden registrar `Gol`es, `Sancion`es y `Tarjeta`s. Un partido también pasa por estados: programado, en curso y finalizado.

El `Pago` es el comprobante que sube el capitán para inscribir a su equipo. Empieza como pendiente, pasa a revisión y termina aprobado o rechazado. La `Alineacion` es la formación táctica que define el capitán antes de cada partido, con los jugadores titulares y reservas.

---

```mermaid
classDiagram

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
        +iniciar() void
        +finalizar() void
    }

    class EstadoTorneo {
        <<enumeration>>
        CREADO
        EN_CURSO
        FINALIZADO
    }

    class Partido {
        -String id
        -LocalDateTime fecha
        -String cancha
        -int marcadorLocal
        -int marcadorVisitante
        -PartidoEstado estado
        +iniciar() void
        +registrarResultado(gl, gv) void
        +finalizar() void
    }

    class PartidoEstado {
        <<enumeration>>
        PROGRAMADO
        EN_CURSO
        FINALIZADO
    }

    class Gol {
        -String id
        -int minuto
        -String jugadorNombre
    }

    class Sancion {
        -String id
        -TipoSancion tipoSancion
        -String descripcion
    }

    class TipoSancion {
        <<enumeration>>
        TARJETA_ROJA
        TARJETA_AMARILLA
        AGRESION_VERBAL
        AGRESION_FISICA
    }

    class Tarjeta {
        -String id
        -TipoTarjeta tipo
        -int minuto
    }

    class TipoTarjeta {
        <<enumeration>>
        AMARILLA
        ROJA
    }

    class Pago {
        -String id
        -String comprobante
        -LocalDate fechaSubida
        -PagoEstado estado
        -String equipoNombre
        +avanzar() void
        +rechazar() void
    }

    class PagoEstado {
        <<enumeration>>
        PENDIENTE
        EN_REVISION
        APROBADO
        RECHAZADO
    }

    class Alineacion {
        -String id
        -String equipoId
        -String partidoId
        -Formacion formacion
        -List~String~ titulares
        -List~String~ reservas
    }

    class Formacion {
        <<enumeration>>
        F_4_4_2
        F_4_3_3
        F_3_5_2
        F_4_5_1
        F_5_3_2
    }

    class RegistroAuditoria {
        -String id
        -String administradorId
        -String usuario
        -TipoAccionAuditoria tipoAccion
        -String descripcion
        -LocalDateTime fecha
    }

    class TipoAccionAuditoria {
        <<enumeration>>
        LOGIN_ADMIN
        REGISTRO_ORGANIZADOR
        REGISTRO_ARBITRO
    }

    Torneo "1" --> "1" EstadoTorneo : estado
    Partido "1" --> "1" PartidoEstado : estado
    Partido "1" --> "1" Torneo : pertenece a
    Partido "1" --> "1" Equipo : equipoLocal
    Partido "1" --> "1" Equipo : equipoVisitante
    Partido "1" *-- "0..*" Gol : registra
    Partido "1" *-- "0..*" Sancion : registra
    Partido "1" *-- "0..*" Tarjeta : registra
    Sancion "1" --> "1" TipoSancion : tipo
    Tarjeta "1" --> "1" TipoTarjeta : tipo
    Pago "0..*" --> "1" Equipo : de equipo
    Pago "1" --> "1" PagoEstado : estado
    Alineacion "1" --> "1" Equipo : de equipo
    Alineacion "1" --> "1" Partido : para partido
    Alineacion "1" --> "1" Formacion : formacion
    RegistroAuditoria "1" --> "1" TipoAccionAuditoria : tipo
```
