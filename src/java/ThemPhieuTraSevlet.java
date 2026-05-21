/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mr.Khoa
 */
@WebServlet(urlPatterns = {"/ThemPhieuTraSevlet"})
public class ThemPhieuTraSevlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cấu hình UTF-8 để không bị lỗi font tiếng Việt khi nhận/gửi dữ liệu
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Process_Data.PhieuTra dao = new Process_Data.PhieuTra();
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                int maSach = Integer.parseInt(request.getParameter("masach"));
                String ngayTra = request.getParameter("ngaytra");
                if (dao.kiemdocgiavamasach(maDocGia, maSach) != null) {
                    int result = dao.themPhieuTra(maDocGia, maSach, ngayTra);
                    if (result > 0) {
                        out.print("{\"success\": true}");
                    } else {
                        out.print("{\"success\": false, \"message\": \"Không thể lập phiếu trả.\"}");
                    }
                } else {
                    out.print("{\"success\": false, \"message\": \"Không tồn tại phiếu mượn.\"}");
                }
            } else if ("update".equals(action)) {
                int maPhieuTra = Integer.parseInt(request.getParameter("maphieutra"));
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                int maSach = Integer.parseInt(request.getParameter("masach"));
                String ngayTra = request.getParameter("ngaytra");
                if (dao.kiemdocgiavamasach(maDocGia, maSach) != null) {
                    int result = dao.updatePhieuTra(maPhieuTra, maDocGia, maSach, ngayTra);
                    if (result > 0) {
                        out.print("{\"success\": true}");
                    } else {
                        out.print("{\"success\": false, \"message\": \"Cập nhật phiếu trả thất bại.\"}");
                    }
                } else {
                    out.print("{\"success\": false, \"message\": \"Không tồn tại phiếu mượn.\"}");
                }
            } else if ("delete".equals(action)) {
                int maPhieuTra = Integer.parseInt(request.getParameter("maphieutra"));
                int result = dao.deletePhieuTra(maPhieuTra);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể xóa phiếu trả này.\"}");
                }
            }
        } catch (Exception e) {
            // Trả về thông báo lỗi chi tiết nếu có ngoại lệ xảy ra
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
