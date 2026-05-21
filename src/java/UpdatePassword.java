
import java.io.IOException;
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
@WebServlet("/UpdatePassword")
public class UpdatePassword extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

// PHƯƠNG THỨC POST: Dùng để xử lý dữ liệu khi người dùng nhấn nút "Thêm"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        model.Account Account = (model.Account) request.getSession().getAttribute("account");
        Process_Data.Login log = new Process_Data.Login();
        model.Account account = new model.Account();
        if (Account == null) {
            request.setAttribute("error", "Session hết hạn, vui lòng đăng nhập lại!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
            if (Account.getQuyen() == 1) {
                int madg = Account.getMaDocGia();
                String matkhaucu = request.getParameter("matkhaucu");
                String matkhaumoi1 = request.getParameter("matkhaumoi1");
                String matkhaumoi2 = request.getParameter("matkhaumoi2");
                Process_Data.Customer cus = new Process_Data.Customer();
                model.Account checkAccount = log.login(Account.getTenDangNhap(), matkhaucu);
                if (checkAccount == null) {
                    request.setAttribute("s", "Mật khẩu hiện tại không đúng!");
                } else {
                    try {
                        cus.SuaMatKhau(madg, matkhaucu, matkhaumoi1);
                        account = log.login(Account.getTenDangNhap(), matkhaumoi1);
                        request.getSession().setAttribute("account", account);
                        request.setAttribute("s", "Đổi mật khẩu thành công!");
                    } catch (Exception e) {
                        request.setAttribute("s", "Lỗi khi cập nhật mật khẩu!");
                    }
                }
                String sophieumuon = cus.PhieuMuon(Account.getMaDocGia());
                request.setAttribute("sophieumuon", sophieumuon);
                
                String sophieutra = cus.PhieuTra(Account.getMaDocGia());
                request.setAttribute("sophieutra", sophieutra);
                
                String sophieuphat = cus.PhieuPhat(Account.getMaDocGia());
                request.setAttribute("sophieuphat", sophieuphat);
                
                request.setAttribute("account", Account);
                Vector<model.Sach> listsach = cus.DanhSachSach();
                request.setAttribute("listsach", listsach);
                
                Vector<model.ChiTietMuon> chitietmuon = cus.DachSachMuon(Account.getMaDocGia());
                request.setAttribute("listmuon", chitietmuon);
                
                Vector<model.ChiTietTra> chitiettra = cus.DachSachTra(Account.getMaDocGia());
                request.setAttribute("listtra", chitiettra);
                
                model.DocGia thongtindg = cus.ThongTinDG(Account.getMaDocGia());
                request.setAttribute("thongtindg", thongtindg);
                
                request.getRequestDispatcher("Customer.jsp").forward(request, response);
            } else if (Account.getQuyen() == 0) {
                request.setAttribute("account", Account);
                request.getRequestDispatcher("Admin.jsp").forward(request, response);
            } else {
                int manv = Account.getMaNV();
                String matkhaucu = request.getParameter("matkhaucu");
                String matkhaumoi1 = request.getParameter("matkhaumoi1");
                String matkhaumoi2 = request.getParameter("matkhaumoi2");
                Process_Data.Staff staff = new Process_Data.Staff();
                model.Account checkAccount = log.login(Account.getTenDangNhap(), matkhaucu);
                 if (checkAccount == null) {
                    request.setAttribute("s", "Mật khẩu hiện tại không đúng!");
                } else {
                    try {
                        staff.SuaMatKhau(manv, matkhaucu, matkhaumoi1);
                        account = log.login(Account.getTenDangNhap(), matkhaumoi1);
                        request.getSession().setAttribute("account", account);
                        request.setAttribute("s", "Đổi mật khẩu thành công!");
                    } catch (Exception e) {
                        request.setAttribute("s", "Lỗi khi cập nhật mật khẩu!");
                    }
                }
                Process_Data.Admin admin = new Process_Data.Admin();
                request.setAttribute("account", Account);
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
                
                model.DocGia thongtindg = staff.ThongTinDG(Account.getMaNV());
                request.setAttribute("thongtindg", thongtindg);
                
                request.getRequestDispatcher("Staff.jsp").forward(request, response);
            }
    }
}
