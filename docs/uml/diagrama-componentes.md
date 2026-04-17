# Diagrama de Componentes

Este diagrama muestra cómo está organizado el sistema por dentro: qué piezas existen, de qué depende cada una y cómo se comunican entre sí. Está dividido en cinco módulos para que sea más fácil de entender.

Toda petición que llega del cliente pasa primero por el filtro JWT, que verifica que el usuario tenga un token válido. Si lo tiene, la petición llega al controlador correspondiente. El controlador le pasa el trabajo al servicio, que contiene la lógica del negocio. El servicio usa los validadores para verificar que los datos estén bien antes de hacer cualquier cosa, y los repositorios para guardar o consultar datos en PostgreSQL a través de la capa de persistencia con JPA y los mappers de MapStruct.

---

## Modulos

- [Arquitectura general](componentes/componentes-general.md)
- [Acceso y Seguridad](componentes/componentes-acceso.md)
- [Torneo y Partido](componentes/componentes-torneo-partido.md)
- [Usuarios, Equipos y Pagos](componentes/componentes-usuarios-equipos.md)
- [Administracion y Auditoria](componentes/componentes-admin.md)
