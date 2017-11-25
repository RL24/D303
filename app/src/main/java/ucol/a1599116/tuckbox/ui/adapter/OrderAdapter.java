package ucol.a1599116.tuckbox.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.order.Order;

/**
 * Order adapter
 * Used for controlling the layout of the containing list view
 */
public class OrderAdapter extends ArrayAdapter<Order> {

    /**
     * View holder
     * Used for containing the components used for each list item
     */
    private static class ViewHolder {
        //The name and icon labels
        private TextView tvName, tvDateCreated, tvIcon;
    }

    /**
     * Constructor for the order adapter
     *
     * @param orders  The list of orders to be displayed
     * @param context The context of this view adapter
     */
    public OrderAdapter(List<Order> orders, Context context) {
        super(context, R.layout.item_order, orders);
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
            v = inf.inflate(R.layout.item_order, group, false);
            v.setTag(holder = new ViewHolder());

            //Bind the view holder values to the view's components
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvDateCreated = (TextView) v.findViewById(R.id.tvDateCreated);
            holder.tvIcon = (TextView) v.findViewById(R.id.tvIcon);

            //Set the background color of the item icon
            Drawable background = holder.tvIcon.getBackground();
            if (background instanceof GradientDrawable)
                ((GradientDrawable) background).setColor(item.getColor());
        } else
            holder = (ViewHolder) v.getTag();

        //Date formatting for the date created
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm", Locale.forLanguageTag("nz"));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("NZ"));
        calendar.setTimeInMillis(item.getTimeCreated());

        //Set the content of the view's components
        holder.tvName.setText(item.getName());
        holder.tvDateCreated.setText(String.format("Created: %s", dateFormat.format(calendar.getTime())));
        holder.tvIcon.setText(item.getName().substring(0, 1).toUpperCase());

        return v;
    }
}
