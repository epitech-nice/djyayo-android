package eu.epitech.djyayo.api;


import java.util.ArrayList;

public class DJYayoRoom {

    private String name;
    private ArrayList<Music> musicList;
    private int playerCount;

    public DJYayoRoom(String name) {
        this.name = name;
        musicList = new ArrayList<Music>();
    }

    public void addMusic(Music music) {
        musicList.add(music);
    }

    public void removeMusic(Music music) {
        musicList.remove(music);
    }

    public Music getMusic(int position) {
        return musicList.get(position);
    }

    public int getMusicCount() {
        return musicList.size();
    }

    public void clearMusic(Music music) {
        musicList.clear();
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public static class Music {
        // Vote related attributes
        public boolean voted;
        public int voteCount;

        // Adder related attributes
        public int addrId;
        public String addrName;
        public String addrUrl;

        // Track related thingies
        public String trackName;
        public String trackUrl;
        public String trackArtist;
    }

}
