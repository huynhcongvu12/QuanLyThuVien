/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@WebServlet(urlPatterns = {"/PhieuPhatServlet"})
public class PhieuPhatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("search");
        if (keyword == null) {
            keyword = "";
        }
        Process_Data.PhieuPhat phieuPhatDAO = new Process_Data.PhieuPhat();
        Vector<model.PhieuPhat> listPhieuPhat = phieuPhatDAO.searchPhieuPhat(keyword);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        String json = gson.toJson(listPhieuPhat);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }
}
