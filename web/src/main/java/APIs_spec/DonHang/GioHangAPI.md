# Gio Hang API Specification

## Basic Information

* **API Name:** Shopping Cart Management
* **Description:** Handles all operations related to the user's active shopping cart, including viewing items, adding/editing items, and placing orders.
* **HTTP Method:** GET, POST, DELETE
* **URL Path:** `/api/cart`
* **Example URL Request:** `/api/cart`, `/api/cart?action=edit`

## Authentication

* **Login Required:** Yes
* **Role:** `Khách hàng` (Customer)
* **Session Requirement:** Requires an active session with a `user` object and `customerId`.

## Endpoints

### 1. View Cart Items
Retrieves the list of food items currently in the active cart.

* **Method:** GET
* **URL:** `/api/cart`

**Success Response (200 OK):**
```json
{
  "idGioHang": 25,
  "idKhachHang": 5,
  "trangThai": "Đang tạo",
  "tongTien": 155000.0,
  "items": [
    {
      "idMonAn": 101,
      "tenMon": "Phở Bò",
      "soLuong": 2,
      "gia": 50000.0,
      "img": "assets/images/pho.jpg"
    }
  ]
}
```

---

### 2. Add/Edit Item in Cart
Adds a new item to the cart or updates the quantity of an existing item.

* **Method:** POST
* **URL:** `/api/cart?action=edit`
* **Request Body:**
```json
{
  "monAnId": 101,
  "soLuong": 1
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `monAnId` | int | Yes | ID of the food item to add/update. |
| `soLuong` | int | Yes | Quantity to add (positive to add, negative to reduce, or absolute new quantity depending on implementation). |

---

### 3. Remove Item from Cart
Deletes a specific food item from the active cart.

* **Method:** DELETE
* **URL:** `/api/cart?monAnId=101`

---

### 4. Place Order (Checkout)
Converts the active cart into a confirmed order.

* **Method:** POST
* **URL:** `/api/cart?action=order`
* **Request Body:**
```json
{
  "phuongThucThanhToan": "Tiền mặt",
  "idVoucher": 1
}
```

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `phuongThucThanhToan` | String | Yes | Payment method (e.g., "Tiền mặt", "Chuyển khoản"). |
| `idVoucher` | int | No | ID of the voucher to apply. |

## Business Rules

* Only one active cart (status `Đang tạo`) per customer is allowed at a time.
* Total price in the cart is automatically updated via database triggers.
* Placing an order changes the cart status to `Đã đặt` and creates a corresponding entry in the `DONHANG` table.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`
