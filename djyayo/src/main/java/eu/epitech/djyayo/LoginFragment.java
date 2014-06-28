package eu.epitech.djyayo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import eu.epitech.djyayo.eu.epitech.djyayo.api.AppInfo;
import eu.epitech.djyayo.eu.epitech.djyayo.social.FacebookSocial;


public class LoginFragment extends Fragment {

    private final static String TAG = "LoginFragment";

    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this.getActivity(), new StatusCallback());
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_facebook);
        loginButton.setFragment(this);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Manually calls onSessionStateChange if the session already exist
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        uiHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception e) {
        if (session.isOpened()) {
            if (AppInfo.getInstance().getServer().isEmpty()) {
                session.closeAndClearTokenInformation();
            } else {
                AppInfo.getInstance().setSocial(new FacebookSocial(session));
                Intent intent = new Intent(this.getActivity(), DjActivity.class);
                startActivity(intent);
            }
        }
    }

    private class StatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception e) {
            onSessionStateChange(session, state, e);
        }
    }

}
