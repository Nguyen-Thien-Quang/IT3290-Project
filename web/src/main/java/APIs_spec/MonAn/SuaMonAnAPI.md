# Menu Management (Sua Mon An) API Specification

## 1. Add New Food Item

* **API Name:** Add Food
* **Description:** Adds a new food item to the menu of the logged-in store.
* **HTTP Method:** POST
* **URL Path:** `/api/food`
* **Example URL Request:** `/api/food`

### Authentication
* **Login Required:** Yes
* **Role:** `Cửa hàng` (Shop)
* **Session Requirement:** Requires an active session with `user` and `storeId`.

### Request Body
**Example Request:**
```json
{
  "tenMon": "Cơm Tấm Sườn",
  "idLoai": 2,
  "gia": 45000.0,
  "img": "com_tam.jpg"
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `tenMon` | string | Yes | Name of the food item. |
| `idLoai` | int | Yes | ID of the food category. |
| `gia` | double | Yes | Price of the food item. |
| `img` | string | No | Image filename or URL. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Food item added successfully"
}
```

---

## 2. Update Food Item

* **API Name:** Update Food
* **Description:** Updates the details of an existing food item.
* **HTTP Method:** PUT
* **URL Path:** `/api/food`
* **Example URL Request:** `/api/food`

### Authentication
* **Login Required:** Yes
* **Role:** `Cửa hàng`

### Request Body
**Example Request:**
```json
{
  "idMonAn": 105,
  "tenMon": "Cơm Tấm Sườn Bì",
  "gia": 50000.0,
  "trangThai": "Hết hàng",
  "img": "com_tam_update.jpg"
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `idMonAn` | int | Yes | ID of the item to update. |
| `tenMon` | string | Yes | New name. |
| `gia` | double | Yes | New price. |
| `trangThai` | string | Yes | New status ('Còn hàng' or 'Hết hàng'). |
| `img` | string | No | New image link. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Food item updated successfully"
}
```

---

## 3. Delete Food Item

* **API Name:** Delete Food
* **Description:** Removes a food item from the store's menu.
* **HTTP Method:** DELETE
* **URL Path:** `/api/food`
* **Example URL Request:** `/api/food?id=105`

### Authentication
* **Login Required:** Yes
* **Role:** `Cửa hàng`

### Request Parameters

#### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `id` | int | Yes | ID of the food item to delete. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Food item deleted successfully"
}
```

### Error Responses
**HTTP Status:** 400 Bad Request
```json
{
  "success": false,
  "message": "Failed to delete item. It may have existing orders or you do not have permission."
}
```

---

## Business Rules
* **Ownership:** A store can only modify or delete food items that belong to its own `storeId`.
* **Deletion Constraint:** Items that have been part of past or current orders cannot be deleted (to maintain order history integrity). In such cases, use the Update API to change status to 'Hết hàng'.
* **Default Status:** New items are automatically set to 'Còn hàng' upon creation.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
