package eu.epitech.djyayo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class LoadingFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog rtn = new ProgressDialog(this.getActivity());

        this.setStyle(STYLE_NORMAL, this.getTheme());
        rtn.setTitle(getString(R.string.loading_dialog_title));
        rtn.setMessage(getString(R.string.loading_dialog_msg1));
        rtn.setCancelable(false);
        return rtn;
    }

}
