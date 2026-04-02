# Diagramas de componentes

Estos diagramas muestran cómo está organizado el sistema por dentro: qué partes existen, cómo se hablan entre sí y de qué depende cada una. El sistema está dividido en capas: primero llega la petición al controlador, el controlador le pasa el trabajo al servicio, el servicio consulta el repositorio, y el repositorio devuelve los datos del modelo.

---

## General

El diagrama general muestra toda esa cadena de un vistazo. El cliente (puede ser Swagger, Postman o el frontend) manda una petición HTTP. Los **Controllers** la reciben y la delegan a los **Services**, que son los que tienen la lógica de negocio. Los **Services** usan los **Repositories** para guardar o consultar datos. Los **Validators** entran antes de que el servicio haga cualquier cosa, para asegurarse de que los datos estén bien. Y si algo sale mal, las **Exceptions** lo capturan y devuelven un error claro al cliente.

<img width="196" height="511" alt="image" src="https://github.com/user-attachments/assets/6dbe1308-0a07-4652-9dbc-616fbac276b0" />



---

## Específicos

---

**Pago**

Este módulo maneja todo lo del pago de inscripción. Un equipo sube su comprobante, eso crea un pago en estado `PENDIENTE`. El organizador lo revisa y lo pasa a `EN_REVISION`, y luego lo aprueba o rechaza. El `PagoValidator` se asegura de que el comprobante no esté vacío y que el equipo exista antes de guardar nada.

<img width="448" height="612" alt="Componente pago" src="https://github.com/user-attachments/assets/18aa3f53-9515-4c51-bc63-41023b6d0e19" />

---

**Partido**

Este módulo maneja el ciclo de vida de cada partido. Un partido empieza `PROGRAMADO`, luego pasa a `EN_CURSO` cuando arranca, y termina en `FINALIZADO` cuando se registra el resultado. El `PartidoValidator` verifica que los equipos sean distintos y que la fecha sea válida. Además, cuando pasa algo importante en el partido (como que inicia), el servicio notifica a los observers registrados.

<img width="437" height="599" alt="Componente partido" src="https://github.com/user-attachments/assets/e803bab5-bcfe-4e9e-abc6-6d9897b15f2e" />

---

**Torneo**

Este módulo maneja la creación y gestión del torneo. Un torneo arranca como `CREADO`, pasa a `EN_CURSO` cuando inicia y termina en `FINALIZADO`. Dependiendo del estado, ciertas cosas están habilitadas o no (por ejemplo, no puedes inscribir equipos en un torneo que ya está en curso). El `ValidacionTorneo` revisa que las fechas tengan sentido y que haya suficientes equipos.

<img width="437" height="596" alt="Componente torneo" src="https://github.com/user-attachments/assets/03893296-1cd4-4552-983a-6691a5c15e6e" />

---

**Equipo**

Este módulo maneja la creación y composición de los equipos. Se puede crear un equipo, agregarle jugadores y consultarlo. El `ValidacionEquipo` se asegura de que el equipo tenga capitán, nombre y la cantidad mínima de jugadores antes de que pueda participar en el torneo.

<img width="441" height="597" alt="Componente equipo" src="https://github.com/user-attachments/assets/976cdd5f-b3a2-4adf-9d2b-f35986888815" />

---

**Usuario**

Este módulo maneja el registro y el login. Cuando alguien se registra, el `RegistroValidator` revisa que el correo sea válido y que la contraseña cumpla los requisitos. La contraseña se guarda cifrada usando `PasswordUtil`. Cuando alguien hace login, el `AccesoServiceImpl` verifica las credenciales y devuelve la información del usuario. El `AccesoMapper` se encarga de transformar los datos entre lo que llega del cliente y lo que maneja el sistema.

<img width="558" height="595" alt="Componente usuario" src="https://github.com/user-attachments/assets/aa6b7b29-050c-4ffc-ba37-5472eee05125" />
