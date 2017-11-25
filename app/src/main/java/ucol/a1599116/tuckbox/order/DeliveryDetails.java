package ucol.a1599116.tuckbox.order;

import java.io.Serializable;

/**
 * Payment details class
 * Used for storing delivery and payment information for each order
 */
public class DeliveryDetails implements Serializable {

    //Delivery detail storage fields
    private String cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, deliveryTime, deliveryDate;

    /**
     * Default constructor for the delivery details
     */
    public DeliveryDetails() {
    }

    /**
     * Constructor for the delivery details
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
    DeliveryDetails(String cardNumber, String cardCVV, String cardExpiryMonth, String cardExpiryYear, String addressStreet, String addressRegion, String deliveryDate, String deliveryTime) {
        setDeliveryDetails(cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, deliveryDate, deliveryTime);
    }

    /**
     * Set the delivery details
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
    private void setDeliveryDetails(String cardNumber, String cardCVV, String cardExpiryMonth, String cardExpiryYear, String addressStreet, String addressRegion, String deliveryDate, String deliveryTime) {
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardExpiryMonth = cardExpiryMonth;
        this.cardExpiryYear = cardExpiryYear;
        this.addressStreet = addressStreet;
        this.addressRegion = addressRegion;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    /**
     * Get the credit card number
     *
     * @return The credit card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Set the credit card number
     *
     * @param cardNumber The credit card number
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Get the credit card CVV
     *
     * @return The credit card CVV
     */
    public String getCardCVV() {
        return cardCVV;
    }

    /**
     * Set the credit card CVV
     *
     * @param cardCVV The credit card CVV
     */
    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    /**
     * Get the credit card expiry month
     *
     * @return The credit card expiry month
     */
    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * Set the credit card expiry month
     *
     * @param cardExpiryMonth The credit card expiry month
     */
    public void setCardExpiryMonth(String cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    /**
     * Get the credit card expiry year
     *
     * @return The credit card expiry year
     */
    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    /**
     * Set the credit card expiry year
     *
     * @param cardExpiryYear The credit card expiry year
     */
    public void setCardExpiryYear(String cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    /**
     * Get the address street
     *
     * @return The address street
     */
    public String getAddressStreet() {
        return addressStreet;
    }

    /**
     * Set the address street
     *
     * @param addressStreet The address street
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    /**
     * Get the address region
     *
     * @return The address region
     */
    public String getAddressRegion() {
        return addressRegion;
    }

    /**
     * Set the address region
     *
     * @param addressRegion The address region
     */
    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    /**
     * Get the delivery time
     *
     * @return The delivery time
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Set the delivery time
     *
     * @param deliveryTime The delivery time
     */
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * Get the delivery date
     *
     * @return The delivery date
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Set the delivery date
     *
     * @param deliveryDate The delivery date
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
