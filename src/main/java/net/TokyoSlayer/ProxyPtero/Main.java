package net.TokyoSlayer.ProxyPtero;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import net.TokyoSlayer.ProxyPtero.database.Redis.RedisConnection;
import net.TokyoSlayer.ProxyPtero.database.Redis.RedisData;
import net.TokyoSlayer.ProxyPtero.database.Sql.SqlConnection;
import net.TokyoSlayer.ProxyPtero.database.Sql.SqlManager;
import net.TokyoSlayer.ProxyPtero.utils.Files;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private SqlManager sql;
    private RedisConnection redis;
    private RedisData data;
    private Files files;
    private PteroClient client;
    private PteroApplication app;
    private Plugin plugin;

    @Override
    public void onEnable() {
        this.files = new Files();
        files.load(plugin,"config");
        this.sql = new SqlManager(new SqlConnection(files.translate("lobby.sql.host"),files.translate("lobby.sql.user"),files.translate("lobby.sql.pass"),files.translate("lobby.sql.dbname"),files.translateInt("lobby.sql.port")));
        this.redis = new RedisConnection(files.translate("lobby.redis.host"),files.translate("lobby.redis.pass"),files.translateInt("lobby.redis.port"));
        this.app = PteroBuilder.createApplication(files.translate("lobby.ptero.url"), files.translate("lobby.ptero.token.app"));
        this.client = PteroBuilder.createClient(files.translate("lobby.ptero.url"), files.translate("lobby.ptero.token.client"));

        data = new RedisData(redis);
    }

    @Override
    public void onDisable() {
        this.sql.close();
        this.redis.delAll();
    }

    public Files getFiles() { return files; }

    public SqlManager getSql() { return sql; }

    public RedisConnection getRedis() { return redis; }

    public RedisData getData() { return data; }

    public PteroClient getClient() { return client; }

    public PteroApplication getApp() { return app; }
}
