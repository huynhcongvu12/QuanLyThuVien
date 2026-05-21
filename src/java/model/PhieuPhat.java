// Model: PhieuPhat.java
package model;
import java.util.Date;

public class PhieuPhat {
    private int maPhieuPhat;
    private int maDocGia;
    private String tenDocGia;
    private int maPhieu;
    private String lyDo;
    private double soTien;
    private Date ngayLap;
    private int trangThai;  // 0 = Chưa đóng, 1 = Đã đóng, -1 = Xóa

    // Getter & Setter
    public int getMaPhieuPhat() { return maPhieuPhat; }
    public void setMaPhieuPhat(int maPhieuPhat) { this.maPhieuPhat = maPhieuPhat; }

    public int getMaDocGia() { return maDocGia; }
    public void setMaDocGia(int maDocGia) { this.maDocGia = maDocGia; }

    public String getTenDocGia() { return tenDocGia; }
    public void setTenDocGia(String tenDocGia) { this.tenDocGia = tenDocGia; }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }
    
    

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}
