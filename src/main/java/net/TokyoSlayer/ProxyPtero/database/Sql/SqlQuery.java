package net.TokyoSlayer.ProxyPtero.database.Sql;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;

public class SqlQuery {
    private final Connection connection;

    public SqlQuery(Connection connection) {
        this.connection = connection;
    }

    public ResultSet executeUpdate(String query, Object[] data) throws SQLException {
        final PreparedStatement statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        for(int i = 0; i < data.length; i++) {
            bindData(statement, i + 1, data[i]);
        }

        statement.executeUpdate();

        return statement.getGeneratedKeys();
    }

    public int[] executeBatch(String query, Object[][] data) throws SQLException {
        final PreparedStatement statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int[] count;

        connection.setAutoCommit(false);

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                bindData(statement, j + 1, data[i][j]);
            }

            statement.addBatch();
        }

        count = statement.executeBatch();
        connection.commit();
        connection.setAutoCommit(true);

        return count;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return executeQuery(query, null);
    }

    public ResultSet executeQuery(String query, Object[] data) throws SQLException {
        final PreparedStatement statement = this.connection.prepareStatement(query);

        if(data != null) {
            for(int i = 0; i < data.length; i++) {
                bindData(statement, i + 1, data[i]);
            }
        }

        return statement.executeQuery();
    }

    public void executeCommit(String[] queries, Object[][] data) throws SQLException {
        final PreparedStatement[] preparedStatements = new PreparedStatement[queries.length];

        connection.setAutoCommit(false);

        for(int i = 0; i < queries.length; i++) {
            preparedStatements[i] = connection.prepareStatement(queries[i]);
        }

        for(int i = 0; i < preparedStatements.length; i++) {
            if(data != null) {
                for(int j = 0; j < data[i].length; j++) {
                    bindData(preparedStatements[i], j + 1, data[i][j]);
                }
            }
            preparedStatements[i].execute();
        }

        connection.commit();
        connection.setAutoCommit(true);
    }

    public boolean tableExists(String table) throws SQLException {
        final ResultSet tables = this.executeQuery("SHOW TABLES");
        final HashSet<String> tablesName = new HashSet<>();

        while(tables.next()) {
            tablesName.add(tables.getString(1));
        }

        return tablesName.contains(table);
    }

    private void bindData(PreparedStatement statement, int index, Object primitive) throws SQLException {
        if(primitive instanceof Integer) {
            statement.setInt(index, (Integer)primitive);
        } else if(primitive instanceof Long) {
            statement.setLong(index, (Long)primitive);
        } else if(primitive instanceof String) {
            statement.setString(index, (String)primitive);
        } else if(primitive instanceof Boolean) {
            statement.setBoolean(index, (Boolean)primitive);
        } else if(primitive instanceof Timestamp) {
            statement.setTimestamp(index, (Timestamp)primitive);
        } else if(primitive instanceof Byte) {
            statement.setByte(index, (Byte)primitive);
        } else if(primitive instanceof Date) {
            statement.setDate(index, (Date)primitive);
        } else if(primitive instanceof Double) {
            statement.setDouble(index, (Double)primitive);
        } else if(primitive instanceof Float) {
            statement.setFloat(index, (Float)primitive);
        } else if(primitive instanceof BigDecimal) {
            statement.setBigDecimal(index, (BigDecimal)primitive);
        } else if(primitive == null) {
            statement.setNull(index, Types.NULL);
        } else {
            statement.setObject(index, primitive);
        }
    }

    public String[] compactQueries(String... query) {
        return query;
    }

    public Object[] compactData(Object... o) {
        return o;
    }

    public void close() throws SQLException {
        if(this.connection != null) {
            this.connection.close();
        }
    }
}