<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bạn cần đăng nhập | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">401</div>
        <h1>Bạn cần đăng nhập</h1>
        <p>Phiên đăng nhập đã hết hạn hoặc bạn chưa đăng nhập vào hệ thống.</p>
        <div class="qltv-error-hint">Đăng nhập lại để tiếp tục sử dụng chức năng.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
