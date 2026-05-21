import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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

        if (account == null) {
            request.setAttribute("error", "Sai tai khoan hoac mat khau.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        request.getSession().invalidate();
        request.getSession(true).setAttribute("account", account);

        if (account.getQuyen() == 1) {
            loadCustomer(request, response, account);
        } else if (account.getQuyen() == 0) {
            loadAdmin(request, response, account);
        } else if (account.getQuyen() == 2) {
            loadStaff(request, response, account);
        } else {
            request.getSession().invalidate();
            request.setAttribute("error", "Tai khoan chua duoc gan quyen hop le.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    private void loadCustomer(HttpServletRequest request, HttpServletResponse response, model.Account account)
            throws ServletException, IOException {
        Process_Data.Customer cus = new Process_Data.Customer();
        request.setAttribute("sophieumuon", cus.PhieuMuon(account.getMaDocGia()));
        request.setAttribute("sophieutra", cus.PhieuTra(account.getMaDocGia()));
        request.setAttribute("sophieuphat", cus.PhieuPhat(account.getMaDocGia()));
        request.setAttribute("listsach", cus.DanhSachSach());
        request.setAttribute("listmuon", cus.DachSachMuon(account.getMaDocGia()));
        request.setAttribute("listtra", cus.DachSachTra(account.getMaDocGia()));
        request.setAttribute("listphieuphat", cus.DanhSachPhieuPhat(account.getMaDocGia()));
        request.setAttribute("thongtindg", cus.ThongTinDG(account.getMaDocGia()));
        request.setAttribute("account", account);
        request.getRequestDispatcher("Customer.jsp").forward(request, response);
    }

    private void loadAdmin(HttpServletRequest request, HttpServletResponse response, model.Account account)
            throws ServletException, IOException {
        Process_Data.Admin admin = new Process_Data.Admin();
        Vector<model.Sach> listsach = admin.DanhSachSach();
        request.setAttribute("listsach", listsach);
        request.setAttribute("luotmuonthang", admin.LuotMuonThangHienTai());
        request.setAttribute("pt", admin.PhanTramTangTruong());
        request.setAttribute("luottrathang", admin.LuotTraThangHienTai());
        request.setAttribute("phatthang", admin.DemTongPhieuPhatThang());
        request.setAttribute("phatthangchuatra", admin.DemPhieuPhatChuaTra());
        request.setAttribute("tongtienphat", admin.TinhTongTienPhatThang());
        request.setAttribute("listdocgia", admin.DanhSachDG());
        request.setAttribute("listnhanvien", admin.DanhSachNV());
        request.setAttribute("listphieumuon", admin.layTatCaPhieuMuon());
        request.setAttribute("listphieutra", admin.getAllPhieuTra());
        request.setAttribute("listphieuphat", admin.getAllphieutphat());
        request.setAttribute("account", account);
        request.getRequestDispatcher("Admin.jsp").forward(request, response);
    }

    private void loadStaff(HttpServletRequest request, HttpServletResponse response, model.Account account)
            throws ServletException, IOException {
        Process_Data.Admin admin = new Process_Data.Admin();
        Process_Data.Staff staff = new Process_Data.Staff();
        request.setAttribute("listsach", staff.DanhSachSach());
        request.setAttribute("phieumuonhomnay", staff.PhieuMuonHomNay());
        request.setAttribute("phieutrahomnay", staff.PhieuTraHomNay());
        request.setAttribute("soluong", staff.TongSoLuongSach());
        request.setAttribute("sosanh", staff.SoSanhPhieuMuon());
        request.setAttribute("sophieuquahan", staff.DemSoPhieuQuaHan());
        request.setAttribute("listphieumuon", admin.layTatCaPhieuMuon());
        request.setAttribute("listphieutra", admin.getAllPhieuTra());
        request.setAttribute("thongtindg", staff.ThongTinDG(account.getMaNV()));
        request.setAttribute("account", account);
        request.getRequestDispatcher("Staff.jsp").forward(request, response);
    }
}
