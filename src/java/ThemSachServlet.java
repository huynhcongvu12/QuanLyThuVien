import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ThemSachServlet")
public class ThemSachServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");
            Process_Data.Sach dao = new Process_Data.Sach();

            if ("delete".equals(action)) {
                int maSach = parsePositiveInt(request.getParameter("masach"), "Ma sach khong hop le.");
                int result = dao.deleteSach(maSach);
                writeJson(out, result > 0, result > 0 ? "" : "Khong the xoa sach. Kiem tra rang buoc phieu muon/tra.");
                return;
            }

            String ten = required(request.getParameter("tensach"), "Ten sach khong duoc de trong.");
            String tacGia = required(request.getParameter("tacgia"), "Tac gia khong duoc de trong.");
            String theLoai = required(request.getParameter("theloai"), "The loai khong duoc de trong.");
            String nxb = required(request.getParameter("nhaxuatban"), "Nha xuat ban khong duoc de trong.");
            double gia = parseNonNegativeDouble(request.getParameter("giasach"), "Gia sach phai la so khong am.");
            int soLuong = parseNonNegativeInt(request.getParameter("soluong"), "So luong phai la so nguyen khong am.");

            int result;
            if ("update".equals(action)) {
                int maSach = parsePositiveInt(request.getParameter("masach"), "Ma sach khong hop le.");
                result = dao.updateSach(maSach, ten, tacGia, theLoai, nxb, gia, soLuong);
                writeJson(out, result > 0, result > 0 ? "" : "Khong the cap nhat sach.");
            } else if ("add".equals(action)) {
                result = dao.themSach(ten, tacGia, theLoai, nxb, gia, soLuong);
                writeJson(out, result > 0, result > 0 ? "" : "Khong the luu sach vao database.");
            } else {
                writeJson(out, false, "Thao tac khong hop le.");
            }
        } catch (IllegalArgumentException ex) {
            writeJson(out, false, ex.getMessage());
        } catch (Exception ex) {
            writeJson(out, false, "Loi he thong khi xu ly sach. Kiem tra du lieu va thu lai.");
        }
    }

    private String required(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private int parsePositiveInt(String value, String message) {
        int number = parseNonNegativeInt(value, message);
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    private int parseNonNegativeInt(String value, String message) {
        try {
            int number = Integer.parseInt(value);
            if (number < 0) {
                throw new IllegalArgumentException(message);
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private double parseNonNegativeDouble(String value, String message) {
        try {
            double number = Double.parseDouble(value);
            if (number < 0) {
                throw new IllegalArgumentException(message);
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private void writeJson(PrintWriter out, boolean success, String message) {
        out.print("{\"success\":" + success + ",\"message\":\"" + escapeJson(message) + "\"}");
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
