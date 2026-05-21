import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String path = request.getRequestURI().substring(request.getContextPath().length());
        Integer role = requiredRole(path);
        if (role == null) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        model.Account account = session == null ? null : (model.Account) session.getAttribute("account");
        if (account == null) {
            reject(request, response, 401, "Ban can dang nhap de thuc hien thao tac nay.");
            return;
        }

        if (!hasAccess(account, role)) {
            reject(request, response, 403, "Ban khong co quyen thuc hien thao tac nay.");
            return;
        }

        chain.doFilter(request, response);
    }

    private Integer requiredRole(String path) {
        if (path == null) {
            return null;
        }
        if (path.endsWith("/Admin.jsp") || path.endsWith("ThemSachServlet")
                || path.endsWith("ThemNhanVienServlet")
                || path.endsWith("NhanVienServlet") || path.endsWith("ThemDocGiaServlet")
                || path.endsWith("DocGiaServlet") || path.endsWith("ThemPhieuPhatServlet")
                || path.endsWith("PhieuPhatServlet")) {
            return 0;
        }
        if (path.endsWith("/Staff.jsp")) {
            return 2;
        }
        if (path.endsWith("/Customer.jsp") || path.endsWith("CustomerBorrowServlet")) {
            return 1;
        }
        if (path.endsWith("ThemPhieuMuonServlet") || path.endsWith("PhieuMuonServlet")
                || path.endsWith("ThemPhieuTraSevlet") || path.endsWith("PhieuTraServlet")
                || path.endsWith("BorrowActionServlet")) {
            return -2;
        }
        if (path.endsWith("SachServlet") || path.endsWith("UpdateAccount")
                || path.endsWith("UpdatePassword") || path.endsWith("LogoutServlet")) {
            return -1;
        }
        return null;
    }

    private boolean hasAccess(model.Account account, int role) {
        if (role == -1) {
            return true;
        }
        if (role == -2) {
            return account.getQuyen() == 0 || account.getQuyen() == 2;
        }
        return account.getQuyen() == role;
    }

    private void reject(HttpServletRequest request, HttpServletResponse response, int status, String message)
            throws IOException {
        if (wantsJson(request)) {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print("{\"success\":false,\"message\":\"" + message + "\"}");
            return;
        }
        if (status == 401) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/error/403.jsp");
        }
    }

    private boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");
        String path = request.getRequestURI();
        String servletName = path.substring(path.lastIndexOf('/') + 1);
        return (accept != null && accept.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || (path.endsWith("Servlet") && !"LoginServlet".equals(servletName));
    }

    @Override
    public void destroy() {
    }
}
