
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Mr.Khoa
 */
@WebServlet("/ThemSachServlet")
public class ThemSachServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        Process_Data.Sach dao = new Process_Data.Sach();
        if ("update".equals(action)) {
            String ten = request.getParameter("tensach");
            String tacGia = request.getParameter("tacgia");
            String theLoai = request.getParameter("theloai");
            String nxb = request.getParameter("nhaxuatban");
            double gia = Double.parseDouble(request.getParameter("giasach"));
            int soLuong = Integer.parseInt(request.getParameter("soluong"));
            int maSach = Integer.parseInt(request.getParameter("masach"));
            int result = dao.updateSach(maSach, ten, tacGia, theLoai, nxb, gia, soLuong);
            if (result > 0) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không thể cập nhật database.\"}");
            }
        } else if ("delete".equals(action)) {
            int maSach = Integer.parseInt(request.getParameter("masach"));
            int result = dao.deleteSach(maSach);

            if (result > 0) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không thể xóa sách.\"}");
            }
        } else if ("add".equals(action)) {
            // 1. Lấy dữ liệu từ request
            String ten = request.getParameter("tensach");
            String tacGia = request.getParameter("tacgia");
            String theLoai = request.getParameter("theloai");
            String nxb = request.getParameter("nhaxuatban");
            Double gia = Double.parseDouble(request.getParameter("giasach"));
            int soLuong = Integer.parseInt(request.getParameter("soluong"));

            int result = dao.themSach(ten, tacGia, theLoai, nxb, gia, soLuong);

            if (result > 0) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không thể lưu vào database\"}");
            }
        } else {
            // Xử lý logic search hiện tại của bạn ở đây và trả về mảng JSON
            // (Sử dụng hàm DanhSachSach() của bạn để lấy dữ liệu)
        }
    }
}
