# Diagrama de Contexto

Este diagrama muestra el sistema completo de un vistazo: quiénes lo usan y cómo interactúan con él.

Hay seis tipos de personas que usan el sistema. El jugador se registra, crea su perfil deportivo y espera que lo inviten a un equipo. El capitán crea el equipo, invita jugadores, sube el comprobante de pago y define la alineación antes de cada partido. El organizador crea y gestiona el torneo, programa los partidos y aprueba o rechaza los pagos. El árbitro inicia y finaliza los partidos, y registra los goles y sanciones. El administrador es quien registra a los organizadores y árbitros en el sistema, y puede consultar el historial de auditoría. El usuario Google es cualquier persona que entra con su cuenta de Gmail en vez de registrarse manualmente.

Por fuera del sistema hay tres piezas clave: PostgreSQL es la base de datos donde se guarda todo, Google OAuth2 es el servicio de Google que maneja el login con Gmail, y JWT Service es el que genera y valida los tokens de seguridad que identifican a cada usuario en cada petición.

---

```mermaid
graph TD
    subgraph Actores
        J([Jugador])
        C([Capitan])
        O([Organizador])
        A([Arbitro])
        AD([Administrador])
        G([Usuario Google])
    end

    subgraph TECHCUP_FUTBOL["TECHCUP FUTBOL API"]
        ACC[Acceso / Auth]
        USR[Gestion Usuarios]
        TOR[Gestion Torneos]
        PAR[Gestion Partidos]
        PAG[Gestion Pagos]
        ALI[Gestion Alineaciones]
        AUD[Auditoria]
    end

    subgraph Externos
        PG[(PostgreSQL)]
        GG[Google OAuth2]
        JWT[JWT Service]
    end

    J -->|Registro / Login| ACC
    J -->|Perfil deportivo| USR
    C -->|Crear equipo / Invitar jugadores| USR
    C -->|Subir comprobante| PAG
    C -->|Definir alineacion| ALI
    O -->|Crear / Gestionar torneo| TOR
    O -->|Crear partidos| PAR
    O -->|Aprobar / Rechazar pagos| PAG
    A -->|Iniciar / Finalizar partido| PAR
    A -->|Registrar goles y sanciones| PAR
    AD -->|Registrar organizadores y arbitros| USR
    AD -->|Consultar auditoria| AUD
    G -->|Login con Google| GG

    ACC --> JWT
    GG --> ACC
    TECHCUP_FUTBOL --> PG
```
