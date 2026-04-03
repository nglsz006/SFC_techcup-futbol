Requirements · MD
Copiar

# Requerimientos — TechCup Fútbol

# Requerimientos — TechCup Fútbol

## Requerimientos Funcionales

| Código | Nombre | Tipo |
|--------|--------|------|
| RF_01 | Crear torneo | Funcional |
| RF_02 | Iniciar torneo | Funcional |
| RF_03 | Finalizar torneo | Funcional |
| RF_04 | Consultar torneo | Funcional |
| RF_06 | Registro de jugadores | Funcional |
| RF_08 | Perfil deportivo | Funcional |
| RF_11 | Disponibilidad de jugador | Funcional |
| RF_12 | Invitaciones a equipo | Funcional |
| RF_13 | Crear equipo | Funcional |
| RF_15 | Gestionar equipo | Funcional |
| RF_16 | Validar equipo | Funcional |
| RF_17 | Buscar jugadores | Funcional |
| RF_18 | Subir comprobante de pago | Funcional |
| RF_19 | Verificar pago | Funcional |
| RF_22 | Configurar torneo | Funcional |
| RF_23 | Gestionar alineación | Funcional |
| RF_24 | Consultar alineación rival | Funcional |
| RF_25 | Gestionar partido | Funcional |
| RF_26 | Consultar partidos asignados (árbitro) | Funcional |
| RF_27 | Tabla de posiciones | Funcional |
| RF_28 | Consultar bracket eliminatorio | Funcional |
| RF_29 | Estadísticas generales del torneo | Funcional |
| RF_30 | Registrar organizadores y árbitros | Funcional |
| RF_31 | Consultar auditoría del sistema | Funcional |
| RF_32 | Iniciar sesión | Funcional |
| RF_33 | Autenticación con Google | Funcional |
| RF_34 | Inscribir equipo al torneo | Funcional |
| RF_35 | Asignar árbitro a partido | Funcional |

---

## Requerimientos No Funcionales

| Código | Nombre | Tipo |
|--------|--------|------|
| RNF_01 | Paleta de colores | No Funcional |
| RNF_02 | Diseño responsive | No Funcional |
| RNF_03 | Tiempo de respuesta | No Funcional |
| RNF_04 | Cifrado de contraseñas | No Funcional |
| RNF_05 | Restricción de formato de comprobante | No Funcional |
| RNF_06 | Disponibilidad del sistema | No Funcional |
| RNF_07 | Compatibilidad de navegadores | No Funcional |
| RNF_08 | Control de acceso por roles | No Funcional |
| RNF_09 | Integridad de datos | No Funcional |
| RNF_10 | Autenticación segura | No Funcional |
| RNF_11 | Vigencia de sesión | No Funcional |
| RNF_12 | Documentación de API | No Funcional |
| RNF_13 | Registro de eventos y trazabilidad | No Funcional |
| RNF_14 | Portabilidad del sistema | No Funcional |
| RNF_15 | Integración y despliegue continuo | No Funcional |

---

## RF_01 — Crear torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_01 |
| **Nombre:** | Crear torneo |

| |                                                                                                                                                                                                                                                                   |
|---|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descripción:** | El sistema debe permitir que un usuario con rol Organizador registre un nuevo torneo ingresando la información general requerida. Al crearse, el torneo debe quedar almacenado en el sistema con estado inicial CREADO y asociado al organizador que lo registró. |
| **Cómo se ejecutará:** | El organizador accede al módulo de creación de torneos, diligencia el formulario con la información requerida y envía la solicitud de creación. El sistema valida los datos ingresados y, si son correctos, registra el torneo y retorna la información creada.   |
| **Actor principal:** | Organizador                                                                                                                                                                                                                                                       |
| **Precondiciones:** | El usuario debe estar autenticado, el usuario debe existir en el sistema, el usuario debe tener rol Organizador y el organizador debe tener permisos para registrar torneos.                                                                                      |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación                                                                                       | Obligatorio |
|--------|------------|---------------|-----------------------------------------------------------------------------------------------------------|-------------|
| Nombre | Nombre del torneo | String        | No puede estar vacío, no debe contener solo espacios y debe cumplir la longitud permitida por el sistema  | Sí |
| Fecha inicial | Fecha y hora de inicio del torneo | LocalDateTime | Debe ser una fecha válida y posterior o igual a la fecha actual según la regla del sistema   | Sí |
| Fecha final | Fecha y hora de finalización del torneo | LocalDateTime | Debe ser posterior a la fecha inicial                                                                          | Sí |
| Cantidad de equipos | Número máximo de equipos participantes | Integer       | Debe ser un número entero positivo mayor a 1                                                                       | Sí |
| Costo por equipo | Valor de inscripción por equipo | Double        | Debe ser mayor o igual a 0                                                                        | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Torneo creado | Información completa del torneo registrado | Objeto Torneo | Debe incluir identificador, datos ingresados, organizador asociado y estado inicial CREADO | Sí |
| Mensaje de confirmación | Confirmación de creación exitosa | String | Debe indicar que el torneo fue creado correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1    | Organizador | Accede al módulo de creación de torneos |             |
| 2    | Organizador | Ingresa nombre, fecha inicial, fecha final, cantidad de equipos y costo por equipo |             |
| 3    | Organizador | Envía la solicitud de creación del torneo |
| 4    | Sistema | Valida que el usuario esté autenticado, exista y tenga rol de Organizador | Usuario no autenticado o sin permisos |
| 5    | Sistema | Valida que todos los campos obligatorios estén completos y cumplan las reglas definidas | Campos inválidos o incompletos |
| 6    | Sistema | Registra el torneo con estado inicial CREADO y lo asocia al organizador |  Error al persistir información           |
| 7    | Sistema | Retorna la información del torneo creado y un mensaje de confirmación |             |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4    | Sistema | Si el usuario no está autenticado o no tiene rol de Organizador, rechaza la solicitud | Acceso no autorizado            |
| 5    | Sistema | Si el nombre está vacío o no cumple las reglas establecidas, no crea el torneo |  Nombre inválido           |
| 5    | Sistema | Si la fecha final es menor o igual a la fecha inicial, no crea el torneo |   Fechas inválidas          |
| 5    | Sistema | Si la cantidad de equipos es menor o igual a 1, no crea el torneo |  Cantidad de equipos inválida           |
| 6    | Sistema | Si el costo por equipo es negativo, no crea el torneo | Costo inválido            |
| 6    | Sistema | Si ocurre un error al guardar el torneo, informa el fallo y no registra la operación | Error interno del sistema            |

| |                                                                                                                                                                                                                                                                                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Notas y comentarios:** | os estados del torneo manejados por el sistema son: CREADO, EN_CURSO y FINALIZADO. El torneo debe iniciar siempre en estado CREADO. La relación entre organizador y torneo debe quedar definida por el sistema. Si el sistema no permite nombres duplicados de torneo, esta restricción debe incluirse explícitamente como regla de negocio. |

**Diagrama casos de uso**

![HU-05_Crear_torneo.drawio.png](../uml/CasosSeUso/HU-05_Crear_torneo.drawio.png)

**Prototipo**

![RF_01_CrearTorneo.PNG](../Images/Mock/RF_01_CrearTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1   | Solo un usuario con rol Organizador puede crear torneos. |
| 2   | El nombre del torneo es obligatorio y no puede estar vacío ni contener solo espacios. |
| 3   | La fecha final del torneo debe ser posterior a la fecha inicial. |
| 4   | La cantidad de equipos debe ser un número entero positivo mayor a 1. |
| 5   | El costo por equipo debe ser mayor o igual a 0. |
| 6   | Todo torneo creado debe registrarse con estado inicial CREADO. |
| 7   | El torneo creado debe quedar asociado al organizador que lo registró. |
| 8   | Si existe una restricción de unicidad de nombre o límite de torneos por organizador, esta debe ser validada antes de la creación. |

### Validaciones
**Validaciones para Frontend**

Frontend debería validar antes de enviar:
nombre no vacío
nombre sin solo espacios
fecha inicial diligenciada
fecha final diligenciada
fecha final > fecha inicial
cantidad de equipos > 1
costo por equipo >= 0

**Validaciones para Backend**
Backend debe validar: autenticación
rol del usuario
existencia del organizador
formato de datos
reglas de fechas
rango de cantidad de equipos
rango del costo
reglas de negocio adicionales

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha      | Descripción y Justificación de Cambios |
|--------------|-------------|------------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento, definiendo su estructura funcional, descripción, actores, precondiciones, entradas, salidas, flujo básico, flujo alterno y reglas de negocio para el Sprint #1. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Actualización y refinamiento del requerimiento con base en el feedback del Sprint #1, ajustando su alcance funcional, validaciones, estructura técnica, consistencia con el backlog, pruebas y arquitectura del sistema para el Sprint #2. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario a cada requerimiento |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste de entradas, salidas y reglas para reflejar la creación real del torneo y su estado inicial CREADO. |

---

## RF_02 — Iniciar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_02 |
| **Nombre:** | Iniciar torneo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador inicie un torneo previamente creado, siempre que cumpla con las condiciones necesarias para pasar a ejecución. |
| **Cómo se ejecutará:** | El organizador accede al módulo de gestión del torneo y selecciona la opción para iniciar el torneo. El sistema valida las condiciones requeridas y, si se cumplen, cambia el estado del torneo y genera automáticamente los partidos correspondientes. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado, tener rol de Organizador, existir en el sistema y contar con un torneo asociado en estado `CREADO`. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Acción | Solicitud de inicio del torneo | String / Enum | Valor esperado: `INICIAR` | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Torneo actualizado | Información del torneo con su estado actualizado | Objeto Torneo | El torneo debe quedar en estado `EN_CURSO` | Sí |
| Partidos generados | Lista de partidos creados automáticamente para el torneo | Lista de Partidos | Se generan al momento de iniciar el torneo | Sí |
| Mensaje de confirmación | Confirmación del inicio exitoso del torneo | String | Debe indicar que el torneo fue iniciado correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al módulo de gestión del torneo | |
| 2 | Organizador | Selecciona la opción para iniciar el torneo | |
| 3 | Sistema | Valida que el usuario esté autenticado, exista y tenga rol de Organizador | Usuario no autenticado o sin permisos |
| 4 | Sistema | Recupera el torneo asociado al organizador | Organizador sin torneo asociado |
| 5 | Sistema | Verifica que el torneo se encuentre en estado `CREADO` | Estado inválido |
| 6 | Sistema | Verifica que el torneo tenga al menos 2 equipos inscritos | Cantidad insuficiente de equipos |
| 7 | Sistema | Cambia el estado del torneo a `EN_CURSO` | Error al actualizar estado |
| 8 | Sistema | Genera automáticamente los partidos del torneo | Error al generar partidos |
| 9 | Sistema | Bloquea la inscripción de nuevos equipos en el torneo | |
| 10 | Sistema | Retorna el torneo actualizado, los partidos generados y un mensaje de confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene permisos, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el organizador no tiene un torneo asociado, no se ejecuta la operación | Torneo no encontrado |
| 5 | Sistema | Si el torneo no está en estado `CREADO`, no se puede iniciar | Transición de estado no permitida |
| 6 | Sistema | Si el torneo tiene menos de 2 equipos inscritos, no se puede iniciar | Requisito mínimo no cumplido |
| 8 | Sistema | Si ocurre un error durante la generación de partidos, el torneo no debe quedar parcialmente iniciado | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | Al iniciar el torneo, se deben generar automáticamente los partidos según la lógica de emparejamiento definida por el sistema. La inscripción de equipos debe quedar bloqueada una vez el torneo pase a estado `EN_CURSO`. |

**Diagrama casos de uso**

![RF_02_Iniciar_torneo.drawio.png](../uml/CasosSeUso/RF_02_Iniciar_torneo.drawio.png)

**Prototipo**

![RF_02_IniciarTorneo.PNG](../Images/Mock/RF_02_IniciarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un usuario con rol **Organizador** puede iniciar un torneo. |
| RN_02 | Un torneo solo puede iniciarse si se encuentra en estado `CREADO`. |
| RN_03 | El torneo debe tener al menos **2 equipos inscritos** para poder iniciar. |
| RN_04 | Al iniciar el torneo, el sistema debe cambiar su estado a `EN_CURSO`. |
| RN_05 | Al iniciar el torneo, el sistema debe **generar automáticamente los partidos** correspondientes. |
| RN_06 | Una vez iniciado el torneo, **no se permite la inscripción de nuevos equipos**. |
| RN_07 | Si ocurre un error durante la generación de partidos o actualización del estado, la operación no debe dejar el torneo en un estado inconsistente. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento dentro del módulo de gestión del torneo. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste de estructura funcional del requerimiento. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento de gestión del torneo para definir de manera independiente el inicio del torneo, sus validaciones, reglas de negocio y generación automática de partidos. |
---
## RF_03 — Finalizar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_03 |
| **Nombre:** | Finalizar torneo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador finalice un torneo en curso, siempre que se hayan cumplido las condiciones necesarias para cerrar oficialmente la competencia. |
| **Cómo se ejecutará:** | El organizador accede al módulo de gestión del torneo y selecciona la opción para finalizar el torneo. El sistema valida las condiciones requeridas y, si se cumplen, cambia el estado del torneo a finalizado. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado, tener rol de Organizador, existir en el sistema y contar con un torneo asociado en estado `EN_CURSO`. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Acción | Solicitud de finalización del torneo | String / Enum | Valor esperado: `FINALIZAR` | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Torneo actualizado | Información del torneo con su estado actualizado | Objeto Torneo | El torneo debe quedar en estado `FINALIZADO` | Sí |
| Mensaje de confirmación | Confirmación de la finalización exitosa del torneo | String | Debe indicar que el torneo fue finalizado correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al módulo de gestión del torneo | |
| 2 | Organizador | Selecciona la opción para finalizar el torneo | |
| 3 | Sistema | Valida que el usuario esté autenticado, exista y tenga rol de Organizador | Usuario no autenticado o sin permisos |
| 4 | Sistema | Recupera el torneo asociado al organizador | Organizador sin torneo asociado |
| 5 | Sistema | Verifica que el torneo se encuentre en estado `EN_CURSO` | Estado inválido |
| 6 | Sistema | Verifica que todos los partidos del torneo hayan sido jugados | Partidos pendientes |
| 7 | Sistema | Verifica que exista un equipo ganador definido | Ganador no definido |
| 8 | Sistema | Cambia el estado del torneo a `FINALIZADO` | Error al actualizar estado |
| 9 | Sistema | Congela las estadísticas finales del torneo | |
| 10 | Sistema | Retorna el torneo actualizado y un mensaje de confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene permisos, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el organizador no tiene un torneo asociado, no se ejecuta la operación | Torneo no encontrado |
| 5 | Sistema | Si el torneo no está en estado `EN_CURSO`, no se puede finalizar | Transición de estado no permitida |
| 6 | Sistema | Si existen partidos pendientes, no se puede finalizar el torneo | Requisito de cierre no cumplido |
| 7 | Sistema | Si no existe un ganador definido, no se puede finalizar el torneo | Ganador no definido |
| 8 | Sistema | Si ocurre un error durante la actualización del estado, la operación no debe completarse | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | Al finalizar el torneo, las estadísticas y resultados deben quedar consolidados. El torneo no debe poder volver a estado `EN_CURSO` o `CREADO`. |

**Diagrama casos de uso**

![RF_03_Finalizar_torneo.drawio.png](../uml/CasosSeUso/RF_03_Finalizar_torneo.drawio.png)

**Prototipo**

![RF_03_FinalizarTorneo.PNG](../Images/Mock/RF_03_FinalizarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un usuario con rol **Organizador** puede finalizar un torneo. |
| RN_02 | Un torneo solo puede finalizarse si se encuentra en estado EN_CURSO. |
| RN_03 | Todos los partidos del torneo deben estar marcados como jugados antes de finalizarlo. |
| RN_04 | Debe existir un **equipo ganador definido** para poder finalizar el torneo. |
| RN_05 | Al finalizar el torneo, el sistema debe cambiar su estado a FINALIZADO. |
| RN_06 | Al finalizar el torneo, las estadísticas deben quedar **congeladas** y no deben ser alteradas por operaciones posteriores. |
| RN_07 | Un torneo en estado FINALIZADO no puede volver a cambiar de estado. |
| RN_08 | Si ocurre un error durante la finalización, la operación no debe dejar el torneo en un estado inconsistente. |
| RN_09 | Una vez finalizado el torneo, no se deben permitir modificaciones sobre partidos, resultados o equipos inscritos. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento dentro del módulo de gestión del torneo. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste de estructura funcional del requerimiento. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Adición de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento de gestión del torneo para definir de manera independiente la finalización del torneo, sus validaciones, reglas de negocio y cierre formal de estadísticas y resultados. |
---

## RF_04 — Consultar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_04 |
| **Nombre:** | Consultar torneo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir a cualquier usuario autenticado consultar la información detallada de un torneo específico mediante su identificador. |
| **Cómo se ejecutará:** | El usuario accede al módulo de torneos y selecciona un torneo específico. El sistema recupera la información completa del torneo y la presenta al usuario. |
| **Actor principal:** | Usuario autenticado |
| **Precondiciones:** | El usuario debe estar autenticado y el torneo debe existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador único del torneo | String | Debe corresponder a un torneo existente | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Información del torneo | Detalle completo del torneo | Objeto Torneo | Incluye nombre, fechas, cantidad de equipos, costo, estado y configuración asociada | Sí |
| Configuración adicional | Información complementaria del torneo | Objeto | Puede incluir reglamento, cierre de inscripciones, canchas, horarios y sanciones cuando existan | No |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al módulo de torneos | |
| 2 | Usuario | Selecciona un torneo específico para consultar | |
| 3 | Sistema | Valida que el usuario esté autenticado | Usuario no autenticado |
| 4 | Sistema | Busca el torneo por su identificador | Torneo no encontrado |
| 5 | Sistema | Recupera la información completa del torneo | |
| 6 | Sistema | Retorna la información del torneo al usuario | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado, rechaza la solicitud | Acceso no autorizado |
| 4 | Sistema | Si el torneo no existe, responde con error | Torneo no encontrado |

| | |
|---|---|
| **Notas y comentarios:** | El sistema no diferencia la información visible del torneo según el rol del usuario autenticado; todos los usuarios ven la misma información. |

**Diagrama casos de uso**

![RF_04_Consultar_torneo.drawio.png](../uml/CasosSeUso/RF_04_Consultar_torneo.drawio.png)

**Prototipo**

![RF_04_ConsultarTorneo.PNG](../Images/Mock/RF_04_ConsultarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo usuarios autenticados pueden consultar la información de un torneo. |
| RN_02 | El torneo debe existir en el sistema para poder ser consultado. |
| RN_03 | La consulta debe retornar toda la información asociada al torneo disponible en el sistema. |
| RN_04 | Todos los usuarios autenticados visualizan la misma información del torneo, sin restricción por rol. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para definir la consulta individual de torneos de forma independiente y clara. |
---
## RF_05 — Listar torneos

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_05 |
| **Nombre:** | Listar torneos |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir a cualquier usuario autenticado visualizar el listado de torneos registrados en el sistema. |
| **Cómo se ejecutará:** | El usuario accede al módulo de torneos y solicita el listado general. El sistema recupera todos los torneos disponibles y los presenta en forma de lista. |
| **Actor principal:** | Usuario autenticado |
| **Precondiciones:** | El usuario debe estar autenticado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Filtros (opcional) | Parámetros de filtrado de torneos | Objeto | Puede incluir estado, fecha u otros criterios | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de torneos | Colección de torneos registrados | Lista de Objetos Torneo | Cada elemento contiene información resumida del torneo | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al módulo de torneos | |
| 2 | Usuario | Solicita el listado de torneos | |
| 3 | Sistema | Valida que el usuario esté autenticado | Usuario no autenticado |
| 4 | Sistema | Recupera la lista de torneos registrados | |
| 5 | Sistema | Retorna la lista de torneos al usuario | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado, rechaza la solicitud | Acceso no autorizado |
| 4 | Sistema | Si no existen torneos registrados, retorna una lista vacía | |

| | |
|---|---|
| **Notas y comentarios:** | La información retornada en el listado corresponde a un resumen de cada torneo. |

**Diagrama casos de uso**

![RF_05_Listar_torneos.drawio.png](../uml/CasosSeUso/RF_05_Listar_torneos.drawio.png)

**Prototipo**

![RF_05_ListarTorneos.PNG](../Images/Mock/RF_05_ListarTorneos.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo usuarios autenticados pueden visualizar el listado de torneos. |
| RN_02 | El sistema debe retornar todos los torneos registrados, incluso si la lista es vacía. |
| RN_03 | Cada torneo en el listado debe incluir información resumida suficiente para su identificación. |
| RN_04 | La consulta puede incluir filtros opcionales para refinar los resultados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para definir el listado de torneos de forma independiente y mejorar claridad funcional. |

---

## RF_06 — Registro de usuarios por formulario

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_06 |
| **Nombre:** | Registro de usuarios por formulario |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un participante se registre en la plataforma mediante un formulario de registro, ingresando su nombre, correo electrónico, contraseña y tipo de usuario. |
| **Cómo se ejecutará:** | El participante accede al formulario de registro, diligencia la información requerida y envía la solicitud. El sistema valida los datos ingresados y, si son correctos, crea la cuenta del usuario. |
| **Actor principal:** | Participante |
| **Precondiciones:** | El participante no debe tener una cuenta registrada previamente con el mismo correo electrónico. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Nombre | Nombre del usuario | String | No puede estar vacío ni contener solo espacios | Sí |
| Correo electrónico | Correo del participante | String | Debe tener formato válido y corresponder al dominio permitido según el tipo de usuario | Sí |
| Contraseña | Contraseña de acceso | String | Mínimo 8 caracteres | Sí |
| Tipo de usuario | Categoría del participante dentro de la plataforma | Enum / String | Valores permitidos: ESTUDIANTE, GRADUADO, PROFESOR | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta creada | Confirmación de creación de la cuenta | String | Mensaje: `Usuario registrado correctamente` | Sí |
| Usuario registrado | Información básica del usuario creado | Objeto Usuario | Incluye nombre, correo y tipo de usuario | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Participante | Accede al formulario de registro | |
| 2 | Participante | Ingresa nombre, correo electrónico, contraseña y tipo de usuario | |
| 3 | Participante | Envía la solicitud de registro | |
| 4 | Sistema | Valida que todos los campos obligatorios estén completos | Campos incompletos |
| 5 | Sistema | Valida el formato del correo electrónico | Correo inválido |
| 6 | Sistema | Valida que el dominio del correo corresponda al tipo de usuario seleccionado | Dominio no permitido |
| 7 | Sistema | Valida que la contraseña cumpla con la longitud mínima definida | Contraseña inválida |
| 8 | Sistema | Verifica que no exista una cuenta previa con el mismo correo | Correo ya registrado |
| 9 | Sistema | Crea la cuenta del usuario en la plataforma | Error al registrar usuario |
| 10 | Sistema | Retorna la confirmación del registro y la información básica del usuario | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si faltan campos obligatorios, rechaza la solicitud | Datos incompletos |
| 5 | Sistema | Si el correo no tiene formato válido, no crea la cuenta | Correo inválido |
| 6 | Sistema | Si el correo no pertenece al dominio permitido según el tipo de usuario, no crea la cuenta | Dominio no permitido |
| 7 | Sistema | Si la contraseña no cumple con la longitud mínima, no crea la cuenta | Contraseña inválida |
| 8 | Sistema | Si el correo ya está registrado, no crea la cuenta | Correo duplicado |
| 9 | Sistema | Si ocurre un error interno durante el registro, la cuenta no debe crearse parcialmente | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | Este requerimiento cubre únicamente el registro manual por formulario. El registro con Google para familiares se documenta en un requerimiento independiente. |

**Diagrama casos de uso**

![RF_06_Registro_usuarios_formulario.drawio.png](../uml/CasosSeUso/RF_06_Registro_usuarios_formulario.drawio.png)

**Prototipo**

![RF_06_RegistroUsuariosFormulario.PNG](../Images/Mock/RF_06_RegistroUsuariosFormulario.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El sistema debe permitir el registro por formulario únicamente para los tipos de usuario ESTUDIANTE, GRADUADO, PROFESOR. |
| RN_02 | El nombre del usuario es obligatorio y no puede estar vacío ni contener solo espacios. |
| RN_03 | El correo electrónico debe tener un formato válido. |
| RN_04 | El correo institucional es obligatorio para ESTUDIANTE, GRADUADO, PROFESOR. |
| RN_05 | La contraseña debe tener una longitud mínima de 8 caracteres. |
| RN_06 | No se permiten cuentas duplicadas con el mismo correo electrónico. |
| RN_07 | El sistema debe asociar el tipo de usuario seleccionado a la cuenta creada. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de registro de usuarios. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural del flujo de registro. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para definir de forma independiente el registro manual por formulario y evitar mezclarlo con autenticación externa. |
---

## RF_07 — Registro con Google para familiares

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_07 |
| **Nombre:** | Registro con Google para familiares |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un familiar se registre o inicie sesión en la plataforma utilizando su cuenta de Google, creando automáticamente la cuenta si no existe previamente. |
| **Cómo se ejecutará:** | El participante selecciona la opción de registro con Google desde la interfaz de acceso. El sistema autentica al usuario mediante Google y, si la cuenta no existe en la plataforma, la registra automáticamente como usuario tipo `FAMILIAR`. |
| **Actor principal:** | Familiar |
| **Precondiciones:** | El usuario debe contar con una cuenta válida de Google y autorizar el acceso a la plataforma. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta de Google | Cuenta utilizada para autenticarse | Cuenta externa | Debe ser una cuenta válida autenticada por Google | Sí |
| Correo electrónico | Correo recuperado desde Google | String | Se obtiene automáticamente del proveedor de autenticación | Sí |
| Nombre | Nombre recuperado desde Google | String | Se obtiene automáticamente del proveedor de autenticación | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta registrada o autenticada | Confirmación del acceso del usuario | String | Puede indicar registro exitoso o inicio de sesión exitoso | Sí |
| Usuario autenticado | Información básica del usuario autenticado | Objeto Usuario | Incluye nombre, correo y tipo de usuario FAMILIAR | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Familiar | Accede a la pantalla de acceso o registro | |
| 2 | Familiar | Selecciona la opción `Continuar con Google` | |
| 3 | Sistema | Redirige al proveedor de autenticación de Google | Error de autenticación externa |
| 4 | Familiar | Autoriza el acceso con su cuenta de Google | Acceso no autorizado por el usuario |
| 5 | Sistema | Recibe la información básica del usuario autenticado desde Google | Datos no disponibles |
| 6 | Sistema | Verifica si ya existe una cuenta asociada al correo recuperado | |
| 7 | Sistema | Si la cuenta no existe, crea automáticamente el usuario con tipo FAMILIAR | Error al registrar usuario |
| 8 | Sistema | Si la cuenta ya existe, autentica al usuario en la plataforma | |
| 9 | Sistema | Retorna la confirmación de acceso y la información básica del usuario | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si ocurre un error en la autenticación con Google, no se completa el proceso | Error de autenticación externa |
| 4 | Familiar | Si el usuario no autoriza el acceso, el sistema cancela la operación | Acceso denegado |
| 5 | Sistema | Si Google no retorna la información mínima requerida, no se registra ni autentica al usuario | Datos insuficientes |
| 7 | Sistema | Si ocurre un error al registrar automáticamente al usuario, la cuenta no debe quedar parcialmente creada | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | Este requerimiento aplica exclusivamente al tipo de usuario FAMILIAR y utiliza autenticación externa con Google como mecanismo de registro y acceso. |

**Diagrama casos de uso**

![RF_07_Registro_google_familiares.drawio.png](../uml/CasosSeUso/RF_07_Registro_google_familiares.drawio.png)

**Prototipo**

![RF_07_RegistroGoogleFamiliares.PNG](../Images/Mock/RF_07_RegistroGoogleFamiliares.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El registro con Google solo aplica para usuarios de tipo FAMILIAR. |
| RN_02 | El sistema debe recuperar automáticamente el nombre y correo electrónico desde Google. |
| RN_03 | Si el usuario autenticado con Google no existe previamente en la plataforma, el sistema debe registrarlo automáticamente. |
| RN_04 | Si el usuario ya existe en la plataforma, el sistema debe autenticarlo sin duplicar la cuenta. |
| RN_05 | Toda cuenta creada mediante este flujo debe registrarse con tipo de usuario FAMILIAR. |
| RN_06 | El sistema no debe crear cuentas incompletas si ocurre un fallo durante el proceso de autenticación o registro. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de registro de usuarios. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural del flujo de registro. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente el registro y autenticación con Google para familiares. |

---

## RF_08 — Crear perfil deportivo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_08 |
| **Nombre:** | Crear perfil deportivo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un usuario registrado cree su perfil deportivo para participar como jugador dentro de la plataforma. |
| **Cómo se ejecutará:** | El usuario accede al módulo de perfil deportivo, diligencia la información requerida y envía la solicitud de creación. El sistema valida los datos y, si son correctos, registra el perfil deportivo asociado al usuario. |
| **Actor principal:** | Usuario registrado que participará como jugador |
| **Precondiciones:** | El usuario debe existir en el sistema, estar autenticado y no debe tener un perfil deportivo previamente creado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Posiciones de juego | Posiciones en las que puede desempeñarse el jugador | Lista de Enum | Debe enviarse al menos una posición válida | Sí |
| Número dorsal | Número de camiseta del jugador | Integer | Debe ser mayor que 0 | Sí |
| Foto | Imagen o referencia de la foto del jugador | String | Campo opcional | No |
| Semestre | Semestre académico actual del jugador, si aplica | Integer | Si se envía, debe ser mayor que 0 | Condicional |
| Edad | Edad del jugador | Integer | Debe estar entre 1 y 100 | Sí |
| Género | Género del jugador | Enum | Valores permitidos: MASCULINO, FEMENINO, OTRO | Sí |
| Identificación | Número de identificación del jugador | String | No puede estar vacío y debe ser único en el sistema | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Perfil deportivo creado | Información del perfil deportivo registrado | Objeto PerfilDeportivo | Debe quedar asociado al usuario | Sí |
| Mensaje de confirmación | Confirmación de creación exitosa | String | Debe indicar que el perfil fue creado correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al módulo de perfil deportivo | |
| 2 | Usuario | Ingresa la información requerida para crear su perfil deportivo | |
| 3 | Usuario | Envía la solicitud de creación del perfil | |
| 4 | Sistema | Valida que el usuario exista, esté autenticado y no tenga un perfil deportivo previo | Usuario no encontrado o perfil ya existente |
| 5 | Sistema | Valida posiciones, dorsal, edad, género, identificación y semestre cuando corresponda | Datos inválidos |
| 6 | Sistema | Registra el perfil deportivo y lo asocia al usuario | Error al guardar información |
| 7 | Sistema | Retorna el perfil deportivo creado y un mensaje de confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no existe o ya tiene perfil deportivo, rechaza la creación | Perfil ya registrado |
| 5 | Sistema | Si alguno de los datos no cumple las reglas establecidas, no crea el perfil | Datos inválidos |
| 6 | Sistema | Si ocurre un error interno durante el registro, el perfil no debe quedar parcialmente creado | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | El perfil deportivo solo puede ser creado por usuarios que participarán como jugadores dentro del sistema. |

**Diagrama casos de uso**

![RF_08_Crear_perfil_deportivo.drawio.png](../uml/CasosSeUso/RF_08_Crear_perfil_deportivo.drawio.png)

**Prototipo**

![RF_08_CrearPerfilDeportivo.PNG](../Images/Mock/RF_08_CrearPerfilDeportivo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un usuario registrado que vaya a participar como jugador puede crear un perfil deportivo. |
| RN_02 | Cada usuario puede tener un único perfil deportivo registrado. |
| RN_03 | Debe seleccionarse al menos una posición de juego válida. |
| RN_04 | El número dorsal debe ser un número entero positivo. |
| RN_05 | La identificación del jugador es obligatoria y debe ser única en todo el sistema. |
| RN_06 | El semestre solo aplica cuando corresponda al tipo de usuario del jugador. |
| RN_07 | El perfil deportivo creado debe quedar asociado al usuario que lo registró. |
| RN_08 | La validación de unicidad del dorsal aplica dentro del equipo una vez el jugador sea vinculado a uno. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de perfil deportivo. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente la creación del perfil deportivo y sus validaciones. |

---
## RF_09 — Consultar perfil deportivo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_09 |
| **Nombre:** | Consultar perfil deportivo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un jugador consulte la información de su perfil deportivo previamente registrado. |
| **Cómo se ejecutará:** | El jugador accede al módulo de perfil deportivo y solicita la visualización de su información registrada. El sistema recupera y presenta los datos del perfil deportivo asociado al usuario. |
| **Actor principal:** | Jugador registrado |
| **Precondiciones:** | El usuario debe estar autenticado, existir en el sistema y tener un perfil deportivo registrado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Identificador del usuario o sesión activa | Referencia del jugador autenticado | String / Token | Se utiliza para localizar el perfil deportivo asociado | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Perfil deportivo | Información registrada del perfil del jugador | Objeto PerfilDeportivo | Incluye posiciones, dorsal, foto, semestre, edad, género e identificación | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede al módulo de perfil deportivo | |
| 2 | Jugador | Solicita la consulta de su perfil deportivo | |
| 3 | Sistema | Valida que el usuario esté autenticado y exista en el sistema | Usuario no autenticado o no encontrado |
| 4 | Sistema | Busca el perfil deportivo asociado al jugador | Perfil no encontrado |
| 5 | Sistema | Recupera y retorna la información del perfil deportivo | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no existe, rechaza la solicitud | Acceso no autorizado |
| 4 | Sistema | Si el jugador no tiene perfil deportivo registrado, responde con error | Perfil no registrado |

| | |
|---|---|
| **Notas y comentarios:** | La consulta del perfil deportivo retorna la información actual registrada en el sistema para el jugador autenticado. |

**Diagrama casos de uso**

![RF_09_Consultar_perfil_deportivo.drawio.png](../uml/CasosSeUso/RF_09_Consultar_perfil_deportivo.drawio.png)

**Prototipo**

![RF_09_ConsultarPerfilDeportivo.PNG](../Images/Mock/RF_09_ConsultarPerfilDeportivo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un jugador autenticado puede consultar su perfil deportivo. |
| RN_02 | El perfil deportivo consultado debe existir en el sistema. |
| RN_03 | La consulta debe retornar la información actual registrada del jugador. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de perfil deportivo. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente la consulta del perfil deportivo. |
---

## RF_10 — Actualizar perfil deportivo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_10 |
| **Nombre:** | Actualizar perfil deportivo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un jugador actualice la información editable de su perfil deportivo previamente registrado. |
| **Cómo se ejecutará:** | El jugador accede al módulo de edición de perfil deportivo, modifica los campos permitidos y envía la solicitud de actualización. El sistema valida los cambios y actualiza la información correspondiente. |
| **Actor principal:** | Jugador registrado |
| **Precondiciones:** | El usuario debe estar autenticado, existir en el sistema y tener un perfil deportivo registrado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Posiciones de juego | Posiciones actualizadas del jugador | Lista de Enum | Si se envía, debe contener al menos una posición válida | No |
| Número dorsal | Nuevo número de camiseta del jugador | Integer | Si se envía, debe ser mayor que 0 | No |
| Foto | Nueva imagen o referencia de la foto del jugador | String | Campo opcional editable | No |
| Semestre | Nuevo semestre académico, si aplica | Integer | Si se envía, debe ser mayor que 0 | No |
| Edad | Nueva edad del jugador | Integer | Si se envía, debe estar entre 1 y 100 | No |
| Género | Género actualizado del jugador | Enum | Valores permitidos: MASCULINO, FEMENINO, OTRO | No |
| Identificación | Número de identificación actualizado | String | Si se envía, no puede estar vacío y debe seguir siendo único | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Perfil deportivo actualizado | Información del perfil deportivo después de la actualización | Objeto PerfilDeportivo | Debe reflejar los cambios aplicados | Sí |
| Mensaje de confirmación | Confirmación de actualización exitosa | String | Debe indicar que el perfil fue actualizado correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede al módulo de edición de perfil deportivo | |
| 2 | Jugador | Modifica uno o varios campos permitidos del perfil | |
| 3 | Jugador | Envía la solicitud de actualización | |
| 4 | Sistema | Valida que el usuario esté autenticado, exista y tenga perfil deportivo registrado | Usuario no encontrado o perfil no registrado |
| 5 | Sistema | Valida que los campos enviados cumplan las reglas definidas | Datos inválidos |
| 6 | Sistema | Actualiza la información del perfil deportivo | Error al guardar cambios |
| 7 | Sistema | Retorna el perfil actualizado y un mensaje de confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no existe o no tiene perfil deportivo, no actualiza la información | Perfil no registrado |
| 5 | Sistema | Si los datos enviados son inválidos, rechaza la actualización | Datos inválidos |
| 6 | Sistema | Si ocurre un error interno, la actualización no debe quedar parcialmente aplicada | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | El sistema solo debe permitir la actualización de campos editables definidos para el perfil deportivo del jugador. |

**Diagrama casos de uso**

![RF_10_Actualizar_perfil_deportivo.drawio.png](../uml/CasosSeUso/RF_10_Actualizar_perfil_deportivo.drawio.png)

**Prototipo**

![RF_10_ActualizarPerfilDeportivo.PNG](../Images/Mock/RF_10_ActualizarPerfilDeportivo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un jugador autenticado con perfil deportivo registrado puede actualizar su perfil. |
| RN_02 | La actualización solo puede aplicarse sobre campos editables permitidos por el sistema. |
| RN_03 | Si se actualiza la identificación, esta debe seguir siendo única en todo el sistema. |
| RN_04 | Si se actualiza el dorsal, este debe seguir siendo un número positivo. |
| RN_05 | La validación de unicidad del dorsal aplica dentro del equipo una vez el jugador sea vinculado a uno. |
| RN_06 | La actualización no debe dejar el perfil en un estado inconsistente si ocurre un error durante el proceso. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de perfil deportivo. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente la actualización del perfil deportivo y sus validaciones. |

---

## RF_11 — Marcar disponibilidad de jugador

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_11 |
| **Nombre:** | Marcar disponibilidad de jugador |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un jugador registrado marque su estado como disponible para ser considerado en procesos de invitación a equipos dentro de la plataforma. |
| **Cómo se ejecutará:** | El jugador accede a su perfil deportivo y selecciona la opción para marcarse como disponible. El sistema valida las condiciones requeridas y actualiza su estado de disponibilidad. |
| **Actor principal:** | Jugador registrado |
| **Precondiciones:** | El usuario debe estar autenticado, tener un perfil deportivo registrado y no pertenecer actualmente a un equipo. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Solicitud de disponibilidad | Acción de marcar disponibilidad del jugador | Acción del sistema | Se ejecuta mediante interacción del usuario | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de disponibilidad | Estado actualizado del jugador | Boolean | Valor `true` indicando disponibilidad | Sí |
| Mensaje de confirmación | Confirmación de la operación | String | Mensaje: `Jugador marcado como disponible` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a su perfil deportivo | |
| 2 | Jugador | Selecciona la opción para marcarse como disponible | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga perfil deportivo | Usuario no autenticado o sin perfil |
| 4 | Sistema | Verifica que el jugador no pertenezca a un equipo | Jugador ya pertenece a un equipo |
| 5 | Sistema | Actualiza el estado de disponibilidad del jugador a true | Error al actualizar |
| 6 | Sistema | Retorna confirmación de la operación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene perfil, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el jugador ya pertenece a un equipo, no se permite marcar disponibilidad | Restricción de negocio |
| 5 | Sistema | Si ocurre un error interno, no se actualiza el estado | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | El sistema no permite que el jugador marque manualmente su estado como no disponible. Este estado cambia automáticamente cuando el jugador acepta una invitación a un equipo. |

**Diagrama casos de uso**

![RF_11_Disponibilidad_jugador.drawio.png](../uml/CasosSeUso/RF_11_Disponibilidad_jugador.drawio.png)

**Prototipo**

![RF_11_DisponibilidadJugador.PNG](../Images/Mock/RF_11_DisponibilidadJugador.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un jugador con perfil deportivo registrado puede marcarse como disponible. |
| RN_02 | Un jugador que pertenece a un equipo no puede marcarse como disponible. |
| RN_03 | La disponibilidad del jugador se establece únicamente en valor true mediante esta operación. |
| RN_04 | El estado de disponibilidad cambia automáticamente a no disponible cuando el jugador acepta una invitación a un equipo. |
| RN_05 | El sistema debe garantizar que el estado de disponibilidad refleje la situación actual del jugador en la plataforma. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para definir claramente la operación de disponibilidad, sus validaciones y restricciones de negocio. |
---

## RF_12 — Aceptar invitación a equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_12 |
| **Nombre:** | Aceptar invitación a equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un jugador registre la aceptación de una invitación a equipo, actualizando su estado de disponibilidad dentro de la plataforma. |
| **Cómo se ejecutará:** | El jugador accede a la opción para responder invitaciones y selecciona la acción de aceptar. El sistema procesa la respuesta y actualiza su estado de disponibilidad. |
| **Actor principal:** | Jugador registrado |
| **Precondiciones:** | El usuario debe estar autenticado, existir en el sistema y tener perfil deportivo registrado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Respuesta de invitación | Confirmación de aceptación de invitación | Acción / Enum | Valor esperado: `ACEPTAR` | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de disponibilidad | Estado actualizado del jugador | Boolean | Debe quedar en `false` | Sí |
| Mensaje de confirmación | Confirmación de aceptación de invitación | String | Mensaje: `Invitación aceptada correctamente` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a la opción para responder invitaciones | |
| 2 | Jugador | Selecciona la acción de aceptar invitación | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga perfil deportivo registrado | Usuario no autenticado o sin perfil |
| 4 | Sistema | Recupera el jugador asociado a la sesión o identificador enviado | Jugador no encontrado |
| 5 | Sistema | Actualiza el estado de disponibilidad del jugador a `false` | Error al actualizar |
| 6 | Sistema | Retorna el mensaje de confirmación correspondiente | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene perfil deportivo, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el jugador no existe en el sistema, no procesa la aceptación | Jugador no encontrado |
| 5 | Sistema | Si ocurre un error interno al actualizar la disponibilidad, la operación no se completa | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | En el comportamiento actual del backend, aceptar una invitación solo actualiza la disponibilidad del jugador a no disponible. No implica automáticamente su vinculación a un equipo ni el registro persistente del detalle de la invitación. |

**Diagrama casos de uso**

![RF_12_Aceptar_invitacion_equipo.drawio.png](../uml/CasosSeUso/RF_12_Aceptar_invitacion_equipo.drawio.png)

**Prototipo**

![RF_12_AceptarInvitacionEquipo.PNG](../Images/Mock/RF_12_AceptarInvitacionEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un jugador registrado con perfil deportivo puede aceptar una invitación a equipo. |
| RN_02 | Al aceptar una invitación, el estado de disponibilidad del jugador debe cambiar a `false`. |
| RN_03 | La aceptación de la invitación no implica automáticamente la vinculación del jugador a un equipo en la versión actual del backend. |
| RN_04 | El sistema no debe dejar el estado del jugador inconsistente si ocurre un error durante la operación. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de invitaciones. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente la aceptación de invitaciones a equipo según el comportamiento real del backend. |

---

## RF_13 — Rechazar invitación a equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_13 |
| **Nombre:** | Rechazar invitación a equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un jugador registre el rechazo de una invitación a equipo, actualizando su estado de disponibilidad dentro de la plataforma. |
| **Cómo se ejecutará:** | El jugador accede a la opción para responder invitaciones y selecciona la acción de rechazar. El sistema procesa la respuesta y actualiza su estado de disponibilidad. |
| **Actor principal:** | Jugador registrado |
| **Precondiciones:** | El usuario debe estar autenticado, existir en el sistema y tener perfil deportivo registrado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Respuesta de invitación | Confirmación de rechazo de invitación | Acción / Enum | Valor esperado: `RECHAZAR` | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Estado de disponibilidad | Estado actualizado del jugador | Boolean | Debe quedar en `true` | Sí |
| Mensaje de confirmación | Confirmación de rechazo de invitación | String | Mensaje: `Invitación rechazada correctamente` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Accede a la opción para responder invitaciones | |
| 2 | Jugador | Selecciona la acción de rechazar invitación | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga perfil deportivo registrado | Usuario no autenticado o sin perfil |
| 4 | Sistema | Recupera el jugador asociado a la sesión o identificador enviado | Jugador no encontrado |
| 5 | Sistema | Actualiza el estado de disponibilidad del jugador a `true` | Error al actualizar |
| 6 | Sistema | Retorna el mensaje de confirmación correspondiente | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene perfil deportivo, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el jugador no existe en el sistema, no procesa el rechazo | Jugador no encontrado |
| 5 | Sistema | Si ocurre un error interno al actualizar la disponibilidad, la operación no se completa | Error interno del sistema |

| | |
|---|---|
| **Notas y comentarios:** | En el comportamiento actual del backend, rechazar una invitación actualiza la disponibilidad del jugador a disponible. No se persiste el detalle histórico de la invitación. |

**Diagrama casos de uso**

![RF_13_Rechazar_invitacion_equipo.drawio.png](../uml/CasosSeUso/RF_13_Rechazar_invitacion_equipo.drawio.png)

**Prototipo**

![RF_13_RechazarInvitacionEquipo.PNG](../Images/Mock/RF_13_RechazarInvitacionEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un jugador registrado con perfil deportivo puede rechazar una invitación a equipo. |
| RN_02 | Al rechazar una invitación, el estado de disponibilidad del jugador debe cambiar a `true`. |
| RN_03 | El rechazo de la invitación no genera vinculación a un equipo ni persistencia del detalle de la invitación en la versión actual del backend. |
| RN_04 | El sistema no debe dejar el estado del jugador inconsistente si ocurre un error durante la operación. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento de invitaciones. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y ajuste estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Separación del requerimiento original para documentar de forma independiente el rechazo de invitaciones a equipo según el comportamiento real del backend. |

---

## RF_14 — Crear equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_14 |
| **Nombre:** | Crear equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un usuario con rol de capitán cree un nuevo equipo dentro de la plataforma ingresando el nombre del equipo. Al crearlo, el capitán debe quedar automáticamente asociado como creador y miembro del equipo. |
| **Cómo se ejecutará:** | El capitán accede al módulo de equipos, ingresa el nombre del equipo y envía la solicitud de creación. El sistema valida la información, registra el equipo y vincula automáticamente al capitán como integrante. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, existir en el sistema, tener perfil deportivo registrado, estar habilitado como capitán y no pertenecer ya a otro equipo como capitán. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Nombre del equipo | Nombre que identifica al equipo | String | No puede estar vacío, no debe contener solo espacios y debe ser único en el sistema | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Equipo creado | Información del equipo registrado | Objeto Equipo | Incluye identificador, nombre, capitán asociado y miembros iniciales | Sí |
| Mensaje de confirmación | Confirmación de creación exitosa | String | `Equipo creado correctamente` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al módulo de equipos | |
| 2 | Capitán | Ingresa el nombre del equipo | |
| 3 | Capitán | Envía la solicitud de creación | |
| 4 | Sistema | Valida que el usuario esté autenticado, exista y tenga rol de capitán | Usuario no autorizado |
| 5 | Sistema | Verifica que el capitán no tenga previamente un equipo creado | Capitán ya tiene equipo |
| 6 | Sistema | Valida que el nombre del equipo sea válido y no esté repetido | Nombre inválido o duplicado |
| 7 | Sistema | Crea el equipo y lo asocia al capitán | Error al guardar |
| 8 | Sistema | Registra automáticamente al capitán como miembro del equipo | Error al asociar capitán |
| 9 | Sistema | Retorna la información del equipo creado y mensaje de confirmación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no está autenticado o no tiene rol de capitán, rechaza la operación | Acceso no autorizado |
| 5 | Sistema | Si el capitán ya tiene un equipo creado, no se permite registrar otro | Restricción de negocio |
| 6 | Sistema | Si el nombre del equipo es inválido o ya existe en el sistema, no se crea el equipo | Nombre inválido o duplicado |
| 7 | Sistema | Si ocurre un error durante la creación, el equipo no debe quedar parcialmente registrado | Error interno |
| 8 | Sistema | Si falla la vinculación automática del capitán al equipo, la operación no debe dejar el sistema en estado inconsistente | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la creación del equipo solo requiere el nombre. Otros atributos como escudo o colores pueden gestionarse en funcionalidades posteriores. |

**Diagrama casos de uso**

![RF_14_Crear_equipo.drawio.png](../uml/CasosSeUso/RF_14_Crear_equipo.drawio.png)

**Prototipo**

![RF_14_CrearEquipo.PNG](../Images/Mock/RF_14_CrearEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un usuario con rol de capitán puede crear equipos. |
| RN_02 | Un capitán solo puede crear un único equipo dentro del sistema. |
| RN_03 | El nombre del equipo es obligatorio, no puede estar vacío y debe ser único en el sistema. |
| RN_04 | El equipo creado debe quedar asociado al capitán que lo registró. |
| RN_05 | Al crear el equipo, el capitán debe quedar automáticamente registrado como miembro del equipo. |
| RN_06 | El sistema no debe permitir la creación parcial del equipo si ocurre un error durante el proceso. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para mejorar claridad funcional, validaciones y alineación con las reglas de negocio definidas para capitanes y equipos. |
---

## RF_15 — Gestionar equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_15 |
| **Nombre:** | Gestionar equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un capitán gestione la administración básica de su equipo dentro de la plataforma, incluyendo la visualización de su composición actual y el envío de invitaciones a jugadores disponibles. |
| **Cómo se ejecutará:** | El capitán accede al módulo de gestión de su equipo, donde puede consultar la información general del equipo, visualizar sus integrantes y ejecutar acciones disponibles como invitar jugadores. El sistema valida las condiciones requeridas y procesa la operación correspondiente. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, existir como capitán en el sistema y tener un equipo previamente creado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Acción de gestión | Acción a ejecutar sobre el equipo | Enum / String | Puede incluir consulta de composición o invitación de jugador según las opciones habilitadas por el sistema | Sí |
| Jugador a invitar | Identificador del jugador seleccionado para invitación | String / Selector | Solo aplica cuando la acción sea invitar y el jugador debe encontrarse disponible | Condicional |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Información del equipo | Datos actuales del equipo gestionado | Objeto Equipo | Incluye nombre del equipo, capitán e integrantes actuales | Sí |
| Resultado de la operación | Confirmación de la acción ejecutada | String | Puede indicar consulta exitosa o invitación enviada correctamente | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al módulo de gestión de su equipo | |
| 2 | Capitán | Selecciona la acción de gestión que desea realizar | |
| 3 | Sistema | Valida que el usuario esté autenticado, tenga rol de capitán y posea un equipo creado | Usuario no autorizado o sin equipo |
| 4 | Sistema | Recupera la información actual del equipo | Equipo no encontrado |
| 5 | Capitán | Si desea invitar un jugador, selecciona un jugador disponible | Jugador no encontrado o no disponible |
| 6 | Sistema | Procesa la acción seleccionada por el capitán | Error al ejecutar acción |
| 7 | Sistema | Retorna la información del equipo y el resultado de la operación ejecutada | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado, no es capitán o no tiene equipo creado, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el equipo no existe, no permite continuar con la gestión | Equipo no encontrado |
| 5 | Sistema | Si el jugador seleccionado no existe o no está disponible, no procesa la invitación | Restricción de negocio |
| 6 | Sistema | Si ocurre un error interno durante la gestión, la operación no debe dejar el sistema en estado inconsistente | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la gestión del equipo se enfoca en la administración básica del equipo y el envío de invitaciones a jugadores disponibles. No incluye edición avanzada de datos del equipo ni administración completa de plantilla persistida. |

**Diagrama casos de uso**

![RF_15_Gestionar_equipo.drawio.png](../uml/CasosSeUso/RF_15_Gestionar_equipo.drawio.png)

**Prototipo**

![RF_15_GestionarEquipo.PNG](../Images/Mock/RF_15_GestionarEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un usuario con rol de capitán puede gestionar un equipo. |
| RN_02 | El capitán debe tener un equipo creado para acceder a las opciones de gestión. |
| RN_03 | Solo se puede invitar a jugadores que se encuentren disponibles. |
| RN_04 | La gestión del equipo debe operar únicamente sobre el equipo asociado al capitán autenticado. |
| RN_05 | Las acciones disponibles dentro de la gestión del equipo dependen de las funcionalidades habilitadas por el sistema en la versión actual. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Redefinición del requerimiento para reflejar la gestión básica real del equipo por parte del capitán y evitar limitarlo únicamente al envío de invitaciones. |
---

## RF_16 — Validar composición del equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_16 |
| **Nombre:** | Validar composición del equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un capitán valide la composición de su equipo verificando que la cantidad de jugadores registrados cumpla con los límites definidos por el sistema. |
| **Cómo se ejecutará:** | El capitán accede al módulo de su equipo y solicita la validación de la composición. El sistema evalúa la cantidad de jugadores registrados y retorna el resultado de la validación. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, existir como capitán en el sistema y tener un equipo previamente creado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Solicitud de validación | Acción de validar la composición del equipo | Acción del sistema | Se ejecuta mediante interacción del capitán | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Resultado de validación | Resultado de la evaluación del equipo | Objeto | Incluye identificador del equipo, nombre, total de jugadores, estado de validez y mensaje asociado | Sí |
| Estado de validez | Indica si el equipo cumple con las reglas | Boolean | `true` si cumple, `false` si no cumple | Sí |
| Mensaje | Descripción del resultado de la validación | String | Puede indicar cumplimiento o error según el caso | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede a la gestión de su equipo | |
| 2 | Capitán | Solicita la validación de la composición del equipo | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga un equipo creado | Capitán no autorizado o sin equipo |
| 4 | Sistema | Recupera la información del equipo | Equipo no encontrado |
| 5 | Sistema | Cuenta el total de jugadores registrados en el equipo | |
| 6 | Sistema | Evalúa si la cantidad de jugadores está dentro del rango permitido | |
| 7 | Sistema | Retorna el resultado de la validación con el estado correspondiente | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene equipo, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el equipo no existe, no se puede realizar la validación | Equipo no encontrado |
| 6 | Sistema | Si el equipo tiene menos de 7 jugadores, retorna mensaje de error por cantidad insuficiente | No cumple mínimo |
| 6 | Sistema | Si el equipo tiene más de 12 jugadores, retorna mensaje de error por exceso de jugadores | Excede máximo |

| | |
|---|---|
| **Notas y comentarios:** | La validación implementada actualmente se basa únicamente en la cantidad de jugadores registrados en el equipo. No contempla otras reglas de composición en esta versión del sistema. |

**Diagrama casos de uso**

![RF_16_Validar_composicion_equipo.drawio.png](../uml/CasosSeUso/RF_16_Validar_composicion_equipo.drawio.png)

**Prototipo**

![RF_16_ValidarEquipo.PNG](../Images/Mock/RF_16_ValidarEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Un equipo debe tener mínimo 7 jugadores para ser válido. |
| RN_02 | Un equipo no puede tener más de 12 jugadores. |
| RN_03 | La validación del equipo se basa únicamente en la cantidad total de jugadores registrados. |
| RN_04 | Solo el capitán del equipo puede realizar la validación. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para mejorar claridad funcional y definir correctamente la validación de la composición del equipo. |
---

## RF_17 — Buscar jugadores

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_17 |
| **Nombre:** | Buscar jugadores |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que un capitán busque jugadores registrados en la plataforma según su posición de juego, mostrando únicamente aquellos que se encuentren disponibles para ser invitados a un equipo. |
| **Cómo se ejecutará:** | El capitán accede al módulo de búsqueda de jugadores, selecciona la posición que desea consultar y envía la solicitud. El sistema procesa la búsqueda y retorna la lista de jugadores disponibles que cumplen con el criterio indicado. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, tener rol de capitán y existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Posición | Posición de juego utilizada como criterio de búsqueda | String / Enum | Debe corresponder a una posición válida del sistema | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de jugadores | Conjunto de jugadores disponibles que coinciden con la posición consultada | Lista de Objeto JugadorResponse | Puede incluir información básica como nombre, posición, dorsal y disponibilidad | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al módulo de búsqueda de jugadores | |
| 2 | Capitán | Selecciona la posición de juego que desea consultar | |
| 3 | Capitán | Envía la solicitud de búsqueda | |
| 4 | Sistema | Valida que el usuario esté autenticado y tenga rol de capitán | Usuario no autorizado |
| 5 | Sistema | Filtra los jugadores registrados por la posición indicada | Posición inválida |
| 6 | Sistema | Excluye de los resultados a los jugadores que no se encuentren disponibles | |
| 7 | Sistema | Retorna la lista de jugadores disponibles que cumplen con la posición consultada | |
| 8 | Capitán | Revisa el resultado para identificar posibles jugadores a invitar | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no está autenticado o no tiene rol de capitán, rechaza la búsqueda | Acceso no autorizado |
| 5 | Sistema | Si no se envía una posición válida, no se ejecuta la búsqueda | Posición inválida |
| 7 | Sistema | Si no existen jugadores disponibles que coincidan con la posición consultada, retorna una lista vacía | Sin resultados |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la búsqueda filtra por posición de juego y solo retorna jugadores que se encuentren disponibles. No se aplican filtros adicionales como semestre, edad, género o nombre. |

**Diagrama casos de uso**

![RF_17_Buscar_jugadores.drawio.png](../uml/CasosSeUso/RF_17_Buscar_jugadores.drawio.png)

**Prototipo**

![RF_17_BuscarJugadores.PNG](../Images/Mock/RF_17_BuscarJugadores.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | La posición de juego es obligatoria para realizar la búsqueda. |
| RN_02 | Solo un usuario con rol de capitán puede buscar jugadores desde esta funcionalidad. |
| RN_03 | La búsqueda debe filtrar jugadores por posición de juego. |
| RN_04 | Solo deben aparecer en los resultados jugadores que se encuentren disponibles. |
| RN_05 | Si no existen coincidencias, el sistema debe retornar una lista vacía. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la búsqueda de jugadores por posición y la restricción de disponibilidad en los resultados. |
---

## RF_18 — Subir comprobante de pago

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_18 |
| **Nombre:** | Subir comprobante de pago |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el capitán de un equipo registre el comprobante de pago correspondiente a la inscripción de su equipo dentro del torneo. |
| **Cómo se ejecutará:** | El capitán accede al módulo de pagos, selecciona su equipo e ingresa la referencia del comprobante de pago. El sistema valida la información y registra el pago con estado inicial pendiente. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, existir como capitán en el sistema, ser el responsable del equipo asociado y el equipo no debe tener un pago aprobado o pendiente previamente. Si existe un comprobante anterior, solo se permite un nuevo registro cuando el estado del pago anterior sea `RECHAZADO`. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Referencia del comprobante | Referencia textual del comprobante de pago | String | No puede estar vacía y no debe superar 500 caracteres | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Pago registrado | Información del pago creado | Objeto Pago | Incluye identificador, equipo asociado, comprobante, fecha de registro y estado | Sí |
| Estado del pago | Estado inicial del registro de pago | Enum / String | Valor inicial: `PENDIENTE` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al módulo de pagos de su equipo | |
| 2 | Capitán | Ingresa la referencia del comprobante de pago | |
| 3 | Capitán | Envía la solicitud de registro del comprobante | |
| 4 | Sistema | Valida que el usuario esté autenticado, sea capitán y esté asociado al equipo correspondiente | Usuario no autorizado |
| 5 | Sistema | Verifica que el equipo exista y valida el estado del pago anterior, si existe | Equipo no encontrado o estado no permitido |
| 6 | Sistema | Valida que la referencia del comprobante sea correcta | Comprobante inválido |
| 7 | Sistema | Registra el pago con fecha de subida y estado inicial `PENDIENTE` | Error al guardar |
| 8 | Sistema | Retorna la información del pago registrado | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no está autenticado, no es capitán o no está asociado al equipo, rechaza la operación | Acceso no autorizado |
| 5 | Sistema | Si el equipo no existe, no se registra el comprobante | Equipo no encontrado |
| 5 | Sistema | Si el equipo ya tiene un pago en estado `PENDIENTE` o `APROBADO`, no se permite registrar un nuevo comprobante | Restricción de negocio |
| 5 | Sistema | Si el último pago registrado tiene estado `RECHAZADO`, se permite un nuevo registro | |
| 6 | Sistema | Si la referencia del comprobante es inválida o está vacía, no se procesa el registro | Comprobante inválido |
| 7 | Sistema | Si ocurre un error interno durante el registro, el pago no debe quedar parcialmente almacenado | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, el sistema almacena el comprobante como una referencia textual. No se realiza carga de archivos binarios ni almacenamiento directo de imágenes o documentos en esta operación. |

**Diagrama casos de uso**

![RF_18_Subir_comprobante_pago.drawio.png](../uml/CasosSeUso/RF_18_Subir_comprobante_pago.drawio.png)

**Prototipo**

![RF_18_SubirComprobanteDePago.PNG](../Images/Mock/RF_18_SubirComprobanteDePago.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo el capitán del equipo puede registrar el comprobante de pago. |
| RN_02 | El comprobante de pago se registra inicialmente con estado PENDIENTE. |
| RN_03 | No se permite registrar un nuevo comprobante para un equipo que ya tenga un pago en estado `PENDIENTE` o `APROBADO`. |
| RN_04 | Solo se permite registrar un nuevo comprobante si el último pago del equipo tiene estado `RECHAZADO`. |
| RN_05 | El comprobante debe registrarse como una referencia textual válida. |
| RN_06 | El sistema no debe dejar pagos incompletos o inconsistentes si ocurre un error durante el registro. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente el registro del comprobante de pago, la reutilización condicionada tras rechazo y el estado inicial pendiente. |
---

## RF_19 — Consultar pagos pendientes

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_19 |
| **Nombre:** | Consultar pagos pendientes |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador consulte la lista de pagos registrados que se encuentran pendientes de verificación. |
| **Cómo se ejecutará:** | El organizador accede al módulo de pagos y solicita la consulta de pagos pendientes. El sistema retorna la lista de pagos que requieren revisión. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado y tener rol de organizador. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Solicitud de consulta | Acción de consulta de pagos pendientes | Acción del sistema | Se ejecuta mediante interacción del organizador | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de pagos | Pagos que se encuentran en estado pendiente de revisión | Lista de Objeto Pago | Incluye equipo, comprobante, fecha y estado | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al módulo de pagos | |
| 2 | Organizador | Solicita la consulta de pagos pendientes | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga rol de organizador | Usuario no autorizado |
| 4 | Sistema | Recupera los pagos en estado PENDIENTE o EN_REVISION | |
| 5 | Sistema | Retorna la lista de pagos encontrados | |
| 6 | Organizador | Revisa los pagos para su verificación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado o no tiene rol de organizador, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si no existen pagos pendientes, retorna una lista vacía | Sin resultados |

| | |
|---|---|
| **Notas y comentarios:** | La consulta incluye pagos en estados PENDIENTE y EN_REVISION, los cuales requieren acción por parte del organizador. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un organizador puede consultar pagos pendientes. |
| RN_02 | Se deben listar únicamente pagos en estado PENDIENTE o EN_REVISION. |
| RN_03 | Si no existen pagos pendientes, el sistema debe retornar una lista vacía. |

---

## RF_20 — Aprobar pago

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_20 |
| **Nombre:** | Aprobar pago |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador apruebe un pago registrado, validando el comprobante y actualizando su estado dentro del sistema. |
| **Cómo se ejecutará:** | El organizador selecciona un pago pendiente y ejecuta la acción de aprobación. El sistema procesa la solicitud y actualiza el estado del pago. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado, tener rol de organizador y el pago debe existir en un estado válido para aprobación. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Identificador de pago | Pago que será aprobado | String | Debe existir en el sistema | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Pago actualizado | Información del pago con estado actualizado | Objeto Pago | Estado final: APROBADO | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Selecciona el pago a aprobar | |
| 2 | Organizador | Ejecuta la acción de aprobación | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga rol de organizador | Usuario no autorizado |
| 4 | Sistema | Recupera el pago seleccionado | Pago no encontrado |
| 5 | Sistema | Verifica que el estado del pago permita la aprobación | Estado inválido |
| 6 | Sistema | Actualiza el estado del pago a APROBADO | Error al actualizar |
| 7 | Sistema | Retorna el pago actualizado | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autorizado, rechaza la operación | Acceso no autorizado |
| 5 | Sistema | Si el pago no está en estado válido, no se permite la aprobación | Transición inválida |
| 6 | Sistema | Si ocurre un error interno, no se actualiza el estado del pago | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la aprobación puede implicar una transición interna desde PENDIENTE a EN_REVISION antes de llegar a APROBADO. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un organizador puede aprobar pagos. |
| RN_02 | Un pago solo puede ser aprobado si se encuentra en un estado válido (PENDIENTE o EN_REVISION). |
| RN_03 | La aprobación actualiza el estado final del pago a APROBADO. |

---

## RF_21 — Rechazar pago

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_21 |
| **Nombre:** | Rechazar pago |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador rechace un pago registrado cuando el comprobante no cumple con los criterios de validación. |
| **Cómo se ejecutará:** | El organizador selecciona un pago en revisión y ejecuta la acción de rechazo. El sistema procesa la solicitud y actualiza el estado del pago. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado, tener rol de organizador y el pago debe encontrarse en un estado válido para rechazo. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Identificador de pago | Pago que será rechazado | String | Debe existir en el sistema | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Pago actualizado | Información del pago con estado actualizado | Objeto Pago | Estado final: RECHAZADO | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Selecciona el pago a rechazar | |
| 2 | Organizador | Ejecuta la acción de rechazo | |
| 3 | Sistema | Valida que el usuario esté autenticado y tenga rol de organizador | Usuario no autorizado |
| 4 | Sistema | Recupera el pago seleccionado | Pago no encontrado |
| 5 | Sistema | Verifica que el estado del pago sea EN_REVISION | Estado inválido |
| 6 | Sistema | Actualiza el estado del pago a RECHAZADO | Error al actualizar |
| 7 | Sistema | Retorna el pago actualizado | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autorizado, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el pago no existe, no se procesa la operación | Pago no encontrado |
| 5 | Sistema | Si el pago no está en estado EN_REVISION, no se permite el rechazo | Transición inválida |
| 6 | Sistema | Si ocurre un error interno, no se actualiza el estado del pago | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | El rechazo de un pago permite que el equipo pueda registrar un nuevo comprobante posteriormente. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo un organizador puede rechazar pagos. |
| RN_02 | Un pago solo puede ser rechazado si se encuentra en estado EN_REVISION. |
| RN_03 | El rechazo actualiza el estado del pago a RECHAZADO. |
| RN_04 | Un pago rechazado habilita el registro de un nuevo comprobante por parte del capitán. |
---

## RF_22 — Configurar torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_22 |
| **Nombre:** | Configurar torneo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el organizador actualice la configuración de su torneo actual, definiendo información complementaria necesaria para su desarrollo, como reglamento, cierre de inscripciones, horarios, canchas y sanciones. |
| **Cómo se ejecutará:** | El organizador accede al módulo de configuración del torneo, modifica uno o varios campos disponibles y envía la actualización. El sistema valida la información y guarda únicamente los cambios permitidos. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El usuario debe estar autenticado, existir como organizador en el sistema y tener un torneo actual asociado. El torneo no debe encontrarse en estado EN_CURSO ni FINALIZADO. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Reglamento | Reglas generales del torneo | String | Campo opcional en la actualización parcial | No |
| Cierre de inscripciones | Fecha límite para inscripción de equipos | LocalDateTime | Si se envía, debe ser anterior a la fecha de inicio del torneo | No |
| Horarios de partidos | Horarios disponibles para la programación de partidos | String | Campo opcional en la actualización parcial | No |
| Canchas | Canchas disponibles para la realización de partidos | String | Campo opcional en la actualización parcial | No |
| Sanciones | Definición de sanciones aplicables dentro del torneo | String | Campo opcional en la actualización parcial | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Torneo actualizado | Información del torneo con la configuración actualizada | Objeto Torneo | Incluye los cambios aplicados | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede al módulo de configuración de su torneo actual | |
| 2 | Organizador | Modifica uno o varios campos de configuración | |
| 3 | Organizador | Envía la solicitud de actualización | |
| 4 | Sistema | Valida que el usuario esté autenticado, sea organizador y tenga un torneo actual asociado | Usuario no autorizado o sin torneo |
| 5 | Sistema | Verifica que el torneo se encuentre en un estado que permita configuración | Estado inválido |
| 6 | Sistema | Valida el cierre de inscripciones cuando este sea enviado | Fecha inválida |
| 7 | Sistema | Actualiza únicamente los campos enviados con contenido válido | Error al guardar |
| 8 | Sistema | Retorna la información del torneo actualizada | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no está autenticado, no es organizador o no tiene torneo actual, rechaza la operación | Acceso no autorizado |
| 5 | Sistema | Si el torneo está en estado EN_CURSO o FINALIZADO, no se permite modificar la configuración | Restricción de negocio |
| 6 | Sistema | Si el cierre de inscripciones es posterior o igual a la fecha de inicio del torneo, no se permite guardar el cambio | Fecha inválida |
| 7 | Sistema | Si ocurre un error interno durante la actualización, no se deben guardar cambios parciales inconsistentes | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | La configuración del torneo se realiza de forma parcial, por lo que solo se actualizan los campos enviados con contenido válido. En la implementación actual no se gestionan campos adicionales como “fechas importantes” fuera de los definidos en esta operación. |

**Diagrama casos de uso**

![HU-08_Configurar_torneo.drawio.png](../uml/CasosSeUso/HU-08_Configurar_torneo.drawio.png)

**Prototipo**

![RF_22_ConfigurarTorneo.PNG](../Images/Mock/RF_22_ConfigurarTorneo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El cierre de inscripciones debe ser anterior a la fecha inicial del torneo. |
| RN_02 | No se puede configurar un torneo que se encuentre en estado EN_CURSO o FINALIZADO. |
| RN_03 | La configuración del torneo se actualiza de manera parcial, modificando únicamente los campos enviados. |
| RN_04 | Solo un organizador puede configurar el torneo asociado a su cuenta. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la configuración parcial del torneo, sus restricciones por estado y validaciones funcionales. |
---

## RF_23 — Definir titulares del equipo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_23 |
| **Nombre:** | Definir titulares del equipo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el capitán defina la lista de jugadores titulares de su equipo para la preparación de la alineación previa a un partido. |
| **Cómo se ejecutará:** | El capitán accede al módulo de alineación de su equipo, selecciona los jugadores titulares y envía la información. El sistema valida que la selección cumpla con las reglas establecidas y procesa la solicitud. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El usuario debe estar autenticado, existir como capitán en el sistema y tener un equipo asociado. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Titulares | Lista de jugadores titulares seleccionados para el equipo | Lista de Jugadores | Debe contener mínimo 5 y máximo 7 jugadores | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Confirmación de titulares | Confirmación del procesamiento de la solicitud | String | Mensaje de confirmación de la operación | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Accede al módulo de alineación de su equipo | |
| 2 | Capitán | Selecciona entre 5 y 7 jugadores titulares de su equipo | |
| 3 | Capitán | Envía la solicitud de definición de titulares | |
| 4 | Sistema | Valida que el usuario esté autenticado, sea capitán y tenga un equipo asociado | Usuario no autorizado |
| 5 | Sistema | Verifica que la lista enviada contenga entre 5 y 7 jugadores | Cantidad inválida |
| 6 | Sistema | Verifica que todos los jugadores seleccionados pertenezcan al equipo del capitán | Jugador no pertenece al equipo |
| 7 | Sistema | Procesa la solicitud de titulares | Error al procesar |
| 8 | Sistema | Retorna la confirmación de la operación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 4 | Sistema | Si el usuario no está autenticado, no es capitán o no tiene equipo asociado, rechaza la operación | Acceso no autorizado |
| 5 | Sistema | Si se envían menos de 5 o más de 7 jugadores titulares, no se procesa la solicitud | Restricción de negocio |
| 6 | Sistema | Si uno o más jugadores no pertenecen al equipo del capitán, no se procesa la solicitud | Restricción de negocio |
| 7 | Sistema | Si ocurre un error interno durante el procesamiento, no se debe guardar información inconsistente | Error interno |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la operación valida únicamente la cantidad permitida de jugadores titulares pertenecientes al equipo del capitán. No se gestionan reservas, formación táctica ni posiciones específicas en cancha dentro de esta funcionalidad. |

**Diagrama casos de uso**

![HU-18_Definir_alineacion.drawio.png](../uml/CasosSeUso/HU-18_Definir_alineacion.drawio.png)

**Prototipo**

![RF_23_DefinirTitularesEquipo.PNG](../Images/Mock/RF_23_DefinirTitularesEquipo.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | La lista de titulares debe incluir mínimo 5 y máximo 7 jugadores. |
| RN_02 | Todos los jugadores titulares deben pertenecer al equipo del capitán. |
| RN_03 | Solo el capitán del equipo puede definir los titulares. |
| RN_04 | En esta versión del sistema no se gestionan reservas, formación táctica ni posiciones específicas de juego. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la selección de entre 5 y 7 titulares pertenecientes al equipo del capitán. |
---

## RF_24 — Consultar alineación

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_24 |
| **Nombre:** | Consultar alineación |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que los usuarios autenticados consulten una alineación previamente registrada en el sistema, con el fin de visualizar los jugadores titulares definidos para un equipo. |
| **Cómo se ejecutará:** | El usuario accede al módulo de alineaciones y solicita la consulta de una alineación específica o del listado general de alineaciones registradas. El sistema procesa la solicitud y retorna la información disponible. |
| **Actor principal:** | Capitán, Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El usuario debe estar autenticado. Para la consulta individual, la alineación debe existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID de alineación | Identificador de la alineación a consultar | String | Requerido para la consulta individual | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Información de alineación | Información registrada de la alineación consultada | Objeto Alineación | Incluye identificador de alineación, identificador de equipo, identificador de partido (si aplica) y lista de titulares | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al módulo de alineaciones | |
| 2 | Usuario | Solicita la consulta de una alineación específica o del listado general | |
| 3 | Sistema | Valida que el usuario esté autenticado | Usuario no autorizado |
| 4 | Sistema | Recupera la alineación solicitada o la colección de alineaciones registradas | Alineación no encontrada |
| 5 | Sistema | Retorna la información de la alineación o el listado disponible | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el usuario no está autenticado, rechaza la consulta | Acceso no autorizado |
| 4 | Sistema | Si la alineación consultada no existe, retorna error | Alineación no encontrada |
| 5 | Sistema | Si se solicita el listado general y no existen alineaciones registradas, retorna una lista vacía | Sin resultados |

| | |
|---|---|
| **Notas y comentarios:** | En la implementación actual, la consulta de alineaciones no filtra automáticamente la alineación rival por partido o por equipo. La información disponible corresponde únicamente a las alineaciones registradas en el sistema. |

**Diagrama casos de uso**

![HU-19_Consultar_alineacion_rival.drawio.png](../uml/CasosSeUso/HU-19_Consultar_alineacion_rival.drawio.png)

**Prototipo**

![RF_24_ConsultarAlineacion.PNG](../Images/Mock/RF_24_ConsultarAlineacion.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | La alineación solo puede ser consultada si ya fue registrada previamente en el sistema. |
| RN_02 | Solo los usuarios autenticados pueden consultar alineaciones. |
| RN_03 | En esta versión del sistema, la consulta no identifica automáticamente si la alineación corresponde a un equipo rival. |
| RN_04 | Si no existen alineaciones registradas, el sistema debe retornar una lista vacía. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la consulta de alineaciones registradas sin identificación automática de alineación rival. |
---

## RF_25 — Gestionar partido

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_25 |
| **Nombre:** | Gestionar partido |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir la gestión integral de un partido dentro del torneo, cubriendo su programación por parte del organizador y su operación en tiempo de juego por parte del árbitro. Esta funcionalidad incluye la creación del encuentro, la consulta de partidos asignados al árbitro, el inicio del partido, el registro del marcador, el registro de eventos relevantes como goles y sanciones, y la finalización del encuentro. |
| **Cómo se ejecutará:** | El organizador programa el partido registrando la información básica del encuentro. Posteriormente, el árbitro consulta los partidos que tiene asignados y, sobre cada partido, puede iniciar el juego, registrar el desarrollo del encuentro y finalizarlo. El sistema valida el estado del partido y las reglas de negocio en cada etapa del proceso. |
| **Actor principal:** | Organizador, Árbitro |
| **Precondiciones:** | El usuario debe estar autenticado y contar con el rol correspondiente según la acción a ejecutar. Para programar el partido deben existir el torneo y los equipos participantes. Para iniciar, registrar información o finalizar el partido, este debe existir y estar asignado al árbitro cuando corresponda. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo al que pertenece el partido | String | Obligatorio para la programación del partido | Sí |
| ID equipo local | Identificador del equipo local | String | No puede ser igual al equipo visitante | Sí |
| ID equipo visitante | Identificador del equipo visitante | String | No puede ser igual al equipo local | Sí |
| Fecha del partido | Fecha programada del encuentro | LocalDateTime | Obligatoria para programar el partido | Sí |
| Cancha | Lugar asignado para el partido | String | Obligatoria para programar el partido | Sí |
| Acción de gestión | Acción a ejecutar sobre el partido | Enum / Acción | Puede corresponder a consultar, iniciar, registrar marcador, registrar eventos o finalizar | Sí |
| Marcador equipo local | Cantidad de goles del equipo local | Int | Debe ser mayor o igual a 0 | Condicional |
| Marcador equipo visitante | Cantidad de goles del equipo visitante | Int | Debe ser mayor o igual a 0 | Condicional |
| Goleadores | Registro de goles realizados durante el partido | Lista / Datos compuestos | Incluye jugador y minuto del gol | No |
| Sanciones | Registro de sanciones aplicadas durante el partido | Lista / Datos compuestos | Incluye jugador, tipo de sanción y descripción | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Partido gestionado | Información del partido creado, consultado o actualizado | Objeto Partido / Lista de Partidos | Incluye estado, equipos, fecha, cancha, marcador y eventos registrados según la operación ejecutada | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Programa el partido registrando torneo, equipos, fecha y cancha | Torneo o equipos no encontrados |
| 2 | Sistema | Valida los datos de programación y crea el partido con estado PROGRAMADO | Equipos iguales o datos inválidos |
| 3 | Árbitro | Accede a su panel y consulta los partidos que tiene asignados | Sin partidos asignados |
| 4 | Árbitro | Selecciona un partido y ejecuta la acción correspondiente: iniciar, registrar marcador, registrar eventos o finalizar | Partido no encontrado |
| 5 | Sistema | Valida que el árbitro tenga acceso al partido y que la acción sea válida según el estado actual | Acción inválida o estado incorrecto |
| 6 | Sistema | Procesa la operación solicitada y actualiza la información del partido | Error al guardar |
| 7 | Sistema | Retorna la información actualizada del partido o la lista de partidos asignados | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si los equipos son iguales o los datos de programación son inválidos, no crea el partido | Restricción de negocio |
| 3 | Sistema | Si el árbitro no tiene partidos asignados, retorna una lista vacía o mensaje informativo | Sin resultados |
| 5 | Sistema | Si se intenta registrar marcador, goles o sanciones sobre un partido que no está en EN_CURSO, no se permite la operación | Estado inválido |
| 5 | Sistema | Si se intenta iniciar o finalizar un partido en un estado no permitido, no se ejecuta la acción | Restricción de estado |
| 6 | Sistema | Si el marcador contiene valores negativos o la información registrada es inconsistente, no se guarda el cambio | Datos inválidos |

| | |
|---|---|
| **Notas y comentarios:** | Esta funcionalidad agrupa el ciclo operativo completo del partido. En la implementación actual, el organizador se encarga de la programación del encuentro, mientras que el árbitro gestiona su desarrollo y cierre. La consulta de partidos del árbitro solo muestra los encuentros que le han sido asignados. |

**Diagrama casos de uso**

![HU-20_Registrar_resultado_partido.drawio.png](../uml/CasosSeUso/HU-20_Registrar_resultado_partido.drawio.png)

**Prototipo**

![RF_17_RegistrarPartidos.PNG](../Images/Mock/RF_17_RegistrarPartidos.PNG)

![RF_18_ConsultarPartido(Arbitro).PNG](../Images/Mock/RF_18_ConsultarPartido%28Arbitro%29.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El equipo local y el equipo visitante no pueden ser el mismo. |
| RN_02 | Solo el organizador puede programar partidos. |
| RN_03 | Solo el árbitro asignado puede consultar y gestionar operativamente el partido. |
| RN_04 | El partido se crea inicialmente con estado PROGRAMADO. |
| RN_05 | El marcador no puede contener valores negativos. |
| RN_06 | El resultado, los goles y las sanciones solo pueden registrarse cuando el partido está en estado EN_CURSO. |
| RN_07 | Un partido solo puede finalizarse si previamente ha sido iniciado. |
| RN_08 | El árbitro solo puede visualizar los partidos que le han sido asignados. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Consolidación del ciclo funcional completo de gestión de partidos, incluyendo programación, consulta arbitral, operación en curso y finalización. |
---

## RF_26 — Consultar partidos asignados (árbitro)

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_26 |
| **Nombre:** | Consultar partidos asignados (árbitro) |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el árbitro consulte los partidos que tiene asignados, visualizando la información necesaria para su gestión durante el torneo. |
| **Cómo se ejecutará:** | El árbitro accede a su panel de partidos asignados dentro de la plataforma, donde el sistema muestra la lista de encuentros asociados a su cuenta. |
| **Actor principal:** | Árbitro |
| **Precondiciones:** | El usuario debe estar autenticado y registrado con rol de árbitro en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Solicitud de consulta | Acción de consultar los partidos asignados | Acción del sistema | No requiere parámetros manuales | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Lista de partidos asignados | Conjunto de partidos asociados al árbitro | Lista de Objeto Partido | Incluye ID del partido, fecha, hora, cancha, estado, equipo local y equipo visitante | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Árbitro | Accede a su panel de partidos asignados | |
| 2 | Sistema | Valida que el usuario esté autenticado y tenga rol de árbitro | Usuario no autorizado |
| 3 | Sistema | Recupera los partidos asignados al árbitro | |
| 4 | Sistema | Muestra la lista de partidos con su información básica | |
| 5 | Árbitro | Consulta los partidos disponibles para su gestión | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el usuario no está autenticado o no es árbitro, rechaza la consulta | Acceso no autorizado |
| 3 | Sistema | Si no existen partidos asignados, retorna una lista vacía | Sin resultados |
| 4 | Sistema | Si no hay partidos asignados, muestra mensaje informativo | |

| | |
|---|---|
| **Notas y comentarios:** | El sistema retorna únicamente los partidos asignados al árbitro autenticado. Si no existen asignaciones, la respuesta corresponde a una lista vacía. |

**Diagrama casos de uso**

![HU-20_Registrar_resultado_partido.drawio.png](../uml/CasosSeUso/HU-20_Registrar_resultado_partido.drawio.png)

**Prototipo**

![RF_18_ConsultarPartido(Arbitro).PNG](../Images/Mock/RF_18_ConsultarPartido%28Arbitro%29.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El árbitro solo puede visualizar los partidos que le han sido asignados. |
| RN_02 | Solo usuarios autenticados con rol de árbitro pueden acceder a esta funcionalidad. |
| RN_03 | Si no existen partidos asignados, el sistema debe retornar una lista vacía o mostrar un mensaje informativo. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la consulta de partidos asignados al árbitro y los datos visibles en la plataforma. |
---

## RF_27 — Tabla de posiciones

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_27 |
| **Nombre:** | Tabla de posiciones |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir a los usuarios autenticados consultar la tabla de posiciones de un torneo, calculada automáticamente a partir de los partidos finalizados registrados en el sistema. |
| **Cómo se ejecutará:** | El usuario accede a la sección de tabla de posiciones del torneo y el sistema calcula y muestra la clasificación actual con base en los resultados registrados. |
| **Actor principal:** | Usuario autenticado |
| **Precondiciones:** | El usuario debe estar autenticado y el torneo debe existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo a consultar | String | Obligatorio para calcular la tabla de posiciones | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Tabla de posiciones | Clasificación de equipos del torneo | Lista de registros de posición | Incluye equipo, partidos jugados, ganados, empatados, perdidos, goles a favor, goles en contra y puntos | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede a la sección de tabla de posiciones del torneo | |
| 2 | Sistema | Valida que el usuario esté autenticado y que el torneo exista | Usuario no autorizado o torneo no encontrado |
| 3 | Sistema | Recupera los partidos finalizados asociados al torneo | |
| 4 | Sistema | Calcula las estadísticas acumuladas de cada equipo | |
| 5 | Sistema | Ordena la tabla de posiciones según los puntos obtenidos | |
| 6 | Sistema | Muestra la tabla de posiciones actualizada | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el usuario no está autenticado o el torneo no existe, rechaza la consulta | Acceso no autorizado o torneo inexistente |
| 3 | Sistema | Si no existen partidos finalizados para el torneo, retorna una tabla vacía | Sin resultados |
| 6 | Sistema | Si no hay datos suficientes, muestra una tabla vacía sin afectar el sistema | |

| | |
|---|---|
| **Notas y comentarios:** | La tabla de posiciones se genera dinámicamente al momento de la consulta, a partir de los partidos finalizados registrados en el torneo. En la implementación actual, la diferencia de gol puede inferirse a partir de los goles a favor y en contra, aunque no se retorne explícitamente como campo independiente. |

**Diagrama casos de uso**

![HU-22_Tabla_posiciones.drawio.png](../uml/CasosSeUso/HU-22_Tabla_posiciones.drawio.png)

**Prototipo**

![RF_19_TablaDePosiciones.PNG](../Images/Mock/RF_19_TablaDePosiciones.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Una victoria otorga 3 puntos, un empate otorga 1 punto y una derrota otorga 0 puntos. |
| RN_02 | La tabla de posiciones se ordena por puntos en orden descendente. |
| RN_03 | Solo se tienen en cuenta los partidos finalizados para el cálculo de la tabla. |
| RN_04 | Si no existen partidos finalizados, la tabla debe mostrarse vacía. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente el cálculo dinámico de la tabla de posiciones con base en partidos finalizados. |
---

## RF_28 — Consultar bracket eliminatorio

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_28 |
| **Nombre:** | Consultar bracket eliminatorio |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir a los usuarios autenticados consultar los partidos correspondientes a la fase eliminatoria de un torneo, con el fin de representarlos visualmente en formato de llaves o bracket. |
| **Cómo se ejecutará:** | El usuario accede a la sección de llaves eliminatorias del torneo y el sistema recupera los partidos correspondientes a esta fase para construir la representación visual. |
| **Actor principal:** | Usuario autenticado |
| **Precondiciones:** | El usuario debe estar autenticado y el torneo debe existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo a consultar | String | Obligatorio para consultar el bracket eliminatorio | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Bracket eliminatorio | Información de los partidos de la fase eliminatoria | Lista de Objeto Partido | Incluye ID del partido, equipo local, equipo visitante, marcador, estado y fecha | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede a la sección de llaves eliminatorias del torneo | |
| 2 | Sistema | Valida que el usuario esté autenticado y que el torneo exista | Usuario no autorizado o torneo no encontrado |
| 3 | Sistema | Recupera los partidos correspondientes a la fase eliminatoria del torneo | |
| 4 | Sistema | Construye la respuesta para la representación visual del bracket | |
| 5 | Sistema | Retorna la información de los encuentros eliminatorios | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el usuario no está autenticado o el torneo no existe, rechaza la consulta | Acceso no autorizado o torneo inexistente |
| 3 | Sistema | Si no existen partidos en fase eliminatoria, retorna una lista vacía | Sin resultados |
| 5 | Sistema | Si alguno de los equipos aún no está definido, la interfaz puede representarlo como pendiente (`TBD`) | Información incompleta |

| | |
|---|---|
| **Notas y comentarios:** | El backend retorna únicamente los partidos correspondientes a la fase eliminatoria del torneo. No genera automáticamente la estructura de rondas (cuartos, semifinales, final), por lo que la organización visual del bracket es responsabilidad del frontend. |

**Diagrama casos de uso**

![HU-23_Llaves_eliminatorias.drawio.png](../uml/CasosSeUso/HU-23_Llaves_eliminatorias.drawio.png)

**Prototipo**

![RF_20_LlavesEliminatorias.PNG](../Images/Mock/RF_20_LlavesEliminatorias.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | El bracket se construye únicamente con los partidos de la fase eliminatoria del torneo. |
| RN_02 | Solo usuarios autenticados pueden consultar el bracket eliminatorio. |
| RN_03 | Si no existen partidos eliminatorios, el sistema debe retornar una lista vacía. |
| RN_04 | Si un partido no tiene equipos completamente definidos, puede representarse como pendiente (`TBD`). |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la consulta de partidos de fase eliminatoria y su representación en bracket. |
---

## RF_29 — Estadísticas generales del torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_29 |
| **Nombre:** | Estadísticas generales del torneo |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir a los usuarios autenticados consultar un resumen estadístico general del torneo, calculado a partir de los partidos registrados y sus resultados. |
| **Cómo se ejecutará:** | El usuario accede a la sección de estadísticas del torneo y el sistema calcula y muestra los indicadores generales disponibles. |
| **Actor principal:** | Usuario autenticado |
| **Precondiciones:** | El usuario debe estar autenticado y el torneo debe existir en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo a consultar | String | Obligatorio para calcular las estadísticas | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Resumen estadístico del torneo | Conjunto de estadísticas generales calculadas | Objeto Estadísticas | Incluye total de partidos, partidos finalizados, en curso, programados, total de goles y promedio de goles por partido | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede a la sección de estadísticas del torneo | |
| 2 | Sistema | Valida que el usuario esté autenticado y que el torneo exista | Usuario no autorizado o torneo no encontrado |
| 3 | Sistema | Recupera los partidos registrados del torneo | |
| 4 | Sistema | Calcula las estadísticas generales del torneo | |
| 5 | Sistema | Retorna el resumen estadístico calculado | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 2 | Sistema | Si el usuario no está autenticado o el torneo no existe, rechaza la consulta | Acceso no autorizado o torneo inexistente |
| 3 | Sistema | Si no existen partidos registrados, retorna estadísticas con valores en 0 | Sin datos disponibles |
| 5 | Sistema | Si no hay partidos finalizados, el promedio de goles por partido se retorna como 0 | |

| | |
|---|---|
| **Notas y comentarios:** | Las estadísticas se calculan dinámicamente al momento de la consulta. El promedio de goles por partido se calcula únicamente con base en los partidos finalizados. |

**Diagrama casos de uso**

![HU-24_Estadisticas.drawio.png](../uml/CasosSeUso/HU-24_Estadisticas.drawio.png)

**Prototipo**

![RF_21_Estadisticas.PNG](../Images/Mock/RF_21_Estadisticas.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Las estadísticas se calculan en tiempo real a partir de los partidos registrados del torneo. |
| RN_02 | Solo se tienen en cuenta los partidos del torneo consultado. |
| RN_03 | El promedio de goles por partido se calcula únicamente con partidos finalizados. |
| RN_04 | Si no existen partidos registrados, las estadísticas deben retornar valores en 0. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente el cálculo dinámico de estadísticas generales del torneo. |
---

## RF_30 — Registrar organizadores y árbitros

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_30 |
| **Nombre:** | Registrar organizadores y árbitros |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir que el administrador registre manualmente nuevos usuarios con rol de organizador o árbitro dentro de la plataforma. |
| **Cómo se ejecutará:** | El administrador accede al módulo de gestión de usuarios y registra la información del nuevo usuario mediante una sesión válida en el sistema. |
| **Actor principal:** | Administrador |
| **Precondiciones:** | El administrador debe estar autenticado y contar con una sesión válida. El correo del usuario a registrar no debe existir previamente en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Nombre completo | Nombre del usuario a registrar | String | No puede estar vacío | Sí |
| Correo electrónico | Correo del organizador o árbitro | String | No debe estar registrado previamente | Sí |
| Tipo de usuario base | Tipo de usuario asociado al nuevo registro | Enum | Debe corresponder al tipo de cuenta permitido por el sistema | Sí |
| Rol asignado | Rol funcional del nuevo usuario | Enum | Solo se permiten ORGANIZADOR o ARBITRO | Sí |
| Contraseña temporal | Contraseña inicial de acceso | String | Debe cumplir los requisitos mínimos de seguridad | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Confirmación de registro | El sistema confirma la creación del usuario con el rol asignado | String | Mensaje: `Usuario registrado correctamente.` | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Administrador | Accede al módulo de gestión de usuarios | |
| 2 | Administrador | Registra el nombre, correo, tipo de usuario base, rol y contraseña temporal del nuevo usuario | |
| 3 | Sistema | Valida la sesión del administrador y la información ingresada | Sesión inválida o datos inválidos |
| 4 | Sistema | Verifica que el correo no exista previamente y que el rol asignado sea permitido | Correo duplicado o rol no permitido |
| 5 | Sistema | Crea la cuenta del usuario con el rol correspondiente | |
| 6 | Sistema | Retorna la confirmación de registro exitoso | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si la sesión del administrador no es válida, rechaza la operación | Acceso no autorizado |
| 4 | Sistema | Si el correo ya existe, el rol no es permitido o los datos son inválidos, no registra la cuenta | Validación fallida |
| 6 | Sistema | Si ocurre un error durante el registro, informa al administrador sin crear la cuenta | Error de procesamiento |

| | |
|---|---|
| **Notas y comentarios:** | El registro administrativo está restringido a usuarios con rol de organizador o árbitro. La creación de estos usuarios puede generar eventos de auditoría internos en el sistema. |

**Diagrama casos de uso**

![HU-25 — Registrar organizadores y árbitros.png](../uml/CasosSeUso/HU-25%20%E2%80%94%20Registrar%20organizadores%20y%20%C3%A1rbitros.png)

**Prototipo**

![RF_22_RegistrarORganizadoresYarbitros.PNG](../Images/Mock/RF_22_RegistrarORganizadoresYarbitros.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo el administrador puede registrar usuarios con rol de organizador o árbitro. |
| RN_02 | No se permiten cuentas duplicadas con el mismo correo electrónico. |
| RN_03 | La contraseña temporal debe tener mínimo 8 caracteres. |
| RN_04 | Solo se permiten los roles ORGANIZADOR y ARBITRO. |
| RN_05 | El administrador debe contar con una sesión válida para ejecutar esta funcionalidad. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente el registro administrativo de organizadores y árbitros, sus validaciones y restricciones de acceso. |
---

## RF_31 — Consultar auditoría del sistema

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_31 |
| **Nombre:** | Consultar auditoría del sistema |

| | |
|---|---|
| **Descripción:** | El sistema debe permitir al administrador consultar el historial de acciones administrativas registradas, con el fin de auditar las operaciones realizadas en la plataforma. |
| **Cómo se ejecutará:** | El administrador accede al módulo de auditoría y consulta los registros mediante una sesión válida, pudiendo aplicar filtros opcionales. |
| **Actor principal:** | Administrador |
| **Precondiciones:** | El administrador debe estar autenticado y contar con una sesión válida en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Filtro por usuario | Filtrar registros por usuario específico | String | Coincidencia parcial opcional | No |
| Tipo de acción | Filtrar registros por tipo de acción | Enum | Valores actuales: LOGIN_ADMIN, REGISTRO_ORGANIZADOR, REGISTRO_ARBITRO | No |
| Rango de fechas | Filtrar registros por periodo de tiempo | LocalDate | Se envía como fechaDesde y fechaHasta | No |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Historial de auditoría | Lista de eventos registrados en el sistema | Lista de registros de auditoría | Incluye fecha y hora, descripción de la acción, usuario asociado y tipo de acción | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Administrador | Accede al módulo de auditoría | |
| 2 | Administrador | Aplica filtros opcionales de usuario, tipo de acción o rango de fechas | |
| 3 | Sistema | Valida la sesión del administrador y la consistencia de los filtros | Sesión inválida o filtros inválidos |
| 4 | Sistema | Recupera los registros de auditoría que coinciden con los filtros | |
| 5 | Sistema | Ordena los resultados por fecha en orden descendente | |
| 6 | Sistema | Retorna el historial de auditoría | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si la sesión del administrador no es válida, rechaza la consulta | Acceso no autorizado |
| 3 | Sistema | Si el rango de fechas es inconsistente, no ejecuta la consulta | Validación fallida |
| 4 | Sistema | Si no existen registros que coincidan con los filtros, retorna una lista vacía | Sin resultados |

| | |
|---|---|
| **Notas y comentarios:** | Los eventos auditados corresponden actualmente a inicio de sesión del administrador y registro de organizadores y árbitros. El sistema puede extender los tipos de eventos auditados en futuras versiones. |

**Diagrama casos de uso**

![HU-26 Consulta Auditoria.PNG](../uml/CasosSeUso/HU-26%20Consulta%20Auditoria.PNG)

**Prototipo**

![RF_23_ConsultaAuditoria.PNG](../Images/Mock/RF_23_ConsultaAuditoria.PNG)

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| RN_01 | Solo el administrador puede consultar la auditoría del sistema. |
| RN_02 | Los registros se ordenan por fecha en orden descendente. |
| RN_03 | El rango de fechas debe ser consistente (fechaDesde ≤ fechaHasta). |
| RN_04 | Los filtros son opcionales y pueden combinarse entre sí. |

**HISTORIAL DE REVISIÓN**

| Elaborado por | Aprobado por | Fecha | Descripción y Justificación de Cambios |
|--------------|-------------|-------|----------------------------------------|
| Shawarma FC | Nicolas Guillermo Ibañez Leon | 12/03/2026 | Creación inicial del requerimiento. |
| Shawarma FC | Daniel Felipe Rayo Rodriguez | 17/03/2026 | Refinamiento técnico y estructural. |
| Shawarma FC | Juan Manuel Lopez Barrera | 23/03/2026 | Inclusión de prototipo e historia de usuario. |
| Shawarma FC | Jeyder Nicolay Leon Lancheros | 03/04/2026 | Ajuste del requerimiento para reflejar correctamente la consulta de auditoría, filtros disponibles y comportamiento del backend. |

---

## RF_32 — Iniciar sesión

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_32 |
| **Nombre:** | Iniciar sesión |

| | |
|---|---|
| **Descripción:** | El usuario puede autenticarse en la plataforma mediante sus credenciales registradas en el sistema. |
| **Cómo se ejecutará:** | Mediante el envío de correo electrónico y contraseña al servicio de autenticación. |
| **Actor principal:** | Administrador, Organizador, Árbitro, Capitán, Jugador |
| **Precondiciones:** | El usuario debe estar registrado en el sistema y contar con credenciales válidas. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Correo electrónico | Correo del usuario | String | Debe tener formato válido | Sí |
| Contraseña | Clave de acceso del usuario | String | Debe coincidir con la registrada | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Sesión iniciada | El sistema confirma el inicio de sesión y retorna la información de autenticación | Objeto / String | Incluye token o información de sesión | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Accede al formulario de inicio de sesión | |
| 2 | Usuario | Ingresa correo y contraseña | |
| 3 | Sistema | Valida las credenciales enviadas | Credenciales inválidas |
| 4 | Sistema | Retorna confirmación e inicia la sesión del usuario | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si las credenciales no son válidas, responde con error y no inicia sesión | |

| | |
|---|---|
| **Notas y comentarios:** | La autenticación puede apoyarse en JWT o mecanismos equivalentes definidos por la arquitectura del backend. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo los usuarios registrados pueden iniciar sesión. |
| 2 | El sistema debe validar correo y contraseña antes de generar la sesión. |

---

## RF_33 — Autenticación con Google

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_33 |
| **Nombre:** | Autenticación con Google |

| | |
|---|---|
| **Descripción:** | El usuario puede autenticarse en la plataforma mediante una cuenta de Google autorizada. |
| **Cómo se ejecutará:** | Mediante la opción de inicio de sesión con Google disponible en la plataforma. |
| **Actor principal:** | Estudiante, Graduado, Profesor, Personal Administrativo, Familiares |
| **Precondiciones:** | El usuario debe contar con una cuenta de Google válida y autorizada. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Cuenta Google | Cuenta seleccionada para autenticación | String / OAuth2 | Debe ser válida | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Sesión iniciada | Confirmación de autenticación con proveedor externo | Objeto / String | Incluye token o sesión autorizada | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Selecciona la opción de inicio de sesión con Google | |
| 2 | Sistema | Redirige al proveedor de autenticación | Error de autenticación |
| 3 | Usuario | Autoriza el acceso con su cuenta Google | |
| 4 | Sistema | Valida la autenticación y crea la sesión correspondiente | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si la autenticación externa falla, responde con error y no inicia sesión | |

| | |
|---|---|
| **Notas y comentarios:** | Esta funcionalidad permite autenticación externa sin requerir ingreso manual de contraseña en la plataforma. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se permite el acceso si la autenticación externa es válida. |

---

## RF_34 — Inscribir equipo al torneo

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_34 |
| **Nombre:** | Inscribir equipo al torneo |

| | |
|---|---|
| **Descripción:** | El capitán registra su equipo como participante de un torneo activo. |
| **Cómo se ejecutará:** | Mediante la operación de inscripción de equipo al torneo. |
| **Actor principal:** | Capitán |
| **Precondiciones:** | El capitán debe existir, tener un equipo válido y el torneo debe encontrarse habilitado para inscripción. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del torneo | Identificador del torneo | String | Debe existir | Sí |
| ID del equipo | Identificador del equipo | String | Debe existir y pertenecer al capitán | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Inscripción confirmada | Confirmación de registro del equipo en el torneo | String / Objeto | Se registra como participante del torneo | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Selecciona el torneo al que desea inscribir su equipo | |
| 2 | Capitán | Envía la solicitud de inscripción | |
| 3 | Sistema | Valida la existencia del torneo y del equipo | Torneo o equipo no encontrado |
| 4 | Sistema | Registra la inscripción del equipo en el torneo | |
| 5 | Sistema | Retorna la confirmación de inscripción | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el torneo no está disponible o el equipo no cumple condiciones, responde con error | |

| | |
|---|---|
| **Notas y comentarios:** | La inscripción del equipo puede depender del estado del torneo, la validación del equipo y el estado del pago. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo equipos válidos pueden inscribirse al torneo. |
| 2 | El torneo debe encontrarse habilitado para inscripción. |

---

## RF_35 — Asignar árbitro a partido

| | |
|---|---|
| **FUNCIONALIDAD:** | |
| **Código:** | RF_35 |
| **Nombre:** | Asignar árbitro a partido |

| | |
|---|---|
| **Descripción:** | El organizador asigna un árbitro a un partido previamente programado. |
| **Cómo se ejecutará:** | Mediante la operación de asignación de árbitro a partido. |
| **Actor principal:** | Organizador |
| **Precondiciones:** | El partido debe existir y el árbitro debe encontrarse registrado en el sistema. |

**DATOS DE ENTRADA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| ID del partido | Identificador del partido | String | Debe existir | Sí |
| ID del árbitro | Identificador del árbitro | String | Debe existir | Sí |

**DATOS DE SALIDA**

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|--------|------------|---------------|---------------------|-------------|
| Asignación confirmada | Confirmación de asignación del árbitro | String / Objeto | Relaciona árbitro con partido | Sí |

**FLUJO BÁSICO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Selecciona el partido programado | |
| 2 | Organizador | Selecciona el árbitro disponible | |
| 3 | Sistema | Valida la existencia del partido y del árbitro | Partido o árbitro no encontrado |
| 4 | Sistema | Registra la asignación | |
| 5 | Sistema | Retorna la confirmación de asignación | |

**FLUJO ALTERNO:**

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 3 | Sistema | Si el partido o árbitro no existe, responde con error y no realiza la asignación | |

| | |
|---|---|
| **Notas y comentarios:** | Esta funcionalidad permite que posteriormente el árbitro consulte los partidos que le fueron asignados. |

**REGLAS DE NEGOCIO**

| No. | Descripción |
|-----|------------|
| 1 | Solo se puede asignar un árbitro registrado a un partido existente. |

---
# Requerimientos No Funcionales

| Código | Nombre | Descripción |
|--------|--------|-------------|
| RNF_01 | Paleta de colores | La interfaz de usuario debe utilizar principalmente los colores blanco, verde y negro, junto con sus tonalidades, para mantener coherencia visual con la identidad del proyecto. |
| RNF_02 | Diseño responsive | La plataforma debe adaptarse correctamente a dispositivos móviles, tabletas y equipos de escritorio, con soporte desde 320 px hasta 1920 px de ancho de pantalla. |
| RNF_03 | Tiempo de respuesta | Las operaciones y vistas principales del sistema deben responder en un tiempo menor a 3 segundos en condiciones normales de uso. |
| RNF_04 | Cifrado de contraseñas | Las contraseñas de los usuarios deben almacenarse de forma cifrada utilizando bcrypt o un mecanismo equivalente de seguridad. |
| RNF_05 | Restricción de formato de comprobante | El sistema debe permitir registrar comprobantes de pago mediante referencias textuales o enlaces, sin requerir procesamiento obligatorio de archivos binarios. |
| RNF_06 | Disponibilidad del sistema | La plataforma debe mantener una disponibilidad mínima del 95 % durante su operación normal en el periodo del torneo. |
| RNF_07 | Compatibilidad de navegadores | La aplicación debe funcionar correctamente en Chrome, Firefox, Edge y Safari, considerando al menos las dos versiones más recientes de cada navegador. |
| RNF_08 | Control de acceso por roles | El sistema debe restringir el acceso a funcionalidades según el rol del usuario autenticado (Administrador, Organizador, Árbitro, Capitán o Jugador). |
| RNF_09 | Integridad de datos | El sistema debe validar y conservar la consistencia de la información registrada para torneos, equipos, jugadores, partidos, pagos y auditoría. |
| RNF_10 | Autenticación segura | El sistema debe autenticar usuarios internos mediante JWT y usuarios externos mediante OAuth2 con Google o mecanismos equivalentes definidos por la arquitectura. |
| RNF_11 | Vigencia de sesión | Los mecanismos de autenticación del sistema deben manejar expiración controlada de sesión o token para proteger el acceso a la plataforma. |
| RNF_12 | Documentación de API | El backend debe contar con documentación técnica de sus endpoints mediante Swagger/OpenAPI o herramienta equivalente. |
| RNF_13 | Registro de eventos y trazabilidad | El sistema debe generar registros técnicos (logs) para facilitar el monitoreo, depuración y trazabilidad de eventos importantes y errores. |
| RNF_14 | Portabilidad del sistema | La aplicación backend debe poder ejecutarse en entornos estandarizados mediante contenedores Docker o una estrategia equivalente de despliegue. |
| RNF_15 | Integración y despliegue continuo | El proyecto debe contar con un proceso automatizado de integración, pruebas y despliegue para ambientes de desarrollo, pruebas o producción. |
