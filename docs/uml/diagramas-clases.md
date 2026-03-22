# Diagrama de clases

Acá se muestra cómo están organizadas las clases del sistema, qué datos maneja cada una y cómo se relacionan entre sí. El diagrama está dividido en 4 partes porque el sistema tiene bastantes entidades.

---

**Parte 1 — Los actores del sistema: usuarios, jugadores y equipos**

Acá se ve la base del sistema. `Usuario` es la clase padre de todos los que pueden entrar a la plataforma, tiene los datos básicos como nombre, correo, contraseña y tipo de usuario. De ahí heredan `Jugador`, `Capitan`, `Arbitro` y `Organizador`, cada uno con sus propias cosas. Por ejemplo, `Jugador` agrega el número de camiseta, la posición en la cancha y si está disponible para jugar. `Equipo` agrupa jugadores bajo un capitán y tiene nombre, escudo y colores. `Torneo` tiene toda la configuración del torneo: fechas, costo, cantidad de equipos y reglamento.

<img width="1268" height="724" alt="Diagrama de clases parte 1" src="https://github.com/user-attachments/assets/3d7f2d01-8cdc-47e3-91c8-237de78c26b5" />

---

**Parte 2 — Cómo cambia de estado el torneo y el pago**

Acá se ve cómo se maneja el hecho de que un torneo o un pago pueden estar en diferentes momentos. Un torneo puede estar `CREADO`, `EN_CURSO` o `FINALIZADO`, y dependiendo de en cuál esté, ciertas cosas están permitidas o no. Lo mismo pasa con el pago: va de `PENDIENTE` → `EN_REVISION` → `APROBADO` o `RECHAZADO`. Esto se implementó con el patrón de diseño **State**, que básicamente hace que cada estado sepa qué puede hacer y qué no, en vez de tener un montón de ifs por todo el código.

<img width="618" height="708" alt="Diagrama de clases parte 2" src="https://github.com/user-attachments/assets/61635ba4-e939-4cef-a4a4-eccec6da1458" />

---

**Parte 3 — Los partidos y las alineaciones**

Acá se ve `Partido`, que tiene dos equipos (local y visitante), la fecha, la cancha, el marcador y las listas de goles y tarjetas. El partido también tiene sus propios estados: `PROGRAMADO` → `EN_CURSO` → `FINALIZADO`, y cada estado controla qué se puede hacer (no puedes registrar un gol en un partido que no ha empezado, por ejemplo). `Alineacion` está ligada a un equipo y un partido, y guarda la formación táctica junto con los jugadores titulares y reservas.

<img width="1453" height="622" alt="Diagrama de clases parte 3" src="https://github.com/user-attachments/assets/9d080056-8f94-406b-81d8-2bf0a865540a" />

---

**Parte 4 — Notificaciones y utilidades**

Acá se ve cómo el sistema avisa cuando pasan cosas importantes, como cuando inicia un partido. Se usó el patrón **Observer**: hay una clase `Subject` que mantiene una lista de "escuchas" y cuando ocurre un evento los notifica a todos. `NotificadorTorneo` es uno de esos escuchas. También están las clases de utilidad: `PasswordUtil` que se encarga de cifrar las contraseñas, y `AccesoMapper` que convierte los datos del login/registro entre lo que llega del cliente y lo que maneja el sistema internamente.

<img width="1362" height="365" alt="Diagrama de clases parte 4" src="https://github.com/user-attachments/assets/f94f411a-6273-4dc7-8db2-0b153d456566" />
