# Tim Cua Hang API Specification

## Basic Information

* **API Name:** Search Shops
* **Description:** Searches for shops whose names contain the provided keyword. If no keyword is provided, it may return all shops or an empty list depending on the implementation.
* **HTTP Method:** GET
* **URL Path:** `/api/shop/search`
* **Example URL Request:** `/api/shop/search?keyword=phở`

## Authentication

* **Login Required:** No (Publicly accessible search)
* **Role:** Any

## Request Parameters

### Path Parameters
None.

### Query Parameters

| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `keyword` | String | No | The search string to match against shop names. Defaults to an empty string if omitted. |

### Request Body
None.

## Response

### Success Response

**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "keyword": "phở",
  "data": [
    {
      "idCuaHang": 1,
      "idTaiKhoan": 5,
      "tenCuaHang": "Phở Gia Truyền",
      "diaChi": "123 Đường Láng, Hà Nội",
      "sdt": "0987654321",
      "doanhThu": 15000000.0
    }
  ],
  "count": 1
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the request was successful. |
| `keyword` | String | The keyword used for the search. |
| `data` | Array | List of `CuaHang` objects that matched the search criteria. |
| `count` | int | Total number of matching shops returned. |

### Error Responses

#### 500 Internal Server Error
Returned when a database error or unexpected system failure occurs.

**HTTP Status:** 500 Internal Server Error
```json
{
  "success": false,
  "message": "An error occurred during shop search: [Error details]"
}
```

## Business Rules

* The search is case-insensitive and matches any part of the shop name (handled by `CuaHangDAO`).
* The `keyword` is trimmed of leading and trailing whitespace before searching.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`
