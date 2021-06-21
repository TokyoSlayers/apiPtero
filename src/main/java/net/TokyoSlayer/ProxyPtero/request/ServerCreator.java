package net.TokyoSlayer.ProxyPtero.request;

import net.TokyoSlayer.ProxyPtero.Main;
import net.TokyoSlayer.ProxyPtero.server.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCreator {

    private final Ptero ptero;
    private final Main main;
    private final List<String> listType = Type.getListName();

    public ServerCreator(Main main){
        this.main = main;
        this.ptero = new Ptero(main);
        Type.getListName().forEach(s -> ptero.setNbrServType(s,0));
    }

    public void createAll(){
        getEggs().forEach((name, nameEgg) -> {
            if(ptero.getNbrServ(name) < main.getFiles().translateInt("lobby.ptero.maxnbrserv")) {
                ptero.setNbrServType(name, ptero.getNbrServ(name) + 1);
                ptero.servCreator(name, nameEgg);
            }else{
                System.out.println("max server for type" +name);
            }
        });
    }

    public void deleteAll(){
        ptero.deleteAll();
    }

    public void create(String type){
        String egg = getEggs().get(type);
        if(ptero.getNbrServ(type) < main.getFiles().translateInt("lobby.ptero.maxnbrserv")) {
            ptero.setNbrServType(type, ptero.getNbrServ(type) + 1);
            ptero.servCreator(type, egg);
        }else{
            System.out.println("max server for type " +type);
        }
    }

    public void delete(String name){
        ptero.serverDelete(name);
    }

    public Map<String,String> getEggs(){
        Map<String,String> eggId = new HashMap<>();

        listType.forEach(s -> {
            if(ptero.eggId(s).equals("null")){
                System.out.println(s + " n'à pas été trouvé!");
            }else {
                System.out.println(s + " à été trouvé!\nCréation du serveur en cours...");
                eggId.put(s,ptero.eggId(s));
            }
        });
        return eggId;
    }

    public boolean check(){
        if(!ptero.serversList().isEmpty() || ptero.serversList() != null) {
            return true;
        } else {
            return false;
        }
    }

}
