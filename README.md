# IT3290-Project — Hệ thống quản lý đặt và giao đồ ăn trực tuyến

## Giới thiệu

**IT3290-Project** là website mô phỏng một nền tảng đặt và giao đồ ăn trực tuyến (O2O) hoàn chỉnh, từ tìm kiếm món ăn, đặt hàng, đến quản lý thực đơn và giao vận. Hệ thống được xây dựng bằng **Java (Jakarta EE 10 / Servlet)** theo mô hình **MVC + DAO**, phục vụ 3 nhóm người dùng chính:

| Vai trò | Mô tả |
|---|---|
| 👤 **Khách hàng (Customer)** | Tìm kiếm món ăn/cửa hàng, đặt hàng, theo dõi đơn |
| 🏪 **Cửa hàng (Shop)** | Quản lý thực đơn, nhận đơn, theo dõi doanh thu |
| 🛵 **Shipper** | Tiếp nhận đơn hàng và cập nhật trạng thái giao |

---

## ✨ Chức năng chính

### 👤 Khách hàng (Customer)
- Đăng ký / đăng nhập tài khoản
- Xem & cập nhật thông tin cá nhân (`/api/me/customer`)
- Tìm kiếm món ăn theo từ khóa (`/api/foods?keyword=...`)
- Tìm kiếm cửa hàng theo từ khóa (`/api/shops?keyword=...`)
- Quản lý giỏ hàng: thêm / sửa / xóa món ăn (`/api/cart`, `/api/cart/items`)
- Đặt hàng với **voucher** và lựa chọn **phương thức thanh toán** (`/api/cart/checkout`)
- Xem danh sách đơn hàng (`/api/orders`)
- Xem chi tiết đơn hàng (`/api/orders/{id}`)
- Hủy đơn hàng khi chưa có shipper nhận (`/api/orders/{id}/cancel`)

### 🏪 Cửa hàng (Shop)
- Đăng ký / đăng nhập tài khoản cửa hàng
- Xem thông tin cửa hàng (`/api/me/shop`)
- Quản lý menu món ăn: thêm, sửa, xóa (`/api/me/shop/menu`)
- Xem danh sách đơn hàng của quán (`/api/orders`)
- Xem **doanh thu** trong khoảng thời gian cụ thể (`/api/me/shop/revenue`)
- Xem **món ăn bán chạy nhất** (`/api/me/shop/best-sellers`)

### 🛵 Shipper
- Đăng ký / đăng nhập tài khoản
- Xem thông tin cá nhân (`/api/me/shipper`)
- Xem danh sách đơn hàng chờ xác nhận (`/api/orders?status=pending`)
- Tiếp nhận đơn hàng (`/api/orders/{id}/accept`)
- Cập nhật trạng thái đơn: **Đang giao** → **Đã giao** (`/api/orders/{id}/deliver`)

Trạng thái đơn hàng được quản lý qua các hằng số: `Chờ xác nhận`, `Đang giao`, `Đã giao`, `Đã hủy`.

---

## 🛠 Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 17 |
| Backend | Jakarta EE 10 (Servlet 6.0), JSP/JSTL |
| Database | Microsoft SQL Server |
| Kết nối DB | JDBC + MSSQL JDBC Driver |
| JSON | Google Gson |
| Frontend | HTML5, CSS3 (vanilla), JavaScript (ES6+, Fetch API) |
| Build tool | Maven (WAR package) |
| Runtime | Tomcat 10 (Docker) |
| Dev server | Jetty (jetty-maven-plugin, port 8080) |
| Container | Docker & Docker Compose |
| Kiến trúc | MVC + DAO (Model-View-Controller + Data Access Object) |

---

## 📁 Cấu trúc dự án

```
IT3290-Project
├── database/
│   └── FoodProject.bak          # SQL Server backup (sample data kèm theo)
├── web/                         # Maven webapp
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   ├── context/     # DBContext (jdbc connection)
│           │   ├── controller/  # Servlets (REST-like API, chia theo vai trò)
│           │   │   ├── auth/          # Đăng ký, đăng nhập, đăng xuất
│           │   │   ├── profile/       # Xem/sửa thông tin cá nhân
│           │   │   ├── cart/          # Giỏ hàng & checkout
│           │   │   ├── order/         # Quản lý đơn hàng
│           │   │   ├── food/          # Tìm kiếm & xem món ăn
│           │   │   ├── shop/          # Quản lý cửa hàng & menu
│           │   │   └── utility/       # AuthGuard, HashUtil, JsonResponse
│           │   ├── model/       # POJO models (food, order, user)
│           │   └── dao/         # Data Access Objects
│           └── webapp/
│               ├── index.html / login.html / user_profile.html
│               ├── assets/      # CSS, JS, hình ảnh món ăn
│               └── WEB-INF/web.xml
└── REPORT.md                    # Báo cáo dự án chi tiết
```

### Các gói mã nguồn chính
- `context.DBContext` — quản lý kết nối SQL Server
- `controller/` — các Servlet xử lý request, trả JSON (chia theo `auth`, `profile`, `cart`, `order`, `food`, `shop`, `utility`)
- `model/` — POJO theo 3 nhóm: `user`, `food`, `order` (kèm các class hằng số trạng thái)
- `dao/` — tầng truy cập dữ liệu, tương ứng từng model

---

## 🗄 Cơ sở dữ liệu

File backup SQL Server: **`database/FoodProject.bak`** (bao gồm schema + dữ liệu mẫu). Các bảng chính:

- **TAIKHOAN** — tài khoản & vai trò (Khách hàng, Cửa hàng, Shipper, Admin)
- **KHACHHANG / CUAHANG / SHIPPER** — thông tin chi tiết từng vai trò, liên kết `TAIKHOAN`
- **LOAIMONAN / MONAN** — danh mục và thực đơn
- **GIOHANG / GIOHANG_MONAN** — giỏ hàng tạm thời và các mục chọn
- **DONHANG** — bảng trung tâm quản lý giao dịch
- **VOUCHER** — mã giảm giá

---

## 🔌 Danh sách API chính

### Auth & Profile

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/auth/register` | Đăng ký tài khoản (customer/shop/shipper) |
| POST | `/api/auth/login` | Đăng nhập |
| POST | `/api/auth/logout` | Đăng xuất |
| GET | `/api/me/customer` | Xem thông tin khách hàng |
| PUT | `/api/me/customer` | Cập nhật thông tin khách hàng |
| GET | `/api/me/shop` | Xem thông tin cửa hàng |
| GET | `/api/me/shipper` | Xem thông tin shipper |

### Tìm kiếm (Public)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/foods?keyword=&page=&size=` | Tìm món ăn theo từ khóa |
| GET | `/api/foods/{id}` | Xem chi tiết món ăn |
| GET | `/api/shops?keyword=&page=&size=` | Tìm cửa hàng theo từ khóa |
| GET | `/api/shops/{id}` | Xem chi tiết cửa hàng |
| GET | `/api/shops/menu/{id}` | Xem menu công khai của cửa hàng |

### Giỏ hàng & Đặt hàng (Customer)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/cart` | Xem giỏ hàng hiện tại |
| POST | `/api/cart/items` | Thêm món vào giỏ hàng |
| PUT | `/api/cart/items/{id}` | Cập nhật số lượng món trong giỏ |
| DELETE | `/api/cart/items/{id}` | Xóa món khỏi giỏ hàng |
| POST | `/api/cart/checkout` | Đặt hàng (từ giỏ hàng) |

### Quản lý đơn hàng

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/orders?scope=active` | Danh sách đơn (customer: đang hoạt động) |
| GET | `/api/orders?status=pending` | Danh sách đơn (shipper: chờ xác nhận) |
| GET | `/api/orders/{id}` | Xem chi tiết đơn hàng |
| POST | `/api/orders/{id}/accept` | Shipper nhận đơn |
| POST | `/api/orders/{id}/deliver` | Shipper đánh dấu đã giao |
| POST | `/api/orders/{id}/cancel` | Customer hủy đơn |

### Quản lý cửa hàng (Shop)

| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/api/me/shop/menu` | Xem danh sách món của cửa hàng |
| POST | `/api/me/shop/menu` | Thêm món mới |
| PUT | `/api/me/shop/menu/{id}` | Cập nhật món ăn |
| DELETE | `/api/me/shop/menu/{id}` | Xóa món ăn |
| GET | `/api/me/shop/revenue` | Xem doanh thu theo thời gian |
| GET | `/api/me/shop/best-sellers` | Món ăn bán chạy nhất |

---

## Cài đặt & chạy (Docker)

### Yêu cầu
- Docker & Docker Compose (v2+)

### Các bước

**1. Chạy docker-compose**
```bash
docker compose -f docker/docker-compose.yml up -d
```

Lần đầu sẽ build image (tải Maven, JDK, Tomcat, SQL Server) — mất vài phút.

**2. Khôi phục database** (bên trong container SQL Server)  
Sau khi container `mssql_db` đã chạy (`docker compose -f docker/docker-compose.yml ps` cho thấy status `healthy` hoặc `running`):
```bash
# Vào container SQL Server
docker exec -it mssql_db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'Thang123' -C -Q "RESTORE DATABASE FoodProject FROM DISK = '/var/opt/mssql/backup/FoodProject.bak' WITH MOVE 'FoodProject' TO '/var/opt/mssql/data/FoodProject.mdf', MOVE 'FoodProject_log' TO '/var/opt/mssql/data/FoodProject_log.ldf';"
```
> Lưu ý: `-C` để trust server certificate (SQL Server 2025 dùng encryption mặc định).

**3. Mở trình duyệt**
```
http://localhost:8080/
```
> ứng dụng deploy dưới context path `/` (ROOT.war) trên Tomcat 10, port 8080.

---

### Thông tin kết nối DB (đã cấu hình sẵn trong docker-compose)
- **Host**: `db` (tên service trong docker network) hoặc `localhost` từ máy host
- **Port**: `1433`
- **Database**: `FoodProject`
- **User**: `sa`

Nếu cần đổi mật khẩu, sửa `SA_PASSWORD` trong `docker/docker-compose.yml` và cập nhật tương ứng trong `web/src/main/java/context/DBContext.java` rồi rebuild.

### Dừng & dọn dẹp
```bash
docker compose -f docker/docker-compose.yml down           # dừng container
docker compose -f docker/docker-compose.yml down -v        # dừng + xóa volume data (mất DB)
```
---

## 🔄 Luồng đặt hàng

1. Khách hàng tìm món (`/api/foods`) → thêm vào giỏ hàng (`/api/cart/items`) (hệ thống tự tạo `GIOHANG` khi thêm món đầu tiên).
2. Mỗi khách hàng chỉ có **1 giỏ hàng đang hoạt động** tại một thời điểm.
3. Chọn voucher (nếu có) và phương thức thanh toán → nhấn **Đặt hàng** (`/api/cart/checkout`).
4. Giỏ hàng được lưu trữ (không thể chỉnh sửa) và hệ thống tạo `DONHANG` tương ứng (**quan hệ 1-1** giỏ hàng ↔ đơn hàng).
5. **Shipper** nhận đơn (`/api/orders/{id}/accept`) → cập nhật **Đang giao** → **Đã giao** (`/api/orders/{id}/deliver`), hệ thống tự cập nhật doanh thu cho cửa hàng.

---

## 📄 Báo cáo dự án

Xem `REPORT.md` — báo cáo chi tiết về phân tích bài toán, use cases, thiết kế database, kiến trúc API và quy trình nghiệp vụ.