# TECHCUP FUTBOL

Repositorio dedicado al desarrollo del proyecto **TECHCUP FUTBOL**.

---

## Equipo

**SHAWARMA F.C**

| Integrante |
|---|
| Daniel Felipe Rayo Rodriguez |
| Juan Esteban Hernandez Moreno |
| Juan Manuel Lopez Barrera |
| Nicolas Andres Parrado Gonzalez |
| Nicolas Guillermo Ibanez Leon |
| Jeyder Nicolay Leon Lancheros |

---

## Descripcion del problema

Los programas de Ingenieria de Sistemas, IA, Ciberseguridad y Estadistica realizan cada semestre un torneo interno de futbol en el que participan estudiantes de distintos semestres. Aunque la actividad tiene alta acogida, su organizacion actualmente depende de procesos manuales: mensajes por WhatsApp, formularios aislados y hojas de calculo.

Esta forma de manejo genera desorden, retrasos y confusion entre participantes y organizadores. Muchos estudiantes no conocen el proceso de inscripcion, los equipos se completan tarde, los pagos se verifican manualmente y la informacion del torneo (fechas, reglas, resultados) se encuentra dispersa.

**TECHCUP FUTBOL** propone desarrollar una plataforma web que centralice toda la gestion del torneo, permitiendo que estudiantes, capitanes y organizadores interactuen en un solo sistema organizado y transparente.

### Dificultades actuales

- El proceso de inscripcion no es claro para los participantes.
- Los capitanes tienen problemas para completar sus equipos.
- Los pagos no se verifican de forma rapida y es un proceso manual.
- Los resultados y la tabla de posiciones se actualizan manualmente.
- Las llaves eliminatorias se organizan a mano.
- No existe historial ni estadisticas del torneo.
- La informacion oficial esta dispersa.
- Esto ocasiona retrasos, errores administrativos y conflictos entre los participantes.

---

## Requerimientos

### Funcionales

- Crear torneo
- Gestionar torneo
- Consultar torneo
- Registro de jugadores
- Perfil deportivo
- Disponibilidad del jugador
- Invitaciones a equipo
- Crear equipo
- Gestionar equipo
- Validar equipo
- Buscar jugadores
- Subir comprobante de pago
- Verificar pago
- Configurar torneo
- Gestionar alineacion
- Consultar alineacion rival
- Registrar partido
- Consultar partido
- Tabla de posiciones
- Llave eliminatoria
- Estadisticas
- Registro de arbitros y organizadores
- Consultar auditoria

### No funcionales

- Paleta de colores
- Diseno responsive
- Tiempo de respuesta
- Cifrado de contrasenas
- Limite de archivos
- Disponibilidad del sistema
- Compatibilidad de navegadores

### Analisis de requerimientos

[Ver requerimientos del sistema](docs/requierments/requirements.md)

---

## Enlaces

| Recurso | Enlace |
|---|---|
| Repositorio Frontend | [Ver repositorio](https://github.com/juanhernandez2003/ShawarmaFCTechCupFrontEnd.git) |
| Mockup Figma | [Ver diseno](https://www.figma.com/design/RBlkiZOJCzHNZrpJdHP88Q/TECHCUP?node-id=0-1&p=f&t=hCEw9tLBCOW7NR71-0) |
| Manual de identidad | [Ver manual](https://github.com/nglsz006/SFC_techcup-futbol/blob/develop/SFC_TechUp_Futbol/docs/planing/manual%20identidad.md) |
| Analisis de requerimientos | [Ver documento](docs/requierments/requirements.md) |
| Tablero JIRA | [Ver tablero](https://mail-team-w4y4k0y5.atlassian.net/jira/software/projects/TF/boards/35?atlOrigin=eyJpIjoiY2ZjNWQ5ZGRjMTM3NDAwMGIxNDEwYWUwNjFkNGY1NTMiLCJwIjoiaiJ9) |
| Video mockup | [Ver video](https://www.youtube.com/watch?v=u2pviu8SbS0) |
| Video demo tecnica persistencia | [Ver video](https://www.youtube.com/watch?v=RVWfsO4EjQk) |
| Video demo tecnica seguridad JWT | [Ver video](https://www.youtube.com/watch?v=un_0RBnjYJM) |
| Video demo tecnica seguridad OAuth2 | [Ver video](https://www.youtube.com/watch?v=Jcp5-ZxAYo4) |

---

## Trazabilidad de videos

Esta seccion permite llevar un registro continuo de lo que se viene desarrollando y aplicando en el proyecto mediante videos. Cada registro resume de forma breve que se implemento y que se puede observar en la grabacion, con el fin de dejar evidencia clara del avance funcional y tecnico.

### Videos disponibles

| Tipo | Tema | Que se aplico | Que se ve en el video | Enlace |
|---|---|---|---|---|
| Mockup | Propuesta visual inicial | Se definieron pantallas base, estructura de navegacion y enfoque visual inicial del sistema. | Se observa la propuesta de interfaz, distribucion de pantallas y flujo general esperado para los usuarios. | [Ver video](https://www.youtube.com/watch?v=u2pviu8SbS0) |
| Demo tecnica | Persistencia | Se implemento la persistencia de entidades y operaciones principales del backend. | Se evidencia el almacenamiento, consulta y manejo de informacion dentro del sistema. | [Ver video](https://www.youtube.com/watch?v=RVWfsO4EjQk) |
| Demo tecnica | Seguridad JWT | Se aplico autenticacion y autorizacion mediante tokens JWT para proteger endpoints. | Se observa el inicio de sesion, la generacion del token y el acceso controlado a recursos protegidos. | [Ver video](https://www.youtube.com/watch?v=un_0RBnjYJM) |
| Demo tecnica | Seguridad OAuth2 | Se integro autenticacion externa con Google usando OAuth2. | Se evidencia el flujo de autenticacion con proveedor externo y su integracion con el sistema. | [Ver video](https://www.youtube.com/watch?v=Jcp5-ZxAYo4) |

### Espacios para videos funcionales

| Codigo | Funcionalidad | Descripcion breve | Estado | Enlace del video |
|---|---|---|---|---|
| RF_01 | Crear torneo | Mostrar la creacion del torneo con sus datos principales y estado inicial. | Pendiente de agregar |  |
| RF_02 | Gestionar torneo | Mostrar el inicio y la finalizacion del torneo segun el flujo implementado. | Pendiente de agregar |  |
| RF_03 | Consultar torneo | Mostrar la consulta individual y el listado general de torneos registrados. | Pendiente de agregar |  |
| RF_04 | Registro de jugadores | Mostrar el registro de usuarios y sus validaciones de acceso. | Pendiente de agregar |  |
| RF_05 | Perfil deportivo | Mostrar la creacion, consulta y actualizacion del perfil deportivo del jugador. | Pendiente de agregar |  |
| RF_06 | Disponibilidad del jugador | Mostrar el cambio de disponibilidad del jugador para procesos de invitacion. | Pendiente de agregar |  |
| RF_07 | Invitaciones a equipo | Mostrar la aceptacion o rechazo de invitaciones y su efecto sobre la disponibilidad. | Pendiente de agregar |  |
| RF_08 | Crear equipo | Mostrar la creacion del equipo por parte del capitan. | Pendiente de agregar |  |
| RF_09 | Gestionar equipo | Mostrar el envio de invitaciones a jugadores disponibles desde la cuenta del capitan. | Pendiente de agregar |  |
| RF_10 | Validar equipo | Mostrar la validacion de la cantidad minima y maxima de jugadores del equipo. | Pendiente de agregar |  |
| RF_11 | Buscar jugadores | Mostrar la busqueda de jugadores por posicion. | Pendiente de agregar |  |
| RF_12 | Subir comprobante de pago | Mostrar el registro del comprobante de pago asociado a un equipo. | Pendiente de agregar |  |
| RF_13 | Verificar pago | Mostrar la aprobacion o rechazo del pago por parte del organizador. | Pendiente de agregar |  |
| RF_14 | Configurar torneo | Mostrar la actualizacion de reglamento, horarios, canchas y cierre de inscripciones. | Pendiente de agregar |  |
| RF_15 | Gestionar alineacion | Mostrar el registro de titulares enviados para la alineacion. | Pendiente de agregar |  |
| RF_16 | Consultar alineacion rival | Mostrar la consulta de alineaciones registradas por el sistema. | Pendiente de agregar |  |
| RF_17 | Registrar partido | Mostrar la creacion y gestion operativa del partido por organizador y arbitro. | Pendiente de agregar |  |
| RF_18 | Consultar partido | Mostrar la consulta de partidos asignados al arbitro. | Pendiente de agregar |  |
| RF_19 | Tabla de posiciones | Mostrar el calculo de la tabla con base en partidos finalizados. | Pendiente de agregar |  |
| RF_20 | Llave eliminatoria | Mostrar la visualizacion del bracket construido con los partidos registrados. | Pendiente de agregar |  |
| RF_21 | Estadisticas | Mostrar el resumen de partidos, goles y estados del torneo. | Pendiente de agregar |  |
| RF_22 | Registro de arbitros y organizadores | Mostrar el registro administrativo de usuarios con roles permitidos. | Pendiente de agregar |  |
| RF_23 | Consultar auditoria | Mostrar la consulta del historial de auditoria con sus filtros disponibles. | Pendiente de agregar |  |

---

## Diagramas

### Diagrama de contexto del sistema

[DiagramaContexto(2).png](docs/uml/DiagramaContexto%282%29.png)

### Diagrama de clases

[Ver diagrama de clases](docs/uml/diagramas-clases.md)

### Diagramas de componentes

[Ver diagramas de componentes](docs/uml/diagramas-componentes.md)

### Diagrama de Entidad-Relacion

[Ver diagrama Entidad-Relacion](docs/uml/DiagramaER/DiagramaER.jpg)

### Diagramas de secuencia

- [01 - Registro de usuario](docs/uml/diagramas-secuencia/01-registro-usuario.md)
- [02 - Login](docs/uml/diagramas-secuencia/02-login.md)
- [03 - Torneos](docs/uml/diagramas-secuencia/03-torneos.md)
- [04 - Equipos](docs/uml/diagramas-secuencia/04-equipos.md)
- [05 - Jugadores](docs/uml/diagramas-secuencia/05-jugadores.md)
- [06 - Partidos](docs/uml/diagramas-secuencia/06-partidos.md)
- [07 - Pagos](docs/uml/diagramas-secuencia/07-pagos.md)
- [08 - Capitanes](docs/uml/diagramas-secuencia/08-capitanes.md)
- [09 - Organizadores](docs/uml/diagramas-secuencia/09-organizadores.md)
- [10 - Arbitros](docs/uml/diagramas-secuencia/10-arbitros.md)
- [11 - Alineaciones](docs/uml/diagramas-secuencia/11-alineaciones.md)

---

## Pruebas

[Ver reporte de pruebas](docs/planing/pruebas.md)
