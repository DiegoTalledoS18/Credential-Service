# Credential Service

### Requisitos
- Docker y Docker Compose
- Java 17 (para compilar localmente)

### Cómo ejecutar
1. Levantar todo: `docker compose up --build`

### Ejemplos de Curl

* **Registro de Usuario**
  `POST http://localhost:8080/api/v1/auth/register`
    ```json
    {
        "email": "correo@gmail.com",
        "password": "password"
    }
    ```

* **Login**
  `POST http://localhost:8080/api/v1/auth/login`
    ```json
    {
        "email": "correo@gmail.com",
        "password": "password"
    }
    ```

---

### Gestión de Credenciales (Usuarios)

* **Crear Credencial** (Devuelve el ID generado)
  `POST http://localhost:8080/api/v1/credentials`
    ```json
    {
        "type": "EPA_608",
        "issuer": "Department of Licensing",
        "licenseNumber": "EPA-2026-XYZ",
        "expiryDate": "2026-12-31T23:59:59Z"
    }
    ```

* **Listar Credenciales** (Paginación por Cursor)
  `GET http://localhost:8080/api/v1/credentials?userId=[id_del_usuario]&size=2`

* **Listar con Cursor** (Siguiente página)
  `GET http://localhost:8080/api/v1/credentials?userId=[id_del_usuario]&size=2&cursor=2`

* **Filtrar por Tipo**
  `GET http://localhost:8080/api/v1/credentials?userId=[id_del_usuario]&size=2&type=HVAC_LICENSE`

* **Obtener Detalle**
  `GET http://localhost:8080/api/v1/credentials/[id_de_la_credencial]`

* **Borrado Lógico (Soft Delete)**
  `DELETE http://localhost:8080/api/v1/credentials/[id_de_la_credencial]`
  *Nota: Solo permitido para estados PENDING o REJECTED.*

---

### ⚡ Panel de Administración

* **Aprobar Credencial**
  `PUT http://localhost:8080/api/v1/admin/credentials/[id_de_la_credencial]/status`
    ```json
    {
        "status": "APPROVED"
    }
    ```

---

## Lógica de Expiración (Scheduled Job)

1.  **Expiración Estándar:** Si una licencia `APPROVED` llega a su fecha de expiración, cambia a `EXPIRED`.
2.  **Periodo de Gracia:** Si existe una renovación en estado `PENDING` para el mismo usuario y tipo de licencia, la licencia antigua se mantiene activa para evitar interrupciones.



## Pruebas del Sistema

Se implementaron tests de integración para asegurar el cumplimiento de las reglas de negocio:
* **Caso A:** Validación de expiración inmediata sin renovación previa.
* **Caso B:** Validación del mantenimiento de estado durante el periodo de gracia.