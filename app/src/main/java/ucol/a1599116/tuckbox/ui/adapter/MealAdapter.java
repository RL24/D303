package ucol.a1599116.tuckbox.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
 * Meal adapter
 * Used for controlling the layout of the containing list view
 */
public class MealAdapter extends ArrayAdapter<Meal> {

    /**
     * View holder
     * Used for containing the components used for each list item
     */
    private static class ViewHolder {
        //The name and option labels
        private TextView tvName, tvOption;

        //The item icon
        private ImageView ivIcon;
    }

    /**
     * Constructor for the meal adapter
     *
     * @param meals   The list of meals to be displayed
     * @param context The context of this view adapter
     */
    public MealAdapter(List<Meal> meals, Context context) {
        super(context, R.layout.item_meal, meals);
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
        Meal item = getItem(pos);
        if (item == null)
            return v;

        ViewHolder holder;

        //Check if the view doesn't already exist
        if (v == null) {
            //Inflate a new layout for the view
            LayoutInflater inf = LayoutInflater.from(getContext());
            v = inf.inflate(R.layout.item_meal, group, false);
            v.setTag(holder = new ViewHolder());

            //Bind the view holder values to the view's components
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvOption = (TextView) v.findViewById(R.id.tvOption);
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);

            //Set the default background color of the item icon
            Drawable background = holder.ivIcon.getBackground();
            if (background instanceof GradientDrawable)
                ((GradientDrawable) background).setColor(item.getColor());
        } else
            holder = (ViewHolder) v.getTag();

        //Set the content of the view's components
        holder.tvName.setText(item.getType().getName());
        holder.tvOption.setText(item.getOption());
        holder.ivIcon.setImageDrawable(v.getResources().getDrawable(item.getType().getImage()));

        return v;
    }

}
