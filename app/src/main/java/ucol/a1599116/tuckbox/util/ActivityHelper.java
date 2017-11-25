package ucol.a1599116.tuckbox.util;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

/**
 * Activity helper utility
 * Used for helping launching activities and creating intents easily
 */
public class ActivityHelper {

    /**
     * Launch an activity from a given class with extras
     *
     * @param activity The parent activity
     * @param cls      The class of the activity to launch
     * @param extras   Any extras passed from the parent to the child intent
     */
    public static void launchActivity(Activity activity, Class<? extends Activity> cls, Object... extras) {
        activity.startActivity(createIntent(activity, cls, extras));
    }

    /**
     * Launch an activity from a given class with extras for a returning result
     *
     * @param activity    The parent activity
     * @param cls         The class of the activity to launch
     * @param requestCode The returning result request code
     * @param extras      Any extras passed from the parent to the child intent
     */
    public static void launchActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode, Object... extras) {
        activity.startActivityForResult(createIntent(activity, cls, extras), requestCode);
    }

    /**
     * Create a new intent from a given class with extras
     *
     * @param activity The parent activity
     * @param cls      The class of the intent to create
     * @param extras   Any extras passed from the parent to the child intent
     * @return An intent created to launch an activity from the given class with extras
     */
    private static Intent createIntent(Activity activity, Class<? extends Activity> cls, Object... extras) {
        Intent intent = new Intent(activity, cls);
        for (int i = 0; i < extras.length; i += 2)
            intent.putExtra(extras[i].toString(), (Serializable) extras[i + 1]);
        return intent;
    }

}
