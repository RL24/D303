package ucol.a1599116.tuckbox.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.ModalHelper;
import ucol.a1599116.tuckbox.util.dialog.Callback;
import ucol.a1599116.tuckbox.util.dialog.DialogButton;

/**
 * Forgotten password activity
 * Handles users resetting their passwords if forgotten
 */
public class ForgottenPasswordActivity extends AbstractBaseActivity {

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
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
     * Reset password button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickResetPassword(View v) {
        //TODO: Implement password reset through Firebase in part two

        ModalHelper.makeMessage(this, R.string.reset_password_message, new DialogButton<>(R.string.ok, DialogButton.Type.POSITIVE, new Callback<DialogInterface>() {
            /**
             * The callback response
             * @param dialogInterface The result value of the evaluation
             */
            @Override
            public void response(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                ForgottenPasswordActivity.this.finish();
            }
        })).show();
    }

    /**
     * Cancel button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickCancel(View v) {
        finish();
    }
}
