<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chưa có dữ liệu hiển thị | Quản lý thư viện</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">DỮ LIỆU</div>
        <h1>Chưa có dữ liệu hiển thị</h1>
        <p>Danh sách hiện tại đang trống hoặc bộ lọc tìm kiếm không có kết quả.</p>
        <div class="qltv-error-hint">Thử đổi từ khóa tìm kiếm hoặc thêm dữ liệu mẫu vào SQL.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Về đăng nhập</a>
            <a href="javascript:history.back()">Quay lại trang trước</a>
        </div>
    </main>
</body>
</html>
