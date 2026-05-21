# QuanLyThuVien - Bản tích hợp nghiệp vụ đầy đủ

## Tài khoản test
- admin / 123: quản trị viên
- staff / 123: nhân viên
- customer / 123: độc giả

## Chức năng đã tích hợp rõ theo từng giao diện

### 1. Giao diện khách hàng
- Tìm kiếm sách theo tên sách hoặc tác giả.
- Xem danh sách sách, số lượng còn/hết.
- Mượn sách trực tiếp bằng nút **Mượn** trên từng dòng sách.
- Khi mượn thành công, hệ thống tự tạo phiếu mượn với hạn trả 14 ngày.
- Tự động trừ số lượng sách sau khi mượn.
- Chặn mượn sách nếu sách đã hết.
- Chặn mượn trùng cùng một sách khi khách hàng chưa trả.
- Xem sách đang mượn và hạn trả.
- Xem lịch sử mượn/trả/quá hạn.
- Sửa thông tin cá nhân.
- Đổi mật khẩu.

### 2. Giao diện nhân viên
- Tìm kiếm sách.
- Lập phiếu mượn cho độc giả.
- Lập phiếu trả.
- Khi lập phiếu trả, hệ thống tự cộng lại số lượng sách.
- Xem thống kê nghiệp vụ trong ngày: phiếu mượn, phiếu trả, tổng sách, phiếu quá hạn.
- Sửa thông tin nhân viên.
- Đổi mật khẩu.

### 3. Giao diện admin
- Quản lý sách: thêm, sửa, xóa, tìm kiếm.
- Quản lý độc giả: thêm, sửa, xóa, tìm kiếm.
- Quản lý nhân viên: thêm, sửa, xóa, tìm kiếm, phân quyền.
- Quản lý phiếu mượn.
- Quản lý phiếu trả.
- Quản lý phiếu phạt.
- Thống kê tổng quan: lượt mượn tháng, lượt trả tháng, phiếu phạt, tiền phạt, tăng trưởng.

## File quan trọng đã cập nhật
- `web/Customer.jsp`
- `web/assets/js.js`
- `src/java/CustomerBorrowServlet.java`
- `src/java/Process_Data/PhieuMuon.java`
- `src/java/Process_Data/PhieuTra.java`

## Ghi chú chạy project
Sau khi import vào NetBeans, clean/build lại project để NetBeans biên dịch servlet mới. Nếu dùng database mới, chạy file SQL chuẩn 5 dữ liệu mỗi bảng trước.
