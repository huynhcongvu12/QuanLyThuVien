package Controller;

import ConnectionData.CONNECTIONSQLSERVER;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String hoTen = trim(request.getParameter("hoTen"));
        String tenDangNhap = trim(request.getParameter("tenDangNhap"));
        String email = trim(request.getParameter("email"));
        String soDienThoai = trim(request.getParameter("soDienThoai"));
        String gioiTinh = trim(request.getParameter("gioiTinh"));
        String ngaySinh = trim(request.getParameter("ngaySinh"));
        String diaChi = trim(request.getParameter("diaChi"));
        String matKhau = trim(request.getParameter("matKhau"));
        String confirmPassword = trim(request.getParameter("confirmPassword"));

        if (hoTen.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            fail(request, response, "Vui lòng nhập đầy đủ họ tên, tên đăng nhập và mật khẩu.");
            return;
        }

        if (tenDangNhap.length() < 3 || matKhau.length() < 3) {
            fail(request, response, "Tên đăng nhập và mật khẩu phải có ít nhất 3 ký tự.");
            return;
        }

        if (!matKhau.equals(confirmPassword)) {
            fail(request, response, "Mật khẩu xác nhận không khớp.");
            return;
        }

        if (!email.isEmpty() && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            fail(request, response, "Email không hợp lệ.");
            return;
        }

        if (!soDienThoai.isEmpty() && !soDienThoai.matches("^[0-9+ ]{9,15}$")) {
            fail(request, response, "Số điện thoại không hợp lệ.");
            return;
        }

        CONNECTIONSQLSERVER db = new CONNECTIONSQLSERVER();

        try {
            db.Open();

            if (db.cnn == null) {
                fail(request, response, db.databaseErrorMessage());
                return;
            }

            PreparedStatement checkUser = db.cnn.prepareStatement("SELECT COUNT(*) FROM ACCOUNT WHERE TenDangNhap = ?");
            checkUser.setString(1, tenDangNhap);
            ResultSet rsUser = checkUser.executeQuery();
            if (rsUser.next() && rsUser.getInt(1) > 0) {
                fail(request, response, "Tên đăng nhập đã tồn tại.");
                return;
            }

            if (!email.isEmpty()) {
                PreparedStatement checkEmail = db.cnn.prepareStatement("SELECT COUNT(*) FROM DOCGIA WHERE Email = ?");
                checkEmail.setString(1, email);
                ResultSet rsEmail = checkEmail.executeQuery();
                if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                    fail(request, response, "Email đã tồn tại.");
                    return;
                }
            }

            String insertDocGiaSql = "INSERT INTO DOCGIA "
                    + "(TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, Email, SoDienThoai) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement psDocGia = db.cnn.prepareStatement(insertDocGiaSql, Statement.RETURN_GENERATED_KEYS);
            psDocGia.setString(1, tenDangNhap);
            psDocGia.setString(2, hoTen);
            psDocGia.setString(3, gioiTinh);
            psDocGia.setString(4, ngaySinh);
            psDocGia.setString(5, diaChi);
            psDocGia.setString(6, email);
            psDocGia.setString(7, soDienThoai);
            psDocGia.executeUpdate();

            ResultSet generatedKeys = psDocGia.getGeneratedKeys();
            int maDocGia = 0;
            if (generatedKeys.next()) {
                maDocGia = generatedKeys.getInt(1);
            }

            PreparedStatement psAccount = db.cnn.prepareStatement(
                    "INSERT INTO ACCOUNT (TenDangNhap, MatKhau, Quyen, MaDocGia, MaNV) VALUES (?, ?, 1, ?, NULL)");
            psAccount.setString(1, tenDangNhap);
            psAccount.setString(2, matKhau);
            psAccount.setInt(3, maDocGia);
            psAccount.executeUpdate();

            response.sendRedirect("Login.jsp?register=success");

        } catch (Exception e) {
            fail(request, response, "Lỗi hệ thống khi đăng ký. Vui lòng kiểm tra dữ liệu và thử lại.");
        } finally {
            db.Close();
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void fail(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("Register.jsp").forward(request, response);
    }
}
