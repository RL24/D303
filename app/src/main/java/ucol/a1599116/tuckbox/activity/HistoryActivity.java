package ucol.a1599116.tuckbox.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.DeliveryDetails;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.OrderHandler;
import ucol.a1599116.tuckbox.ui.adapter.OrderHistoryAdapter;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.LoginHelper;
import ucol.a1599116.tuckbox.util.ModalHelper;
import ucol.a1599116.tuckbox.util.dialog.Callback;
import ucol.a1599116.tuckbox.util.dialog.DialogButton;

/**
 * History activity
 * Displays user order history
 */
public class HistoryActivity extends AbstractBaseActivity {

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.order_history);

        ListView lvOrders = (ListView) findViewById(R.id.lvOrders);

        final List<Order> orderHistory = OrderHandler.INSTANCE.getHistory();
        final OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orderHistory, getApplicationContext());

        lvOrders.setAdapter(orderHistoryAdapter);
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Item OnClick event
             * @param adapterView    The adapter view for the item
             * @param view           The view the method was invoked by
             * @param i              The position of the view in the adapter
             * @param l              The row id of the item
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ModalHelper.makeMessage(HistoryActivity.this, R.string.history_reorder_message,
                        new DialogButton<>(R.string.ok, DialogButton.Type.POSITIVE, new Callback<DialogInterface>() {
                            /**
                             * The callback response
                             * @param dialogInterface The result value of the evaluation
                             */
                            @Override
                            public void response(final DialogInterface dialogInterface) {
                                Order order = orderHistory.get(i);
                                OrderHandler.INSTANCE.addOrder(new Order(order.getName()).copyFrom(order).resetUid().setDeliveryDetails(order.getDeliveryDetails()));

                                //Check if any current orders have stored the delivery details already
                                //If so, ask which delivery address they would prefer
                                final List<String> addressList = new ArrayList<>();
                                final List<String> timeList = new ArrayList<>();
                                for (Order o : OrderHandler.INSTANCE.getOrders()) {
                                    DeliveryDetails dd = o.getDeliveryDetails();
                                    if (dd == null)
                                        continue;

                                    String addr = dd.getAddressStreet() + ", " + dd.getAddressRegion();
                                    String time = dd.getDeliveryTime() + ", " + dd.getDeliveryDate();

                                    if (!addressList.contains(addr))
                                        addressList.add(addr);
                                    if (!timeList.contains(time))
                                        timeList.add(time);
                                }

                                if (addressList.isEmpty() || timeList.isEmpty()) {
                                    makeToast(R.string.history_invalid_address_time, Toast.LENGTH_SHORT);
                                } else {
                                    ModalHelper.makeListSelection(HistoryActivity.this, R.string.history_select_address, addressList, new Callback<Integer>() {
                                        /**
                                         * The callback response
                                         *
                                         * @param addressIndex The result value of the evaluation
                                         */
                                        @Override
                                        public void response(Integer addressIndex) {
                                            if (addressIndex == -1) {
                                                dialogInterface.dismiss();
                                                return;
                                            }
                                            String[] address = addressList.get(addressIndex).split(",");
                                            LoginHelper.fbUser.setAddressStreet(address[0].trim());
                                            LoginHelper.fbUser.setAddressRegion(address[1].trim());
                                            ModalHelper.makeListSelection(HistoryActivity.this, R.string.history_select_time, timeList, new Callback<Integer>() {
                                                /**
                                                 * The callback response
                                                 *
                                                 * @param timeIndex The result value of the evaluation
                                                 */
                                                @Override
                                                public void response(Integer timeIndex) {
                                                    if (timeIndex == -1) {
                                                        dialogInterface.dismiss();
                                                        return;
                                                    }
                                                    String[] time = timeList.get(timeIndex).split(",");
                                                    LoginHelper.fbUser.setDeliveryTime(time[0].trim());
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();
                                        }
                                    }).show();
                                    return;
                                }

                                dialogInterface.dismiss();
                            }
                        }), new DialogButton<>(R.string.cancel, DialogButton.Type.NEGATIVE, new Callback<DialogInterface>() {
                            /**
                             * The callback response
                             * @param dialogInterface The result value of the evaluation
                             */
                            @Override
                            public void response(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        })).show();
            }
        });
    }

    /**
     * Ensure when the user tries to return to the previous activity from the order history, that
     * the orders activity is shown instead
     */
    @Override
    public void onBackPressed() {
        ActivityHelper.launchActivity(this, OrdersActivity.class);
    }
}
