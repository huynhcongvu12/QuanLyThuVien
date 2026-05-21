# Deploy QuanLyThuVien len Render Free

Tai lieu nay ghi lai checklist sau khi project da duoc chuan bi deploy Render bang Docker va Tomcat 9.

## 1. Ket qua kiem tra

- `Dockerfile` dang copy dung file WAR: `dist/QuanLyThuVien.war`.
- WAR duoc deploy thanh `/usr/local/tomcat/webapps/ROOT.war`, nen URL chay tren Render la `https://<service-name>.onrender.com/`.
- `dist/QuanLyThuVien.war` phai ton tai truoc khi push len GitHub, vi Dockerfile copy truc tiep file nay.
- `web/META-INF/context.xml` khong hard-code `/QuanLyThuVien`; app chay duoc o root context khi WAR duoc doi thanh `ROOT.war`.
- CSS/JS/image duoc dat trong `web/assets` va `web/styles.css`; cac duong dan hien tai chay duoc khi deploy o root `/`.
- `CONNECTIONSQLSERVER.java` van dung SQL Server, khong doi sang MySQL/PostgreSQL.
- Khi chay tren Render, app yeu cau du 3 bien `DB_URL`, `DB_USER`, `DB_PASSWORD`. Neu thieu, app hien thong bao loi ro tren man hinh login/register hoac trang 500, khong crash trang trang.

## 2. Dockerfile

```dockerfile
FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY dist/QuanLyThuVien.war /usr/local/tomcat/webapps/ROOT.war

ENV CATALINA_OPTS="-Dfile.encoding=UTF-8"
EXPOSE 8080

CMD ["catalina.sh", "run"]
```

Sau khi deploy, truy cap:

```text
https://<service-name>.onrender.com/
```

Khong dung:

```text
https://<service-name>.onrender.com/QuanLyThuVien
```

## 3. Build WAR truoc khi push

Lenh build tren may hien tai:

```powershell
& 'C:\Program Files\Apache NetBeans\extide\ant\bin\ant.bat' "-Dlibs.CopyLibs.classpath=C:\Program Files\Apache NetBeans\java\ant\extra\org-netbeans-modules-java-j2seproject-copylibstask.jar" "-Dplatforms.JDK_17.home=C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot" clean dist
```

Build thanh cong khi log co:

```text
BUILD SUCCESSFUL
```

File can co sau build:

```text
dist/QuanLyThuVien.war
```

## 4. Environment Variables can nhap tren Render

Nhap dung 3 bien sau trong Render Dashboard > Web Service > Environment:

```text
DB_URL=jdbc:sqlserver://<sql-server-host>:1433;databaseName=QuanLyThuVien;encrypt=true;trustServerCertificate=true;integratedSecurity=false
DB_USER=<sql-server-username>
DB_PASSWORD=<sql-server-password>
```

Ghi chu:

- `<sql-server-host>` phai la host SQL Server public ma Render co the truy cap tu internet.
- Neu SQL Server dung port khac `1433`, sua port trong `DB_URL`.
- Khong can nhap `DB_HOST`, `DB_PORT`, `DB_NAME` tren Render neu da dung `DB_URL`.
- Project nay dang dung SQL Server va driver `mssql-jdbc`; khong chuyen database sang MySQL/PostgreSQL khi chua co yeu cau.

## 5. Cac buoc deploy Render Free

1. Push project len GitHub.
2. Vao Render Dashboard.
3. Chon New > Web Service.
4. Connect repo GitHub `QuanLyThuVien`.
5. Runtime chon Docker.
6. Plan chon Free.
7. Them Environment Variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`.
8. Bam Create Web Service.
9. Doi Render build Docker image va start Tomcat.
10. Mo URL `https://<service-name>.onrender.com/`.

## 6. Checklist test sau deploy

1. Trang `/` tu dong vao `Login.jsp`.
2. CSS/JS/logo/background hien dung.
3. Mo `Register.jsp` khong loi asset.
4. Thu login/register khi co DB online.
5. Neu thieu bien DB tren Render, man hinh phai bao ro thieu `DB_URL`, `DB_USER`, `DB_PASSWORD`.
6. Khong them `/QuanLyThuVien` vao URL sau deploy.

## 7. Push sau khi build

```powershell
git add .
git commit -m "Prepare Render deployment checklist"
git push
```
