package eu.epitech.djyayo.eu.epitech.djyayo.api;

import eu.epitech.djyayo.eu.epitech.djyayo.social.AbstractSocial;

public class AppInfo {

    private static AppInfo instance;

    private AbstractSocial social;
    private DJYayo djyayo;

    private String server;

    public AppInfo() {
        djyayo = new DJYayo();
    }

    public AbstractSocial getSocial() {
        return social;
    }

    public void setSocial(AbstractSocial social) {
        this.social = social;
    }

    public DJYayo getDJYayo() {
        return djyayo;
    }

    public void setDJYayo(DJYayo djyayo) {
        this.djyayo = djyayo;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static AppInfo getInstance() {
        if (instance == null)
            instance = new AppInfo();
        return instance;
    }

}
