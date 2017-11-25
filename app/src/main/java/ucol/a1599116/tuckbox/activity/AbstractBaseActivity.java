package ucol.a1599116.tuckbox.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashSet;
import java.util.Set;

import ucol.a1599116.tuckbox.util.LoginHelper;

/**
 * Abstract base activity
 * Provides basic features that may be required by multiple activities (e.g. progress dialogs, google auth)
 */
public abstract class AbstractBaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Activity request/response codes
    protected static final int REQ_RES_GOOGLE_SIGNIN = 1000;
    protected static final int REQ_RES_GOOGLE_PLACES = 1001;
    protected static final int REQ_RES_NEW_ORDER_MEAL = 1002;

    //Google API Client (authentication)
    protected static GoogleApiClient apiClient;

    //Progress dialog instance
    protected ProgressDialog progressDialog;

    //Toasts currently shown / to show
    private final Set<Toast> TOASTS = new HashSet<>();

    /**
     * Handle authentication connection errors for the Google API Client
     *
     * @param connectionResult The connection result data
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /**
     * Activity OnStop event
     * Overridden to clear all toasts currently showing
     */
    @Override
    public void onStop() {
        super.onStop();

        for (Toast toast : TOASTS)
            toast.cancel();

        TOASTS.clear();
    }

    /**
     * Show the progress dialog alert
     *
     * @param message The message to be shown in the progress dialog
     */
    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * Hide the progress dialog if it's currently shown
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * Sign out of all accounts and remove all connections to the user
     */
    protected void logout() {
        //Check if a user is logged in
        if (LoginHelper.isAuthenticated())
            LoginHelper.signout();
    }

    /**
     * Replacement for "Toast.makeText" to ensure each toast is added to the current activities toast set
     *
     * @param res      The string resource ID
     * @param duration The duration of the toast
     * @return The toast to show
     */
    protected Toast makeToast(int res, int duration) {
        return makeToast(Toast.makeText(this, res, duration));
    }

    /**
     * Replacement for "Toast.makeText" to ensure each toast is added to the current activities toast set
     *
     * @param res      The string to display
     * @param duration The duration of the toast
     * @return The toast to show
     */
    protected Toast makeToast(CharSequence res, int duration) {
        return makeToast(Toast.makeText(this, res, duration));
    }

    /**
     * Add the toast to the TOASTS list
     *
     * @param toast The toast to add
     * @return The created toast
     */
    private Toast makeToast(Toast toast) {
        TOASTS.add(toast);
        return toast;
    }

    /**
     * Get if the app has a network connection
     *
     * @return Whether or not there is a network connection
     */
    protected boolean hasNetwork() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan == null)
            return false;
        NetworkInfo netInf = conMan.getActiveNetworkInfo();
        return netInf != null && netInf.isConnected();
    }
}
