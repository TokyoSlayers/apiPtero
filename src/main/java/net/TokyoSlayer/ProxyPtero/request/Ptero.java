package net.TokyoSlayer.ProxyPtero.request;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.EnvironmentValue;
import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroAction;
import com.mattmalec.pterodactyl4j.application.entities.*;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import net.TokyoSlayer.ProxyPtero.Main;
import net.TokyoSlayer.ProxyPtero.database.Redis.RedisData;
import net.TokyoSlayer.ProxyPtero.server.Server;
import net.TokyoSlayer.ProxyPtero.server.Stats;
import net.TokyoSlayer.ProxyPtero.server.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Ptero {

    private final Main main;
    private final Bungee bungee;
    private final PteroApplication app;
    private final PteroClient client;
    private final RedisData data;

    private final Map<String,Integer> nbrServType = new HashMap<>();
    public final Map<String, ScheduledTask> task = new HashMap<>();

    private final List<Allocation> allocation = new ArrayList<>();
    private final List<Integer> node1 = new ArrayList<>();
    private final List<Integer> node2 = new ArrayList<>();

    private int nbr1 = 0;
    private int nbr2 = 0;
    private int r = 0;

    public Ptero(Main main){
        this.main = main;
        this.app = main.getApp();
        this.client = main.getClient();
        this.allocation.addAll(app.retrieveAllocations().execute());
        this.bungee = new Bungee(main);
        this.data = main.getData();
    }

    public void deleteAll(){
        Type.getListName().forEach(this::serverDelete);
    }

    public void serverDelete(String identifier){
        serversList().forEach(applicationServer -> {
            if(applicationServer.getName().contains(identifier)) {
                String name = applicationServer.getName();
                bungee.delete(name.replace(" ","_"));
                System.out.println(name +" viens de ce delete.");
                Allocation alloc = applicationServer.getAllocations().get().get(0);
                data.del(alloc.getIP()+":"+alloc.getPortInt());
                ClientServer clientServer = client.retrieveServerByIdentifier(applicationServer.getIdentifier()).execute();
                client.setPower(clientServer, PowerAction.KILL).execute();
                applicationServer.getController().delete(false).execute();

                List<String> t = Type.getListName().stream().filter(s -> s.contains(name.replace(" ","_"))).collect(Collectors.toList());
                removeNbrServerType(t.get(0));
            }
        });
    }

    public void servCreator(String name,String eggID){
        int i = servPerNode();
        int port = 0;
        if(i == 2){
            port = node1.get(new Random().nextInt(node1.size()));
        }else if(i == 3){
            port = node2.get(new Random().nextInt(node2.size()));
        }

        Nest nest = app.retrieveNestById("8").execute();
        Location location = app.retrieveLocationById(i).execute();
        ApplicationEgg egg = app.retrieveEggById(nest, eggID).execute();

        Map<String, EnvironmentValue<?>> map = new HashMap<>();
        map.put("SERVER_JARFILE", EnvironmentValue.ofString("server.jar"));
        map.put("MINECRAFT_VERSION", EnvironmentValue.ofString("1.16.5"));

        int nbr = nbrServType.get(name);

        PteroAction<ApplicationServer> action = app.createServer()
                .setName("API " + name+ " " +nbr)
                .setDescription("")
                .setOwner(app.retrieveUserById("3").execute())
                .setEgg(egg)
                .setLocation(location)
                .setAllocations(1L)
                .setDatabases(0L)
                .setCPU(0L)
                .setDisk(2L, DataType.GB)
                .setMemory(4L, DataType.GB)
                .setDockerImage("matthieuleboeuf/dropcraft:pterodactyl-java-16")
                .setPort(port)
                .startOnCompletion(false)
                .setEnvironment(map);

        ApplicationServer server = action.execute();
        if(i == 2){
            node1.remove(port);
        }else if(i == 3){
            node2.remove(port);
        }
        Link created = new Link(name,client,server,this,main);
        task.put(server.getIdentifier(),this.main.getPlugin().getProxy().getScheduler().schedule(main.getPlugin(), created, 0, 10,TimeUnit.SECONDS));
    }

    private int servPerNode(){

        allocation.forEach(allocation1 -> {
            if (allocation1.getAlias().contains("node1")){
                if (!allocation1.isAssigned()) {
                    node1.add(allocation1.getPortInt());
                } else {
                    nbr1++;
                }
            }
            if (allocation1.getAlias().contains("node2")){
                if (!allocation1.isAssigned()) {
                    node2.add(allocation1.getPortInt());
                } else {
                    nbr2++;
                }
            }
        });

        if(nbr1 > nbr2){
            return 2;
        }else if(nbr1 < nbr2){
            return 3;
        }else{
            return 2 + (int)(Math.random() * (3 - 1));
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public String eggId(String name){
        List<ApplicationEgg> eggs = app.retrieveEggsByNest(app.retrieveNestById("8").execute()).execute();
        List<ApplicationEgg> eggList = eggs.stream().filter(applicationEgg -> applicationEgg.getName().startsWith(name)).collect(Collectors.toList());
        if(eggList.size() == 0 || eggList.isEmpty()){
            return "null";
        }
        if(!serversList().isEmpty()){
            serversList().forEach(applicationServer -> {
                if(eggList.contains(applicationServer.getEgg().get().get())){
                    eggList.remove(applicationServer.getEgg().get().get());
                }else{
                    r = new Random().nextInt(eggList.size());
                }
            });
        }else{
            r = new Random().nextInt(eggList.size());
        }
        return eggList.get(r).getId();
    }

    public List<ApplicationServer> serversList(){
        List<ApplicationServer> servers = new ArrayList<>();
        app.retrieveServers().execute().forEach(applicationServer -> {
            if(applicationServer.getName().contains("API")){
                servers.add(applicationServer);
            }
        });
        return servers;
    }

    public void createLink(ApplicationServer server, String name){
        if(server.getAllocations().isPresent()) {
            Allocation alloc = server.getAllocations().get().get(0);
            bungee.create(server.getName(), alloc.getIP(), alloc.getPortInt());
            String ip = alloc.getIP()+ ":" +alloc.getPortInt();
            Server servers = new Server(Integer.parseInt(server.getId()), Type.getByName(name), Stats.ONLINE, ip, server.getName());
            data.setServer(servers);
        }else{
            System.out.println(ChatColor.DARK_RED + "ERROR Allocation is no present.");
        }
    }

    public int getNbrServ(String typeName){
        return nbrServType.get(typeName);
    }

    public boolean mapExist(String type){
        return nbrServType.containsKey(type);
    }

    public void removeNbrServerType(String typeName){
        if(nbrServType.get(typeName) == 1){
            this.nbrServType.remove(typeName);
        }else if(nbrServType.containsKey(typeName)){
            this.nbrServType.replace(typeName,getNbrServ(typeName)-1);
        }
    }

    public void setNbrServType(String typeName) {
        if(nbrServType.containsKey(typeName)){
            this.nbrServType.replace(typeName,getNbrServ(typeName)+1);
        }else {
            this.nbrServType.put(typeName,1);
        }
    }
}