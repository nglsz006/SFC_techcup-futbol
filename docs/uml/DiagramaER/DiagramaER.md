# Diagrama de Entidad-Relación

Este diagrama muestra cómo se organiza toda la información del torneo en la base de datos. Cada bloque es una tabla donde se guardan datos, y las líneas entre ellas muestran cómo se conectan. Por ejemplo, un partido siempre pertenece a un torneo, y un gol siempre pertenece a un partido.

---

## ¿Qué significa cada tabla?

- **usuario** — Guarda la información básica de todas las personas del sistema: nombre, correo y contraseña. De aquí parten todos los demás tipos de usuario.
- **jugador** — Es un usuario que además tiene número de camiseta, posición y foto.
- **capitan** — Es un jugador que además lidera un equipo.
- **arbitro** — Es un usuario que dirige los partidos.
- **organizador** — Es un usuario que crea y gestiona el torneo.
- **administrador** — Es quien registra a los organizadores y árbitros, y tiene acceso al historial de acciones.
- **usuario_registrado** — Es quien se registra directamente en la plataforma o con Google.
- **equipo** — Agrupa a los jugadores bajo un capitán, con nombre, escudo y colores.
- **torneo** — Tiene toda la información de la competencia: fechas, costo, reglamento, canchas y horarios.
- **partido** — Es cada enfrentamiento entre dos equipos dentro del torneo.
- **gol** — Registra cada gol: en qué minuto fue y quién lo hizo.
- **tarjeta** — Registra cada tarjeta (amarilla o roja): en qué minuto y a quién.
- **sancion** — Registra sanciones más graves como agresiones verbales o físicas.
- **pago** — Es el comprobante que sube el capitán para inscribir a su equipo.
- **alineacion** — Es la formación táctica que define el capitán antes de cada partido.
- **perfil_deportivo** — Guarda información deportiva detallada del jugador: edad, dorsal, género y semestre.
- **registro_auditoria** — Guarda un historial de todas las acciones importantes que hace el administrador.

---

```mermaid
erDiagram

    usuario {
        string id PK
        string name
        string email
        string password
        string user_type
        string tipo_rol
    }

    jugador {
        string id PK
        int jersey_number
        string position
        boolean available
        string photo
        string equipo_id FK
    }

    capitan {
        string id PK
        string team_id FK
    }

    arbitro {
        string id PK
    }

    organizador {
        string id PK
        string torneo_id FK
    }

    administrador {
        string id PK
        boolean activo
    }

    usuario_registrado {
        string id PK
    }

    equipo {
        string id PK
        string nombre
        string escudo
        string color_principal
        string color_secundario
        string capitan_id FK
    }

    equipo_jugadores {
        string equipo_id FK
        string jugador_id FK
    }

    torneo {
        string id PK
        string nombre
        datetime fecha_inicio
        datetime fecha_fin
        int cantidad_equipos
        double costo
        string estado
        text reglamento
        datetime cierre_inscripciones
        string canchas
        string horarios
        string sanciones
    }

    partido {
        string id PK
        datetime fecha
        string cancha
        int marcador_local
        int marcador_visitante
        string estado
        string torneo_id FK
        string equipo_local_id FK
        string equipo_visitante_id FK
    }

    gol {
        string id PK
        int minuto
        string jugador_id FK
        string partido_id FK
    }

    tarjeta {
        string id PK
        string tipo
        int minuto
        string jugador_id FK
        string partido_id FK
    }

    sancion {
        string id PK
        string tipo_sancion
        string descripcion
        string jugador_id FK
    }

    pago {
        string id PK
        string comprobante
        date fecha_subida
        string estado
        string equipo_id FK
    }

    alineacion {
        string id PK
        string equipo_id FK
        string partido_id FK
        string formacion
    }

    alineacion_titulares {
        string alineacion_id FK
        string jugador_id
    }

    alineacion_reservas {
        string alineacion_id FK
        string jugador_id
    }

    perfil_deportivo {
        string id PK
        string jugador_id FK
        int dorsal
        string foto
        int edad
        string genero
        string identificacion
        int semestre
    }

    perfil_posiciones {
        string perfil_id FK
        string posicion
    }

    registro_auditoria {
        string id PK
        string administrador_id FK
        string usuario
        string tipo_accion
        text descripcion
        datetime fecha
    }

    usuario ||--o{ jugador : "es"
    usuario ||--o{ arbitro : "es"
    usuario ||--o{ organizador : "es"
    usuario ||--o{ administrador : "es"
    usuario ||--o{ usuario_registrado : "es"
    jugador ||--o{ capitan : "es"

    equipo ||--o{ equipo_jugadores : "tiene"
    capitan ||--o| equipo : "lidera"
    organizador ||--o| torneo : "gestiona"

    torneo ||--o{ partido : "tiene"
    partido }o--|| equipo : "equipo local"
    partido }o--|| equipo : "equipo visitante"

    partido ||--o{ gol : "registra"
    partido ||--o{ tarjeta : "registra"
    jugador ||--o{ gol : "anota"
    jugador ||--o{ tarjeta : "recibe"
    jugador ||--o{ sancion : "recibe"

    equipo ||--o{ pago : "tiene"
    equipo ||--o{ alineacion : "define"
    partido ||--o{ alineacion : "tiene"
    alineacion ||--o{ alineacion_titulares : "titulares"
    alineacion ||--o{ alineacion_reservas : "reservas"

    jugador ||--o| perfil_deportivo : "tiene"
    perfil_deportivo ||--o{ perfil_posiciones : "posiciones"

    administrador ||--o{ registro_auditoria : "genera"
```
