package ucol.a1599116.tuckbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.firebase.FBUser;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.Debug;
import ucol.a1599116.tuckbox.util.LoginHelper;

/**
 * Login activity
 * Handles users logging in via Email/password or Google account authentication
 */
public class LoginActivity extends AbstractBaseActivity {

    //Login and password input fields
    private EditText etEmail, etPassword;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    public void onStart() {
        super.onStart();

        logout();
    }

    /**
     * Sign in button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickSignIn(final View v) {
        //Prevent continuing the sign in process if the username or password is empty
        if (etEmail.length() == 0 || etPassword.length() == 0) {
            Snackbar.make(v, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        } else if (etPassword.length() < 6) {
            Snackbar.make(v, R.string.invalid_pass, Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Get the email and password from the input fields
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        //Validate the email address
        boolean emailMatch = email.matches("^[a-z0-9_.]+(@[^.])[a-z0-9.]+[a-z]{2,}$");
        if (!emailMatch) {
            Snackbar.make(v, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(getString(R.string.progress_authenticating));

        if (!Debug.DEBUG)
            //Sign in using Firebase authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        /**
                         * Authentication OnComplete event
                         * @param task    The task containing result data
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                LoginHelper.authenticateFirebase(task.getResult().getUser());
                                login();
                            } else
                                Snackbar.make(v, R.string.login_failed_firebase, Snackbar.LENGTH_SHORT).show();
                        }
                    });
        else {
            hideProgressDialog();
            login();
        }
    }

    /**
     * Google sign in button OnClick event
     *
     * @param v Thew view the method was invoked by
     */
    public void onClickSignInGoogle(View v) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(intent, REQ_RES_GOOGLE_SIGNIN);
    }

    /**
     * Register button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickRegister(View v) {
        ActivityHelper.launchActivity(this, RegisterActivity.class);
    }

    /**
     * Forgot password button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickForgotPassword(View v) {
        ActivityHelper.launchActivity(this, ForgottenPasswordActivity.class);
    }

    /**
     * Event called when a launched activity for results is finished
     *
     * @param requestCode The request code for the intent
     * @param resultCode  The result code for the intent
     * @param intent      The finished intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //Check if the request/response code matches the defined code for Google authentication
        if (requestCode == REQ_RES_GOOGLE_SIGNIN) {
            //Get the sign in result from the Google authentication intent
            GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            //Check if the sign in was successful
            if (res.isSuccess()) {
                showProgressDialog(getString(R.string.progress_authenticating));
                GoogleSignInAccount acc = res.getSignInAccount();
                if (acc != null) {
                    //Pass the users Google authentication token to Firebase to create a Firebase user
                    AuthCredential cred = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(cred).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                LoginHelper.authenticateFirebase(task.getResult().getUser());
                                login();
                            } else
                                makeToast(R.string.login_failed_google, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else
                makeToast(R.string.login_failed_google, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Prevent moving back to the Main launch activity
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Load orders and order history from database prior to moving to the next activity after
     * authentication was successful
     */
    private void login() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(LoginHelper.getUID());
        if (userRef != null) {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FBUser user = dataSnapshot.getValue(FBUser.class);
                    if (user == null)
                        user = new FBUser(LoginHelper.getUID());
                    LoginHelper.fbUser = user;
                    ActivityHelper.launchActivity(LoginActivity.this, OrdersActivity.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ActivityHelper.launchActivity(LoginActivity.this, OrdersActivity.class);
                }
            });
        } else {
            LoginHelper.fbUser = new FBUser(LoginHelper.getUID());
            ActivityHelper.launchActivity(LoginActivity.this, OrdersActivity.class);
        }
    }
}

