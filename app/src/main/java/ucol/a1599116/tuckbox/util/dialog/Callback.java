package ucol.a1599116.tuckbox.util.dialog;

/**
 * Callback interface
 * Used for asynchronous callback tasks
 *
 * @param <T> Callback response value type
 */
public interface Callback<T> {

    /**
     * Called when a task has evaluated successfully
     *
     * @param value The result value of the evaluation
     */
    void response(T value);

}
