# Componentes — Arquitectura General

Aca se ve el sistema completo de un vistazo. Toda peticion que llega del cliente pasa primero por el filtro JWT que verifica que el usuario tenga un token valido. Si lo tiene, la peticion llega al controlador correspondiente. El controlador le pasa el trabajo al servicio, que contiene la logica del negocio. El servicio usa los validadores para verificar que los datos esten bien, los patrones de diseno para manejar estados y notificaciones, y los repositorios para acceder a la base de datos PostgreSQL a traves de la capa de persistencia JPA con mappers de MapStruct.

---

```mermaid
graph TD
    CLI[Cliente - Swagger / Postman / Frontend]

    subgraph Seguridad["Seguridad"]
        JF[JwtFilter]
        JS[JwtService]
        OH[OAuth2SuccessHandler]
        SC[SecurityConfig]
    end

    subgraph Controllers["Controllers"]
        AC[AccesoController]
        UC[UsuarioController]
        TC[TorneoController]
        PC[PartidoController]
        PAC[PagoController]
        ALC[AlineacionController]
        ADC[AdministradorController]
        AUC[AuditoriaController]
        EC[EquipoController]
        OC[OAuth2Controller]
    end

    subgraph Services["Services"]
        SVC[AccesoServiceImpl\nJugadorService\nCapitanService\nOrganizadorService\nArbitroService\nTorneoService\nPartidoServiceImpl\nPagoServiceImpl\nAlineacionService\nAdministradorService\nAuditoriaService\nPerfilDeportivoServiceImpl\nOAuth2Service\nEquipoService]
    end

    subgraph Validators["Validators"]
        VAL[AccesoValidator\nRegistroValidator\nJugadorValidator\nPartidoValidator\nSancionValidator\nPagoValidator\nValidacionTorneo\nValidacionEquipo\nValidacionAlineacion\nPerfilDeportivoValidator\nAdministradorValidator\nAuditoriaValidator]
    end

    subgraph Patterns["Patrones"]
        PAT[Subject - Observer\nState - Partido\nState - Pago\nState - Torneo]
    end

    subgraph Repositories["Repositories"]
        REP[UsuarioRegistradoRepository\nJugadorRepository\nCapitanRepository\nOrganizadorRepository\nArbitroRepository\nTorneoRepository\nPartidoRepository\nPagoRepository\nAlineacionRepository\nAdministradorRepository\nRegistroAuditoriaRepository\nPerfilDeportivoRepository\nEquipoRepository\nSancionRepository]
    end

    subgraph Persistence["JPA"]
        ENT[Entities]
        MAP[Mappers MapStruct]
        JPA[JPA Repositories]
    end

    PG[(PostgreSQL)]

    CLI -->|HTTP Request| JF
    JF --> JS
    JF -->|request autenticado| Controllers
    OH -->|OAuth2 callback| Services
    Controllers --> Services
    Controllers --> Validators
    Services --> Validators
    Services --> Patterns
    Services --> Repositories
    Repositories --> ENT
    ENT <--> MAP
    MAP <--> JPA
    JPA --> PG
```
