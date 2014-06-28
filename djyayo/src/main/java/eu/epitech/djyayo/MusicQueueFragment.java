package eu.epitech.djyayo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eu.epitech.djyayo.eu.epitech.djyayo.api.AppInfo;
import eu.epitech.djyayo.eu.epitech.djyayo.api.DJYayo;
import eu.epitech.djyayo.eu.epitech.djyayo.api.DJYayoListener;


public class MusicQueueFragment extends Fragment implements DJYayoListener {

    private String currentRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicqueue, container, false);
        // ListView listView = (ListView) getActivity().findViewById(R.id.list_tracks);
        // TODO : listView.setAdapter(new DJYayoListAdapter());
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

            // Refresh the room list
            djYayo.retrieveRoomInfo(AppInfo.getInstance().getServer());

            // Print toast
            String toastText = (djYayo.getCurrentRoom().isEmpty()) ?
                    getString(R.string.no_room_toast) :
                    String.format(getString(R.string.room_toast), djYayo.getCurrentRoom());
            Toast.makeText(getActivity().getApplicationContext(), toastText,
                    Toast.LENGTH_SHORT).show();
        }

        // Update player count
        TextView textView = (TextView) getActivity().findViewById(R.id.text_player_count);
        int playerCount = (djYayo.getRoom() == null) ? 0 : djYayo.getRoom().getPlayerCount();
        String textViewText = resources.getQuantityString(R.plurals.players_musicqueue,
                playerCount, playerCount);
        textView.setText(textViewText);
    }

}
