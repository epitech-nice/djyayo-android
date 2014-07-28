package eu.epitech.djyayo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import eu.epitech.djyayo.api.AppInfo;


public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        AppInfo info = AppInfo.getInstance();

        // Checks if the user has already selected a server. If not, ask him for it
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        setServer(preferences.getString("server", ""));
        if (info.getServer().isEmpty())
            askForServer();

        // Load login fragment
        Fragment loginFragment;
        if (savedInstanceState == null) {
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginFragment).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString("server", AppInfo.getInstance().getServer());
        prefEditor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_select_server) {
            askForServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void askForServer() {
        ServerSelectionFragment serverFragment = new ServerSelectionFragment();
        serverFragment.setSelectListener(new ServerSelectionFragment.SelectListener() {
            @Override
            public void onSelect(String server) {
                setServer(server);
            }
        });
        serverFragment.show(getFragmentManager(), "server_selection");
    }

    public void setServer(String server) {
        AppInfo info = AppInfo.getInstance();
        TextView serverText = (TextView) findViewById(R.id.login_text_server);

        info.setServer(server);
        if (server.isEmpty()) {
            serverText.setText(R.string.login_noserver);
        } else {
            serverText.setText(getString(R.string.login_server, server));
        }
    }
}
