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

    public CONNECTIONSQLSERVER() {
        this.UserName = env("DB_USER", "sa");
        this.PassWord = env("DB_PASSWORD", "123456789");
        this.DataBaseName = env("DB_NAME", "QuanLyThuVien");
        this.ServerName = env("DB_HOST", "LAPTOP-R8MA8LPR");
        this.DriverClass = env("DB_DRIVER", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

        this.DriverURL = env("DB_URL", "jdbc:sqlserver://" + ServerName
                + ":" + env("DB_PORT", "1433")
                + ";databaseName=" + DataBaseName
                + ";encrypt=" + env("DB_ENCRYPT", "false")
                + ";trustServerCertificate=" + env("DB_TRUST_SERVER_CERTIFICATE", "true")
                + ";integratedSecurity=false");
    }

    private String env(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(name);
        }
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    @Override
    public void Open() {
        try {
            LastError = null;
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
