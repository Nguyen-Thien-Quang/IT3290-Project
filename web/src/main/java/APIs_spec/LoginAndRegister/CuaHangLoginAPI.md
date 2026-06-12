# API Specification: Store Login

## Basic Information

* **API Name**: Store Login
* **Description**: Authenticate a shop manager account using email and password. Upon successful authentication, a session is created containing the user's account information and their specific shop ID.
* **HTTP Method**: POST
* **URL Path**: `/api/shop/login`
* **Example URL Request**: `http://localhost:8080/OnlineFoodWeb/api/shop/login`

---

## Authentication

* **Login Required**: No
* **Required Role(s)**: None (Endpoint is used to establish authentication)
* **Session Requirements**: Creates a new session or updates existing session upon success. Sets `user` (TaiKhoan object) and `shopId` (Integer) in the session.

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
  "password": "yourpassword123"
}
```

| Field    | Type   | Required | Description                        |
| -------- | ------ | -------- | ---------------------------------- |
| email    | String | Yes      | The unique email address of the store manager. |
| password | String | Yes      | The plain-text password of the store manager. |

---

## Response

### Success Response

**HTTP Status Code**: 200 OK

**Example Response**:
```json
{
  "success": true,
  "message": "Login successful",
  "role": "Cửa hàng",
  "email": "shop@example.com"
}
```

| Field   | Type    | Description                                      |
| ------- | ------- | ------------------------------------------------ |
| success | Boolean | Indicates if the login was successful (`true`). |
| message | String  | Confirmation message.                            |
| role    | String  | The role assigned to the account ("Cửa hàng").  |
| email   | String  | The email of the authenticated user.             |

---

## Business Rules

*   **Role Validation**: Only accounts with the role `Cửa hàng` are permitted to log in via this endpoint.
*   **Password Security**: Passwords are hashed using SHA-256 on the backend before comparison.
*   **Session Management**:
    *   Stores `user` object (of type `TaiKhoan`) in the session.
    *   Stores `shopId` (the primary key from the `CUAHANG` table) in the session for use in subsequent requests.

---

## Global API Conventions

*   **Base URL**: `/api`
*   **Content Type**: `application/json`
*   **Character Encoding**: `UTF-8`
*   **Session Handling**: Standard Jakarta EE session management via `JSESSIONID` cookie.
