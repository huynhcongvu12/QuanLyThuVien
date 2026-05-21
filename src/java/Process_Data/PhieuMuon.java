package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class PhieuMuon {

    CONNECTIONSQLSERVER con;

    public PhieuMuon() {
        con = new CONNECTIONSQLSERVER();
    }

    public int kiemTraSach(int masach) {
        int soluong = 0;
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("kiemtrasach", new Object[]{masach});
            if (rs.next()) {
                soluong = rs.getInt("SoLuong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }

    public String kiemDocGia(int madocgia) {
        String madg = null;
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("kiemtradocgia", new Object[]{madocgia});
            if (rs.next()) {
                madg = rs.getString("MaDocGia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return madg;
    }

    public String kiemMaSach(int masach) {
        String mas = null;
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("kiemtramasach", new Object[]{masach});
            if (rs.next()) {
                mas = rs.getString("MaSach");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mas;
    }

    public int soSachDangMuon(int maDocGia) {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) AS Tong FROM PHIEUMUON WHERE MaDocGia = " + maDocGia
                    + " AND TrangThai IN (N'Pending', N'Approved', N'Borrowed', N'Overdue')";
            ResultSet rs = con.GetResultSetSQL(sql);
            if (rs.next()) total = rs.getInt("Tong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public boolean coSachQuaHan(int maDocGia) {
        try {
            String sql = "SELECT TOP 1 MaPhieu FROM PHIEUMUON WHERE MaDocGia = " + maDocGia
                    + " AND TrangThai IN (N'Borrowed', N'Overdue') AND NgayPhaiTra < CAST(GETDATE() AS DATE)";
            ResultSet rs = con.GetResultSetSQL(sql);
            return rs != null && rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Customer gửi yêu cầu mượn: giữ sách ngay để số lượng hiển thị giảm đúng.
    public int themYeuCauMuon(int maDocGia, int maSach, String ngayMuon, String ngayPhaiTra) {
        try {
            if (kiemTraSach(maSach) <= 0) return -1;
            String sql = "INSERT INTO PHIEUMUON(MaDocGia, MaSach, NgayMuon, NgayPhaiTra, TrangThai, RequestDate) "
                    + "VALUES (" + maDocGia + "," + maSach + ",'" + ngayMuon + "','" + ngayPhaiTra + "',N'Pending',GETDATE())";
            return con.ExecuteUpdateSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Staff/Admin lập trực tiếp phiếu mượn: xem như đã giao sách, trừ số lượng.
    public int themPhieuMuon(int maDocGia, int maSach, String ngayMuon, String ngayPhaiTra) {
        try {
            con.Open();
            Connection cnn = con.cnn;
            cnn.setAutoCommit(false);

            try (PreparedStatement reserve = cnn.prepareStatement(
                    "UPDATE SACH SET SoLuong = SoLuong - 1 WHERE MaSach = ? AND SoLuong > 0")) {
                reserve.setInt(1, maSach);
                if (reserve.executeUpdate() == 0) {
                    cnn.rollback();
                    return -1;
                }
            }

            try (PreparedStatement insert = cnn.prepareStatement(
                    "INSERT INTO PHIEUMUON(MaDocGia, MaSach, NgayMuon, NgayPhaiTra, TrangThai, RequestDate, ApprovedDate, BorrowDate) "
                    + "VALUES (?, ?, ?, ?, N'Borrowed', GETDATE(), GETDATE(), ?)")) {
                insert.setInt(1, maDocGia);
                insert.setInt(2, maSach);
                insert.setString(3, ngayMuon);
                insert.setString(4, ngayPhaiTra);
                insert.setString(5, ngayMuon);
                int k = insert.executeUpdate();
                if (k > 0) {
                    cnn.commit();
                    return k;
                }
                cnn.rollback();
            } catch (Exception e) {
                cnn.rollback();
                throw e;
            } finally {
                cnn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return 0;
    }

    public boolean dangMuonChuaTra(int maDocGia, int maSach) {
        try {
            String sql = "SELECT TOP 1 MaPhieu FROM PHIEUMUON WHERE MaDocGia=" + maDocGia
                    + " AND MaSach=" + maSach
                    + " AND TrangThai IN (N'Pending', N'Approved', N'Borrowed', N'Overdue')";
            ResultSet rs = con.GetResultSetSQL(sql);
            return rs != null && rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int capNhatQuaHanTuDong() {
        String sql = "UPDATE PHIEUMUON SET TrangThai=N'Overdue' "
                + "WHERE TrangThai=N'Borrowed' AND NgayPhaiTra < CAST(GETDATE() AS DATE)";
        return con.ExecuteUpdateSQL(sql);
    }

    public int duyetYeuCau(int maPhieu, int maNV) {
        String sql = "UPDATE PHIEUMUON SET TrangThai=N'Approved', ApprovedDate=GETDATE(), StaffId=" + maNV
                + " WHERE MaPhieu=" + maPhieu + " AND TrangThai=N'Pending'";
        return con.ExecuteUpdateSQL(sql);
    }

    public int tuChoiYeuCau(int maPhieu, int maNV, String lyDo) {
        if (lyDo == null || lyDo.trim().isEmpty()) lyDo = "Không đủ điều kiện mượn";
        try {
            lyDo = lyDo.replace("'", "''");
            String sql = "UPDATE PHIEUMUON SET TrangThai=N'Rejected', StaffId=" + maNV + ", LyDoTuChoi=N'" + lyDo + "' "
                    + "WHERE MaPhieu=" + maPhieu + " AND TrangThai IN (N'Pending', N'Approved')";
            return con.ExecuteUpdateSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int xacNhanGiaoSach(int maPhieu, int maNV) {
        try {
            con.Open();
            Connection cnn = con.cnn;
            cnn.setAutoCommit(false);
            int maSach;

            try (PreparedStatement find = cnn.prepareStatement(
                    "SELECT MaSach FROM PHIEUMUON WHERE MaPhieu = ? AND TrangThai = N'Approved'")) {
                find.setInt(1, maPhieu);
                ResultSet rs = find.executeQuery();
                if (!rs.next()) {
                    cnn.rollback();
                    return 0;
                }
                maSach = rs.getInt("MaSach");
            }

            try (PreparedStatement reserve = cnn.prepareStatement(
                    "UPDATE SACH SET SoLuong = SoLuong - 1 WHERE MaSach = ? AND SoLuong > 0")) {
                reserve.setInt(1, maSach);
                if (reserve.executeUpdate() == 0) {
                    cnn.rollback();
                    return -1;
                }
            }

            try (PreparedStatement update = cnn.prepareStatement(
                    "UPDATE PHIEUMUON SET TrangThai=N'Borrowed', BorrowDate=GETDATE(), "
                    + "NgayMuon=CAST(GETDATE() AS DATE), StaffId=? WHERE MaPhieu=? AND TrangThai=N'Approved'")) {
                update.setInt(1, maNV);
                update.setInt(2, maPhieu);
                int k = update.executeUpdate();
                if (k > 0) {
                    cnn.commit();
                    return k;
                }
                cnn.rollback();
            } catch (Exception e) {
                cnn.rollback();
                throw e;
            } finally {
                cnn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return 0;
    }

    public int xacNhanTraSach(int maPhieu, String tinhTrangSach, String ghiChu) {
        try {
            capNhatQuaHanTuDong();
            String q = "SELECT PM.MaDocGia, PM.MaSach, PM.NgayPhaiTra, PM.TrangThai, ISNULL(S.GiaSach, 0) AS GiaSach "
                    + "FROM PHIEUMUON PM JOIN SACH S ON PM.MaSach = S.MaSach "
                    + "WHERE PM.MaPhieu=" + maPhieu + " AND PM.TrangThai IN (N'Borrowed', N'Overdue')";
            ResultSet rs = con.GetResultSetSQL(q);
            if (rs == null || !rs.next()) return 0;
            int maDocGia = rs.getInt("MaDocGia");
            int maSach = rs.getInt("MaSach");
            double giaSach = rs.getDouble("GiaSach");
            int soNgayTre = 0;
            try {
                ResultSet late = con.GetResultSetSQL("SELECT CASE WHEN DATEDIFF(DAY, NgayPhaiTra, CAST(GETDATE() AS DATE)) > 0 "
                        + "THEN DATEDIFF(DAY, NgayPhaiTra, CAST(GETDATE() AS DATE)) ELSE 0 END AS SoNgayTre "
                        + "FROM PHIEUMUON WHERE MaPhieu=" + maPhieu);
                if (late != null && late.next()) soNgayTre = late.getInt("SoNgayTre");
            } catch (Exception ignored) {
                soNgayTre = 0;
            }
            String status = "Returned";
            boolean congSach = true;
            String lyDoPhat = "";
            double tienPhat = soNgayTre * 5000.0;
            if (soNgayTre > 0) lyDoPhat = "Trả sách quá hạn " + soNgayTre + " ngày";
            if ("damaged".equalsIgnoreCase(tinhTrangSach)) {
                status = "Damaged";
                congSach = false;
                lyDoPhat = lyDoPhat.isEmpty() ? "Sách hư hỏng" : lyDoPhat + "; sách hư hỏng";
                tienPhat += Math.max(50000.0, giaSach * 0.5);
            }
            if ("lost".equalsIgnoreCase(tinhTrangSach)) {
                status = "Lost";
                congSach = false;
                lyDoPhat = lyDoPhat.isEmpty() ? "Mất sách" : lyDoPhat + "; mất sách";
                tienPhat += Math.max(100000.0, giaSach);
            }
            ghiChu = ghiChu == null ? "" : ghiChu.replace("'", "''");
            int k = con.ExecuteUpdateSQL("UPDATE PHIEUMUON SET TrangThai=N'" + status + "', ReturnDate=GETDATE(), GhiChu=N'" + ghiChu
                    + "', FineAmount=" + tienPhat + " WHERE MaPhieu=" + maPhieu);
            if (k > 0) {
                con.ExecuteUpdateSQL("INSERT INTO PHIEUTRA(MaDocGia, MaSach, NgayTra) VALUES(" + maDocGia + "," + maSach + ",CAST(GETDATE() AS DATE))");
                if (congSach) con.ExecuteUpdateSQL("UPDATE SACH SET SoLuong = SoLuong + 1 WHERE MaSach=" + maSach);
                if (tienPhat > 0) {
                    lyDoPhat = lyDoPhat.replace("'", "''");
                    con.ExecuteUpdateSQL("INSERT INTO PHIEUPHAT(MaPhieu, MaDocGia, LyDo, SoTien, NgayLap, TrangThai) "
                            + "VALUES(" + maPhieu + "," + maDocGia + ",N'" + lyDoPhat + "'," + tienPhat + ",CAST(GETDATE() AS DATE),0)");
                }
            }
            return k;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updatePhieuMuon(int maPhieu, int maDocGia, int maSach, String ngayMuon, String ngayPhaiTra) {
        try {
            con.Open();
            Connection cnn = con.cnn;
            cnn.setAutoCommit(false);
            int oldMaSach;
            String trangThai;

            try (PreparedStatement find = cnn.prepareStatement(
                    "SELECT MaSach, TrangThai FROM PHIEUMUON WHERE MaPhieu = ?")) {
                find.setInt(1, maPhieu);
                ResultSet rs = find.executeQuery();
                if (!rs.next()) {
                    cnn.rollback();
                    return 0;
                }
                oldMaSach = rs.getInt("MaSach");
                trangThai = rs.getString("TrangThai");
            }

            boolean active = "Borrowed".equalsIgnoreCase(trangThai)
                    || "Overdue".equalsIgnoreCase(trangThai);

            if (active && oldMaSach != maSach) {
                try (PreparedStatement reserveNew = cnn.prepareStatement(
                        "UPDATE SACH SET SoLuong = SoLuong - 1 WHERE MaSach = ? AND SoLuong > 0")) {
                    reserveNew.setInt(1, maSach);
                    if (reserveNew.executeUpdate() == 0) {
                        cnn.rollback();
                        return -1;
                    }
                }
                try (PreparedStatement restoreOld = cnn.prepareStatement(
                        "UPDATE SACH SET SoLuong = SoLuong + 1 WHERE MaSach = ?")) {
                    restoreOld.setInt(1, oldMaSach);
                    restoreOld.executeUpdate();
                }
            }

            try (PreparedStatement update = cnn.prepareStatement(
                    "UPDATE PHIEUMUON SET MaDocGia=?, MaSach=?, NgayMuon=?, NgayPhaiTra=? WHERE MaPhieu=?")) {
                update.setInt(1, maDocGia);
                update.setInt(2, maSach);
                update.setString(3, ngayMuon);
                update.setString(4, ngayPhaiTra);
                update.setInt(5, maPhieu);
                int k = update.executeUpdate();
                if (k > 0) {
                    cnn.commit();
                    return k;
                }
                cnn.rollback();
            } catch (Exception e) {
                cnn.rollback();
                throw e;
            } finally {
                cnn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return 0;
    }

    public int deletePhieuMuon(int maPhieu) {
        try {
            con.Open();
            Connection cnn = con.cnn;
            cnn.setAutoCommit(false);
            int maSach;
            String trangThai;

            try (PreparedStatement find = cnn.prepareStatement(
                    "SELECT MaSach, TrangThai FROM PHIEUMUON WHERE MaPhieu = ?")) {
                find.setInt(1, maPhieu);
                ResultSet rs = find.executeQuery();
                if (!rs.next()) {
                    cnn.rollback();
                    return 0;
                }
                maSach = rs.getInt("MaSach");
                trangThai = rs.getString("TrangThai");
            }

            try (PreparedStatement delete = cnn.prepareStatement(
                    "DELETE FROM PHIEUMUON WHERE MaPhieu = ?")) {
                delete.setInt(1, maPhieu);
                int k = delete.executeUpdate();
                if (k > 0) {
                    if ("Borrowed".equalsIgnoreCase(trangThai)
                            || "Overdue".equalsIgnoreCase(trangThai)) {
                        try (PreparedStatement restore = cnn.prepareStatement(
                                "UPDATE SACH SET SoLuong = SoLuong + 1 WHERE MaSach = ?")) {
                            restore.setInt(1, maSach);
                            restore.executeUpdate();
                        }
                    }
                    cnn.commit();
                    return k;
                }
                cnn.rollback();
            } catch (Exception e) {
                cnn.rollback();
                throw e;
            } finally {
                cnn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return 0;
    }

    public Vector<model.PhieuMuon> searchPhieuMuon(String keyword) {
        Vector<model.PhieuMuon> vt = new Vector<>();
        try {
            String SQL = "SELECT PM.MaPhieu, DG.MaDocGia, DG.HoTen, S.MaSach, S.TenSach, PM.NgayMuon, PM.NgayPhaiTra, PM.TrangThai, PM.LyDoTuChoi "
                    + "FROM PHIEUMUON PM JOIN DOCGIA DG ON PM.MaDocGia = DG.MaDocGia JOIN SACH S ON PM.MaSach = S.MaSach "
                    + "WHERE DG.HoTen LIKE N'%" + keyword + "%' OR S.TenSach LIKE N'%" + keyword + "%' OR CAST(PM.MaPhieu AS VARCHAR) LIKE '%" + keyword + "%' "
                    + "ORDER BY PM.MaPhieu DESC";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuMuon pm = new model.PhieuMuon();
                pm.setMaPhieu(rs.getInt("MaPhieu"));
                pm.setMaDocGia(rs.getInt("MaDocGia"));
                pm.setHoTen(rs.getString("HoTen"));
                pm.setMaSach(rs.getInt("MaSach"));
                pm.setTenSach(rs.getString("TenSach"));
                pm.setNgayMuon(rs.getDate("NgayMuon"));
                pm.setNgayPhaiTra(rs.getDate("NgayPhaiTra"));
                pm.setTrangThai(rs.getString("TrangThai"));
                pm.setLyDoTuChoi(rs.getString("LyDoTuChoi"));
                vt.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }
}
