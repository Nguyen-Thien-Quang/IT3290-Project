use online_food;
GO

-- TAIKHOAN customer
INSERT INTO TAIKHOAN (MATKHAU, VAITRO) VALUES
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer'),
(N'123456', N'customer');

-- KHACHHANG
INSERT INTO KHACHHANG
(ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT, TIEN)
VALUES
(1, N'Nguyễn Minh Anh', '2003-05-12', N'Cầu Giấy, Hà Nội', '0981234567', 120000),
(2, N'Trần Quốc Bảo', '1999-11-03', N'Hải Châu, Đà Nẵng', '0972345678', 540000),
(3, N'Lê Hoàng Nam', '2001-08-19', N'Ninh Kiều, Cần Thơ', '0963456789', 75000),
(4, N'Phạm Gia Hân', '2004-01-25', N'Thủ Đức, TP.HCM', '0954567890', 300000),
(5, N'Đặng Tuấn Kiệt', '1998-07-14', N'Biên Hòa, Đồng Nai', '0945678901', 990000),
(6, N'Bùi Khánh Linh', '2002-12-09', N'Long Biên, Hà Nội', '0936789012', 150000),
(7, N'Ngô Đức Thịnh', '1997-09-30', N'Huế, Thừa Thiên Huế', '0927890123', 450000),
(8, N'Võ Thanh Tùng', '2000-03-11', N'Vũng Tàu, Bà Rịa - Vũng Tàu', '0918901234', 870000),
(9, N'Phan Ngọc Mai', '2005-06-21', N'Nam Từ Liêm, Hà Nội', '0909012345', 220000),
(10, N'Đỗ Hải Yến', '2003-04-18', N'Lê Chân, Hải Phòng', '0890123456', 510000),

(11, N'Nguyễn Văn Hùng', '1995-02-15', N'Thanh Xuân, Hà Nội', '0881234567', 1100000),
(12, N'Trịnh Bảo Châu', '2001-10-10', N'Quận 7, TP.HCM', '0872345678', 340000),
(13, N'Hoàng Nhật Quang', '1996-06-05', N'TP Vinh, Nghệ An', '0863456789', 670000),
(14, N'Cao Mỹ Duyên', '2004-09-27', N'Hạ Long, Quảng Ninh', '0854567890', 200000),
(15, N'Lý Thành Đạt', '1999-01-08', N'Buôn Ma Thuột, Đắk Lắk', '0845678901', 980000),
(16, N'John Smith', '1994-07-17', N'California, USA', '0701234567', 2500000),
(17, N'Sakura Yamamoto', '2000-05-22', N'Tokyo, Japan', '0712345678', 1750000),
(18, N'Chen Wei', '1998-12-01', N'Shanghai, China', '0723456789', 1320000),
(19, N'Nguyễn Thảo Vy', '2002-11-13', N'Bắc Từ Liêm, Hà Nội', '0734567890', 410000),
(20, N'Trần Đức Long', '1997-03-29', N'TP Nha Trang, Khánh Hòa', '0745678901', 620000);

-- TAIKHOAN cho cửa hàng
INSERT INTO TAIKHOAN (MATKHAU, VAITRO) VALUES
(N'kfc@001', N'shop manager'),
(N'lotte@002', N'shop manager'),
(N'mcd@003', N'shop manager'),
(N'phuclong@004', N'shop manager'),
(N'thecoffee@005', N'shop manager'),
(N'bachhoa@006', N'shop manager'),
(N'shopee@007', N'shop manager'),
(N'lazada@008', N'shop manager'),
(N'circlek@009', N'shop manager'),
(N'vinmart@010', N'shop manager');

-- CUAHANG
INSERT INTO CUAHANG (ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT, DOANHTHU) VALUES
(1, N'KFC cơ sở Nguyễn Tuân', N'Nguyễn Tuân, Thanh Xuân, Hà Nội', '0901111111', 120000000),
(2, N'KFC cơ sở Hà Đông', N'Quang Trung, Hà Đông, Hà Nội', '0902222222', 98000000),
(3, N'Lotte cơ sở Giải Phóng', N'Giải Phóng, Hoàng Mai, Hà Nội', '0903333333', 150000000),
(4, N'McDonald cơ sở Cầu Giấy', N'Cầu Giấy, Hà Nội', '0904444444', 175000000),
(5, N'Phúc Long cơ sở Trần Duy Hưng', N'Trần Duy Hưng, Cầu Giấy, Hà Nội', '0905555555', 65000000),
(6, N'The Coffee House cơ sở Tây Sơn', N'Tây Sơn, Đống Đa, Hà Nội', '0906666666', 72000000),
(7, N'Bách Hóa Xanh cơ sở Láng Hạ', N'Láng Hạ, Ba Đình, Hà Nội', '0907777777', 200000000),
(8, N'Shopee Express cơ sở Minh Khai', N'Minh Khai, Hai Bà Trưng, Hà Nội', '0908888888', 300000000),
(9, N'Lazada cơ sở Mỹ Đình', N'Mỹ Đình, Nam Từ Liêm, Hà Nội', '0909999999', 250000000),
(10, N'Circle K cơ sở Kim Mã', N'Kim Mã, Ba Đình, Hà Nội', '0910000000', 55000000);


-- TAIKHOAN cho shipper
INSERT INTO TAIKHOAN (MATKHAU, VAITRO) VALUES
(N'shipper@001', N'shipper'),
(N'shipper@002', N'shipper');

INSERT INTO SHIPPER (ID_TAIKHOAN, HOTEN, NGAYSINH, SDT) VALUES
(11, N'Nguyễn Văn Huy', '1998-04-12', '0912345678'),
(12, N'Trần Minh Khôi', '2000-09-25', '0923456789');

--loai mon an
INSERT INTO LOAIMONAN (TENLOAI, IMG) VALUES
(N'Đồ ăn nhanh', NULL),
(N'Gà rán', NULL),
(N'Burger', NULL),
(N'Pizza', NULL),
(N'Mì - Phở - Bún', NULL),
(N'Cơm phần', NULL),
(N'Đồ uống', NULL),
(N'Trà sữa', NULL),
(N'Cà phê', NULL),
(N'Đồ ăn vặt', NULL),
(N'Tráng miệng', NULL),
(N'Kem', NULL),
(N'Healthy / Eat clean', NULL),
(N'Combo / Set menu', NULL),
(N'Đồ chay', NULL);

INSERT INTO MONAN (ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA) VALUES
-- KFC (ID_CUAHANG = 1)
(1, 2, N'Gà rán 2 miếng', N'còn hàng', 89000),
(1, 2, N'Gà rán 3 miếng', N'còn hàng', 129000),
(1, 14, N'Combo KFC cá nhân', N'còn hàng', 99000),

-- KFC Hà Đông (2)
(2, 2, N'Gà rán giòn cay', N'còn hàng', 95000),
(2, 14, N'Combo gia đình KFC', N'hết hàng', 199000),

-- Lotte (3)
(3, 3, N'Burger bò Lotte', N'còn hàng', 75000),
(3, 2, N'Gà rán Lotte', N'còn hàng', 85000),
(3, 14, N'Combo Lotte meal', N'còn hàng', 120000),

-- McDonald (4)
(4, 3, N'Big Mac', N'còn hàng', 89000),
(4, 3, N'Burger gà McChicken', N'còn hàng', 79000),
(4, 14, N'Combo Happy Meal', N'hết hàng', 110000),

-- Phúc Long (5)
(5, 9, N'Cà phê sữa đá', N'còn hàng', 45000),
(5, 9, N'Trà đào cam sả', N'còn hàng', 55000),

-- The Coffee House (6)
(6, 9, N'Latte nóng', N'còn hàng', 65000),
(6, 9, N'Cà phê đen', N'còn hàng', 40000),

-- Bách Hóa Xanh (7)
(7, 6, N'Cơm gà xối mỡ', N'còn hàng', 55000),
(7, 6, N'Cơm sườn trứng', N'hết hàng', 60000),

-- Shopee Food (8)
(8, 1, N'Mì trộn Hàn Quốc', N'còn hàng', 70000),
(8, 10, N'Khoai tây chiên lớn', N'còn hàng', 30000),

-- Lazada Food (9)
(9, 5, N'Phở bò tái', N'còn hàng', 60000),
(9, 5, N'Bún bò Huế', N'còn hàng', 65000),

-- Circle K (10)
(10, 10, N'Bánh mì sandwich', N'còn hàng', 25000),
(10, 7, N'Nước ngọt Coca Cola', N'còn hàng', 15000),
(10, 10, N'Mì ly Hảo Hảo', N'hết hàng', 12000);

