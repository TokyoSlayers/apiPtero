package net.TokyoSlayer.ProxyPtero.request;

import net.TokyoSlayer.ProxyPtero.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;

public class Bungee {

    private final Main main;

    public Bungee(Main main){
        this.main = main;
    }

    public void create(String name,String ip,Integer port) {
        String nameExactrly = name.replace(' ','_');
        ServerInfo info = main.getProxy().constructServerInfo(nameExactrly, new InetSocketAddress(ip, port), name, false);
        main.getLogger().info("Adding the server...");

        main.getProxy().getServersCopy().put(info.getName(), info);
    }

    public void delete(String serverName) {
        ServerInfo info = main.getProxy().getServerInfo(serverName);
        if (info != null){
            main.getLogger().info("Removing the server...");
            TextComponent reason = new TextComponent("You were kicked because the server was removed.");
            for (ProxiedPlayer player : info.getPlayers()) {
                player.disconnect(reason);
            }
            main.getProxy().getServersCopy().remove(info.getName());
        }
    }
}