package eu.epitech.djyayo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import eu.epitech.djyayo.eu.epitech.djyayo.api.AppInfo;


public class LoginActivity extends FragmentActivity {

    private Fragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        AppInfo info = AppInfo.getInstance();

        // Checks if the user has already selected a server. If not, ask him for it
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        info.setServer(preferences.getString("server", ""));
        if (info.getServer().isEmpty()) {
            ServerSelectionFragment serverFragment = new ServerSelectionFragment();
            serverFragment.show(getFragmentManager(), "server_selection");
        }

        // Load login fragment
        if (savedInstanceState == null) {
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, loginFragment)
                    .commit();
        } else {
            loginFragment = (Fragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
