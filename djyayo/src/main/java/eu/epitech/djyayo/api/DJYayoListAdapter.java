package eu.epitech.djyayo.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import eu.epitech.djyayo.R;
import eu.epitech.djyayo.system.ThumbnailLoader;


public class DJYayoListAdapter extends BaseAdapter {

    private DJYayoRoom room;

    @Override
    public int getCount() {
        return ((room == null) ? 0 : room.getTrackCount());
    }

    @Override
    public Object getItem(int position) {
        return ((room == null) ? 0 : room.getTrack(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        DJYayoTrack track = room.getTrack(position);

        // Get (or create) the item view
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_djyayo, parent, false);
            view.setTag(track);
        } else {
            view = convertView;
        }
        track = (DJYayoTrack) view.getTag();

        // Set track-related infos
        ImageView trackImage = (ImageView) view.findViewById(R.id.track_image);
        TextView trackTitle = (TextView) view.findViewById(R.id.track_title);
        TextView trackAuthor = (TextView) view.findViewById(R.id.track_author);
        ThumbnailLoader loader = new ThumbnailLoader(trackImage);
        loader.load(track.thumbUrl, parent.getContext().getCacheDir().getAbsolutePath() +
                "/thumb" + Integer.toString(position) + ".png");
        trackTitle.setText(track.name);
        trackAuthor.setText(track.artist);

        // Set vote-related infos
        ImageView adderImage = (ImageView) view.findViewById(R.id.track_adder_image);
        TextView adderName = (TextView) view.findViewById(R.id.track_adder_name);
        TextView trackVotes = (TextView) view.findViewById(R.id.track_votes);
        loader = new ThumbnailLoader(adderImage);
        loader.load(track.adder.thumbUrl, parent.getContext().getCacheDir().getAbsolutePath() +
            "/adder_thumb" + Integer.toString(position) + ".png");
        adderName.setText(track.adder.name);
        trackVotes.setText(Integer.toString(track.voteCount));

        return view;
    }

    public DJYayoRoom getRoom() {
        return room;
    }

    public void setRoom(DJYayoRoom room) {
        this.room = room;
    }

}
