<%-- 
    Document   : Admin
    Created on : Apr 29, 2026, 5:55:00 PM
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
    if (currentAccount.getQuyen() != 0) {
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
            <title>Admin | Quản lý mượn sách</title>
            <link rel="stylesheet" href="styles.css" />
            <link rel="stylesheet" href="assets/styles.css">
        
            <link rel="stylesheet" href="assets/luxury-ui.css">
</head>

        <body class="admin-page" data-role="admin">
            <jsp:include page="components/alert.jsp" />
            <div class="app-layout">
                <aside class="sidebar">
                    <div class="sidebar-brand"><img src="assets/image/logo1.jpg" style="border-radius: 7px 7px 7px 7px" width="40px" height="40px" alt="alt"/><span>Thư viện Số</span></div>
                    <div class="role-pill">Giao diện admin</div>
                    <nav class="nav"><a class="active" href="#stats">Thống kê</a>
                        <a href="#mgmt-book">Quản lý sách</a>
                        <a href="#mgmt-staff">Nhân viên</a>
                        <a href="#mgmt-account">Tài khoản</a>
                        <a href="#mgmt-reader">Độc giả</a>
                        <a href="#mgmt-borrow">Lập phiếu mượn</a>
                        <a href="#mgmt-return">Lập phiếu trả</a>
                        <a href="#mgmt-fine">Lập phiếu phạt</a>
                        <a href="LogoutServlet">Đăng xuất</a>
                    </nav>
                    <div class="sidebar-footer"></div>
                </aside>
                <main class="main">
                    <header class="topbar">
                        <div class="page-title"><span class="kicker"></span>
                            <h1>Quản lý thư viện</h1>
                            <p></p>
                        </div>
                        <div class="user-chip">
                            <strong>Admin</strong>
                        </div>
                    </header>
                    <section class="visual-hero admin-hero">
                        <div>
                            <h2>Trung tâm quản trị thư viện</h2>
                            <p>Theo dõi thống kê, quản lý sách, độc giả, nhân viên và toàn bộ nghiệp vụ mượn/trả/phạt trên một màn hình rõ ràng.</p>
                            <div class="hero-actions">
                                <span class="hero-chip">📚 Quản lý sách</span>
                                <span class="hero-chip">👥 Phân quyền</span>
                                <span class="hero-chip">📊 Thống kê</span>
                            </div>
                        </div>
                        <img src="assets/image/library/admin-dashboard-banner.jpg" alt="Minh họa dashboard admin" />
                    </section>
                    <c:set var="adminBorrowedCount" value="0" />
                    <c:set var="adminOverdueCount" value="0" />
                    <c:forEach var="pmStat" items="${listphieumuon}">
                        <c:if test="${pmStat.trangThai == 'Borrowed'}">
                            <c:set var="adminBorrowedCount" value="${adminBorrowedCount + 1}" />
                        </c:if>
                        <c:if test="${pmStat.trangThai == 'Overdue'}">
                            <c:set var="adminOverdueCount" value="${adminOverdueCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <section class="stats-grid admin-overview-grid" id="stats">
                        <div class="stat-card"><small>Tổng sách</small><strong>${fn:length(listsach)}</strong><em>Đầu sách trong hệ thống</em></div>
                        <div class="stat-card"><small>Tổng độc giả</small><strong>${fn:length(listdocgia)}</strong><em>Tài khoản Customer</em></div>
                        <div class="stat-card"><small>Tổng nhân viên</small><strong>${fn:length(listnhanvien)}</strong><em>Tài khoản Staff</em></div>
                        <div class="stat-card"><small>Sách đang mượn</small><strong>${adminBorrowedCount}</strong><em>Phiếu Borrowed</em></div>
                        <div class="stat-card"><small>Sách quá hạn</small><strong>${adminOverdueCount}</strong><em>Cần theo dõi</em></div>
                        <div class="stat-card"><small>Phạt chưa đóng</small><strong>${phatthangchuatra}</strong><em>Phiếu chưa thu</em></div>
                        <div class="stat-card"><small>Tiền phạt tháng này</small><strong><fmt:formatNumber value="${tongtienphat}" type="number" groupingUsed="true" /></strong><em>VNĐ</em></div>
                    </section>
                    <section class="stats-grid admin-month-grid">
                        <div class="stat-card"><small>Lượt mượn tháng</small><strong>${luotmuonthang}</strong><em></em></div>
                        <div class="stat-card"><small>Lượt trả tháng</small><strong>${luottrathang}</strong><em></em></div>
                        <div class="stat-card"><small>Phiếu phạt</small><strong>${phatthang}</strong><em>${phatthangchuatra} phiếu chưa thu</em></div>
                        <div class="stat-card"><small>Doanh thu phạt</small><strong><fmt:formatNumber value="${tongtienphat}" type="number" groupingUsed="true" /></strong><em>VNĐ</em></div>
                    </section>
                    <section class="dashboard-grid">
                       
                    </section>
                    <section class="panel" id="mgmt-book" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý sách</h2>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input id="keyword" placeholder="Tên sách, tác giả" />
                            </div>
                            <!-- Thay button submit bằng button thường để gọi hàm JS -->
                            <button type="button" class="btn" onclick="searchBooks()">Tìm kiếm</button>
                        </div>
                        <form class="modal-form" id="bookForm">
                            <h3>Thêm / Sửa sách</h3>
                            <div class="field full">
                                <label>Mã sách</label>
                                <input placeholder="BK-2050" name="masach"  readonly />
                            </div>
                            <div class="field">
                                <label>Tên sách</label>
                                <input placeholder="Tên đầy đủ của sách" name="tensach"/>
                            </div>
                            <div class="field">
                                <label>Tác giả</label>
                                <input placeholder="Tên tác giả" name="tacgia"/>
                            </div>
                            <div class="field">
                                <label>Nhà xuất bản</label>
                                <input placeholder="Nhà xuất bản" name="nhaxuatban"/>
                            </div>
                            <div class="field">
                                <label>Thể loại</label>
                                <select name="theloai">
                                    <option>Văn học</option>
                                    <option>Công nghệ</option>
                                    <option>Khoa học</option>
                                    <option>Toán học</option>
                                    <option>Lý Luận</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>Số lượng</label>
                                <input type="number" placeholder="10" name="soluong"/>
                            </div>
                            <div class="field">
                                <label>Giá sách</label>
                                <input placeholder="1000VNĐ" name="giasach"/>
                            </div>
                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn" onclick="saveBook()">Lưu sách</button>
                            </div>
                        </form>
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
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                </thead>
                                <tbody id="bookResult" style="font: Arial, Helvetica, sans-serif;">
                                    <c:forEach var="ls" items="${listsach}">
                                        <!-- Dữ liệu mặc định khi mới load trang -->
                                        <tr>
                                            <td>${ls.maSach}</td>
                                            <td><div class="book-title-cell"><img src="assets/image/library/book-cover-${((ls.maSach - 1) % 12) + 1}.jpg" alt="Bìa sách"><span>${ls.tenSach}</span></div></td>
                                            <td>${ls.tacGia}</td>
                                            <td>${ls.theLoai}</td>
                                            <td>${ls.nhaXuatBan}</td>
                                            <td>
                                                <fmt:formatNumber value="${ls.giaSach}" type="number" groupingUsed="true" /> VNĐ
                                            </td>
                                            <td>${ls.soLuong == 0 ? "Hết" : "Còn: ".concat(ls.soLuong)}</td>
                                            <td class="row-actions">
                                                <button type="button" class="btn sm secondary" 
                                                        onclick="editBook('${ls.maSach}', '${ls.tenSach}', '${ls.tacGia}', '${ls.theLoai}', '${ls.nhaXuatBan}', '${ls.giaSach}', '${ls.soLuong}')">
                                                    Sửa
                                                </button>
                                                <button type="button" class="btn sm danger" 
                                                        onclick="deleteBook('${ls.maSach}', this)">
                                                    Xóa
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="panel" id="mgmt-reader" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý độc giả</h2>
                            <button class="btn">Clear</button>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input placeholder="Tên độc giả" id="readerKeyword"/>
                            </div>
                            <!-- Thay button submit bằng button thường để gọi hàm JS -->
                            <button type="button" class="btn" onclick="searchReaders()">Tìm kiếm</button>
                        </div>
                        <form class="modal-form" id="readerForm">
                            <h3>Thêm / Sửa độc giả</h3>
                            <div class="field">
                                <label>Mã độc giả</label>
                                <input placeholder="DG-0125" name="madocgia" readonly/></div>
                            <div class="field">
                                <label>Tên đăng nhập</label>
                                <input placeholder="aaaa@gmail.com" name="tendangnhap"/></div>
                            <div class="field">
                                <label>Họ tên</label>
                                <input placeholder="Nguyễn Thị B" name="hoten"/>
                            </div>
                            <div class="field">
                                <label>Giới tính</label>
                                <select name="gioitinh">
                                    <option value="">-- Chọn giới tính --</option>
                                    <option value="Nam">Nam</option>
                                    <option value="Nữ">Nữ</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>Ngày sinh</label>
                                <input type="date" name="ngaysinh"/>
                            </div>
                            <div class="field">
                                <label>Địa chỉ</label>
                                <input placeholder="Số nhà, phường, quận, tỉnh" name="diachi" />
                            </div>
                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn" onclick="saveReader()">Lưu độc giả</button>
                            </div>
                        </form>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã độc giả</th>
                                        <th>Họ tên</th>
                                        <th>Giới tính</th>
                                        <th>Ngày sinh</th>
                                        <th>Địa chỉ</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="readerResult">
                                    <c:forEach var="dg" items="${listdocgia}">
                                        <tr>
                                            <td>${dg.maDocGia}</td>
                                            <td>${dg.hoTen}</td>
                                            <td>${dg.gioiTinh}</td>
                                            <td>${dg.ngaySinh}</td>
                                            <td>${dg.diaChi}</td>
                                            <td class="row-actions">
                                                <button type="button" class="btn sm secondary" 
                                                        onclick="editReader('${dg.maDocGia}', '${dg.tenDangNhap}', '${dg.hoTen}', '${dg.gioiTinh}', '${dg.ngaySinh}', '${dg.diaChi}')">
                                                    Sửa
                                                </button>
                                                <button type="button" class="btn sm danger" 
                                                        onclick="deleteReader('${dg.maDocGia}', this)">
                                                    Xóa
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="panel" id="mgmt-staff" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý nhân viên</h2>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input placeholder="Tên nhân viên" id="staffKeyword"/>
                            </div>
                            <!-- Thay button submit bằng button thường để gọi hàm JS -->
                            <button type="button" class="btn" onclick="searchStaff()">Tìm kiếm</button>
                        </div>
                        <!-- Form Thêm/Sửa: Đặt ID và Name chính xác -->
                        <form class="modal-form" id="staffForm">
                            <h3>Thêm / Sửa nhân viên</h3>
                            <div class="field full">
                                <label>Mã nhân viên</label>
                                <input name="manv" readonly placeholder="NV-001" />
                            </div>
                            <div class="field">
                                <label>Tên đăng nhập</label>
                                <input placeholder="aaaa@gmail.com" name="tdangnhapnv"/></div>
                            <div class="field">
                                <label>Họ tên</label>
                                <input placeholder="Nguyễn Thị B" name="htnv"/>
                            </div>
                            <div class="field">
                                <label>Giới tính</label>
                                <select name="gtnv">
                                    <option value="">-- Chọn giới tính --</option>
                                    <option value="Nam">Nam</option>
                                    <option value="Nữ">Nữ</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>Ngày sinh</label>
                                <input type="date" name="nsnv"/>
                            </div>
                            <div class="field">
                                <label>Địa chỉ</label>
                                <input name="dcnv" placeholder="Số nhà, phường, quận, tỉnh" />
                            </div>
                            <div class="field">
                                <label>Chức vụ</label>
                                <select name="cvnv">
                                    <option value="">-- Chọn chức vụ --</option>
                                    <option value="Quản trị">Quản trị</option>
                                    <option value="Thủ thư">Thủ thư</option>
                                </select>
                            </div>
                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn" onclick="saveStaff()">Lưu nhân viên</button>
                            </div>
                        </form>

                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã nhân viên</th>
                                        <th>Họ tên</th>
                                        <th>Giới tính</th>
                                        <th>Ngày sinh</th>
                                        <th>Địa chỉ</th>
                                        <th>Chức vụ</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="staffResult">
                                    <c:forEach var="listnv" items="${listnhanvien}">
                                        <tr>
                                            <td>${listnv.maNhanVien}</td>
                                            <td>${listnv.hoTen}</td>
                                            <td>${listnv.gioiTinh}</td>
                                            <td>${listnv.ngaySinh}</td>
                                            <td>${listnv.diaChi}</td>
                                            <td>${listnv.chucVu}</td>
                                            <td class="row-actions">
                                                <button type="button" class="btn sm secondary" 
                                                        onclick="eStaff('${listnv.maNhanVien}', '${listnv.tenDangNhap}', '${listnv.hoTen}', '${listnv.gioiTinh}', '${listnv.ngaySinh}', '${listnv.diaChi}', '${listnv.chucVu}')">
                                                    Sửa
                                                </button>
                                                <button type="button" class="btn sm danger" onclick="deleteStaff('${listnv.maNhanVien}', this)">Xóa</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="panel admin-account-panel" id="mgmt-account" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý tài khoản</h2>
                            <span class="badge">Admin / Staff / Customer</span>
                        </div>
                        <div class="admin-account-summary">
                            <div><strong>Admin</strong><span>Toàn quyền quản trị, phân quyền, thống kê và dữ liệu.</span></div>
                            <div><strong>Staff</strong><span>Xử lý nghiệp vụ mượn, giao sách, trả sách và phạt.</span></div>
                            <div><strong>Customer</strong><span>Tìm sách, gửi yêu cầu mượn và theo dõi lịch sử.</span></div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Tài khoản</th>
                                        <th>Người dùng</th>
                                        <th>Vai trò</th>
                                        <th>Mã liên kết</th>
                                        <th>Trạng thái</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="dg" items="${listdocgia}">
                                        <tr>
                                            <td>${dg.tenDangNhap}</td>
                                            <td>${dg.hoTen}</td>
                                            <td><span class="badge status-borrowed">Customer</span></td>
                                            <td>DG-${dg.maDocGia}</td>
                                            <td><span class="badge">Đang hoạt động</span></td>
                                        </tr>
                                    </c:forEach>
                                    <c:forEach var="listnv" items="${listnhanvien}">
                                        <tr>
                                            <td>${listnv.tenDangNhap}</td>
                                            <td>${listnv.hoTen}</td>
                                            <td><span class="badge status-approved">Staff</span></td>
                                            <td>NV-${listnv.maNhanVien}</td>
                                            <td><span class="badge">Đang hoạt động</span></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty listdocgia && empty listnhanvien}">
                                        <tr><td colspan="5"><div class="smart-empty-state">Chưa có dữ liệu tài khoản để hiển thị.</div></td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="panel" id="mgmt-borrow" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý phiếu mượn</h2>
                        </div>
                        <div class="search-row">
                            <div class="search-box">
                                <input id="borrowKeyword" placeholder="Mã phiếu, tên độc giả..." />
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
                    <section class="panel" id="mgmt-return" style="margin-top:18px">
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
                    <section class="panel" id="mgmt-fine" style="margin-top:18px">
                        <div class="panel-header">
                            <h2>Quản lý phiếu phạt</h2>
                        </div>

                        <div class="search-row">
                            <div class="search-box">
                                <input id="fineKeyword" placeholder="Mã phiếu phạt, tên độc giả" />
                            </div>
                            <button type="button" class="btn danger" onclick="searchFines()">Tìm kiếm</button>
                        </div>

                        <form class="modal-form" id="fineForm">
                            <h3>Thêm / Sửa phiếu phạt</h3>

                            <div class="field full">
                                <label>Mã phiếu phạt</label>
                                <input type="text" name="maphieuphat" placeholder="PP-001" readonly />
                            </div>

                            <div class="field">
                                <label>Mã độc giả</label>
                                <input type="number" name="madocgia" placeholder="Nhập mã độc giả" required />
                            </div>

                            <div class="field">
                                <label>Mã phiếu mượn liên quan</label>
                                <input type="number" name="maphieu" placeholder="Mã phiếu mượn" required />
                            </div>

                            <div class="field">
                                <label>Lý do phạt</label>
                                <select name="lydo">
                                    <option value="">-- Lý do --</option>
                                    <option value="Trả sách quá hạn">Trả sách quá hạn</option>
                                    <option value="Làm hỏng sách">Làm hỏng sách</option>
                                    <option value="Làm mất sách">Làm mất sách</option>
                                    <option value="Khác">Khác</option>
                                </select>
                            </div>

                            <div class="field">
                                <label>Số tiền (VNĐ)</label>
                                <input type="number" name="sotien" placeholder="Ví dụ: 50000" required />
                            </div>

                            <div class="field">
                                <label>Ngày lập</label>
                                <input type="date" name="ngaylap" required />
                            </div>

                            <div class="field">
                                <label>Trạng thái</label>
                                <select name="trangthai">
                                    <option value="0">Chưa thu</option>
                                    <option value="1">Đã thu</option>
                                </select>
                            </div>

                            <div class="actions">
                                <button type="button" class="btn ghost" onclick="this.form.reset()">Hủy</button>
                                <button type="button" class="btn danger" onclick="saveFine()">Lưu phiếu phạt</button>
                            </div>
                        </form>

                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã phiếu</th>
                                        <th>Mã độc giả</th>
                                        <th>Họ tên</th>
                                        <th>Mã phiếu phạt</th>
                                        <th>Lý do</th>
                                        <th>Số tiền</th>
                                        <th>Ngày lập</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody id="fineResult">
                                    <c:forEach var="listpp" items="${listphieuphat}">
                                        <tr>
                                            <td>${listpp.maPhieuPhat}</td>
                                            <td>${listpp.maDocGia}</td>
                                            <td>${listpp.tenDocGia}</td>
                                            <td>${listpp.maPhieu}</td>
                                            <td>${listpp.lyDo}</td>
                                            <td>
                                                <fmt:formatNumber value="${listpp.soTien}" type="number" groupingUsed="true" /> VNĐ
                                            </td>
                                            <td>${listpp.ngayLap}</td>
                                            <td>
                                                <span class="badge ${listpp.trangThai == 1 ? 'success' : 'badge danger'}">
                                                    ${listpp.trangThai == 1 ? 'Đã thu' : 'Chưa thu'}
                                                </span>
                                            </td>
                                            <td class="row-actions">
                                                <button type="button" class="btn sm secondary" 
                                                        onclick="editFine('${listpp.maPhieuPhat}', '${listpp.maDocGia}', '${listpp.maPhieu}', '${listpp.lyDo}', '${listpp.soTien}', '${listpp.ngayLap}', '${listpp.trangThai}')">
                                                    Sửa
                                                </button>
                                                <button type="button" class="btn sm danger" 
                                                        onclick="deleteFine('${listpp.maPhieuPhat}', this)">
                                                    Xóa
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
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
