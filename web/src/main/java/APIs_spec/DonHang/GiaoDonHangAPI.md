# Giao Don Hang (Delivery Management) API Specification

## 1. Get Active Shipping Orders

* **Description:** Retrieves all orders currently assigned to the logged-in shipper that have the status 'Đang giao' (Shipping).
* **HTTP Method:** GET
* **URL Path:** `/api/order/shipping`
* **Example URL Request:** `/api/order/shipping`

### Authentication
* **Login Required:** Yes
* **Role:** `Shipper`
* **Session Requirement:** Requires `user` and `shipperId` to be present in the session.

### Request Parameters
None.

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "data": [
    {
      "idDonHang": 12,
      "idGioHang": 30,
      "idKhachHang": 5,
      "idShipper": 3,
      "idVoucher": null,
      "thoiGianDat": "Jun 10, 2026, 11:45:00 AM",
      "trangThai": "Đang giao",
      "tongTien": 210000.0,
      "phuongThucThanhToan": "Chuyển khoản"
    }
  ],
  "count": 1
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the request was successful. |
| `data` | Array | List of `DonHang` objects currently being shipped by this shipper. |
| `count` | int | Total number of orders returned. |

### Error Responses
* **401 Unauthorized:** User not logged in.
* **403 Forbidden:** User is not a Shipper.
* **500 Internal Server Error:** System error or missing session data (`shipperId`).

---

## 2. Confirm Delivery Completion

* **Description:** Marks an active shipping order as 'Đã giao' (Delivered).
* **HTTP Method:** POST
* **URL Path:** `/api/order/shipping`
* **Example URL Request:** `/api/order/shipping?id=12`

### Authentication
* **Login Required:** Yes
* **Role:** `Shipper`

### Request Parameters

| Parameter | Type | In | Description |
| --------- | ---- | -- | ----------- |
| `id` | int | Query | ID of the order to confirm as delivered. |

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "message": "Order successfully marked as 'Đã giao' (Delivered)"
}
```

### Error Responses

#### 400 Bad Request
Returned if `id` is missing, invalid, or the order status could not be updated.
```json
{
  "success": false,
  "message": "Missing order ID"
}
```

#### 401 Unauthorized
Returned if not logged in.

#### 403 Forbidden
Returned if the user is not a Shipper.

#### 500 Internal Server Error
Returned on database or system failure.

---

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
