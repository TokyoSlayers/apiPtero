package net.TokyoSlayer.ProxyPtero.server;

public enum Stats {
    BOOTING("§cDémarrage..."),
    WHITELISED("§dWhitelist"),
    ONLINE("§aOuvert"),
    PLAYING("§bEn jeu"),
    CLOSING("§6Arrêt...");

    private final String name;

    Stats(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Stats getByName(String name){
        for(Stats type:values()){
            if(type.getName().equalsIgnoreCase(name)){
                return type;
            }
        }
        return null;
    }
}

