USE FoodProject;
GO

---------------------------------------------------
-- 1. CAP NHAT GIOHANG.TONGTIEN sau mỗi lần thêm/sửa/xóa món ăn trong giỏ hàng
---------------------------------------------------

CREATE TRIGGER TRG_CAPNHAT_TONGTIEN_GIOHANG
ON GIOHANG_MONAN
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE G
    SET TONGTIEN = ISNULL(T.TONG, 0)
    FROM GIOHANG G
    OUTER APPLY (
        SELECT SUM(GM.SOLUONG * M.GIA) AS TONG FROM GIOHANG_MONAN GM
        JOIN MONAN M ON GM.ID_MONAN = M.ID_MONAN
        WHERE GM.ID_GIOHANG = G.ID_GIOHANG
    ) T
    WHERE G.ID_GIOHANG IN (
        SELECT ID_GIOHANG FROM inserted
        UNION
        SELECT ID_GIOHANG FROM deleted
    );
END;
GO

---------------------------------------------------
-- 2. CAP NHAT DONHANG.TONGTIEN sau mỗi lần thêm đơn hàng
---------------------------------------------------
CREATE TRIGGER TRG_APDUNG_VOUCHER_DONHANG
ON DONHANG
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

UPDATE D
SET D.TONGTIEN = CASE V.LOAIVOUCHER
                     WHEN N'-10%' THEN I.TONGTIEN - (I.TONGTIEN * 0.10)
                     WHEN N'-30%' THEN I.TONGTIEN - (I.TONGTIEN * 0.30)
                     WHEN N'-50%' THEN I.TONGTIEN - (I.TONGTIEN * 0.50)
                     WHEN N'-70%' THEN I.TONGTIEN - (I.TONGTIEN * 0.70)
    -- ví dụ trừ cứng 15.000đ tiền ship
                     WHEN N'Freeship' THEN I.TONGTIEN - 15000
                     ELSE I.TONGTIEN
    END
    FROM DONHANG D
    JOIN inserted I ON D.ID_DONHANG = I.ID_DONHANG
    JOIN VOUCHER V ON I.ID_VOUCHER = V.ID_VOUCHER
WHERE I.ID_VOUCHER IS NOT NULL;
END;
GO
