package eu.epitech.djyayo.social;

import com.facebook.Session;

public class FacebookSocial extends AbstractSocial {

    private Session session;

    public FacebookSocial(Session session) {
        this.session = session;
    }

    public void logout() {
        if (session != null && !session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    public String getAuthMethod() {
        return "facebook";
    }

    public String getAuthToken() {
        return session.getAccessToken();
    }

}
