<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty success || not empty param.successMsg || not empty message || not empty param.message}">
    <div class="qltv-alert qltv-alert-success" role="alert">
        <span class="qltv-alert-icon">✓</span>
        <div>
            <strong>Thành công</strong>
            <p>
                <c:out value="${success}" />
                <c:out value="${param.successMsg}" />
                <c:out value="${message}" />
                <c:out value="${param.message}" />
            </p>
        </div>
        <button type="button" onclick="this.parentElement.remove()">×</button>
    </div>
</c:if>

<c:if test="${not empty warning || not empty param.warning}">
    <div class="qltv-alert qltv-alert-warning" role="alert">
        <span class="qltv-alert-icon">!</span>
        <div>
            <strong>Cảnh báo</strong>
            <p><c:out value="${warning}" /><c:out value="${param.warning}" /></p>
        </div>
        <button type="button" onclick="this.parentElement.remove()">×</button>
    </div>
</c:if>

<c:if test="${not empty error || not empty param.error || not empty errorMessage || not empty param.errorMsg}">
    <div class="qltv-alert qltv-alert-danger" role="alert">
        <span class="qltv-alert-icon">×</span>
        <div>
            <strong>Lỗi xử lý</strong>
            <p>
                <c:out value="${error}" />
                <c:out value="${param.error}" />
                <c:out value="${errorMessage}" />
                <c:out value="${param.errorMsg}" />
            </p>
        </div>
        <button type="button" onclick="this.parentElement.remove()">×</button>
    </div>
</c:if>

<c:if test="${not empty info || not empty param.info}">
    <div class="qltv-alert qltv-alert-info" role="alert">
        <span class="qltv-alert-icon">i</span>
        <div>
            <strong>Thông tin</strong>
            <p><c:out value="${info}" /><c:out value="${param.info}" /></p>
        </div>
        <button type="button" onclick="this.parentElement.remove()">×</button>
    </div>
</c:if>
