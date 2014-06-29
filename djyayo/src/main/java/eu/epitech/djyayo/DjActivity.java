package eu.epitech.djyayo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import eu.epitech.djyayo.api.AppInfo;
import eu.epitech.djyayo.api.DJYayo;
import eu.epitech.djyayo.api.DJYayoListener;


public class DjActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment navigationDrawerFragment;
    private LoadingFragment loadingFragment;
    private Fragment currentFragment;

    private CharSequence lastTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        // Setup the drawer thingy
        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Reset listeners, add connect listener
        DJYayo djYayo = AppInfo.getInstance().getDJYayo();
        djYayo.clearDJYayoListeners();
        djYayo.addDJYayoListener(new DJYayoConnectListener());

        // Get DJYayo token
        this.initDJYayo();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        // Select the fragment associated to the navigation drawer position
        switch (position) {
            case 0:
                fragment = new MusicQueueFragment();
                break;
        }

        // Show that fragment if it isn't null
        if (fragment != null) {
            currentFragment = fragment;
            onSectionAttached(position);
            restoreActionBar();
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                lastTitle = getString(R.string.title_musicqueue);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(lastTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.dj, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (currentFragment.onOptionsItemSelected(item) || super.onOptionsItemSelected(item));
    }

    private void initDJYayo() {
        AppInfo info = AppInfo.getInstance();
        DJYayo djYayo = info.getDJYayo();

        if (djYayo.getAccessToken() == null) {
            // Setup loading fragment
            loadingFragment = new LoadingFragment();
            FragmentManager manager = this.getFragmentManager();
            loadingFragment.show(manager, "loading");

            // Connect and retrieve token and room list
            String server = info.getServer();
            djYayo.connect(server, info.getSocial());
            djYayo.retrieveRooms(server);
        }
    }

    public void pushErrorDialog(String error) {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error_title));
        builder.setMessage(error);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.error_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Finish the activity and log out if there's an error
                AppInfo info = AppInfo.getInstance();
                info.getSocial().logout();
                info.setDJYayo(new DJYayo());
                finish();
            }
        });

        // Create and show the dialog!
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class DJYayoConnectListener implements DJYayoListener {
        @Override
        public void onChange(DJYayo djYayo) {
            if (djYayo.getLastHttpCode() / 100 != 2) { // 2XX HTTP SUCCESS
                pushErrorDialog(getString(R.string.error_msg1));
            } else if (djYayo.getAccessToken() != null && djYayo.getCurrentRoom() != null) {
                if (loadingFragment != null) {
                    loadingFragment.dismiss();
                    loadingFragment = null;
                }
            }
        }
    }

}
