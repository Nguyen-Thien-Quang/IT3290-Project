/* =========================
TAIKHOAN (10 KH + 10 CH + 5 SHIPPER)
password = 123456
========================= */

/* =========================
TAIKHOAN SAMPLE DATA (HASHED PASSWORD)
password = 123456
========================= */

-- 10 KHÁCH HÀNG
INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
VALUES
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh2@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh3@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh4@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh5@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh6@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh7@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh8@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh9@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Khách hàng', 'kh10@gmail.com');

-- 10 CỬA HÀNG
INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
VALUES
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'kfc1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'kfc2@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'lotteria1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'lotteria2@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'pizza1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'pizza2@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'bunbo1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'comtam1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'phobo1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Cửa hàng', 'traisua1@gmail.com');

-- 5 SHIPPER
INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
VALUES
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Shipper', 'ship1@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Shipper', 'ship2@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Shipper', 'ship3@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Shipper', 'ship4@gmail.com'),
('8d969eef6ecad3c29a3a629280e686cff8fad7f0f9c9d7a8f5e8a3d5d0f3c2a7', N'Shipper', 'ship5@gmail.com');
/* =========================
CUAHANG
(ID 11-20 tương ứng)
========================= */
INSERT INTO CUAHANG(ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT)
VALUES
(11,N'KFC Bình Thạnh',N'HCM','111'),
(12,N'KFC Hoàng Mai',N'Hà Nội','112'),
(13,N'Lotteria Q1',N'HCM','113'),
(14,N'Lotteria Cầu Giấy',N'Hà Nội','114'),
(15,N'Pizza Hut Q7',N'HCM','115'),
(16,N'Pizza Hut Đống Đa',N'Hà Nội','116'),
(17,N'Bún Bò Huế A',N'Huế','117'),
(18,N'Cơm Tấm B',N'HCM','118'),
(19,N'Phở 24',N'Hà Nội','119'),
(20,N'Trà Sữa X',N'HCM','120');


/* =========================
SHIPPER
(ID 21-25)
========================= */
INSERT INTO SHIPPER(ID_TAIKHOAN, HOTEN, NGAYSINH, SDT)
VALUES
(21,N'Ship A','2000-01-01','2001'),
(22,N'Ship B','2000-01-01','2002'),
(23,N'Ship C','2000-01-01','2003'),
(24,N'Ship D','2000-01-01','2004'),
(25,N'Ship E','2000-01-01','2005');


/* =========================
LOAIMONAN (5 loại)
========================= */
INSERT INTO LOAIMONAN(TENLOAI, IMG)
VALUES
(N'Gà rán','ga.jpg'),
(N'Pizza','pizza.jpg'),
(N'Bún/Phở','pho.jpg'),
(N'Cơm','com.jpg'),
(N'Trà sữa','trasua.jpg');


/* =========================
MONAN (20 món)
========================= */
INSERT INTO MONAN(ID_CUAHANG, ID_LOAI, TENMON, TRANGTHAI, GIA, IMG)
VALUES
(1,1,N'Gà rán giòn','Còn hàng',50000,'1.jpg'),
(2,1,N'Gà cay','Còn hàng',55000,'2.jpg'),
(3,1,N'Gà viên','Còn hàng',40000,'3.jpg'),
(4,1,N'Gà combo','Còn hàng',90000,'4.jpg'),

(5,2,N'Pizza cheese','Còn hàng',120000,'5.jpg'),
(6,2,N'Pizza bò','Còn hàng',130000,'6.jpg'),
(7,2,N'Pizza hải sản','Còn hàng',150000,'7.jpg'),
(8,2,N'Pizza xúc xích','Còn hàng',110000,'8.jpg'),

(9,3,N'Phở bò','Còn hàng',45000,'9.jpg'),
(10,3,N'Bún bò Huế','Còn hàng',50000,'10.jpg'),
(11,3,N'Bún chả','Còn hàng',45000,'11.jpg'),
(12,3,N'Hủ tiếu','Còn hàng',40000,'12.jpg'),

(13,4,N'Cơm gà','Còn hàng',60000,'13.jpg'),
(14,4,N'Cơm sườn','Còn hàng',65000,'14.jpg'),
(15,4,N'Cơm tấm đặc biệt','Còn hàng',80000,'15.jpg'),
(16,4,N'Cơm chiên','Còn hàng',55000,'16.jpg'),

(17,5,N'Trà sữa trân châu','Còn hàng',30000,'17.jpg'),
(18,5,N'Trà sữa matcha','Còn hàng',35000,'18.jpg'),
(19,5,N'Trà đào','Còn hàng',30000,'19.jpg'),
(20,5,N'Trà sữa socola','Còn hàng',32000,'20.jpg');


/* =========================
VOUCHER (3 cái)
========================= */
INSERT INTO VOUCHER(ID_KHACHHANG, LOAIVOUCHER)
VALUES
(1,N'-10%'),
(2,N'Freeship'),
(3,N'-30%');