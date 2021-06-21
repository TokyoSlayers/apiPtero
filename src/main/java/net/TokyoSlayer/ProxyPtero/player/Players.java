package net.TokyoSlayer.ProxyPtero.player;

import net.TokyoSlayer.ProxyPtero.database.Redis.RedisData;
import net.TokyoSlayer.ProxyPtero.database.Sql.SqlManager;
import net.TokyoSlayer.ProxyPtero.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.util.UUID;

public class Players {

    private final UUID uuid;
    private final RedisData data;
    private final SqlManager sql;

    public Players(ProxiedPlayer player, Main main){
        this.uuid = player.getUniqueId();
        this.data = main.getData();
        this.sql = main.getSql();
    }

    public void get() throws SQLException {
       Connection connection = sql.getAccess().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE uuid = ?");
        statement.setString(1,uuid.toString());
        statement.executeQuery();
        ResultSet result = statement.getResultSet();
        if(result.next()){
            int id = result.getInt("id");
            float coins = result.getFloat("coins");
            float level = result.getFloat("level");
            Account account = new Account(id,uuid,coins,level);

            if(data.getAccount(uuid.toString()) != null){
                data.del(uuid.toString());
            }
            data.setAccount(account);
            System.out.println("account for this player is existing !\n data recovery performed !");
        }else{
            System.out.println("account for this player dont exist in db \nCreating ...");
            create();
        }
        connection.close();
    }

    public void create() throws SQLException {
        Account account = new Account(0,uuid,10F,0f);

        Connection connection = sql.getAccess().getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (uuid,coins,level) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1,account.getUuid().toString());
        statement.setFloat(2,account.getCoins());
        statement.setFloat(3,account.getLevel());
        statement.executeQuery();

        final int row = statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        if(result.next()){
            if (row > 0 && result.next()) {
                final int id = result.getInt(1);
                account.setId(id);
                data.setAccount(account);
            }
        }
        connection.close();
        System.out.println("account for this player is create !");
    }

    public void modify() throws SQLException {
        Connection connection = sql.getAccess().getConnection();
        Account account = data.getAccount(uuid.toString());
        PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET coins = ?, level = ? WHERE uuid = ?");
        statement.setFloat(1,account.getCoins());
        statement.setFloat(2,account.getLevel());
        statement.executeQuery();
        if(data.getAccount(uuid.toString()) != null){
            data.del(uuid.toString());
        }
        data.setAccount(account);

        connection.close();
        System.out.println("account for this player is modify !");
    }

}
