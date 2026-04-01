# Componentes — Acceso y Seguridad

Acá se muestra cómo entra un usuario al sistema. Hay dos formas: con correo y contraseña, o con Google. En ambos casos el sistema devuelve un token JWT que el usuario debe usar en todas las peticiones siguientes.

Cuando alguien se registra, el `AccesoValidator` verifica que el correo sea válido y que la contraseña cumpla los requisitos. La contraseña se guarda cifrada con BCrypt gracias a `PasswordUtil`. Cuando alguien hace login, el sistema detecta automáticamente si es organizador, árbitro, capitán o jugador y genera el token con el rol correcto.

Para el login con Google, el `OAuth2SuccessHandler` recibe el callback de Google, el `OAuth2Service` registra al usuario si es nuevo y devuelve solo el token, sin exponer datos personales.

---

```mermaid
graph TD
    subgraph Entrada
        CLI[Cliente]
        GG[Google OAuth2]
    end

    subgraph Seguridad["Seguridad"]
        JF[JwtFilter]
        JS[JwtService]
        OH[OAuth2SuccessHandler]
        SC[SecurityConfig]
    end

    subgraph Acceso["Acceso"]
        AC[AccesoController]
        OC[OAuth2Controller]
        AS[AccesoServiceImpl]
        O2S[OAuth2Service]
        AV[AccesoValidator]
        AM[AccesoMapper]
        PU[PasswordUtil]
    end

    subgraph Repos["Repositorios"]
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
    O2S -->|OAuth2Response token| CLI
    AC --> AV
    AC --> AS
    AS --> UR
    AS --> OR
    AS --> ARR
    AS --> CR
    AS --> JS
    AS --> AM
    AM --> PU
    CLI -->|todas las peticiones con Bearer token| JF
    JF --> JS
    JF -->|request autenticado| AC
```
