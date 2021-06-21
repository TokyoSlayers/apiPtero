package net.TokyoSlayer.ProxyPtero.database.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.SafeEncoder;

import java.util.*;
import java.util.stream.Collectors;

public class RedisConnection {

    private Jedis jedis;

    public RedisConnection(String host, String pass, int port) {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, host, port, 400, pass);
        this.jedis = jedisPool.getResource();
    }

    public void set(String key, String value) {
        this.jedis.set(key, value);
        System.out.println("create key value : "+ key);
    }

    public void setAll(Map<String,String> map){
        map.forEach(this::set);
    }

    public String get(String key) {
        System.out.println("Value is "+ this.jedis.get(key));
        return this.jedis.get(key);
    }

    public void del(String key){
        jedis.del(SafeEncoder.encode(key));
        System.out.println("delete key : "+ key);
    }

    public void delAll(){
        Set<String> keys = this.jedis.keys("'*'");
        keys.forEach(this::del);
    }

    public List<String> getAllAccount(){
        Set<String> keys = this.jedis.keys("'*'");
        List<String> stringUUID = keys.stream().filter(s -> s.length() == UUID.randomUUID().toString().length()).collect(Collectors.toList());
        List<String> accounts = new ArrayList<>();
        stringUUID.forEach(s -> accounts.add(get(s)));
        return accounts;
    }

    public List<String> getAllServer(){
        Set<String> keys = this.jedis.keys("'*'");
        List<String> stringUUID = keys.stream().filter(s -> s.length() != UUID.randomUUID().toString().length()).collect(Collectors.toList());
        List<String> servers = new ArrayList<>();
        stringUUID.forEach(s -> servers.add(get(s)));
        return servers;
    }

    public Pipeline pipelined() {
        return jedis.pipelined();
    }

    public List<Object> getValues(List<String> keys) {
        Pipeline pipeline = this.pipelined();
        keys.forEach(pipeline::get);
        return pipeline.syncAndReturnAll();
    }

    public List<Object> getAll() {
        Pipeline pipeline = this.pipelined();
        Set<String> keys = this.jedis.keys("'*'");
        keys.forEach(pipeline::get);
        return pipeline.syncAndReturnAll();
    }

}