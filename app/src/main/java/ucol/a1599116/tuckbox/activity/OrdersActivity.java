package ucol.a1599116.tuckbox.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.OrderHandler;
import ucol.a1599116.tuckbox.ui.adapter.OrderAdapter;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.StringHelper;

/**
 * Order list activity
 * Displays all current orders
 */
public class OrdersActivity extends AbstractBaseActivity {

    //Order view adapter
    private OrderAdapter orderAdapter;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_orders);
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();

        ListView lvOrders = (ListView) findViewById(R.id.lvItems);

        orderAdapter = new OrderAdapter(OrderHandler.INSTANCE.getOrders(), getApplicationContext());

        final SwipeActionAdapter swipeAdapter = new SwipeActionAdapter(orderAdapter);
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

                    Order order = orderAdapter.getItem(pos);
                    OrderHandler.INSTANCE.removeOrder(order);
                }
                OrdersActivity.this.onStart();
            }
        });

        lvOrders.setAdapter(swipeAdapter);
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Item OnClick event
             * @param adapterView    The adapter view for the item
             * @param view           The view the method was invoked by
             * @param i              The position of the view in the adapter
             * @param l              The row id of the item
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order item = OrderHandler.INSTANCE.getOrders().get(i);
                ActivityHelper.launchActivity(OrdersActivity.this, CustomizeOrderActivity.class, StringHelper.ORDER_ID, item);
            }
        });
    }

    /**
     * Activity create options menu event
     *
     * @param menu The item option menu
     * @return If the options menu was created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orders_options, menu);
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
            case R.id.payment:
                if (OrderHandler.INSTANCE.getOrders().isEmpty()) {
                    makeToast(R.string.order_failed_no_orders, Toast.LENGTH_SHORT).show();
                    break;
                }
                ActivityHelper.launchActivity(this, DeliveryActivity.class);
                break;
            case R.id.order_history:
                ActivityHelper.launchActivity(this, HistoryActivity.class);
                break;
            case R.id.sign_out:
                logout();
                ActivityHelper.launchActivity(OrdersActivity.this, LoginActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Prevent moving back to the Main launch activity
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Add order button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickFabAdd(View v) {
        ActivityHelper.launchActivity(this, CustomizeOrderActivity.class);
    }
}
