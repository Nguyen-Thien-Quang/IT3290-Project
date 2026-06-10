# API Specification: Shipper Register

## Basic Information

* **API Name**: Shipper Register
* **Description**: Register a new shipper account. This endpoint creates both a general account (TaiKhoan) and a specific shipper profile (Shipper).
* **HTTP Method**: POST
* **URL Path**: `/api/shipper/register`
* **Example URL Request**: `http://localhost:8080/OnlineFoodWeb/api/shipper/register`

---

## Authentication

* **Login Required**: No
* **Required Role(s)**: None
* **Session Requirements**: None

---

## Request Parameters

### Path Parameters
*None*

### Query Parameters
*None*

### Request Body

**Content-Type**: `application/json`

**Example Body Request**:
```json
{
  "email": "shipper@example.com",
  "password": "yourpassword123",
  "name": "Shipper Full Name",
  "birthday": "1995-05-05",
  "SDT": "0123456789"
}
```

| Field    | Type   | Required | Description                        |
| -------- | ------ | -------- | ---------------------------------- |
| email    | String | Yes      | The unique email address for the account. |
| password | String | Yes      | The plain-text password to be hashed on the backend. |
| name     | String | No       | The full name of the shipper.      |
| birthday | String | No       | The birthday of the shipper (format: `YYYY-MM-DD`). |
| SDT      | String | No       | The contact phone number of the shipper. |

---

## Response

### Success Response

**HTTP Status Code**: 200 OK

**Example Response**:
```json
{
  "success": true,
  "message": "Registration successful",
  "redirect": "/OnlineFoodWeb/login.html"
}
```

| Field    | Type    | Description                                      |
| -------- | ------- | ------------------------------------------------ |
| success  | Boolean | Indicates if the registration was successful.   |
| message  | String  | Confirmation message.                            |
| redirect | String  | URL to the login page for the frontend.          |

---

## Business Rules

*   **Email Uniqueness**: The registration will fail if the provided email is already registered in the system.
*   **Password Security**: The password is hashed using SHA-256 on the backend before being saved to the database.
*   **Date Format**: The `birthday` field must be in `YYYY-MM-DD` format to be correctly parsed by `java.sql.Date.valueOf()`.
*   **Data Persistence**: The process involves adding an entry to the `TAIKHOAN` table with the role `Shipper` and a corresponding entry in the `SHIPPER` table.

---

## Global API Conventions

*   **Base URL**: `/api`
*   **Content Type**: `application/json`
*   **Character Encoding**: `UTF-8`
