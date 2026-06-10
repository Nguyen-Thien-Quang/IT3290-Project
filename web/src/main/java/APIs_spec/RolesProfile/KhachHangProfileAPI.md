# KhachHang Profile API Specification

## Basic Information

* **API Name:** Get Customer Profile
* **Description:** Retrieves the profile details of the currently logged-in customer.
* **HTTP Method:** GET
* **URL Path:** `/api/customer/profile`
* **Example URL Request:** `/api/customer/profile`

## Authentication

* **Login Required:** Yes
* **Role:** `Khách hàng` (Customer)
* **Session Requirement:** Requires an active session with a `user` attribute of type `TaiKhoan`.

## Request Parameters

### Path Parameters
None.

### Query Parameters
None.

### Request Body
None.

## Response

### Success Response

**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "idKhachHang": 1,
  "idTaiKhoan": 10,
  "hoTen": "Nguyễn Văn A",
  "ngaySinh": "1995-05-20",
  "diaChi": "456 Đường ABC, Quận 1, TP.HCM",
  "sdt": "0123456789"
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `idKhachHang` | int | Unique identifier for the customer. |
| `idTaiKhoan` | int | ID of the linked user account. |
| `hoTen` | String | Full name of the customer. |
| `ngaySinh` | Date | Date of birth (YYYY-MM-DD). |
| `diaChi` | String | Physical address of the customer. |
| `sdt` | String | Contact phone number of the customer. |

### Error Responses

#### 401 Unauthorized
Returned when the user is not logged in or does not have the "Khách hàng" role.

**HTTP Status:** 401 Unauthorized
```json
{
  "error": "Unauthorized or not a customer account"
}
```

#### 404 Not Found
Returned when the customer details cannot be found for the authenticated account.

**HTTP Status:** 404 Not Found
```json
{
  "error": "Customer details not found"
}
```

## Business Rules

* The API strictly enforces that the session user must have the role `Khách hàng`.
* The data returned is specific to the account ID stored in the current session.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
