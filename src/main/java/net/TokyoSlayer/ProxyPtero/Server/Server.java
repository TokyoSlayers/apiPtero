package net.TokyoSlayer.ProxyPtero.Server;

public class Server {

    private int id;
    private Stats stats;
    private final String ip;
    private final String serverName;

    public Server(final int id, Stats stats, String ip, String serverName) {
        this.id = id;
        this.stats = stats;
        this.ip = ip;
        this.serverName = serverName;
    }

    public int getId() { return id; }

    public Stats getStats() { return stats; }

    public String getIp() { return ip; }

    public String getServerName() { return serverName; }

    public void setId(int id) { this.id = id; }

    public void setStats(Stats stats) { this.stats = stats; }
}
