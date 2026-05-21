<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lỗi máy chủ | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">500</div>
        <h1>Lỗi máy chủ</h1>
        <p>Hệ thống đang gặp lỗi trong quá trình xử lý yêu cầu.</p>
        <div class="qltv-error-hint">Kiểm tra kết nối database, servlet, DAO hoặc thử lại sau.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
