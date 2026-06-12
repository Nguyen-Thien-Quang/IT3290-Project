# Lich Su Don Hang API Specification

## Basic Information

* **API Name:** Get Order History
* **Description:** Retrieves the history of orders associated with the logged-in user. The results vary based on whether the user is a Customer, Shop Manager, or Shipper.
* **HTTP Method:** GET
* **URL Path:** `/api/order/history`
* **Example URL Request:** `/api/order/history`

## Authentication

* **Login Required:** Yes
* **Role:** `Khách hàng`, `Cửa hàng`, `Shipper`
* **Session Requirement:** Requires an active session with a `user` object and a role-specific ID (`customerId`, `shopId`, or `shipperId`).

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
      "idDonHang": 1,
      "idGioHang": 15,
      "idKhachHang": 5,
      "idShipper": 3,
      "idVoucher": null,
      "thoiGianDat": "Jun 01, 2026, 12:00:00 PM",
      "trangThai": "Đã giao",
      "tongTien": 120000.0,
      "phuongThucThanhToan": "Chuyển khoản"
    }
  ]
}
```

| Field | Type | Description |
| ----- | ---- | ----------- |
| `success` | boolean | Indicates if the request was successful. |
| `data` | Array | List of `DonHang` objects filtered by user role. |

### Role-Based Data Filtering

The content of the `data` array is context-sensitive based on the authenticated user's role:

| Role | Filtering Logic |
| ---- | -------------- |
| **Khách hàng** (Customer) | Returns only orders created by the logged-in customer (matching `idKhachHang`). |
| **Cửa hàng** (Shop) | Returns all orders that contain at least one food item belonging to the logged-in shop (matching `idCuaHang`). |
| **Shipper** | Returns all orders that have been accepted or delivered by the logged-in shipper (matching `idShipper`). |

### Error Responses

#### 401 Unauthorized
Returned when the user is not logged in.

**HTTP Status:** 401 Unauthorized
```json
{
  "success": false,
  "message": "User not authenticated. Please login."
}
```

#### 403 Forbidden
Returned when the user's role does not have access to order history (e.g., Admin).

**HTTP Status:** 403 Forbidden
```json
{
  "success": false,
  "message": "Access denied: Order history is not available for this role."
}
```

#### 500 Internal Server Error
Returned when the role-specific ID is missing from the session or a database error occurs.

**HTTP Status:** 500 Internal Server Error
```json
{
  "success": false,
  "message": "Customer ID not found in session."
}
```

## Business Rules

* **Customers:** See their own past and present orders.
* **Shop Managers:** See all orders containing items from their shop.
* **Shippers:** See all orders they have delivered or are currently delivering.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
