package ConnectionData;

import java.sql.*;

public class CONNECTIONSQLSERVER implements ICONNECTIONDATA {

    public String UserName;
    public String PassWord;
    public String DataBaseName;
    public String ServerName;
    public String DriverClass;
    public Connection cnn = null;
    public Statement stm = null;
    public String DriverURL;
    public String LastError = null;
    private final boolean runningOnRender;

    public CONNECTIONSQLSERVER() {
        this.runningOnRender = isRenderEnvironment();
        validateRenderDatabaseEnvironment();

        this.UserName = env("DB_USER", "");
        this.PassWord = env("DB_PASSWORD", "");
        this.DataBaseName = env("DB_NAME", "QuanLyThuVien");
        this.ServerName = env("DB_HOST", "");
        this.DriverClass = env("DB_DRIVER", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

        this.DriverURL = env("DB_URL", "");
        if (this.DriverURL == null || this.DriverURL.trim().isEmpty()) {
            if (this.ServerName != null && !this.ServerName.trim().isEmpty()) {
                this.DriverURL = "jdbc:sqlserver://" + ServerName
                        + ":" + env("DB_PORT", "1433")
                        + ";databaseName=" + DataBaseName
                        + ";encrypt=" + env("DB_ENCRYPT", "false")
                        + ";trustServerCertificate=" + env("DB_TRUST_SERVER_CERTIFICATE", "true")
                        + ";integratedSecurity=false";
            } else if (LastError == null || LastError.trim().isEmpty()) {
                LastError = "Thieu cau hinh database. Hay cau hinh DB_URL, DB_USER, DB_PASSWORD.";
            }
        }
    }

    private String env(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(name);
        }
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    private String env(String name) {
        return env(name, null);
    }

    private boolean isRenderEnvironment() {
        return env("RENDER") != null
                || env("RENDER_SERVICE_ID") != null
                || env("RENDER_EXTERNAL_URL") != null;
    }

    private void validateRenderDatabaseEnvironment() {
        StringBuilder missing = new StringBuilder();
        appendMissing(missing, "DB_URL");
        appendMissing(missing, "DB_USER");
        appendMissing(missing, "DB_PASSWORD");

        if (missing.length() > 0) {
            LastError = "Thieu bien moi truong database" + (runningOnRender ? " tren Render" : "")
                    + ": " + missing + ". Hay cau hinh DB_URL, DB_USER, DB_PASSWORD.";
        }
    }

    private void appendMissing(StringBuilder missing, String name) {
        if (env(name) != null) {
            return;
        }
        if (missing.length() > 0) {
            missing.append(", ");
        }
        missing.append(name);
    }

    @Override
    public void Open() {
        try {
            if (LastError != null && !LastError.trim().isEmpty()) {
                System.out.println("LOI CAU HINH DATABASE! " + LastError);
                return;
            }
            Class.forName(DriverClass);
            cnn = DriverManager.getConnection(DriverURL, UserName, PassWord);
            stm = cnn.createStatement();
            System.out.println("KET NOI SQL SERVER THANH CONG!");
        } catch (Exception ex) {
            LastError = "Khong ket noi duoc database. Kiem tra DB_URL, DB_USER, DB_PASSWORD tren Render hoac cau hinh SQL Server local.";
            System.out.println("LOI KET NOI SQL SERVER! " + LastError);
            ex.printStackTrace();
        }
    }

    @Override
    public void Close() {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (cnn != null && !cnn.isClosed()) {
                cnn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ResultSet GetResultSetSQL(String SQL) {
        try {
            Open();
            if (stm == null) {
                throw new SQLException(LastError);
            }
            return stm.executeQuery(SQL);
        } catch (Exception ex) {
            throw new RuntimeException(databaseErrorMessage(), ex);
        }
    }

    @Override
    public ResultSet GetResultSetStoredProcedurce(String procedureName, Object[] param) {
        try {
            Open();
            if (cnn == null) {
                throw new SQLException(LastError);
            }

            StringBuilder sql = new StringBuilder("{call " + procedureName);

            if (param != null && param.length > 0) {
                sql.append("(");
                for (int i = 0; i < param.length; i++) {
                    sql.append("?");
                    if (i < param.length - 1) {
                        sql.append(",");
                    }
                }
                sql.append(")");
            }

            sql.append("}");

            CallableStatement cs = cnn.prepareCall(sql.toString());

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    cs.setObject(i + 1, param[i]);
                }
            }

            return cs.executeQuery();

        } catch (Exception ex) {
            throw new RuntimeException(databaseErrorMessage(), ex);
        }
    }

    @Override
    public int ExecuteUpdateSQL(String SQL) {
        try {
            Open();
            if (stm == null) {
                throw new SQLException(LastError);
            }
            return stm.executeUpdate(SQL);
        } catch (Exception ex) {
            throw new RuntimeException(databaseErrorMessage(), ex);
        }
    }

    @Override
    public int ExecuteStoredProcedures(String NameStoredProcedures, Object[] param) {
        try {
            Open();
            if (cnn == null) {
                throw new SQLException(LastError);
            }

            StringBuilder sql = new StringBuilder("{call " + NameStoredProcedures);

            if (param != null && param.length > 0) {
                sql.append("(");
                for (int i = 0; i < param.length; i++) {
                    sql.append("?");
                    if (i < param.length - 1) {
                        sql.append(",");
                    }
                }
                sql.append(")");
            }

            sql.append("}");

            CallableStatement cs = cnn.prepareCall(sql.toString());

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    cs.setObject(i + 1, param[i]);
                }
            }

            return cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException(databaseErrorMessage(), ex);
        }
    }

    public String databaseErrorMessage() {
        if (LastError != null && !LastError.trim().isEmpty()) {
            return LastError;
        }
        return "Loi database. Kiem tra DB_URL, DB_USER, DB_PASSWORD va trang thai SQL Server.";
    }
}
