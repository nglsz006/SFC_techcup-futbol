# Backlog — TechCup Fútbol

**Escala de estimación:** Fibonacci (1, 2, 3, 5, 8, 13) — a definir con el equipo
**Sprints:** 4 (semanas 8–11)
**Equipo:** Shawarma FC (6 personas)

---

## Épica 1: Autenticación y Registro

> Permite a los usuarios registrarse, iniciar sesión y gestionar sus roles en la plataforma.

### HU-1.1 — Registro con correo institucional
**Como** estudiante, graduado, profesor o personal administrativo
**Quiero** registrarme con mi correo institucional
**Para** acceder a la plataforma y participar en el torneo.

**Tareas:**
- T-1.1.1: Diseñar modelo de Usuario en la base de datos
- T-1.1.2: Implementar lógica de registro con validación de dominio institucional
- T-1.1.3: Crear formulario de registro (frontend)
- T-1.1.4: Implementar validaciones de campos en el formulario (frontend)
- T-1.1.5: Escribir pruebas unitarias del registro

### HU-1.2 — Registro con Gmail para familiares
**Como** familiar de un miembro de la comunidad
**Quiero** registrarme con mi correo personal de Gmail
**Para** poder participar en el torneo.

**Tareas:**
- T-1.2.1: Agregar validación de correo Gmail en la lógica de registro
- T-1.2.2: Agregar opción de tipo "Familiar" en el formulario de registro (frontend)
- T-1.2.3: Escribir pruebas para registro con Gmail

### HU-1.3 — Inicio de sesión
**Como** usuario registrado
**Quiero** iniciar sesión con mi correo y contraseña
**Para** acceder a las funcionalidades según mi rol.

**Tareas:**
- T-1.3.1: Implementar lógica de autenticación y generación de sesión
- T-1.3.2: Implementar cifrado de contraseñas (bcrypt)
- T-1.3.3: Crear pantalla de login (frontend)
- T-1.3.4: Implementar manejo de sesión en el cliente (frontend)
- T-1.3.5: Escribir pruebas de autenticación

### HU-1.4 — Control de roles y permisos
**Como** administrador
**Quiero** que cada usuario tenga un rol asignado
**Para** que solo pueda acceder a las funcionalidades permitidas.

**Tareas:**
- T-1.4.1: Diseñar modelo de roles en la base de datos
- T-1.4.2: Implementar lógica de autorización por rol
- T-1.4.3: Implementar protección de rutas por rol (frontend)
- T-1.4.4: Escribir pruebas de autorización
---

## Épica 2: Gestión de Torneos

> Permite a los organizadores crear, configurar y administrar torneos.

### HU-2.1 — Crear torneo
**Como** organizador
**Quiero** crear un torneo con fecha inicial, fecha final, cantidad de equipos y costo
**Para** dar inicio al proceso de organización.

**Tareas:**
- T-2.1.1: Crear modelo Torneo en la base de datos
- T-2.1.2: Implementar lógica de creación de torneo con estado inicial "Borrador"
- T-2.1.3: Crear formulario de creación de torneo (frontend)
- T-2.1.4: Implementar validaciones de fechas y campos (frontend)
- T-2.1.5: Escribir pruebas de creación de torneo

### HU-2.2 — Cambiar estado del torneo
**Como** organizador
**Quiero** iniciar y finalizar un torneo
**Para** controlar el ciclo de vida del torneo.

**Tareas:**
- T-2.2.1: Implementar máquina de estados (Borrador → Activo → En progreso → Finalizado)
- T-2.2.2: Agregar botones de cambio de estado en la vista del torneo (frontend)
- T-2.2.3: Escribir pruebas de transiciones de estado

### HU-2.3 — Consultar torneo
**Como** usuario autenticado
**Quiero** ver la información de un torneo
**Para** conocer fechas, estado y detalles.

**Tareas:**
- T-2.3.1: Implementar lógica de consulta de torneo (individual y listado)
- T-2.3.2: Crear vista de detalle de torneo (frontend)
- T-2.3.3: Crear vista de listado de torneos (frontend)

### HU-2.4 — Configurar torneo
**Como** organizador
**Quiero** definir reglamento, fechas importantes, horarios, canchas y sanciones
**Para** que toda la información del torneo esté centralizada.

**Tareas:**
- T-2.4.1: Crear modelos de configuración (reglamento, canchas, horarios, sanciones) en la base de datos
- T-2.4.2: Implementar lógica de guardar y editar configuración
- T-2.4.3: Crear formularios de configuración (frontend)
- T-2.4.4: Crear vista pública de reglamento y fechas (frontend)
- T-2.4.5: Escribir pruebas de configuración

---

## Épica 3: Perfil de Jugador

> Permite a los participantes crear y gestionar su perfil deportivo.

### HU-3.1 — Crear perfil deportivo
**Como** jugador registrado
**Quiero** completar mi perfil con posiciones, dorsal, foto, edad, género e identificación
**Para** que los capitanes puedan conocerme y reclutarme.

**Tareas:**
- T-3.1.1: Crear modelo PerfilDeportivo en la base de datos
- T-3.1.2: Implementar lógica de creación y edición de perfil
- T-3.1.3: Implementar carga y almacenamiento de foto
- T-3.1.4: Crear formulario de perfil deportivo (frontend)
- T-3.1.5: Escribir pruebas del módulo de perfil

### HU-3.2 — Marcar disponibilidad
**Como** jugador sin equipo
**Quiero** marcarme como disponible
**Para** que los capitanes puedan encontrarme y enviarme invitaciones.

**Tareas:**
- T-3.2.1: Implementar lógica de cambio de disponibilidad con validación de equipo
- T-3.2.2: Agregar toggle de disponibilidad en el perfil (frontend)
- T-3.2.3: Escribir pruebas de disponibilidad

---

## Épica 4: Gestión de Equipos

> Permite a los capitanes crear equipos, invitar jugadores y gestionar la plantilla.

### HU-4.1 — Crear equipo
**Como** jugador registrado
**Quiero** crear un equipo con nombre, escudo y colores de uniforme
**Para** participar como capitán en el torneo.

**Tareas:**
- T-4.1.1: Crear modelo Equipo en la base de datos
- T-4.1.2: Implementar lógica de creación de equipo con asignación automática de capitán
- T-4.1.3: Implementar carga y almacenamiento de escudo
- T-4.1.4: Crear formulario de creación de equipo (frontend)
- T-4.1.5: Escribir pruebas de creación de equipo

### HU-4.2 — Invitar jugadores al equipo
**Como** capitán
**Quiero** enviar invitaciones a jugadores disponibles
**Para** completar la plantilla de mi equipo.

**Tareas:**
- T-4.2.1: Crear modelo invitacion en la base de datos
- T-4.2.2: Implementar lógica de envío de invitación con validaciones (disponibilidad, equipo no lleno)
- T-4.2.3: Crear interfaz de envío de invitaciones (frontend)
- T-4.2.4: Escribir pruebas de invitaciones

### HU-4.3 — Responder invitación
**Como** jugador
**Quiero** aceptar o rechazar invitaciones de equipos
**Para** unirme al equipo que prefiera.

**Tareas:**
- T-4.3.1: Implementar lógica de aceptar/rechazar (al aceptar, rechazar las demás pendientes)
- T-4.3.2: Crear vista de invitaciones recibidas (frontend)
- T-4.3.3: Escribir pruebas de aceptación/rechazo

### HU-4.4 — Validar composición del equipo
**Como** sistema
**Quiero** validar que un equipo cumpla las reglas de composición
**Para** asegurar que solo equipos válidos se inscriban.

**Tareas:**
- T-4.4.1: Implementar validación de mínimo 7 y máximo 12 jugadores
- T-4.4.2: Implementar validación de mayoría de programas permitidos
- T-4.4.3: Implementar validación de pertenencia a programas/maestrías autorizados
- T-4.4.4: Implementar validación de jugador único por equipo
- T-4.4.5: Mostrar estado de validación en el panel del equipo (frontend)
- T-4.4.6: Escribir pruebas de todas las validaciones

---

## Épica 5: Búsqueda de Jugadores

> Permite a los capitanes encontrar jugadores disponibles mediante filtros.

### HU-5.1 — Buscar jugadores por filtros
**Como** capitán
**Quiero** buscar jugadores disponibles por posición, semestre, edad, género, nombre o identificación
**Para** encontrar los jugadores que necesito para mi equipo.

**Tareas:**
- T-5.1.1: Implementar lógica de búsqueda con filtros combinables
- T-5.1.2: Crear vista de búsqueda con filtros (frontend)
- T-5.1.3: Integrar botón de invitar desde los resultados de búsqueda (frontend)
- T-5.1.4: Escribir pruebas de búsqueda con filtros

---

## Épica 6: Inscripción y Pagos

> Gestiona la inscripción de equipos mediante comprobantes de pago.

### HU-6.1 — Subir comprobante de pago
**Como** capitán
**Quiero** subir el comprobante de pago de mi equipo
**Para** completar la inscripción al torneo.

**Tareas:**
- T-6.1.1: Crear modelo Pago en la base de datos
- T-6.1.2: Implementar lógica de carga y almacenamiento del comprobante
- T-6.1.3: Crear formulario de carga de comprobante (frontend)
- T-6.1.4: Escribir pruebas de carga de comprobante

### HU-6.2 — Verificar pago
**Como** organizador
**Quiero** revisar los comprobantes de pago y aprobar o rechazar la inscripción
**Para** controlar qué equipos participan en el torneo.

**Tareas:**
- T-6.2.1: Implementar lógica de transiciones de estado (Pendiente → En revisión → Aprobado/Rechazado)
- T-6.2.2: Crear panel de revisión de pagos con vista del comprobante (frontend)
- T-6.2.3: Escribir pruebas de verificación de pago

---

## Épica 7: Alineaciones

> Permite a los capitanes organizar la formación de su equipo para cada partido.

### HU-7.1 — Definir alineación
**Como** capitán
**Quiero** seleccionar titulares, reservas y formación táctica antes de un partido
**Para** organizar a mi equipo para el encuentro.

**Tareas:**
- T-7.1.1: Crear modelo Alineacion en la base de datos
- T-7.1.2: Implementar lógica de guardado de alineación con validación de 7 titulares
- T-7.1.3: Crear interfaz visual de cancha para ubicar jugadores (frontend)
- T-7.1.4: Implementar selector de formación táctica (frontend)
- T-7.1.5: Implementar drag & drop o selección de posiciones (frontend)
- T-7.1.6: Escribir pruebas de alineación

### HU-7.2 — Consultar alineación rival
**Como** jugador o capitán
**Quiero** ver la alineación del equipo rival
**Para** prepararme para el partido.

**Tareas:**
- T-7.2.1: Implementar lógica de consulta de alineación rival (solo si fue registrada)
- T-7.2.2: Crear vista de alineación rival con representación visual (frontend)

---

## Épica 8: Partidos y Resultados

> Gestiona el registro de partidos, resultados y la consulta por parte de los árbitros.

### HU-8.1 — Registrar resultado de partido
**Como** organizador
**Quiero** registrar el marcador, goleadores y tarjetas de un partido
**Para** mantener los resultados actualizados.

**Tareas:**
- T-8.1.1: Crear modelos Partido, Gol, Tarjeta en la base de datos
- T-8.1.2: Implementar lógica de registro de resultado con validación de consistencia (marcador vs goleadores)
- T-8.1.3: Crear formulario de registro de resultado (frontend)
- T-8.1.4: Escribir pruebas de registro de resultado

### HU-8.2 — Consultar partidos como árbitro
**Como** árbitro
**Quiero** ver los partidos que tengo asignados con fecha, hora, cancha y equipos
**Para** saber dónde y cuándo debo arbitrar.

**Tareas:**
- T-8.2.1: Implementar lógica de consulta de partidos filtrados por árbitro
- T-8.2.2: Crear vista de partidos asignados al árbitro (frontend)

---

## Épica 9: Tabla de Posiciones

> Calcula y muestra automáticamente la clasificación del torneo.

### HU-9.1 — Calcular y mostrar tabla de posiciones
**Como** usuario autenticado
**Quiero** ver la tabla de posiciones actualizada
**Para** conocer la clasificación de los equipos.

**Tareas:**
- T-9.1.1: Implementar lógica de cálculo de puntos (PJ, PG, PE, PP, GF, GC, DG, Pts)
- T-9.1.2: Implementar ordenamiento por puntos y diferencia de gol
- T-9.1.3: Crear vista de tabla de posiciones (frontend)
- T-9.1.4: Escribir pruebas del cálculo de posiciones

---

## Épica 10: Llaves Eliminatorias

> Genera y muestra el cuadro de eliminación directa del torneo.

### HU-10.1 — Generar llaves eliminatorias
**Como** sistema
**Quiero** generar automáticamente los emparejamientos eliminatorios
**Para** organizar la fase final del torneo.

**Tareas:**
- T-10.1.1: Crear modelo LlaveEliminatoria en la base de datos
- T-10.1.2: Implementar generación aleatoria de emparejamientos iniciales
- T-10.1.3: Implementar avance automático de llaves al registrar resultados
- T-10.1.4: Crear vista de bracket eliminatorio (frontend)
- T-10.1.5: Escribir pruebas de generación y avance de llaves

---

## Épica 11: Estadísticas

> Muestra estadísticas del torneo: goleadores, historial y resultados por equipo.

### HU-11.1 — Consultar estadísticas del torneo
**Como** usuario autenticado
**Quiero** ver los máximos goleadores, historial de partidos y resultados por equipo
**Para** seguir el desempeño de los jugadores y equipos.

**Tareas:**
- T-11.1.1: Implementar lógica de ranking de goleadores
- T-11.1.2: Implementar consulta de historial de partidos
- T-11.1.3: Implementar consulta de resultados por equipo
- T-11.1.4: Crear vista de estadísticas con filtro por equipo (frontend)
- T-11.1.5: Escribir pruebas de estadísticas
