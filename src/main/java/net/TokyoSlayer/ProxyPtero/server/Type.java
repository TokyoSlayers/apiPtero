package net.TokyoSlayer.ProxyPtero.server;

import java.util.ArrayList;
import java.util.List;

public enum Type {
    LOBBY("Lobby",1),
    DROPPER("Dropper",1),
    INFECTE("Infecte",1),
    QUAKE("QuakeCraft",1),
    SPLEEF("Spleef",1),
    TNTRUN("TnTRun",2),
    MURDER("Murder",1),
    HUNGERGAME("HungerGames",1),
    BEDWARS("BedWars",1),
    TARGET("Target",1),
    UHC("Uhc",1),
    TAUPEGUN("TaupeGun",1);

    private final String name;
    private final int nombreOfMap;

    Type(String name, int nombreOfMap){
        this.name = name;
        this.nombreOfMap = nombreOfMap;
    }

    public String getName() {
        return name;
    }

    public static List<String> getListName() {
        List<String> names = new ArrayList<>();
        for (Type type: values()){
            if(!type.name.equals("Lobby")) {
                names.add(type.name);
            }
        }
        return names;
    }

    public static Type getByName(String name){

        for(Type type:values()){

            if(type.getName().equalsIgnoreCase(name)){
                return type;
            }else if(name.contains(type.getName())){
                return type;
            }
        }
        return null;
    }

}
