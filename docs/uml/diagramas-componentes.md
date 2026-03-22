# Diagramas de componentes

Los diagramas de componentes muestran cómo está organizado el sistema internamente, qué módulos existen, cómo se comunican entre sí y cuáles son sus dependencias. El sistema sigue una arquitectura en capas: controladores → servicios → repositorios → modelos.

---

## General

El diagrama general muestra la arquitectura completa del sistema. El cliente (frontend o herramienta como Swagger) se comunica con los **Controllers** a través de HTTP. Los controllers delegan la lógica de negocio a los **Services**, que a su vez acceden a los datos mediante los **Repositories**. Los **Models** representan las entidades del dominio. Los **Validators** son transversales y validan las reglas de negocio antes de que los servicios procesen cualquier operación. Las **Exceptions** manejan los errores de forma centralizada a través del `ErrorHandler`.

<img width="304" height="542" alt="Diagrama de componentes general" src="https://github.com/user-attachments/assets/18c074fe-b614-4267-8ff3-f57cf9fe90f7" />

---

## Específicos

---

**Pago**

El módulo de pago gestiona el flujo de comprobantes de inscripción. El `PagoController` expone los endpoints para subir un comprobante y verificarlo. El `PagoServiceImpl` contiene la lógica de transición de estados usando el patrón State: un pago pasa de `PENDIENTE` → `EN_REVISION` → `APROBADO` o `RECHAZADO`. El `PagoValidator` valida que el comprobante no esté vacío y que el equipo exista. El `PagoRepositoryImpl` almacena los pagos en memoria.

<img width="448" height="612" alt="Componente pago" src="https://github.com/user-attachments/assets/18aa3f53-9515-4c51-bc63-41023b6d0e19" />

---

**Partido**

El módulo de partido maneja el ciclo de vida de cada encuentro del torneo. El `PartidoController` expone endpoints para crear, iniciar, registrar resultado y finalizar un partido. El `PartidoServiceImpl` aplica el patrón State para controlar las transiciones válidas: `PROGRAMADO` → `EN_CURSO` → `FINALIZADO`. El `PartidoValidator` verifica que los equipos sean distintos y que las fechas sean válidas. Además, el servicio extiende `Subject` para notificar a los observers cuando ocurre un evento como el inicio del partido.

<img width="437" height="599" alt="Componente partido" src="https://github.com/user-attachments/assets/e803bab5-bcfe-4e9e-abc6-6d9897b15f2e" />

---

**Torneo**

El módulo de torneo centraliza la configuración y gestión del torneo. El `TorneoController` permite crear, consultar, iniciar y finalizar torneos. El `TorneoService` aplica el patrón State con tres estados: `TorneoCreado`, `TorneoEnCurso` y `TorneoFinalizado`, cada uno controlando qué operaciones están permitidas. El `ValidacionTorneo` verifica reglas como fechas coherentes y cantidad mínima de equipos. El `TorneoRepositoryImpl` persiste los torneos en memoria.

<img width="437" height="596" alt="Componente torneo" src="https://github.com/user-attachments/assets/03893296-1cd4-4552-983a-6691a5c15e6e" />

---

**Equipo**

El módulo de equipo gestiona la creación y composición de los equipos participantes. El `EquipoController` expone endpoints para crear equipos, agregar jugadores y consultar información. El `EquipoService` aplica las reglas de negocio como el límite de jugadores por equipo. El `ValidacionEquipo` verifica que el equipo tenga capitán, nombre y la cantidad mínima de jugadores requerida. El `EquipoRepositoryImpl` almacena los equipos en memoria.

<img width="441" height="597" alt="Componente equipo" src="https://github.com/user-attachments/assets/976cdd5f-b3a2-4adf-9d2b-f35986888815" />

---

**Usuario**

El módulo de usuario maneja el registro, login y gestión de los distintos tipos de usuarios del sistema. El `AccesoController` expone los endpoints de registro y login. El `AccesoServiceImpl` coordina la validación de credenciales usando `PasswordUtil` para el cifrado de contraseñas y `AccesoMapper` para transformar los datos entre capas. El `AccesoValidator` y `RegistroValidator` verifican que los datos de entrada sean correctos. `UsuarioRegistradoRepositoryImpl` almacena los usuarios registrados.

<img width="558" height="595" alt="Componente usuario" src="https://github.com/user-attachments/assets/aa6b7b29-050c-4ffc-ba37-5472eee05125" />
