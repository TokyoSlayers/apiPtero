package net.TokyoSlayer.ProxyPtero.request;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.UtilizationState;
import com.mattmalec.pterodactyl4j.application.entities.ApplicationServer;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import net.TokyoSlayer.ProxyPtero.Main;

public class Link implements Runnable {

    private final PteroClient client;
    private final ApplicationServer server;
    private final Ptero ptero;
    private final String name;
    private final Main main;

    public Link(String name, PteroClient client, ApplicationServer server, Ptero ptero, Main main){
        this.ptero = ptero;
        this.server = server;
        this.client = client;
        this.name = name;
        this.main = main;
    }

    @Override
    public void run() {
        if(!client.retrieveServerByIdentifier(server.getIdentifier()).execute().isInstalling()) {
            if(client.retrieveUtilization(client.retrieveServerByIdentifier(server.getIdentifier()).execute()).execute().getState() == UtilizationState.OFFLINE) {
                ClientServer clientServer = client.retrieveServerByIdentifier(server.getIdentifier()).execute();
                client.setPower(clientServer, PowerAction.START).execute();
            }else if(client.retrieveUtilization(client.retrieveServerByIdentifier(server.getIdentifier()).execute()).execute().getState() == UtilizationState.RUNNING) {
                ptero.createLink(server,name);
                main.getProxy().getScheduler().cancel(ptero.task.get(server.getIdentifier()).getId());
            }
        }
    }

}
