USE FoodProject;
GO
/* PASSWORD SHA-256 của 123456 */
DECLARE @PASS NVARCHAR(255) =
'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92';

/* =========================
10 KHÁCH HÀNG
========================= */
EXEC create_user 'kh1@gmail.com',@PASS,N'Nguyen Van A','2000-01-01',N'Ha Noi','0901';
EXEC create_user 'kh2@gmail.com',@PASS,N'John Smith','1999-02-02',N'HCM','0902';
EXEC create_user 'kh3@gmail.com',@PASS,N'Tran Thi B','2001-03-03',N'Da Nang','0903';
EXEC create_user 'kh4@gmail.com',@PASS,N'Anna Lee','2002-04-04',N'Hai Phong','0904';
EXEC create_user 'kh5@gmail.com',@PASS,N'Le Van C','1998-05-05',N'Ha Noi','0905';
EXEC create_user 'kh6@gmail.com',@PASS,N'Carlos M','1997-06-06',N'HCM','0906';
EXEC create_user 'kh7@gmail.com',@PASS,N'Pham D','2000-07-07',N'Hue','0907';
EXEC create_user 'kh8@gmail.com',@PASS,N'Emily W','2003-08-08',N'Ninh Binh','0908';
EXEC create_user 'kh9@gmail.com',@PASS,N'Hoang E','1996-09-09',N'Ha Noi','0909';
EXEC create_user 'kh10@gmail.com',@PASS,N'Nguyen F','1995-10-10',N'HCM','0910';

/* =========================
10 CỬA HÀNG
========================= */
EXEC create_shop_manager 'kfc1@gmail.com',@PASS,N'KFC Bình Thạnh',N'HCM','1001';
EXEC create_shop_manager 'kfc2@gmail.com',@PASS,N'KFC Hoàng Mai',N'Hà Nội','1002';
EXEC create_shop_manager 'lotteria1@gmail.com',@PASS,N'Lotteria Q1',N'HCM','1003';
EXEC create_shop_manager 'lotteria2@gmail.com',@PASS,N'Lotteria Cầu Giấy',N'Hà Nội','1004';
EXEC create_shop_manager 'pizza1@gmail.com',@PASS,N'Pizza Hut Q7',N'HCM','1005';
EXEC create_shop_manager 'pizza2@gmail.com',@PASS,N'Pizza Hut Đống Đa',N'Hà Nội','1006';
EXEC create_shop_manager 'bunbo1@gmail.com',@PASS,N'Bún Bò Huế A',N'Hue','1007';
EXEC create_shop_manager 'comtam1@gmail.com',@PASS,N'Cơm Tấm B',N'HCM','1008';
EXEC create_shop_manager 'phobo1@gmail.com',@PASS,N'Phở 24',N'Hà Nội','1009';
EXEC create_shop_manager 'traisua1@gmail.com',@PASS,N'Trà Sữa X',N'HCM','1010';

/* =========================
5 SHIPPER
========================= */
EXEC sp_create_shipper 'ship1@gmail.com',@PASS,N'Ship A','2000-01-01','2001';
EXEC sp_create_shipper 'ship2@gmail.com',@PASS,N'Ship B','2000-01-01','2002';
EXEC sp_create_shipper 'ship3@gmail.com',@PASS,N'Ship C','2000-01-01','2003';
EXEC sp_create_shipper 'ship4@gmail.com',@PASS,N'Ship D','2000-01-01','2004';
EXEC sp_create_shipper 'ship5@gmail.com',@PASS,N'Ship E','2000-01-01','2005';

/* =========================
LOAI MON
========================= */
INSERT INTO LOAIMONAN(TENLOAI,IMG)
VALUES
(N'Gà rán','ga.jpg'),
(N'Pizza','pizza.jpg'),
(N'Bún/Phở','pho.jpg'),
(N'Cơm','com.jpg'),
(N'Trà sữa','trasua.jpg');


/* =========================
MON AN (KHÔNG hardcode CUAHANG ID)
→ lấy TOP cửa hàng theo email
========================= */

DECLARE @kfc1 INT = (SELECT ID_CUAHANG FROM CUAHANG c JOIN TAIKHOAN t ON c.ID_TAIKHOAN=t.ID_TAIKHOAN WHERE t.EMAIL='kfc1@gmail.com');
DECLARE @kfc2 INT = (SELECT ID_CUAHANG FROM CUAHANG c JOIN TAIKHOAN t ON c.ID_TAIKHOAN=t.ID_TAIKHOAN WHERE t.EMAIL='kfc2@gmail.com');
DECLARE @pizza1 INT = (SELECT ID_CUAHANG FROM CUAHANG c JOIN TAIKHOAN t ON c.ID_TAIKHOAN=t.ID_TAIKHOAN WHERE t.EMAIL='pizza1@gmail.com');
DECLARE @pho INT = (SELECT ID_CUAHANG FROM CUAHANG c JOIN TAIKHOAN t ON c.ID_TAIKHOAN=t.ID_TAIKHOAN WHERE t.EMAIL='phobo1@gmail.com');

INSERT INTO MONAN(ID_CUAHANG,ID_LOAI,TENMON,TRANGTHAI,GIA,IMG)
VALUES
(@kfc1,1,N'Gà rán giòn',N'Còn hàng',50000,'1.jpg'),
(@kfc2,1,N'Gà cay',N'Còn hàng',55000,'2.jpg'),
(@kfc1,1,N'Gà combo',N'Còn hàng',90000,'3.jpg'),

(@pizza1,2,N'Pizza cheese',N'Còn hàng',120000,'4.jpg'),
(@pizza1,2,N'Pizza bò',N'Còn hàng',130000,'5.jpg'),

(@pho,3,N'Phở bò',N'Còn hàng',45000,'6.jpg'),
(@pho,3,N'Bún bò Huế',N'Còn hàng',50000,'7.jpg');


/* =========================
VOUCHER
========================= */
INSERT INTO VOUCHER(ID_KHACHHANG,LOAIVOUCHER)
VALUES
(1,N'-10%'),
(2,N'Freeship'),
(3,N'-30%');