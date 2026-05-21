<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký | Quản lý mượn sách</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/luxury-ui.css">
</head>
<body class="auth-page login-body auth-luxury-page" style="--auth-bg-image: url('${pageContext.request.contextPath}/assets/image/library/login-library-bg.jpg');">
    <jsp:include page="components/alert.jsp" />

    <button type="button" class="auth-theme-toggle" aria-label="Chuyển sáng tối" onclick="window.toggleTheme && window.toggleTheme()">
        <span>☾</span>
    </button>

    <main class="login-shell auth-luxury-card auth-register-card">
        <section class="brand-panel auth-brand-panel">
            <div class="auth-logo-row">
                <div class="library-mark">
                    <img src="${pageContext.request.contextPath}/assets/image/logo1.jpg" alt="Logo thư viện">
                </div>
                <span>Thư viện số</span>
            </div>

            <div class="auth-brand-copy">
                <p class="auth-eyebrow">Start reading</p>
                <h1>Quản lý mượn sách</h1>
                <p>Tạo tài khoản độc giả để mượn sách, theo dõi lịch sử và nhận thông báo thư viện.</p>
            </div>

            <div class="auth-media-card">
                <img class="auth-illustration" src="${pageContext.request.contextPath}/assets/image/library/login-student-panel.jpg" alt="Sinh viên đọc sách">
                <span>Thư viện số</span>
            </div>
        </section>

        <section class="form-panel auth-form-panel">
            <div class="auth-form-heading">
                <p class="auth-eyebrow">Create account</p>
                <h2>Tạo tài khoản</h2>
                <p>Điền thông tin cần thiết để bắt đầu sử dụng.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="auth-message auth-message-error">
                    ${error}
                </div>
            </c:if>

            <form class="form-grid auth-form register-form" action="RegisterServlet" method="POST">
                <div class="field">
                    <label for="hoTen">Họ và tên *</label>
                    <input id="hoTen" type="text" name="hoTen" placeholder="Nguyễn Văn A" autocomplete="name" required>
                </div>

                <div class="field">
                    <label for="tenDangNhap">Tên đăng nhập *</label>
                    <input id="tenDangNhap" type="text" name="tenDangNhap" placeholder="username123" autocomplete="username" required>
                </div>

                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="email" placeholder="email@example.com" autocomplete="email">
                </div>

                <div class="field">
                    <label for="soDienThoai">Số điện thoại</label>
                    <input id="soDienThoai" type="tel" name="soDienThoai" placeholder="0912345678" autocomplete="tel">
                </div>

                <div class="field">
                    <label for="gioiTinh">Giới tính</label>
                    <select id="gioiTinh" name="gioiTinh">
                        <option value="Nam">Nam</option>
                        <option value="Nữ">Nữ</option>
                    </select>
                </div>

                <div class="field">
                    <label for="ngaySinh">Ngày sinh</label>
                    <input id="ngaySinh" type="date" name="ngaySinh">
                </div>

                <div class="field full">
                    <label for="diaChi">Địa chỉ</label>
                    <input id="diaChi" type="text" name="diaChi" placeholder="Số nhà, đường, quận/huyện" autocomplete="street-address">
                </div>

                <div class="field password-wrapper">
                    <label for="matKhau">Mật khẩu *</label>
                    <input id="matKhau" type="password" name="matKhau" placeholder="••••••••" autocomplete="new-password" required>
                    <button type="button" class="toggle-pass" data-target="matKhau">Hiện</button>
                </div>

                <div class="field password-wrapper">
                    <label for="confirmPassword">Xác nhận mật khẩu *</label>
                    <input id="confirmPassword" type="password" name="confirmPassword" placeholder="••••••••" autocomplete="new-password" required>
                    <button type="button" class="toggle-pass" data-target="confirmPassword">Hiện</button>
                </div>

                <div class="actions full">
                    <button type="submit" class="btn auth-submit">Đăng ký tài khoản</button>
                </div>
            </form>

            <p class="auth-switch">
                Đã có tài khoản?
                <a href="Login.jsp">Đăng nhập ngay</a>
            </p>
        </section>
    </main>

    <script>
        document.querySelectorAll('.toggle-pass').forEach(function (button) {
            button.addEventListener('click', function () {
                const input = document.getElementById(button.dataset.target);
                const isPassword = input.type === 'password';
                input.type = isPassword ? 'text' : 'password';
                button.textContent = isPassword ? 'Ẩn' : 'Hiện';
            });
        });

        const pass = document.getElementById('matKhau');
        const confirm = document.getElementById('confirmPassword');

        function checkMatch() {
            if (confirm.value && pass.value !== confirm.value) {
                confirm.classList.add('is-invalid');
            } else {
                confirm.classList.remove('is-invalid');
            }
        }

        if (pass && confirm) {
            pass.addEventListener('input', checkMatch);
            confirm.addEventListener('input', checkMatch);
        }
    </script>
    <script src="${pageContext.request.contextPath}/assets/ui-upgrade.js"></script>
    <script src="${pageContext.request.contextPath}/assets/luxury-ui.js"></script>
</body>
</html>
