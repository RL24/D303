package ucol.a1599116.tuckbox.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.meal.Meal;

/**
 * Order review adapter
 * Used for controlling the layout of the containing list view
 */
public class OrderReviewAdapter extends ArrayAdapter<Order> {

    /**
     * View holder
     * Used for containing the components used for each list item
     */
    private static class ViewHolder {
        //The name, order, and icon labels
        private TextView tvName, tvOrders, tvIcon;
    }

    /**
     * Constructor for the order adapter
     *
     * @param orders  The list of orders to be displayed
     * @param context The context of this view adapter
     */
    public OrderReviewAdapter(List<Order> orders, Context context) {
        super(context, R.layout.item_order_review, orders);
    }

    /**
     * Get the view for each item in the adapter list
     *
     * @param pos   The position of the item
     * @param v     The old view to reuse if it already exists
     * @param group The parent of the view to attach to
     * @return A view corresponding to the data at the specified position
     */
    @Override
    public View getView(int pos, View v, ViewGroup group) {
        Order item = getItem(pos);
        if (item == null)
            return v;

        ViewHolder holder;

        //Check if the view doesn't already exist
        if (v == null) {
            //Inflate a new layout for the view
            LayoutInflater inf = LayoutInflater.from(getContext());
            v = inf.inflate(R.layout.item_order_review, group, false);
            v.setTag(holder = new ViewHolder());

            //Bind the view holder values to the view's components
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvOrders = (TextView) v.findViewById(R.id.tvOrders);
            holder.tvIcon = (TextView) v.findViewById(R.id.tvIcon);

            //Set the background color of the item icon
            Drawable background = holder.tvIcon.getBackground();
            if (background instanceof GradientDrawable)
                ((GradientDrawable) background).setColor(item.getColor());
        } else
            holder = (ViewHolder) v.getTag();

        //Group the meals to display "2x Green Saland Lunch: None" for all meals etc.
        List<String> meals = new ArrayList<>();
        Map<String, Integer> mealCount = new HashMap<>();

        for (Meal meal : item.getMeals()) {
            String mealRep = meal.toString();
            if (!meals.contains(mealRep))
                meals.add(mealRep);
            if (meals.contains(mealRep))
                mealCount.put(mealRep, (mealCount.containsKey(mealRep) ? mealCount.get(mealRep) : 0) + 1);
        }

        for (String meal : new ArrayList<>(meals)) {
            if (mealCount.containsKey(meal)) {
                meals.remove(meal);
                meals.add(String.format("%sx %s", mealCount.get(meal), meal));
            } else
                meals.add(meal);
        }
        Collections.sort(meals);

        //Set the content of the view's components
        holder.tvName.setText(item.getName());
        holder.tvOrders.setText(TextUtils.join("\n", meals));
        holder.tvIcon.setText(item.getName().substring(0, 1).toUpperCase());

        return v;
    }
}
