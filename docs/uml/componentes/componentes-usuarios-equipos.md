# Componentes — Usuarios, Equipos y Pagos

Aca se muestra como se gestionan los jugadores, capitanes, arbitros, equipos, pagos y alineaciones.

El capitan puede crear su equipo, invitar jugadores y subir el comprobante de pago. El `JugadorValidator` verifica que el jugador este disponible antes de ser invitado. El `PagoServiceImpl` maneja el ciclo de vida del pago usando el patron State: pendiente, en revision, aprobado o rechazado. El `PerfilDeportivoServiceImpl` gestiona la informacion deportiva de cada jugador. El `SancionValidator` verifica que las sanciones registradas en los partidos sean validas. El `RegistroValidator` verifica que el correo sea del dominio correcto segun el tipo de usuario al momento del registro.

---

```mermaid
graph TD
    subgraph Controllers["Controllers"]
        UC[UsuarioController]
        EC[EquipoController]
        PAC[PagoController]
        ALC[AlineacionController]
    end

    subgraph Services["Services"]
        JS[JugadorService]
        CS[CapitanService]
        ARS[ArbitroService]
        ES[EquipoService]
        PAS[PagoServiceImpl]
        ALS[AlineacionService]
        PDS[PerfilDeportivoServiceImpl]
    end

    subgraph Validators["Validators"]
        JV[JugadorValidator]
        PAV[PagoValidator]
        ALV[ValidacionAlineacion]
        EV[ValidacionEquipo]
        PDV[PerfilDeportivoValidator]
        SV[SancionValidator]
        RV[RegistroValidator]
    end

    subgraph Patterns["Patrones"]
        SP[State - Pago]
        SA[Subject - Alineacion]
    end

    subgraph Repos["Repositorios"]
        JR[JugadorRepository]
        CR[CapitanRepository]
        ARR[ArbitroRepository]
        ER[EquipoRepository]
        PAR[PagoRepository]
        ALR[AlineacionRepository]
        PDR[PerfilDeportivoRepository]
    end

    UC --> JS
    UC --> CS
    UC --> ARS
    UC --> PAS
    UC --> ALS
    UC --> PDS
    EC --> ES
    PAC --> PAS
    PAC --> PAV
    ALC --> ALS
    JS --> JR
    JS --> JV
    CS --> CR
    CS --> JV
    ARS --> ARR
    ES --> ER
    ES --> EV
    PAS --> PAR
    PAS --> ER
    PAS --> PAV
    PAS --> SP
    ALS --> ALR
    ALS --> ALV
    ALS --> SA
    PDS --> PDR
    PDS --> JR
    PDS --> PDV
    UC --> RV
```
