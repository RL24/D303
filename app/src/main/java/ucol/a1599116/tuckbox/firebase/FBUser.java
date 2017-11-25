package ucol.a1599116.tuckbox.firebase;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ucol.a1599116.tuckbox.order.Order;
import ucol.a1599116.tuckbox.order.OrderHandler;

/**
 * The firebase user class
 * Stores all data related to the firebase user, including updating the firebase database with the
 * users details and order/history information
 */
public class FBUser {

    //The firebase user stored details
    protected String uid, cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, deliveryDate, deliveryTime;

    //The firebase user current orders
    protected Map<String, Order> orders = new HashMap<>();

    //The firebase user order history
    protected Map<String, Order> history = new HashMap<>();

    /**
     * Firebase User default constructor
     */
    public FBUser() {
        uid = cardNumber = cardCVV = cardExpiryMonth = cardExpiryYear = addressStreet = addressRegion = deliveryDate = deliveryTime = "";
    }

    /**
     * Firebase User alternative constructor
     *
     * @param uid The unique identifier of the firebase user
     */
    public FBUser(String uid) {
        this.uid = uid;
        cardNumber = cardCVV = cardExpiryMonth = cardExpiryYear = addressStreet = addressRegion = deliveryDate = deliveryTime = "";
        updateDatabase();
    }

    /**
     * Add an order to the firebase user and database
     *
     * @param order The order to add
     */
    public void addOrder(Order order) {
        getOrders().put(order.getUid(), order);
        updateDatabase();
    }

    /**
     * Remove an order from the firebase user and database
     *
     * @param order The order to remove
     */
    public void removeOrder(Order order) {
        getOrders().remove(order.getUid());
        updateDatabase();
    }

    /**
     * Clear orders from the firebase user and database
     */
    public void clearOrders() {
        getOrders().clear();
        updateDatabase();
    }

    /**
     * Add an order to the history of the firebase user
     *
     * @param order The order to add
     */
    public void addHistory(Order order) {
        getHistory().put(order.getUid(), order);
        updateDatabase();
    }

    /**
     * Remove an order from the history of the firebase user
     *
     * @param order The order to remove
     */
    public void removeHistory(Order order) {
        getHistory().remove(order.getUid());
        updateDatabase();
    }

    /**
     * Clear history from the firebase user
     */
    public void clearHistory() {
        getHistory().clear();
        updateDatabase();
    }

    /**
     * Update the current user entry in the firebase database
     */
    private void updateDatabase() {
        if (uid != null && !uid.isEmpty())
            FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(this);
    }

    /**
     * Get the firebase user unique identifier
     *
     * @return The firebase user unique identifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Set the firebase user unique identifier
     *
     * @param uid The firebase user unique identifier
     */
    public void setUid(String uid) {
        this.uid = uid;
        updateDatabase();
    }

    /**
     * Get the firebase user credit card number
     *
     * @return The firebase user credit card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Set the firebase user credit card number
     *
     * @param cardNumber The firebase user credit card number
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        updateDatabase();
    }

    /**
     * Get the firebase user credit card CVV
     *
     * @return The firebase user credit card CVV
     */
    public String getCardCVV() {
        return cardCVV;
    }

    /**
     * Set the firebase user credit card CVV
     *
     * @param cardCVV The firebase user credit card CVV
     */
    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
        updateDatabase();
    }

    /**
     * Get the firebase user credit card expiry month
     *
     * @return The firebase user credit card expiry month
     */
    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * Set the firebase use credit card expiry month
     *
     * @param cardExpiryMonth The firebase user credit card expiry month
     */
    public void setCardExpiryMonth(String cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
        updateDatabase();
    }

    /**
     * Get the firebase user credit card expiry year
     *
     * @return The firebase user credit card expiry year
     */
    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    /**
     * Set the firebase user credit card expiry year
     *
     * @param cardExpiryYear The firebase user credit card expiry year
     */
    public void setCardExpiryYear(String cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
        updateDatabase();
    }

    /**
     * Get the firebase user address street
     *
     * @return The firebase user address street
     */
    public String getAddressStreet() {
        return addressStreet;
    }

    /**
     * Set the firebase user address street
     *
     * @param addressStreet The firebase user address street
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
        updateDatabase();
    }

    /**
     * Get the firebase user address region
     *
     * @return The firebase user address region
     */
    public String getAddressRegion() {
        return addressRegion;
    }

    /**
     * Set the firebase user address region
     *
     * @param addressRegion The firebase user address region
     */
    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
        updateDatabase();
    }

    /**
     * Get the firebase user delivery date
     *
     * @return The firebase user delivery date
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Set the firebase user delivery date
     *
     * @param deliveryDate The firebase user delivery date
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        updateDatabase();
    }

    /**
     * Get the firebase user delivery time
     *
     * @return The firebase user delivery time
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Set the firebase user delivery time
     *
     * @param deliveryTime The firebase user delivery time
     */
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
        updateDatabase();
    }

    /**
     * Get the firebase user current orders
     *
     * @return The firebase user current orders
     */
    public Map<String, Order> getOrders() {
        return orders;
    }

    /**
     * Set the firebase user current orders
     *
     * @param orders The firebase user current orders
     */
    public void setOrders(Map<String, Order> orders) {
        this.orders = orders;
        OrderHandler.INSTANCE.setOrders(orders);
        updateDatabase();
    }

    /**
     * Get the firebase user order history
     *
     * @return The firebase user order history
     */
    public Map<String, Order> getHistory() {
        return history;
    }

    /**
     * Set the firebase use order history
     *
     * @param history The firebase user order history
     */
    public void setHistory(Map<String, Order> history) {
        this.history = history;
        OrderHandler.INSTANCE.setHistory(history);
        updateDatabase();
    }
}
