# Don Hang Hien Tai API Specification

## 1. Get Pending Orders

* **Description:** Retrieves all orders currently in 'Chờ xác nhận' (Pending) status. This is primarily used by shippers to find available deliveries.
* **HTTP Method:** GET
* **URL Path:** `/api/order`
* **Example URL Request:** `/api/order`

### Request Parameters
None.

### Success Response
**HTTP Status:** 200 OK
```json
{
  "success": true,
  "data": [...],
  "count": 1
}
```

## 2. Accept Order

* **Description:** Allows a shipper to accept a pending order. Changes status to 'Đang giao'.
* **HTTP Method:** POST
* **URL Path:** `/api/order`
* **Example URL Request:** `/api/order?id=10`

### Request Parameters

| Parameter | Type | In | Description |
| --------- | ---- | -- | ----------- |
| `id` | int | Query | ID of the order to accept. |

### Success Response

**HTTP Status:** 200 OK

**Example Response:**
```json
{
  "success": true,
  "message": "Order accepted successfully. Status changed to 'Đang giao'"
}
```

### Error Responses

#### 400 Bad Request
Returned if `id` is missing, invalid, or the order cannot be accepted (e.g., already taken).
```json
{
  "success": false,
  "message": "Missing order ID"
}
```

#### 401 Unauthorized
Returned if not logged in as a Shipper.

#### 403 Forbidden
Returned if the shipper profile is not found for the logged-in account.

## Authentication

* **Login Required:** Yes
* **Role:** `Shipper`
* **Session Requirement:** Requires an active session with a `user` (TaiKhoan) object.

## Response Field Definitions (GET)

* Only orders with status `Chờ xác nhận` (Pending) are returned.
* Only users with the `Shipper` role can access this endpoint.

## Global API Conventions

### Content Type
`application/json`

### Character Encoding
`UTF-8`

### Session Handling
`Cookie: JSESSIONID`
