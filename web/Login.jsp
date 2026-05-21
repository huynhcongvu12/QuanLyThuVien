<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | Quản lý mượn sách</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/luxury-ui.css">
</head>
<body class="auth-page login-body auth-luxury-page" style="--auth-bg-image: url('${pageContext.request.contextPath}/assets/image/library/login-library-bg.jpg');">
    <jsp:include page="components/alert.jsp" />

    <button type="button" class="auth-theme-toggle" aria-label="Chuyển sáng tối" onclick="window.toggleTheme && window.toggleTheme()">
        <span>☾</span>
    </button>

    <main class="login-shell auth-luxury-card">
        <section class="brand-panel auth-brand-panel">
            <div class="auth-logo-row">
                <div class="library-mark">
                    <img src="${pageContext.request.contextPath}/assets/image/logo1.jpg" alt="Logo thư viện">
                </div>
                <span>Thư viện số</span>
            </div>

            <div class="auth-brand-copy">
                <p class="auth-eyebrow">Library Management</p>
                <h1>Quản lý mượn sách</h1>
                <p>Đăng nhập để quản lý sách, mượn trả và theo dõi hoạt động thư viện trong một không gian hiện đại.</p>
            </div>

            <div class="auth-media-card">
                <img class="auth-illustration" src="${pageContext.request.contextPath}/assets/image/library/login-student-panel.jpg" alt="Sinh viên đọc sách">
                <span>Thư viện số</span>
            </div>
        </section>

        <section class="form-panel auth-form-panel">
            <div class="auth-form-heading">
                <p class="auth-eyebrow">Welcome back</p>
                <h2>Đăng nhập</h2>
                <p>Nhập thông tin tài khoản để tiếp tục.</p>
            </div>

            <c:if test="${param.success == '1' || param.register == 'success'}">
                <div class="auth-message auth-message-success">
                    Đăng ký thành công! Vui lòng đăng nhập.
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="auth-message auth-message-error">
                    ${error}
                </div>
            </c:if>

            <form class="form-grid auth-form" action="LoginServlet" method="POST" onsubmit="return validateLoginForm(event)">
                <div class="field">
                    <label for="email">Email hoặc mã người dùng</label>
                    <input id="email" name="email" type="text" placeholder="Tên đăng nhập" autocomplete="username">
                </div>

                <div class="field">
                    <label for="password">Mật khẩu</label>
                    <input id="password" name="password" type="password" placeholder="Nhập mật khẩu" autocomplete="current-password">
                </div>

                <button type="submit" class="btn auth-submit">Đăng nhập</button>
            </form>

            <div id="message" class="auth-message auth-message-error" style="display:none;"></div>

            <p class="auth-switch">
                Chưa có tài khoản?
                <a href="Register.jsp">Đăng ký ngay</a>
            </p>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/assets/js.js"></script>
    <script src="${pageContext.request.contextPath}/assets/ui-upgrade.js"></script>
    <script src="${pageContext.request.contextPath}/assets/luxury-ui.js"></script>
</body>
</html>
