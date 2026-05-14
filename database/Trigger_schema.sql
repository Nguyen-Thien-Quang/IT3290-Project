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
