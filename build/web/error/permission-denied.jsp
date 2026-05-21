<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sai quyền sử dụng | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">PHÂN QUYỀN</div>
        <h1>Sai quyền sử dụng</h1>
        <p>Customer không được truy cập trang Admin/Staff và ngược lại.</p>
        <div class="qltv-error-hint">Hãy đăng nhập bằng đúng tài khoản theo vai trò.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
