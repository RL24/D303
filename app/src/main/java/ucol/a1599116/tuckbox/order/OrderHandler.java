package ucol.a1599116.tuckbox.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucol.a1599116.tuckbox.util.LoginHelper;

/**
 * Order handler Singleton (using enums vs single static instance of itself)
 * Used for storing all current orders and updating the firebase order database per user
 */
public enum OrderHandler {
    INSTANCE;

    //Order list per user
    private Map<String, Order> orders = new HashMap<>();

    //History order list per user
    private Map<String, Order> history = new HashMap<>();

    /**
     * Get the provided order list's values sorted by the time the order was created
     *
     * @param map The map of orders to retrieve sorted values from
     * @return A sorted order list from the provided map
     */
    private List<Order> getOrders(Map<String, Order> map) {
        List<Order> result = new ArrayList<>(map.values());
        Collections.sort(result, new Comparator<Order>() {
            /**
             * Compare two objects
             * @param a    The first object
             * @param b    The second object
             * @return The comparative result of the two objects
             */
            @Override
            public int compare(Order a, Order b) {
                return Long.valueOf(a.getTimeCreated()).compareTo(b.getTimeCreated());
            }
        });
        return result;
    }

    /**
     * Get the list of orders sorted by time created
     *
     * @return The list of orders
     */
    public List<Order> getOrders() {
        return getOrders(orders);
    }

    /**
     * Get the order history sorted by time created
     *
     * @return The order history
     */
    public List<Order> getHistory() {
        return getOrders(history);
    }

    /**
     * Set the current orders
     *
     * @param orders The current orders
     */
    public void setOrders(Map<String, Order> orders) {
        this.orders = orders;
    }

    /**
     * Set the order history
     *
     * @param history The order history
     */
    public void setHistory(Map<String, Order> history) {
        this.history = history;
    }

    /**
     * Add an order to the order map by it's UID
     *
     * @param order The order to add to the order map
     */
    public void addOrder(Order order) {
        orders.put(order.getUid(), order);
        LoginHelper.fbUser.addOrder(order);
    }

    /**
     * Remove an order from the order map by it's UID
     *
     * @param order The order to remove from the order map
     */
    public void removeOrder(Order order) {
        orders.remove(order.getUid());
        LoginHelper.fbUser.removeOrder(order);
    }

    /**
     * Clears all orders from the order map
     * Only a temporary method until database management is added
     */
    public void clearOrders() {
        orders.clear();
        LoginHelper.fbUser.clearOrders();
    }

    /**
     * Add an order to the history map by it's UID
     *
     * @param order The order to add to the history map
     */
    public void addHistory(Order order) {
        history.put(order.getUid(), order);
        LoginHelper.fbUser.addHistory(order);
    }

    /**
     * Convert the current orders to history and clear the current orders
     */
    public void makeHistoryFromOrders() {
        history.putAll(orders);
        for (Order order : history.values())
            LoginHelper.fbUser.addHistory(order);
        clearOrders();
    }

    /**
     * Set all the current orders delivery and payment details
     *
     * @param cardNumber      The credit card number
     * @param cardCVV         The credit card CVV/CVC/CCV number
     * @param cardExpiryMonth The credit card expiry month
     * @param cardExpiryYear  The credit card expiry year
     * @param addressStreet   The address street
     * @param addressRegion   The address region
     * @param deliveryDate    The date to be delivered (always "Today" unless the order was made after 10am)
     * @param deliveryTime    The time to be delivered
     */
    public void setOrderDeliveryDetails(String cardNumber, String cardCVV, String cardExpiryMonth, String cardExpiryYear, String addressStreet, String addressRegion, String deliveryDate, String deliveryTime) {
        for (Order order : orders.values())
            order.setDeliveryDetails(new DeliveryDetails(cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, deliveryDate, deliveryTime));
    }

    /**
     * Clear current orders and history during logout
     */
    public void logout() {
        orders.clear();
        history.clear();
    }

}
