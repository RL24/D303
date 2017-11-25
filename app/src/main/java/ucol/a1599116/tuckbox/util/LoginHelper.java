package ucol.a1599116.tuckbox.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ucol.a1599116.tuckbox.firebase.FBUser;
import ucol.a1599116.tuckbox.order.OrderHandler;

/**
 * Login helper utility
 * Used for handling sign in / sign out
 */
public class LoginHelper {

    public static FBUser fbUser;

    //The firebase user account
    private static FirebaseUser firebaseAccount;

    /**
     * Check if the user has already authenticated themselves
     *
     * @return Whether the user has already logged in (with Firebase or Google)
     */
    public static boolean isAuthenticated() {
        return firebaseAccount != null;
    }

    /**
     * Get the users authenticated unique ID
     *
     * @return The authenticated users unique ID
     */
    public static String getUID() {
        return firebaseAccount.getUid();
    }

    /**
     * Authenticate using Firebase
     *
     * @param _firebaseAccount The authenticated Firebase account
     */
    public static void authenticateFirebase(FirebaseUser _firebaseAccount) {
        firebaseAccount = _firebaseAccount;
    }

    /**
     * Sign out and remove all links to authentication
     */
    public static void signout() {
        FirebaseAuth.getInstance().signOut();
        OrderHandler.INSTANCE.logout();
        firebaseAccount = null;
        fbUser = null;
    }
}
