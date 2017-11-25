package ucol.a1599116.tuckbox.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.Debug;

/**
 * Register account activity
 * Handles registering an account with Firebase authentication services
 */
public class RegisterActivity extends AbstractBaseActivity {

    //Username, email, and password input fields
    private EditText etUsername, etEmail, etPassword;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    /**
     * Option item selected event
     *
     * @param item The selected item
     * @return If the item was successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Register button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickRegister(final View v) {
        //Prevent continuing the sign in process if the username, email, or password is empty
        if (etUsername.length() == 0) {
            Snackbar.make(v, R.string.invalid_user, Snackbar.LENGTH_SHORT).show();
            return;
        } else if (etEmail.length() == 0) {
            Snackbar.make(v, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        } else if (etPassword.length() < 6) {
            Snackbar.make(v, R.string.invalid_pass, Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Get the username and password from the input fields
        //Nothing is actually done with "user" yet as it's not required,
        //but may be implemented in the future
        String user = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        //Validate the email address
        boolean emailMatch = email.matches("^[a-z0-9_.]+(@[^.])[a-z0-9.]+[a-z]{2,}$");
        if (!emailMatch) {
            Snackbar.make(v, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(getString(R.string.progress_registering));

        if (!Debug.DEBUG)
            //Create a new account for Firebase authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        /**
                         * Registration OnComplete event
                         * @param task    The task containing result data
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Snackbar.make(v, R.string.register_success_firebase, Snackbar.LENGTH_SHORT).show();
                                finish();
                            } else
                                Snackbar.make(v, R.string.register_failed_firebase, Snackbar.LENGTH_SHORT).show();
                        }
                    });
        else {
            hideProgressDialog();
            finish();
        }
    }

    /**
     * Sign in button OnClick event
     *
     * @param v The view  the method was invoked by
     */
    public void onClickSignIn(View v) {
        finish();
    }
}
