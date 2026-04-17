# Diagrama de Secuencia — OAuth2 y Administrador

Aca se muestran dos flujos. El primero es el login con Google: el usuario entra con su Gmail, Google redirige al sistema, se extrae el email, si es nuevo se registra automaticamente y se devuelve un token JWT con rol JUGADOR. El segundo es el flujo del administrador: hace login con su correo y contrasena propios, el sistema le genera un token de sesion UUID, y con ese token puede registrar organizadores y arbitros. Cada accion del administrador queda registrada en el historial de auditoria.

---

```mermaid
sequenceDiagram
    actor Usuario
    actor Administrador
    participant Navegador
    participant OAuth2SuccessHandler
    participant OAuth2Service
    participant UsuarioRegistradoRepository
    participant JwtService
    participant AdministradorController
    participant AutenticacionAdministradorService
    participant AdministradorService
    participant AuditoriaService

    %% Login con Google OAuth2
    Usuario->>Navegador: GET /oauth2/authorization/google
    Navegador->>Navegador: Redirige a Google
    Usuario->>Navegador: Inicia sesion con Gmail
    Navegador->>OAuth2SuccessHandler: onAuthenticationSuccess(token)
    OAuth2SuccessHandler->>OAuth2Service: procesarCallback(token)
    OAuth2Service->>OAuth2Service: extraer email y nombre de atributos
    alt email no es @gmail.com
        OAuth2Service-->>OAuth2SuccessHandler: IllegalArgumentException
        OAuth2SuccessHandler-->>Navegador: 400 Bad Request
    end
    OAuth2Service->>UsuarioRegistradoRepository: findByEmail(email)
    alt usuario nuevo
        UsuarioRegistradoRepository-->>OAuth2Service: Optional.empty()
        OAuth2Service->>UsuarioRegistradoRepository: save(nuevo usuario) → UUID generado
    end
    OAuth2Service->>JwtService: generarToken(email, JUGADOR)
    JwtService-->>OAuth2Service: JWT token
    OAuth2Service-->>OAuth2SuccessHandler: OAuth2Response token
    OAuth2SuccessHandler-->>Navegador: 200 OK token

    %% Login de Administrador
    Administrador->>AdministradorController: POST /api/admin/login {email, password}
    AdministradorController->>AutenticacionAdministradorService: login(email, password)
    AutenticacionAdministradorService->>AutenticacionAdministradorService: findByEmail + verificar password
    alt credenciales incorrectas
        AutenticacionAdministradorService-->>AdministradorController: AutenticacionAdminException
        AdministradorController-->>Administrador: 401 Unauthorized
    end
    AutenticacionAdministradorService->>AutenticacionAdministradorService: UUID token → sesiones.put(token, adminId)
    AutenticacionAdministradorService-->>AdministradorController: token
    AdministradorController->>AuditoriaService: registrarEvento(adminId, email, LOGIN_ADMIN, descripcion)
    AdministradorController-->>Administrador: 200 OK id, nombre, email, token

    %% Registrar organizador o arbitro
    Administrador->>AdministradorController: POST /api/admin/users {nombre, email, password, tipoUsuario, rol}
    AdministradorController->>AutenticacionAdministradorService: validarSesion(adminId, token)
    alt sesion invalida
        AutenticacionAdministradorService-->>AdministradorController: AutenticacionAdminException
        AdministradorController-->>Administrador: 401 Unauthorized
    end
    AdministradorController->>AdministradorService: registrarUsuarioAdministrativo(adminId, request)
    AdministradorService->>AdministradorService: switch rol → ORGANIZADOR o ARBITRO
    AdministradorService->>AdministradorService: validarCorreoUnico(email)
    AdministradorService->>AdministradorService: save(usuario) → UUID generado
    AdministradorService->>AuditoriaService: registrarEvento(adminId, email, REGISTRO_ORGANIZADOR/ARBITRO, descripcion)
    AdministradorService-->>AdministradorController: Usuario registrado
    AdministradorController-->>Administrador: 200 OK id, nombre, email, tipoUsuario, rol, adminId
```
