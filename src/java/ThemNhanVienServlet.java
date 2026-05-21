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

@WebServlet(urlPatterns = {"/ThemNhanVienServlet"})
public class ThemNhanVienServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Điều hướng về trang quản lý nếu cần
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cấu hình phản hồi JSON
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        Process_Data.Staff dao = new Process_Data.Staff(); 

        try {
            if ("update".equals(action)) {
                // 1. Lấy dữ liệu cập nhật (Có thêm Chức vụ)
                int maNV = Integer.parseInt(request.getParameter("manv"));
                String tenDN = request.getParameter("tdangnhapnv");
                String hoTen = request.getParameter("htnv");
                String gioiTinh = request.getParameter("gtnv");
                String ngaySinh = request.getParameter("nsnv");
                String diaChi = request.getParameter("dcnv");
                String chucVu = request.getParameter("cvnv");

                // 2. Gọi hàm update trong DAO
                int result = dao.updateNhanVien(maNV, tenDN, hoTen, gioiTinh, ngaySinh, diaChi, chucVu);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể cập nhật nhân viên.\"}");
                }

            } else if ("delete".equals(action)) {
                // Xử lý Xóa
                int maNV = Integer.parseInt(request.getParameter("manv"));        
                int result = dao.deleteNhanVien(maNV);
                
                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể xóa nhân viên.\"}");
                }

            } else if ("add".equals(action)) {
                // 1. Lấy dữ liệu thêm mới
                String tenDN = request.getParameter("tdangnhapnv");
                String hoTen = request.getParameter("htnv");
                String gioiTinh = request.getParameter("gtnv");
                String ngaySinh = request.getParameter("nsnv");
                String diaChi = request.getParameter("dcnv");
                String chucVu = request.getParameter("cvnv");

                // 2. Gọi hàm thêm mới
                int result = dao.themNhanVien(tenDN, hoTen, gioiTinh, ngaySinh, diaChi, chucVu);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể lưu nhân viên vào database.\"}");
                }
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}