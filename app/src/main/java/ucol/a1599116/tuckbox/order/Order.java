package ucol.a1599116.tuckbox.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ucol.a1599116.tuckbox.order.meal.Meal;
import ucol.a1599116.tuckbox.util.ColorHelper;

/**
 * Serializable order class
 * Used for storing order information and for passing between activities using extras
 */
public class Order implements Serializable {

    //The unique identifier for the order
    private String uid;

    //The order name
    private String name;

    //The color associated with the list view entry
    private int color;

    //The list of meals for this order
    private List<Meal> meals = new ArrayList<>();

    //The time the order was created (used for list view sorting by date created)
    private long timeCreated = 0;

    //The delivery and payment details for this order
    private DeliveryDetails deliveryDetails;

    /**
     * Default constructor for the order
     */
    public Order() {
    }

    /**
     * Constructor for the order
     *
     * @param name The name of the order
     */
    public Order(String name) {
        this.name = name;
        resetUid();
        color = ColorHelper.generateSaturatedColor();
        timeCreated = System.currentTimeMillis();
    }

    /**
     * Set the order unique identifier
     *
     * @param uid The unique identifier of the order
     * @return The current order
     */
    public Order setUid(String uid) {
        this.uid = uid;
        return this;
    }

    /**
     * Set the order name
     *
     * @param name The name of the order
     * @return The current order
     */
    public Order setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the delivery details
     *
     * @param deliveryDetails The delivery details for the order
     * @return The current order
     */
    public Order setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
        return this;
    }

    /**
     * Set the order meals
     *
     * @param meals The meals for the order
     * @return The current order
     */
    public Order setMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

    /**
     * Get the unique identifier for the order
     *
     * @return The orders unique identifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Get the order name
     *
     * @return The name of the order
     */
    public String getName() {
        return name;
    }

    /**
     * Get the color associated with the order in the list view
     *
     * @return The associated order color
     */
    public int getColor() {
        return color;
    }

    /**
     * Get the list of meals for this order
     *
     * @return The current orders list of meals
     */
    public List<Meal> getMeals() {
        return meals;
    }

    /**
     * Get the time the order was created
     *
     * @return The time the order was created
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * Get the orders delivery details
     *
     * @return The delivery details of the order
     */
    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    /**
     * Reset the orders unique identifier
     *
     * @return The current order
     */
    public Order resetUid() {
        uid = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Copy all content from another order
     *
     * @param order The order to copy content from
     * @return The current order
     */
    public Order copyFrom(Order order) {
        this.uid = order.getUid();
        this.name = order.getName();
        this.color = order.getColor();
        this.timeCreated = order.getTimeCreated();
        this.deliveryDetails = order.getDeliveryDetails();
        meals.clear();
        meals.addAll(order.getMeals());
        return this;
    }

}
