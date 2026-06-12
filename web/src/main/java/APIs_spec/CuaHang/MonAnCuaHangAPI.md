# MonAn Cua Hang API Specification

## Basic Information

* **API Name:** Get Shop Menu Items
* **Description:** Retrieves all food items belonging to a specific shop.
* **HTTP Method:** GET
* **URL Path:** `/api/shop/foods`
* **Example URL Request:** `/api/shop/foods?id=1`

## Authentication

* **Login Required:** No (Publicly accessible menu)
* **Role:** Any

## Request Parameters

### Path Parameters
None.

### Query Parameters

| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `id` | int | Yes | The unique identifier (ID) of the shop whose menu is being requested. |

### Request Body
None.

## Response

### Success Response

**HTTP Status:** 200 OK

**Example Response:**
```json
[
  {
    "idMonAn": 101,
    "idCuaHang": 1,
    "idLoai": 2,
    "tenMon": "Bún Chả Hà Nội",
    "trangThai": "con_ban",
    "gia": 45000.0,
    "img": "assets/images/bun_cha.jpg"
  },
  {
    "idMonAn": 102,
    "idCuaHang": 1,
    "idLoai": 2,
    "tenMon": "Nem Rán",
    "trangThai": "het_hang",
    "gia": 10000.0,
    "img": "assets/images/nem_ran.jpg"
  }
]
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `idMonAn` | int | Unique identifier for the food item. |
| `idCuaHang` | int | ID of the shop that owns this item. |
| `idLoai` | int | ID of the food category/type. |
| `tenMon` | String | Name of the food item. |
| `trangThai` | String | Status: `con_ban` (Available), `het_hang` (Out of stock), `ngung_kinh_doanh` (Discontinued). |
| `gia` | double | Price of the food item. |
| `img` | String | Path or URL to the food's image. |

### Error Responses

#### 400 Bad Request
Returned when the `id` parameter is missing or invalid.

**HTTP Status:** 400 Bad Request
```json
{
  "success": false,
  "message": "Missing mandatory 'id' parameter."
}
```
OR
```json
{
  "success": false,
  "message": "Invalid shop ID format."
}
```

## Business Rules

* Returns an empty array `[]` if the shop exists but has no menu items or if the shop ID does not exist.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`
