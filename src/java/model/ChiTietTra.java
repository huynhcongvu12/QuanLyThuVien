/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Mr.Khoa
 */
public class ChiTietTra {
    private String maPhieu;
    private String tenSach;
    private Date hanTra;
    private String trangThai;
    
    public ChiTietTra() {
       
    }
    // Các hàm Getters nếu cần lấy dữ liệu
    public String getTenSach() 
    { 
        return tenSach; 
    }
    public Date getHanTra() 
    { 
        return hanTra; 
    }
    public String getMaPhieu() 
    { 
        return maPhieu; 
    }
    public String getTrangThai(){
        return trangThai;
    }
    public void setTenSach(String tenSach){
        this.tenSach=tenSach;
    }
    public void setHanTra(Date hanTra){
        this.hanTra=hanTra;
    }
    public void setMaPhieu(String maPhieu){
        this.maPhieu =maPhieu;
    }
    public void setTrangThai(String trangThai){
        this.trangThai = trangThai;
    }
}
