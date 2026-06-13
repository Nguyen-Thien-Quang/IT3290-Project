# Food Search API Specification

## 1. Search Food by Name

* **API Name:** Search Food
* **Description:** Searches for food items whose names contain the provided keyword. Only returns items that are currently in stock ('Còn hàng').
* **HTTP Method:** GET
* **URL Path:** `/api/food/search`
* **Example URL Request:** `/api/food/search?keyword=phở`

### Authentication
* **Login Required:** No (Public API)
* **Role:** Any

### Request Parameters

#### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `keyword` | string | No | The search term. If omitted, returns all available food items. |

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "keyword": "phở",
  "count": 1,
  "data": [
    {
      "idMonAn": 10,
      "idCuaHang": 3,
      "idLoai": 1,
      "tenMon": "Phở Bò",
      "trangThai": "Còn hàng",
      "gia": 75000.0,
      "img": "pho_bo.jpg"
    }
  ]
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the search was successful. |
| `keyword` | string | The keyword used for the search. |
| `count` | int | Number of food items found. |
| `data` | array | List of food objects matching the criteria. |

---

## Business Rules
* **Status Filter:** Only food items with status `Còn hàng` are returned.
* **Partial Match:** The search is case-insensitive and supports partial matches (e.g., "bún" matches "Bún chả").

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`
