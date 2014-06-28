package eu.epitech.djyayo.eu.epitech.djyayo.api;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import eu.epitech.djyayo.R;
import eu.epitech.djyayo.eu.epitech.djyayo.api.DJYayoRoom;


public class DJYayoListAdapter implements ListAdapter {

    private DJYayoRoom room;

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true; // Why not
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (convertView == null) ? View.inflate(parent.getContext(), R.layout.list_djyayo,
                parent) : convertView;
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public DJYayoRoom getRoom() {
        return room;
    }

    public void setRoom(DJYayoRoom room) {
        this.room = room;
    }

}
