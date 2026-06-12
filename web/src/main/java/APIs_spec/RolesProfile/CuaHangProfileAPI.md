# CuaHang Profile API Specification

## Basic Information

* **API Name:** Get Shop Profile
* **Description:** Retrieves the profile details of the currently logged-in shop manager.
* **HTTP Method:** GET
* **URL Path:** `/api/shop/profile`
* **Example URL Request:** `/api/shop/profile`

## Authentication

* **Login Required:** Yes
* **Role:** `Cửa hàng` (Shop Manager)
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
  "idCuaHang": 1,
  "idTaiKhoan": 5,
  "tenCuaHang": "Phở Gia Truyền",
  "diaChi": "123 Đường Láng, Hà Nội",
  "sdt": "0987654321",
  "doanhThu": 15000000.0
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `idCuaHang` | int | Unique identifier for the shop. |
| `idTaiKhoan` | int | ID of the linked user account. |
| `tenCuaHang` | String | Display name of the shop. |
| `diaChi` | String | Physical address of the shop. |
| `sdt` | String | Contact phone number of the shop. |
| `doanhThu` | double | Total accumulated revenue of the shop. |

### Error Responses

#### 401 Unauthorized
Returned when the user is not logged in or does not have the "Cửa hàng" role.

**HTTP Status:** 401 Unauthorized
```json
{
  "error": "Unauthorized or not a shop account"
}
```

#### 404 Not Found
Returned when the shop details cannot be found for the authenticated account.

**HTTP Status:** 404 Not Found
```json
{
  "error": "Shop details not found"
}
```

## Business Rules

* The API strictly enforces that the session user must have the role `Cửa hàng`.
* The data returned is specific to the account ID stored in the current session.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
