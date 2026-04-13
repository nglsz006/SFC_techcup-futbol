# Componentes — Arquitectura General

Aca se ve el sistema completo de un vistazo. Toda peticion que llega del cliente pasa primero por el filtro JWT que verifica que el usuario tenga un token valido. Si lo tiene, la peticion llega al controlador correspondiente. El controlador le pasa el trabajo al servicio, que contiene la logica del negocio. El servicio usa los validadores para verificar que los datos esten bien, los patrones de diseno para manejar estados y notificaciones, y los repositorios para acceder a la base de datos PostgreSQL a traves de la capa de persistencia JPA con mappers de MapStruct.

---
<img width="229" height="487" alt="image" src="https://github.com/user-attachments/assets/52cd1fdb-a30a-4ee5-afb3-5c18cc9ed51c" />

