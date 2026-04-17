# Clases — Parte 2: Torneo, Partido, Pago y Alineacion

Aca se muestra el corazon del torneo: como se organiza la competencia, como se juegan los partidos, como se manejan los pagos y como se registran las alineaciones.

Un `Torneo` puede estar en tres momentos: recien creado, en curso o finalizado. Dentro del torneo se juegan `Partido`s, cada uno entre dos equipos (local y visitante). Durante un partido se pueden registrar `Gol`es, `Sancion`es y `Tarjeta`s. Un partido tambien pasa por estados: programado, en curso y finalizado.

El `Pago` es el comprobante que sube el capitan para inscribir a su equipo. Empieza como pendiente, pasa a revision y termina aprobado o rechazado. La `Alineacion` es la formacion tactica que define el capitan antes de cada partido, con los jugadores titulares y reservas.

El `RegistroAuditoria` guarda un historial de las acciones importantes que hace el administrador, como registrar un organizador o un arbitro.

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
        -String campeonId
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
        -Fase fase
        -int marcadorLocal
        -int marcadorVisitante
        -PartidoEstado estado
        +iniciar() void
        +registrarResultado(gl, gv) void
        +finalizar() void
    }

    class Fase {
        <<enumeration>>
        CUARTOS
        SEMIFINAL
        FINAL
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
        -MedioPago medioPago
        -double monto
        -String equipoNombre
        +avanzar() void
        +rechazar() void
    }

    class MedioPago {
        <<enumeration>>
        NEQUI
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
    Partido "1" --> "0..1" Fase : fase
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
    Pago "1" --> "1" MedioPago : medioPago
    Alineacion "1" --> "1" Equipo : de equipo
    Alineacion "1" --> "1" Partido : para partido
    Alineacion "1" --> "1" Formacion : formacion
    RegistroAuditoria "1" --> "1" TipoAccionAuditoria : tipo
```
