# BÁO CÁO DỰ ÁN: HỆ THỐNG QUẢN LÝ ĐẶT VÀ GIAO ĐỒ ĂN TRỰC TUYẾN (FoodProject)

---

### 1. Giới thiệu và Mô tả bài toán
**1.1. Lý do chọn đề tài**
Trong kỷ nguyên số hóa, nhu cầu đặt đồ ăn trực tuyến đã trở thành một phần thiết yếu của cuộc sống hiện đại. Việc xây dựng một hệ thống mô phỏng quy trình từ đặt hàng, chế biến đến giao hàng giúp nắm vững kiến thức về lập trình web Java EE, quản trị cơ sở dữ liệu và quy trình nghiệp vụ thực tế của các nền tảng O2O (Online-to-Offline).

**1.2. Đối tượng sử dụng (Actors)**
Hệ thống được thiết kế cho 3 nhóm đối tượng chính:
*   **Khách hàng (Customer):** Người tìm kiếm món ăn, đặt hàng và thanh toán.
*   **Cửa hàng (Store):** Người quản lý thực đơn, nhận đơn và theo dõi doanh thu.
*   **Shipper:** Người tiếp nhận đơn hàng và thực hiện quy trình giao vận.
*   **Quản trị viên (Admin):** Quản lý toàn bộ hệ thống (tài khoản, báo cáo).

---

### 2. Các chức năng chính (Use Cases)
**2.1. Khách hàng**
*   Tìm kiếm món ăn/cửa hàng theo từ khóa.
*   Quản lý giỏ hàng (Thêm/Sửa/Xóa món ăn).
*   Đặt hàng với Voucher và lựa chọn phương thức thanh toán.
*   Theo dõi trạng thái và hủy đơn hàng (khi chưa có Shipper nhận).

**2.2. Cửa hàng**
*   Quản lý danh mục món ăn (CRUD: Thêm, Sửa, Xóa).
*   Xem thống kê doanh thu theo thời gian thực.
*   Xem báo cáo món ăn bán chạy nhất để tối ưu menu.
*   Theo dõi danh sách đơn hàng của quán.

**2.3. Shipper**
*   Xem danh sách các đơn hàng đang chờ xác nhận.
*   Tiếp nhận đơn hàng và cập nhật trạng thái "Đang giao", "Đã giao".

---

### 3. Công nghệ và Công cụ sử dụng
*   **Ngôn ngữ lập trình:** Java (JDK 11+).
*   **Công nghệ Backend:** Jakarta EE 10 (Servlets).
*   **Cơ sở dữ liệu:** Microsoft SQL Server.
*   **Thư viện xử lý JSON:** Google Gson (phiên bản 2.10.1).
*   **Frontend:** HTML5, CSS3 (Vanilla), JavaScript (ES6+), Fetch API.
*   **Quản lý dự án:** Maven.
*   **Mô hình kiến trúc:** MVC (Model-View-Controller) kết hợp DAO (Data Access Object).

---

### 4. Thiết kế Cơ sở dữ liệu (Database Schema)
Hệ thống sử dụng cơ sở dữ liệu quan hệ với các bảng chính:
*   **TAIKHOAN:** Lưu trữ thông tin định danh và vai trò (VAITRO).
*   **KHACHHANG, CUAHANG, SHIPPER:** Các bảng mở rộng chứa thông tin chi tiết cho từng vai trò, liên kết với TAIKHOAN.
*   **MONAN & LOAIMONAN:** Quản lý thực đơn và danh mục.
*   **GIOHANG & GIOHANG_MONAN:** Lưu vết giỏ hàng tạm thời và các mục chọn.
*   **DONHANG:** Bảng trung tâm quản lý luồng giao dịch, kết nối Khách hàng, Shipper và Giỏ hàng.
*   **VOUCHER:** Quản lý các mã giảm giá cho khách hàng.

---

### 5. Kiến trúc API (API Architecture)
Hệ thống giao tiếp thông qua RESTful-like API trả về dữ liệu định dạng JSON:
*   **Auth API:** `/api/customer/login`, `/api/shop/register`, v.v.
*   **Menu API:** `/api/food/search` (tìm kiếm), `/api/food` (CRUD cho Store).
*   **Order API:** `/api/cart` (giỏ hàng), `/api/order/history` (lịch sử đơn).
*   **Report API:** `/api/shop/sales` (doanh thu), `/api/shop/best-sellers`.

---

### 6. Luồng nghiệp vụ tiêu biểu (Workflow)
1.  **Tạo đơn:** Khách hàng tìm món -> Thêm vào giỏ (GIOHANG) -> Nhấn "Đặt hàng" -> Hệ thống chuyển trạng thái giỏ sang "Đã đặt" và sinh bản ghi DONHANG (Trạng thái: "Chờ xác nhận").
2.  **Vận chuyển:** Shipper quét danh sách đơn chờ -> Nhấn "Nhận đơn" -> Trạng thái DONHANG chuyển sang "Đang giao".
3.  **Hoàn tất:** Shipper nhấn "Hoàn thành" -> Trạng thái chuyển sang "Đã giao" -> Hệ thống tự động cập nhật doanh thu cho CUAHANG.

---
*Báo cáo được tổng hợp dựa trên mã nguồn và tài liệu kỹ thuật của dự án OnlineFoodWeb.*
