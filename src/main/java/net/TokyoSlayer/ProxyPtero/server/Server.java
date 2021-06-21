package net.TokyoSlayer.ProxyPtero.server;

public class Server {

    private int id;
    private Type type;
    private Stats stats;
    private final String ip;
    private final String serverName;

    public Server(final int id, Type type, Stats stats, String ip, String serverName) {
        this.id = id;
        this.type = type;
        this.stats = stats;
        this.ip = ip;
        this.serverName = serverName;
    }

    public int getId() { return id; }

    public Type getType() {
        return type;
    }

    public Stats getStats() { return stats; }

    public String getIp() { return ip; }

    public String getServerName() { return serverName; }

    public void setId(int id) { this.id = id; }

    public void setStats(Stats stats) { this.stats = stats; }
}
