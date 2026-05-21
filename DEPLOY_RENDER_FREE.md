# Deploy QuanLyThuVien len Render Free

## 1. Ket qua kiem tra project

- Project la Java Web Servlet/JSP dang NetBeans Ant project.
- Server cau hinh trong `nbproject/project.properties`: `j2ee.server.type=Tomcat`.
- Tomcat local trong `nbproject/private/private.properties`: `apache-tomcat-9.0.73`.
- WAR output: `dist/QuanLyThuVien.war`.
- Servlet mapping dang dung annotation `@WebServlet`; `web/WEB-INF/web.xml` chi khai bao welcome file, session timeout va error pages.
- Tai nguyen CSS/JS/anh nam trong `web/assets`, `web/image`; Dockerfile deploy WAR thanh `ROOT.war` va `context.xml` da bo hard-code `/QuanLyThuVien` de giam loi sai path khi deploy online.

## 2. Build WAR

### Cach khuyen nghi bang NetBeans

1. Mo project trong NetBeans.
2. Chon server Tomcat 9, JDK 17.
3. Chuot phai project > Clean and Build.
4. Kiem tra file WAR moi tai `dist/QuanLyThuVien.war`.

### Cach build bang Ant cua NetBeans tren may hien tai

```powershell
& 'C:\Program Files\Apache NetBeans\extide\ant\bin\ant.bat' "-Dlibs.CopyLibs.classpath=C:\Program Files\Apache NetBeans\java\ant\extra\org-netbeans-modules-java-j2seproject-copylibstask.jar" "-Dplatforms.JDK_17.home=C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot" clean dist
```

Neu may khac, doi 2 duong dan `ant.bat` va `jdk-17...` theo may do.

## 3. Dockerfile

Project da co `Dockerfile`. WAR duoc copy thanh `ROOT.war`, nen khi deploy xong truy cap truc tiep:

```text
https://ten-service.onrender.com/
```

Khong can them `/QuanLyThuVien`.

## 4. Push GitHub

Neu thu muc chua co Git:

```powershell
git init
git add .
git commit -m "Prepare servlet jsp app for Render deployment"
git branch -M main
git remote add origin https://github.com/<username>/<repo>.git
git push -u origin main
```

Moi lan sua code Java/JSP truoc khi push nen chay lai build WAR, vi Dockerfile dang dung file `dist/QuanLyThuVien.war`.

## 5. Deploy Render Free

1. Vao Render Dashboard.
2. New > Web Service.
3. Ket noi GitHub repo.
4. Chon runtime/language la Docker.
5. Chon plan Free.
6. Render se build image tu `Dockerfile` va chay `CMD` trong Dockerfile.
7. Sau khi deploy xong, mo URL dang `https://<service-name>.onrender.com/`.

Render ho tro deploy Docker tu Dockerfile trong repo va mac dinh chay `CMD` cua Dockerfile. Web service can bind HTTP port; Tomcat trong image nay chay cong `8080`, Render thuong detect duoc cong nay.

## 6. Cau hinh database bang bien moi truong

Code da doi `CONNECTIONSQLSERVER.java` de doc bien moi truong, van giu fallback local cu de chay trong NetBeans.

Bien moi truong can nhap tren Render > Service > Environment:

```text
DB_URL=jdbc:sqlserver://<host>:1433;databaseName=QuanLyThuVien;encrypt=true;trustServerCertificate=true;integratedSecurity=false
DB_USER=<username>
DB_PASSWORD=<password>
```

3 bien tren la bo khuyen nghi cho deploy Render. Neu khong nhap `DB_URL`, app se fallback sang cau hinh local cu va se khong ket noi duoc tren Render. Khi login/register gap loi ket noi, man hinh se hien thong bao kiem tra `DB_URL`, `DB_USER`, `DB_PASSWORD`.

Co the dung dang tach bien neu khong dung `DB_URL`:

```text
DB_HOST=<host>
DB_PORT=1433
DB_NAME=QuanLyThuVien
DB_USER=<username>
DB_PASSWORD=<password>
DB_ENCRYPT=true
DB_TRUST_SERVER_CERTIFICATE=true
```

Neu deploy demo ma chua co database online, web van len giao dien dang nhap nhung dang nhap/chuc nang du lieu se loi ket noi DB.

## 7. Database

Project hien dang phu thuoc SQL Server/T-SQL:

- Driver `mssql-jdbc-12.8.1.jre11.jar`.
- Cu phap `TOP`, `GETDATE()`, `ISNULL`, `DATEDIFF`, `N'...'`.
- Nhieu stored procedure trong file `SQL_CHUAN_DAY_DU_YEU_CAU_THU_VIEN.sql`.

Theo yeu cau hien tai, khong chuyen database sang MySQL/PostgreSQL. De demo nhanh va it rui ro nhat:

1. Giu SQL Server, dung SQL Server online/cloud neu co goi phu hop, roi cau hinh `DB_URL`, `DB_USER`, `DB_PASSWORD`.
2. Voi bai nop can chay on dinh, phuong an thuc te nhat la dung mot SQL Server co public host tam thoi, hoac demo local database neu khong tim duoc SQL Server free on dinh.
3. Chi migrate sang PostgreSQL/MySQL khi co yeu cau rieng, vi can sua schema, stored procedures va SQL trong Java.

## 8. Neu chuyen sang PostgreSQL/MySQL

Can lam toi thieu:

- Them driver PostgreSQL hoac MySQL vao `web/WEB-INF/lib`.
- Tao lop connection moi hoac doi `CONNECTIONSQLSERVER`.
- Convert script:
  - `IDENTITY(1,1)` sang `SERIAL`/`GENERATED ... AS IDENTITY` hoac `AUTO_INCREMENT`.
  - `NVARCHAR` sang `VARCHAR`/`TEXT`.
  - `GETDATE()` sang `CURRENT_TIMESTAMP`/`CURRENT_DATE`.
  - `ISNULL()` sang `COALESCE()`.
  - `TOP 1` sang `LIMIT 1`.
  - Stored procedure T-SQL sang function/procedure tuong ung.
- Sua cac cau SQL hard-code trong `src/java/Process_Data`.
- Test lai dang nhap, dang ky, CRUD sach, phieu muon, phieu tra, phieu phat.

## 9. Checklist truoc khi nop/demo

1. Chay Clean and Build de tao `dist/QuanLyThuVien.war`.
2. Commit ca `Dockerfile`, `.dockerignore`, `DEPLOY_RENDER_FREE.md`, `dist/QuanLyThuVien.war`.
3. Push GitHub.
4. Tao Render Web Service bang Docker, Free plan.
5. Them bien moi truong database: `DB_URL`, `DB_USER`, `DB_PASSWORD`.
6. Deploy va mo `/`.
7. Test `/Login.jsp`, `/Register.jsp`, dang nhap Admin/Staff/Customer, CSS/JS/anh va cac thao tac DB.

## 10. Nguon tham khao

- Render Docker docs: https://render.com/docs/docker
- Render Web Services/port binding: https://render.com/docs/web-services
- Render deploy flow: https://render.com/docs/deploys
- Render Free: https://render.com/free
- Neon Postgres Free: https://neon.com/docs/introduction/pro-plan
- Aiven MySQL Free: https://aiven.io/docs/products/mysql/concepts/mysql-free-tier
