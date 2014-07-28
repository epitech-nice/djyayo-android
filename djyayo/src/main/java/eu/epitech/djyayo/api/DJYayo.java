package eu.epitech.djyayo.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import eu.epitech.djyayo.jsonhelper.JSONHelper;
import eu.epitech.djyayo.jsonhelper.JSONValue;
import eu.epitech.djyayo.social.AbstractSocial;
import eu.epitech.djyayo.system.SysUtils;


/*
 * DISCLAIMER
 * This class is ugly. But still better than what it used to be.
 * It is not really optimized and is a bit hard to maintain.
 * If you feel like it, feel free to rewrite it from scratch. If you do so, you will probably have
 * to make little changes in the entire code (especially the fragments of the DjActivity), sorry :(
 */
public class DJYayo {

    private ArrayList<DJYayoListener> listeners;

    private int lastHttpCode;

    // Global info
    private DJYayoUser user;
    private String accessToken;
    private ArrayList<String> rooms;

    // Room specific info
    private DJYayoRoom room;
    private String currentRoom;

    public DJYayo() {
        listeners = new ArrayList<DJYayoListener>();
    }

    public void connect(final String server, AbstractSocial social) {
        // Build the login request
        String str = server + "/login?method=" + social.getAuthMethod() + "&token=" +
                social.getAuthToken();

        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(JSONHelper helper) {
                accessToken = SysUtils.safeCast(helper.getValue("access_token"),
                        String.class);

                // When the login request is processed, follow by a me request
                new RetrieveRequest(new RequestListener() {
                    @Override
                    public void process(JSONHelper helper) {
                        user = readUser(helper);
                    }
                }).execute(server + "/me?access_token=" + accessToken);
            }
        }).execute(str);
    }

    public void retrieveRooms(final String server) {
        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(JSONHelper helper) {
                rooms = new ArrayList<String>();

                ArrayList roomList = SysUtils.safeCast(helper.getEntry().getValue(),
                        ArrayList.class);
                if (roomList != null) {
                    for (int it = 0; it < roomList.size(); it++) {
                        String roomName = SysUtils.safeCast(helper.getValue(it, "name"),
                                String.class);
                        if (roomName != null)
                            rooms.add(roomName);
                    }
                }

                currentRoom = (rooms.isEmpty()) ? "" : rooms.get(0);
            }
        }).execute(server + "/rooms");
    }

    public void retrieveRoomInfo(String server) {
        this.retrieveRoomInfo(server, currentRoom);
    }

    public void retrieveRoomInfo(String server, final String roomName) {
        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(JSONHelper helper) {
                room = new DJYayoRoom(roomName);

                ArrayList players = SysUtils.safeCast(helper.getValue("players"), ArrayList.class);
                room.setPlayerCount(players.size());

                // Get current track, if any
                JSONValue value = helper.get("currentTrack");
                if (value != null) {
                    DJYayoTrack toAdd = readTrack(new JSONHelper(value));
                    toAdd.state = DJYayoTrack.STATE_CURRENT;
                    room.addTrack(toAdd);
                }

                // Fill track queue
                ArrayList queue = SysUtils.safeCast(helper.get("queue"), ArrayList.class);
                if (queue != null) {
                    int queueCount = queue.size();
                    for (int it = 0; it < queueCount; it++) {
                        room.addTrack(readTrack(new JSONHelper(helper.get("queue", it))));
                    }
                }
            }
        }).execute(server + "/room/" + roomName);
    }

    private DJYayoTrack readTrack(JSONHelper helper) {
        DJYayoTrack rtn = new DJYayoTrack();

        // Main track info
        rtn.name = SysUtils.safeCast(helper.getValue("track", "name"), String.class);
        rtn.artist = SysUtils.safeCast(helper.getValue("track", "artists", 0, "name"),
                String.class);
        rtn.thumbUrl = SysUtils.safeCast(helper.getValue("track", "imgUrl"), String.class);

        // Vote info
        rtn.voteCount = SysUtils.safeCast(helper.getValue("votes"), ArrayList.class).size();
        rtn.state = DJYayoTrack.STATE_DEFAULT;
        for (int it = 0; it < rtn.voteCount; it++) {
            if (user.id.equals(SysUtils.safeCast(
                    helper.getValue("votes", it, "id"), String.class))) {
                rtn.state = DJYayoTrack.STATE_VOTED;
            }
        }

        // Adder info
        rtn.adder = readUser(new JSONHelper(helper.get("addedBy")));

        return rtn;
    }

    private DJYayoUser readUser(JSONHelper helper) {
        DJYayoUser rtn = new DJYayoUser();
        rtn.id = SysUtils.safeCast(helper.getValue("id"), String.class);
        rtn.name = SysUtils.safeCast(helper.getValue("name"), String.class);
        rtn.thumbUrl = SysUtils.safeCast(helper.getValue("imgUrl"), String.class);
        return rtn;
    }

    public void selectRoom(int position) {
        currentRoom = rooms.get(position);
        notifyChanges(); // Manually notify changes since no request is executed
    }

    /**
     * Calling this function will notify all the DJYayoListeners.
     */
    public void notifyChanges() {
        for (DJYayoListener listener : listeners) {
            listener.onChange(this);
        }
    }

    // DJYayoListener management functions
    public void addDJYayoListener(DJYayoListener listener) {
        listeners.add(listener);
    }

    public void removeDJYayoListener(DJYayoListener listener) {
        listeners.remove(listener);
    }

    public void clearDJYayoListeners() {
        listeners.clear();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getLastHttpCode() {
        return lastHttpCode;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

    public DJYayoRoom getRoom() {
        return room;
    }

    /**
     * Used for retrieving and parsing JSON requests
     * Takes a RequestListener in constructor for processing the data when it's retrieved
     */
    private class RetrieveRequest extends AsyncTask<String, String, JSONHelper> {
        private RequestListener listener;

        public RetrieveRequest(RequestListener listener) {
            this.listener = listener;
        }

        @Override
        public JSONHelper doInBackground(String... url) {
            try {
                URL connectUrl = new URL(url[0]);
                HttpURLConnection connection = (HttpURLConnection) connectUrl.openConnection();

                lastHttpCode = connection.getResponseCode();
                if (lastHttpCode / 100 == 2) {
                    JSONHelper helper = new JSONHelper();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    helper.cacheStream(is);
                    connection.disconnect();
                    return helper;
                } else {
                    Log.i("DJYayo", "HTTP request failed, error code " + lastHttpCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONHelper response) {
            super.onPostExecute(response);

            if (response != null) {
                Integer httpCode = SysUtils.safeCast(response.getValue("code"), Double.class)
                        .intValue();
                if (httpCode / 100 == 2) {
                    listener.process(new JSONHelper(response.get("data")));
                } else {
                    Log.i("DJYayo", "HTTP REST request failed, error code " + httpCode.toString());
                }
                lastHttpCode = httpCode;
            }
            notifyChanges();
        }
    }

    private interface RequestListener {
        void process(JSONHelper helper);
    }

}
