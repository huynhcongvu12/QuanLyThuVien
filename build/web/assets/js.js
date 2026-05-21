function validatePasswordForm(event) {
    event.preventDefault();
    const oldInput = event.target.querySelector('input[name="matkhaucu"]');
    const newInput = event.target.querySelector('input[name="matkhaumoi1"]');
    const confirmInput = event.target.querySelector('input[name="matkhaumoi2"]');
    if (!oldInput || !newInput || !confirmInput) {
        event.target.submit();
        return true;
    }
    const matkhaucu = oldInput.value.trim();
    const matkhaumoi1 = newInput.value.trim();
    const matkhaumoi2 = confirmInput.value.trim();

    const messageDiv = document.querySelector('#message');
    if (!messageDiv) {
        event.target.submit();
        return true;
    }

    // Kiểm tra không để trống
    if (!matkhaucu || !matkhaumoi1 || !matkhaumoi2) {
        messageDiv.textContent = 'Vui lòng điền đầy đủ các trường!';
        messageDiv.style.backgroundColor = '#ffebee';
        messageDiv.style.color = '#c62828';
        messageDiv.style.display = 'block';
        return false;
    }

    // Kiểm tra mật khẩu mới giống nhau
    if (matkhaumoi1 !== matkhaumoi2) {
        messageDiv.textContent = 'Mật khẩu mới và nhập lại mật khẩu không giống nhau!';
        messageDiv.style.backgroundColor = '#ffebee';
        messageDiv.style.color = '#c62828';
        messageDiv.style.display = 'block';
        return false;
    }

    // Nếu hợp lệ thì hiển thị thành công và submit
    messageDiv.textContent = 'Dữ liệu hợp lệ! Đang gửi...';
    messageDiv.style.backgroundColor = '#e8f5e9';
    messageDiv.style.color = '#2e7d32';
    messageDiv.style.display = 'block';

    event.target.submit();
}
function validateAccountForm(event) {
    event.preventDefault();

    const form = event.target;
    const hoten = (form.querySelector('input[name="hoten"]') || {}).value || '';
    const diachi = (form.querySelector('input[name="diachi"]') || {}).value || '';
    const ngaysinh = (form.querySelector('input[name="ngaysinh"]') || {}).value || '';
    const gioitinhEl = form.querySelector('select[name="gioitinh"], input[name="gioitinh"]');
    const gioitinh = gioitinhEl ? gioitinhEl.value : '';

    const messageDiv = document.querySelector('#message1');
    if (!messageDiv) {
        event.target.submit();
        return true;
    }

    // Kiểm tra không để trống
    if (!hoten.trim() || !diachi.trim() || !ngaysinh.trim() || !gioitinh.trim()) {
        messageDiv.textContent = 'Vui lòng điền đầy đủ các trường!';
        messageDiv.style.backgroundColor = '#ffebee';
        messageDiv.style.color = '#c62828';
        messageDiv.style.display = 'block';
        return false;
    }

    event.target.submit();
}

function validateLoginForm(event) {
    event.preventDefault();

    const emailInput = event.target.querySelector('input[name="email"]');
    const passwordInput = event.target.querySelector('input[name="password"]');
    if (!emailInput || !passwordInput) {
        event.target.submit();
        return true;
    }
    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();

    const messageDiv = document.querySelector('#message');
    if (!messageDiv) {
        event.target.submit();
        return true;
    }

    // Kiểm tra không để trống
    if (!email || !password) {
        messageDiv.textContent = 'Vui lòng điền đầy đủ các trường!';
        messageDiv.style.backgroundColor = '#ffebee';
        messageDiv.style.color = '#c62828';
        messageDiv.style.display = 'block';
        return false;
    }

    event.target.submit();
}
function searchBookss() {
    const keyword = document.getElementById("keywordd").value;
    const bookResult = document.getElementById("bookResultt"); // Tbody bên trang Độc giả

    fetch(`SachServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        let html = "";
        if (data.length === 0) {
            html = "<tr><td colspan='7' style='text-align:center;'>Không tìm thấy sách!</td></tr>";
        } else {
            data.forEach(ls => {
                const tinhTrang = ls.soLuong == 0 ? "<span class='badge warn'>Hết</span>" : "Còn: " + ls.soLuong;
                const actionButton = ls.soLuong == 0
                    ? "<span class='badge warn'>Hết sách</span>"
                    : `<form action="CustomerBorrowServlet" method="post" style="margin:0;">
                           <input type="hidden" name="masach" value="${ls.maSach}" />
                           <button type="submit" class="btn sm">Mượn</button>
                       </form>`;
                html += `
                <tr>
                    <td>${ls.maSach}</td>
                    <td><div class="book-title-cell"><img src="assets/image/library/book-cover-${((Number(ls.maSach)||1)-1)%12+1}.jpg" alt="Bìa sách"><span>${ls.tenSach}</span></div></td>
                    <td>${ls.tacGia}</td>
                    <td>${ls.theLoai}</td>
                    <td>${ls.nhaXuatBan}</td> 
                    <td>${tinhTrang}</td>
                    <td>${actionButton}</td>
                </tr>`;
            });
        }
        bookResult.innerHTML = html;
    })
    .catch(error => console.error('Lỗi:', error));
}
function searchBooks() {
    const keyword = document.getElementById("keyword").value;
    const bookResult = document.getElementById("bookResult");

    // Sử dụng Fetch API để gửi yêu cầu ngầm
    fetch(`SachServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST' // Hoặc GET tùy bạn cấu hình Servlet
    })
            .then(response => response.json()) // Giả định Servlet trả về mảng JSON
            .then(data => {
                let html = "";
                if (data.length === 0) {
                    html = "<tr><td colspan='5'>Không tìm thấy sách nào!</td></tr>";
                } else {
                    data.forEach(ls => {
                        const tinhTrang = ls.soLuong == 0 ? "Hết" : `Còn: ${ls.soLuong}`;
                        html += `
                    <tr>
                        <td>${ls.maSach}</td>
                        <td><div class="book-title-cell"><img src="assets/image/library/book-cover-${((Number(ls.maSach)||1)-1)%12+1}.jpg" alt="Bìa sách"><span>${ls.tenSach}</span></div></td>
                        <td>${ls.tacGia}</td>
                        <td>${ls.theLoai}</td>
                        <td>${ls.nhaXuatBan}</td>
                        <td>${ls.giaSach}</td>
                        <td>${tinhTrang}</td>
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
                    </tr>`;
                    });
                }
                // Chỉ cập nhật duy nhất nội dung bên trong tbody
                bookResult.innerHTML = html;
            })
            .catch(error => console.error('Lỗi AJAX:', error));
}
function saveBook() {
    const form = document.getElementById("bookForm");
    const formData = new FormData(form);
    const params = new URLSearchParams();

    // Lấy mã sách để kiểm tra là Thêm mới hay Sửa
    const maSach = formData.get("masach");

    // Nếu có mã sách thì gửi action là update, ngược lại là add
    const actionType = (maSach && maSach.trim() !== "") ? "update" : "add";
    params.append("action", actionType);

    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }
    fetch('ThemSachServlet', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert(actionType === "update" ? "Cập nhật thành công!" : "Thêm mới thành công!");
                    form.reset(); // Hàm reset form và trả lại trạng thái ban đầu
                    searchBooks();   // Tải lại bảng dữ liệu[cite: 9]
                } else {
                    alert("Lỗi: " + result.message);
                }
            })
            .catch(error => console.error('Lỗi:', error));
}
function deleteBook(maSach, button) {
    if (confirm("Bạn có chắc chắn muốn xóa sách này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("masach", maSach);

        fetch('ThemSachServlet', {// Gửi đến Servlet xử lý sách
            method: 'POST',
            body: params
        })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        // Xóa dòng đó khỏi bảng ngay lập tức trên giao diện
                        const row = button.closest('tr');
                        row.remove();
                        alert("Đã xóa thành công!");
                    } else {
                        alert("Lỗi: " + result.message);
                    }
                })
                .catch(error => console.error('Lỗi xóa:', error));
    }
}
function editBook(ma, ten, tacgia, theloai, nxb, gia, soluong) {
    document.querySelector('input[name="masach"]').value = ma;
    document.querySelector('input[name="tensach"]').value = ten;
    document.querySelector('input[name="tacgia"]').value = tacgia;
    document.querySelector('input[name="nhaxuatban"]').value = nxb;
    document.querySelector('select[name="theloai"]').value = theloai;
    document.querySelector('input[name="soluong"]').value = soluong;
    document.querySelector('input[name="giasach"]').value = gia;

    // Di chuyển màn hình về phía form để người dùng thực hiện sửa
    document.getElementById("bookForm").scrollIntoView({behavior: 'smooth'});
}
function searchReaders() {
    const keywordReaders = document.getElementById("readerKeyword").value; // ID của ô input tìm kiếm độc giả
    const readerResult = document.getElementById("readerResult"); // ID của tbody bảng độc giả

    fetch(`DocGiaServlet?search=${encodeURIComponent(keywordReaders)}`, {
        method: 'POST'
    })
            .then(response => response.json())
            .then(data => {
                let html = "";
                if (data.length === 0) {
                    html = "<tr><td colspan='6'>Không tìm thấy độc giả nào!</td></tr>";
                } else {
                    data.forEach(dg => {
                        html += `
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
                </tr>`;
                    });
                }
                readerResult.innerHTML = html;
            })
            .catch(error => console.error('Lỗi AJAX:', error));
}
function saveReader() {
    const form = document.getElementById("readerForm"); // ID của form độc giả[cite: 13]
    const formData = new FormData(form);
    const params = new URLSearchParams();

    const maDocGia = formData.get("madocgia");
    const actionType = (maDocGia && maDocGia.trim() !== "") ? "update" : "add";
    params.append("action", actionType);

    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }

    fetch('ThemDocGiaServlet', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert(actionType === "update" ? "Cập nhật độc giả thành công!" : "Thêm độc giả thành công!");
                    form.reset();
                    searchReaders(); // Tải lại danh sách
                } else {
                    alert("Lỗi: " + result.message);
                }
            })
            .catch(error => console.error('Lỗi:', error));
}
function deleteReader(maDocGia, button) {
    if (confirm("Bạn có chắc chắn muốn xóa độc giả này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("madocgia", maDocGia);

        fetch('ThemDocGiaServlet', {
            method: 'POST',
            body: params
        })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        button.closest('tr').remove();
                        alert("Đã xóa thành công!");
                    } else {
                        alert("Lỗi: " + result.message);
                    }
                })
                .catch(error => console.error('Lỗi xóa:', error));
    }
}

function editReader(ma, tendn, hoten, gioitinh, ngaysinh, diachi) {
    const form = document.getElementById("readerForm");
    form.querySelector('input[name="madocgia"]').value = ma;
    form.querySelector('input[name="tendangnhap"]').value = tendn;
    form.querySelector('input[name="hoten"]').value = hoten;
    form.querySelector('select[name="gioitinh"]').value = gioitinh;
    form.querySelector('input[name="ngaysinh"]').value = ngaysinh;
    form.querySelector('input[name="diachi"]').value = diachi;
    form.scrollIntoView({behavior: 'smooth'});
}
function searchStaff() {
    const keyword = document.getElementById("staffKeyword").value;
    const staffResult = document.getElementById("staffResult");

    fetch(`NhanVienServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST'
    })
            .then(response => response.json())
            .then(data => {
                let html = "";
                if (data.length === 0) {
                    html = "<tr><td colspan='6'>Không tìm thấy nhân viên nào!</td></tr>";
                } else {
                    data.forEach(nv => {
                        html += `
                <tr>
                    <td>${nv.maNhanVien}</td>
                    <td>${nv.hoTen}</td>
                    <td>${nv.gioiTinh}</td>
                    <td>${nv.ngaySinh}</td>
                    <td>${nv.diaChi}</td>
                    <td class="row-actions">
                        <button type="button" class="btn sm secondary" 
                            onclick="eStaff('${nv.maNhanVien}', '${nv.tenDangNhap}', '${nv.hoTen}', '${nv.gioiTinh}', '${nv.ngaySinh}', '${nv.diaChi}')">
                            Sửa
                        </button>
                        <button type="button" class="btn sm danger" onclick="deleteStaff('${nv.maNhanVien}', this)">
                            Xóa
                        </button>
                    </td>
                </tr>`;
                    });
                }
                staffResult.innerHTML = html;
            })
            .catch(error => console.error('Lỗi:', error));
}
function saveStaff() {
    const form = document.getElementById("staffForm");
    const formData = new FormData(form);
    const params = new URLSearchParams();

    const maNV = formData.get("manv");
    const actionType = (maNV && maNV.trim() !== "") ? "update" : "add";
    params.append("action", actionType);

    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }

    fetch('ThemNhanVienServlet', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert(actionType === "update" ? "Cập nhật thành công!" : "Thêm thành công!");
                    form.reset();
                    searchStaff(); // Tải lại danh sách
                } else {
                    alert("Lỗi: " + result.message);
                }
            })
            .catch(error => console.error('Lỗi:', error));
    ;
}
function deleteStaff(maNV, button) {
    if (confirm("Bạn có chắc chắn muốn xóa nhân viên này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("manv", maNV);

        fetch('ThemNhanVienServlet', {
            method: 'POST',
            body: params
        })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        button.closest('tr').remove();
                        alert("Đã xóa nhân viên thành công!");
                    } else {
                        alert("Lỗi: " + result.message);
                    }
                })
                .catch(error => console.error('Lỗi xóa:', error));
    }
}

// 4. Đổ dữ liệu vào Form để sửa
function eStaff(manv, tendn, hoten, gioitinh, ngaysinh, diachi, chucvu) {
    const form = document.getElementById("staffForm");
    form.querySelector('input[name="manv"]').value = manv;
    form.querySelector('input[name="tdangnhapnv"]').value = tendn;
    form.querySelector('input[name="htnv"]').value = hoten;
    form.querySelector('select[name="gtnv"]').value = gioitinh;
    form.querySelector('input[name="nsnv"]').value = ngaysinh;
    form.querySelector('input[name="dcnv"]').value = diachi;
    form.querySelector('select[name="cvnv"]').value = chucvu;
    form.scrollIntoView({behavior: 'smooth'});
}









function borrowActionButtons(pm) {
    const id = pm.maPhieu;
    if (pm.trangThai === 'Pending') {
        return `<form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="approve"><button class="btn sm" type="submit">Duyệt</button></form>
                <form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="reject"><input type="hidden" name="reason" value="Yêu cầu không hợp lệ"><button class="btn sm danger" type="submit">Từ chối</button></form>`;
    }
    if (pm.trangThai === 'Approved') {
        return `<form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="handover"><button class="btn sm" type="submit">Giao sách</button></form>`;
    }
    if (pm.trangThai === 'Borrowed' || pm.trangThai === 'Overdue') {
        return `<form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="return"><input type="hidden" name="tinhTrangSach" value="normal"><button class="btn sm" type="submit">Trả</button></form>
                <form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="return"><input type="hidden" name="tinhTrangSach" value="damaged"><input type="hidden" name="note" value="Sách hư hỏng"><button class="btn sm danger" type="submit">Hư</button></form>
                <form action="BorrowActionServlet" method="post" style="display:inline"><input type="hidden" name="maPhieu" value="${id}"><input type="hidden" name="action" value="return"><input type="hidden" name="tinhTrangSach" value="lost"><input type="hidden" name="note" value="Sách bị mất"><button class="btn sm danger" type="submit">Mất</button></form>`;
    }
    return pm.lyDoTuChoi || 'Xem chi tiết';
}

function searchBorrows() {
    const keyword = document.getElementById("borrowKeyword").value; // Giả định ID input search
    const borrowResult = document.getElementById("borrowResult"); // ID của tbody bảng phiếu mượn

    fetch(`PhieuMuonServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST'
    })
            .then(response => response.json())
            .then(data => {
                let html = "";
                if (data.length === 0) {
                    html = "<tr><td colspan='9'>Không tìm thấy phiếu mượn nào!</td></tr>";
                } else {
                    data.forEach(pm => {
                        html += `
                <tr>
                    <td>${pm.maPhieu}</td>
                    <td>${pm.maDocGia}</td>
                    <td>${pm.hoTen}</td>
                    <td>${pm.maSach}</td>
                    <td>${pm.tenSach}</td>
                    <td>${pm.ngayMuon}</td>
                    <td>${pm.ngayPhaiTra}</td>
                    <td><span class="badge">${pm.trangThai || ''}</span></td>
                    <td class="row-actions">
                        ${borrowActionButtons(pm)}
                    </td>
                </tr>`;
                    });
                }
                borrowResult.innerHTML = html;
            })
            .catch(error => console.error('Lỗi', error));
}
function saveBorrow() {
    const form = document.getElementById("borrowForm");
    const formData = new FormData(form);
    const params = new URLSearchParams();

    // Kiểm tra mã phiếu để quyết định action là add hay update
    const maPhieu = formData.get("maphieu");
    const actionType = (maPhieu && maPhieu.trim() !== "") ? "update" : "add";
    params.append("action", actionType);

    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }

    fetch('ThemPhieuMuonServlet', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert(actionType === "update" ? "Cập nhật phiếu mượn thành công!" : "Lập phiếu mượn thành công!");
                    form.reset();
                    searchBorrows(); // Tải lại danh sách sau khi lưu
                } else {
                    alert("Lỗi: " + result.message);
                }
            })
            .catch(error => console.error('Lỗi', error));
}
function deleteBorrow(maPhieu, button) {
    if (confirm("Bạn có chắc chắn muốn xóa phiếu mượn này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("maphieu", maPhieu);

        fetch('ThemPhieuMuonServlet', {
            method: 'POST',
            body: params
        })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        button.closest('tr').remove();
                        alert("Đã xóa phiếu mượn thành công!");
                    } else {
                        alert("Lỗi: " + result.message);
                    }
                })
                .catch(error => console.error('Lỗi:', error));
    }
}

function editBorrow(maphieu, madocgia, masach, ngaymuon, ngayphaitra) {
    const form = document.getElementById("borrowForm");
    form.querySelector('input[name="maphieu"]').value = maphieu;
    form.querySelector('input[name="madocgia"]').value = madocgia;
    form.querySelector('input[name="masach"]').value = masach;
    form.querySelector('input[name="ngaymuon"]').value = ngaymuon;
    form.querySelector('input[name="ngayphaitra"]').value = ngayphaitra;
    form.scrollIntoView({behavior: 'smooth'});
}




function searchReturns() {
    const keyword = document.getElementById("returnKeyword").value; // ID của ô input search phiếu trả
    const returnResult = document.getElementById("returnResult"); // ID của tbody bảng phiếu trả

    fetch(`PhieuTraServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST'
    })
            .then(response => response.json())
            .then(data => {
                let html = "";
                if (data.length === 0) {
                    html = "<tr><td colspan='7' style='text-align:center;'>Không tìm thấy phiếu trả nào!</td></tr>";
                } else {
                    data.forEach(pt => {
                        html += `
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
                </tr>`;
                    });
                }
                returnResult.innerHTML = html;
            })
            .catch(error => console.error('Lỗi:', error));
}
function saveReturn() {
    const form = document.getElementById("returnForm"); // ID của form nhập liệu phiếu trả
    const formData = new FormData(form);
    const params = new URLSearchParams();
    const maPhieuTra = formData.get("maphieutra");
    const actionType = (maPhieuTra && maPhieuTra.trim() !== "") ? "update" : "add";
    params.append("action", actionType);
    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }

    fetch('ThemPhieuTraSevlet', {
        method: 'POST',
        body: params
    })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert(actionType === "update" ? "Cập nhật phiếu trả thành công!" : "Lập phiếu trả thành công!");
                    form.reset();
                    searchReturns(); // Tải lại danh sách sau khi lưu thành công
                } else {
                    alert("Lỗi: " + result.message);
                }
            })
            .catch(error => console.error('Lỗi :', error));
}
function deleteReturn(maPhieuTra, button) {
    if (confirm("Bạn có chắc chắn muốn xóa phiếu trả này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("maphieutra", maPhieuTra);

        fetch('ThemPhieuTraSevlet', {
            method: 'POST',
            body: params
        })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        button.closest('tr').remove();
                        alert("Đã xóa phiếu trả thành công!");
                    } else {
                        alert("Lỗi: " + result.message);
                    }
                })
                .catch(error => console.error('Lỗi:', error));
    }
}
function editReturn(maphieutra, madocgia, masach, ngaytra, tinhtrang) {
    const form = document.getElementById("returnForm");
    if (form.querySelector('input[name="maphieutra"]'))
        form.querySelector('input[name="maphieutra"]').value = maphieutra;
    form.querySelector('input[name="madocgia"]').value = madocgia;
    form.querySelector('input[name="masach"]').value = masach;
    form.querySelector('input[name="ngaytra"]').value = ngaytra;
    if (form.querySelector('input[name="tinhtrang"]')) {
        form.querySelector('input[name="tinhtrang"]').value = tinhtrang;
    } else if (form.querySelector('select[name="tinhtrang"]')) {
        form.querySelector('select[name="tinhtrang"]').value = tinhtrang;
    }
    form.scrollIntoView({behavior: 'smooth'});
}



function searchFines() {
    const keyword = document.getElementById("fineKeyword").value; // ID của ô input search phiếu phạt
    const fineResult = document.getElementById("fineResult"); // ID của tbody bảng phiếu phạt

    fetch(`PhieuPhatServlet?search=${encodeURIComponent(keyword)}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        let html = "";
        if (data.length === 0) {
            html = "<tr><td colspan='9' style='text-align:center;'>Không tìm thấy phiếu phạt nào!</td></tr>";
        } else {
            data.forEach(p => {
                html += `
                <tr>
                    <td>${p.maPhieuPhat}</td>
                    <td>${p.maDocGia}</td>
                    <td>${p.tenDocGia}</td>
                    <td>${p.maPhieu}</td>
                    <td>${p.lyDo}</td>
                    <td>${p.soTien.toLocaleString()} VNĐ</td>
                    <td>${p.ngayLap}</td>
                    <td>
                        <span class="badge ${p.trangThai === 1 ? 'success' : 'badge danger'}">
                            ${p.trangThai === 1 ? 'Đã thu' : 'Chưa thu'}
                        </span>
                    </td>
                    <td class="row-actions">
                        <button type="button" class="btn sm secondary" 
                            onclick="editFine('${p.maPhieuPhat}', '${p.maDocGia}', '${p.maPhieu}', '${p.lyDo}', '${p.soTien}', '${p.ngayLap}', '${p.trangThai}')">
                            Sửa
                        </button>
                        <button type="button" class="btn sm danger" 
                            onclick="deleteFine('${p.maPhieuPhat}', this)">
                            Xóa
                        </button>
                    </td>
                </tr>`;
            });
        }
        fineResult.innerHTML = html;
    })
    .catch(error => console.error('Lỗi:', error));
}

// 2. Hàm Lưu (Thêm hoặc Sửa) Phiếu Phạt
function saveFine() {
    const form = document.getElementById("fineForm"); // ID của form nhập liệu phiếu phạt
    const formData = new FormData(form);
    const params = new URLSearchParams();
    const maPhieuPhat = formData.get("maphieuphat");
    const actionType = (maPhieuPhat && maPhieuPhat.trim() !== "") ? "update" : "add";
    params.append("action", actionType);
    for (const pair of formData) {
        params.append(pair[0], pair[1]);
    }
    fetch('ThemPhieuPhatServlet', {
        method: 'POST',
        body: params
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert(actionType === "update" ? "Cập nhật phiếu phạt thành công!" : "Lập phiếu phạt thành công!");
            form.reset();
            form.querySelector('input[name="maphieuphat"]').value = "";
            searchFines(); 
        } else {
            alert("Lỗi: " + result.message);
        }
    })
    .catch(error => console.error('Lỗi :', error));
}

// 3. Hàm Xóa Phiếu Phạt
function deleteFine(maPhieuPhat, button) {
    if (confirm("Bạn có chắc chắn muốn xóa phiếu phạt này không?")) {
        const params = new URLSearchParams();
        params.append("action", "delete");
        params.append("maphieuphat", maPhieuPhat);

        fetch('ThemPhieuPhatServlet', {
            method: 'POST',
            body: params
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                button.closest('tr').remove();
                alert("Đã xóa phiếu phạt thành công!");
            } else {
                alert("Lỗi: " + result.message);
            }
        })
        .catch(error => console.error('Lỗi:', error));
    }
}

// 4. Hàm Đổ dữ liệu vào Form khi nhấn nút "Sửa"
function editFine(maphieuphat, madocgia, maphieu, lydo, sotien, ngaylap, trangthai) {
    const form = document.getElementById("fineForm");
    form.querySelector('input[name="maphieuphat"]').value = maphieuphat;
    form.querySelector('input[name="madocgia"]').value = madocgia;
    form.querySelector('input[name="maphieu"]').value = maphieu;
    form.querySelector('select[name="lydo"]').value = lydo;
    form.querySelector('input[name="sotien"]').value = sotien;
    form.querySelector('input[name="ngaylap"]').value = ngaylap;
    form.querySelector('select[name="trangthai"]').value = trangthai;
    form.scrollIntoView({behavior: 'smooth'});
}
/* ============================================================
   MODERN UI/UX ENHANCEMENTS - Library Management System
   Added: Dark/Light Mode, Clock, Toast, Loading, Realtime Search,
   Sidebar Collapse, Profile Dropdown, Animations
   ============================================================ */

// ==================== 1. THEME (DARK / LIGHT) ====================
function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    if (savedTheme === 'dark') {
        document.documentElement.classList.add('dark');
    }
    
    // Create toggle button if not exists (for pages that have topbar)
    const topbar = document.querySelector('.topbar');
    if (topbar && !document.querySelector('.theme-toggle')) {
        const toggleBtn = document.createElement('button');
        toggleBtn.className = 'theme-toggle';
        toggleBtn.innerHTML = document.documentElement.classList.contains('dark') ? '☀️' : '🌙';
        toggleBtn.title = 'Chuyển chế độ sáng/tối';
        toggleBtn.onclick = toggleTheme;
        
        // Insert before user profile or at end of topbar actions
        const userProfile = topbar.querySelector('.user-profile') || topbar.querySelector('.user-chip');
        if (userProfile) {
            userProfile.parentNode.insertBefore(toggleBtn, userProfile);
        } else {
            topbar.appendChild(toggleBtn);
        }
    }
}

function toggleTheme() {
    const isDark = document.documentElement.classList.toggle('dark');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    
    // Update toggle icon
    const toggleBtn = document.querySelector('.theme-toggle');
    if (toggleBtn) {
        toggleBtn.innerHTML = isDark ? '☀️' : '🌙';
    }
    
    // Optional: show toast
    showToast(isDark ? 'info' : 'info', isDark ? 'Đã chuyển sang chế độ tối' : 'Đã chuyển sang chế độ sáng', 1800);
}

// ==================== 2. REALTIME CLOCK ====================
function updateClock() {
    const clockEl = document.getElementById('clockWidget');
    if (!clockEl) return;

    const now = new Date();
    
    const days = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];
    const dayName = days[now.getDay()];
    
    const dateStr = `${now.getDate().toString().padStart(2, '0')}/${(now.getMonth()+1).toString().padStart(2,'0')}/${now.getFullYear()}`;
    
    const timeStr = now.toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit',
        hour12: false 
    });

    clockEl.innerHTML = `
        <div class="clock-time">${timeStr}</div>
        <div class="clock-date">${dayName}, ${dateStr}</div>
    `;
}

function initClock() {
    // Create clock widget in topbar if not exists
    const topbar = document.querySelector('.topbar');
    if (topbar && !document.getElementById('clockWidget')) {
        const clockDiv = document.createElement('div');
        clockDiv.id = 'clockWidget';
        clockDiv.className = 'clock-widget';
        clockDiv.style.marginRight = '16px';
        
        // Insert at beginning of topbar or after page-title
        const pageTitle = topbar.querySelector('.page-title');
        if (pageTitle) {
            pageTitle.parentNode.insertBefore(clockDiv, pageTitle.nextSibling);
        } else {
            topbar.prepend(clockDiv);
        }
    }
    
    updateClock();
    setInterval(updateClock, 1000);
}

// ==================== 3. TOAST NOTIFICATION SYSTEM ====================
function showToast(type = 'success', message = '', duration = 3200) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    let icon = '✅';
    if (type === 'error') icon = '❌';
    if (type === 'warning') icon = '⚠️';
    if (type === 'info') icon = 'ℹ️';

    toast.innerHTML = `
        <div class="toast-icon">${icon}</div>
        <div class="toast-content">${message}</div>
        <button class="toast-close" onclick="this.parentElement.remove()">×</button>
    `;

    container.appendChild(toast);

    // Auto remove
    setTimeout(() => {
        if (toast.parentNode) toast.parentNode.removeChild(toast);
    }, duration);
}

// Override default alert for better UX (optional - keep for compatibility)
const originalAlert = window.alert;
window.alert = function(msg) {
    // Use toast for modern feel, fallback to original if needed
    showToast('info', msg, 4000);
};

// ==================== 4. LOADING OVERLAY ====================
function showLoading(text = 'Đang xử lý...') {
    let overlay = document.querySelector('.loading-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.className = 'loading-overlay';
        overlay.innerHTML = `
            <div class="spinner"></div>
            <div class="loading-text">${text}</div>
        `;
        document.body.appendChild(overlay);
    } else {
        overlay.querySelector('.loading-text').textContent = text;
    }
    overlay.style.display = 'flex';
    overlay.classList.add('show');
}

function hideLoading() {
    const overlay = document.querySelector('.loading-overlay');
    if (overlay) {
        overlay.style.display = 'none';
        overlay.classList.remove('show');
    }
}

// ==================== 5. SIDEBAR COLLAPSE ====================
function initSidebarCollapse() {
    const sidebar = document.querySelector('.sidebar');
    if (!sidebar) return;

    // Add toggle button
    if (!sidebar.querySelector('.sidebar-toggle')) {
        const toggle = document.createElement('button');
        toggle.className = 'sidebar-toggle';
        toggle.innerHTML = '⟪';
        toggle.title = 'Thu gọn / Mở rộng menu';
        toggle.onclick = () => {
            sidebar.classList.toggle('collapsed');
            toggle.innerHTML = sidebar.classList.contains('collapsed') ? '⟫' : '⟪';
            localStorage.setItem('sidebarCollapsed', sidebar.classList.contains('collapsed'));
        };
        sidebar.appendChild(toggle);
    }

    // Restore state
    if (localStorage.getItem('sidebarCollapsed') === 'true') {
        sidebar.classList.add('collapsed');
        const toggle = sidebar.querySelector('.sidebar-toggle');
        if (toggle) toggle.innerHTML = '⟫';
    }

    // Mobile: auto collapse on small screen
    function handleResize() {
        if (window.innerWidth < 1024 && !sidebar.classList.contains('collapsed')) {
            sidebar.classList.add('collapsed');
        }
    }
    window.addEventListener('resize', handleResize);
    handleResize();
}

// ==================== 6. PROFILE DROPDOWN ====================
function initProfileDropdown() {
    const userChip = document.querySelector('.user-chip');
    if (!userChip || userChip.parentElement.classList.contains('user-profile')) return;

    // Wrap in profile container
    const wrapper = document.createElement('div');
    wrapper.className = 'user-profile';
    userChip.parentNode.insertBefore(wrapper, userChip);
    wrapper.appendChild(userChip);

    // Create dropdown menu
    const dropdown = document.createElement('div');
    dropdown.className = 'dropdown-menu';
    
    const userName = userChip.querySelector('strong') ? userChip.querySelector('strong').textContent : 'User';
    const roleText = document.querySelector('.role-pill') ? document.querySelector('.role-pill').textContent : '';

    dropdown.innerHTML = `
        <div class="dropdown-header">
            <strong>${userName}</strong>
            <small>${roleText}</small>
        </div>
        <a href="#profile" class="dropdown-item"><span>👤</span> Hồ sơ cá nhân</a>
        <a href="#password" class="dropdown-item"><span>🔒</span> Đổi mật khẩu</a>
        <div style="height:1px; background:var(--line); margin:6px 0;"></div>
        <a href="Login.jsp" class="dropdown-item danger"><span>🚪</span> Đăng xuất</a>
    `;

    wrapper.appendChild(dropdown);

    // Toggle on click
    userChip.onclick = (e) => {
        e.stopImmediatePropagation();
        dropdown.classList.toggle('show');
    };

    // Close when click outside
    document.addEventListener('click', (e) => {
        if (!wrapper.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });
}

// ==================== 7. REALTIME TABLE SEARCH (Client-side filter + highlight) ====================
function initRealtimeSearch() {
    // For book search in Admin/Staff
    const keywordInput = document.getElementById('keyword');
    if (keywordInput) {
        keywordInput.addEventListener('input', debounce(() => {
            filterTableRows('bookResult', keywordInput.value);
        }, 280));
    }

    // Reader
    const readerInput = document.getElementById('readerKeyword');
    if (readerInput) {
        readerInput.addEventListener('input', debounce(() => {
            filterTableRows('readerResult', readerInput.value);
        }, 280));
    }

    // Staff
    const staffInput = document.getElementById('staffKeyword');
    if (staffInput) {
        staffInput.addEventListener('input', debounce(() => {
            filterTableRows('staffResult', staffInput.value);
        }, 280));
    }

    // Borrow, Return, Fine can use same pattern
    const borrowInput = document.getElementById('borrowKeyword');
    if (borrowInput) {
        borrowInput.addEventListener('input', debounce(() => {
            filterTableRows('borrowResult', borrowInput.value);
        }, 280));
    }
    
    const returnInput = document.getElementById('returnKeyword');
    if (returnInput) {
        returnInput.addEventListener('input', debounce(() => {
            filterTableRows('returnResult', returnInput.value);
        }, 280));
    }
    
    const fineInput = document.getElementById('fineKeyword');
    if (fineInput) {
        fineInput.addEventListener('input', debounce(() => {
            filterTableRows('fineResult', fineInput.value);
        }, 280));
    }
}

function filterTableRows(tbodyId, keyword) {
    const tbody = document.getElementById(tbodyId);
    if (!tbody) return;

    const rows = tbody.querySelectorAll('tr');
    const lowerKeyword = keyword.toLowerCase().trim();

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        if (lowerKeyword === '') {
            row.style.display = '';
            // remove previous highlights
            row.querySelectorAll('mark.search-highlight').forEach(m => {
                m.outerHTML = m.innerHTML;
            });
        } else if (text.includes(lowerKeyword)) {
            row.style.display = '';
            // simple highlight (first match)
            highlightTextInRow(row, lowerKeyword);
        } else {
            row.style.display = 'none';
        }
    });
}

function highlightTextInRow(row, keyword) {
    // Remove old highlights
    row.querySelectorAll('mark.search-highlight').forEach(m => m.outerHTML = m.innerHTML);

    const cells = row.querySelectorAll('td');
    cells.forEach(cell => {
        if (cell.textContent.toLowerCase().includes(keyword)) {
            const original = cell.innerHTML;
            const regex = new RegExp(`(${keyword})`, 'gi');
            cell.innerHTML = original.replace(regex, '<mark class="search-highlight">$1</mark>');
        }
    });
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ==================== 8. ENHANCE EXISTING CRUD TO USE TOAST ====================
// We can wrap the alert calls in existing functions, but to avoid breaking, 
// we add helper and recommend replacing alert in future.
// For now, the showToast is available globally.

// Example: Replace alert in save functions (we can do it here for key ones)
function enhanceExistingAlerts() {
    // This is optional - the original functions still work with alert (now shows as toast via override)
    console.log('%c[UI] Modern toast system active. Alerts will show as beautiful toasts.', 'color:#2a9f7a');
}

// ==================== 9. INIT ALL MODERN FEATURES ====================
function initModernUI() {
    initTheme();
    initClock();
    initSidebarCollapse();
    initProfileDropdown();
    initRealtimeSearch();
    enhanceExistingAlerts();
    
    // Add subtle card hover class to panels and stat cards
    document.querySelectorAll('.panel, .stat-card').forEach(el => {
        el.classList.add('card-hover');
    });

    // Show welcome toast on first load (optional)
    // setTimeout(() => {
    //     if (!localStorage.getItem('welcomed')) {
    //         showToast('info', 'Chào mừng bạn đến với hệ thống thư viện hiện đại!', 2500);
    //         localStorage.setItem('welcomed', '1');
    //     }
    // }, 1200);

    console.log('%c[Library UI] All modern enhancements initialized successfully.', 'color:#1f6f5b; font-weight:600');
}

// Auto init when DOM ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initModernUI);
} else {
    initModernUI();
}

// Also expose some functions globally for debugging / future use
window.showToast = showToast;
window.showLoading = showLoading;
window.hideLoading = hideLoading;
window.toggleTheme = toggleTheme;
