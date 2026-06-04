GO

/* =========================
   1. TẠO USER
========================= */
CREATE PROCEDURE create_user
    @Email NVARCHAR(255),
    @MatKhau NVARCHAR(255),
    @HoTen NVARCHAR(255),
    @NgaySinh DATE,
    @DiaChi NVARCHAR(255),
    @SDT NVARCHAR(15)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;
    INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
    VALUES (@MatKhau, N'Khách hàng', @Email);

    DECLARE @ID INT = SCOPE_IDENTITY();

    INSERT INTO KHACHHANG(ID_TAIKHOAN, HOTEN, NGAYSINH, DIACHI, SDT)
    VALUES (@ID, @HoTen, @NgaySinh, @DiaChi, @SDT);
    COMMIT;
END
GO


/* =========================
   2. TẠO SHOP MANAGER
========================= */
CREATE PROCEDURE create_shop_manager
    @Email NVARCHAR(255),
    @MatKhau NVARCHAR(255),
    @TenCuaHang NVARCHAR(255),
    @DiaChi NVARCHAR(255),
    @SDT NVARCHAR(15)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;
    BEGIN TRAN;
    INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
    VALUES (@MatKhau, N'Cửa hàng', @Email);

    DECLARE @ID INT = SCOPE_IDENTITY();

    INSERT INTO CUAHANG(ID_TAIKHOAN, TENCUAHANG, DIACHI, SDT)
    VALUES (@ID, @TenCuaHang, @DiaChi, @SDT);
    COMMIT;
END
GO


/* =========================
   3. TẠO SHIPPER
========================= */
CREATE PROCEDURE sp_create_shipper
    @Email NVARCHAR(255),
    @MatKhau NVARCHAR(255),
    @HoTen NVARCHAR(255),
    @NgaySinh DATE,
    @SDT NVARCHAR(15)
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;
    BEGIN TRAN;
    INSERT INTO TAIKHOAN(MATKHAU, VAITRO, EMAIL)
    VALUES (@MatKhau, N'Shipper', @Email);

    DECLARE @ID INT = SCOPE_IDENTITY();

    INSERT INTO SHIPPER(ID_TAIKHOAN, HOTEN, NGAYSINH, SDT)
    VALUES (@ID, @HoTen, @NgaySinh, @SDT);
    COMMIT;
END
GO


/* =========================
   4. CHECK LOGIN
   RETURN 1/0
========================= */
CREATE PROCEDURE check_login
    @Email NVARCHAR(255),
    @MatKhau NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (
        SELECT 1 FROM TAIKHOAN
        WHERE EMAIL = @Email AND MATKHAU = @MatKhau
    )
        SELECT 1 AS Result;
    ELSE
        SELECT 0 AS Result;
END
GO


/* =========================
   5. CHECKOUT GIỎ HÀNG
   + đổi trạng thái
   + tạo DONHANG 1-1
========================= */
CREATE PROCEDURE checkout_cart
    @ID_GIOHANG INT,
    @ID_KHACHHANG INT,
    @PHUONGTHUC NVARCHAR(50),
    @ID_VOUCHER INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRAN;

    UPDATE GIOHANG
    SET TRANGTHAI = N'Đã đặt'
    WHERE ID_GIOHANG = @ID_GIOHANG AND TRANGTHAI = N'Đang tạo';

    INSERT INTO DONHANG(
        ID_GIOHANG,ID_VOUCHER, ID_KHACHHANG, THOIGIANDAT,
        TRANGTHAI, TONGTIEN, PHUONGTHUCTHANHTOAN
    )
    SELECT 
        ID_GIOHANG,
        @ID_VOUCHER,
        @ID_KHACHHANG,
        GETDATE(),
        N'Chờ xác nhận',
        TONGTIEN,
        @PHUONGTHUC
    FROM GIOHANG
    WHERE ID_GIOHANG = @ID_GIOHANG;

    COMMIT;
END
GO


/* =========================
   6. BEST SELLER (THEO SỐ LƯỢNG)
========================= */
CREATE PROCEDURE best_seller_by_quantity
    @ID_CUAHANG INT,
    @TOP_K INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT TOP (@TOP_K) m.ID_MONAN, m.TENMON, SUM(gm.SOLUONG) AS TotalSold
    FROM MONAN m
    JOIN GIOHANG_MONAN gm ON m.ID_MONAN = gm.ID_MONAN
    JOIN GIOHANG gh ON gm.ID_GIOHANG = gh.ID_GIOHANG
    WHERE m.ID_CUAHANG = @ID_CUAHANG AND gh.TRANGTHAI = N'Đã đặt'
    GROUP BY m.ID_MONAN, m.TENMON
    ORDER BY TotalSold DESC;
END
GO


/* =========================
   7. BEST SELLER (THEO DOANH THU)
========================= */
CREATE PROCEDURE sp_best_seller_by_revenue
    @ID_CUAHANG INT,
    @TOP_K INT
AS
BEGIN
    SELECT TOP (@TOP_K) 
        m.ID_MONAN,
        m.TENMON,
        SUM(gm.SOLUONG * m.GIA) AS TotalRevenue
    FROM MONAN m
    JOIN GIOHANG_MONAN gm ON m.ID_MONAN = gm.ID_MONAN
    JOIN GIOHANG gh ON gm.ID_GIOHANG = gh.ID_GIOHANG
    WHERE m.ID_CUAHANG = @ID_CUAHANG AND gh.TRANGTHAI = N'Đã đặt'
    GROUP BY m.ID_MONAN, m.TENMON
    ORDER BY TotalRevenue DESC;
END
GO