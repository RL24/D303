package ucol.a1599116.tuckbox.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.meal.Meal;

/**
 * Meal Type adapter
 * Used for controlling the layout of the containing list view
 */
public class MealTypeAdapter extends ArrayAdapter<Meal.Type> {

    /**
     * View holder
     * Used for containing the components used for each list item
     */
    private static class ViewHolder {
        //The name label
        private TextView tvName;

        //The meal type image
        private ImageView ivImage;
    }

    /**
     * Constructor for the meal type adapter
     *
     * @param mealTypes The list of meal types to be displayed
     * @param context   The context of this view adapter
     */
    public MealTypeAdapter(List<Meal.Type> mealTypes, Context context) {
        super(context, R.layout.item_meal, mealTypes);
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
        Meal.Type item = getItem(pos);
        if (item == null)
            return v;

        ViewHolder holder;

        //Check if the view doesn't already exist
        if (v == null) {
            //Inflate a new layout for the view
            LayoutInflater inf = LayoutInflater.from(getContext());
            v = inf.inflate(R.layout.item_mealtype, group, false);
            v.setTag(holder = new ViewHolder());

            //Bind the view holder values to the view's components
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.ivImage = (ImageView) v.findViewById(R.id.ivImage);
        } else
            holder = (ViewHolder) v.getTag();

        //Set the content of the view's components
        holder.tvName.setText(item.getName());
        holder.ivImage.setImageDrawable(v.getResources().getDrawable(item.getImage()));

        return v;
    }

}
