package model;

import java.util.Date;

public class PhieuMuon {
    private int maPhieu;
    private int maDocGia;
    private String hoTen;
    private int maSach;
    private String tenSach;
    private Date ngayMuon;
    private Date ngayPhaiTra;
    private String trangThai;
    private String lyDoTuChoi;

    public PhieuMuon() {
    }

    public PhieuMuon(int maPhieu, int maDocGia, String hoTen, int maSach, String tenSach, Date ngayMuon, Date ngayPhaiTra) {
        this.maPhieu = maPhieu;
        this.maDocGia = maDocGia;
        this.hoTen = hoTen;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.ngayMuon = ngayMuon;
        this.ngayPhaiTra = ngayPhaiTra;
    }

    public int getMaPhieu() { return maPhieu; }
    public void setMaPhieu(int maPhieu) { this.maPhieu = maPhieu; }

    public int getMaDocGia() { return maDocGia; }
    public void setMaDocGia(int maDocGia) { this.maDocGia = maDocGia; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public int getMaSach() { return maSach; }
    public void setMaSach(int maSach) { this.maSach = maSach; }

    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }

    public Date getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Date ngayMuon) { this.ngayMuon = ngayMuon; }

    public Date getNgayPhaiTra() { return ngayPhaiTra; }
    public void setNgayPhaiTra(Date ngayPhaiTra) { this.ngayPhaiTra = ngayPhaiTra; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getLyDoTuChoi() { return lyDoTuChoi; }
    public void setLyDoTuChoi(String lyDoTuChoi) { this.lyDoTuChoi = lyDoTuChoi; }
}
