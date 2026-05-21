<%-- 
    Document   : Customer
    Created on : Apr 29, 2026, 5:22:49 PM
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
    if (currentAccount.getQuyen() != 1) {
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
            <link rel="stylesheet" href="assets/styles.css">
                    <title>Khách hàng | Quản lý mượn sách</title>
                    <link rel="stylesheet" href="styles.css" />
                    
            <link rel="stylesheet" href="assets/luxury-ui.css">
</head>
                    <body class="customer-page" data-role="customer">
            <jsp:include page="components/alert.jsp" />
                        <div class="app-layout">
                            <aside class="sidebar">
                                <div class="sidebar-brand"><img src="assets/image/logo1.jpg" style="border-radius: 7px 7px 7px 7px" width="40px" height="40px" alt="alt"/><span>Thư viện Số</span></div>
                                <div class="role-pill">Giao diện khách hàng</div>
                                <nav class="nav">
                                    <a class="active" href="#mgmt-book">Tìm kiếm & mượn sách</a>
                                    <a href="#profile">Sửa thông tin</a>
                                    <a href="#password">Đổi mật khẩu</a>
                                    <a href="#borrowed">Sách đang mượn</a>
                                    <a href="#history">Lịch sử mượn</a>
                                    <a href="#fines">Phí phạt</a>
                                    <a href="LogoutServlet">Đăng xuất</a>
                                </nav>
                                <div class="sidebar-footer"></div>
                            </aside>
                            <main class="main">
                                <header class="topbar">
                                    <div class="page-title"><span class="kicker"></span><h1>Xin chào, ${account.getTenDangNhap()}</h1><p></p></div>
                                    <div class="user-chip"><strong>Khách hàng</strong></div>
                                </header>
                                <section class="visual-hero customer-hero">
                                    <div>
                                        <h2>Khám phá kho sách của bạn</h2>
                                        <p>Tìm kiếm sách theo tên, tác giả, thể loại và gửi yêu cầu mượn ngay khi sách còn số lượng.</p>
                                        <div class="hero-actions">
                                            <span class="hero-chip">🔎 Tìm kiếm nhanh</span>
                                            <span class="hero-chip">📖 Mượn sách</span>
                                            <span class="hero-chip">🕒 Theo dõi lịch sử</span>
                                        </div>
                                    </div>
                                    <img src="assets/image/library/customer-library-banner.jpg" alt="Banner kho sách hiện đại" />
                                </section>
                                <section class="stats-grid" style="grid-template-columns: repeat(3, minmax(0, 1fr));">
                                    <div class="stat-card">
                                        <small>Đang mượn</small>
                                        <strong><c:out value="${sophieumuon}" default="0" /></strong>
                                    </div>
                                    <div class="stat-card">
                                        <small>Lịch sử</small>
                                        <strong><c:out value="${sophieutra}" default="0" /></strong>
                                        <em>Lượt mượn</em>
                                    </div>
                                    <div class="stat-card">
                                        <small>Trạng thái</small>
                                        <strong><c:out value="${sophieuphat}" default="0" /></strong>
                                        <em>${sophieuphat > 0 ? 'Có phí phạt' : 'Không có phí phạt'}</em>
                                    </div>
                                </section>
                                <section class="dashboard-grid" style="display: block;">
                                    <div class="panel" id="mgmt-book">
                                        <div class="panel-header">
                                            <h2>Tìm kiếm & mượn sách</h2>
                                        </div>
                                        <c:if test="${not empty borrowMessage}">
                                            <div class="alert ${borrowMessageType == 'success' ? 'alert-success' : 'alert-danger'}" style="margin: 10px 0;">
                                                ${borrowMessage}
                                            </div>
                                        </c:if>
                                        <div class="search-row">
                                            <div class="search-box">
                                                <input id="keywordd" placeholder="Tên sách, tác giả" />
                                            </div>
                                            <!-- Thay button submit bằng button thường để gọi hàm JS -->
                                            <button type="button" class="btn" onclick="searchBookss()">Tìm kiếm</button>
                                        </div>
                                        
                                        <div class="yt-book-showcase">
                                            <c:forEach var="book" items="${listsach}" begin="0" end="7">
                                                <div class="yt-book-card">
                                                    <img src="assets/image/library/book-cover-${((book.maSach - 1) % 12) + 1}.jpg" alt="Bìa sách ${book.tenSach}" />
                                                    <div>
                                                        <h3>${book.tenSach}</h3>
                                                        <p>${book.tacGia}</p>
                                                        <span>${book.soLuong == 0 ? 'Hết sách' : 'Còn sách'}</span>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <div class="table-wrap">
                                            <table>
                                                <thead>
                                                        <tr>
                                                            <th>Mã sách</th>
                                                            <th>Tên sách</th>
                                                            <th>Tác giả</th>
                                                            <th>Thể loại</th>
                                                            <th>Nhà xuất bản</th>
                                                            <th>Số lượng</th>
                                                            <th>Thao tác</th>
                                                        </tr>
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
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${ls.soLuong != 0}">
                                                                        <form action="CustomerBorrowServlet" method="post" style="margin:0;">
                                                                            <input type="hidden" name="masach" value="${ls.maSach}" />
                                                                            <button type="submit" class="btn sm">Mượn</button>
                                                                        </form>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge warn">Hết sách</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="panel" id="borrowed" >
                                        <div class="panel-header"><h2>Sách đang mượn</h2></div>
                                        <div class="table-wrap">
                                            <table>
                                                <thead>
                                                    <tr><th>Tên sách</th><th>Hạn trả</th><th>Trạng thái</th></tr>
                                                </thead>
                                                <tbody>
                                                    <c:set var="hasBorrowed" value="false" />
                                                    <c:forEach var="lst" items="${listtra}">
                                                        <c:if test="${lst.trangThai == 'Chưa trả' || lst.trangThai == 'Quá hạn'}">
                                                            <c:set var="hasBorrowed" value="true" />
                                                            <tr>
                                                                <td>${lst.tenSach}</td>
                                                                <td>${lst.hanTra}</td>
                                                                <td><span class="${lst.trangThai == 'Quá hạn' ? 'badge warn' : 'badge'}">${lst.trangThai}</span></td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                    <c:if test="${hasBorrowed == false}">
                                                        <tr><td colspan="3"><div class="smart-empty-state">Chưa có sách đang mượn.</div></td></tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </section>
                                <section class="forms-two" style="margin-top:18px">
                                    <div class="panel" id="profile"><div class="panel-header"><h2>Sửa thông tin cá nhân</h2></div>
                                        <form class="form-grid" action="UpdateAccount" method="POST">
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
                                    <div class="panel" id="password"><div class="panel-header">
                                            <h2>Đổi mật khẩu</h2>
                                        </div>
                                        <form class="form-grid" action="UpdatePassword" method="POST" onsubmit="return validatePasswordForm(event)">
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
                                            <div style="margin-top: 10px; padding: 10px; border-radius: 4px;"></div>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel" id="history" style="margin-top:18px"><div class="panel-header"><h2>Lịch sử mượn</h2></div><div class="table-wrap">
                                        <table>
                                            <thead>
                                                <tr><th>Mã phiếu</th><th>Sách</th><th>Ngày mượn</th><th>Ngày trả</th><th>Trạng thái</th></tr>
                                            </thead>
                                            <tbody>
                                                <c:set var="hasHistory" value="false" />
                                                <c:forEach var="lsm" items="${listmuon}">
                                                    <c:set var="hasHistory" value="true" />
                                                    <tr>
                                                        <td>${lsm.maPhieu}</td>
                                                        <td>${lsm.tenSach}</td>
                                                        <td>${lsm.ngayMuon}</td>
                                                        <td>${lsm.ngayTra}</td>
                                                        <td><span class="${lsm.trangThai =='Đã trả'? 'badge' : 'badge warn'}">${lsm.trangThai}</span></td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${hasHistory == false}">
                                                    <tr><td colspan="5"><div class="smart-empty-state">Chưa có lịch sử mượn/trả.</div></td></tr>
                                                </c:if>
                                            </tbody>
                                        </table></div></section>
                                <section class="panel" id="fines" style="margin-top:18px">
                                    <div class="panel-header"><h2>Phí phạt</h2></div>
                                    <div class="table-wrap">
                                        <table>
                                            <thead>
                                                <tr><th>Mã phiếu phạt</th><th>Phiếu mượn</th><th>Lý do</th><th>Số tiền</th><th>Ngày lập</th><th>Trạng thái</th></tr>
                                            </thead>
                                            <tbody>
                                                <c:set var="hasFine" value="false" />
                                                <c:forEach var="fine" items="${listphieuphat}">
                                                    <c:set var="hasFine" value="true" />
                                                    <tr>
                                                        <td>${fine.maPhieuPhat}</td>
                                                        <td>${fine.maPhieu}</td>
                                                        <td>${fine.lyDo}</td>
                                                        <td><fmt:formatNumber value="${fine.soTien}" type="number" groupingUsed="true" /> VNĐ</td>
                                                        <td>${fine.ngayLap}</td>
                                                        <td><span class="badge ${fine.trangThai == 1 ? '' : 'warn'}">${fine.trangThai == 1 ? 'Đã đóng phạt' : 'Chưa đóng phạt'}</span></td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${hasFine == false}">
                                                    <tr><td colspan="6"><div class="smart-empty-state">Hiện chưa có phí phạt nào.</div></td></tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </section>
                            </main>
                        </div>
                        <script src="assets/js.js"></script>
                    
            <script src="assets/ui-upgrade.js"></script>
            <script src="assets/luxury-ui.js"></script>
        </body>
                    </html>
