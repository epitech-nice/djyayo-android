package eu.epitech.djyayo.api;

import java.util.ArrayList;


public class DJYayoRoom {

    private String name;
    private ArrayList<DJYayoTrack> trackList;
    private int playerCount;

    public DJYayoRoom(String name) {
        this.name = name;
        trackList = new ArrayList<DJYayoTrack>();
    }

    public void addTrack(DJYayoTrack track) {
        trackList.add(track);
    }

    public void removeTrack(DJYayoTrack track) {
        trackList.remove(track);
    }

    public DJYayoTrack getTrack(int position) {
        return trackList.get(position);
    }

    public int getTrackCount() {
        return trackList.size();
    }

    public void clearTracks() {
        trackList.clear();
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

}
