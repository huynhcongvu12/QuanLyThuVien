BẢN NÂNG CẤP UI BOOTSTRAP - QUẢN LÝ THƯ VIỆN

Đã cập nhật:
1. Thêm Bootstrap 5.3.3 CDN vào các JSP:
   - Admin.jsp
   - Staff.jsp
   - Customer.jsp
   - Login.jsp
   - Register.jsp

2. Thêm Font Awesome CDN để dùng icon khi cần mở rộng.

3. Nâng cấp giao diện trong:
   - web/assets/styles.css
   - build/web/assets/styles.css

4. Thêm file JS hỗ trợ UI:
   - web/assets/ui-upgrade.js
   - build/web/assets/ui-upgrade.js

5. Thêm file web/styles.css để tránh lỗi 404 vì một số JSP đang link styles.css ở thư mục gốc.

6. Giữ nguyên source Java, Servlet, Model, Process_Data, JDBC để không làm hỏng logic đang chạy.

Cách mở trong NetBeans:
1. Giải nén file zip.
2. Mở NetBeans > File > Open Project.
3. Chọn thư mục QuanLyThuVien.
4. Kiểm tra lại cấu hình SQL Server trong src/java/ConnectionData/CONNECTIONSQLSERVER.java.
5. Run project bằng Tomcat/GlassFish như cũ.

Lưu ý:
- Bootstrap và Font Awesome đang dùng CDN nên máy cần có internet để tải đầy đủ style/icon.
- Nếu không có internet, CSS local trong assets/styles.css vẫn làm giao diện đẹp hơn bản cũ.
