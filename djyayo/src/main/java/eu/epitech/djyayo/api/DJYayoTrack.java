package eu.epitech.djyayo.api;

public class DJYayoTrack {

    public final static int STATE_DEFAULT = 0;
    public final static int STATE_VOTED = 1;
    public final static int STATE_CURRENT = 2;

    public int state;
    public int voteCount;

    public String name;
    public String artist;
    public String thumbUrl;

    public DJYayoUser adder;

}
