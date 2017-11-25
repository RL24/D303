package ucol.a1599116.tuckbox.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.OrderHandler;
import ucol.a1599116.tuckbox.order.meal.Meal;
import ucol.a1599116.tuckbox.ui.adapter.MealAdapter;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.StringHelper;

/**
 * New order activity
 * Handles creating and populating a new order with meals
 */
public class CustomizeOrderActivity extends AbstractBaseActivity {

    //List view to display meals added to the order
    private ListView lvItems;

    //Meal view adapter
    private MealAdapter mealAdapter;

    //The current order, and a backup of the currently editing order in case the user reverts
    //changes made to the order
    private Order order, backup;

    //Order name input field
    private EditText etOrderName;

    //Whether or not the user is currently editing an already created order
    private boolean isEditing;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_order);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Apply a custom layout for the action bar to include a EditText input field
            actionBar.setCustomView(R.layout.actionbar_new_order);
            actionBar.setDisplayShowHomeEnabled(true);
            etOrderName = (EditText) actionBar.getCustomView().findViewById(R.id.etOrderName);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        //Check if there is an order to be editing
        if (getIntent().hasExtra(StringHelper.ORDER_ID)) {
            isEditing = true;
            order = (Order) getIntent().getSerializableExtra(StringHelper.ORDER_ID);
            etOrderName.setText(order.getName());
            backup = new Order(order.getName()).copyFrom(order);
            OrderHandler.INSTANCE.removeOrder(order);
        }
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();

        lvItems = (ListView) findViewById(R.id.lvItems);

        //Create a new order if not already editing one
        if (order == null) {
            order = new Order(getString(R.string.default_order_name));
            isEditing = false;
        }

        mealAdapter = new MealAdapter(order.getMeals(), getApplicationContext());

        //Set up swipe to delete for meals
        final SwipeActionAdapter swipeAdapter = new SwipeActionAdapter(mealAdapter);
        swipeAdapter.setListView(lvItems);
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

                    Meal meal = mealAdapter.getItem(pos);
                    order.getMeals().remove(meal);
                }
                CustomizeOrderActivity.this.onStart();
            }
        });

        lvItems.setAdapter(swipeAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Item OnClick event
             * @param adapterView    The adapter view for the item
             * @param view           The view the method was invoked by
             * @param i              The position of the view in the adapter
             * @param l              The row id of the item
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Meal item = CustomizeOrderActivity.this.order.getMeals().get(i);

                //Create a simple dialog to display meal type options
                AlertDialog.Builder mealOptionDialog = new AlertDialog.Builder(CustomizeOrderActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                mealOptionDialog.setTitle(String.format("%s %s", item.getType().getName(), getString(R.string.meal_options_option)));

                //The meal type option list adapter
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomizeOrderActivity.this, android.R.layout.simple_list_item_1);
                adapter.addAll(item.getType().getExtrasAsStrings());

                mealOptionDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    /**
                     * Dialog item OnClick event
                     * @param dialogInterface    The dialog the item belongs to
                     * @param i                  The index of the item clicked
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setOption(item.getType().getExtras()[i].getStoredName());
                        dialogInterface.dismiss();
                        CustomizeOrderActivity.this.onStart();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    /**
                     * Dialog negative button OnClick event
                     * @param dialogInterface    The dialog the button belongs to
                     * @param i                  The index of the button clicked
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
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
                if (order.getMeals().isEmpty()) {
                    Snackbar.make(findViewById(item.getItemId()), R.string.order_failed_no_meals, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                order.setName(etOrderName.getText().toString());
                OrderHandler.INSTANCE.addOrder(order);
                finish();
                break;
            case android.R.id.home:
                if (isEditing)
                    OrderHandler.INSTANCE.addOrder(backup);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Add meal button OnClick event
     *
     * @param v The view the method was invoked by
     */
    public void onClickFabAdd(View v) {
        ActivityHelper.launchActivityForResult(this, MealsActivity.class, REQ_RES_NEW_ORDER_MEAL, StringHelper.ORDER, order);
    }

    /**
     * Event called when a launched activity for results is finished
     *
     * @param requestCode The request code for the intent
     * @param resultCode  The result code for the intent
     * @param intent      The finished intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == REQ_RES_NEW_ORDER_MEAL) {
            Order order = (Order) intent.getSerializableExtra(StringHelper.NEW_ORDER);
            this.order.copyFrom(order);
        }
    }
}
