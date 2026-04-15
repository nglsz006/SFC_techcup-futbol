# Diagrama de Secuencia — Registro de Usuario

Aca se muestra paso a paso como se registra un usuario nuevo. El cliente manda el nombre, correo, contrasena y tipo de usuario. Primero se validan los datos: que el nombre no este vacio, que el correo sea valido segun el tipo de usuario (institucional o Gmail), y que la contrasena cumpla los requisitos. Si algo falla, se devuelve un error 400. Si todo esta bien, se verifica que el correo no este ya registrado. Si no existe, se cifra la contrasena con BCrypt y se guarda el usuario con un UUID generado automaticamente.

---

```mermaid
sequenceDiagram
    actor Cliente
    participant AccesoController
    participant AccesoValidator
    participant AccesoServiceImpl
    participant UsuarioRegistradoRepository
    participant AccesoMapper
    participant PasswordUtil

    Cliente->>AccesoController: POST /api/access/register {nombre, email, password, tipoUsuario}
    AccesoController->>AccesoValidator: validarRegistro(request)
    AccesoValidator->>AccesoValidator: nombreValido(nombre)
    AccesoValidator->>AccesoValidator: correoValido(email)
    AccesoValidator->>AccesoValidator: contrasenaValida(password)
    AccesoValidator->>AccesoValidator: correoValidoSegunTipo(email, tipoUsuario)
    AccesoValidator->>AccesoValidator: si ESTUDIANTE, carrera obligatoria
    alt validacion falla
        AccesoValidator-->>AccesoController: IllegalArgumentException
        AccesoController-->>Cliente: 400 Bad Request
    end
    AccesoController->>AccesoServiceImpl: registrar(request)
    AccesoServiceImpl->>UsuarioRegistradoRepository: findByEmail(email)
    alt email ya existe
        UsuarioRegistradoRepository-->>AccesoServiceImpl: Optional.of(usuario)
        AccesoServiceImpl-->>AccesoController: IllegalStateException
        AccesoController-->>Cliente: 409 Conflict
    end
    UsuarioRegistradoRepository-->>AccesoServiceImpl: Optional.empty()
    AccesoServiceImpl->>AccesoMapper: toModelo(request)
    AccesoMapper->>PasswordUtil: cifrar(password)
    PasswordUtil-->>AccesoMapper: passwordHash BCrypt
    AccesoMapper-->>AccesoServiceImpl: UsuarioRegistrado
    AccesoServiceImpl->>UsuarioRegistradoRepository: save(usuario)
    UsuarioRegistradoRepository-->>AccesoServiceImpl: UsuarioRegistrado guardado con UUID
    AccesoServiceImpl->>AccesoMapper: toUsuarioResponse(usuario)
    AccesoMapper-->>AccesoServiceImpl: UsuarioResponse
    AccesoServiceImpl-->>AccesoController: UsuarioResponse
    AccesoController-->>Cliente: 200 OK {id, nombre, email, tipoUsuario}
```
