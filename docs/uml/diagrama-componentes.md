# Diagrama de Componentes

Arquitectura interna del sistema por capas, mostrando las dependencias entre componentes.

---

## Arquitectura general por capas

```mermaid
graph TD
    subgraph Cliente["Cliente"]
        SW[Swagger UI / Postman / Frontend]
    end

    subgraph Seguridad["🔒 Seguridad"]
        JF[JwtFilter]
        JS[JwtService]
        SC[SecurityConfig]
        OH[OAuth2SuccessHandler]
    end

    subgraph Controllers["🎮 Controllers"]
        AC[AccesoController]
        UC[UsuarioController]
        TC[TorneoController]
        PC[PartidoController]
        PAC[PagoController]
        ALC[AlineacionController]
        ADC[AdministradorController]
        AUC[AuditoriaController]
        OC[OAuth2Controller]
        EC[EquipoController]
    end

    subgraph Services["⚙️ Services"]
        AS[AccesoServiceImpl]
        JS2[JugadorService]
        CS[CapitanService]
        OS[OrganizadorService]
        ARS[ArbitroService]
        TS[TorneoService]
        PS[PartidoServiceImpl]
        PAS[PagoServiceImpl]
        ALS[AlineacionService]
        ADS[AdministradorService]
        AUS[AuditoriaService]
        PDS[PerfilDeportivoServiceImpl]
        O2S[OAuth2Service]
        AUTS[AutenticacionAdministradorService]
    end

    subgraph Validators["✅ Validators"]
        AV[AccesoValidator]
        JV[JugadorValidator]
        PV[PartidoValidator]
        PAV[PagoValidator]
        TV[ValidacionTorneo]
        EV[ValidacionEquipo]
        ALV[ValidacionAlineacion]
        PDV[PerfilDeportivoValidator]
        ADV[AdministradorValidator]
        AUV[AuditoriaValidator]
        UV[UsuarioValidator]
        SV[SancionValidator]
    end

    subgraph Patterns["🔄 Patrones de Diseño"]
        SUB[Subject - abstract]
        OBS[Observer - interface]
        NT[NotificadorTorneo]
        LO[LoggerObserver]
    end

    subgraph Repositories["🗄️ Repositories Core"]
        UR[UsuarioRegistradoRepository]
        JR[JugadorRepository]
        CR[CapitanRepository]
        OR[OrganizadorRepository]
        ARR[ArbitroRepository]
        TR[TorneoRepository]
        PR[PartidoRepository]
        PAR[PagoRepository]
        ALR[AlineacionRepository]
        ADR[AdministradorRepository]
        AUR[RegistroAuditoriaRepository]
        PDR[PerfilDeportivoRepository]
        ER[EquipoRepository]
        SR[SancionRepository]
    end

    subgraph Persistence["💾 Persistence JPA"]
        ENT[Entities]
        MAP[Mappers]
        JPA[JPA Repositories]
    end

    subgraph DB["🐘 Base de Datos"]
        PG[(PostgreSQL)]
    end

    SW -->|HTTP + Bearer JWT| JF
    JF --> JS
    JF -->|request autenticado| Controllers
    SC --> JF
    OH --> O2S

    Controllers --> Services
    Controllers --> Validators
    Services --> Validators
    Services --> Patterns
    Services --> Repositories
    Repositories --> Persistence
    ENT <--> MAP
    MAP <--> JPA
    JPA --> PG
```

---

## Módulo de Acceso y Seguridad

```mermaid
graph LR
    subgraph Entrada
        CLI[Cliente]
    end

    subgraph Seguridad
        JF[JwtFilter]
        JS[JwtService]
        OH[OAuth2SuccessHandler]
        O2S[OAuth2Service]
        GG[Google OAuth2]
    end

    subgraph Acceso
        AC[AccesoController]
        AS[AccesoServiceImpl]
        AV[AccesoValidator]
        AM[AccesoMapper]
        PU[PasswordUtil]
    end

    subgraph Repos
        UR[UsuarioRegistradoRepository]
        OR[OrganizadorRepository]
        ARR[ArbitroRepository]
        CR[CapitanRepository]
    end

    CLI -->|POST /api/access/register| AC
    CLI -->|POST /api/access/login| AC
    CLI -->|GET /oauth2/authorization/google| GG
    GG -->|callback| OH
    OH --> O2S
    O2S --> UR
    O2S --> JS
    AC --> AV
    AC --> AS
    AS --> UR
    AS --> OR
    AS --> ARR
    AS --> CR
    AS --> JS
    AS --> AM
    AM --> PU
```

---

## Módulo de Torneo y Partido

```mermaid
graph LR
    subgraph Controllers
        UC[UsuarioController]
        TC[TorneoController]
        PC[PartidoController]
    end

    subgraph Services
        OS[OrganizadorService]
        TS[TorneoService]
        PS[PartidoServiceImpl]
        SUB[Subject]
        NT[NotificadorTorneo]
    end

    subgraph Validators
        TV[ValidacionTorneo]
        PV[PartidoValidator]
        UV[UsuarioValidator]
    end

    subgraph Repos
        TR[TorneoRepository]
        PR[PartidoRepository]
        ER[EquipoRepository]
        JR[JugadorRepository]
        OR[OrganizadorRepository]
    end

    UC --> OS
    UC --> PS
    TC --> TS
    TC --> PS
    PC --> PS
    PC --> PV
    OS --> OR
    OS --> TS
    OS --> UV
    TS --> TR
    TS --> TV
    TS --> SUB
    SUB --> NT
    PS --> PR
    PS --> TR
    PS --> ER
    PS --> JR
    PS --> PV
```

---

## Módulo de Usuarios, Equipos y Pagos

```mermaid
graph LR
    subgraph Controllers
        UC[UsuarioController]
        EC[EquipoController]
        PAC[PagoController]
        ALC[AlineacionController]
    end

    subgraph Services
        JS[JugadorService]
        CS[CapitanService]
        ARS[ArbitroService]
        ES[EquipoService]
        PAS[PagoServiceImpl]
        ALS[AlineacionService]
        PDS[PerfilDeportivoServiceImpl]
    end

    subgraph Validators
        JV[JugadorValidator]
        PAV[PagoValidator]
        ALV[ValidacionAlineacion]
        EV[ValidacionEquipo]
        PDV[PerfilDeportivoValidator]
    end

    subgraph Repos
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
    UC --> ES
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
    ALS --> ALR
    ALS --> ALV
    PDS --> PDR
    PDS --> JR
    PDS --> PDV
```

---

## Módulo de Administración y Auditoría

```mermaid
graph LR
    subgraph Controllers
        ADC[AdministradorController]
        AUC[AuditoriaController]
    end

    subgraph Services
        ADS[AdministradorService]
        AUS[AuditoriaService]
        AUTS[AutenticacionAdministradorService]
    end

    subgraph Validators
        ADV[AdministradorValidator]
        AUV[AuditoriaValidator]
    end

    subgraph Repos
        ADR[AdministradorRepository]
        AUR[RegistroAuditoriaRepository]
        OR[OrganizadorRepository]
        ARR[ArbitroRepository]
        UR[UsuarioRegistradoRepository]
    end

    ADC --> ADV
    ADC --> AUTS
    ADC --> ADS
    ADC --> AUS
    AUC --> AUV
    AUC --> AUS
    ADS --> ADR
    ADS --> OR
    ADS --> ARR
    ADS --> UR
    ADS --> AUS
    AUS --> AUR
    AUTS --> ADR
```
