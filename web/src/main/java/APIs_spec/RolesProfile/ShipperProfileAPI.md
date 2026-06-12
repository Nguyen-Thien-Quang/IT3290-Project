# Shipper Profile API Specification

## Basic Information

* **API Name:** Get Shipper Profile
* **Description:** Retrieves the profile details of the currently logged-in shipper.
* **HTTP Method:** GET
* **URL Path:** `/api/shipper/profile`
* **Example URL Request:** `/api/shipper/profile`

## Authentication

* **Login Required:** Yes
* **Role:** `Shipper`
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
  "idShipper": 1,
  "idTaiKhoan": 15,
  "hoTen": "Trần Văn B",
  "ngaySinh": "1992-08-15",
  "sdt": "0912345678"
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `idShipper` | int | Unique identifier for the shipper. |
| `idTaiKhoan` | int | ID of the linked user account. |
| `hoTen` | String | Full name of the shipper. |
| `ngaySinh` | Date | Date of birth (YYYY-MM-DD). |
| `sdt` | String | Contact phone number of the shipper. |

### Error Responses

#### 401 Unauthorized
Returned when the user is not logged in or does not have the "Shipper" role.

**HTTP Status:** 401 Unauthorized
```json
{
  "error": "Unauthorized or not a shipper account"
}
```

#### 404 Not Found
Returned when the shipper details cannot be found for the authenticated account.

**HTTP Status:** 404 Not Found
```json
{
  "error": "Shipper details not found"
}
```

## Business Rules

* The API strictly enforces that the session user must have the role `Shipper`.
* The data returned is specific to the account ID stored in the current session.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
