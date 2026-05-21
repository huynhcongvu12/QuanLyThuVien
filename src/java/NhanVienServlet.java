/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/NhanVienServlet"})
public class NhanVienServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể để trống
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("search");
        Process_Data.Staff NhanVienDAO = new Process_Data.Staff();
        Vector<model.NhanVien> listnhanvien = NhanVienDAO.searchNhanVien(keyword);

        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .setDateFormat("yyyy-MM-dd") 
                .create();
                
        String json = gson.toJson(listnhanvien);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}