<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Không thể xác nhận trả sách | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">TRẢ SÁCH</div>
        <h1>Không thể xác nhận trả sách</h1>
        <p>Phiếu mượn không tồn tại, đã trả trước đó hoặc trạng thái không hợp lệ.</p>
        <div class="qltv-error-hint">Nhân viên cần kiểm tra lại mã phiếu/mã độc giả/mã sách.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
