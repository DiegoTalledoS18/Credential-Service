# Credential Service

### Requirements
- Docker and Docker Compose
- Java 17 (for local compilation)

### How to Run
1. Bring everything up: `docker compose up --build`

### Curl Examples

* **User Registration**  
  `POST http://localhost:8080/api/v1/auth/register`
    ```json
    {
        "email": "email@gmail.com",
        "password": "password"
    }
    ```

* **Login**  
  `POST http://localhost:8080/api/v1/auth/login`
    ```json
    {
        "email": "email@gmail.com",
        "password": "password"
    }
    ```

---

### Credential Management (Users)

* **Create Credential** (Returns generated ID)  
  `POST http://localhost:8080/api/v1/credentials`
    ```json
    {
        "type": "EPA_608",
        "issuer": "Department of Licensing",
        "licenseNumber": "EPA-2026-XYZ",
        "expiryDate": "2026-12-31T23:59:59Z"
    }
    ```
> **Note:** Although the system has user registration and login, authentication is not required to create a credential. Therefore, the system assigns a random registered user by default.

* **List Credentials** (Cursor-based Pagination)  
  `GET http://localhost:8080/api/v1/credentials?userId=[user_id]&size=2`

* **List with Cursor** (Next Page)  
  `GET http://localhost:8080/api/v1/credentials?userId=[user_id]&size=2&cursor=2`

* **Filter by Type**  
  `GET http://localhost:8080/api/v1/credentials?userId=[user_id]&size=2&type=HVAC_LICENSE`

* **Get Details**  
  `GET http://localhost:8080/api/v1/credentials/[credential_id]`

* **Soft Delete**  
  `DELETE http://localhost:8080/api/v1/credentials/[credential_id]`
  *Note: Only allowed for PENDING or REJECTED states.*

---

### ⚡ Admin Panel

* **Approve Credential**  
  `PUT http://localhost:8080/api/v1/admin/credentials/[credential_id]/status`
    ```json
    {
        "status": "APPROVED"
    }
    ```

---

## Expiration Logic (Scheduled Job)

1. **Standard Expiration:** If an `APPROVED` license reaches its expiration date, it changes to `EXPIRED`.
2. **Grace Period:** If there is a renewal in `PENDING` status for the same user and license type, the old license remains active to avoid interruptions.

---

## System Tests

Integration tests were implemented to ensure business rules compliance:
* **Case A:** Immediate expiration validation without prior renewal.
* **Case B:** Validation of state maintenance during the grace period.
