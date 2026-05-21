/*
    SQL CHUẨN - DATABASE: QuanLyThuVien
    Mỗi bảng chính có 5 dòng dữ liệu mẫu

    DATABASE: QuanLyThuVien
    Dùng cho project Java Web MVC Servlet/JSP + JDBC + SQL Server
    Tương thích với source hiện tại:
    - SACH
    - DOCGIA
    - NHANVIEN
    - ACCOUNT
    - PHIEUMUON
    - PHIEUTRA
    - PHIEUPHAT
*/

IF DB_ID(N'QuanLyThuVien') IS NOT NULL
BEGIN
    ALTER DATABASE QuanLyThuVien SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyThuVien;
END
GO

CREATE DATABASE QuanLyThuVien;
GO

USE QuanLyThuVien;
GO

/* =========================
   1. BẢNG SÁCH
========================= */
CREATE TABLE SACH (
    MaSach INT IDENTITY(1,1) PRIMARY KEY,
    TenSach NVARCHAR(255) NOT NULL,
    TacGia NVARCHAR(150) NOT NULL,
    TheLoai NVARCHAR(100) NULL,
    NhaXuatBan NVARCHAR(150) NULL,
    GiaSach DECIMAL(18,2) NOT NULL DEFAULT 0,
    SoLuong INT NOT NULL DEFAULT 0,
    TinhTrang NVARCHAR(50) NOT NULL DEFAULT N'Còn',
    CONSTRAINT CK_SACH_GiaSach CHECK (GiaSach >= 0),
    CONSTRAINT CK_SACH_SoLuong CHECK (SoLuong >= 0)
);
GO

/* =========================
   2. BẢNG ĐỘC GIẢ
========================= */
CREATE TABLE DOCGIA (
    MaDocGia INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap VARCHAR(100) NOT NULL UNIQUE,
    HoTen NVARCHAR(150) NOT NULL,
    GioiTinh NVARCHAR(10) NULL,
    NgaySinh DATE NULL,
    DiaChi NVARCHAR(255) NULL,
    Email VARCHAR(150) NULL UNIQUE,
    SoDienThoai VARCHAR(20) NULL,
    CONSTRAINT CK_DOCGIA_GioiTinh CHECK (GioiTinh IS NULL OR GioiTinh IN (N'Nam', N'Nữ', N'Khác'))
);
GO

/* =========================
   3. BẢNG NHÂN VIÊN
========================= */
CREATE TABLE NHANVIEN (
    MaNV INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap VARCHAR(100) NOT NULL UNIQUE,
    HoTen NVARCHAR(150) NOT NULL,
    GioiTinh NVARCHAR(10) NULL,
    NgaySinh DATE NULL,
    DiaChi NVARCHAR(255) NULL,
    ChucVu NVARCHAR(100) NULL,
    CONSTRAINT CK_NHANVIEN_GioiTinh CHECK (GioiTinh IS NULL OR GioiTinh IN (N'Nam', N'Nữ', N'Khác'))
);
GO

/* =========================
   4. BẢNG TÀI KHOẢN
   Quyen: 0 = Admin, 1 = Customer/Độc giả, 2 = Staff/Nhân viên
========================= */
CREATE TABLE ACCOUNT (
    MaAccount INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap VARCHAR(100) NOT NULL UNIQUE,
    MatKhau VARCHAR(255) NOT NULL,
    Quyen INT NOT NULL,
    MaDocGia INT NULL,
    MaNV INT NULL,
    CONSTRAINT CK_ACCOUNT_Quyen CHECK (Quyen IN (0,1,2)),
    CONSTRAINT FK_ACCOUNT_DOCGIA FOREIGN KEY (MaDocGia) REFERENCES DOCGIA(MaDocGia),
    CONSTRAINT FK_ACCOUNT_NHANVIEN FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV),
    CONSTRAINT CK_ACCOUNT_OWNER CHECK (
        (Quyen = 1 AND MaDocGia IS NOT NULL AND MaNV IS NULL)
        OR
        (Quyen IN (0,2) AND MaNV IS NOT NULL AND MaDocGia IS NULL)
    )
);
GO

/* =========================
   5. BẢNG PHIẾU MƯỢN
========================= */
CREATE TABLE PHIEUMUON (
    MaPhieu INT IDENTITY(1,1) PRIMARY KEY,
    MaDocGia INT NOT NULL,
    MaSach INT NOT NULL,
    NgayMuon DATE NOT NULL,
    NgayPhaiTra DATE NOT NULL,
    RequestDate DATETIME NOT NULL DEFAULT GETDATE(),
    ApprovedDate DATETIME NULL,
    BorrowDate DATETIME NULL,
    ReturnDate DATETIME NULL,
    TrangThai NVARCHAR(30) NOT NULL DEFAULT N'Pending',
    StaffId INT NULL,
    LyDoTuChoi NVARCHAR(255) NULL,
    GhiChu NVARCHAR(255) NULL,
    FineAmount DECIMAL(18,2) NOT NULL DEFAULT 0,
    CONSTRAINT FK_PHIEUMUON_DOCGIA FOREIGN KEY (MaDocGia) REFERENCES DOCGIA(MaDocGia),
    CONSTRAINT FK_PHIEUMUON_SACH FOREIGN KEY (MaSach) REFERENCES SACH(MaSach),
    CONSTRAINT FK_PHIEUMUON_STAFF FOREIGN KEY (StaffId) REFERENCES NHANVIEN(MaNV),
    CONSTRAINT CK_PHIEUMUON_Ngay CHECK (NgayPhaiTra >= NgayMuon),
    CONSTRAINT CK_PHIEUMUON_TrangThai CHECK (TrangThai IN (N'Pending', N'Approved', N'Rejected', N'Borrowed', N'Returned', N'Overdue', N'Lost', N'Damaged'))
);
GO

/* =========================
   6. BẢNG PHIẾU TRẢ
========================= */
CREATE TABLE PHIEUTRA (
    MaPhieu INT IDENTITY(1,1) PRIMARY KEY,
    MaDocGia INT NOT NULL,
    MaSach INT NOT NULL,
    NgayTra DATE NOT NULL,
    CONSTRAINT FK_PHIEUTRA_DOCGIA FOREIGN KEY (MaDocGia) REFERENCES DOCGIA(MaDocGia),
    CONSTRAINT FK_PHIEUTRA_SACH FOREIGN KEY (MaSach) REFERENCES SACH(MaSach)
);
GO

/* =========================
   7. BẢNG PHIẾU PHẠT
   TrangThai: 0 = Chưa đóng, 1 = Đã đóng, -1 = Xóa/Hủy
========================= */
CREATE TABLE PHIEUPHAT (
    MaPhieuPhat INT IDENTITY(1,1) PRIMARY KEY,
    MaPhieu INT NOT NULL,
    MaDocGia INT NOT NULL,
    LyDo NVARCHAR(255) NOT NULL,
    SoTien DECIMAL(18,2) NOT NULL DEFAULT 0,
    NgayLap DATE NOT NULL DEFAULT GETDATE(),
    TrangThai INT NOT NULL DEFAULT 0,
    CONSTRAINT FK_PHIEUPHAT_PHIEUMUON FOREIGN KEY (MaPhieu) REFERENCES PHIEUMUON(MaPhieu),
    CONSTRAINT FK_PHIEUPHAT_DOCGIA FOREIGN KEY (MaDocGia) REFERENCES DOCGIA(MaDocGia),
    CONSTRAINT CK_PHIEUPHAT_SoTien CHECK (SoTien >= 0),
    CONSTRAINT CK_PHIEUPHAT_TrangThai CHECK (TrangThai IN (-1,0,1))
);
GO

/* =========================
   8. TRIGGER TỰ CẬP NHẬT TÌNH TRẠNG SÁCH
========================= */
CREATE TRIGGER TRG_SACH_UpdateTinhTrang
ON SACH
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE S
    SET TinhTrang = CASE WHEN S.SoLuong > 0 THEN N'Còn' ELSE N'Hết' END
    FROM SACH S
    INNER JOIN inserted I ON S.MaSach = I.MaSach;
END;
GO

/* =========================
   9. STORED PROCEDURES CHO CUSTOMER
========================= */
CREATE PROCEDURE SoPhieuMuon
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS sophieumuon
    FROM PHIEUMUON
    WHERE MaDocGia = @MaDocGia;
END;
GO

CREATE PROCEDURE LichSuMuon
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS sophieutra
    FROM PHIEUTRA
    WHERE MaDocGia = @MaDocGia;
END;
GO

CREATE PROCEDURE SoPhieuPhat
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS sophieuphat
    FROM PHIEUPHAT
    WHERE MaDocGia = @MaDocGia;
END;
GO

CREATE PROCEDURE SachMuon
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        PM.MaPhieu,
        S.TenSach,
        CONVERT(VARCHAR(10), PM.NgayMuon, 120) AS NgayMuon,
        ISNULL(CONVERT(VARCHAR(10), PT.NgayTra, 120), '') AS NgayTra,
        CASE
            WHEN PM.TrangThai = N'Pending' THEN N'Chờ duyệt'
            WHEN PM.TrangThai = N'Approved' THEN N'Đã duyệt - chờ nhận'
            WHEN PM.TrangThai = N'Rejected' THEN N'Bị từ chối'
            WHEN PM.TrangThai = N'Returned' THEN N'Đã trả'
            WHEN PM.TrangThai IN (N'Lost', N'Damaged') THEN PM.TrangThai
            WHEN PM.NgayPhaiTra < CAST(GETDATE() AS DATE) AND PM.TrangThai = N'Borrowed' THEN N'Quá hạn'
            ELSE N'Đang mượn'
        END AS TrangThai
    FROM PHIEUMUON PM
    INNER JOIN SACH S ON PM.MaSach = S.MaSach
    LEFT JOIN PHIEUTRA PT ON PT.MaDocGia = PM.MaDocGia AND PT.MaSach = PM.MaSach
    WHERE PM.MaDocGia = @MaDocGia
    ORDER BY PM.MaPhieu DESC;
END;
GO

CREATE PROCEDURE SachTra
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        S.TenSach,
        PM.NgayPhaiTra,
        CASE
            WHEN PM.TrangThai = N'Pending' THEN N'Chờ duyệt'
            WHEN PM.TrangThai = N'Approved' THEN N'Đã duyệt - chờ nhận'
            WHEN PM.TrangThai = N'Rejected' THEN N'Bị từ chối'
            WHEN PM.TrangThai = N'Returned' THEN N'Đã trả'
            WHEN PM.TrangThai IN (N'Lost', N'Damaged') THEN PM.TrangThai
            WHEN PM.NgayPhaiTra < CAST(GETDATE() AS DATE) AND PM.TrangThai = N'Borrowed' THEN N'Quá hạn'
            ELSE N'Chưa trả'
        END AS TrangThai
    FROM PHIEUMUON PM
    INNER JOIN SACH S ON PM.MaSach = S.MaSach
    LEFT JOIN PHIEUTRA PT ON PT.MaDocGia = PM.MaDocGia AND PT.MaSach = PM.MaSach
    WHERE PM.MaDocGia = @MaDocGia
    ORDER BY PM.MaPhieu DESC;
END;
GO

CREATE PROCEDURE ThongTinDG
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT HoTen, GioiTinh, DiaChi, NgaySinh
    FROM DOCGIA
    WHERE MaDocGia = @MaDocGia;
END;
GO

CREATE PROCEDURE CapNhapAcount
    @MaDocGia INT,
    @HoTen NVARCHAR(150),
    @GioiTinh NVARCHAR(10),
    @DiaChi NVARCHAR(255),
    @NgaySinh DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE DOCGIA
    SET HoTen = @HoTen,
        GioiTinh = @GioiTinh,
        DiaChi = @DiaChi,
        NgaySinh = @NgaySinh
    WHERE MaDocGia = @MaDocGia;
END;
GO

/* =========================
   10. STORED PROCEDURES CHO STAFF / ADMIN
========================= */
CREATE PROCEDURE ThongTinNV
    @MaNV INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT HoTen, GioiTinh, DiaChi, NgaySinh
    FROM NHANVIEN
    WHERE MaNV = @MaNV;
END;
GO

CREATE PROCEDURE CapNhapNV
    @MaNV INT,
    @HoTen NVARCHAR(150),
    @GioiTinh NVARCHAR(10),
    @DiaChi NVARCHAR(255),
    @NgaySinh DATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE NHANVIEN
    SET HoTen = @HoTen,
        GioiTinh = @GioiTinh,
        DiaChi = @DiaChi,
        NgaySinh = @NgaySinh
    WHERE MaNV = @MaNV;
END;
GO

CREATE PROCEDURE PhieuMuonHomNay
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS phieumuonhomnay
    FROM PHIEUMUON
    WHERE NgayMuon = CAST(GETDATE() AS DATE);
END;
GO

CREATE PROCEDURE PhieuTraHomNay
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS phieutrahomnay
    FROM PHIEUTRA
    WHERE NgayTra = CAST(GETDATE() AS DATE);
END;
GO

CREATE PROCEDURE TongSoLuongSach
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ISNULL(SUM(SoLuong),0) AS SoLuong
    FROM SACH;
END;
GO

CREATE PROCEDURE DemPhieuQuaHan
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS SoPhieuQuaHan
    FROM PHIEUMUON PM
    WHERE PM.NgayPhaiTra < CAST(GETDATE() AS DATE)
      AND PM.TrangThai IN (N'Borrowed', N'Overdue');
END;
GO

CREATE PROCEDURE SoSanhPhieuMuon
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @HomNay INT = (
        SELECT COUNT(*) FROM PHIEUMUON WHERE NgayMuon = CAST(GETDATE() AS DATE)
    );

    DECLARE @HomQua INT = (
        SELECT COUNT(*) FROM PHIEUMUON WHERE NgayMuon = DATEADD(DAY, -1, CAST(GETDATE() AS DATE))
    );

    SELECT (@HomNay - @HomQua) AS chenhlech;
END;
GO

CREATE PROCEDURE LuotMuonThangHienTai
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS SoLuong
    FROM PHIEUMUON
    WHERE MONTH(NgayMuon) = MONTH(GETDATE())
      AND YEAR(NgayMuon) = YEAR(GETDATE());
END;
GO

CREATE PROCEDURE LuotTraThangHienTai
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS TongLuotTra
    FROM PHIEUTRA
    WHERE MONTH(NgayTra) = MONTH(GETDATE())
      AND YEAR(NgayTra) = YEAR(GETDATE());
END;
GO

CREATE PROCEDURE DemTongPhieuPhatThang
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS TongSoPhieuPhat
    FROM PHIEUPHAT
    WHERE MONTH(NgayLap) = MONTH(GETDATE())
      AND YEAR(NgayLap) = YEAR(GETDATE());
END;
GO

CREATE PROCEDURE TinhTongTienPhatThang
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ISNULL(SUM(SoTien),0) AS TongTienPhatThangNay
    FROM PHIEUPHAT
    WHERE MONTH(NgayLap) = MONTH(GETDATE())
      AND YEAR(NgayLap) = YEAR(GETDATE());
END;
GO

CREATE PROCEDURE DemPhieuPhatChuaTra
AS
BEGIN
    SET NOCOUNT ON;
    SELECT COUNT(*) AS SoPhieuPhatChuaThanhToan
    FROM PHIEUPHAT
    WHERE TrangThai = 0;
END;
GO

CREATE PROCEDURE sp_ThongKeTangTruongMuonSachTuDong
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @ThangNay INT = (
        SELECT COUNT(*)
        FROM PHIEUMUON
        WHERE MONTH(NgayMuon) = MONTH(GETDATE())
          AND YEAR(NgayMuon) = YEAR(GETDATE())
    );

    DECLARE @ThangTruoc INT = (
        SELECT COUNT(*)
        FROM PHIEUMUON
        WHERE MONTH(NgayMuon) = MONTH(DATEADD(MONTH, -1, GETDATE()))
          AND YEAR(NgayMuon) = YEAR(DATEADD(MONTH, -1, GETDATE()))
    );

    SELECT
        CASE
            WHEN @ThangTruoc = 0 AND @ThangNay = 0 THEN N'0%'
            WHEN @ThangTruoc = 0 THEN N'100%'
            ELSE CONCAT(CAST(((@ThangNay - @ThangTruoc) * 100.0 / @ThangTruoc) AS DECIMAL(10,2)), '%')
        END AS PhanTramTangTruong;
END;
GO

/* =========================
   11. STORED PROCEDURES KIỂM TRA MƯỢN / TRẢ
========================= */
CREATE PROCEDURE kiemtrasach
    @MaSach INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT SoLuong
    FROM SACH
    WHERE MaSach = @MaSach;
END;
GO

CREATE PROCEDURE kiemtradocgia
    @MaDocGia INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT MaDocGia
    FROM DOCGIA
    WHERE MaDocGia = @MaDocGia;
END;
GO

CREATE PROCEDURE kiemtramasach
    @MaSach INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT MaSach
    FROM SACH
    WHERE MaSach = @MaSach;
END;
GO

CREATE PROCEDURE kiemtramasachPM
    @MaDocGia INT,
    @MaSach INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT TOP 1 MaDocGia, MaSach
    FROM PHIEUMUON PM
    WHERE PM.MaDocGia = @MaDocGia
      AND PM.MaSach = @MaSach
      AND PM.TrangThai IN (N'Borrowed', N'Overdue')
    ORDER BY PM.MaPhieu DESC;
END;
GO

/* =========================
   12. DỮ LIỆU MẪU - MỖI BẢNG 5 DÒNG
   Tài khoản test:
   - admin / 123      : Quản trị viên
   - staff / 123      : Nhân viên
   - staff2 / 123     : Nhân viên
   - customer / 123   : Độc giả
   - docgia2 / 123    : Độc giả
========================= */

/* 12.1. SÁCH: 5 dòng */
INSERT INTO SACH (TenSach, TacGia, TheLoai, NhaXuatBan, GiaSach, SoLuong, TinhTrang)
VALUES
(N'Lập trình Java căn bản', N'Nguyễn Văn A', N'Công nghệ thông tin', N'NXB Trẻ', 120000, 10, N'Còn'),
(N'Cơ sở dữ liệu SQL Server', N'Trần Văn B', N'Công nghệ thông tin', N'NXB Giáo Dục', 150000, 8, N'Còn'),
(N'Thiết kế Web với HTML CSS Bootstrap', N'Lê Thị C', N'Lập trình Web', N'NXB Lao Động', 99000, 15, N'Còn'),
(N'Phân tích thiết kế hệ thống', N'Phạm Văn D', N'Hệ thống thông tin', N'NXB Đại Học', 135000, 5, N'Còn'),
(N'Kỹ năng học đại học', N'Hoàng Minh E', N'Kỹ năng', N'NXB Tổng Hợp', 80000, 3, N'Còn');
GO

/* 12.2. ĐỘC GIẢ: 5 dòng */
INSERT INTO DOCGIA (TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, Email, SoDienThoai)
VALUES
('customer', N'Nguyễn Văn Khách', N'Nam', '2004-05-10', N'TP. Hồ Chí Minh', 'customer@gmail.com', '0900000001'),
('docgia2', N'Trần Thị Lan', N'Nữ', '2003-09-15', N'Hà Nội', 'docgia2@gmail.com', '0900000002'),
('docgia3', N'Lê Minh Tuấn', N'Nam', '2002-01-20', N'Đà Nẵng', 'docgia3@gmail.com', '0900000003'),
('docgia4', N'Phạm Ngọc Hân', N'Nữ', '2005-07-22', N'Cần Thơ', 'docgia4@gmail.com', '0900000004'),
('docgia5', N'Võ Quốc Bảo', N'Nam', '2001-11-30', N'Bình Dương', 'docgia5@gmail.com', '0900000005');
GO

/* 12.3. NHÂN VIÊN: 5 dòng */
INSERT INTO NHANVIEN (TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, ChucVu)
VALUES
('admin', N'Quản trị viên', N'Nam', '1995-01-01', N'TP. Hồ Chí Minh', N'Admin'),
('staff', N'Nhân viên thư viện', N'Nữ', '1998-03-12', N'TP. Hồ Chí Minh', N'Nhân viên'),
('staff2', N'Lê Thanh Bình', N'Nam', '1997-08-19', N'Đồng Nai', N'Nhân viên'),
('staff3', N'Nguyễn Thị Mai', N'Nữ', '1996-04-25', N'Long An', N'Thủ thư'),
('staff4', N'Đỗ Minh Khang', N'Nam', '1994-12-09', N'Tây Ninh', N'Quản lý kho sách');
GO

/* 12.4. ACCOUNT: 5 dòng */
INSERT INTO ACCOUNT (TenDangNhap, MatKhau, Quyen, MaDocGia, MaNV)
VALUES
('admin', '123', 0, NULL, 1),
('staff', '123', 2, NULL, 2),
('staff2', '123', 2, NULL, 3),
('customer', '123', 1, 1, NULL),
('docgia2', '123', 1, 2, NULL);
GO

/* 12.5. PHIẾU MƯỢN: 5 dòng */
INSERT INTO PHIEUMUON (MaDocGia, MaSach, NgayMuon, NgayPhaiTra, RequestDate, ApprovedDate, BorrowDate, ReturnDate, TrangThai, StaffId, LyDoTuChoi, GhiChu, FineAmount)
VALUES
(1, 1, DATEADD(DAY, -5, CAST(GETDATE() AS DATE)), DATEADD(DAY, 9, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-5,GETDATE()), DATEADD(DAY,-1,GETDATE()), N'Returned', 2, NULL, N'Tra sach co rach nhe bia', 10000),
(1, 2, DATEADD(DAY, -20, CAST(GETDATE() AS DATE)), DATEADD(DAY, -6, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-20,GETDATE()), NULL, N'Overdue', 2, NULL, N'Dang qua han 6 ngay', 30000),
(2, 3, CAST(GETDATE() AS DATE), DATEADD(DAY, 14, CAST(GETDATE() AS DATE)), GETDATE(), NULL, NULL, NULL, N'Pending', NULL, NULL, N'Khách gửi yêu cầu', 0),
(3, 4, DATEADD(DAY, -2, CAST(GETDATE() AS DATE)), DATEADD(DAY, 12, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), NULL, NULL, N'Approved', 2, NULL, N'Đã duyệt chờ nhận sách', 0),
(4, 5, DATEADD(DAY, -1, CAST(GETDATE() AS DATE)), DATEADD(DAY, 13, CAST(GETDATE() AS DATE)), GETDATE(), NULL, NULL, NULL, N'Rejected', 2, N'Độc giả đang có sách quá hạn', NULL, 0),
(5, 1, DATEADD(DAY, -8, CAST(GETDATE() AS DATE)), DATEADD(DAY, 6, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-8,GETDATE()), DATEADD(DAY,-1,GETDATE()), N'Damaged', 2, NULL, N'Sách hư bìa', 20000),
(2, 2, DATEADD(DAY, -10, CAST(GETDATE() AS DATE)), DATEADD(DAY, 4, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-10,GETDATE()), DATEADD(DAY,-2,GETDATE()), N'Lost', 2, NULL, N'Mất sách', 150000),
(3, 3, DATEADD(DAY, -4, CAST(GETDATE() AS DATE)), DATEADD(DAY, 10, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-4,GETDATE()), DATEADD(DAY,-1,GETDATE()), N'Returned', 3, NULL, N'Tra binh thuong', 0),
(4, 4, DATEADD(DAY, -9, CAST(GETDATE() AS DATE)), DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), GETDATE(), GETDATE(), DATEADD(DAY,-9,GETDATE()), DATEADD(DAY,-1,GETDATE()), N'Damaged', 3, NULL, N'Sach uot nuoc', 50000);
GO

/* 12.6. PHIẾU TRẢ: 5 dòng */
INSERT INTO PHIEUTRA (MaDocGia, MaSach, NgayTra)
VALUES
(1, 1, DATEADD(DAY, -1, CAST(GETDATE() AS DATE))),
(5, 1, DATEADD(DAY, -1, CAST(GETDATE() AS DATE))),
(2, 2, DATEADD(DAY, -2, CAST(GETDATE() AS DATE))),
(3, 3, DATEADD(DAY, -1, CAST(GETDATE() AS DATE))),
(4, 4, DATEADD(DAY, -1, CAST(GETDATE() AS DATE)));
GO

/* 12.7. PHIẾU PHẠT: 5 dòng */
INSERT INTO PHIEUPHAT (MaPhieu, MaDocGia, LyDo, SoTien, NgayLap, TrangThai)
VALUES
(1, 1, N'Làm cong bìa sách', 10000, DATEADD(DAY, -1, CAST(GETDATE() AS DATE)), 1),
(2, 1, N'Trả sách quá hạn', 30000, CAST(GETDATE() AS DATE), 0),
(6, 5, N'Sách hư bìa', 20000, DATEADD(DAY, -1, CAST(GETDATE() AS DATE)), 0),
(7, 2, N'Mất sách', 150000, DATEADD(DAY, -2, CAST(GETDATE() AS DATE)), 0),
(9, 4, N'Sách ướt nước', 50000, CAST(GETDATE() AS DATE), 0);
GO

/* =========================
   13. INDEX TỐI ƯU TÌM KIẾM
========================= */
CREATE INDEX IX_SACH_TenSach ON SACH(TenSach);
CREATE INDEX IX_DOCGIA_HoTen ON DOCGIA(HoTen);
CREATE INDEX IX_NHANVIEN_HoTen ON NHANVIEN(HoTen);
CREATE INDEX IX_PHIEUMUON_MaDocGia ON PHIEUMUON(MaDocGia);
CREATE INDEX IX_PHIEUTRA_MaDocGia ON PHIEUTRA(MaDocGia);
GO

/* =========================
   14. KIỂM TRA NHANH SAU KHI CHẠY SQL
========================= */
SELECT 'SACH' AS TenBang, COUNT(*) AS SoDong FROM SACH
UNION ALL SELECT 'DOCGIA', COUNT(*) FROM DOCGIA
UNION ALL SELECT 'NHANVIEN', COUNT(*) FROM NHANVIEN
UNION ALL SELECT 'ACCOUNT', COUNT(*) FROM ACCOUNT
UNION ALL SELECT 'PHIEUMUON', COUNT(*) FROM PHIEUMUON
UNION ALL SELECT 'PHIEUTRA', COUNT(*) FROM PHIEUTRA
UNION ALL SELECT 'PHIEUPHAT', COUNT(*) FROM PHIEUPHAT;
GO

SELECT * FROM ACCOUNT;
SELECT * FROM SACH;
GO
