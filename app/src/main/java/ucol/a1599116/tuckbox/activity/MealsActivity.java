package ucol.a1599116.tuckbox.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.meal.Meal;
import ucol.a1599116.tuckbox.ui.adapter.MealTypeAdapter;
import ucol.a1599116.tuckbox.util.StringHelper;

/**
 * Meal list activity
 * Displays all current meals for a selected/new order
 */
public class MealsActivity extends AbstractBaseActivity {

    //List view to display meal types added to the meal
    private ListView lvMealTypes;

    //Meal type view adapter
    private MealTypeAdapter mealTypeAdapter;

    //The current order unique identifier
    private Order order;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.title_meal_select);
        }

        //Get the order passed from the previous intent as a serializable extra
        order = (Order) getIntent().getSerializableExtra(StringHelper.ORDER);
    }

    /**
     * Activity OnStart event
     * Called when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();

        lvMealTypes = (ListView) findViewById(R.id.lvMealTypes);

        final List<Meal.Type> mealTypes = Arrays.asList(Meal.Type.values());

        mealTypeAdapter = new MealTypeAdapter(mealTypes, getApplicationContext());

        lvMealTypes.setAdapter(mealTypeAdapter);
        lvMealTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Item OnClick event
             * @param adapterView    The adapter view for the item
             * @param view           The view the method was invoked by
             * @param i              The position of the view in the adapter
             * @param l              The row id of the item
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Meal.Type item = mealTypes.get(i);

                //Create a simple dialog to display meal type options
                AlertDialog.Builder mealOptionDialog = new AlertDialog.Builder(MealsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                mealOptionDialog.setTitle(String.format("%s %s", item.getName(), getString(R.string.meal_options_option)));

                //The meal type option list adapter
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(MealsActivity.this, android.R.layout.simple_list_item_1);
                adapter.addAll(item.getExtrasAsStrings());

                mealOptionDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    /**
                     * Dialog item OnClick event
                     * @param dialogInterface    The dialog the item belongs to
                     * @param i                  The index of the item clicked
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        order.getMeals().add(new Meal().setType(item).setOption(item.getExtras()[i].getStoredName()));

                        //Return the order details to the parent activity
                        Intent intent = new Intent();
                        intent.putExtra(StringHelper.NEW_ORDER, order);
                        setResult(REQ_RES_NEW_ORDER_MEAL, intent);
                        finish();

                        dialogInterface.dismiss();
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
     * Option item selected event
     *
     * @param item The selected item
     * @return If the item was successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
