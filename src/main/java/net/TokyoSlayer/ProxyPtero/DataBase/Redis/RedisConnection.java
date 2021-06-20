package net.TokyoSlayer.ProxyPtero.DataBase.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.SafeEncoder;

import java.util.*;
import java.util.stream.Collectors;

public class RedisConnection {

    private final JedisPool jedisPool;
    private final Jedis jedis;

    public RedisConnection(String host, String pass, String port) {
        JedisPoolConfig config = new JedisPoolConfig();
        this.jedisPool = new JedisPool(config, host, Integer.parseInt(port), 300, pass);
        this.jedis = this.getJedis();
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public JedisPool getJedisPool(){return this.jedisPool; }

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
        getJedis().del(SafeEncoder.encode(key));
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
        return this.jedisPool.getResource().pipelined();
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