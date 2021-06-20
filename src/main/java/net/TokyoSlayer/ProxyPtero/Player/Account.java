package net.TokyoSlayer.ProxyPtero.Player;

import java.util.UUID;

public class Account {

    private int id;
    private final UUID uuid;
    private Float coins;
    private Float level;

    public Account(int id, UUID uuid, Float coins, Float level) {
        this.id = id;
        this.uuid = uuid;
        this.coins = coins;
        this.level = level;
    }

    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        if(!(o instanceof Account)){
            return false;
        }else {
            return ((Account)o).getId() ==this.id;
        }
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Float getCoins() {
        return coins;
    }

    public Float getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCoins(Float coins) { this.coins = coins; }

    public void setLevel(Float level) { this.level = level; }
}