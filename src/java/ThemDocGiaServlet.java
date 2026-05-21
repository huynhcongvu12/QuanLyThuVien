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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ThemDocGiaServlet"})
public class ThemDocGiaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể để trống hoặc điều hướng về trang quản lý
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cấu hình phản hồi về Client dưới dạng JSON[cite: 12]
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        // Giả sử lớp xử lý dữ liệu của bạn tên là DocGia nằm trong package Process_Data
        Process_Data.Customer dao = new Process_Data.Customer(); 

        try {
            if ("update".equals(action)) {
                // 1. Lấy dữ liệu cập nhật
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));
                String tenDN = request.getParameter("tendangnhap");
                String hoTen = request.getParameter("hoten");
                String gioiTinh = request.getParameter("gioitinh");
                String ngaySinh = request.getParameter("ngaysinh");
                String diaChi = request.getParameter("diachi");
                // 2. Gọi hàm update trong DAO
                int result = dao.updateDocGia(maDocGia, tenDN, hoTen, gioiTinh, ngaySinh, diaChi);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể cập nhật độc giả.\"}");
                }
            } else if ("delete".equals(action)) {
                int maDocGia = Integer.parseInt(request.getParameter("madocgia"));        
                int result = dao.deleteDocGia(maDocGia);
                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể xóa độc giả.\"}");
                }

            } else if ("add".equals(action)) {
                // 1. Lấy dữ liệu thêm mới[cite: 13]
                String tenDN = request.getParameter("tendangnhap");
                String hoTen = request.getParameter("hoten");
                String gioiTinh = request.getParameter("gioitinh");
                String ngaySinh = request.getParameter("ngaysinh");
                String diaChi = request.getParameter("diachi");

                // 2. Gọi hàm thêm mới
                int result = dao.themDocGia(tenDN, hoTen, gioiTinh, ngaySinh, diaChi);

                if (result > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Không thể lưu độc giả vào database.\"}");
                }
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        }
    }
}