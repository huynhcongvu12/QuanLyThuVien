<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loi may chu | Quan ly thu vien</title>
    <link rel="stylesheet" href="../assets/styles.css">
</head>
<body class="qltv-error-body">
    <main class="qltv-error-card">
        <div class="qltv-error-code">500</div>
        <h1>Loi may chu</h1>
        <p>He thong dang gap loi trong qua trinh xu ly yeu cau.</p>
        <c:if test="${not empty requestScope['javax.servlet.error.message']}">
            <div class="qltv-error-hint">
                <c:out value="${requestScope['javax.servlet.error.message']}" />
            </div>
        </c:if>
        <div class="qltv-error-hint">Kiem tra ket noi database, servlet, DAO hoac thu lai sau.</div>
        <div class="qltv-error-actions">
            <a href="../Login.jsp">Ve dang nhap</a>
            <a href="javascript:history.back()">Quay lai trang truoc</a>
        </div>
    </main>
</body>
</html>
