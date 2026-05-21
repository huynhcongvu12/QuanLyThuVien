

import java.io.IOException;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Mr.Khoa
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Process_Data.Login log = new Process_Data.Login();
        model.Account account;
        try {
            account = log.login(email, password);
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        
        if (account != null) {
            request.getSession().setAttribute("account", account);
            
            if (account.getQuyen() == 1) {  // Customer
                Process_Data.Customer cus = new Process_Data.Customer();
                String sophieumuon = cus.PhieuMuon(account.getMaDocGia());
                request.setAttribute("sophieumuon", sophieumuon);
                String sophieutra = cus.PhieuTra(account.getMaDocGia());
                request.setAttribute("sophieutra", sophieutra);
                String sophieuphat = cus.PhieuPhat(account.getMaDocGia());
                request.setAttribute("sophieuphat", sophieuphat);
                Vector<model.Sach> listsach = cus.DanhSachSach();
                request.setAttribute("listsach", listsach);
                Vector<model.ChiTietMuon> chitietmuon = cus.DachSachMuon(account.getMaDocGia());
                request.setAttribute("listmuon", chitietmuon);
                Vector<model.ChiTietTra> chitiettra = cus.DachSachTra(account.getMaDocGia());
                request.setAttribute("listtra", chitiettra);
                request.setAttribute("listphieuphat", cus.DanhSachPhieuPhat(account.getMaDocGia()));
                
                model.DocGia thongtindg = cus.ThongTinDG(account.getMaDocGia());
                request.setAttribute("thongtindg", thongtindg);
                request.setAttribute("account", account);
                
                request.getRequestDispatcher("Customer.jsp").forward(request, response);
                
            } else if (account.getQuyen() == 0) {  // Admin
                Process_Data.Admin admin = new Process_Data.Admin();
                Vector<model.Sach> listsach = admin.DanhSachSach();
                request.setAttribute("listsach", listsach);
                              
                Vector<model.Sach> phieumuonthang = admin.DanhSachSach();
                request.setAttribute("listsach", listsach);
                
                String luotmuonthang = admin.LuotMuonThangHienTai();
                request.setAttribute("luotmuonthang", luotmuonthang);
                
                Vector<String> pt = admin.PhanTramTangTruong();
                request.setAttribute("pt", pt);
                
                String luottrathang = admin.LuotTraThangHienTai();
                request.setAttribute("luottrathang", luottrathang);
                
                String phatthang = admin.DemTongPhieuPhatThang();
                request.setAttribute("phatthang", phatthang);
                
                String phatthangchuatra = admin.DemPhieuPhatChuaTra();
                request.setAttribute("phatthangchuatra", phatthangchuatra);
                
                String tongtienphat = admin.TinhTongTienPhatThang();
                request.setAttribute("tongtienphat", tongtienphat);
                
                Vector<model.DocGia> listdocgia = admin.DanhSachDG();
                request.setAttribute("listdocgia", listdocgia);
                
                Vector<model.NhanVien> listnhanvien = admin.DanhSachNV();
                request.setAttribute("listnhanvien", listnhanvien);
                
                Vector<model.PhieuMuon> listphieumuon = admin.layTatCaPhieuMuon();
                request.setAttribute("listphieumuon", listphieumuon);
                
                Vector<model.PhieuTra> listphieutra = admin.getAllPhieuTra();
                request.setAttribute("listphieutra", listphieutra);
                
                Vector<model.PhieuPhat> listphieuphat = admin.getAllphieutphat();
                request.setAttribute("listphieuphat", listphieuphat);
                
                request.getRequestDispatcher("Admin.jsp").forward(request, response);
                
            } else {  // Staff
                Process_Data.Admin admin = new Process_Data.Admin();
                Process_Data.Staff staff = new Process_Data.Staff();
                Vector<model.Sach> listsach = staff.DanhSachSach();
                request.setAttribute("listsach", listsach);
             
                String phieumuonhomnay = staff.PhieuMuonHomNay();
                request.setAttribute("phieumuonhomnay", phieumuonhomnay);

                String phieutrahomnay = staff.PhieuTraHomNay();
                request.setAttribute("phieutrahomnay", phieutrahomnay);
                
                String soluong = staff.TongSoLuongSach();
                request.setAttribute("soluong", soluong);
                
                String sosanh = staff.SoSanhPhieuMuon();
                request.setAttribute("sosanh", sosanh);
                
                String sophieuquahan = staff.DemSoPhieuQuaHan();
                request.setAttribute("sophieuquahan", sophieuquahan);
                
                Vector<model.PhieuMuon> listphieumuon = admin.layTatCaPhieuMuon();
                request.setAttribute("listphieumuon", listphieumuon);
                
                Vector<model.PhieuTra> listphieutra = admin.getAllPhieuTra();
                request.setAttribute("listphieutra", listphieutra);
                
                model.DocGia thongtindg = staff.ThongTinDG(account.getMaNV());
                request.setAttribute("thongtindg", thongtindg);
                request.setAttribute("account", account);
                
                request.getRequestDispatcher("Staff.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
