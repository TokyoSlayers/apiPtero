package net.TokyoSlayer.ProxyPtero.database.Sql;

public class SqlManager {

    private final SqlAccess access;

    public SqlManager(SqlConnection sqlConnection){
        this.access = new SqlAccess(sqlConnection);
    }

    public SqlAccess getAccess(){
        return this.access;
    }

    public void init(){
        this.access.initPool();
    }

    public void close(){
        this.access.closePool();
    }
}
