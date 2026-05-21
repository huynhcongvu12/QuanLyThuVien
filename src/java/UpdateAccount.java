
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
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
@WebServlet("/UpdateAccount")
public class UpdateAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

// PHƯƠNG THỨC POST: Dùng để xử lý dữ liệu khi người dùng nhấn nút "Thêm"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        model.Account account = (model.Account) request.getSession().getAttribute("account");
        if (account != null) {
            if (account.getQuyen() == 1) {
                int madg = account.getMaDocGia();
                String name = request.getParameter("hoten");
                String diachi = request.getParameter("diachi");
                String ngaysinhRaw = request.getParameter("ngaysinh");
                java.sql.Date ngaysinh = null;
                if (ngaysinhRaw != null && !ngaysinhRaw.isEmpty()) {
                    ngaysinh = java.sql.Date.valueOf(ngaysinhRaw);
                }
                String gioitinh = request.getParameter("gioitinh");
                Process_Data.Customer cus = new Process_Data.Customer();
                cus.SuaAccount(madg, name, gioitinh, diachi, ngaysinh);
                
                String sophieumuon = cus.PhieuMuon(account.getMaDocGia());
                request.setAttribute("sophieumuon", sophieumuon);
                
                String sophieutra = cus.PhieuTra(account.getMaDocGia());
                request.setAttribute("sophieutra", sophieutra);
                
                String sophieuphat = cus.PhieuPhat(account.getMaDocGia());
                request.setAttribute("sophieuphat", sophieuphat);
                
                request.setAttribute("account", account);
                Vector<model.Sach> listsach = cus.DanhSachSach();
                request.setAttribute("listsach", listsach);
                
                Vector<model.ChiTietMuon> chitietmuon = cus.DachSachMuon(account.getMaDocGia());
                request.setAttribute("listmuon", chitietmuon);
                
                Vector<model.ChiTietTra> chitiettra = cus.DachSachTra(account.getMaDocGia());
                request.setAttribute("listtra", chitiettra);
                
                model.DocGia thongtindg = cus.ThongTinDG(account.getMaDocGia());
                request.setAttribute("thongtindg", thongtindg);
                
                request.getRequestDispatcher("Customer.jsp").forward(request, response);
            } else if (account.getQuyen() == 0) {
                request.setAttribute("acount", account);
                request.getRequestDispatcher("Admin.jsp").forward(request, response);
            } else {
                int madg = account.getMaNV();
                String name = request.getParameter("hoten");
                String diachi = request.getParameter("diachi");
                String ngaysinhRaw = request.getParameter("ngaysinh");
                java.sql.Date ngaysinh = null;
                if (ngaysinhRaw != null && !ngaysinhRaw.isEmpty()) {
                    ngaysinh = java.sql.Date.valueOf(ngaysinhRaw);
                }
                Process_Data.Admin admin = new Process_Data.Admin();
                String gioitinh = request.getParameter("gioitinh");
                Process_Data.Staff staff = new Process_Data.Staff();
                staff.SuaAccount(madg, name, gioitinh, diachi, ngaysinh);
                
                request.setAttribute("account", account);
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
                
                request.getRequestDispatcher("Staff.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
