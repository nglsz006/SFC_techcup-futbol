# TECHCUP FUTBOL

Repositorio dedicado al desarrollo del proyecto **TECHCUP FUTBOL**.

---

## 🎬 Demo del Proyecto

[![Ver demo completa del proyecto](https://img.shields.io/badge/▶%20Ver%20Demo-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/nywiG4Y0fFc)

> Prueba completa del sistema TECHCUP FÚTBOL — flujo end-to-end del proyecto.

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

| Recurso                             | Enlace                                                                                                                                                               |
|-------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Repositorio Frontend                | [Ver repositorio](https://github.com/juanhernandez2003/ShawarmaFCTechCupFrontEnd.git)                                                                                |
| Mockup Figma                        | [Ver diseño](https://www.figma.com/design/RBlkiZOJCzHNZrpJdHP88Q/TECHCUP?node-id=0-1&p=f&t=hCEw9tLBCOW7NR71-0)                             |
| Manual de identidad                 | [Ver manual](docs/planing/manual%20identidad.md)                                       |
| Tablero JIRA                        | [Ver tablero](https://mail-team-w4y4k0y5.atlassian.net/jira/software/projects/TF/boards/35?atlOrigin=eyJpIjoiY2ZjNWQ5ZGRjMTM3NDAwMGIxNDEwYWUwNjFkNGY1NTMiLCJwIjoiaiJ9) |
| Video mockup                        | [Ver video](https://www.youtube.com/watch?v=u2pviu8SbS0)                                                                                                             |
| Video demo tecnica persistencia     | [Ver video](https://www.youtube.com/watch?v=RVWfsO4EjQk)                                                                                                             |
| Video demo tecnica seguridad jwt    | [Ver video](https://www.youtube.com/watch?v=un_0RBnjYJM)                                                                                                           |
| Video demo tecnica seguridad AOuth2 | [Ver video](https://www.youtube.com/watch?v=Jcp5-ZxAYo4)                                                                                                           |

---

## Diagramas

### Diagrama de contexto del sistema

[Ver diagrama de contexto](docs/uml/diagrama-contexto.md)

### Diagrama de clases

[Ver diagrama de clases](docs/uml/diagrama-clases.md)

### Diagrama de componentes

[Ver diagrama de componentes](docs/uml/diagrama-componentes.md)

### Diagrama de Entidad-Relacion

[Ver diagrama Entidad-Relacion](docs/uml/DiagramaER/DiagramaER.md)

### Diagramas de secuencia

- [01 - Registro de usuario](docs/uml/diagramas-secuencia/01-registro-usuario.md)
- [02 - Login y JWT](docs/uml/diagramas-secuencia/02-login.md)
- [03 - Torneos](docs/uml/diagramas-secuencia/03-torneos.md)
- [04 - Equipos](docs/uml/diagramas-secuencia/04-equipos.md)
- [05 - Jugadores](docs/uml/diagramas-secuencia/05-jugadores.md)
- [06 - Partidos](docs/uml/diagramas-secuencia/06-partidos.md)
- [07 - Pagos](docs/uml/diagramas-secuencia/07-pagos.md)
- [08 - Capitanes](docs/uml/diagramas-secuencia/08-capitanes.md)
- [09 - Organizadores](docs/uml/diagramas-secuencia/09-organizadores.md)
- [10 - Arbitros](docs/uml/diagramas-secuencia/10-arbitros.md)
- [11 - Alineaciones](docs/uml/diagramas-secuencia/11-alineaciones.md)
- [12 - OAuth2 y Administrador](docs/uml/diagramas-secuencia/12-oauth2-admin.md)

---

## Pruebas

[Ver reporte de pruebas](docs/planing/pruebas.md)

### Cobertura de pruebas

Reporte generado con **JaCoCo** y analizado con **SonarCloud**.

| Métrica | Cubierto | Total | Cobertura |
|---|---|---|---|
| Líneas | 1964 | 2188 | 90% |
| Ramas | 509 | 745 | 68% |
| Métodos | 744 | 794 | 94% |

- Las capas de **servicio**, **validación** y **controlador** concentran la mayor parte de las pruebas unitarias e integración.
- La cobertura de ramas del 68% refleja principalmente ramas de manejo de errores y flujos alternativos en lógica de negocio compleja (llaves eliminatorias, avance de fases).
- SonarCloud reporta **0 bugs abiertos** tras los arreglos de `Optional.get()` sin verificar en `AlineacionService` y `CapitanService`.
- Las capas de persistencia (`persistence/`) están excluidas del análisis de cobertura por configuración de JaCoCo.

---

## Sprint 4 — Videos de pruebas técnicas

| Prueba | Enlace |
|---|---|
| Demo persistencia | [Ver video](https://youtu.be/eJXH36PVICs) |
| Demo JWT | [Ver video](https://youtu.be/PX0B0lXNhfQ) |
| Demo OAuth2 Google | [Ver video](https://youtu.be/ZZAPFg6h4mE) |

---

## Repositorio Frontend

[https://github.com/juanhernandez2003/ShawarmaFCTechCupFrontEnd.git](https://github.com/juanhernandez2003/ShawarmaFCTechCupFrontEnd.git)
