# Backlog — TechCup Fútbol

**Escala de estimación:** Fibonacci (1, 2, 3, 5, 8, 13) — a definir con el equipo
**Sprints:** 4 (semanas 8–11)
**Equipo:** Shawarma FC (6 personas)

---

## Épica 1: Backend

> Cubre toda la lógica de negocio, modelos de base de datos, autenticación y APIs del sistema.

### HU-1.1 — Registro con correo institucional [Backend]
**Como** estudiante, graduado, profesor o personal administrativo
**Quiero** registrarme con mi correo institucional
**Para** acceder a la plataforma y participar en el torneo.

**Tareas:**
- T-1.1.1: Diseñar modelo de Usuario en la base de datos
- T-1.1.2: Implementar lógica de registro con validación de dominio institucional
- T-1.1.5: Escribir pruebas unitarias del registro

---

### HU-1.2 — Registro con Gmail para familiares [Backend]
**Como** familiar de un miembro de la comunidad
**Quiero** registrarme con mi correo personal de Gmail
**Para** poder participar en el torneo.

**Tareas:**
- T-1.2.1: Agregar validación de correo Gmail en la lógica de registro
- T-1.2.3: Escribir pruebas para registro con Gmail

---

### HU-1.3 — Inicio de sesión [Backend]
**Como** usuario registrado
**Quiero** iniciar sesión con mi correo y contraseña
**Para** acceder a las funcionalidades según mi rol.

**Tareas:**
- T-1.3.1: Implementar lógica de autenticación y generación de sesión
- T-1.3.2: Implementar cifrado de contraseñas (bcrypt)
- T-1.3.5: Escribir pruebas de autenticación

---

### HU-1.4 — Control de roles y permisos [Backend]
**Como** administrador
**Quiero** que cada usuario tenga un rol asignado
**Para** que solo pueda acceder a las funcionalidades permitidas.

**Tareas:**
- T-1.4.1: Diseñar modelo de roles en la base de datos
- T-1.4.2: Implementar lógica de autorización por rol
- T-1.4.4: Escribir pruebas de autorización

---

### HU-1.5 — Crear torneo [Backend]
**Como** organizador
**Quiero** crear un torneo con fecha inicial, fecha final, cantidad de equipos y costo
**Para** dar inicio al proceso de organización.

**Tareas:**
- T-1.5.1: Crear modelo Torneo en la base de datos
- T-1.5.2: Implementar lógica de creación de torneo con estado inicial "Borrador"
- T-1.5.3: Escribir pruebas de creación de torneo

---

### HU-1.6 — Cambiar estado del torneo [Backend]
**Como** organizador
**Quiero** iniciar y finalizar un torneo
**Para** controlar el ciclo de vida del torneo.

**Tareas:**
- T-1.6.1: Implementar máquina de estados (Borrador → Activo → En progreso → Finalizado)
- T-1.6.2: Escribir pruebas de transiciones de estado

---

### HU-1.7 — Consultar torneo [Backend]
**Como** usuario autenticado
**Quiero** ver la información de un torneo
**Para** conocer fechas, estado y detalles.

**Tareas:**
- T-1.7.1: Implementar lógica de consulta de torneo (individual y listado)

---

### HU-1.8 — Configurar torneo [Backend]
**Como** organizador
**Quiero** definir reglamento, fechas importantes, horarios, canchas y sanciones
**Para** que toda la información del torneo esté centralizada.

**Tareas:**
- T-1.8.1: Crear modelos de configuración (reglamento, canchas, horarios, sanciones) en la base de datos
- T-1.8.2: Implementar lógica de guardar y editar configuración
- T-1.8.3: Escribir pruebas de configuración

---

### HU-1.9 — Crear perfil deportivo [Backend]
**Como** jugador registrado
**Quiero** completar mi perfil con posiciones, dorsal, foto, edad, género e identificación
**Para** que los capitanes puedan conocerme y reclutarme.

**Tareas:**
- T-1.9.1: Crear modelo PerfilDeportivo en la base de datos
- T-1.9.2: Implementar lógica de creación y edición de perfil
- T-1.9.3: Implementar carga y almacenamiento de foto
- T-1.9.4: Escribir pruebas del módulo de perfil

---

### HU-1.10 — Marcar disponibilidad [Backend]
**Como** jugador sin equipo
**Quiero** marcarme como disponible
**Para** que los capitanes puedan encontrarme y enviarme invitaciones.

**Tareas:**
- T-1.10.1: Implementar lógica de cambio de disponibilidad con validación de equipo
- T-1.10.2: Escribir pruebas de disponibilidad

---

### HU-1.11 — Crear equipo [Backend]
**Como** jugador registrado
**Quiero** crear un equipo con nombre, escudo y colores de uniforme
**Para** participar como capitán en el torneo.

**Tareas:**
- T-1.11.1: Crear modelo Equipo en la base de datos
- T-1.11.2: Implementar lógica de creación de equipo con asignación automática de capitán
- T-1.11.3: Implementar carga y almacenamiento de escudo
- T-1.11.4: Escribir pruebas de creación de equipo

---

### HU-1.12 — Invitar jugadores al equipo [Backend]
**Como** capitán
**Quiero** enviar invitaciones a jugadores disponibles
**Para** completar la plantilla de mi equipo.

**Tareas:**
- T-1.12.1: Crear modelo Invitacion en la base de datos
- T-1.12.2: Implementar lógica de envío de invitación con validaciones (disponibilidad, equipo no lleno)
- T-1.12.3: Escribir pruebas de invitaciones

---

### HU-1.13 — Responder invitación [Backend]
**Como** jugador
**Quiero** aceptar o rechazar invitaciones de equipos
**Para** unirme al equipo que prefiera.

**Tareas:**
- T-1.13.1: Implementar lógica de aceptar/rechazar (al aceptar, rechazar las demás pendientes)
- T-1.13.2: Escribir pruebas de aceptación/rechazo

---

### HU-1.14 — Validar composición del equipo [Backend]
**Como** sistema
**Quiero** validar que un equipo cumpla las reglas de composición
**Para** asegurar que solo equipos válidos se inscriban.

**Tareas:**
- T-1.14.1: Implementar validación de mínimo 7 y máximo 12 jugadores
- T-1.14.2: Implementar validación de mayoría de programas permitidos
- T-1.14.3: Implementar validación de pertenencia a programas/maestrías autorizados
- T-1.14.4: Implementar validación de jugador único por equipo
- T-1.14.5: Escribir pruebas de todas las validaciones

---

### HU-1.15 — Buscar jugadores por filtros [Backend]
**Como** capitán
**Quiero** buscar jugadores disponibles por posición, semestre, edad, género, nombre o identificación
**Para** encontrar los jugadores que necesito para mi equipo.

**Tareas:**
- T-1.15.1: Implementar lógica de búsqueda con filtros combinables
- T-1.15.2: Escribir pruebas de búsqueda con filtros

---

### HU-1.16 — Subir comprobante de pago [Backend]
**Como** capitán
**Quiero** subir el comprobante de pago de mi equipo
**Para** completar la inscripción al torneo.

**Tareas:**
- T-1.16.1: Crear modelo Pago en la base de datos
- T-1.16.2: Implementar lógica de carga y almacenamiento del comprobante
- T-1.16.3: Escribir pruebas de carga de comprobante

---

### HU-1.17 — Verificar pago [Backend]
**Como** organizador
**Quiero** revisar los comprobantes de pago y aprobar o rechazar la inscripción
**Para** controlar qué equipos participan en el torneo.

**Tareas:**
- T-1.17.1: Implementar lógica de transiciones de estado (Pendiente → En revisión → Aprobado/Rechazado)
- T-1.17.2: Escribir pruebas de verificación de pago

---

### HU-1.18 — Definir alineación [Backend]
**Como** capitán
**Quiero** seleccionar titulares, reservas y formación táctica antes de un partido
**Para** organizar a mi equipo para el encuentro.

**Tareas:**
- T-1.18.1: Crear modelo Alineacion en la base de datos
- T-1.18.2: Implementar lógica de guardado de alineación con validación de 7 titulares
- T-1.18.3: Escribir pruebas de alineación

---

### HU-1.19 — Consultar alineación rival [Backend]
**Como** jugador o capitán
**Quiero** ver la alineación del equipo rival
**Para** prepararme para el partido.

**Tareas:**
- T-1.19.1: Implementar lógica de consulta de alineación rival (solo si fue registrada)

---

### HU-1.20 — Registrar resultado de partido [Backend]
**Como** organizador
**Quiero** registrar el marcador, goleadores y tarjetas de un partido
**Para** mantener los resultados actualizados.

**Tareas:**
- T-1.20.1: Crear modelos Partido, Gol, Tarjeta en la base de datos
- T-1.20.2: Implementar lógica de registro de resultado con validación de consistencia (marcador vs goleadores)
- T-1.20.3: Escribir pruebas de registro de resultado

---

### HU-1.21 — Consultar partidos como árbitro [Backend]
**Como** árbitro
**Quiero** ver los partidos que tengo asignados con fecha, hora, cancha y equipos
**Para** saber dónde y cuándo debo arbitrar.

**Tareas:**
- T-1.21.1: Implementar lógica de consulta de partidos filtrados por árbitro

---

### HU-1.22 — Calcular y mostrar tabla de posiciones [Backend]
**Como** usuario autenticado
**Quiero** ver la tabla de posiciones actualizada
**Para** conocer la clasificación de los equipos.

**Tareas:**
- T-1.22.1: Implementar lógica de cálculo de puntos (PJ, PG, PE, PP, GF, GC, DG, Pts)
- T-1.22.2: Implementar ordenamiento por puntos y diferencia de gol
- T-1.22.3: Escribir pruebas del cálculo de posiciones

---

### HU-1.23 — Generar llaves eliminatorias [Backend]
**Como** sistema
**Quiero** generar automáticamente los emparejamientos eliminatorios
**Para** organizar la fase final del torneo.

**Tareas:**
- T-1.23.1: Crear modelo LlaveEliminatoria en la base de datos
- T-1.23.2: Implementar generación aleatoria de emparejamientos iniciales
- T-1.23.3: Implementar avance automático de llaves al registrar resultados
- T-1.23.4: Escribir pruebas de generación y avance de llaves

---

### HU-1.24 — Consultar estadísticas del torneo [Backend]
**Como** usuario autenticado
**Quiero** ver los máximos goleadores, historial de partidos y resultados por equipo
**Para** seguir el desempeño de los jugadores y equipos.

**Tareas:**
- T-1.24.1: Implementar lógica de ranking de goleadores
- T-1.24.2: Implementar consulta de historial de partidos
- T-1.24.3: Implementar consulta de resultados por equipo
- T-1.24.4: Escribir pruebas de estadísticas

---

### HU-1.25 — Registrar organizadores y árbitros [Backend]
**Como** administrador
**Quiero** registrar usuarios con rol de Organizador o Árbitro en la plataforma
**Para** que puedan acceder y ejercer sus funciones en el torneo.

**Tareas:**
- T-1.25.1: Implementar lógica de creación de usuario con rol Organizador o Árbitro
- T-1.25.2: Implementar validación de correo no duplicado para estos roles
- T-1.25.3: Escribir pruebas del registro administrativo

---

## Épica 2: Frontend

> Cubre todas las vistas, formularios y componentes de la aplicación web (React + TypeScript).

### HU-2.1 — Registro con correo institucional [Frontend]
**Como** estudiante, graduado, profesor o personal administrativo
**Quiero** registrarme con mi correo institucional
**Para** acceder a la plataforma y participar en el torneo.

**Tareas:**
- T-2.1.1: Crear formulario de registro (frontend)
- T-2.1.2: Implementar validaciones de campos en el formulario (frontend)

---

### HU-2.2 — Registro con Gmail para familiares [Frontend]
**Como** familiar de un miembro de la comunidad
**Quiero** registrarme con mi correo personal de Gmail
**Para** poder participar en el torneo.

**Tareas:**
- T-2.2.1: Agregar opción de tipo "Familiar" en el formulario de registro (frontend)

---

### HU-2.3 — Inicio de sesión [Frontend]
**Como** usuario registrado
**Quiero** iniciar sesión con mi correo y contraseña
**Para** acceder a las funcionalidades según mi rol.

**Tareas:**
- T-2.3.1: Crear pantalla de login (frontend)
- T-2.3.2: Implementar manejo de sesión en el cliente (frontend)

---

### HU-2.4 — Control de roles y permisos [Frontend]
**Como** administrador
**Quiero** que cada usuario tenga un rol asignado
**Para** que solo pueda acceder a las funcionalidades permitidas.

**Tareas:**
- T-2.4.1: Implementar protección de rutas por rol (frontend)

---

### HU-2.5 — Crear torneo [Frontend]
**Como** organizador
**Quiero** crear un torneo con fecha inicial, fecha final, cantidad de equipos y costo
**Para** dar inicio al proceso de organización.

**Tareas:**
- T-2.5.1: Crear formulario de creación de torneo (frontend)
- T-2.5.2: Implementar validaciones de fechas y campos (frontend)

---

### HU-2.6 — Cambiar estado del torneo [Frontend]
**Como** organizador
**Quiero** iniciar y finalizar un torneo
**Para** controlar el ciclo de vida del torneo.

**Tareas:**
- T-2.6.1: Agregar botones de cambio de estado en la vista del torneo (frontend)

---

### HU-2.7 — Consultar torneo [Frontend]
**Como** usuario autenticado
**Quiero** ver la información de un torneo
**Para** conocer fechas, estado y detalles.

**Tareas:**
- T-2.7.1: Crear vista de detalle de torneo (frontend)
- T-2.7.2: Crear vista de listado de torneos (frontend)

---

### HU-2.8 — Configurar torneo [Frontend]
**Como** organizador
**Quiero** definir reglamento, fechas importantes, horarios, canchas y sanciones
**Para** que toda la información del torneo esté centralizada.

**Tareas:**
- T-2.8.1: Crear formularios de configuración (frontend)
- T-2.8.2: Crear vista pública de reglamento y fechas (frontend)

---

### HU-2.9 — Crear perfil deportivo [Frontend]
**Como** jugador registrado
**Quiero** completar mi perfil con posiciones, dorsal, foto, edad, género e identificación
**Para** que los capitanes puedan conocerme y reclutarme.

**Tareas:**
- T-2.9.1: Crear formulario de perfil deportivo (frontend)

---

### HU-2.10 — Marcar disponibilidad [Frontend]
**Como** jugador sin equipo
**Quiero** marcarme como disponible
**Para** que los capitanes puedan encontrarme y enviarme invitaciones.

**Tareas:**
- T-2.10.1: Agregar toggle de disponibilidad en el perfil (frontend)

---

### HU-2.11 — Crear equipo [Frontend]
**Como** jugador registrado
**Quiero** crear un equipo con nombre, escudo y colores de uniforme
**Para** participar como capitán en el torneo.

**Tareas:**
- T-2.11.1: Crear formulario de creación de equipo (frontend)

---

### HU-2.12 — Invitar jugadores al equipo [Frontend]
**Como** capitán
**Quiero** enviar invitaciones a jugadores disponibles
**Para** completar la plantilla de mi equipo.

**Tareas:**
- T-2.12.1: Crear interfaz de envío de invitaciones (frontend)

---

### HU-2.13 — Responder invitación [Frontend]
**Como** jugador
**Quiero** aceptar o rechazar invitaciones de equipos
**Para** unirme al equipo que prefiera.

**Tareas:**
- T-2.13.1: Crear vista de invitaciones recibidas (frontend)

---

### HU-2.14 — Validar composición del equipo [Frontend]
**Como** sistema
**Quiero** validar que un equipo cumpla las reglas de composición
**Para** asegurar que solo equipos válidos se inscriban.

**Tareas:**
- T-2.14.1: Mostrar estado de validación en el panel del equipo (frontend)

---

### HU-2.15 — Buscar jugadores por filtros [Frontend]
**Como** capitán
**Quiero** buscar jugadores disponibles por posición, semestre, edad, género, nombre o identificación
**Para** encontrar los jugadores que necesito para mi equipo.

**Tareas:**
- T-2.15.1: Crear vista de búsqueda con filtros (frontend)
- T-2.15.2: Integrar botón de invitar desde los resultados de búsqueda (frontend)

---

### HU-2.16 — Subir comprobante de pago [Frontend]
**Como** capitán
**Quiero** subir el comprobante de pago de mi equipo
**Para** completar la inscripción al torneo.

**Tareas:**
- T-2.16.1: Crear formulario de carga de comprobante (frontend)

---

### HU-2.17 — Verificar pago [Frontend]
**Como** organizador
**Quiero** revisar los comprobantes de pago y aprobar o rechazar la inscripción
**Para** controlar qué equipos participan en el torneo.

**Tareas:**
- T-2.17.1: Crear panel de revisión de pagos con vista del comprobante (frontend)

---

### HU-2.18 — Definir alineación [Frontend]
**Como** capitán
**Quiero** seleccionar titulares, reservas y formación táctica antes de un partido
**Para** organizar a mi equipo para el encuentro.

**Tareas:**
- T-2.18.1: Crear interfaz visual de cancha para ubicar jugadores (frontend)
- T-2.18.2: Implementar selector de formación táctica (frontend)
- T-2.18.3: Implementar drag & drop o selección de posiciones (frontend)

---

### HU-2.19 — Consultar alineación rival [Frontend]
**Como** jugador o capitán
**Quiero** ver la alineación del equipo rival
**Para** prepararme para el partido.

**Tareas:**
- T-2.19.1: Crear vista de alineación rival con representación visual (frontend)

---

### HU-2.20 — Registrar resultado de partido [Frontend]
**Como** organizador
**Quiero** registrar el marcador, goleadores y tarjetas de un partido
**Para** mantener los resultados actualizados.

**Tareas:**
- T-2.20.1: Crear formulario de registro de resultado (frontend)

---

### HU-2.21 — Consultar partidos como árbitro [Frontend]
**Como** árbitro
**Quiero** ver los partidos que tengo asignados con fecha, hora, cancha y equipos
**Para** saber dónde y cuándo debo arbitrar.

**Tareas:**
- T-2.21.1: Crear vista de partidos asignados al árbitro (frontend)

---

### HU-2.22 — Calcular y mostrar tabla de posiciones [Frontend]
**Como** usuario autenticado
**Quiero** ver la tabla de posiciones actualizada
**Para** conocer la clasificación de los equipos.

**Tareas:**
- T-2.22.1: Crear vista de tabla de posiciones (frontend)

---

### HU-2.23 — Generar llaves eliminatorias [Frontend]
**Como** sistema
**Quiero** generar automáticamente los emparejamientos eliminatorios
**Para** organizar la fase final del torneo.

**Tareas:**
- T-2.23.1: Crear vista de bracket eliminatorio (frontend)

---

### HU-2.24 — Consultar estadísticas del torneo [Frontend]
**Como** usuario autenticado
**Quiero** ver los máximos goleadores, historial de partidos y resultados por equipo
**Para** seguir el desempeño de los jugadores y equipos.

**Tareas:**
- T-2.24.1: Crear vista de estadísticas con filtro por equipo (frontend)

---

### HU-2.25 — Registrar organizadores y árbitros [Frontend]
**Como** administrador
**Quiero** un formulario para registrar organizadores y árbitros
**Para** gestionar fácilmente quiénes cumplen esos roles en el torneo.

**Tareas:**
- T-2.25.1: Crear formulario de registro administrativo con selector de rol (frontend)
- T-2.25.2: Implementar validaciones de campos en el formulario (frontend)

---