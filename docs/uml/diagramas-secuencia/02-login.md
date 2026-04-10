# Diagrama de Secuencia — Login

Aca se muestra como entra un usuario al sistema. El cliente manda el correo y la contrasena. El sistema busca al usuario en orden: primero verifica si es organizador, luego arbitro, luego capitan, y por ultimo jugador registrado. Segun quien sea, genera un token JWT con el rol correspondiente. Si el correo no existe en ninguna tabla o la contrasena es incorrecta, devuelve un error 400.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant AccesoController
    participant AccesoValidator
    participant AccesoServiceImpl
    participant OrganizadorRepository
    participant ArbitroRepository
    participant CapitanRepository
    participant UsuarioRegistradoRepository
    participant PasswordUtil
    participant JwtService

    Cliente->>AccesoController: POST /api/access/login {email, password}
    AccesoController->>AccesoValidator: validarLogin(request)
    alt validacion falla
        AccesoValidator-->>AccesoController: IllegalArgumentException
        AccesoController-->>Cliente: 400 Bad Request
    end
    AccesoController->>AccesoServiceImpl: login(request)

    AccesoServiceImpl->>OrganizadorRepository: findByEmail(email)
    alt es organizador
        OrganizadorRepository-->>AccesoServiceImpl: Optional.of(organizador)
        AccesoServiceImpl->>PasswordUtil: verificar(password, hash)
        AccesoServiceImpl->>JwtService: generarToken(email, ORGANIZADOR)
        JwtService-->>AccesoServiceImpl: JWT token
        AccesoServiceImpl-->>AccesoController: LoginResponse
        AccesoController-->>Cliente: 200 OK {token, nombre, email, tipoUsuario}
    end

    AccesoServiceImpl->>ArbitroRepository: findByEmail(email)
    alt es arbitro
        ArbitroRepository-->>AccesoServiceImpl: Optional.of(arbitro)
        AccesoServiceImpl->>JwtService: generarToken(email, ARBITRO)
        JwtService-->>AccesoServiceImpl: JWT token
        AccesoServiceImpl-->>AccesoController: LoginResponse
        AccesoController-->>Cliente: 200 OK {token, nombre, email, tipoUsuario}
    end

    AccesoServiceImpl->>CapitanRepository: findByEmail(email)
    alt es capitan
        CapitanRepository-->>AccesoServiceImpl: Optional.of(capitan)
        AccesoServiceImpl->>JwtService: generarToken(email, CAPITAN)
        JwtService-->>AccesoServiceImpl: JWT token
        AccesoServiceImpl-->>AccesoController: LoginResponse
        AccesoController-->>Cliente: 200 OK {token, nombre, email, tipoUsuario}
    end

    AccesoServiceImpl->>UsuarioRegistradoRepository: findByEmail(email)
    alt usuario no encontrado
        UsuarioRegistradoRepository-->>AccesoServiceImpl: Optional.empty()
        AccesoServiceImpl-->>AccesoController: IllegalArgumentException
        AccesoController-->>Cliente: 400 Bad Request
    end
    UsuarioRegistradoRepository-->>AccesoServiceImpl: Optional.of(usuario)
    AccesoServiceImpl->>PasswordUtil: verificar(password, hash)
    alt password incorrecta
        PasswordUtil-->>AccesoServiceImpl: false
        AccesoServiceImpl-->>AccesoController: IllegalArgumentException
        AccesoController-->>Cliente: 400 Bad Request
    end
    AccesoServiceImpl->>JwtService: generarToken(email, JUGADOR)
    JwtService-->>AccesoServiceImpl: JWT token
    AccesoServiceImpl-->>AccesoController: LoginResponse
    AccesoController-->>Cliente: 200 OK {token, nombre, email, tipoUsuario}
```
