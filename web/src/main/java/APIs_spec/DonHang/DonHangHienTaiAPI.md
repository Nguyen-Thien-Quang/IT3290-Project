# Don Hang Hien Tai API Specification

## Basic Information

* **API Name:** Get Pending Orders
* **Description:** Retrieves all orders currently in 'Chờ xác nhận' (Pending) status. This is primarily used by shippers to find available deliveries.
* **HTTP Method:** GET
* **URL Path:** `/api/order`
* **Example URL Request:** `/api/order`

## Authentication

* **Login Required:** Yes
* **Role:** `Shipper`
* **Session Requirement:** Requires an active session with a `user` (TaiKhoan) object.

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
  "success": true,
  "data": [
    {
      "idDonHang": 10,
      "idGioHang": 25,
      "idKhachHang": 5,
      "idShipper": null,
      "idVoucher": null,
      "thoiGianDat": "Jun 10, 2026, 10:30:00 AM",
      "trangThai": "Chờ xác nhận",
      "tongTien": 155000.0,
      "phuongThucThanhToan": "Tiền mặt"
    }
  ],
  "count": 1
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the request was successful. |
| `data` | Array | List of pending `DonHang` objects. |
| `count` | int | Total number of pending orders returned. |

### Error Responses

#### 401 Unauthorized
Returned when the user is not logged in or is not a Shipper.

**HTTP Status:** 401 Unauthorized
```json
{
  "success": false,
  "message": "Unauthorized: Please login as a Shipper"
}
```

#### 500 Internal Server Error
Returned when a system or database error occurs.

**HTTP Status:** 500 Internal Server Error
```json
{
  "success": false,
  "message": "System error: [Error details]"
}
```

## Business Rules

* Only orders with status `Chờ xác nhận` (Pending) are returned.
* Only users with the `Shipper` role can access this endpoint.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
