package net.TokyoSlayer.ProxyPtero.request;

import net.TokyoSlayer.ProxyPtero.Main;
import net.TokyoSlayer.ProxyPtero.server.Type;

import java.util.*;

public class ServerCreator {

    private final Ptero ptero;
    private final Main main;
    private final List<String> listType = Type.getListName();

    public ServerCreator(Main main){
        this.main = main;
        this.ptero = new Ptero(main);
    }

    public void createAll(){
        List<String> names = new ArrayList<>(getEggs().values());
        for (int i = 0; i < 5; i++) {
            String name = names.get(i);
            create(name);
        }
    }

    public void deleteAll(){
        ptero.deleteAll();
    }

    public void create(String type){
        String egg = getEggs().get(type);

        if(!ptero.mapExist(type)) {
            ptero.servCreator(type, egg);
        }else {
            if (ptero.getNbrServ(type) < main.getFiles().translateInt("lobby.ptero.maxnbrserv")) {
                ptero.servCreator(type, egg);
            } else {
                System.out.println("max server for type" + type);
            }
        }
        ptero.setNbrServType(type);
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
                System.out.println(s + " à été trouvé!");
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
