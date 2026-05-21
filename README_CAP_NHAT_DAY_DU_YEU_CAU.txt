BẢN CẬP NHẬT ĐẦY ĐỦ YÊU CẦU NGHIỆP VỤ QUẢN LÝ THƯ VIỆN

1. CUSTOMER
- Tìm kiếm sách.
- Gửi yêu cầu mượn sách.
- Chặn mượn khi sách hết.
- Chặn mượn trùng sách đang chờ duyệt/đang mượn.
- Chặn mượn khi có sách quá hạn.
- Giới hạn tối đa 5 quyển đang mượn/chờ duyệt.
- Yêu cầu mượn của khách được tạo trạng thái Pending, không trừ sách ngay.

2. STAFF
- Xem danh sách phiếu mượn kèm trạng thái.
- Duyệt yêu cầu mượn Pending -> Approved.
- Từ chối yêu cầu Pending -> Rejected.
- Xác nhận giao sách Approved -> Borrowed và lúc này mới trừ số lượng sách.
- Xác nhận trả sách Borrowed/Overdue -> Returned và cộng lại số lượng sách.
- Ghi nhận sách hư hỏng -> Damaged, không cộng lại số lượng.
- Ghi nhận sách mất -> Lost, không cộng lại số lượng.

3. ADMIN
- Xem thống kê.
- Quản lý sách, độc giả, nhân viên, phiếu mượn, phiếu trả, phiếu phạt.
- Bảng phiếu mượn bổ sung trạng thái và nút xử lý giống Staff.

4. DATABASE
- Bổ sung trạng thái phiếu mượn: Pending, Approved, Rejected, Borrowed, Returned, Overdue, Lost, Damaged.
- Bổ sung các cột nghiệp vụ: RequestDate, ApprovedDate, BorrowDate, ReturnDate, StaffId, LyDoTuChoi, GhiChu, FineAmount.
- Có file SQL mới: SQL_CHUAN_DAY_DU_YEU_CAU_THU_VIEN.sql.
- Dữ liệu mẫu tối thiểu 5 dòng cho mỗi bảng chính, đồng thời có nhiều trạng thái phiếu mượn để test.

5. FILE ĐÃ SỬA CHÍNH
- src/java/CustomerBorrowServlet.java
- src/java/BorrowActionServlet.java
- src/java/Process_Data/PhieuMuon.java
- src/java/Process_Data/Admin.java
- src/java/model/PhieuMuon.java
- web/Customer.jsp
- web/Staff.jsp
- web/Admin.jsp
- web/assets/js.js
- SQL_CHUAN_DAY_DU_YEU_CAU_THU_VIEN.sql

6. CÁCH CHẠY
- Import project vào NetBeans.
- Chạy file SQL_CHUAN_DAY_DU_YEU_CAU_THU_VIEN.sql trên SQL Server.
- Kiểm tra chuỗi kết nối trong ConnectionData/CONNECTIONSQLSERVER.java.
- Clean and Build project.
- Chạy Tomcat.

TÀI KHOẢN TEST
- admin / 123
- staff / 123
- staff2 / 123
- customer / 123
- docgia2 / 123
