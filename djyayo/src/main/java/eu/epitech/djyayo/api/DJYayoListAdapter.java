package eu.epitech.djyayo.api;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import eu.epitech.djyayo.R;


public class DJYayoListAdapter extends BaseAdapter {

    private DJYayoRoom room;

    @Override
    public int getCount() {
        return ((room == null) ? 0 : room.getMusicCount());
    }

    @Override
    public Object getItem(int position) {
        return ((room == null) ? 0 : room.getMusic(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        DJYayoRoom.Music track = room.getMusic(position);

        // Get (or create) the item view
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_djyayo, parent);
            view.setTag(track);
        } else {
            view = convertView;
        }
        track = (DJYayoRoom.Music) view.getTag();

        ImageView trackImage = (ImageView) view.findViewById(R.id.track_image);
        TextView trackTitle = (TextView) view.findViewById(R.id.track_title);
        TextView trackAuthor = (TextView) view.findViewById(R.id.track_author);
        trackImage.setImageURI(Uri.parse(track.trackUrl));
        trackTitle.setText(track.trackName);
        trackAuthor.setText(track.trackArtist);

        return view;
    }

    public DJYayoRoom getRoom() {
        return room;
    }

    public void setRoom(DJYayoRoom room) {
        this.room = room;
    }

}
