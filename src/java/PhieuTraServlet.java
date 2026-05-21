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
@WebServlet(urlPatterns = {"/PhieuTraServlet"})
public class PhieuTraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể để trống hoặc dùng để điều hướng
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Cấu hình tiếng Việt và kiểu dữ liệu trả về là JSON
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("search");
        if (keyword == null) {
            keyword = "";
        }
        Process_Data.PhieuTra PhieuTraDAO = new Process_Data.PhieuTra();
        java.util.Vector<model.PhieuTra> listphieutra = PhieuTraDAO.searchPhieuTra(keyword);
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        String json = gson.toJson(listphieutra);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

}
