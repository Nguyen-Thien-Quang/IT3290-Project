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
- Xem thông tin cá nhân (ID, họ tên, ngày sinh, SĐT...)
- Tìm kiếm món ăn theo từ khóa (`/api/food/search`)
- Tìm kiếm cửa hàng theo tên (`/api/shop/search`)
- Quản lý giỏ hàng: thêm / sửa / xóa món ăn (`/api/cart`)
- Đặt hàng với **voucher** và lựa chọn **phương thức thanh toán**
- Xem lịch sử đơn hàng (`/api/order/history`)
- Theo dõi trạng thái đơn đang giao (`/api/order`)
- Hủy đơn hàng khi chưa có shipper nhận (`/api/order/customer`)

### 🏪 Cửa hàng (Shop)
- Đăng ký / đăng nhập tài khoản cửa hàng
- Xem thông tin cửa hàng (tên, địa chỉ, SĐT...)
- Quản lý menu món ăn: thêm, sửa, xóa (`/api/shop/foods`, `/api/food`)
- Theo dõi danh sách đơn hàng của quán
- Xem **doanh thu** trong khoảng thời gian cụ thể (`/api/shop/sales`)
- Xem **món ăn bán chạy nhất** (`/api/shop/food/bestSeller`)

### 🛵 Shipper
- Đăng ký / đăng nhập tài khoản
- Xem thông tin cá nhân (ID, họ tên, ngày sinh...)
- Xem danh sách đơn hàng chờ xác nhận
- Tiếp nhận đơn hàng mới
- Cập nhật trạng thái đơn: **Đang giao** → **Đã giao** (`/api/order/shipping`)

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
| Dev server | Jetty (jetty-maven-plugin, port 8080) |
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
│           │   ├── model/       # POJO models (food, order, user)
│           │   ├── dao/         # Data Access Objects
│           │   └── APIs_spec/   # Tài liệu đặc tả API dạng Markdown
│           └── webapp/
│               ├── index.html / login.html / user_profile.html
│               ├── assets/      # CSS, JS, hình ảnh món ăn
│               └── WEB-INF/web.xml
└── REPORT.md                    # Báo cáo dự án chi tiết
```

### Các gói mã nguồn chính
- `context.DBContext` — quản lý kết nối SQL Server
- `controller/` — các Servlet xử lý request, trả JSON (chia theo `Account`, `KhachHang`, `CuaHang`, `Shipper`, `DonHang`, `MonAn`)
- `model/` — POJO theo 3 nhóm: `user`, `food`, `order` (kèm các class hằng số trạng thái)
- `dao/` — tầng truy cập dữ liệu, tương ứng từng model
- `APIs_spec/` — đặc tả giao thức từng API

---

## 🗄 Cơ sở dữ liệu

File backup SQL Server: **`database/FoodProject.bak`** (bao gồm schema + dữ liệu mẫu). Các bảng chính:

- **TAIKHOAN** — tài khoản & vai trò (Khách hàng, Cửa hàng, Shipper, Admin)
- **KHACHHANG / CUAHANG / SHIPPER** — thông tin chi tiết từng vai trò, liên kết `TAIKHOAN`
- **LOAIMONAN / MONAN** — danh mục và thực đơn
- **GIOHANG / GIOHANG_MONAN** — giỏ hàng tạm thời và các mục chọn
- **DONHANG** — bảng trung tâm quản lý giao dịch
- **VOUCHER** — mã giảm giá

> Thông tin kết nối (default trong `DBContext.java`): `jdbc:sqlserver://localhost:1433;databaseName=FoodProject;encrypt=false` — user `sa`, pass `123456`. Hãy điều chỉnh theo môi trường của bạn.

---

## 🔌 Danh sách API chính

| Nhóm | Method | Endpoint | Mô tả |
|---|---|---|---|
| **Auth** | POST | `/api/{role}/register` | Đăng ký (customer/shop/shipper) |
| | POST | `/api/{role}/login` | Đăng nhập |
| **Profile** | GET | `/api/customer/profile`, `/api/shop/profile`, `/api/shipper/profile` | Thông tin cá nhân |
| **Khách hàng** | GET | `/api/food/search` | Tìm món theo từ khóa |
| | GET | `/api/shop/search` | Tìm cửa hàng theo từ khóa |
| | GET/POST | `/api/cart` | Quản lý giỏ hàng |
| | GET | `/api/order` , `/api/order/history` | Đơn hiện tại / lịch sử |
| | POST | `/api/order/customer` | Hủy đơn |
| **Cửa hàng** | GET | `/api/shop/foods` | Danh sách món của cửa hàng |
| | POST | `/api/food` | Thêm / cập nhật món ăn |
| | GET | `/api/shop/food/bestSeller` | Món bán chạy |
| | GET | `/api/shop/sales` | Doanh thu theo thời gian |
| **Shipper** | GET/POST | `/api/order/shipping` | Nhận & cập nhật đơn |

Chi tiết request/response cho từng API: thư mục `web/src/main/java/APIs_spec/`.

---

## 🚀 Cài đặt & chạy

### Yêu cầu
- JDK 17+
- Maven 3.6+
- Microsoft SQL Server (đã tạo DB `FoodProject` với dữ liệu từ `.bak`)

### Các bước

**1. Khôi phục database** (SQL Server Management Studio hoặc `sqlcmd`):
```sql
RESTORE DATABASE FoodProject FROM DISK = 'path/to/database/FoodProject.bak';
```

**2. Cấu hình kết nối** — sửa thông tin trong `web/src/main/java/context/DBContext.java` nếu khác mặc định.

**3. Chạy server (Jetty, port 8080):**
```bash
cd web
mvn jetty:run
```

**4. Mở trình duyệt:**
```
http://localhost:8080/OnlineFoodWeb/
```

> Ứng dụng trả dữ liệu **JSON** qua các API `/api/*`; frontend (HTML/CSS/JS) giao tiếp bằng Fetch API.

---

## 🔄 Luồng đặt hàng

1. Khách hàng tìm món → thêm vào giỏ hàng (hệ thống tự tạo `GIOHANG` khi thêm món đầu tiên).
2. Mỗi khách hàng chỉ có **1 giỏ hàng đang hoạt động** tại một thời điểm.
3. Chọn voucher (nếu có) và phương thức thanh toán → nhấn **Đặt hàng**.
4. Giỏ hàng được lưu trữ (không thể chỉnh sửa) và hệ thống tạo `DONHANG` tương ứng (**quan hệ 1-1** giỏ hàng ↔ đơn hàng).
5. **Shipper** nhận đơn → cập nhật **Đang giao** → **Đã giao**, hệ thống tự cập nhật doanh thu cho cửa hàng.

---

## 📄 Báo cáo dự án

Xem `REPORT.md` — báo cáo chi tiết về phân tích bài toán, use cases, thiết kế database, kiến trúc API và quy trình nghiệp vụ.