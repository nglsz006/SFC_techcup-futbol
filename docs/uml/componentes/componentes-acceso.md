# Componentes — Acceso y Seguridad

Aca se muestra como entra un usuario al sistema. Hay dos formas: con correo y contrasena, o con Google. En ambos casos el sistema devuelve un token JWT que el usuario debe usar en todas las peticiones siguientes.

Cuando alguien se registra, el `AccesoValidator` verifica que el correo sea valido segun el tipo de usuario (institucional o Gmail) y que la contrasena cumpla los requisitos. La contrasena se guarda cifrada con BCrypt gracias a `PasswordUtil`. Cuando alguien hace login, el sistema detecta automaticamente si es organizador, arbitro, capitan o jugador y genera el token con el rol correcto.

Para el login con Google, el `OAuth2SuccessHandler` recibe el callback de Google, el `OAuth2Service` registra al usuario si es nuevo y devuelve solo el token, sin exponer datos personales.

---
<img width="1451" height="566" alt="image" src="https://github.com/user-attachments/assets/76d44266-744a-4867-a075-0d633da86a33" />

