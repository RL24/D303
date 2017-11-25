package ucol.a1599116.tuckbox.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.List;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.dialog.Callback;
import ucol.a1599116.tuckbox.util.dialog.DialogButton;

/**
 * Modal Helper Utility
 * Used for helping opening modal dialogs easily
 */
public class ModalHelper {

    /**
     * Create an alert dialog with a given message resource ID and dialog buttons
     *
     * @param context The context of the modal
     * @param res     The resource ID of the shown message
     * @param buttons The buttons to show on the alert dialog
     * @return An alert dialog created with the desired message and buttons
     */
    public static AlertDialog makeMessage(Context context, int res, final DialogButton<DialogInterface>... buttons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(res);
        return makeMessage(builder, buttons);
    }

    /**
     * Create an alert dialog with a given message and dialog buttons
     *
     * @param context The context of the modal
     * @param res     The message to be shown
     * @param buttons The buttons to show on the alert dialog
     * @return An alert dialog created with the desired message and buttons
     */
    public static AlertDialog makeMessage(Context context, CharSequence res, final DialogButton<DialogInterface>... buttons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(res);
        return makeMessage(builder, buttons);
    }

    /**
     * Create an alert dialog from a builder with given dialog buttons
     *
     * @param builder The predefined dialog builder
     * @param buttons The buttons to show on the alert dialog
     * @return An alert dialog created with the desired specifications and buttons
     */
    private static AlertDialog makeMessage(AlertDialog.Builder builder, final DialogButton<DialogInterface>... buttons) {
        for (final DialogButton<DialogInterface> button : buttons) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                /**
                 * Dialog item OnClick event
                 * @param dialogInterface    The dialog the item belongs to
                 * @param i                  The index of the item clicked
                 */
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    button.getCallback().response(dialogInterface);
                }
            };
            switch (button.getType()) {
                case POSITIVE:
                    builder.setPositiveButton(button.getRes(), listener);
                    break;
                case NEUTRAL:
                    builder.setNeutralButton(button.getRes(), listener);
                    break;
                case NEGATIVE:
                    builder.setNegativeButton(button.getRes(), listener);
                    break;
            }
        }
        return builder.create();
    }

    /**
     * Create an alert dialog with a given title and a selection list
     *
     * @param context  The context of the modal
     * @param res      The string resource id for the alert dialog title
     * @param list     The selection option list
     * @param callback The callback for when a selection or button is clicked
     * @return The alert dialog to show
     */
    public static AlertDialog makeListSelection(Context context, int res, List<String> list, final Callback<Integer> callback) {
        //Create a simple dialog to display a list of options
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setTitle(res);

        //The option list adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.addAll(list);

        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            /**
             * Dialog item OnClick event
             * @param dialogInterface    The dialog the item belongs to
             * @param i                  The index of the item clicked
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.response(i);
                dialogInterface.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            /**
             * Dialog item OnClick event
             * @param dialogInterface    The dialog the item belongs to
             * @param i                  The index of the item clicked
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.response(-1);
                dialogInterface.dismiss();
            }
        });
        return dialog.create();
    }
}
