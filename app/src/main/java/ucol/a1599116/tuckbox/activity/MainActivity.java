package ucol.a1599116.tuckbox.activity;

import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.ActivityHelper;

/**
 * Main loading activity
 * Handles pre-loading features (e.g. Google API Client)
 */
public class MainActivity extends AbstractBaseActivity {

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //Check if the Google API Client has not been created
        if (apiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("mytopic");
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();

        ActivityHelper.launchActivity(this, LoginActivity.class);
    }
}
