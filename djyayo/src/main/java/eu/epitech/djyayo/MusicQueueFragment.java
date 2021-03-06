package eu.epitech.djyayo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eu.epitech.djyayo.api.AppInfo;
import eu.epitech.djyayo.api.DJYayo;
import eu.epitech.djyayo.api.DJYayoListAdapter;
import eu.epitech.djyayo.api.DJYayoListener;


public class MusicQueueFragment extends Fragment implements DJYayoListener {

    private String currentRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicqueue, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_tracks);
        listView.setAdapter(new DJYayoListAdapter());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppInfo.getInstance().getDJYayo().addDJYayoListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppInfo.getInstance().getDJYayo().removeDJYayoListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppInfo info = AppInfo.getInstance();

        if (item.getItemId() == R.id.action_change_room) {
            // Build the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.choose_room);

            // Put the rooms in it and setup the click listener
            ArrayList<String> roomList = info.getDJYayo().getRooms();
            String[] roomArray = new String[roomList.size()];
            roomArray = roomList.toArray(roomArray);
            builder.setItems(roomArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DJYayo djYayo = AppInfo.getInstance().getDJYayo();
                    djYayo.selectRoom(which);
                }
            });

            // Show the dialog!
            builder.create().show();
        }

        if (item.getItemId() == R.id.action_disconnect) {
            info.getSocial().logout();
            info.setDJYayo(new DJYayo());
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(DJYayo djYayo) {
        Resources resources = getResources();

        // Checks if room changed
        String djRoom = djYayo.getCurrentRoom();
        if (djRoom != null && (currentRoom == null || !currentRoom.equals(djRoom))) {
            currentRoom = djRoom;

            String toastText;
            if (currentRoom.isEmpty()) {
                toastText = getString(R.string.no_room_toast);
                currentRoom = null;
            } else {
                toastText = getString(R.string.room_toast, currentRoom);
                djYayo.retrieveRoomInfo(AppInfo.getInstance().getServer());
            }
            Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_SHORT)
                    .show();
        }

        // Update player count
        TextView textView = (TextView) getActivity().findViewById(R.id.text_player_count);
        int playerCount = (djYayo.getRoom() == null) ? 0 : djYayo.getRoom().getPlayerCount();
        String textViewText = resources.getQuantityString(R.plurals.players_musicqueue,
                playerCount, playerCount);
        textView.setText(textViewText);
        textView.setTextColor(getResources().getColor(
                (playerCount == 0) ? android.R.color.holo_red_light :
                        android.R.color.primary_text_dark));

        // Update list
        ListView listView = (ListView) getView().findViewById(R.id.list_tracks);
        DJYayoListAdapter adapter = (DJYayoListAdapter) listView.getAdapter();
        adapter.setRoom(djYayo.getRoom());
        adapter.notifyDataSetChanged();
    }

}
