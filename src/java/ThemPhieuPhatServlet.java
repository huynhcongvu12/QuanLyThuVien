/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mr.Khoa
 */
@WebServlet(urlPatterns = {"/ThemPhieuPhatServlet"})
public class ThemPhieuPhatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể để trống hoặc dùng để forward trang
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Cấu hình tiếng Việt và định dạng JSON
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // 2. Khởi tạo lớp xử lý dữ liệu (DAO)
        // Giả sử lớp PhieuPhat của bạn nằm trong package Process_Data
        Process_Data.PhieuPhat dao = new Process_Data.PhieuPhat();
        String action = request.getParameter("action");
        Gson gson = new Gson();

        try {
            if ("add".equals(action)) {
                // Lấy dữ liệu từ request
                int maPhieu = Integer.parseInt(request.getParameter("maphieu")); // Mã phiếu mượn/trả liên quan
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                String lyDo = request.getParameter("lydo");
                float soTien = Float.parseFloat(request.getParameter("sotien"));
                String ngayLap = request.getParameter("ngaylap");
                int trangThai = Integer.parseInt(request.getParameter("trangthai"));

                int result = dao.themPhieuPhat(maPhieu, maDocGia, lyDo, soTien, ngayLap, trangThai);
                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể thêm phiếu phạt.\"}");
                }

            } else if ("update".equals(action)) {
                int maPhieuPhat = Integer.parseInt(request.getParameter("maphieuphat"));
                int maPhieu = Integer.parseInt(request.getParameter("maphieu"));
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                String lyDo = request.getParameter("lydo");
                float soTien = Float.parseFloat(request.getParameter("sotien"));
                String ngayLap = request.getParameter("ngaylap");
                int trangThai = Integer.parseInt(request.getParameter("trangthai"));
                int result = dao.updatePhieuPhat(maPhieuPhat, maPhieu, maDocGia, lyDo, soTien, ngayLap, trangThai);
                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Cập nhật phiếu phạt thất bại.\"}");
                }

            } else if ("delete".equals(action)) {
                int maPhieuPhat = Integer.parseInt(request.getParameter("maphieuphat"));
                int result = dao.deletePhieuPhat(maPhieuPhat);
                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Xóa phiếu phạt thất bại.\"}");
                }

            } else if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                if (keyword == null) keyword = "";
                
                Vector<model.PhieuPhat> list = dao.searchPhieuPhat(keyword);
                out.print(gson.toJson(list));
            }

        } catch (Exception e) {
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
