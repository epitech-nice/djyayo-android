package eu.epitech.djyayo.api;

import eu.epitech.djyayo.R;

public class DJYayoTrack {

    public final static int STATE_DEFAULT = R.drawable.state_default;
    public final static int STATE_VOTED = R.drawable.state_voted;
    public final static int STATE_CURRENT = R.drawable.state_current;

    public int state;
    public int voteCount;

    public String name;
    public String artist;
    public String thumbUrl;

    public DJYayoUser adder;

}
