package net.TokyoSlayer.ProxyPtero;

import net.TokyoSlayer.ProxyPtero.DataBase.Redis.RedisConnection;
import net.TokyoSlayer.ProxyPtero.DataBase.Redis.RedisData;
import net.TokyoSlayer.ProxyPtero.DataBase.Sql.SqlConnection;
import net.TokyoSlayer.ProxyPtero.DataBase.Sql.SqlManager;
import net.TokyoSlayer.ProxyPtero.utils.Files;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public final class Main extends Plugin {

    private SqlManager sql;
    private RedisConnection redis;
    private RedisData data;
    private Files files;

    @Override
    public void onEnable() {
        this.files = new Files();
        files.load(this,"config");
        this.sql = new SqlManager(new SqlConnection(files.translateRedis("sql.host"),files.translateRedis("sql.user"),files.translateRedis("sql.pass"),files.translateRedis("sql.dbname"),files.translateRedis("sql.port")));
        this.redis = new RedisConnection(files.translateRedis("redis.host"),files.translateRedis("redis.pass"),files.translateRedis("redis.port"));
        data = new RedisData(redis);
    }

    @Override
    public void onDisable() {
        this.sql.close();
        this.redis.delAll();
    }

    public SqlManager getSql() { return sql; }

    public RedisConnection getRedis() { return redis; }

    public RedisData getData() { return data; }
}
