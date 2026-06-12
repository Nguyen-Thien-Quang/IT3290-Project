# Shopping Cart (Gio Hang) API Specification

## 1. Get Current Cart

* **API Name:** Get Cart Details
* **Description:** Retrieves the active shopping cart and all items within it for the logged-in customer.
* **HTTP Method:** GET
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng` (Customer)
* **Session Requirement:** Requires an active session with `customerId`.

### Request Parameters
None.

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "cart": {
    "idGioHang": 25,
    "idKhachHang": 5,
    "trangThai": "Đang tạo",
    "tongTien": 150000.0
  },
  "items": [
    {
      "idGioHang": 25,
      "idMonAn": 10,
      "soLuong": 2
    }
  ]
}
```

---

## 2. Add Item to Cart

* **API Name:** Add to Cart
* **Description:** Adds a new item to the cart or increments the quantity if it already exists.
* **HTTP Method:** POST
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng`

### Request Body
**Example Request:**
```json
{
  "monAnId": 10,
  "quantity": 1
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `monAnId` | int | Yes | ID of the food item to add. |
| `quantity` | int | Yes | Amount to add. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Item added to cart"
}
```

---

## 3. Update Item Quantity

* **API Name:** Update Cart Quantity
* **Description:** Updates the absolute quantity of a specific item in the active cart.
* **HTTP Method:** PUT
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng`

### Request Body
**Example Request:**
```json
{
  "monAnId": 10,
  "quantity": 5
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `monAnId` | int | Yes | ID of the food item to update. |
| `quantity` | int | Yes | The new absolute quantity. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Quantity updated"
}
```

---

## 4. Remove Item from Cart

* **API Name:** Remove from Cart
* **Description:** Completely removes a food item from the active cart.
* **HTTP Method:** DELETE
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart?monAnId=10`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng`

### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `monAnId` | int | Yes | ID of the food item to remove. |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Item removed"
}
```

---

## 5. Checkout (Place Order)

* **API Name:** Checkout Cart
* **Description:** Finalizes the active cart and creates a corresponding order.
* **HTTP Method:** POST
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart?action=order`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng`

### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `action` | string | Yes | Must be `order` to trigger checkout logic. |

### Request Body
**Example Request:**
```json
{
  "phuongThuc": "Tiền mặt",
  "voucherId": 1
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `phuongThuc` | string | Yes | Payment method (e.g., "Tiền mặt", "Chuyển khoản"). |
| `voucherId` | int | No | ID of the voucher to apply (can be null). |

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "message": "Order placed successfully"
}
```

---

## Business Rules

* **Single Active Cart:** A user can only have one cart with status `Đang tạo` (Creating) at a time.
* **Checkout Dependency:** Checkout requires an active cart and a valid payment method.
* **Automatic Cart Creation:** Adding an item to the cart when no active cart exists will automatically create a new one.
* **Authorization:** Users can only view or modify their own shopping carts.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
