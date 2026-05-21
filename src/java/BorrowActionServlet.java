import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/BorrowActionServlet"})
public class BorrowActionServlet extends HttpServlet {

    private void loadStaff(HttpServletRequest request, HttpServletResponse response, model.Account account, String msg)
            throws ServletException, IOException {
        Process_Data.Admin admin = new Process_Data.Admin();
        Process_Data.Staff staff = new Process_Data.Staff();
        request.setAttribute("listsach", staff.DanhSachSach());
        request.setAttribute("phieumuonhomnay", staff.PhieuMuonHomNay());
        request.setAttribute("phieutrahomnay", staff.PhieuTraHomNay());
        request.setAttribute("soluong", staff.TongSoLuongSach());
        request.setAttribute("sosanh", staff.SoSanhPhieuMuon());
        request.setAttribute("sophieuquahan", staff.DemSoPhieuQuaHan());
        Vector<model.PhieuMuon> listphieumuon = admin.layTatCaPhieuMuon();
        request.setAttribute("listphieumuon", listphieumuon);
        request.setAttribute("listphieutra", admin.getAllPhieuTra());
        request.setAttribute("thongtindg", staff.ThongTinDG(account.getMaNV()));
        request.setAttribute("account", account);
        request.setAttribute("borrowActionMessage", msg);
        request.getRequestDispatcher("Staff.jsp").forward(request, response);
    }

    private void loadAdmin(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        Process_Data.Admin admin = new Process_Data.Admin();
        request.setAttribute("listsach", admin.DanhSachSach());
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
        request.setAttribute("borrowActionMessage", msg);
        request.getRequestDispatcher("Admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        model.Account account = (model.Account) request.getSession().getAttribute("account");
        if (account == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
        if (account.getQuyen() != 0 && account.getQuyen() != 2) {
            response.sendRedirect("error/403.jsp");
            return;
        }
        int maNV = account.getMaNV();
        int maPhieu = Integer.parseInt(request.getParameter("maPhieu"));
        String action = request.getParameter("action");
        Process_Data.PhieuMuon dao = new Process_Data.PhieuMuon();
        int result = 0;
        String msg;
        if ("approve".equals(action)) {
            result = dao.duyetYeuCau(maPhieu, maNV);
            msg = result > 0 ? "Đã duyệt yêu cầu mượn." : "Không thể duyệt yêu cầu.";
        } else if ("reject".equals(action)) {
            String reason = request.getParameter("reason");
            result = dao.tuChoiYeuCau(maPhieu, maNV, reason);
            msg = result > 0 ? "Đã từ chối yêu cầu mượn." : "Không thể từ chối yêu cầu.";
        } else if ("handover".equals(action)) {
            result = dao.xacNhanGiaoSach(maPhieu, maNV);
            msg = result > 0 ? "Đã xác nhận giao sách." : "Không thể xác nhận giao sách.";
        } else if ("return".equals(action)) {
            String tinhTrangSach = request.getParameter("tinhTrangSach");
            String note = request.getParameter("note");
            result = dao.xacNhanTraSach(maPhieu, tinhTrangSach, note);
            msg = result > 0 ? "Đã xác nhận trả sách." : "Không thể xác nhận trả sách.";
        } else {
            msg = "Thao tác không hợp lệ.";
        }
        if (account.getQuyen() == 0) {
            loadAdmin(request, response, msg);
        } else {
            loadStaff(request, response, account, msg);
        }
    }
}
