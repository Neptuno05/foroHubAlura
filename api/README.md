# ForoHub API 📚
<p align="left">
   <img src="https://img.shields.io/github/stars/AFernandzG?style=social">
   </p>
¡Bienvenido a ForoHub API! Esta aplicación es una API RESTful para gestionar foros de discusión, permitiendo la creación, actualización y eliminación de temas y respuestas.

---

## Estructura del Proyecto 📂

```plaintext
src
 ├── main
 │   ├── java/
 |   |    └── com/alura/foro/hub/api
 │   │         ├── controller           -> REST controllers.
 │   │         ├── domain               -> JPA Entities repositoriesn and data transfer Objects.
 │   │         └── infra                
 │   │              ├── errores         -> Error handlers, security and docs.
 │   │              ├── security        
 │   │              ├── service         -> Business Logic.
 │   │              └── sprincdoc
 │   └── resources
 │       ├── application.properties     -> Configuration app.
 │       └── db.migration.sql           -> Scripts SQL and migration with flyway (optional).
 └── test
     └── java/com/alura/foro/hub/api          -> Units Test and Integration.
```
## Requisitos 📋
- **Java 11 o superior**
- **Spring Boot**
- **Hibernate**
- **Flyway** (opcional para migraciones de base de datos)

---
## Endpoints Principales 🚀
### Usuarios 👥
- **Crear Usuario**
  - **Método**: POST
  - **URL**: /users
  - **Descripción**: Registra un nuevo usuario en la base de datos.
  - **Cuerpo de la Solicitud**:

```json   
   {
    "username": "johndoe",
    "password": "password123",
    "profileType": "USER"
    }
```
- ### Listar Todos los Usuarios*
  - **Método**: GET
  - **URL**: `/users/all`
  - **Descripción**: Enumera todos los usuarios independientemente de su estado.

---
## Autenticación 🔐
- ### Autenticar Usuario
  - **Método**: POST
  - **URL**: `/login`
  - **Descripción**: Autentica al usuario con las credenciales proporcionadas y genera un token JWT para el acceso.
  - **Cuerpo de la Solicitud**:

```json
   {
    "email": "usuario@example.com",
    "clave": "password123"
    }
```
---
## Respuestas 💬
- ### Crear Respuesta
  - **Método**: POST
  - **URL**: `/answers`
  - **Descripción**: Registra una nueva respuesta en la base de datos, vinculada a un usuario y tema existente.
  - **Cuerpo de la Solicitud**:

```json
{
  "message": "Esta es una respuesta ejemplo.",
  "usuarioId": 5,
  "topicoId": 3
}
```
- ### Listar Respuestas por Tema
- Método: GET
- URL: /answers/topic/{topicId}
- Descripción: Lee todas las respuestas del tema dado.

---
## Temas 📑
- ### Crear Tema
  - **Método**: POST
  - **URL**: `/topics`
  - **Descripción**: Registra un nuevo tema en la base de datos, vinculado a un usuario y curso existentes.
  - **Cuerpo de la Solicitud**:
```json
{
  "title": "Nuevo Tema",
  "message": "Contenido del tema",
  "userid": 1,
  "courseid": 2
}

```
- ### Listar Todos los Temas
  - **Método**: GET
  - **URL**: `/topics/all`
  - **Descripción**: Lee todos los temas independientemente de su estado.
- ### Obtener Solución de Tema
  - **Método**: GET
  - **URL**: /topics/{id}/solution
  - **Descripción**: Lee la respuesta del tema marcada como su solución.

---
## Configuración 🛠️
**Configura la aplicación editando el archivo `application.properties` en el directorio `src/main/resources`.**

---
## Migraciones de Base de Datos 🗄️
**Si estás utilizando Flyway para las migraciones, asegúrate de colocar tus scripts SQL en `src/main/resources/db.migration.sql`.**

---
### 📝 Licencia
**Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para obtener más información.**

---
**¡Gracias por usar ForoHub API! Si tienes alguna pregunta o sugerencia, no dudes en contactar.** 😊

---