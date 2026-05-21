<%-- 
    Document   : Staff
    Created on : Apr 29, 2026, 5:55:22 PM
    Author     : Mr.Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    model.Account currentAccount = (model.Account) session.getAttribute("account");
    if (currentAccount == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
    if (currentAccount.getQuyen() != 2) {
        response.sendRedirect("error/403.jsp");
        return;
    }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <!DOCTYPE html>
    <html lang="vi">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Nhân viên | Quản lý mượn sách</title>
            <link rel="stylesheet" href="styles.css" />
            <link rel="stylesheet" href="assets/styles.css">
        
            <link rel="stylesheet" href="assets/luxury-ui.css">
</head>
        <body class="staff-page" data-role="staff">
            <jsp:include page="components/alert.jsp" />
            <div class="app-layout"><aside class="sidebar">
                    <div class="sidebar-brand">
                        <img src="assets/image/logo1.jpg" style="border-radius: 7px 7px 7px 7px" width="40px" height="40px" alt="alt"/>
                        <span>Thư viện Số</span>
                    </div>
                    <div class="role-pill">Giao diện nhân viên</div>
                    <nav class="nav">
                        <a class="active" href="#mgmt-book">Tìm kiếm sách</a>
                        <a href="#mgmt-book">Lập phiếu mượn</a>
                        <a href="#mgmt-return">Lập phiếu trả</a>
                        <a href="#profile">Sửa thông tin</a>
                        <a href="#password">Đổi mật khẩu</a>
                        <a href="LogoutServlet">Đăng xuất</a>
                    </nav>
                    <div class="sidebar-footer"></div>
                </aside>
                <main class="main staff-main">
                    <header class="topbar">
                        <div class="page-title">
                            <span class="kicker"></span>
                            <h1>Quản lý mượn trả</h1>
                            <p></p>
                        </div>
                        <div class="user-chip">
                           
                            <strong>Nhân viên</strong>
                        </div>
                    </header>
                    <section class="visual-hero staff-hero">
                        <div>
                            <h2>Xử lý mượn trả nhanh chóng</h2>
                            <p>Nhân viên có thể tìm sách, lập phiếu, duyệt yêu cầu, xác nhận giao sách và xử lý trả sách/quá hạn một cách trực quan.</p>
                            <div class="hero-actions staff-hero-actions">
                                <button type="button" class="hero-action-btn" onclick="scrollToStaffSection('pending-borrow-section')">✅ Duyệt yêu cầu</button>
                                <button type="button" class="hero-action-btn" onclick="scrollToStaffSection('return-confirm-section')">🔁 Xác nhận trả</button>
                                <button type="button" class="hero-action-btn warning" onclick="scrollToStaffSection('overdue-section')">⚠️ Theo dõi quá hạn</button>
                                <span class="hero-chip">✅ Duyệt yêu cầu</span>
                                <span class="hero-chip">🔁 Xác nhận trả</span>
                                <span class="hero-chip">⚠️ Theo dõi quá hạn</span>
                            </div>
                        </div>
                        <img src="assets/image/library/staff-dashboard-banner.jpg" alt="Minh họa quy trình nhân viên" />
                    </section>
                    <section class="stats-grid staff-stats-grid" style="grid-template-columns: repeat(4, minmax(0, 1fr));">
                        <div class="stat-card">
                            <small>Phiếu mượn hôm nay</small>
                            <strong>${phieumuonhomnay}</strong>
                            <em>${sosanh} so với hôm qua</em>
                        </div>
                        <div class="stat-card">
                            <small>Phiếu trả hôm nay</small>
                            <strong>${phieutrahomnay}</strong>
                            <em>${sophieuquahan} phiếu quá hạn</em>
                        </div>
                        <div class="stat-card">
                            <small>Sách khả dụng</small>
                            <strong>${soluong}</strong>
                        </div>
                        <div class="stat-card">
                            <small>Quá hạn</small>
                            <strong>${sophieuquahan}</strong>
                            <em>Cần theo dõi</em>
                        </div>
                    </section>
                    <section class="staff-section" id="pending-borrow-section">
                        <div class="staff-section-header">
                            <div>
                                <span class="kicker">Staff workflow</span>
                                <h2>Yêu cầu mượn đang chờ duyệt</h2>
                                <p>Kiểm tra thông tin độc giả, sách và số lượng trước khi duyệt hoặc từ chối yêu cầu.</p>
                            </div>
                            <button type="button" class="btn sm" onclick="scrollToStaffSection('mgmt-borrow')">Xem tất cả phiếu</button>
                        </div>
                        <div class="table-wrap staff-table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Độc giả</th>
                                        <th>Sách</th>
                                        <th>Ngày mượn</th>
                                        <th>Hạn trả</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="hasPendingBorrow" value="false" />
                                    <c:forEach var="listpm" items="${listphieumuon}">
                                        <c:if test="${listpm.trangThai == 'Pending' || empty listpm.trangThai}">
                                            <c:set var="hasPendingBorrow" value="true" />
                                            <tr>
                                                <td>${listpm.maPhieu}</td>
                                                <td>${listpm.hoTen}<br><small>Mã: ${listpm.maDocGia}</small></td>
                                                <td>${listpm.tenSach}<br><small>Mã: ${listpm.maSach}</small></td>
                                                <td>${listpm.ngayMuon}</td>
                                                <td>${listpm.ngayPhaiTra}</td>
                                                <td class="row-actions">
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="approve" />
                                                        <button class="btn sm" type="submit">Duyệt</button>
                                                    </form>
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="reject" />
                                                        <input type="hidden" name="reason" value="Yêu cầu không hợp lệ" />
                                                        <button class="btn sm danger" type="submit">Từ chối</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${!hasPendingBorrow}">
                                        <tr><td colspan="6"><div class="staff-empty-state">Hiện chưa có yêu cầu nào.</div></td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>

                    <section class="staff-section" id="return-confirm-section">
                        <div class="staff-section-header">
                            <div>
                                <span class="kicker">Return queue</span>
                                <h2>Phiếu cần xác nhận trả</h2>
                                <p>Các phiếu đang mượn cần được đối chiếu tình trạng sách trước khi xác nhận trả.</p>
                            </div>
                            <button type="button" class="btn sm" onclick="scrollToStaffSection('mgmt-return')">Tới phiếu trả</button>
                        </div>
                        <div class="table-wrap staff-table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Độc giả</th>
                                        <th>Sách</th>
                                        <th>Hạn trả</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="hasReturnConfirm" value="false" />
                                    <c:forEach var="listpm" items="${listphieumuon}">
                                        <c:if test="${listpm.trangThai == 'Borrowed' || listpm.trangThai == 'Overdue'}">
                                            <c:set var="hasReturnConfirm" value="true" />
                                            <tr>
                                                <td>${listpm.maPhieu}</td>
                                                <td>${listpm.hoTen}<br><small>Mã: ${listpm.maDocGia}</small></td>
                                                <td>${listpm.tenSach}<br><small>Mã: ${listpm.maSach}</small></td>
                                                <td>${listpm.ngayPhaiTra}</td>
                                                <td><span class="badge ${listpm.trangThai == 'Overdue' ? 'warn' : ''}">${listpm.trangThai}</span></td>
                                                <td class="row-actions">
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="return" />
                                                        <input type="hidden" name="tinhTrangSach" value="normal" />
                                                        <button class="btn sm" type="submit">Trả bình thường</button>
                                                    </form>
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="return" />
                                                        <input type="hidden" name="tinhTrangSach" value="damaged" />
                                                        <input type="hidden" name="note" value="Sách hư hỏng" />
                                                        <button class="btn sm danger" type="submit">Hư</button>
                                                    </form>
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="return" />
                                                        <input type="hidden" name="tinhTrangSach" value="lost" />
                                                        <input type="hidden" name="note" value="Sách bị mất" />
                                                        <button class="btn sm danger" type="submit">Mất</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${!hasReturnConfirm}">
                                        <tr><td colspan="6"><div class="staff-empty-state">Hiện chưa có phiếu cần trả.</div></td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>

                    <section class="staff-section" id="overdue-section">
                        <div class="staff-section-header">
                            <div>
                                <span class="kicker">Overdue</span>
                                <h2>Sách quá hạn</h2>
                                <p>Theo dõi các phiếu quá hạn để nhắc độc giả trả sách và xử lý phí phạt khi cần.</p>
                            </div>
                            <button type="button" class="btn sm warning" onclick="scrollToStaffSection('mgmt-borrow')">Kiểm tra phiếu</button>
                        </div>
                        <div class="table-wrap staff-table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Độc giả</th>
                                        <th>Sách</th>
                                        <th>Ngày mượn</th>
                                        <th>Hạn trả</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="hasOverdueBorrow" value="false" />
                                    <c:forEach var="listpm" items="${listphieumuon}">
                                        <c:if test="${listpm.trangThai == 'Overdue'}">
                                            <c:set var="hasOverdueBorrow" value="true" />
                                            <tr>
                                                <td>${listpm.maPhieu}</td>
                                                <td>${listpm.hoTen}<br><small>Mã: ${listpm.maDocGia}</small></td>
                                                <td>${listpm.tenSach}<br><small>Mã: ${listpm.maSach}</small></td>
                                                <td>${listpm.ngayMuon}</td>
                                                <td><span class="badge warn">${listpm.ngayPhaiTra}</span></td>
                                                <td class="row-actions">
                                                    <form action="BorrowActionServlet" method="post" style="display:inline">
                                                        <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                        <input type="hidden" name="action" value="return" />
                                                        <input type="hidden" name="tinhTrangSach" value="normal" />
                                                        <button class="btn sm" type="submit">Xác nhận trả</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${!hasOverdueBorrow}">
                                        <tr><td colspan="6"><div class="staff-empty-state">Hiện chưa có sách quá hạn.</div></td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>

                    <section class="panel staff-section" id="mgmt-book" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý sách</h2>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input id="keywordd" placeholder="Tên sách, tác giả" />
                            </div>
                            <!-- Thay button submit bằng button thường để gọi hàm JS -->
                            <button type="button" class="btn" onclick="searchBookss()">Tìm kiếm</button>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <thead>
                                        <tr>
                                            <th>Mã sách</th>
                                            <th>Tên sách</th>
                                            <th>Tác giả</th>
                                            <th>Thể loại</th>
                                            <th>Nhà xuất bản</th>
                                            <th>Số lượng</th>
                                        </tr>
                                    </thead>
                                </thead>
                                <tbody id="bookResultt" style="font: Arial, Helvetica, sans-serif;">
                                    <c:forEach var="ls" items="${listsach}">
                                        <!-- Dữ liệu mặc định khi mới load trang -->
                                        <tr>
                                            <td>${ls.maSach}</td>
                                            <td><div class="book-title-cell"><img src="assets/image/library/book-cover-${((ls.maSach - 1) % 12) + 1}.jpg" alt="Bìa sách"><span>${ls.tenSach}</span></div></td>
                                            <td>${ls.tacGia}</td>
                                            <td>${ls.theLoai}</td>
                                            <td>${ls.nhaXuatBan}</td>                                         
                                            <td>${ls.soLuong == 0 ? "Hết" : "Còn: ".concat(ls.soLuong)}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </section>    
                    <c:if test="${not empty borrowActionMessage}"><div class="alert alert-info">${borrowActionMessage}</div></c:if>
                    <section class="panel staff-section" id="mgmt-borrow" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý phiếu mượn</h2>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input id="borrowKeyword" placeholder="Mã phiếu, tên độc giả" />
                            </div>
                            <button type="button" class="btn" onclick="searchBorrows()">Tìm kiếm</button>
                        </div>
                        <form class="modal-form" id="borrowForm">
                            <h3>Thêm / Sửa phiếu mượn</h3>
                            <div class="field full">
                                <label>Mã phiếu</label>
                                <input placeholder="PM-001" name="maphieu"  readonly />
                            </div>
                            <div class="field">
                                <label>Mã độc giả</label>
                                <input placeholder="DG-001" name="madocgia"/>
                            </div>
                            <div class="field">
                                <label>Mã sách</label>
                                <input placeholder="BK-2048" name="masach" />
                            </div>
                            <div class="field">
                                <label>Ngày mượn</label>
                                <input type="date" name="ngaymuon" />
                            </div>
                            <div class="field">
                                <label>Ngày hẹn trả</label>
                                <input type="date" name="ngayphaitra" />
                            </div>
                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn" onclick="saveBorrow()">Lưu phiếu mượn</button>
                            </div>
                        </form>

                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Mã độc giả</th>
                                        <th>Họ tên</th>
                                        <th>Mã sách</th>
                                        <th>Tên sách</th>
                                        <th>Ngày mượn</th>
                                        <th>Ngày phải trả</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="borrowResult">
                                    <c:forEach var="listpm" items="${listphieumuon}">
                                        <tr>
                                            <td>${listpm.maPhieu}</td>
                                            <td>${listpm.maDocGia}</td>
                                            <td>${listpm.hoTen}</td>
                                            <td>${listpm.maSach}</td>
                                            <td>${listpm.tenSach}</td>
                                            <td>${listpm.ngayMuon}</td>
                                            <td>${listpm.ngayPhaiTra}</td>
                                            <td><span class="badge">${listpm.trangThai}</span></td>
                                            <td class="row-actions">
                                                <c:choose>
                                                    <c:when test="${listpm.trangThai == 'Pending'}">
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="approve" />
                                                            <button class="btn sm" type="submit">Duyệt</button>
                                                        </form>
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="reject" />
                                                            <input type="hidden" name="reason" value="Yêu cầu không hợp lệ" />
                                                            <button class="btn sm danger" type="submit">Từ chối</button>
                                                        </form>
                                                    </c:when>
                                                    <c:when test="${listpm.trangThai == 'Approved'}">
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="handover" />
                                                            <button class="btn sm" type="submit">Giao sách</button>
                                                        </form>
                                                    </c:when>
                                                    <c:when test="${listpm.trangThai == 'Borrowed' || listpm.trangThai == 'Overdue'}">
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="return" />
                                                            <input type="hidden" name="tinhTrangSach" value="normal" />
                                                            <button class="btn sm" type="submit">Trả bình thường</button>
                                                        </form>
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="return" />
                                                            <input type="hidden" name="tinhTrangSach" value="damaged" />
                                                            <input type="hidden" name="note" value="Sách hư hỏng" />
                                                            <button class="btn sm danger" type="submit">Hư</button>
                                                        </form>
                                                        <form action="BorrowActionServlet" method="post" style="display:inline">
                                                            <input type="hidden" name="maPhieu" value="${listpm.maPhieu}" />
                                                            <input type="hidden" name="action" value="return" />
                                                            <input type="hidden" name="tinhTrangSach" value="lost" />
                                                            <input type="hidden" name="note" value="Sách bị mất" />
                                                            <button class="btn sm danger" type="submit">Mất</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span>${listpm.lyDoTuChoi}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="panel staff-section" id="mgmt-return" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý phiếu trả</h2>
                        </div>

                        <div class="search-row">
                            <div class="search-box">
                                <input id="returnKeyword" placeholder="Mã phiếu trả, tên độc giả" />
                            </div>
                            <button type="button" class="btn" onclick="searchReturns()">Tìm kiếm</button>
                        </div>

                        <form class="modal-form" id="returnForm">
                            <h3>Thêm / Sửa phiếu trả</h3>

                            <div class="field">
                                <label>Mã phiếu trả</label>
                                <input type="text" name="maphieutra" placeholder="PT-001" readonly />
                            </div>

                            <div class="field">
                                <label>Mã độc giả</label>
                                <input type="number" name="madocgia" placeholder="Nhập mã độc giả" required />
                            </div>

                            <div class="field">
                                <label>Mã sách</label>
                                <input type="number" name="masach" placeholder="Nhập mã sách" required />
                            </div>

                            <div class="field">
                                <label>Ngày trả</label>
                                <input type="date" name="ngaytra" required />
                            </div>
                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn" onclick="saveReturn()">Lưu phiếu trả</button>
                            </div>
                        </form>

                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Mã độc giả</th>
                                        <th>Họ tên</th>
                                        <th>Mã sách</th>
                                        <th>Tên sách</th>
                                        <th>Ngày trả</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="returnResult">
                                    <c:forEach var="pt" items="${listphieutra}">
                                        <tr>
                                            <td>${pt.maPhieu}</td>
                                            <td>${pt.maDocGia}</td>
                                            <td>${pt.hoTen}</td>
                                            <td>${pt.maSach}</td>
                                            <td>${pt.tenSach}</td>
                                            <td>${pt.ngayTra}</td>
                                            <td class="row-actions">
                                                <button type="button" class="btn sm secondary" 
                                                        onclick="editReturn('${pt.maPhieu}', '${pt.maDocGia}', '${pt.maSach}', '${pt.ngayTra}')">
                                                    Sửa
                                                </button>
                                                <button type="button" class="btn sm danger" 
                                                        onclick="deleteReturn('${pt.maPhieu}', this)">
                                                    Xóa
                                                </button>

                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="forms-two" style="margin-top:18px">
                        <div class="panel" id="profile">
                            <div class="panel-header">
                                <h2>Sửa thông tin nhân viên</h2>
                            </div>
                            <form class="form-grid" action="UpdateAccount" method="POST" >
                                <div class="field">
                                    <label>Họ tên</label>
                                    <input value="${thongtindg.hoTen}" name="hoten"/>
                                </div>
                                <div class="field">
                                    <label>Địa chỉ</label>
                                    <input value="${thongtindg.diaChi}" name="diachi"/>
                                </div>
                                <div class="field">
                                    <label>Ngày sinh</label>
                                    <input type="date" name="ngaysinh" value="${thongtindg.ngaySinh}"/>
                                </div>

                                <div class="field">
                                    <label>Giới tính</label>
                                    <select name="gioitinh">
                                        <option value="">-- Chọn giới tính --</option>
                                        <option value="Nam" ${thongtindg.gioiTinh == 'Nam' ? 'selected' : ''}>Nam</option>
                                        <option value="Nữ" ${thongtindg.gioiTinh == 'Nữ' ? 'selected' : ''}>Nữ</option>
                                    </select>
                                </div>
                                <button type="submit" class="btn">Lưu thay đổi</button>
                            </form>

                        </div>
                        <div class="panel" id="password">
                            <div class="panel-header">
                                <h2>Đổi mật khẩu</h2>
                            </div>
                            <form class="form-grid" action="UpdatePassword" method="POST"onsubmit="return validatePasswordForm(event)" >
                                <div class="field">
                                    <label>Mật khẩu hiện tại</label>
                                    <input type="password" name="matkhaucu" />
                                </div>
                                <div class="field">
                                    <label>Mật khẩu mới</label>
                                    <input type="password" name="matkhaumoi1" />
                                </div>
                                <div class="field">
                                    <label>Nhập lại mật khẩu</label>
                                    <input type="password" name="matkhaumoi2" />
                                </div>
                                <button class="btn warning" type="submit">Cập nhật</button>
                            </form>

                            <c:choose>
                                <c:when test="${s == 'Mật khẩu hiện tại không đúng!'}">
                                    <div style="margin-top: 10px; padding: 10px; background-color: #ffebee; color: #c62828; border-radius: 4px; display: block;">
                                        Mật khẩu hiện tại không đúng!
                                    </div>
                                </c:when>   
                                <c:when test="${s == 'Đổi mật khẩu thành công!'}">
                                    <div style="margin-top: 10px; padding: 10px; background-color: #e8f5e9; color: #2e7d32; border-radius: 4px; display: block;">
                                        Đổi mật khẩu thành công!
                                    </div>
                                </c:when>
                                <c:when test="${s == 'Lỗi khi cập nhật mật khẩu!'}">
                                    <div style="margin-top: 10px; padding: 10px; background-color: #ffebee; color: #c62828; border-radius: 4px; display: block;">
                                        Lỗi khi cập nhật mật khẩu!
                                    </div>
                                </c:when>   
                            </c:choose>
                            <div id="message" style="margin-top: 10px; padding: 10px; border-radius: 4px; display:none;">
                                <div style="margin-top: 10px; padding: 10px; border-radius: 4px; display: none;"></div>
                            </div>
                        </div>
                    </section>
                </main>
            </div>
            <script src="assets/js.js"></script>
        
            <script src="assets/ui-upgrade.js"></script>
            <script src="assets/luxury-ui.js"></script>
        </body>
    </html>
