# Requerimientos — TechCup Fútbol

| Código | Nombre | Tipo |
|--------|--------|------|
| RF_01 | Crear torneo | Funcional |
| RF_02 | Gestionar torneo | Funcional |
| RF_03 | Consultar torneo | Funcional |
| RF_04 | Registro de jugadores | Funcional |
| RF_05 | Perfil deportivo | Funcional |
| RF_06 | Disponibilidad de jugador | Funcional |
| RF_07 | Invitaciones a equipo | Funcional |
| RF_08 | Crear equipo | Funcional |
| RF_09 | Gestionar equipo | Funcional |
| RF_10 | Validar equipo | Funcional |
| RF_11 | Buscar jugadores | Funcional |
| RF_12 | Subir comprobante de pago | Funcional |
| RF_13 | Verificar pago | Funcional |
| RF_14 | Configurar torneo | Funcional |
| RF_15 | Gestionar alineación | Funcional |
| RF_16 | Consultar alineación rival | Funcional |
| RF_17 | Registrar partido | Funcional |
| RF_18 | Consultar partido (árbitro) | Funcional |
| RF_19 | Tabla de posiciones | Funcional |
| RF_20 | Llaves eliminatorias | Funcional |
| RF_21 | Estadísticas | Funcional |
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
| Fecha inicial | Fecha de inicio del torneo | Selector de fecha | Debe ser una fecha futura | Sí |
| Fecha final | Fecha de finalización del torneo | Selector de fecha | Debe ser posterior a la fecha inicial | Sí |
| Cantidad de equipos | Número de equipos que participarán | Campo numérico | Número entero positivo | Sí |
| Costo por equipo | Valor de inscripción por equipo | Campo numérico | Número positivo | Sí |
| Estado | Estado inicial del torneo | Selector | Valor por defecto: Borrador | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Confirmación de creación | El sistema muestra un mensaje de éxito y redirige al detalle del torneo | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La fecha final debe ser posterior a la fecha inicial. |
| 2 | El torneo se crea siempre con estado "Borrador". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
|--------|------------|---------------|---------------------|-------------|
| Acción | Iniciar o finalizar el torneo | Botón | Solo disponible según el estado actual del torneo | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El sistema actualiza el estado del torneo y muestra confirmación | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se puede iniciar un torneo en estado "Activo". |
| 2 | Solo se puede finalizar un torneo en estado "En progreso". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo a consultar | Automático (selección) | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Información del torneo | Fecha inicial, fecha final, cantidad de equipos, costo, estado | Vista de detalle | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La información visible puede variar según el rol del usuario. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Correo electrónico | Correo institucional o Gmail según el tipo de participante | Campo de texto | Correo institucional para estudiantes, graduados, profesores y personal. Gmail para familiares. | Sí |
| Contraseña | Contraseña de acceso | Campo de texto cifrado | Debe cumplir requisitos de seguridad | Sí |
| Tipo de participante | Estudiante, Graduado, Profesor, Personal Administrativo o Familiar | Selector | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta creada | El sistema confirma el registro y permite acceder a la plataforma | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El correo institucional es obligatorio para estudiantes, graduados, profesores y personal administrativo. |
| 2 | Los familiares se registran con correo personal de Gmail. |
| 3 | No se permiten cuentas duplicadas con el mismo correo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Posiciones de juego | Posiciones preferidas del jugador | Selector múltiple | Portero, Defensa, Volante, Delantero | Sí |
| Número dorsal | Número de camiseta | Campo numérico | Número positivo | Sí |
| Foto | Imagen del jugador | Carga de archivo | Formatos de imagen válidos | No |
| Semestre | Semestre actual (si es estudiante) | Campo numérico | | Condicional |
| Edad | Edad del jugador | Campo numérico | | Sí |
| Género | Género del jugador | Selector | | Sí |
| Identificación | Número de identificación | Campo de texto | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Perfil actualizado | El sistema muestra el perfil deportivo creado o actualizado | Vista de perfil | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El número dorsal debe ser único dentro de un equipo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Estado de disponibilidad | Disponible o no disponible | Toggle | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El sistema actualiza la visibilidad del jugador en las búsquedas | Confirmación | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Un jugador que ya pertenece a un equipo no puede marcarse como disponible. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Respuesta a invitación | Aceptar o rechazar la invitación | Botón | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de invitación | El sistema actualiza el estado de la invitación y notifica al capitán | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Un jugador solo puede pertenecer a un equipo. |
| 2 | Al aceptar una invitación, todas las demás invitaciones pendientes se rechazan automáticamente. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Nombre del equipo | Nombre que identifica al equipo | Campo de texto | Debe ser único | Sí |
| Escudo | Imagen del escudo del equipo | Carga de archivo | Formatos de imagen válidos | No |
| Colores del uniforme | Colores del uniforme del equipo | Selector de color | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Equipo creado | El sistema confirma la creación y asigna al creador como capitán | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El nombre del equipo debe ser único dentro del torneo. |
| 2 | El creador se registra automáticamente como capitán y miembro del equipo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Máximo 12 jugadores por equipo. |
| 2 | Un jugador no puede pertenecer a dos equipos. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Equipo | Equipo a validar | Automático | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Resultado de validación | Aprobado o listado de incumplimientos | Mensaje | | Sí |

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
| Shawarma FC | | | Versión inicial |

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
| Posición | Filtrar por posición de juego | Selector | Portero, Defensa, Volante, Delantero | No |
| Semestre | Filtrar por semestre (si es estudiante) | Campo numérico | | No |
| Edad | Filtrar por edad | Campo numérico | | No |
| Género | Filtrar por género | Selector | | No |
| Nombre | Filtrar por nombre | Campo de texto | | No |
| Identificación | Filtrar por número de identificación | Campo de texto | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de jugadores | Jugadores que coinciden con los filtros aplicados | Tabla / Lista | Solo jugadores marcados como disponibles | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se muestran jugadores con estado disponible. |
| 2 | Los filtros son opcionales y combinables. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Comprobante de pago | Imagen o documento del comprobante de consignación | Carga de archivo | Formatos válidos (imagen/PDF) | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado del pago | El sistema registra el comprobante con estado "Pendiente" | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El pago lo realiza el capitán del equipo. |
| 2 | El comprobante se registra automáticamente con estado "Pendiente". |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Decisión | Aprobar o rechazar el comprobante | Botón | | Sí |
| Observaciones | Comentarios sobre la decisión (opcional) | Campo de texto | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado actualizado | El estado cambia a Aprobado o Rechazado | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo los equipos con pago aprobado pueden participar en el torneo. |
| 2 | Los estados válidos son: Pendiente, En revisión, Aprobado, Rechazado. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Reglamento | Reglas del torneo | Campo de texto largo | | Sí |
| Fechas importantes | Fechas clave del torneo | Selector de fechas | | Sí |
| Cierre de inscripciones | Fecha límite para inscripción | Selector de fecha | Debe ser anterior a la fecha de inicio del torneo | Sí |
| Horarios de partidos | Horarios disponibles para los partidos | Selector de horario | | Sí |
| Canchas | Canchas disponibles para los partidos | Campo de texto / Selector | | Sí |
| Sanciones | Definición de sanciones aplicables | Campo de texto largo | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Configuración guardada | El sistema confirma que la configuración fue guardada | Mensaje | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El cierre de inscripciones debe ser anterior a la fecha inicial del torneo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Titulares | 7 jugadores que participarán | Selector múltiple | Exactamente 7 jugadores | Sí |
| Reservas | Jugadores restantes del plantel | Automático | Los no seleccionados como titulares | Sí |
| Formación | Esquema táctico | Selector | | Sí |
| Posiciones en cancha | Ubicación de cada titular en el campo | Interfaz visual | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Alineación guardada | El sistema guarda la formación para el partido | Confirmación | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Exactamente 7 titulares por partido. |
| 2 | No se permiten transferencias de jugadores entre equipos. |
| 3 | Los jugadores iniciales del equipo deben permanecer hasta el final del torneo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Partido | Selección del partido a consultar | Selector / Automático | | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Alineación rival | Titulares, reservas y formación del equipo rival | Vista visual | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La alineación rival solo es visible una vez que el capitán contrario la haya registrado. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Marcador equipo local | Goles del equipo local | Campo numérico | Número entero ≥ 0 | Sí |
| Marcador equipo visitante | Goles del equipo visitante | Campo numérico | Número entero ≥ 0 | Sí |
| Goleadores | Jugadores que anotaron goles | Selector múltiple | Deben ser jugadores titulares del partido | Sí |
| Tarjetas amarillas | Jugadores amonestados | Selector múltiple | | No |
| Tarjetas rojas | Jugadores expulsados | Selector múltiple | | No |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | La cantidad de goleadores debe corresponder con el marcador. |
| 2 | Los goleadores deben ser jugadores titulares del partido. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Fecha y hora | Fecha y hora del partido | Texto | | Sí |
| Cancha | Cancha donde se juega | Texto | | Sí |
| Equipos | Equipos que disputarán el partido | Texto | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | El árbitro solo puede ver los partidos que le han sido asignados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Partidos jugados | Total de partidos por equipo | Numérico | | Sí |
| Partidos ganados | Partidos ganados por equipo | Numérico | | Sí |
| Partidos empatados | Partidos empatados por equipo | Numérico | | Sí |
| Partidos perdidos | Partidos perdidos por equipo | Numérico | | Sí |
| Goles a favor | Goles anotados por equipo | Numérico | | Sí |
| Goles en contra | Goles recibidos por equipo | Numérico | | Sí |
| Diferencia de gol | Goles a favor menos goles en contra | Numérico | | Sí |
| Puntos | Puntos acumulados por equipo | Numérico | Victoria: 3 pts, Empate: 1 pt, Derrota: 0 pts | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Victoria = 3 puntos, Empate = 1 punto, Derrota = 0 puntos. |
| 2 | La tabla se ordena por puntos, luego por diferencia de gol. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Partidos iniciales | Emparejamientos aleatorios | Vista de bracket | | Sí |
| Cuartos de final | Llaves de cuartos | Vista de bracket | | Sí |
| Semifinal | Llaves de semifinal | Vista de bracket | | Sí |
| Final | Llave de la final | Vista de bracket | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Los partidos iniciales se generan de manera aleatoria. |
| 2 | Las llaves avanzan automáticamente al registrar resultados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

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
| Filtro por equipo | Filtrar resultados por equipo específico | Selector | | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Máximos goleadores | Ranking de jugadores con más goles | Tabla | | Sí |
| Historial de partidos | Lista de todos los partidos jugados con resultados | Tabla | | Sí |
| Resultados por equipo | Resultados filtrados por un equipo específico | Tabla | | Sí |

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

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Las estadísticas se calculan en tiempo real a partir de los datos registrados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | | | Versión inicial |

---

# Requerimientos No Funcionales

| Código | Nombre | Descripción |
|--------|--------|-------------|
| RNF_01 | Paleta de colores | La interfaz debe usar exclusivamente blanco, rojo y negro (y sus tonalidades). |
| RNF_02 | Diseño responsive | La plataforma debe adaptarse a móvil, tablet y escritorio (desde 320px hasta 1920px). |
| RNF_03 | Tiempo de respuesta | Todas las páginas y operaciones deben responder en menos de 3 segundos. |
| RNF_04 | Cifrado de contraseñas | Las contraseñas deben almacenarse cifradas con bcrypt o equivalente. |
| RNF_05 | Límite de archivos | Soporte para carga de archivos de hasta 5 MB (JPG, PNG, PDF). |
| RNF_06 | Disponibilidad del sistema | Disponibilidad mínima del 95% durante el periodo activo del torneo. |
| RNF_07 | Compatibilidad de navegadores | Debe funcionar correctamente en Chrome, Firefox, Edge y Safari (últimas 2 versiones). |
