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
@WebServlet(urlPatterns = {"/ThemPhieuMuonServlet"})
public class ThemPhieuMuonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể điều hướng về trang quản lý nếu truy cập trực tiếp bằng trình duyệt
        response.sendRedirect("Admin_8.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Process_Data.PhieuMuon dao = new Process_Data.PhieuMuon();

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Xử lý thêm mới Phiếu mượn
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                int maSach = Integer.parseInt(request.getParameter("masach"));
                String ngayMuon = request.getParameter("ngaymuon");
                String ngayPhaiTra = request.getParameter("ngayphaitra");
                if (dao.kiemDocGia(maDocGia) != null) {
                    if (dao.kiemMaSach(maSach) != null) {
                        if (dao.kiemTraSach(maSach) != 0) {
                            int result = dao.themPhieuMuon(maDocGia, maSach, ngayMuon, ngayPhaiTra);
                            if (result > 0) {
                                out.print("{\"success\": true}");
                            } else {
                                out.print("{\"success\": false, \"message\": \"Không thể lập phiếu mượn.\"}");
                            }
                        } else {
                            out.print("{\"success\": false, \"message\": \"Hết sách.\"}");
                        }
                    } else {
                        out.print("{\"success\": false, \"message\": \"Không tồn tại mã sach.\"}");
                    }
                } else {
                    out.print("{\"success\": false, \"message\": \"Không tồn tại mã độc giả.\"}");
                }
            } else if ("update".equals(action)) {
                // Xử lý cập nhật Phiếu mượn
                int maPhieu = Integer.parseInt(request.getParameter("maphieu"));
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                int maSach = Integer.parseInt(request.getParameter("masach"));
                String ngayMuon = request.getParameter("ngaymuon");
                String ngayPhaiTra = request.getParameter("ngayphaitra");
                if (dao.kiemDocGia(maDocGia) != null) {
                    if (dao.kiemMaSach(maSach) != null) {
                        if (dao.kiemTraSach(maSach) != 0) {
                            int result = dao.updatePhieuMuon(maPhieu, maDocGia, maSach, ngayMuon, ngayPhaiTra);
                            if (result > 0) {
                                out.print("{\"success\": true}");
                            } else {
                                out.print("{\"success\": false, \"message\": \"Cập nhật phiếu mượn thất bại.\"}");
                            }
                        } else {
                            out.print("{\"success\": false, \"message\": \"Hết sách.\"}");
                        }
                    } else {
                        out.print("{\"success\": false, \"message\": \"Không tồn tại mã sách.\"}");
                    }
                } else {
                    out.print("{\"success\": false, \"message\": \"Không tồn tại mã độc giả.\"}");
                }
            } else if ("delete".equals(action)) {
                // Xử lý xóa Phiếu mượn[cite: 8]
                int maPhieu = Integer.parseInt(request.getParameter("maphieu"));

                int result = dao.deletePhieuMuon(maPhieu);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể xóa phiếu mượn này.\"}");
                }
            }
        } catch (Exception e) {
            // Trả về lỗi hệ thống dưới dạng JSON để JavaScript catch được và alert
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
