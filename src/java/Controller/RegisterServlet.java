package Controller;

import ConnectionData.CONNECTIONSQLSERVER;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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

        String hoTen = request.getParameter("hoTen");
        String tenDangNhap = request.getParameter("tenDangNhap");
        String email = request.getParameter("email");
        String soDienThoai = request.getParameter("soDienThoai");
        String gioiTinh = request.getParameter("gioiTinh");
        String ngaySinh = request.getParameter("ngaySinh");
        String diaChi = request.getParameter("diaChi");
        String matKhau = request.getParameter("matKhau");
        String confirmPassword = request.getParameter("confirmPassword");

        if (hoTen == null || hoTen.trim().isEmpty()
                || tenDangNhap == null || tenDangNhap.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc!");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
            return;
        }

        if (!matKhau.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
            return;
        }

        CONNECTIONSQLSERVER db = new CONNECTIONSQLSERVER();

        try {
            db.Open();

            if (db.cnn == null) {
                request.setAttribute("error", db.databaseErrorMessage());
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            String checkUserSql = "SELECT COUNT(*) FROM ACCOUNT WHERE TenDangNhap = ?";
            PreparedStatement checkUser = db.cnn.prepareStatement(checkUserSql);
            checkUser.setString(1, tenDangNhap.trim());

            ResultSet rsUser = checkUser.executeQuery();

            if (rsUser.next() && rsUser.getInt(1) > 0) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            String checkEmailSql = "SELECT COUNT(*) FROM DOCGIA WHERE Email = ?";
            PreparedStatement checkEmail = db.cnn.prepareStatement(checkEmailSql);
            checkEmail.setString(1, email.trim());

            ResultSet rsEmail = checkEmail.executeQuery();

            if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                request.setAttribute("error", "Email đã tồn tại!");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            String insertDocGiaSql = "INSERT INTO DOCGIA "
                    + "(TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, Email, SoDienThoai) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement psDocGia = db.cnn.prepareStatement(insertDocGiaSql, Statement.RETURN_GENERATED_KEYS);
            psDocGia.setString(1, tenDangNhap.trim());
            psDocGia.setString(2, hoTen.trim());
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

            String insertAccountSql = "INSERT INTO ACCOUNT "
                    + "(TenDangNhap, MatKhau, Quyen, MaDocGia, MaNV) "
                    + "VALUES (?, ?, 1, ?, NULL)";

            PreparedStatement psAccount = db.cnn.prepareStatement(insertAccountSql);
            psAccount.setString(1, tenDangNhap.trim());
            psAccount.setString(2, matKhau.trim());
            psAccount.setInt(3, maDocGia);

            psAccount.executeUpdate();

            response.sendRedirect("Login.jsp?register=success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        } finally {
            db.Close();
        }
    }
}
