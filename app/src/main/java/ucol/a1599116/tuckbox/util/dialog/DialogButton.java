package ucol.a1599116.tuckbox.util.dialog;

/**
 * Dialog button handler class
 * Used for handling dialog button interactions programmatically
 *
 * @param <T> The type of callback response value
 */
public class DialogButton<T> {

    /**
     * Dialog button types
     */
    public enum Type {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    //Button label resource ID
    private int res;

    //Button type
    private Type type;

    //Interaction callback
    private Callback<T> callback;

    /**
     * Constructor for dialog buttons
     *
     * @param res      The label resource ID
     * @param type     The type of the button
     * @param callback The interaction callback
     */
    public DialogButton(int res, Type type, Callback<T> callback) {
        this.res = res;
        this.type = type;
        this.callback = callback;
    }

    /**
     * Get the label resource ID
     *
     * @return The ID of the label resource
     */
    public int getRes() {
        return res;
    }

    /**
     * Get the button type
     *
     * @return The type of the button
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the interaction callback
     *
     * @return The interaction callback
     */
    public Callback<T> getCallback() {
        return callback;
    }

}
