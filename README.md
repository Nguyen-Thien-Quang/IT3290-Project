\# IT3290-Project



\## Giới thiệu



IT3290-Project là website mô phỏng hệ thống quản lý giao và đặt đồ ăn trực tuyến. Hệ thống hỗ trợ ba loại người dùng chính:



\* Người dùng (User)

\* Cửa hàng (Shop)

\* Nhân viên giao hàng (Shipper)



Mỗi đối tượng có các chức năng và quyền truy cập riêng nhằm mô phỏng quy trình hoạt động của một nền tảng giao đồ ăn thực tế.



\---



\## Chức năng



\### 1. Người dùng (User)



Người dùng có thể:



\* Đăng ký tài khoản mới.

\* Xem thông tin cá nhân:



&#x20; \* ID

&#x20; \* Họ và tên

&#x20; \* Ngày sinh

&#x20; \* ...

\* Xem lịch sử đơn hàng của bản thân

\* Tìm kiếm món ăn theo từ khóa.

\* Tìm kiếm cửa hàng theo từ khóa.

\* Tạo đơn hàng mới.

\* Theo dõi trạng thái đơn hàng đã đặt.



\---



\### 2. Cửa hàng (Shop)



Cửa hàng có thể:



\* Đăng ký tài khoản cửa hàng mới.

\* Xem thông tin cửa hàng:



&#x20; \* Tên cửa hàng

&#x20; \* Địa chỉ

&#x20; \* Số điện thoại

&#x20; \* ...

\* Quản lý menu:



&#x20; \* Thêm món ăn

&#x20; \* Chỉnh sửa thông tin món ăn

&#x20; \* Xóa món ăn

\* Xem lịch sử đơn hàng của cửa hàng.

\* Theo dõi doanh thu trong khoảng thời gian cụ thể.

\* Xem danh sách các món ăn bán chạy nhất.



\---



\### 3. Nhân viên giao hàng (Shipper)



Nhân viên giao hàng có thể:



\* Đăng ký tài khoản mới.

\* Xem thông tin cá nhân:



&#x20; \* ID

&#x20; \* Họ và tên

&#x20; \* Ngày sinh

&#x20; \* ...

\* Xem danh sách các đơn hàng đã tiếp nhận.

\* Tiếp nhận đơn hàng mới.

\* Cập nhật trạng thái đơn hàng:



&#x20; \* Đang giao

&#x20; \* Đã giao



\---





\## Luồng đặt hàng



\### Quy trình tạo đơn hàng



1\. Khách hàng chọn thêm các món ăn vào giỏ hàng.

2\. Khi khách hàng thêm món ăn đầu tiên, hệ thống sẽ tự động tạo một giỏ hàng mới.

3\. Mỗi khách hàng chỉ được phép có duy nhất một giỏ hàng đang hoạt động tại một thời điểm.

4\. Khách hàng lựa chọn voucher (nếu có) và phương thức thanh toán rồi mới được nhấn đặt hàng.

5\. Sau khi nhấn \*\*Đặt hàng\*\*:



&#x20;  \* Giỏ hàng sẽ được lưu trữ (archived) và không thể tiếp tục chỉnh sửa.

&#x20;  \* Hệ thống tạo một đơn hàng mới tương ứng với giỏ hàng vừa được đặt.

&#x20;  \* Quan hệ giữa giỏ hàng và đơn hàng là \*\*1 - 1\*\* (mỗi giỏ hàng đã đặt sẽ sinh ra đúng một đơn hàng).







\## Mục tiêu dự án



Dự án được xây dựng nhằm mô phỏng quy trình hoạt động của một hệ thống đặt và giao đồ ăn trực tuyến, bao gồm các chức năng quản lý người dùng, cửa hàng, đơn hàng và vận chuyển.



\## Công nghệ sử dụng



\* Java Servlet

\* JSP

\* JDBC

\* SQL Database

\* HTML/CSS/JavaScript





