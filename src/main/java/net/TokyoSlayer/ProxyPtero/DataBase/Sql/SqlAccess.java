package net.TokyoSlayer.ProxyPtero.DataBase.Sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlAccess {

    private final SqlConnection sql;
    private HikariDataSource hikariDataSource;

    public SqlAccess(SqlConnection sql) {
        this.sql = sql;
    }

    private void setupHikariCP(){
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setJdbcUrl(sql.toURL());
        hikariConfig.setUsername(sql.getUser());
        hikariConfig.setPassword(sql.getPass());
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setIdleTimeout(35000L);
        hikariConfig.setMaxLifetime(45000L);
        hikariConfig.setLeakDetectionThreshold(0);
        hikariConfig.setConnectionTimeout(30000L);
        hikariConfig.setPoolName("API Connection request");

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initPool(){
        setupHikariCP();
    }

    public void closePool(){
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if(this.hikariDataSource == null){
            System.out.println("not connected");
            closePool();
            setupHikariCP();
        }
        return this.hikariDataSource.getConnection();
    }

}

