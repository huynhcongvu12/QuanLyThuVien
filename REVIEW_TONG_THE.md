# REVIEW TONG THE - QuanLyThuVien

Ngay audit: 2026-05-22

URL online:

```text
https://quanlythuvien-zduw.onrender.com/
```

Tai khoan test:

```text
admin / 123
staff / 123
customer / 123
```

## 1. Ket qua test live truoc khi sua

- `admin / 123` dang nhap thanh cong vao `Admin | Quan ly muon sach`.
- `staff / 123` dang nhap thanh cong vao `Nhan vien | Quan ly muon sach`.
- `customer / 123` dang nhap thanh cong vao `Khach hang | Quan ly muon sach`.
- Trang `/` chay dung root path, khong can `/QuanLyThuVien`.
- `Admin.jsp`, `Staff.jsp`, `Customer.jsp` co chan role truc tiep trong JSP.
- Logout invalidate session; sau logout truy cap dashboard bi redirect ve `Login.jsp`.
- Asset chinh cua Admin tra `200`: `styles.css`, `assets/styles.css`, `assets/luxury-ui.css`, `assets/js.js`, `assets/ui-upgrade.js`, `assets/luxury-ui.js`, `logo1.jpg`, `admin-dashboard-banner.jpg`.
- `Register.jsp` mo duoc.
- URL khong ton tai tra trang 404 rieng.

## 2. Loi nghiem trong da phat hien

- Login bi SQL injection: payload `admin' OR '1'='1` co the vao duoc Admin.
- Cac servlet JSON nhu `DocGiaServlet`, `NhanVienServlet`, `PhieuMuonServlet`, `PhieuTraServlet`, `PhieuPhatServlet` co the bi goi boi customer tren ban live cu, lam lo du lieu khong dung vai tro.
- `SachServlet` co `doGet` rong trong khi JavaScript co noi goi bang GET, co nguy co nut tim kiem khong tra du lieu.
- `CONNECTIONSQLSERVER.java` con fallback credential local cu (`sa`, password, ten may), khong nen de trong code deploy.

## 3. Loi vua

- Nhieu DAO cu con noi chuoi SQL trong thao tac doi mat khau, xoa, cap nhat phieu muon/tra/phat. Login va CRUD sach da duoc sua truoc; cac DAO con lai can refactor tiep sang `PreparedStatement`.
- Mat khau nguoi dung dang luu plaintext. Chap nhan duoc voi project sinh vien demo, nhung nen nang cap hash mat khau bang BCrypt neu lam ban that.
- Mot so servlet tra message loi co the lo `e.getMessage()`; nen gom ve thong bao than thien hon neu tiep tuc hardening.
- Chua co CSRF token cho form POST.
- Staff/Admin co nhieu nut xu ly nghiep vu trong cung bang; can test du lieu that ky hon neu demo thao tac hang loat.

## 4. Loi nho/UI/UX

- UI online nhin hien dai, co sidebar, card thong ke, bang du lieu, anh nen va chatbot.
- Responsive co CSS media query va sidebar collapse, nhung chua co kiem thu bang screenshot mobile that trong moi trinh nay.
- Chatbot co rule-based theo vai tro va khong thay loi asset; can theo doi tren mobile de dam bao khong che nut quan trong.
- Mot so thong bao backend moi sua dung ASCII de tranh loi encoding trong source; giao dien chinh van hien tieng Viet dung tren browser live.

## 5. Loi font/Unicode

- JSP chinh co `pageEncoding="UTF-8"` va `<meta charset="UTF-8">`.
- Servlet login va filter da set request/response UTF-8.
- Database SQL dung SQL Server va nhieu noi dung NVARCHAR/N'...' trong script.
- PowerShell local hien thi mot so file bi mojibake do console encoding, nhung browser live hien tieng Viet dung o cac trang chinh.

## 6. Database/JDBC

- Project dung SQL Server, driver `mssql-jdbc-12.8.1.jre11.jar`.
- Online dang ket noi duoc Somee vi 3 role dang nhap va load dashboard thanh cong.
- `CONNECTIONSQLSERVER.java` da doc `DB_URL`, `DB_USER`, `DB_PASSWORD` tu environment/system properties.
- Da bo fallback hard-code user/password/may local trong connection deploy.
- Neu thieu `DB_URL`, `DB_USER`, `DB_PASSWORD`, app bao loi ro thay vi trang trang.
- Script va code van dung T-SQL (`TOP`, `GETDATE()`, `ISNULL`, `DATEDIFF`) phu hop SQL Server; khong chuyen sang MySQL/PostgreSQL.

## 7. Phan quyen

- Da them `SecurityFilter` chan tap trung:
  - Admin-only: `Admin.jsp`, quan ly nhan vien, doc gia, phieu phat, them/sua/xoa sach.
  - Staff-only: `Staff.jsp`.
  - Customer-only: `Customer.jsp`, `CustomerBorrowServlet`.
  - Admin/Staff: phieu muon, phieu tra, `BorrowActionServlet`.
  - Moi user da dang nhap: tim sach, cap nhat ho so, doi mat khau, logout.
- Dashboard JSP van giu check role rieng nhu lop bao ve thu hai.
- Filter them header no-cache de giam rui ro back browser sau logout.

## 8. Nghiep vu muon/tra/phat

- Customer co form gui yeu cau muon va code co check sach het, qua han, gioi han 5 quyen, trung sach chua tra.
- Staff/Admin co nut duyet, tu choi, giao sach, tra binh thuong, hu, mat qua `BorrowActionServlet`.
- Code co thao tac tru/cong so luong sach trong `PhieuMuon`/`PhieuTra`.
- Chua test mutation live toan bo luong muon/tra vi co the lam thay doi database demo. Can test manual co kiem soat truoc buoi nop.

## 9. Loi da sua trong dot nay

- Sua SQL injection login bang `PreparedStatement` trong `Process_Data/Login.java`.
- Sua session fixation: invalidate session cu truoc khi set account moi trong `LoginServlet`.
- Them `SecurityFilter.java` de chan role va them no-cache headers.
- Sua `SachServlet` de GET/POST deu tra JSON tim kiem sach.
- Sua `Process_Data/Sach.java` sang `PreparedStatement` cho search/add/update/delete sach.
- Sua `ThemSachServlet.java` validate rong, so am, ma sach khong hop le va tra JSON ro rang.
- Sua `CONNECTIONSQLSERVER.java` de khong con fallback credential local hard-code.
- Build lai `dist/QuanLyThuVien.war`.

## 10. File da sua

- `src/java/SecurityFilter.java`
- `src/java/LoginServlet.java`
- `src/java/Process_Data/Login.java`
- `src/java/Process_Data/Sach.java`
- `src/java/SachServlet.java`
- `src/java/ThemSachServlet.java`
- `src/java/ConnectionData/CONNECTIONSQLSERVER.java`
- `dist/QuanLyThuVien.war`
- `build/web/WEB-INF/classes/*` sau khi build
- `REVIEW_TONG_THE.md`

## 11. Kiem thu sau sua local

- Chay build NetBeans Ant:

```text
BUILD SUCCESSFUL
```

- WAR moi ton tai:

```text
dist/QuanLyThuVien.war
```

- WAR co cac class quan trong:

```text
WEB-INF/classes/SecurityFilter.class
WEB-INF/classes/Process_Data/Login.class
WEB-INF/classes/Process_Data/Sach.class
WEB-INF/classes/SachServlet.class
WEB-INF/classes/ThemSachServlet.class
WEB-INF/classes/ConnectionData/CONNECTIONSQLSERVER.class
```

Ghi chu: JDK 17 tren may local co in `AccessDeniedException` khi dong `jasper.jar`, nhung Ant tra exit code 0 va ket thuc `BUILD SUCCESSFUL`. Loi nay da xuat hien truoc do va khong chan dong goi WAR.

## 12. Loi con lai/can xu ly tiep

- Refactor tiep cac DAO con noi chuoi SQL: `Admin`, `Customer`, `Staff`, `PhieuMuon`, `PhieuTra`, `PhieuPhat`.
- Them BCrypt cho mat khau neu muon nang muc bao mat.
- Them CSRF token cho form POST.
- Test mutation nghiep vu tren DB demo rieng: muon sach, duyet, giao sach, tra sach, hu/mat, qua han va tien phat.
- Kiem tra responsive bang browser that tren mobile/tablet truoc khi nop.
- Neu giao vien yeu cau bao cao/PDF/Excel, can bo sung module export that; hien audit chua thay export file.

## 13. Diem du kien khi nop

- Neu demo login 3 role, phan quyen, CRUD sach/doc gia/nhan vien, muon/tra/phat chay on dinh: 8.0 - 8.5/10.
- Neu tiep tuc refactor SQL injection toan bo DAO, hash mat khau, test responsive va bo sung bao cao ro hon: 8.5 - 9.0/10.
- Neu khong test ky nghiep vu mutation truoc khi nop, diem co the giam do rui ro nut bam co loi voi du lieu thuc.
