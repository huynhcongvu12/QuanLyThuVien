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

/**
 *
 * @author Mr.Khoa
 */
@WebServlet(urlPatterns = {"/DocGiaServlet"})
public class DocGiaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("search");
        Process_Data.Customer DocGia = new Process_Data.Customer();
        Vector<model.DocGia> listdocgia = DocGia.searchDocGia(keyword);
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // Hoặc "dd/MM/yyyy" tùy bạn muốn
                .create();
        String json = gson.toJson(listdocgia);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();

    }

}
