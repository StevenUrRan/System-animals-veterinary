# Sistema Veterinario — API REST

> **Backend REST API** para la gestión integral de un sistema veterinario: animales, citas, historias clínicas, veterinarios y facturación. Desarrollado con Java 17 y Spring Boot 3.3.

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Módulos](#módulos)
- [Modelo de roles y permisos](#modelo-de-roles-y-permisos)
- [Endpoints principales](#endpoints-principales)
- [Requisitos previos](#requisitos-previos)
- [Configuración del entorno](#configuración-del-entorno)
- [Ejecución local](#ejecución-local)
- [Documentación de la API](#documentación-de-la-api)
- [Guía de pruebas paso a paso](#guía-de-pruebas-paso-a-paso)
- [Referencia rápida de enums](#referencia-rápida-de-enums)
- [Estado del proyecto](#estado-del-proyecto)

---

## Descripción

Sistema Veterinario es una API REST que centraliza la operación de una clínica veterinaria. Permite gestionar el ciclo completo de atención: registro de animales, asignación de veterinarios, agendamiento de citas, historial clínico y generación de facturas.

El sistema implementa autenticación stateless con JWT, verificación de email por código y un modelo de control de acceso basado en roles (RBAC) con seis perfiles diferenciados.

---

## Stack tecnológico

| Categoría        | Tecnología                       |
| ---------------- | -------------------------------- |
| Lenguaje         | Java 17                          |
| Framework        | Spring Boot 3.3.5                |
| Seguridad        | Spring Security + JJWT           |
| Persistencia     | Spring Data JPA + Hibernate      |
| Base de datos    | Postgre                          |
| Mapeo de objetos | MapStruct                        |
| Validación       | Jakarta Bean Validation          |
| Documentación    | SpringDoc OpenAPI 3 (Swagger UI) |
| Email            | Spring Mail (SMTP / Mailtrap)    |
| Build            | Maven                            |
| Utilidades       | Lombok, Spring Actuator          |

---

## Arquitectura

El proyecto sigue una arquitectura en capas dentro de un monolito modular organizado por dominio:

```
com.system.animals
├── config/                  # Seguridad, JWT, OpenAPI, CORS
│   └── security/
│       ├── controller/      # LoginController
│       ├── filter/          # JwtAuthorizationFilter
│       └── service/         # JwtService, CustomUserDetailsService
├── exception/               # Excepciones de dominio + GlobalErrorHandler
├── modules/
│   ├── animals/             # Gestión de animales
│   ├── citation/            # Citas veterinarias
│   ├── history/             # Historias clínicas
│   ├── invoice/             # Facturación
│   ├── user/                # Usuarios y roles
│   └── veterinary/          # Perfiles de veterinarios
└── shared/
    ├── base/                # BaseEntity, MapperSupport
    ├── enums/               # Enumerados del dominio
    ├── utils/               # CodeGenerator
    └── valid/               # ValidationResult
```

Cada módulo contiene su propio conjunto de `entity`, `dto`, `mapper`, `repository`, `service` e `impl`, garantizando alta cohesión y bajo acoplamiento entre dominios.

---

## Módulos

### Animales (`/animals`)

Registro y administración de animales de la clínica. Incluye NIT del animal, especie, género, estado y datos del propietario.

### Veterinarios (`/veterinary`)

Perfil clínico vinculado a un usuario con rol `VETERINARIAN`. Gestiona licencia, especialidad y disponibilidad.

### Citas (`/citation`)

Agendamiento de consultas con validación de slots de horario, disponibilidad del veterinario y días hábiles.

### Historial Clínico (`/history`)

Historia clínica por animal. Solo puede existir un historial por animal. Registra diagnósticos, tratamientos y seguimiento.

### Facturación (`/Invoice`)

Generación y gestión del ciclo de vida de facturas: `PENDING → PAID / CANCELLED / OVERDUE`. Incluye detalle de servicios.

### Usuarios y Roles (`/user`, `/roles`)

Registro de usuarios con verificación de email por código de un solo uso. Asignación de roles diferenciados con control de acceso granular.

### Autenticación (`/auth`, `/login`)

- **Registro**: auto-registro público con envío de código de verificación al correo.
- **Verificación**: activación de cuenta mediante código con expiración y rate limit.
- **Login**: autenticación con email y contraseña; retorna JWT Bearer.

---

## Modelo de roles y permisos

| Rol                 | Descripción                                                       |
| ------------------- | ----------------------------------------------------------------- |
| `ROLE_ADMIN`        | Control total: usuarios, roles, configuración y auditoría global. |
| `ROLE_VETERINARIAN` | Gestión clínica: citas, historias e información de animales.      |
| `ROLE_KEEPER`       | Cuidado diario: consulta y actualización de animales.             |
| `ROLE_MANAGER`      | Gestión operativa: acceso completo a facturación.                 |
| `ROLE_AUDITOR`      | Solo lectura: animales e historias clínicas.                      |
| `ROLE_USER`         | Usuario básico: consulta de animales.                             |

### Matriz de acceso por módulo

| Recurso          | Método          | ADMIN | VETERINARIAN | KEEPER | MANAGER | AUDITOR | USER |
| ---------------- | --------------- | :---: | :----------: | :----: | :-----: | :-----: | :--: |
| `/roles/**`      | Todos           |  ✅   |              |        |         |         |      |
| `/user/**`       | GET             |  ✅   |              |        |         |         |      |
| `/user/**`       | POST            |  ✅   |              |        |         |         |      |
| `/user/nit`      | PUT             |  ✅   |      ✅      |   ✅   |   ✅    |   ✅    |  ✅  |
| `/user/nit`      | DELETE          |  ✅   |              |        |         |         |      |
| `/veterinary/**` | GET             |  ✅   |      ✅      |        |         |         |      |
| `/veterinary/**` | POST/PUT/DELETE |  ✅   |              |        |         |         |      |
| `/animals/**`    | GET             |  ✅   |      ✅      |   ✅   |   ✅    |   ✅    |  ✅  |
| `/animals/**`    | POST/PUT        |  ✅   |      ✅      |   ✅   |         |         |      |
| `/animals/**`    | DELETE          |  ✅   |              |        |         |         |      |
| `/citation/**`   | GET/POST/PUT    |  ✅   |      ✅      |        |         |         |      |
| `/citation/**`   | DELETE          |  ✅   |              |        |         |         |      |
| `/history/**`    | GET             |  ✅   |      ✅      |        |         |   ✅    |      |
| `/history/**`    | POST/PUT        |  ✅   |      ✅      |        |         |         |      |
| `/history/**`    | DELETE          |  ✅   |              |        |         |         |      |
| `/Invoice/**`    | GET/POST/PUT    |  ✅   |              |        |   ✅    |         |      |
| `/Invoice/**`    | DELETE          |  ✅   |              |        |         |         |      |

---

## Endpoints principales

La base de la API es `/api/v1`. Todos los endpoints protegidos requieren el header:

```
Authorization: Bearer <token>
```

### Autenticación (pública)

| Método | Ruta              | Descripción                              |
| ------ | ----------------- | ---------------------------------------- |
| POST   | `/auth/register`  | Registra usuario y envía código al email |
| POST   | `/auth/send-code` | Reenvía código de verificación           |
| POST   | `/auth/verify`    | Verifica el código y activa la cuenta    |
| POST   | `/login`          | Autenticación; retorna JWT               |

### Animales

| Método | Ruta           | Descripción                |
| ------ | -------------- | -------------------------- |
| GET    | `/animals`     | Lista paginada de animales |
| POST   | `/animals`     | Registra un nuevo animal   |
| PUT    | `/animals/nit` | Actualiza animal por NIT   |
| DELETE | `/animals/nit` | Elimina animal por NIT     |

### Veterinarios

| Método | Ruta                  | Descripción                    |
| ------ | --------------------- | ------------------------------ |
| GET    | `/veterinary`         | Lista paginada de veterinarios |
| GET    | `/veterinary/license` | Busca veterinario por licencia |
| POST   | `/veterinary`         | Registra perfil veterinario    |
| PUT    | `/veterinary/license` | Actualiza perfil veterinario   |
| DELETE | `/veterinary/license` | Elimina perfil veterinario     |

### Citas

| Método | Ruta                           | Descripción                 |
| ------ | ------------------------------ | --------------------------- |
| GET    | `/citation`                    | Lista paginada de citas     |
| GET    | `/citation/animalsId/{nit}`    | Cita por NIT del animal     |
| POST   | `/citation/newCitation/{nit}`  | Crea cita para un animal    |
| PUT    | `/citation/update/{nit}`       | Actualiza datos de una cita |
| PUT    | `/citation/updateStatus/{nit}` | Cambia estado de una cita   |
| DELETE | `/citation/delete/{nit}`       | Elimina una cita            |

### Historial Clínico

| Método | Ruta                    | Descripción                   |
| ------ | ----------------------- | ----------------------------- |
| GET    | `/history`              | Lista paginada de historiales |
| GET    | `/history/nit/{nit}`    | Historial por NIT del animal  |
| POST   | `/history`              | Crea historia clínica         |
| PUT    | `/history/update/{nit}` | Actualiza historia clínica    |
| DELETE | `/history/delete/{nit}` | Elimina historia clínica      |

### Facturación

| Método | Ruta                             | Descripción                  |
| ------ | -------------------------------- | ---------------------------- |
| GET    | `/Invoice`                       | Lista paginada de facturas   |
| GET    | `/Invoice/code/{code}`           | Factura por código           |
| POST   | `/Invoice`                       | Crea una nueva factura       |
| PUT    | `/Invoice/processPayment/{code}` | Procesa pago / cambia estado |
| DELETE | `/Invoice/delete/{nit}`          | Elimina una factura          |

---

## Requisitos previos

- Java 17+
- Maven 3.8+
- MySQL 8+
- (Opcional) Cuenta en [Mailtrap](https://mailtrap.io) para pruebas de email

---

## Configuración del entorno

Crea un archivo `.env` en la raíz o exporta las siguientes variables de entorno antes de iniciar:

```env
# Base de datos
DB_USERNAME=root
DB_PASSWORD=tu_password

# JWT
JWT_SECRET=tu_clave_base64_minimo_256_bits
JWT_ACCESS_EXPIRATION=900000

# Email (SMTP)
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=tu_usuario_mailtrap
MAIL_PASSWORD=tu_password_mailtrap

# Rate limit de verificación
VERIFICATION_CODE_MAX_REQUESTS=3
VERIFICATION_CODE_WINDOW_MINUTES=15
```

> **Nota de seguridad:** Nunca expongas credenciales reales en el repositorio. Las propiedades del perfil `dev` contienen valores de ejemplo solo para desarrollo local.

---

## Ejecución local

```bash
# 1. Clona el repositorio
git clone https://github.com/tu-usuario/system-animals.git
cd system-animals

# 2. Crea la base de datos en MySQL
mysql -u root -p -e "CREATE DATABASE animals_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. Configura las variables de entorno (ver sección anterior)

# 4. Ejecuta la aplicación
./mvnw spring-boot:run

# La API estará disponible en:
# http://localhost:8080/api/v1
```

> En el primer arranque, Hibernate crea las tablas automáticamente (`ddl-auto=update`). El script `data.sql` inserta los seis roles iniciales.

---

## Documentación de la API

Con la aplicación corriendo, accede a la interfaz Swagger UI:

```
http://localhost:8080/api/v1/swagger-ui/index.html
```

La especificación OpenAPI está disponible en:

```
http://localhost:8080/api/v1/v3/api-docs
```

---

## Guía de pruebas paso a paso

> Base URL: `http://localhost:8080/api/v1`  
> Todas las rutas protegidas requieren: `Authorization: Bearer <token>`

### Flujo completo recomendado

```
1. Registrar usuario  →  2. Verificar email  →  3. Login (obtener JWT)
         ↓
4. Crear usuario veterinario  →  5. Registrar perfil veterinario  →  6. Registrar animal
         ↓
7. Crear historial clínico  →  8. Agendar cita  →  9. Generar factura  →  10. Procesar pago
```

---
### Nota - Usuario ADMIN por default
**POST** `/login`
```json
{
    "email": "admin.perfecto@system-animals.local", 
    "password": "password123"
}
```
---

### Paso 1 — Registrar usuario `PUBLIC`

**POST** `/auth/register`

```json
{
  "username": "steven developer",
  "email": "steven@correo.com",
  "password": "Secure@123",
  "nit": 1234567890,
  "document": "CC",
  "gender": "MALE"
}
```

> `password` debe tener mínimo 8 caracteres e incluir mayúscula, minúscula, número y símbolo (`@#$%^&+=!_`).  
> `nit` debe estar entre `1000000000` y `9999999999`.

**Respuesta esperada** `201 Created`:

```json
{
  "message": "Usuario registrado. Revisa tu correo para verificar tu cuenta."
}
```

---

### Paso 2 — Verificar cuenta `PUBLIC`

Revisa tu bandeja (o Mailtrap en dev) y copia el código de 6 dígitos.

**POST** `/auth/verify`

```json
{
  "email": "steven@correo.com",
  "code": "482931"
}
```

**Respuesta esperada** `200 OK`:

```json
{
  "message": "Cuenta verificada exitosamente."
}
```

Si el código expiró, reenvíalo con **POST** `/auth/send-code`:

```json
{
  "email": "steven@correo.com"
}
```

---

### Paso 3 — Login `PUBLIC`

**POST** `/login`

```json
{
  "email": "steven@correo.com",
  "password": "Secure@123"
}
```

**Respuesta esperada** `200 OK`:

```json
{
  "data: ": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "message: ": "Login successful. Welcome back!"
}
```

> Copia el `token`. Todas las peticiones siguientes llevan el header:  
> `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`

---

### Paso 4 — Crear usuario con rol específico `ADMIN`

**POST** `/user/user-type`

```json
{
  "username": "dra. martinez perez",
  "email": "dra.martinez@clinica.com",
  "password": "Vet@2024!",
  "nit": 9876543210,
  "document": "CC",
  "gender": "FEMALE",
  "typeRole": "ROLE_VETERINARIAN"
}
```

**Respuesta esperada** `201 Created`:

```json
{
  "username": "dra. martinez perez",
  "email": "dra.martinez@clinica.com",
  "nit": 9876543210,
  "document": "CC",
  "gender": "FEMALE"
}
```

> Guarda el `id` que retorna la respuesta — lo necesitas para la factura (Paso 9).

---

### Paso 5 — Registrar perfil veterinario `ADMIN`

Vincula los datos clínicos a la cuenta del usuario. El `?license=` es el **nit del veterinario**.

**POST** `/veterinary?license=9876543210`

```json
{
  "age": 34,
  "yearsOfExperience": 8,
  "salary": 4500000.0,
  "phone": "3001234567",
  "userDto": {
    "username": "dra. martinez perez",
    "email": "dra.martinez@clinica.com",
    "nit": 9876543210,
    "document": "CC",
    "gender": "FEMALE"
  }
}
```

**Respuesta esperada** `201 Created`:

```json
{
  "age": 34,
  "yearsOfExperience": 8,
  "salary": 4500000.00,
  "phone": "3001234567",
  "userDto": { ... }
}
```

---

### Paso 6 — Registrar animal `ADMIN / VETERINARIAN / KEEPER`

**POST** `/animals`

```json
{
  "name": "Max",
  "age": 3,
  "gender": "M",
  "type": "CANINE",
  "nit": 5555555555,
  "otherTypeAnimals": null,
  "userDto": {
    "username": "steven developer",
    "email": "steven@correo.com",
    "nit": 1234567890,
    "document": "CC",
    "gender": "MALE"
  }
}
```

> `userDto` es el objeto completo del propietario del animal.  
> Si `type` es `OTHER`, completa `otherTypeAnimals` con la especie.

**Respuesta esperada** `201 Created`:

```json
{
  "name": "Max",
  "age": 3,
  "gender": "M",
  "type": "CANINE",
  "nit": 5555555555,
  "otherTypeAnimals": null,
  "userDto": { ... }
}
```

---

### Paso 7 — Crear historia clínica `ADMIN / VETERINARIAN`

La historia debe existir antes de crear la cita, ya que la cita puede referenciarla.

**POST** `/history`

```json
{
  "description": "Paciente sin antecedentes previos. Primera consulta de control general.",
  "animalId": 5555555555,
  "citationIds": []
}
```

> `animalId` es el **nit del animal**.  
> `citationIds` puede ir vacío `[]` en la primera creación.

**Respuesta esperada** `201 Created`:

```json
{
  "description": "Paciente sin antecedentes previos. Primera consulta de control general.",
  "animalId": 5555555555,
  "citationIds": []
}
```

> Guarda el `id` del historial que retorna la respuesta — lo necesitas en el Paso 8.

---

### Paso 8 — Agendar cita `ADMIN / VETERINARIAN`

El `{nit}` en la URL es el **nit del veterinario** que atiende.

**POST** `/citation/newCitation/9876543210`

```json
{
  "reason": "Control general y vacunación anual",
  "state": "PROGRAMADA",
  "note": "El animal presenta buen estado general. Traer carnet de vacunas.",
  "timeDate": "2026-06-20 09:00",
  "animalIds": [5555555555],
  "historyAnimalsId": 1
}
```

> `timeDate` formato: `"yyyy-MM-dd HH:mm"` — debe ser fecha futura o presente.  
> `animalIds` es un array de **nits** de animales (no IDs internos).  
> `historyAnimalsId` es el `id` de la historia creada en el Paso 7.

**Respuesta esperada** `201 Created`:

```json
{
  "codeUnique": 482931,
  "reason": "Control general y vacunación anual",
  "state": "PROGRAMADA",
  "note": "El animal presenta buen estado general. Traer carnet de vacunas.",
  "timeDate": "2026-06-20 09:00",
  "animalIds": [5555555555],
  "historyAnimalsId": 1
}
```

> Guarda el `id` de la cita — lo necesitas en el Paso 9.

---

### Paso 9 — Generar factura `ADMIN / MANAGER`

**POST** `/Invoice`

```json
{
  "paymentMethod": "TARJETA_DEBITO",
  "subTotal": 80000.0,
  "iva": 15200.0,
  "total": 95200.0,
  "invoiceStatus": "PENDING",
  "detailsInvoices": [
    {
      "price": 50000.0,
      "description": "Consulta médica general"
    },
    {
      "price": 30000.0,
      "description": "Vacuna antirrábica"
    }
  ],
  "userId": 1,
  "citationId": 1
}
```

> `userId` es el `id` interno de la BD del usuario (lo retorna la respuesta del Paso 4, no el nit).  
> `citationId` es el `id` interno de la cita creada en el Paso 8.

**Respuesta esperada** `201 Created`:

```json
{
  "paymentMethod": "TARJETA_DEBITO",
  "subTotal": 80000.00,
  "iva": 15200.00,
  "total": 95200.00,
  "invoiceStatus": "PENDING",
  "detailsInvoices": [ ... ],
  "userId": 1,
  "citationId": 1
}
```

---

### Paso 10 — Procesar pago `ADMIN / MANAGER`

**PUT** `/Invoice/processPayment/{code}?code=<codigoFactura>`

```json
"PAID"
```

> El body es directamente el valor del enum como string. `Content-Type: application/json`.

**Respuesta esperada** `200 OK`:

```json
{
  "invoiceStatus": "PAID"
}
```

---

### Consultas útiles de verificación

```
GET /animals?page=0&size=10&sort=name,asc       → Lista paginada de animales
GET /user/email?email=steven@correo.com          → Buscar usuario por email
GET /user/nit?nit=1234567890                     → Buscar usuario por nit
GET /citation/animalsId/5555555555               → Cita por nit del animal
GET /history/nit/5555555555?nit=5555555555       → Historial por nit del animal
GET /Invoice/code/{code}?code=<codigoFactura>    → Factura por código
```

---

## Referencia rápida de enums

| Enum            | Valores válidos                                                                               |
| --------------- | --------------------------------------------------------------------------------------------- |
| `TypeDocument`  | `CC`, `CE`, `PASSPORT`, `NIT`, `TI`, `PEP`                                                    |
| `UserGender`    | `MALE`, `FEMALE`, `OTHER`                                                                     |
| `TypeRole`      | `ROLE_ADMIN`, `ROLE_VETERINARIAN`, `ROLE_KEEPER`, `ROLE_MANAGER`, `ROLE_AUDITOR`, `ROLE_USER` |
| `AnimalGender`  | `M`, `F`                                                                                      |
| `TypeAnimals`   | `CANINE`, `FELINE`, `EQUINE`, `BOVINE`, `BIRD`, `RODENT`, `REPTILE`, `OTHER`                  |
| `TypeState`     | `PROGRAMADA`, `COMPLETADA`, `CANCELADA`, `EN_PROCESO`                                         |
| `InvoiceStatus` | `PENDING`, `PAID`, `CANCELLED`, `OVERDUE`                                                     |
| `PaymentMethod` | `EFECTIVO`, `TARJETA_DEBITO`, `TRANSFERENCIA`                                                 |

---

## Estado del proyecto

| Módulo            | Estado         |
| ----------------- | -------------- |
| Autenticación     | ✅ Completo    |
| Usuarios / Roles  | ✅ Completo    |
| Animales          | ✅ Completo    |
| Veterinarios      | ✅ Completo    |
| Citas             | ✅ Completo    |
| Historial Clínico | ✅ Completo    |
| Facturación       | ✅ Completo    |
| Tests unitarios   | 🔄 En progreso |

---

## Autor

**Steven** — Java Backend Developer  
[GitHub](https://github.com/tu-usuario) · [LinkedIn](https://linkedin.com/in/tu-usuario)
