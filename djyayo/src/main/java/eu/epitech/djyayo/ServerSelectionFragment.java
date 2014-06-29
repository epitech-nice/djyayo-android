package eu.epitech.djyayo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import eu.epitech.djyayo.api.AppInfo;

public class ServerSelectionFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Create a EditText widget to be put into our dialog
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.fragment_serverselection,
                (ViewGroup) getActivity().getCurrentFocus());
        final EditText editText = (EditText) view.findViewById(R.id.edit_select_server);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selection_dialog_title)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(R.string.selection_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppInfo.getInstance().setServer(editText.getText().toString());
                        }
                    }
            );

        // Create the dialog and returns it
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
