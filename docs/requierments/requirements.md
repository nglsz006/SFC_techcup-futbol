Requirements · MD
Copiar

# Requerimientos — TechCup Fútbol

| Código | Nombre | Tipo        |
|--------|--------|-------------|
| RF_01  | Crear torneo | Funcional   |
| RF_02  | Gestionar torneo | Funcional   |
| RF_03  | Consultar torneo | Funcional   |
| RF_04  | Registro de jugadores | Funcional   |
| RF_05  | Perfil deportivo | Funcional   |
| RF_06  | Disponibilidad de jugador | Funcional   |
| RF_07  | Invitaciones a equipo | Funcional   |
| RF_08  | Crear equipo | Funcional   |
| RF_09  | Gestionar equipo | Funcional   |
| RF_10  | Validar equipo | Funcional   |
| RF_11  | Buscar jugadores | Funcional   |
| RF_12  | Subir comprobante de pago | Funcional   |
| RF_13  | Verificar pago | Funcional   |
| RF_14  | Configurar torneo | Funcional   |
| RF_15  | Gestionar alineación | Funcional   |
| RF_16  | Consultar alineación rival | Funcional   |
| RF_17  | Registrar partido | Funcional   |
| RF_18  | Consultar partido (árbitro) | Funcional   |
| RF_19  | Tabla de posiciones | Funcional   |
| RF_20  | Llaves eliminatorias | Funcional   |
| RF_21  | Estadísticas | Funcional   |
| RF_22  | Registrar organizadores y árbitros | Funcional   |
| RF_23  | Consulta Auditoría | Funcional |
| RNF_01 | Paleta de colores | No Funcional |
| RNF_02 | Diseño responsive | No Funcional |
| RNF_03 | Tiempo de respuesta | No Funcional |
| RNF_04 | Cifrado de contraseñas | No Funcional |
| RNF_05 | Límite de archivos | No Funcional |
| RNF_06 | Disponibilidad del sistema | No Funcional |
| RNF_07 | Compatibilidad de navegadores | No Funcional |
 
---

## RF_01 — Crear torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_01 |
| **Nombre:** | Crear torneo |

| | |
|---|---|
| **Descripción:** | El organizador crea un nuevo torneo ingresando la información básica requerida. |
| **Cómo se ejecutará:** | Mediante un formulario de creación de torneo en la plataforma web. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado y tener rol de Organizador. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Fecha inicial | Fecha de inicio del torneo | Date          | Debe ser una fecha futura | Sí |
| Fecha final | Fecha de finalización del torneo | Date          | Debe ser posterior a la fecha inicial | Sí |
| Cantidad de equipos | Número de equipos que participarán | Int           | Número entero positivo | Sí |
| Costo por equipo | Valor de inscripción por equipo | Double        | Número positivo | Sí |
| Estado | Estado inicial del torneo | Enum          | Valor por defecto: Borrador | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Confirmación de creación | El sistema muestra un mensaje de éxito y redirige al detalle del torneo | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede a la opción de crear torneo | |
| 2 | Organizador | Diligencia los campos del formulario (fecha inicial, fecha final, cantidad de equipos, costo por equipo) | |
| 3 | Sistema | Valida los datos ingresados | Datos inválidos o incompletos |
| 4 | Sistema | Crea el torneo con estado "Borrador" y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si los datos son inválidos, muestra mensaje de error y permite corregir | |

| | |
|---|---|
| **Notas y comentarios:** | Los estados posibles del torneo son: Borrador, Activo, En progreso, Finalizado. |

**Diagrama casos de uso**

![HU-05_Crear_torneo.drawio.png](../uml/CasosSeUso/HU-05_Crear_torneo.drawio.png)

**Prototipo**

![RF_01_CrearTorneo.PNG](../Images/Mock/RF_01_CrearTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La fecha final debe ser posterior a la fecha inicial. |
| 2 | El torneo se crea siempre con estado "Borrador". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_02 — Gestionar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_02 |
| **Nombre:** | Gestionar torneo |

| | |
|---|---|
| **Descripción:** | El organizador puede iniciar o finalizar un torneo existente, cambiando su estado. |
| **Cómo se ejecutará:** | Mediante botones de acción en el detalle del torneo. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El torneo debe existir. El usuario debe tener rol de Organizador. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|--------------|---------------------|-------------|
| Acción | Iniciar o finalizar el torneo | Enum         | Solo disponible según el estado actual del torneo | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El sistema actualiza el estado del torneo y muestra confirmación | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al detalle del torneo | |
| 2 | Organizador | Selecciona "Iniciar torneo" o "Finalizar torneo" | |
| 3 | Sistema | Valida que la transición de estado sea válida | Transición no permitida |
| 4 | Sistema | Actualiza el estado y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si la transición no es válida, muestra mensaje de error | |

| | |
|---|---|
| **Notas y comentarios:** | Transiciones válidas: Borrador → Activo → En progreso → Finalizado. |

**Diagrama casos de uso**

![HU-08_Configurar_torneo.drawio.png](../uml/CasosSeUso/HU-08_Configurar_torneo.drawio.png)

**Prototipo**

![RF_02_gestionarTorneo.PNG](../Images/Mock/RF_02_gestionarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se puede iniciar un torneo en estado "Activo". |
| 2 | Solo se puede finalizar un torneo en estado "En progreso". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_03 — Consultar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_03 |
| **Nombre:** | Consultar torneo |

| | |
|---|---|
| **Descripción:** | Cualquier usuario autenticado puede consultar la información de un torneo. |
| **Cómo se ejecutará:** | Mediante una vista de detalle del torneo en la plataforma. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares, Capitán, Organizador, Árbitro, Administrador |
| **Precondiciones:** | El usuario debe estar autenticado. El torneo debe existir. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo     | Reglas / Aplicación | Obligatorio |
|--------|------------|-------------------|---------------------|-------------|
| ID del torneo | Identificador del torneo a consultar | Long (automatico) | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo  | Reglas / Aplicación | Obligatorio |
|--------|------------|----------------|---------------------|-------------|
| Información del torneo | Fecha inicial, fecha final, cantidad de equipos, costo, estado | objeto<Torneo> | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Selecciona un torneo de la lista | |
| 2 | Sistema | Muestra la información completa del torneo | Torneo no encontrado |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el torneo no existe, muestra mensaje de error | |

| | |
|---|---|
| **Notas y comentarios:** | |

**Diagrama casos de uso**

![HU-07_Consultar_torneo.drawio.png](../uml/CasosSeUso/HU-07_Consultar_torneo.drawio.png)

**Prototipo**

![RF_03_consultarTorneo.PNG](../Images/Mock/RF_03_consultarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La información visible puede variar según el rol del usuario. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_04 — Registro de jugadores

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_04 |
| **Nombre:** | Registro de jugadores |

| | |
|---|---|
| **Descripción:** | Un participante se registra en la plataforma con su correo institucional o personal de Gmail. |
| **Cómo se ejecutará:** | Mediante un formulario de registro en la plataforma web. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El usuario no debe tener una cuenta existente. |

**DATOS DE ENTRADA**

| Nombre                 | Descripción                                        | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------------------------|----------------------------------------------------|---------------|---------------------|-------------|
| Correo electrónico     | Correo institucional o Gmail según el tipo de participante | String        | Correo institucional para estudiantes, graduados, profesores y personal. Gmail para familiares. | Sí          |
| Contraseña             | Contraseña de acceso                               | String        | Debe cumplir requisitos de seguridad | Sí          |
| Nombre                 | Nombre del jugador                                 | String        | | Sí          |
| Apellido               | Apellido del jugador                               | String        | | Sí          |
| IdInstitucional/celuda | Apellido del jugador                               | String        | | Sí          |
| Carrera                | Seleccionar carrera en caso de que sea pregado o   | String        | | No          |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta creada | El sistema confirma el registro y permite acceder a la plataforma | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Participante | Accede al formulario de registro | |
| 2 | Participante | Diligencia correo, contraseña y tipo de participante | |
| 3 | Sistema | Valida el correo según el tipo de participante | Correo inválido o ya registrado |
| 4 | Sistema | Crea la cuenta y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el correo no es válido o ya existe, muestra mensaje de error | |

| | |
|---|---|
| **Notas y comentarios:** | Los familiares usan correo personal de Gmail. Todos los demás usan correo institucional. |

**Diagrama casos de uso**

![HU-01_Registro_correo_institucional.drawio.png](../uml/CasosSeUso/HU-01_Registro_correo_institucional.drawio.png)

![HU-02_Registro_Gmail_familiares.drawio.png](../uml/CasosSeUso/HU-02_Registro_Gmail_familiares.drawio.png)

**Prototipo**

![RF_04_RegistroDeJugadores.PNG](../Images/Mock/RF_04_RegistroDeJugadores.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El correo institucional es obligatorio para estudiantes, graduados, profesores y personal administrativo. |
| 2 | Los familiares se registran con correo personal de Gmail. |
| 3 | No se permiten cuentas duplicadas con el mismo correo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_05 — Perfil deportivo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_05 |
| **Nombre:** | Perfil deportivo |

| | |
|---|---|
| **Descripción:** | El jugador registrado crea y edita su perfil deportivo con posiciones, dorsal y foto. |
| **Cómo se ejecutará:** | Mediante un formulario de perfil en la plataforma web. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El usuario debe estar registrado y autenticado. |

**DATOS DE ENTRADA**

| Nombre                 | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------------------------|------------|---------------|---------------------|-------------|
| Posiciones de juego    | Posiciones preferidas del jugador | Enum          | Portero, Defensa, Volante, Delantero | Sí |
| Número dorsal          | Número de camiseta | Int           | Número positivo | Sí |
| Foto                   | Imagen del jugador | Image         | Formatos de imagen válidos | No |
| Semestre               | Semestre actual (si es estudiante) | Int           | | Condicional |
| Edad                   | Edad del jugador | Int           | | Sí |
| Género                 | Género del jugador | String        | | Sí |
| IdInstitucional/cedula | Número de identificación | String        | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo  | Reglas / Aplicación | Obligatorio |
|--------|------------|----------------|---------------------|-------------|
| Perfil actualizado | El sistema muestra el perfil deportivo creado o actualizado | Objeto Jugador | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a la sección de perfil deportivo | |
| 2 | Jugador | Diligencia o edita los campos del perfil | |
| 3 | Sistema | Valida los datos ingresados | Datos inválidos |
| 4 | Sistema | Guarda el perfil y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si los datos son inválidos, muestra mensaje de error | |

| | |
|---|---|
| **Notas y comentarios:** | El semestre solo aplica para estudiantes. Los datos de edad, género e identificación se usan también para búsqueda de jugadores. |

**Diagrama casos de uso**

![HU-09_Crear_perfil_deportivo.drawio.png](../uml/CasosSeUso/HU-09_Crear_perfil_deportivo.drawio.png)

**Prototipo**

![RF_05_PerfilDeportivo.PNG](../Images/Mock/RF_05_PerfilDeportivo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El número dorsal debe ser único dentro de un equipo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_06 — Disponibilidad de jugador

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_06 |
| **Nombre:** | Disponibilidad de jugador |

| | |
|---|---|
| **Descripción:** | Un jugador puede marcarse como disponible para que los capitanes lo contacten. |
| **Cómo se ejecutará:** | Mediante un toggle o botón en el perfil del jugador. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El usuario debe tener perfil deportivo creado. No debe pertenecer a un equipo. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de disponibilidad | Disponible o no disponible | Boolean       | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El sistema actualiza la visibilidad del jugador en las búsquedas | Boolean       | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a su perfil | |
| 2 | Jugador | Activa o desactiva su disponibilidad | |
| 3 | Sistema | Actualiza el estado y confirma | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el jugador ya pertenece a un equipo, no permite activar disponibilidad | |

| | |
|---|---|
| **Notas y comentarios:** | Los jugadores disponibles aparecerán en las búsquedas de los capitanes. |

**Diagrama casos de uso**

![HU-10_Marcar_disponibilidad.drawio.png](../uml/CasosSeUso/HU-10_Marcar_disponibilidad.drawio.png)

**Prototipo**

![RF_06_DisponibilidadDeJugador.PNG](../Images/Mock/RF_06_DisponibilidadDeJugador.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Un jugador que ya pertenece a un equipo no puede marcarse como disponible. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_07 — Invitaciones a equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_07 |
| **Nombre:** | Invitaciones a equipo |

| | |
|---|---|
| **Descripción:** | Un jugador recibe invitaciones de capitanes para unirse a un equipo y puede aceptarlas o rechazarlas. |
| **Cómo se ejecutará:** | Mediante un sistema de notificaciones/invitaciones en la plataforma. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El jugador debe estar registrado. El capitán debe haber enviado una invitación. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Respuesta a invitación | Aceptar o rechazar la invitación | Enum          | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de invitación | El sistema actualiza el estado de la invitación y notifica al capitán | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Recibe una notificación de invitación | |
| 2 | Jugador | Revisa los detalles del equipo que lo invita | |
| 3 | Jugador | Acepta o rechaza la invitación | |
| 4 | Sistema | Si acepta, lo agrega al equipo. Si rechaza, notifica al capitán | Jugador ya pertenece a otro equipo |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el jugador ya pertenece a un equipo, rechaza automáticamente la invitación | |

| | |
|---|---|
| **Notas y comentarios:** | Un jugador no puede pertenecer a dos equipos simultáneamente. |

**Diagrama casos de uso**

![HU-12_Invitar_jugadores.drawio.png](../uml/CasosSeUso/HU-12_Invitar_jugadores.drawio.png)

**Prototipo**

![RF_07_InvitacionDeEquipo.PNG](../Images/Mock/RF_07_InvitacionDeEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Un jugador solo puede pertenecer a un equipo. |
| 2 | Al aceptar una invitación, todas las demás invitaciones pendientes se rechazan automáticamente. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_08 — Crear equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_08 |
| **Nombre:** | Crear equipo |

| | |
|---|---|
| **Descripción:** | Un capitán crea un nuevo equipo asignándole nombre, escudo y colores de uniforme. |
| **Cómo se ejecutará:** | Mediante un formulario de creación de equipo en la plataforma. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado y registrado como jugador. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo    | Reglas / Aplicación | Obligatorio |
|--------|------------|------------------|---------------------|-------------|
| Nombre del equipo | Nombre que identifica al equipo | String           | Debe ser único      | Sí |
| Escudo | Imagen del escudo del equipo | Image         |        | No |
| Colores del uniforme | Colores del uniforme del equipo | String           |                     | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|--------------|---------------------|-------------|
| Equipo creado | El sistema confirma la creación y asigna al creador como capitán | String       | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a la opción de crear equipo | |
| 2 | Jugador | Diligencia nombre, escudo y colores | |
| 3 | Sistema | Valida que el nombre sea único | Nombre duplicado |
| 4 | Sistema | Crea el equipo y asigna al creador como capitán | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el nombre ya existe, muestra error y permite cambiar | |

| | |
|---|---|
| **Notas y comentarios:** | El creador del equipo se convierte automáticamente en capitán. |

**Diagrama casos de uso**

![HU-11_Crear_equipo.drawio.png](../uml/CasosSeUso/HU-11_Crear_equipo.drawio.png)

**Prototipo**

![RF_08_CrearEquipo.PNG](../Images/Mock/RF_08_CrearEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El nombre del equipo debe ser único dentro del torneo. |
| 2 | El creador se registra automáticamente como capitán y miembro del equipo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_09 — Gestionar equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_09 |
| **Nombre:** | Gestionar equipo |

| | |
|---|---|
| **Descripción:** | El capitán administra su equipo: invita jugadores, edita información y gestiona la plantilla. |
| **Cómo se ejecutará:** | Mediante el panel de administración del equipo. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe ser capitán de un equipo existente. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Jugador a invitar | Selección del jugador disponible | Selector / Búsqueda | Jugador debe estar disponible | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Invitación enviada | El sistema envía la invitación al jugador seleccionado | Notificación | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al panel de su equipo | |
| 2 | Capitán | Busca y selecciona un jugador para invitar | |
| 3 | Sistema | Valida que el jugador no pertenezca a otro equipo y que el equipo no haya alcanzado el máximo | Equipo lleno o jugador no disponible |
| 4 | Sistema | Envía la invitación al jugador | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el equipo tiene 12 jugadores o el jugador no está disponible, muestra error | |

| | |
|---|---|
| **Notas y comentarios:** | |

**Diagrama casos de uso**

![HU-14_Validar_composicion_equipo.drawio.png](../uml/CasosSeUso/HU-14_Validar_composicion_equipo.drawio.png)

**Prototipo**

![RF_09_GestionarEquipo.PNG](../Images/Mock/RF_09_GestionarEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Máximo 12 jugadores por equipo. |
| 2 | Un jugador no puede pertenecer a dos equipos. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_10 — Validar equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_10 |
| **Nombre:** | Validar equipo |

| | |
|---|---|
| **Descripción:** | El sistema valida que un equipo cumpla las reglas de composición antes de poder inscribirse al torneo. |
| **Cómo se ejecutará:** | Validación automática al intentar inscribir el equipo. |
| **Actor principal:** | Sistema |
| **Precondiciones:** | El equipo debe existir con jugadores asignados. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo            | Reglas / Aplicación | Obligatorio |
|--------|------------|--------------------------|---------------------|-------------|
| Equipo | Equipo a validar | Objeto Equipo Automático | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Resultado de validación | Aprobado o listado de incumplimientos | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Verifica que el equipo tenga mínimo 7 jugadores | Menos de 7 jugadores |
| 2 | Sistema | Verifica que no exceda 12 jugadores | Más de 12 jugadores |
| 3 | Sistema | Verifica que más de la mitad sean de los programas permitidos | No cumple mayoría |
| 4 | Sistema | Verifica que todos los miembros pertenezcan a los programas o maestrías permitidas | Miembro no válido |
| 5 | Sistema | Marca el equipo como válido para inscripción | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1-4 | Sistema | Si alguna validación falla, muestra los incumplimientos específicos | |

| | |
|---|---|
| **Notas y comentarios:** | Los programas permitidos son: Ing. de Sistemas, IA, Ciberseguridad, Estadística y las maestrías en Gestión de Información, Informática y Ciencia de Datos. |

**Diagrama casos de uso**

![HU-14_Validar_composicion_equipo.drawio.png](../uml/CasosSeUso/HU-14_Validar_composicion_equipo.drawio.png)

**Prototipo**

![RF_10_ValidarEquipo.PNG](../Images/Mock/RF_10_ValidarEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Mínimo 7, máximo 12 jugadores por equipo. |
| 2 | Más de la mitad de los miembros deben ser de los programas de Ing. de Sistemas, IA, Ciberseguridad y Estadística. |
| 3 | Todos los miembros deben pertenecer a los programas o maestrías autorizadas de la Escuela. |
| 4 | Un jugador no puede pertenecer a dos equipos. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_11 — Buscar jugadores

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_11 |
| **Nombre:** | Buscar jugadores |

| | |
|---|---|
| **Descripción:** | Los capitanes buscan jugadores disponibles usando filtros. |
| **Cómo se ejecutará:** | Mediante un buscador con filtros en la plataforma. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe ser capitán de un equipo. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Posición | Filtrar por posición de juego | Enum          | Portero, Defensa, Volante, Delantero | No |
| Semestre | Filtrar por semestre (si es estudiante) | int           | | No |
| Edad | Filtrar por edad | int           | | No |
| Género | Filtrar por género | String        | | No |
| Nombre | Filtrar por nombre | String        | | No |
| Identificación | Filtrar por número de identificación | String        | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de jugadores | Jugadores que coinciden con los filtros aplicados | list(jugador) | Solo jugadores marcados como disponibles | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede a la búsqueda de jugadores | |
| 2 | Capitán | Aplica uno o más filtros | |
| 3 | Sistema | Muestra los jugadores disponibles que coinciden | Sin resultados |
| 4 | Capitán | Selecciona un jugador para invitarlo | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si no hay resultados, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | Solo aparecen jugadores que se han marcado como disponibles y no pertenecen a un equipo. |

**Diagrama casos de uso**

![HU-15_Buscar_jugadores.drawio.png](../uml/CasosSeUso/HU-15_Buscar_jugadores.drawio.png)

**Prototipo**

![RF_11_BuscarJugadores.PNG](../Images/Mock/RF_11_BuscarJugadores.PNG)


**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se muestran jugadores con estado disponible. |
| 2 | Los filtros son opcionales y combinables. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_12 — Subir comprobante de pago

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_12 |
| **Nombre:** | Subir comprobante de pago |

| | |
|---|---|
| **Descripción:** | El capitán sube el comprobante de pago de inscripción del equipo a la plataforma. |
| **Cómo se ejecutará:** | Mediante un formulario de carga de archivo en la sección de inscripción. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El equipo debe existir y estar validado. El pago se realiza fuera de la plataforma (NEQUI o efectivo). |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Comprobante de pago | Imagen o documento del comprobante de consignación | Image         | Formatos válidos (imagen/PDF) | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado del pago | El sistema registra el comprobante con estado "Pendiente" | Enum          | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede a la sección de inscripción de su equipo | |
| 2 | Capitán | Selecciona y sube el comprobante de pago | |
| 3 | Sistema | Valida el archivo y lo registra con estado "Pendiente" | Archivo inválido |
| 4 | Sistema | Muestra confirmación de carga | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el archivo no es válido, muestra error y permite reintentar | |

| | |
|---|---|
| **Notas y comentarios:** | El pago no se realiza dentro de la plataforma. Se paga por NEQUI o efectivo al coordinador. |

**Diagrama casos de uso**

![HU-16_Subir_comprobante_pago.drawio.png](../uml/CasosSeUso/HU-16_Subir_comprobante_pago.drawio.png)

**Prototipo**

![RF_12_SubirComprobanteDePago.PNG](../Images/Mock/RF_12_SubirComprobanteDePago.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El pago lo realiza el capitán del equipo. |
| 2 | El comprobante se registra automáticamente con estado "Pendiente". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_13 — Verificar pago

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_13 |
| **Nombre:** | Verificar pago |

| | |
|---|---|
| **Descripción:** | El organizador revisa los comprobantes de pago y aprueba o rechaza la inscripción del equipo. |
| **Cómo se ejecutará:** | Mediante un panel de revisión de pagos en la plataforma. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El capitán debe haber subido un comprobante de pago. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Decisión | Aprobar o rechazar el comprobante | Boolean       | | Sí |
| Observaciones | Comentarios sobre la decisión (opcional) | String        | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El estado cambia a Aprobado o Rechazado | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al panel de revisión de pagos | |
| 2 | Organizador | Revisa el comprobante subido | |
| 3 | Organizador | Aprueba o rechaza el pago | |
| 4 | Sistema | Actualiza el estado del pago y notifica al capitán | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Organizador | Si rechaza, puede agregar observaciones indicando el motivo | |

| | |
|---|---|
| **Notas y comentarios:** | Estados posibles: Pendiente → En revisión → Aprobado / Rechazado. Solo equipos aprobados participan en el torneo. |

**Diagrama casos de uso**

![HU-17_Verificar_pago.drawio.png](../uml/CasosSeUso/HU-17_Verificar_pago.drawio.png)

**Prototipo**

![RF_13_VerificarPagoPNG.PNG](../Images/Mock/RF_13_VerificarPagoPNG.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo los equipos con pago aprobado pueden participar en el torneo. |
| 2 | Los estados válidos son: Pendiente, En revisión, Aprobado, Rechazado. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_14 — Configurar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_14 |
| **Nombre:** | Configurar torneo |

| | |
|---|---|
| **Descripción:** | El organizador define la configuración detallada del torneo posterior a su creación. |
| **Cómo se ejecutará:** | Mediante formularios de configuración dentro del detalle del torneo. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El torneo debe existir. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo   | Reglas / Aplicación | Obligatorio |
|--------|------------|-----------------|---------------------|-------------|
| Reglamento | Reglas del torneo | String          | | Sí |
| Fechas importantes | Fechas clave del torneo | Date            | | Sí |
| Cierre de inscripciones | Fecha límite para inscripción | Date            | Debe ser anterior a la fecha de inicio del torneo | Sí |
| Horarios de partidos | Horarios disponibles para los partidos | LocalTime       | | Sí |
| Canchas | Canchas disponibles para los partidos | Enum            | | Sí |
| Sanciones | Definición de sanciones aplicables | List(Sanciones) | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Configuración guardada | El sistema confirma que la configuración fue guardada | Torneo        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede a la configuración del torneo | |
| 2 | Organizador | Diligencia reglamento, fechas, horarios, canchas y sanciones | |
| 3 | Sistema | Valida los datos | Datos inválidos |
| 4 | Sistema | Guarda la configuración y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si hay datos inválidos, muestra error y permite corregir | |

| | |
|---|---|
| **Notas y comentarios:** | La configuración puede editarse mientras el torneo esté en estado Borrador o Activo. |

**Diagrama casos de uso**

![RF_02_gestionarTorneo.PNG](../Images/Mock/RF_02_gestionarTorneo.PNG)

**Prototipo**

![HU-06_Cambiar_estado_torneo.drawio.png](../uml/CasosSeUso/HU-06_Cambiar_estado_torneo.drawio.png)

![HU-08_Configurar_torneo.drawio.png](../uml/CasosSeUso/HU-08_Configurar_torneo.drawio.png)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El cierre de inscripciones debe ser anterior a la fecha inicial del torneo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_15 — Gestionar alineación

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_15 |
| **Nombre:** | Gestionar alineación |

| | |
|---|---|
| **Descripción:** | El capitán organiza la formación de su equipo antes de cada partido: titulares, reservas y posición en cancha. |
| **Cómo se ejecutará:** | Mediante una interfaz visual de cancha con drag & drop o selección. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El equipo debe estar inscrito en el torneo. Debe existir un partido programado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo            | Reglas / Aplicación | Obligatorio |
|--------|------------|--------------------------|---------------------|-------------|
| Titulares | 7 jugadores que participarán | List(jugadoer)           | Exactamente 7 jugadores | Sí |
| Reservas | Jugadores restantes del plantel | Automático List(jugador) | Los no seleccionados como titulares | Sí |
| Formación | Esquema táctico | String                   | | Sí |
| Posiciones en cancha | Ubicación de cada titular en el campo | list(Jugador)            | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Alineación guardada | El sistema guarda la formación para el partido | Alineacion    | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede a la alineación para un partido próximo | |
| 2 | Capitán | Selecciona los 7 titulares | No selecciona exactamente 7 |
| 3 | Capitán | Elige la formación táctica | |
| 4 | Capitán | Ubica a los jugadores en la cancha visualmente | |
| 5 | Sistema | Valida y guarda la alineación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si no se seleccionan exactamente 7 titulares, muestra error | |

| | |
|---|---|
| **Notas y comentarios:** | No se permiten cambios de jugadores entre equipos durante el torneo. Los 12 jugadores iniciales deben terminar el torneo. |

**Diagrama casos de uso**

![HU-18_Definir_alineacion.drawio.png](../uml/CasosSeUso/HU-18_Definir_alineacion.drawio.png)

**Prototipo**

![RF_15_GestionarAlineacion.PNG](../Images/Mock/RF_15_GestionarAlineacion.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Exactamente 7 titulares por partido. |
| 2 | No se permiten transferencias de jugadores entre equipos. |
| 3 | Los jugadores iniciales del equipo deben permanecer hasta el final del torneo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_16 — Consultar alineación rival

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_16 |
| **Nombre:** | Consultar alineación rival |

| | |
|---|---|
| **Descripción:** | Capitanes y jugadores pueden consultar la alineación del equipo rival para un partido. |
| **Cómo se ejecutará:** | Mediante la vista del partido en la plataforma. |
| **Actor principal:** | Capitán, Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El partido debe existir. La alineación rival debe haber sido registrada. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo                        | Reglas / Aplicación | Obligatorio |
|--------|------------|--------------------------------------|---------------------|-------------|
| Partido | Selección del partido a consultar | Selector / Automático Objeto Partido | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo     | Reglas / Aplicación | Obligatorio |
|--------|------------|-------------------|---------------------|-------------|
| Alineación rival | Titulares, reservas y formación del equipo rival | Objeto Alineacion | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al detalle de un partido | |
| 2 | Sistema | Muestra la alineación del equipo rival | Alineación no registrada aún |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si la alineación no ha sido registrada, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | |

**Diagrama casos de uso**

![HU-19_Consultar_alineacion_rival.drawio.png](../uml/CasosSeUso/HU-19_Consultar_alineacion_rival.drawio.png)

**Prototipo**

![RF_16_ConsultarAlineacionRival.PNG](../Images/Mock/RF_16_ConsultarAlineacionRival.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La alineación rival solo es visible una vez que el capitán contrario la haya registrado. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_17 — Registrar partido

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_17 |
| **Nombre:** | Registrar partido |

| | |
|---|---|
| **Descripción:** | El organizador registra los resultados de un partido: marcador, goleadores y tarjetas. |
| **Cómo se ejecutará:** | Mediante un formulario de registro de resultados en la plataforma. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El partido debe existir y haberse jugado. |

**DATOS DE ENTRADA**

| Nombre                    | Descripción                       | Tipo de campo | Reglas / Aplicación | Obligatorio |
|---------------------------|-----------------------------------|---------------|---------------------|-------------|
| Marcador equipo local     | Goles del equipo local            | Int           | Número entero ≥ 0 | Sí |
| Marcador equipo visitante | Goles del equipo visitante        | Int           | Número entero ≥ 0 | Sí |
| Goleadores                | Jugadores que anotaron goles      | List(Jugador) | Deben ser jugadores titulares del partido | Sí |
| Sanciones                 | Registrar sanciones por jugadores | List(Sancion) | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Resultado registrado | El sistema actualiza el resultado y la tabla de posiciones | Confirmación | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al partido a registrar | |
| 2 | Organizador | Ingresa marcador, goleadores y tarjetas | |
| 3 | Sistema | Valida que los goleadores coincidan con el marcador | Inconsistencia en datos |
| 4 | Sistema | Registra el resultado y actualiza tabla de posiciones | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si hay inconsistencias, muestra error y permite corregir | |

| | |
|---|---|
| **Notas y comentarios:** | El registro de un partido actualiza automáticamente la tabla de posiciones y las estadísticas. |

**Diagrama casos de uso**

![HU-20_Registrar_resultado_partido.drawio.png](../uml/CasosSeUso/HU-20_Registrar_resultado_partido.drawio.png)

**Prototipo**

![RF_17_RegistrarPartidos.PNG](../Images/Mock/RF_17_RegistrarPartidos.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La cantidad de goleadores debe corresponder con el marcador. |
| 2 | Los goleadores deben ser jugadores titulares del partido. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_18 — Consultar partido (árbitro)

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_18 |
| **Nombre:** | Consultar partido (árbitro) |

| | |
|---|---|
| **Descripción:** | El árbitro consulta la información de los partidos que debe arbitrar. |
| **Cómo se ejecutará:** | Mediante una vista de partidos asignados en la plataforma. |
| **Actor principal:** | Árbitro |
| **Precondiciones:** | El árbitro debe estar autenticado. Debe tener partidos asignados. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| — | No requiere entrada, se muestran los partidos asignados | — | | — |

**DATOS DE SALIDA**

| Nombre  | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|---------|------------|---------------|---------------------|-------------|
| Hora    | Fecha y hora del partido | LocalTime          | | Sí |
| Fecha   | Fecha y hora del partido | Date          | | Sí |
| Cancha  | Cancha donde se juega | Enum          | | Sí |
| Equipos | Equipos que disputarán el partido | List(Equipo)  | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Árbitro | Accede a su panel de partidos | |
| 2 | Sistema | Muestra los partidos asignados con fecha, hora, cancha y equipos | Sin partidos asignados |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si no tiene partidos asignados, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | |

**Diagrama casos de uso**

**Prototipo**

![RF_18_ConsultarPartido(Arbitro).PNG](../Images/Mock/RF_18_ConsultarPartido%28Arbitro%29.PNG)

**REGLAS DE NEGOCIO**

![HU-20_Registrar_resultado_partido.drawio.png](../uml/CasosSeUso/HU-20_Registrar_resultado_partido.drawio.png)

| No. | Descripción |
|-----|------------|
| 1 | El árbitro solo puede ver los partidos que le han sido asignados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_19 — Tabla de posiciones

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_19 |
| **Nombre:** | Tabla de posiciones |

| | |
|---|---|
| **Descripción:** | El sistema calcula y muestra automáticamente la tabla de posiciones del torneo. |
| **Cómo se ejecutará:** | Cálculo automático basado en los resultados registrados, visible en la plataforma. |
| **Actor principal:** | Sistema (consulta: todos los usuarios) |
| **Precondiciones:** | Debe haber al menos un partido registrado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| — | No requiere entrada manual. Se calcula a partir de los resultados de los partidos | — | | — |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Partidos jugados | Total de partidos por equipo | Int           | | Sí |
| Partidos ganados | Partidos ganados por equipo | Int           | | Sí |
| Partidos empatados | Partidos empatados por equipo | Int           | | Sí |
| Partidos perdidos | Partidos perdidos por equipo | Int           | | Sí |
| Goles a favor | Goles anotados por equipo | Int           | | Sí |
| Goles en contra | Goles recibidos por equipo | Int           | | Sí |
| Diferencia de gol | Goles a favor menos goles en contra | Int           | | Sí |
| Puntos | Puntos acumulados por equipo | Int           | Victoria: 3 pts, Empate: 1 pt, Derrota: 0 pts | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede a la sección de tabla de posiciones | |
| 2 | Sistema | Calcula y muestra la tabla ordenada por puntos | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si no hay partidos registrados, muestra tabla vacía | |

| | |
|---|---|
| **Notas y comentarios:** | La tabla se actualiza automáticamente cada vez que se registra un resultado. |

**Diagrama casos de uso**

![HU-22_Tabla_posiciones.drawio.png](../uml/CasosSeUso/HU-22_Tabla_posiciones.drawio.png)

**Prototipo**

![RF_19_TablaDePosiciones.PNG](../Images/Mock/RF_19_TablaDePosiciones.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Victoria = 3 puntos, Empate = 1 punto, Derrota = 0 puntos. |
| 2 | La tabla se ordena por puntos, luego por diferencia de gol. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_20 — Llaves eliminatorias

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_20 |
| **Nombre:** | Llaves eliminatorias |

| | |
|---|---|
| **Descripción:** | El sistema genera automáticamente el cuadro de llaves eliminatorias del torneo. |
| **Cómo se ejecutará:** | Generación automática al pasar a fase eliminatoria, visible como bracket en la plataforma. |
| **Actor principal:** | Sistema (consulta: todos los usuarios) |
| **Precondiciones:** | La fase de grupos debe haber finalizado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| — | No requiere entrada manual. Se genera a partir de la tabla de posiciones | — | | — |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Partidos iniciales | Emparejamientos aleatorios | List(Partido) | | Sí |
| Cuartos de final | Llaves de cuartos | List(Partido) | | Sí |
| Semifinal | Llaves de semifinal | List(Partido) | | Sí |
| Final | Llave de la final | Partido       | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Genera los emparejamientos iniciales de forma aleatoria | |
| 2 | Sistema | A medida que se registran resultados, avanza las llaves (cuartos, semifinal, final) | |
| 3 | Usuario | Consulta el cuadro de llaves en cualquier momento | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Si no hay suficientes equipos clasificados, no genera las llaves | |

| | |
|---|---|
| **Notas y comentarios:** | Los emparejamientos iniciales son aleatorios. |

**Diagrama casos de uso**

![HU-23_Llaves_eliminatorias.drawio.png](../uml/CasosSeUso/HU-23_Llaves_eliminatorias.drawio.png)

**Prototipo**

![RF_20_LlavesEliminatorias.PNG](../Images/Mock/RF_20_LlavesEliminatorias.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Los partidos iniciales se generan de manera aleatoria. |
| 2 | Las llaves avanzan automáticamente al registrar resultados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_21 — Estadísticas

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_21 |
| **Nombre:** | Estadísticas |

| | |
|---|---|
| **Descripción:** | El sistema permite consultar estadísticas del torneo: goleadores, historial y resultados por equipo. |
| **Cómo se ejecutará:** | Mediante una sección de estadísticas en la plataforma. |
| **Actor principal:** | Todos los usuarios autenticados |
| **Precondiciones:** | Debe haber partidos registrados con resultados. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Filtro por equipo | Filtrar resultados por equipo específico | Objeto Equipo | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Máximos goleadores | Ranking de jugadores con más goles | List(Usuario) | | Sí |
| Historial de partidos | Lista de todos los partidos jugados con resultados | List(Partido) | | Sí |
| Resultados por equipo | Resultados filtrados por un equipo específico | List(Equipo)  | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede a la sección de estadísticas | |
| 2 | Usuario | Opcionalmente filtra por equipo | |
| 3 | Sistema | Muestra goleadores, historial de partidos y resultados | Sin datos disponibles |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si no hay datos, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | Las estadísticas se actualizan automáticamente al registrar resultados de partidos. |

**Diagrama casos de uso**

![HU-24_Estadisticas.drawio.png](../uml/CasosSeUso/HU-24_Estadisticas.drawio.png)

**Prototipo**

![RF_21_Estadisticas.PNG](../Images/Mock/RF_21_Estadisticas.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Las estadísticas se calculan en tiempo real a partir de los datos registrados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_22 — Registrar organizadores y árbitros

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_22 |
| **Nombre:** | Registrar organizadores y árbitros |

| | |
|---|---|
| **Descripción:** | El administrador registra manualmente a los usuarios con rol de Organizador o Árbitro en la plataforma. |
| **Cómo se ejecutará:** | Mediante un formulario de registro administrativo en la plataforma. |
| **Actor principal:** | Administrador |
| **Precondiciones:** | El administrador debe estar autenticado. El usuario a registrar no debe tener una cuenta existente. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación                  | Obligatorio |
|--------|------------|---------------|--------------------------------------|-------------|
| Nombre completo | Nombre del usuario a registrar | String        |                                      | Sí |
| Correo electrónico | Correo del organizador o árbitro | String        | No debe estar registrado previamente | Sí |
| Rol | Tipo de rol a asignar | Selector      | Enum                                 | Sí |
| Contraseña temporal | Contraseña inicial de acceso | String        | Debe cumplir requisitos de seguridad | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta creada | El sistema confirma el registro del nuevo usuario con su rol asignado | String        | | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Administrador | Accede al panel de gestión de usuarios | |
| 2 | Administrador | Diligencia nombre, correo, rol y contraseña temporal | |
| 3 | Sistema | Valida que el correo no esté registrado y que los datos sean correctos | Correo duplicado o datos inválidos |
| 4 | Sistema | Crea la cuenta con el rol asignado y muestra confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el correo ya existe o los datos son inválidos, muestra mensaje de error y permite corregir | |

| | |
|---|---|
| **Notas y comentarios:** | Los organizadores y árbitros no pueden registrarse por cuenta propia; solo el administrador puede crearles una cuenta. |

**Diagrama casos de uso**

![HU-25 — Registrar organizadores y árbitros.png](../uml/CasosSeUso/HU-25%20%E2%80%94%20Registrar%20organizadores%20y%20%C3%A1rbitros.png)

**Prototipo**

![RF_22_RegistrarORganizadoresYarbitros.PNG](../Images/Mock/RF_22_RegistrarORganizadoresYarbitros.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo el administrador puede registrar usuarios con rol de Organizador o Árbitro. |
| 2 | No se permiten cuentas duplicadas con el mismo correo. |
| 3 | El rol asignado determina las funcionalidades disponibles para ese usuario. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
 
---

## RF_23 — Consulta Auditoría

| | |
|---|---|
| **Código:** | RF_23 |
| **Nombre:** | Consulta Auditoría |

| | |
|---|---|
| **Descripción:** | El administrador puede consultar el historial completo de acciones realizadas en el sistema. |
| **Cómo se ejecutará:** | Mediante un panel de auditoría con filtros en la plataforma. |
| **Actor principal:** | Administrador |
| **Precondiciones:** | El administrador debe estar autenticado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Filtrar por usuario | Filtrar movimientos por usuario específico | String | Por defecto: "Todos los usuarios" | No |
| Tipo de acción | Filtrar por tipo de acción realizada | Enum | Creación, Aprobación, Registro, Resultado, Rechazo, Configuración, Estado, Sesión. Por defecto: "Todas las acciones" | No |
| Rango de fechas | Filtrar movimientos por periodo de tiempo | Enum | Por defecto: "Última semana" | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Fecha y hora | Fecha y hora en que ocurrió la acción | LocalDateTime | Formato: dd MMM yyyy, HH:mm | Sí |
| Acción | Descripción de la acción realizada | String | | Sí |
| Usuario | Usuario que realizó la acción | String | | Sí |
| Tipo | Categoría de la acción | Enum | Creación, Aprobación, Registro, Resultado, Rechazo, Configuración, Estado, Sesión | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Administrador | Accede al panel de auditoría | |
| 2 | Administrador | Aplica filtros opcionales (usuario, tipo de acción, rango de fechas) | |
| 3 | Administrador | Presiona el botón "Filtrar" | |
| 4 | Sistema | Muestra el historial de acciones que coinciden con los filtros | Sin resultados |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si no hay registros, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | Las acciones se registran automáticamente por el sistema. La tabla se ordena por fecha de más reciente a más antigua. |

**Diagrama casos de uso**

![HU-26 Consulta Auditoria.PNG](../uml/CasosSeUso/HU-26%20Consulta%20Auditoria.PNG)

**Prototipo**

![RF_23_ConsultaAuditoria.PNG](../Images/Mock/RF_23_ConsultaAuditoria.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo el administrador puede consultar la auditoría. |
| 2 | Los registros de auditoría no pueden ser modificados ni eliminados. |
| 3 | El historial se ordena por fecha descendente. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |

# Requerimientos No Funcionales

| Código | Nombre | Descripción                                                                           |
|--------|--------|---------------------------------------------------------------------------------------|
| RNF_01 | Paleta de colores | La interfaz debe usar en su mayoria blanco, verde y negro (y sus tonalidades).        |
| RNF_02 | Diseño responsive | La plataforma debe adaptarse a móvil, tablet y escritorio (desde 320px hasta 1920px). |
| RNF_03 | Tiempo de respuesta | Todas las páginas y operaciones deben responder en menos de 3 segundos.               |
| RNF_04 | Cifrado de contraseñas | Las contraseñas deben almacenarse cifradas con bcrypt o equivalente.                  |
| RNF_05 | Límite de archivos | Soporte para carga de archivos de hasta 5 MB (JPG, PNG, PDF).                         |
| RNF_06 | Disponibilidad del sistema | Disponibilidad mínima del 95% durante el periodo activo del torneo.                   |
| RNF_07 | Compatibilidad de navegadores | Debe funcionar correctamente en Chrome, Firefox, Edge y Safari (últimas 2 versiones). |