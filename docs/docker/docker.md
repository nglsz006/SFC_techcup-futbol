# Docker — TechCup Fútbol

## Prerrequisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y corriendo
- Archivo `.env` configurado en la raíz del proyecto (ver `.envEjemplo`)

---

## Configuración del `.env`

Copia el archivo de ejemplo y completa los valores (Asegurarse de tener el archivo keystore.p12 dentro de
resourcers en la carpeta main):
```bash
cp .envEjemplo .env
```

Variables requeridas:
```env
# Base de datos
DB_NAME=techcup_futbol
DB_USERNAME=postgres
DB_PASSWORD=tu_password

# Google OAuth2
GOOGLE_CLIENT_ID=tu_client_id
GOOGLE_CLIENT_SECRET=tu_client_secret

# Servidor
SERVER_HTTP_PORT=8080

# SSL
SSL_PASSWORD=ContraseñaSSl
```

---

## Levantar el entorno

### Primera vez o después de cambios en el código
```bash
docker-compose up --build
```

### Siguientes veces (sin cambios en el código)
```bash
docker-compose up
```

### En segundo plano
```bash
docker-compose up -d
```

---

## Bajar el entorno

### Mantener los datos de la base de datos
```bash
docker-compose down
```

### Resetear todo incluyendo la base de datos (NO USAR SI NO ES NECESARIO)
```bash
docker-compose down -v
```

---

## URLs de acceso

| Servicio | URL |
|---|---|
| Swagger UI | `https://localhost:8443/swagger-ui/index.html` |
| Redirect HTTP → HTTPS | `http://localhost:8080` → redirige automáticamente |

> El navegador mostrará advertencia de certificado no confiable por ser autofirmado — aceptar y continuar.

---

## Comandos útiles
```bash
# Ver logs en tiempo real
docker logs -f techcup-backend
docker logs -f techcup-db

# Ver estado de los contenedores
docker ps

# Reconstruir sin caché
docker-compose build --no-cache
docker-compose up
```