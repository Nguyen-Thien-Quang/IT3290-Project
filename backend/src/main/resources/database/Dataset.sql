-- 1. DỮ LIỆU BẢNG TAIKHOAN (Mật khẩu giả định là '123')
-- Chèn tài khoản cho 2 khách hàng, 2 cửa hàng, 2 shipper
INSERT INTO TAIKHOAN (EMAIL, MATKHAU, VAITRO) VALUES 
('khachhang1@gmail.com', '123', 'KhachHang'),
('khachhang2@gmail.com', '123', 'KhachHang'),
('store_obama@gmail.com', '123', 'CuaHang'),
('store_pizza@gmail.com', '123', 'CuaHang'),
('shipper_nhanh@gmail.com', '123', 'Shipper'),
('shipper_tietkiem@gmail.com', '123', 'Shipper');

-- 2. DỮ LIỆU BẢNG KHACHHANG (ID_TAIKHOAN 1 và 2)
INSERT INTO KHACHHANG (ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT) VALUES 
(1, N'Nguyễn Văn A', '1995-05-10', N'123 Lý Tự Trọng, Quận 1, HCM', '0901234567'),
(2, N'Trần Thị B', '1998-11-20', N'456 Trần Hưng Đạo, Quận 5, HCM', '0908889999');

-- 3. DỮ LIỆU BẢNG CUAHANG (ID_TAIKHOAN 3 và 4)
INSERT INTO CUAHANG (ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU) VALUES 
(3, N'Bún Chả Obama', N'Lê Thánh Tôn, Quận 1', '0281122334', 0),
(4, N'Pizza Hut Express', N'Nguyễn Trãi, Quận 5', '0285566778', 0);

-- 4. DỮ LIỆU BẢNG SHIPPER (ID_TAIKHOAN 5 và 6)
INSERT INTO SHIPPER (ID_TAIKHOAN, HOTEN, NGAYSINH, SDT) VALUES 
(5, N'Lê Văn Giao', '1990-01-01', '0911000111'),
(6, N'Phạm Thành Đạt', '1992-06-15', '0922000222');

-- 5. DỮ LIỆU BẢNG LOAIMONAN
INSERT INTO LOAIMONAN (TENLOAI, IMG) VALUES 
(N'Món Nước', 'img/categories/noodle.png'),
(N'Đồ Ăn Nhanh', 'img/categories/fastfood.png'),
(N'Cơm Trưa', 'img/categories/rice.png'),
(N'Tráng Miệng', 'img/categories/dessert.png');

-- 6. DỮ LIỆU BẢNG MONAN (Gán cho các cửa hàng và loại món)
-- Cửa hàng 1 (ID_CUAHANG = 1)
INSERT INTO MONAN (ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG) VALUES 
(1, 1, N'Bún Chả Đặc Biệt', 'con_ban', 65000, 'img/food/buncha.jpg'),
(1, 1, N'Nem Rán Giòn', 'con_ban', 15000, 'img/food/nemran.jpg'),
(1, 4, N'Trà Đá', 'con_ban', 5000, 'img/food/trada.jpg');

-- Cửa hàng 2 (ID_CUAHANG = 2)
INSERT INTO MONAN (ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG) VALUES 
(2, 2, N'Pizza Hải Sản Size L', 'con_ban', 250000, 'img/food/pizza_seafood.jpg'),
(2, 2, N'Gà Rán Cay', 'con_ban', 45000, 'img/food/chicken.jpg'),
(2, 4, N'Coca Cola', 'con_ban', 20000, 'img/food/coca.jpg');

-- 7. DỮ LIỆU BẢNG VOUCHER (Gán cho Khách hàng 1)
INSERT INTO VOUCHER (ID_KHACHHANG, LOAIVOUCHER) VALUES 
(1, N'GIAM10'),    -- Giảm 10%
(1, N'FREESHIP'),  -- Miễn phí ship
(2, N'GIAM50K');   -- Giảm thẳng 50k

-- 8. DỮ LIỆU BẢNG GIOHANG (Khách hàng 1 đang có 1 giỏ hàng)
INSERT INTO GIOHANG (ID_KHACHHANG, TRANGTHAI, TONGTIEN) VALUES 
(1, 'dang_tao', 0); -- Tiền sẽ tự nhảy nhờ Trigger bạn đã tạo

-- 9. DỮ LIỆU BẢNG GIOHANG_MONAN (Khách 1 thêm món vào giỏ)
INSERT INTO GIOHANG_MONAN (ID_GIOHANG, ID_MONAN, SOLUONG) VALUES 
(1, 1, 2), -- 2 Bún chả
(1, 2, 4); -- 4 Nem rán

-- 10. DỮ LIỆU BẢNG DONHANG (Lịch sử khách 2 đã từng đặt 1 đơn)
-- Giả sử khách 2 đã có 1 giỏ hàng đã chốt (ID_GIOHANG = 2)
INSERT INTO GIOHANG (ID_KHACHHANG, TRANGTHAI, TONGTIEN) VALUES (2, 'da_dat', 270000);

INSERT INTO DONHANG (ID_GIOHANG, ID_KHACHHANG, ID_SHIPPER, ID_VOUCHER, THOIGIANDAT, TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN) VALUES 
(2, 2, 5, 3, GETDATE(), N'da_giao', 220000, N'tien_mat');