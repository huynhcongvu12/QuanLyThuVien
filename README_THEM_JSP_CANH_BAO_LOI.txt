ĐÃ THÊM JSP CẢNH BÁO LỖI VÀ TRANG THÔNG BÁO

1. File JSP mới trong web/error/
- 400.jsp: lỗi yêu cầu không hợp lệ
- 401.jsp: chưa đăng nhập hoặc hết phiên
- 403.jsp: không có quyền truy cập
- 404.jsp: không tìm thấy trang
- 500.jsp: lỗi server/servlet/database
- borrow-error.jsp: lỗi khi mượn sách
- borrow-success.jsp: gửi yêu cầu mượn thành công
- return-error.jsp: lỗi khi trả sách
- permission-denied.jsp: cảnh báo sai quyền Admin/Staff/Customer
- locked-account.jsp: tài khoản bị khóa
- empty-data.jsp: danh sách trống
- maintenance.jsp: trang bảo trì

2. File include cảnh báo dùng chung
- web/components/alert.jsp

Có thể truyền thông báo qua request attribute hoặc query string:
- ?successMsg=Thao tác thành công
- ?error=Không thể xử lý yêu cầu
- ?warning=Vui lòng kiểm tra dữ liệu
- ?info=Thông tin hệ thống

3. File cấu hình lỗi
- web/WEB-INF/web.xml
Đã map lỗi 400, 401, 403, 404, 500 về các JSP tương ứng.

4. CSS đã thêm
- web/assets/styles.css
Có style cho alert nổi góc phải và trang lỗi đẹp hơn.

5. Đã đồng bộ cả web/ và build/web/
Khi mở NetBeans nên Clean and Build rồi Run.
