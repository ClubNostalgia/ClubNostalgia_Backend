# 🎬 Club Nostalgia - Backend

API REST del sistema Club Nostalgia, una plataforma para gestionar proyectos audiovisuales nostálgicos.

## Tabla de Contenidos

- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#️configuración)
- [Ejecutar la Aplicación](#ejecutar-la-aplicación)
- [API Endpoints](#api-endpoints)
- [Seguridad](#seguridad)
- [Testing](#testing)
- [Scripts Maven](#scripts-maven)
- [Notas Importantes](#notas-importantes)

## Tecnologías

Este proyecto está construido con las siguientes tecnologías:

- **[Java 21](https://openjdk.org/)** - Lenguaje de programación
- **[Spring Boot 3.5.7](https://spring.io/projects/spring-boot)** - Framework principal
- **[Spring Security](https://spring.io/projects/spring-security)** - Autenticación y autorización
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** - Persistencia de datos
- **[H2 Database](https://www.h2database.com/)** - Base de datos en archivo
- **[JWT (java-jwt)](https://github.com/auth0/java-jwt)** - Tokens de autenticación
- **[MapStruct 1.6.3](https://mapstruct.org/)** - Mapeo de DTOs
- **[Lombok 1.18.38](https://projectlombok.org/)** - Reducción de código boilerplate
- **[Maven](https://maven.apache.org/)** - Gestión de dependencias y build
- **[JUnit 5](https://junit.org/junit5/)** - Framework de testing
- **[Mockito](https://site.mockito.org/)** - Mocking para tests
- **[SendGrid](https://sendgrid.com/)** - Envío de emails

##  Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK** >= 21
- **Maven** >= 3.8.0

Verificar versiones:
```bash
java -version
mvn -version
```

##  Instalación

1. **Clona el repositorio**:
```bash
git clone https://github.com/ClubNostalgia/ClubNostalgia_Backend.git
cd ClubNostalgia_Backend
```

2. **Instala las dependencias**:
```bash
mvn clean install
```

3. **Crea el archivo de variables de entorno**:
```bash
cp .env.example .env
```


##  Ejecutar la Aplicación

### Modo Desarrollo
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

```

##  API Endpoints

### Autenticación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Iniciar sesión | No |
| POST | `/api/auth/register` | Registrar usuario | No |

### Usuarios

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/users` | Crear admin | Sí |
| GET | `/api/users` | Listar usuarios | Sí |
| GET | `/api/users/{id}` | Obtener usuario por ID | Sí |
| GET | `/api/users/name/{name}` | Obtener usuario por nombre | Sí |

### Proyectos

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/projects` | Crear proyecto | Sí |
| GET | `/api/projects` | Listar proyectos | No |
| GET | `/api/projects/{id}` | Obtener proyecto por ID | No |
| GET | `/api/projects/title/{title}` | Obtener proyecto por título | No |
| PUT | `/api/projects/{id}` | Actualizar proyecto | Sí |
| DELETE | `/api/projects/{id}` | Eliminar proyecto | Sí |

### Categorías

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/categories` | Listar categorías | No |

### Ejemplo de Request

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

**Crear Proyecto (con token):**
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <tu_token_jwt>" \
  -d '{
    "title": "Proyecto Nostalgia",
    "video": "https://www.youtube.com/watch?v=example",
    "videoType": "YOUTUBE",
    "synopsis": "Una historia nostálgica...",
    "information": "Información adicional...",
    "author": "Director",
    "categoryId": "uuid-de-categoria"
  }'
```

##  Base de Datos

### H2 Console

Accede a la consola de H2 en: `http://localhost:8080/h2-console`



##  Testing

### Estructura de Tests
```
src/test/java/
├── controller/              # Tests unitarios de controllers
│   ├── UserControllerTest.java
│   ├── ProjectControllerTest.java
│   └── CategoryControllerTest.java
├── service/                 # Tests unitarios de services
│   └── impl/
│       └── UserServiceImplTest.java
└── integration/             # Tests de integración
    └── repository/
        └── UserRepositoryIntegrationTest.java
```

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=UserControllerTest
mvn test -Dtest=UserServiceImplTest
mvn test -Dtest=UserRepositoryIntegrationTest

# Con más detalle
mvn test -X
```

### Cobertura de Código

##  Scripts Maven
```bash
# Compilar
mvn compile

# Limpiar y compilar
mvn clean compile

# Ejecutar tests
mvn test

# Generar JAR
mvn package

# Limpiar, compilar, testear y empaquetar
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run

```

##  Notas Importantes

###  Persistencia de Datos

- H2 en modo archivo **persiste los datos** entre reinicios
- Los datos se guardan en `./data/clubnostalgia.mv.db`
- **Hacer backups regulares** de la carpeta `data/`


### Emails

- Configurado con **SendGrid**
- Verificar límites de envío del plan
- Testear en ambiente de desarrollo

```

```
##  Autora
Ana Aguilera Morales  [https://www.linkedin.com/in/ana-aguilera-morales/]

```
