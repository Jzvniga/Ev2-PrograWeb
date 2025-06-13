Evaluación II - Backend

Estudiante: Jose ZúñigaCarrera
Ingeniería Civil Informática - Universidad Católica del Maule

📘 Introducción

Este proyecto corresponde a una API RESTful desarrollada en Java utilizando Spring Boot, cuyo objetivo es modelar y gestionar un sistema de biblioteca. La aplicación permite registrar usuarios y lectores, gestionar préstamos de libros, controlar multas y administrar roles de acceso utilizando autenticación y autorización JWT.

✨ Tecnologías Utilizadas

Java 17

Spring Boot 3.x

Spring Security con JWT

Hibernate + JPA

PostgreSQL

Postman (para pruebas manuales de endpoints)

⚙️ Requisitos Previos

Tener PostgreSQL activo y accesible con una base de datos creada.

Configurar correctamente las credenciales de conexión en application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/tu_basedatos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

Ejecutar la clase DemoApplication.java para levantar el servidor.

Los roles "ADMIN" y "LECTOR" se insertan automáticamente al iniciar por primera vez mediante DataInitializer.java.

🔐 Roles y Permisos

ADMIN: Acceso total a todos los endpoints.

LECTOR: Acceso restringido a consultas sobre libros, sus propios préstamos y multas.

🚀 Cómo Probar el Proyecto Paso a Paso

1. Autenticación y Registro

**POST **/auth/register → Crea usuario nuevo.Body ejemplo:

{
  "email": "admin1@ejemplo.com",
  "password": "123456",
  "role": "ADMIN"
}

**POST **/auth/login → Retorna un token JWT.Body ejemplo:

{
  "email": "admin1@ejemplo.com",
  "password": "123456"
}

Luego copia el token y agrégalo a la cabecera Authorization de Postman:

Bearer <TOKEN>

2. Endpoints de Libros

GET /book/all → Lista todos los libros (ADMIN y LECTOR).

GET /book/all/{type} → Filtra por tipo.

POST /book/new → Crea libro (solo ADMIN).Body:

{
  "title": "1984",
  "author": "George Orwell",
  "type": "Novela"
}

POST /book/newcopy → Crea copia disponible.Body:

{
  "bookId": 5
}

GET /book/find/{title} → Busca libro por título.

GET /book/copy/{title} → Lista copias por título.

3. Endpoints de Préstamos

POST /booking/newBody:

{
  "readerEmail": "usuario@ejemplo.com",
  "bookId": 5
}

POST /booking/return/{id} → Marcar como devuelto.

GET /booking/find/{email} → Lista préstamos por email.

GET /booking/all → Todos los préstamos (ADMIN).

4. Endpoints de Multas

GET /fine/find/{email} → Ver multas de un lector (ADMIN o LECTOR).

5. Endpoints de Lector

GET /reader/find/{email} → Devuelve estado, multas y préstamos.

PUT /reader/toggle/{email} → Activa/desactiva un lector.

🧪 Validaciones Internas

No se puede prestar si:

El lector está inactivo.

No hay copias disponibles.

Solo ADMIN puede crear, prestar y devolver.

LECTOR solo puede consultar lo suyo.

JWT asegura autenticación y autorización en todos los endpoints protegidos.
