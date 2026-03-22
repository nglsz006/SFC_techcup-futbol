# Diagrama de clases

El diagrama de clases representa la estructura del dominio del sistema TECHCUP FÚTBOL. Muestra las entidades principales, sus atributos, métodos y las relaciones entre ellas. El diseño sigue una arquitectura en capas donde el modelo del dominio es independiente de la infraestructura.

---

**Parte 1 — Modelos principales: Usuario, Jugador, Equipo y Torneo**

Esta parte muestra la jerarquía de usuarios del sistema. `Usuario` es una clase abstracta con atributos comunes como `id`, `name`, `email`, `password` y `userType`. De ella heredan `Jugador`, `Capitan`, `Arbitro` y `Organizador`, cada uno con sus propios atributos y responsabilidades. `Jugador` agrega campos como `jerseyNumber`, `position`, `available` y `equipoId`. `Equipo` agrupa jugadores bajo un capitán y tiene atributos como `nombre`, `escudo` y colores. `Torneo` contiene la configuración del torneo: fechas, costo, cantidad de equipos, reglamento y estado.

<img width="1268" height="724" alt="Diagrama de clases parte 1" src="https://github.com/user-attachments/assets/3d7f2d01-8cdc-47e3-91c8-237de78c26b5" />

---

**Parte 2 — Patrón State en Torneo y Pago**

Esta parte muestra cómo se implementó el patrón de diseño **State** para manejar los estados del torneo y del pago. `EstadoTorneoInterface` define los métodos `iniciar()`, `finalizar()` y `puedeInscribirEquipos()`. Las clases `TorneoCreado`, `TorneoEnCurso` y `TorneoFinalizado` implementan ese contrato y controlan las transiciones válidas. De forma similar, `PagoState` define `avanzar()` y `rechazar()`, implementado por `PendienteState`, `EnRevisionState`, `AprobadoState` y `RechazadoState`. Esto evita condicionales dispersos y centraliza la lógica de transición en cada estado.

<img width="618" height="708" alt="Diagrama de clases parte 2" src="https://github.com/user-attachments/assets/61635ba4-e939-4cef-a4a4-eccec6da1458" />

---

**Parte 3 — Partido, Alineación y patrón State en Partido**

Aquí se ve la clase `Partido` con sus relaciones hacia `Torneo`, `Equipo` (local y visitante), y las clases internas `Gol` y `Tarjeta`. El partido también usa el patrón State a través de `PartidoState`, con implementaciones `ProgramadoState`, `EnCursoState` y `FinalizadoPartidoState`, que controlan cuándo se puede iniciar, registrar resultado o finalizar un partido. `Alineacion` está asociada a un equipo y un partido, y contiene la formación táctica junto con las listas de titulares y reservas.

<img width="1453" height="622" alt="Diagrama de clases parte 3" src="https://github.com/user-attachments/assets/9d080056-8f94-406b-81d8-2bf0a865540a" />

---

**Parte 4 — Patrón Observer y utilidades**

Esta parte muestra el patrón de diseño **Observer** implementado en la capa de servicios. `Subject` es una clase abstracta que mantiene una lista de `Observer` y expone los métodos `agregarObserver()`, `removerObserver()` y `notificar()`. `NotificadorTorneo` implementa `Observer` y reacciona a eventos del sistema como el inicio de un partido. También se ven las clases utilitarias `PasswordUtil` para cifrado de contraseñas y `AccesoMapper` para transformar datos entre capas.

<img width="1362" height="365" alt="Diagrama de clases parte 4" src="https://github.com/user-attachments/assets/f94f411a-6273-4dc7-8db2-0b153d456566" />
