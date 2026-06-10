# API Specification: Store Register

## Basic Information

* **API Name**: Store Register
* **Description**: Register a new store account. This endpoint creates both a general account (TaiKhoan) and a specific store profile (CuaHang).
* **HTTP Method**: POST
* **URL Path**: `/api/shop/register`
* **Example URL Request**: `http://localhost:8080/OnlineFoodWeb/api/shop/register`

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
  "email": "shop@example.com",
  "password": "yourpassword123",
  "name": "My Awesome Shop",
  "address": "456 Avenue, City",
  "SDT": "0987654321"
}
```

| Field    | Type   | Required | Description                        |
| -------- | ------ | -------- | ---------------------------------- |
| email    | String | Yes      | The unique email address for the account. |
| password | String | Yes      | The plain-text password to be hashed on the backend. |
| name     | String | No       | The name of the store.             |
| address  | String | No       | The physical address of the store. |
| SDT      | String | No       | The contact phone number of the store. |

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
*   **Data Persistence**: The process involves adding an entry to the `TAIKHOAN` table with the role `Cửa hàng` and a corresponding entry in the `CUAHANG` table.

---

## Global API Conventions

*   **Base URL**: `/api`
*   **Content Type**: `application/json`
*   **Character Encoding**: `UTF-8`
