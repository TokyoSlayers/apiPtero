package net.TokyoSlayer.ProxyPtero.DataBase.Redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.TokyoSlayer.ProxyPtero.Player.Account;
import net.TokyoSlayer.ProxyPtero.Server.Server;

import java.util.ArrayList;
import java.util.List;

public class RedisData {

    private final RedisConnection redis;
    private final Gson gson;

    public RedisData(RedisConnection redis){
        this.redis = redis;
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public Account getAccount(String uuid){
        String json = redis.get(uuid);
        return gson.fromJson(json,Account.class);
    }

    public List<Account> getAllAccount(){
        List<Account> accounts = new ArrayList<>();
        redis.getAllAccount().forEach(s -> accounts.add(getAccount(s)));
        return accounts;
    }

    public Server getServer(String ip){
        String json = redis.get(ip);
        return gson.fromJson(json, Server.class);
    }

    public List<Server> getAllServer(){
        List<Server> servers = new ArrayList<>();
        redis.getAllServer().forEach(s -> servers.add(getServer(s)));
        return servers;
    }

    public void setAccount(Account account){
        String json = gson.toJson(account);
        redis.set(account.getUuid().toString(),json);
    }

    public void setServer(Server account){
        String json = gson.toJson(account);
        redis.set(account.getIp(),json);
    }

    public void del(String key){
        redis.del(key);
    }

}