package ucol.a1599116.tuckbox.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.OrderHandler;
import ucol.a1599116.tuckbox.ui.adapter.OrderReviewAdapter;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.LoginHelper;
import ucol.a1599116.tuckbox.util.ModalHelper;
import ucol.a1599116.tuckbox.util.dialog.Callback;
import ucol.a1599116.tuckbox.util.dialog.DialogButton;

/**
 * Order review activity
 * Displays all current orders the user has selected
 */
public class ReviewOrdersActivity extends AbstractBaseActivity {

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_orders);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_review_orders);
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    public void onStart() {
        super.onStart();

        ListView lvOrders = (ListView) findViewById(R.id.lvOrders);

        final OrderReviewAdapter orderReviewAdapter = new OrderReviewAdapter(OrderHandler.INSTANCE.getOrders(), getApplicationContext());

        //Set up swipe to delete for meals
        final SwipeActionAdapter swipeAdapter = new SwipeActionAdapter(orderReviewAdapter);
        swipeAdapter.setListView(lvOrders);
        swipeAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.item_order_swipe_left);

        swipeAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            /**
             * If the item should have any swipe actions
             * @param position     The index of the item
             * @param direction    The direction of the swipe action
             * @return Whether the swipe action is valid
             */
            @Override
            public boolean hasActions(int position, SwipeDirection direction) {
                return direction.isLeft();
            }

            /**
             * If the item should be removed from the list view when the swipe is successful
             * @param position     The index of the item
             * @param direction    The direction of the swipe action
             * @return Whether the item should be removed from the list view
             */
            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction) {
                return direction.isLeft();
            }

            /**
             * The items OnSwipe action event
             * @param position     The indices of the swiped items
             * @param direction    The direction the items were swiped
             */
            @Override
            public void onSwipe(int[] position, SwipeDirection[] direction) {
                for (int i = 0; i < position.length; i++) {
                    SwipeDirection dir = direction[i];
                    int pos = position[i];
                    if (!dir.isLeft())
                        continue;

                    Order order = orderReviewAdapter.getItem(pos);
                    OrderHandler.INSTANCE.removeOrder(order);
                }
                ReviewOrdersActivity.this.onStart();

                if (OrderHandler.INSTANCE.getOrders().isEmpty()) {
                    ReviewOrdersActivity.this.finish();
                    ActivityHelper.launchActivity(ReviewOrdersActivity.this, OrdersActivity.class);
                }
            }
        });

        lvOrders.setAdapter(swipeAdapter);
    }

    /**
     * Activity create options menu event
     *
     * @param menu The item option menu
     * @return If the options menu was created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkmark, menu);
        return true;
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
            case R.id.check_mark:
                final boolean network = hasNetwork();
                if (!network) {
                    ModalHelper.makeMessage(ReviewOrdersActivity.this, R.string.prompt_save_orders_locally,
                            new DialogButton<>(R.string.ok, DialogButton.Type.POSITIVE, new Callback<DialogInterface>() {
                                @Override
                                public void response(DialogInterface value) {
                                    value.dismiss();
                                    ReviewOrdersActivity.this.finish();
                                    ActivityHelper.launchActivity(ReviewOrdersActivity.this, OrdersActivity.class);
                                }
                            })).show();
                } else {
                    final String cardNumber = LoginHelper.fbUser.getCardNumber();
                    final String cardCVV = LoginHelper.fbUser.getCardCVV();
                    final String cardExpiryMonth = LoginHelper.fbUser.getCardExpiryMonth();
                    final String cardExpiryYear = LoginHelper.fbUser.getCardExpiryYear();
                    final String addressStreet = LoginHelper.fbUser.getAddressStreet();
                    final String addressRegion = LoginHelper.fbUser.getAddressRegion();
                    final String deliveryDate = LoginHelper.fbUser.getDeliveryDate();
                    final String deliveryTime = LoginHelper.fbUser.getDeliveryTime();

                    ModalHelper.makeMessage(this, String.format("" +
                                    "%s:\n\n" +
                                    "%s:\n" +
                                    "%s: %s\n" +
                                    "%s: %s\n" +
                                    "%s: %s/%s\n\n" +
                                    "%s: %s, %s\n\n" +
                                    "%s:\n" +
                                    "%s at %s",
                            getString(R.string.review_delivery_details),
                            getString(R.string.review_title_payment),
                            getString(R.string.review_title_card), cardNumber,
                            getString(R.string.review_title_card_cvv), cardCVV,
                            getString(R.string.review_title_card_expiry), cardExpiryMonth, cardExpiryYear,
                            getString(R.string.review_title_address), addressStreet, addressRegion,
                            getString(R.string.review_title_date_time), deliveryDate, deliveryTime),
                            new DialogButton<>(R.string.ok, DialogButton.Type.POSITIVE, new Callback<DialogInterface>() {
                                /**
                                 * The callback response
                                 *
                                 * @param dialogInterface The result value of the evaluation
                                 */
                                @Override
                                public void response(DialogInterface dialogInterface) {
                                    dialogInterface.dismiss();
                                    ModalHelper.makeMessage(ReviewOrdersActivity.this, R.string.order_success_message,
                                            new DialogButton<>(R.string.ok, DialogButton.Type.NEUTRAL, new Callback<DialogInterface>() {
                                                /**
                                                 * The callback response
                                                 * @param dialogInterface The result value of the evaluation
                                                 */
                                                @Override
                                                public void response(final DialogInterface dialogInterface) {
                                                    OrderHandler.INSTANCE.setOrderDeliveryDetails(cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, deliveryDate, deliveryTime);
                                                    OrderHandler.INSTANCE.makeHistoryFromOrders();

                                                    dialogInterface.dismiss();
                                                    ReviewOrdersActivity.this.finish();
                                                    ActivityHelper.launchActivity(ReviewOrdersActivity.this, HistoryActivity.class);
                                                }
                                            })).show();
                                }
                            }), new DialogButton<>(R.string.cancel, DialogButton.Type.NEGATIVE, new Callback<DialogInterface>() {
                                /**
                                 * The callback response
                                 *
                                 * @param dialogInterface The result value of the evaluation
                                 */
                                @Override
                                public void response(DialogInterface dialogInterface) {
                                    dialogInterface.dismiss();
                                }
                            })).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
