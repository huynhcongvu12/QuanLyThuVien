<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Không thể mượn sách | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">MƯỢN SÁCH</div>
        <h1>Không thể mượn sách</h1>
        <p>Yêu cầu mượn không hợp lệ, sách đã hết, đã mượn trùng hoặc bạn đang có sách quá hạn.</p>
        <div class="qltv-error-hint">Quay lại danh sách sách để chọn sách khác hoặc liên hệ nhân viên.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
