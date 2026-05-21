
import java.io.IOException;
import java.time.LocalDate;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/CustomerBorrowServlet"})
public class CustomerBorrowServlet extends HttpServlet {

    private void loadCustomerPage(HttpServletRequest request, HttpServletResponse response, model.Account account, String message, String messageType)
            throws ServletException, IOException {
        Process_Data.Customer cus = new Process_Data.Customer();
        request.setAttribute("sophieumuon", cus.PhieuMuon(account.getMaDocGia()));
        request.setAttribute("sophieutra", cus.PhieuTra(account.getMaDocGia()));
        request.setAttribute("sophieuphat", cus.PhieuPhat(account.getMaDocGia()));
        Vector<model.Sach> listsach = cus.DanhSachSach();
        request.setAttribute("listsach", listsach);
        request.setAttribute("listmuon", cus.DachSachMuon(account.getMaDocGia()));
        request.setAttribute("listtra", cus.DachSachTra(account.getMaDocGia()));
        request.setAttribute("listphieuphat", cus.DanhSachPhieuPhat(account.getMaDocGia()));
        request.setAttribute("thongtindg", cus.ThongTinDG(account.getMaDocGia()));
        request.setAttribute("account", account);
        request.setAttribute("borrowMessage", message);
        request.setAttribute("borrowMessageType", messageType);
        request.getRequestDispatcher("Customer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        model.Account account = (model.Account) request.getSession().getAttribute("account");
        if (account == null || account.getQuyen() != 1) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            int maSach = Integer.parseInt(request.getParameter("masach"));
            Process_Data.PhieuMuon dao = new Process_Data.PhieuMuon();

            if (dao.kiemMaSach(maSach) == null) {
                loadCustomerPage(request, response, account, "Không tồn tại mã sách.", "error");
                return;
            }
            if (dao.kiemTraSach(maSach) <= 0) {
                loadCustomerPage(request, response, account, "Sách đã hết, không thể mượn.", "error");
                return;
            }
            if (dao.coSachQuaHan(account.getMaDocGia())) {
                loadCustomerPage(request, response, account, "Bạn đang có sách quá hạn, không thể mượn thêm.", "error");
                return;
            }
            if (dao.soSachDangMuon(account.getMaDocGia()) >= 5) {
                loadCustomerPage(request, response, account, "Bạn đã đạt giới hạn 5 quyển đang mượn/chờ duyệt.", "error");
                return;
            }
            if (dao.dangMuonChuaTra(account.getMaDocGia(), maSach)) {
                loadCustomerPage(request, response, account, "Bạn đã gửi yêu cầu hoặc đang mượn sách này.", "error");
                return;
            }

            LocalDate today = LocalDate.now();
            LocalDate dueDate = today.plusDays(14);
            int result = dao.themYeuCauMuon(account.getMaDocGia(), maSach, today.toString(), dueDate.toString());
            if (result > 0) {
                loadCustomerPage(request, response, account, "Yêu cầu mượn sách đã được gửi. Vui lòng chờ nhân viên duyệt và xác nhận giao sách.", "success");
            } else if (result == -1) {
                loadCustomerPage(request, response, account, "Sách vừa hết, không thể gửi yêu cầu mượn.", "error");
            } else {
                loadCustomerPage(request, response, account, "Không thể gửi yêu cầu mượn.", "error");
            }
        } catch (Exception ex) {
            loadCustomerPage(request, response, account, "Lỗi mượn sách: " + ex.getMessage(), "error");
        }
    }
}
