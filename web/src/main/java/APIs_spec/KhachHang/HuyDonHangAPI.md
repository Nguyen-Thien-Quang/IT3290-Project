# API Specification: Customer Active Order Management (Huy Don Hang)

## Basic Information

* **API Name**: Customer Active Order Management
* **Description**: Allows customers to view their active orders (Pending/Shipping) and cancel orders that are still in the 'Pending' status.
* **HTTP Methods**: GET (View), POST (Cancel)
* **URL Path**: `/api/order/customer`
* **Example URL Request**: `http://localhost:8080/OnlineFoodWeb/api/order/customer`

---

## 1. Get Active Orders (GET)

* **Description**: Retrieves all orders for the logged-in customer that are currently in 'Chờ xác nhận' (Pending) or 'Đang giao' (Shipping) status.
* **HTTP Method**: GET
* **URL Path**: `/api/order/customer`

### Authentication
* **Login Required**: Yes
* **Required Role(s)**: `Khách hàng` (Customer)
* **Session Requirements**: Requires an active session with `user` (TaiKhoan object) and `customerId`.

### Request Parameters
*None*

### Response

#### Success Response
**HTTP Status Code**: 200 OK

**Example Response**:
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
      "thoiGianDat": "Oct 27, 2023, 10:00:00 AM",
      "trangThai": "Chờ xác nhận",
      "tongTien": 150000.0,
      "phuongThucThanhToan": "Tiền mặt"
    }
  ]
}
```

| Field   | Type    | Description                                      |
| ------- | ------- | ------------------------------------------------ |
| success | Boolean | Indicates if the request was successful.          |
| count   | Integer | Total number of active orders returned.          |
| data    | Array   | List of active order objects.                    |

---

## 2. Cancel Pending Order (POST)

* **Description**: Allows a customer to cancel an order that is still in 'Chờ xác nhận' (Pending) status.
* **HTTP Method**: POST
* **URL Path**: `/api/order/customer`

### Authentication
* **Login Required**: Yes
* **Required Role(s)**: `Khách hàng` (Customer)

### Request Parameters

#### Path Parameters
*None*

#### Query Parameters
| Name | Type | Required | Description |
| ---- | ---- | -------- | ----------- |
| id   | int  | Yes      | The unique ID of the order to cancel (`idDonHang`). |

#### Request Body
*None*

### Response

#### Success Response
**HTTP Status Code**: 200 OK

**Example Response**:
```json
{
  "success": true,
  "message": "Order cancelled successfully"
}
```

#### Error Responses

**400 Bad Request**
Returned if the `id` is missing, invalid, or the order cannot be cancelled (e.g., already accepted by a shipper).
```json
{
  "success": false,
  "message": "Could not cancel order. It may have already been accepted by a shipper or does not exist."
}
```

**401 Unauthorized**
Returned if the user is not logged in.

**403 Forbidden**
Returned if the logged-in user is not a customer or does not have a `customerId` in their session.

---

## Business Rules

*   **Cancellation Window**: Orders can ONLY be cancelled if their status is 'Chờ xác nhận' (Pending). Once a shipper accepts the order (status changes to 'Đang giao'), the customer can no longer cancel it via this endpoint.
*   **Ownership Verification**: A customer can only view or cancel orders that belong to their own `customerId`.
*   **Status Update**: Successful cancellation changes the order status in the database to 'Đã hủy'.

---

## Global API Conventions

*   **Base URL**: `/api`
*   **Content Type**: `application/json`
*   **Character Encoding**: `UTF-8`
*   **Session Handling**: Standard Jakarta EE session management via `JSESSIONID` cookie.
