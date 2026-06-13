# Active Order Management (Huy Don Hang) API Specification

## 1. Get Active Orders

* **API Name:** Get Active Orders
* **Description:** Retrieves all orders for the logged-in customer that are currently in 'Chờ xác nhận' (Pending) or 'Đang giao' (Shipping) status.
* **HTTP Method:** GET
* **URL Path:** `/api/order/customer`
* **Example URL Request:** `/api/order/customer`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng` (Customer)
* **Session Requirement:** Requires an active session with `user` and `customerId`.

### Request Parameters
None.

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "count": 1,
  "data": [
    {
      "idDonHang": 105,
      "idGioHang": 25,
      "idKhachHang": 5,
      "idShipper": null,
      "idVoucher": 1,
      "thoiGianDat": "2023-10-27 10:00:00.0",
      "trangThai": "Chờ xác nhận",
      "tongTien": 150000.0,
      "phuongThucThanhToan": "Tiền mặt"
    }
  ]
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the request was successful. |
| `count` | int | Total number of active orders returned. |
| `data` | array | List of active order objects. |

---

## 2. Cancel Order

* **API Name:** Cancel Pending Order
* **Description:** Allows a customer to cancel an order that is still in 'Chờ xác nhận' (Pending) status.
* **HTTP Method:** POST
* **URL Path:** `/api/order/customer`
* **Example URL Request:** `/api/order/customer?id=105`

### Authentication
* **Login Required:** Yes
* **Role:** `Khách hàng`

### Request Parameters

#### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| `id` | int | Yes | ID of the order to cancel. |

### Success Response
**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "message": "Order cancelled successfully"
}
```

### Error Responses

#### 400 Bad Request
Returned if the `id` is missing, invalid, or if the order cannot be cancelled (e.g., already accepted by a shipper or already delivered).
```json
{
  "success": false,
  "message": "Could not cancel order. It may have already been accepted by a shipper or does not exist."
}
```

---

## Business Rules

* **Cancellation Window:** Orders can ONLY be cancelled if their status is 'Chờ xác nhận'. Once a shipper accepts the order ('Đang giao'), it can no longer be cancelled by the customer.
* **Ownership:** A customer can only view or cancel their own orders.
* **Status Change:** Cancelling an order updates its status to 'Đã hủy'.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
