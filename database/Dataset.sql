USE FoodProject;
GO

/* =========================
   CUSTOMER
========================= */
EXEC create_user
N'minhanh@gmail.com', N'123456',
N'Nguyễn Minh Anh', '2003-05-12',
N'Cầu Giấy, Hà Nội', '0981234567';

EXEC create_user
N'quocbao@gmail.com', N'123456',
N'Trần Quốc Bảo', '1999-11-03',
N'Hải Châu, Đà Nẵng', '0972345678';

EXEC create_user
N'hoangnam@gmail.com', N'123456',
N'Lê Hoàng Nam', '2001-08-19',
N'Ninh Kiều, Cần Thơ', '0963456789';

EXEC create_user
N'giahan@gmail.com', N'123456',
N'Phạm Gia Hân', '2004-01-25',
N'Thủ Đức, TP.HCM', '0954567890';

EXEC create_user
N'tuankiet@gmail.com', N'123456',
N'Đặng Tuấn Kiệt', '1998-07-14',
N'Biên Hòa, Đồng Nai', '0945678901';

EXEC create_user
N'khanhlinh@gmail.com', N'123456',
N'Bùi Khánh Linh', '2002-12-09',
N'Long Biên, Hà Nội', '0936789012';

EXEC create_user
N'ducthinh@gmail.com', N'123456',
N'Ngô Đức Thịnh', '1997-09-30',
N'Huế, Thừa Thiên Huế', '0927890123';

EXEC create_user
N'thanhtung@gmail.com', N'123456',
N'Võ Thanh Tùng', '2000-03-11',
N'Vũng Tàu, Bà Rịa - Vũng Tàu', '0918901234';

EXEC create_user
N'ngocmai@gmail.com', N'123456',
N'Phan Ngọc Mai', '2005-06-21',
N'Nam Từ Liêm, Hà Nội', '0909012345';

EXEC create_user
N'haiyen@gmail.com', N'123456',
N'Đỗ Hải Yến', '2003-04-18',
N'Lê Chân, Hải Phòng', '0890123456';



/* =========================
   SHOP MANAGER
========================= */
EXEC create_shop_manager
N'kfc1@gmail.com', N'kfc@001',
N'KFC cơ sở Nguyễn Tuân',
N'Nguyễn Tuân, Thanh Xuân, Hà Nội',
N'0901111111';

EXEC create_shop_manager
N'kfc2@gmail.com', N'kfc@002',
N'KFC cơ sở Hà Đông',
N'Quang Trung, Hà Đông, Hà Nội',
N'0902222222';

EXEC create_shop_manager
N'lotte@gmail.com', N'lotte@003',
N'Lotte cơ sở Giải Phóng',
N'Giải Phóng, Hoàng Mai, Hà Nội',
N'0903333333';

EXEC create_shop_manager
N'mcd@gmail.com', N'mcd@004',
N'McDonald cơ sở Cầu Giấy',
N'Cầu Giấy, Hà Nội',
N'0904444444';

EXEC create_shop_manager
N'phuclong@gmail.com', N'phuclong@005',
N'Phúc Long cơ sở Trần Duy Hưng',
N'Trần Duy Hưng, Cầu Giấy, Hà Nội',
N'0905555555';



/* =========================
   SHIPPER
========================= */
EXEC sp_create_shipper
N'shipper1@gmail.com',
N'shipper@001',
N'Nguyễn Văn Huy',
'1998-04-12',
N'0912345678';

EXEC sp_create_shipper
N'shipper2@gmail.com',
N'shipper@002',
N'Trần Minh Khôi',
'2000-09-25',
N'0923456789';



/* =========================
   LOAI MON AN
========================= */
INSERT INTO LOAIMONAN(TENLOAI, IMG)
VALUES
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



/* =========================
   MON AN
========================= */
INSERT INTO MONAN
(ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG)
VALUES

(1, 2, N'Gà rán 2 miếng', N'Còn hàng', 89000, NULL),
(1, 2, N'Gà rán 3 miếng', N'Còn hàng', 129000, NULL),
(1, 14, N'Combo KFC cá nhân', N'Còn hàng', 99000, NULL),

(2, 2, N'Gà rán giòn cay', N'Còn hàng', 95000, NULL),
(2, 14, N'Combo gia đình KFC', N'Hết hàng', 199000, NULL),

(3, 3, N'Burger bò Lotte', N'Còn hàng', 75000, NULL),
(3, 2, N'Gà rán Lotte', N'Còn hàng', 85000, NULL),
(3, 14, N'Combo Lotte meal', N'Còn hàng', 120000, NULL),

(4, 3, N'Big Mac', N'Còn hàng', 89000, NULL),
(4, 3, N'Burger gà McChicken', N'Còn hàng', 79000, NULL),
(4, 14, N'Combo Happy Meal', N'Hết hàng', 110000, NULL),

(5, 9, N'Cà phê sữa đá', N'Còn hàng', 45000, NULL),
(5, 9, N'Trà đào cam sả', N'Còn hàng', 55000, NULL);