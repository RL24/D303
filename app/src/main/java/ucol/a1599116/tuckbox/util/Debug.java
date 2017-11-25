package ucol.a1599116.tuckbox.util;

import android.util.Log;

/**
 * Temporary debug class
 * Used for handling simple debug output for development
 */
public class Debug {

    //The apps environment (debug or not (production))
    public static final boolean DEBUG = false;

    //The debug console tag
    private static final String TAG = "TUCKBOX";

    /**
     * Output a given message to the information log
     *
     * @param obj The object to output
     */
    public static void log(Object obj) {
        Log.i(TAG, obj == null ? "null" : obj.toString());
    }

}
