-- tra tên cửa hàng có chưa chuỗi keyword
CREATE PROCEDURE search_cuahang_by_keyword
    @keyword NVARCHAR(255)
AS
    SET NOCOUNT ON;
    SELECT *
    FROM CUAHANG
    WHERE TENCUAHANG LIKE N'%' + @keyword + N'%'
GO

--Trả về danh sách món ăn của cửa hàng có tên trùng với tham số @tencuahang
CREATE PROCEDURE monan_by_cuahang
    @tencuahang NVARCHAR(255)
AS
    SET NOCOUNT ON;
    SELECT M.*
    FROM MONAN M
    JOIN CUAHANG C ON M.ID_CUAHANG = C.ID_CUAHANG
    WHERE C.TENCUAHANG = @tencuahang
GO

