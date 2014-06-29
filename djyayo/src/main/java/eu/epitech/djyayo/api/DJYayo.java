package eu.epitech.djyayo.api;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

import eu.epitech.djyayo.social.AbstractSocial;


/*
 * DISCLAIMER
 * This class is the UGLIEST shit I ever wrote.
 * It sucks, is not optimized AT ALL and is quite hard to maintain.
 * If you feel like it, feel free to rewrite it from scratch. If you do so, you will probably have
 * to make little changes in the entire code (especially the fragments of the DjActivity), sorry :(
 */
public class DJYayo {

    private final static String ENCODING = "UTF-8";

    private ArrayList<DJYayoListener> listeners;

    private int lastHttpCode;

    // Global info
    private String accessToken;
    private ArrayList<String> rooms;

    // Room specific info
    private DJYayoRoom room;
    private String currentRoom;

    public DJYayo() {
        listeners = new ArrayList<DJYayoListener>();
    }

    public void connect(String server, AbstractSocial social) {
        // Build the request
        StringBuilder builder = new StringBuilder();
        builder.append(server)
            .append("/login?method=")
            .append(social.getAuthMethod())
            .append("&token=")
            .append(social.getAuthToken());

        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(HashMap<String, ?> data) {
                accessToken = (String) data.get("access_token");
            }
        }).execute(builder.toString());
    }

    public void retrieveRooms(String server) {
        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(HashMap<String, ?> data) {
                rooms = new ArrayList<String>();
                for (String key : data.keySet()) {
                    HashMap<String, ?> map = (HashMap<String, ?>) data.get(key);
                    String roomName = (String) map.get("name");
                    if (roomName != null) {
                        rooms.add(roomName);
                    }
                }
                currentRoom = (rooms.isEmpty()) ? new String() : rooms.get(0);
            }
        }).execute(server + "/rooms");
    }

    public void retrieveRoomInfo(String server) {
        this.retrieveRoomInfo(server, currentRoom);
    }

    public void retrieveRoomInfo(String server, final String roomName) {
        new RetrieveRequest(new RequestListener() {
            @Override
            public void process(HashMap<String, ?> data) {
                // Initialize new room
                room = new DJYayoRoom(roomName);

                ArrayList players = (ArrayList) data.get("players");
                room.setPlayerCount(players.size());

                // Read the current track and put it in the music list
                HashMap<String, ?> track = (HashMap) data.get("currentTrack");
                if (track != null)
                    room.addMusic(readTrack(track));
            }
        }).execute(server + "/room/" + roomName);
    }

    private DJYayoRoom.Music readTrack(HashMap<String, ?> track) {
        DJYayoRoom.Music music = new DJYayoRoom.Music();

        if (track != null) {
            HashMap<String, ?> trackInfo = (HashMap) track.get("track");
            music.trackName = (String) trackInfo.get("name");
            music.trackUrl = (String) trackInfo.get("imgUrl");
            music.trackArtist = (String) // I cast so much they call me Gandalf
                    ((ArrayList<HashMap<String, ?>>) trackInfo.get("artists")).get(0).get("name");
        }
        return music;
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
    private class RetrieveRequest extends AsyncTask<String, String, HashMap<String, ?>> {
        private RequestListener listener;

        public RetrieveRequest(RequestListener listener) {
            this.listener = listener;
        }

        @Override
        public HashMap<String, ?> doInBackground(String... url) {
            try {
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<HashMap> response =
                        rest.getForEntity(url[0], HashMap.class);
                HttpStatus status = response.getStatusCode();
                lastHttpCode = status.value();
                if (lastHttpCode / 100 == 2) // HTTP status code is 2xx = success
                    return rest.getForObject(url[0], HashMap.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(HashMap<String, ?> response) {
            super.onPostExecute(response);

            if (response != null) {
                Integer httpCode = (Integer) response.get("code");
                if (httpCode == 200) {
                    HashMap<String, ?> data;
                    if (response.get("data") instanceof HashMap)
                        data = (HashMap<String, ?>) response.get("data");
                    else
                        data = convertType(response.get("data"));
                    listener.process(data);
                } else {
                    Log.i("DJYayo", "HTTP REST request failed, error code " + httpCode.toString());
                }
                lastHttpCode = httpCode;
            }
            notifyChanges();
        }

        // Some wierd-ass type-converting hacks. They work but are soooooo ugly.
        private HashMap<String, ?> convertType(Object data) {
            if (data instanceof ArrayList) {
                int it = 0;
                HashMap<String, Object> rtn = new HashMap<String, Object>();
                for (Object obj : (ArrayList<Object>) data) {
                    rtn.put(Integer.toString(it++), obj);
                }
                return rtn;
            } else {
                return null;
            }
        }
    }

    private interface RequestListener {
        void process(HashMap<String, ?> data);
    }

}
