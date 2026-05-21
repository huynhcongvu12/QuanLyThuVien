
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
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
@WebServlet("/SachServlet")
public class SachServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        model.Account account = (model.Account) request.getSession().getAttribute("account");
        Process_Data.Customer cus = new Process_Data.Customer();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (account != null) {
            if (account.getQuyen() == 1) {
                String keyword = request.getParameter("search");
                Process_Data.Sach Sach = new Process_Data.Sach();
                Vector<model.Sach> listsach = Sach.searchSach(keyword);
                String json = new com.google.gson.Gson().toJson(listsach);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } else if (account.getQuyen() == 0) {
                String keyword = request.getParameter("search");
                Process_Data.Sach Sach = new Process_Data.Sach();
                Vector<model.Sach> listsach = Sach.searchSach(keyword);
                String json = new com.google.gson.Gson().toJson(listsach);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } else {
                String keyword = request.getParameter("search");
                Process_Data.Sach Sach = new Process_Data.Sach();
                Vector<model.Sach> listsach = Sach.searchSach(keyword);
                String json = new com.google.gson.Gson().toJson(listsach);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            }

        } else {

        }
    }
}
