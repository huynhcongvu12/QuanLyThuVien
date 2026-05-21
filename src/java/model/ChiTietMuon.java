/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Mr.Khoa
 */
public class ChiTietMuon {
    private int maPhieu;
    private String tenSach;
    private String ngayMuon;
    private String ngayTra;
    private String trangThai;
    public ChiTietMuon() {
       
    }
    // Các hàm Getters nếu cần lấy dữ liệu
    public String getTenSach() 
    { 
        return tenSach; 
    }
    public String getNgayMuon() 
    { 
        return ngayMuon; 
    }
    public String getTrangThai(){
        return trangThai;
    }
    public int getMaPhieu(){
        return maPhieu;
    }
    public String getNgayTra(){
        return ngayTra;
    }
    public void setMaPhieu(int maPhieu){
        this.maPhieu= maPhieu;
    }
    public void setTrangThai(String trangThai){
        this.trangThai = trangThai;
    }
    public void setTenSach(String tenSach){
        this.tenSach=tenSach;
    }
    public void setNgayMuon(String ngayMuon){
        this.ngayMuon=ngayMuon;
    }
    public void setNgayTra(String ngayTra){
        this.ngayTra=ngayTra;
    }
}
