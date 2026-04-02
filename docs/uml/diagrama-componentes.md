# Diagrama de Componentes

Acá se muestra cómo está organizado el sistema por dentro: qué piezas existen, de qué depende cada una y cómo se comunican entre sí. Está dividido en cuatro módulos para que sea más fácil de entender.

Todas las peticiones pasan primero por el filtro de seguridad JWT, que verifica que el usuario esté autenticado. Luego llegan al controlador correspondiente, que delega el trabajo al servicio, que a su vez usa los repositorios para guardar o consultar datos en PostgreSQL.

---

## Módulos

- [Arquitectura general](componentes/componentes-general.md)
- [Acceso y Seguridad](componentes/componentes-acceso.md)
- [Torneo y Partido](componentes/componentes-torneo-partido.md)
- [Usuarios, Equipos y Pagos](componentes/componentes-usuarios-equipos.md)
- [Administración y Auditoría](componentes/componentes-admin.md)
