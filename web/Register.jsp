<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký | Quản lý mượn sách</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/luxury-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/auth-modern.css">
</head>
<body class="auth-page login-body auth-luxury-page auth-modern-page" style="--auth-bg-image: url('${pageContext.request.contextPath}/assets/image/library/auth-bg-library.jpg');">
    <jsp:include page="components/alert.jsp" />

    <button type="button" class="auth-theme-toggle" aria-label="Chuyển sáng tối" onclick="window.toggleTheme && window.toggleTheme()">
        <span>☾</span>
    </button>

    <main class="auth-modern-shell login-shell auth-luxury-card auth-register-card">
        <section class="auth-modern-brand auth-brand-panel">
            <div class="auth-logo-row">
                <div class="library-mark">
                    <img src="${pageContext.request.contextPath}/assets/image/logo1.jpg" alt="Logo thư viện">
                </div>
                <span>Thư viện số</span>
            </div>

            <div class="auth-brand-copy">
                <p class="auth-eyebrow">Create account</p>
                <h1>Quản lý<br>mượn sách</h1>
                <p>Tạo tài khoản độc giả để tìm sách, gửi yêu cầu mượn và theo dõi lịch sử của bạn.</p>
            </div>

            <div class="auth-media-card">
                <img class="auth-illustration" src="${pageContext.request.contextPath}/assets/image/library/auth-students-reading.jpg" alt="Sinh viên đọc sách trong thư viện">
                <span>Không gian đọc hiện đại</span>
            </div>
        </section>

        <section class="auth-modern-form form-panel auth-form-panel">
            <div class="auth-form-heading">
                <h2>Đăng ký</h2>
                <p>Đã có tài khoản? <a href="Login.jsp">Đăng nhập ngay</a></p>
            </div>

            <c:if test="${not empty error}">
                <div class="auth-message auth-message-error">
                    <c:out value="${error}" />
                </div>
            </c:if>

            <form class="form-grid auth-form register-form" action="RegisterServlet" method="POST" onsubmit="return validateRegisterModern(event)">
                <div class="field">
                    <label for="hoTen">Họ và tên *</label>
                    <input id="hoTen" type="text" name="hoTen" placeholder="Nguyễn Văn A" autocomplete="name" required>
                </div>

                <div class="field">
                    <label for="tenDangNhap">Tên đăng nhập *</label>
                    <input id="tenDangNhap" type="text" name="tenDangNhap" placeholder="username123" autocomplete="username" required minlength="3">
                </div>

                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="email" placeholder="email@example.com" autocomplete="email">
                </div>

                <div class="field">
                    <label for="soDienThoai">Số điện thoại</label>
                    <input id="soDienThoai" type="tel" name="soDienThoai" placeholder="0912345678" autocomplete="tel" pattern="[0-9+ ]{9,15}">
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
                    <input id="matKhau" type="password" name="matKhau" placeholder="••••••••" autocomplete="new-password" required minlength="3">
                    <button type="button" class="toggle-pass" data-target="matKhau">Hiện</button>
                </div>

                <div class="field password-wrapper">
                    <label for="confirmPassword">Xác nhận mật khẩu *</label>
                    <input id="confirmPassword" type="password" name="confirmPassword" placeholder="••••••••" autocomplete="new-password" required minlength="3">
                    <button type="button" class="toggle-pass" data-target="confirmPassword">Hiện</button>
                </div>

                <div class="actions full">
                    <button type="submit" class="btn auth-submit">Đăng ký tài khoản</button>
                </div>
            </form>

            <div id="message" class="auth-message auth-message-error" style="display:none;"></div>
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

        function validateRegisterModern(event) {
            const form = event.target;
            const pass = form.querySelector('[name="matKhau"]');
            const confirm = form.querySelector('[name="confirmPassword"]');
            const phone = form.querySelector('[name="soDienThoai"]');
            const message = document.getElementById('message');

            function show(text) {
                message.textContent = text;
                message.style.display = 'block';
                return false;
            }

            message.style.display = 'none';
            if (pass.value !== confirm.value) {
                confirm.classList.add('is-invalid');
                event.preventDefault();
                return show('Mật khẩu xác nhận không khớp.');
            }
            confirm.classList.remove('is-invalid');

            if (phone.value && !/^[0-9+ ]{9,15}$/.test(phone.value.trim())) {
                event.preventDefault();
                return show('Số điện thoại không hợp lệ.');
            }
            return true;
        }
    </script>
    <script src="${pageContext.request.contextPath}/assets/ui-upgrade.js"></script>
    <script src="${pageContext.request.contextPath}/assets/luxury-ui.js"></script>
</body>
</html>
