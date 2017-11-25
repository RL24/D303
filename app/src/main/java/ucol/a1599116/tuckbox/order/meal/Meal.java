package ucol.a1599116.tuckbox.order.meal;

import java.io.Serializable;
import java.util.UUID;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.ColorHelper;
import ucol.a1599116.tuckbox.util.StringHelper;

/**
 * Serializable meal class
 * Used for storing meal information per order and for passing between activities using extras
 */
public class Meal implements Serializable {

    /**
     * Meal types and included options
     */
    public enum Type {
        GREEN_SALAD_LUNCH("Green Salad Lunch", R.drawable.icon_meal_green_salad_lunch, Dressing.class, Dressing.values()),
        LAMB_KORMA("Lamb Korma", R.drawable.icon_meal_lamb_korma, Spice.class, Spice.values()),
        OPEN_CHICKEN_SANDWICH("Open Chicken Sandwich", R.drawable.icon_meal_open_chicken_sandwich, Bread.class, Bread.values()),
        BEEF_NOODLE_SALAD("Beef Noodle Salad", R.drawable.icon_meal_beef_noodle_salad, Extras.class, Extras.values());

        /**
         * Green Salad Lunch meal options
         */
        public enum Dressing implements IMealOption {
            NONE, RANCH, VINAIGRETTE;

            //String representation of the enum value
            private String name;

            /**
             * Constructor for each Dressing enum value
             */
            Dressing() {
                name = StringHelper.formatEnumToString(this);
            }

            /**
             * Get the string representation of the enum value
             *
             * @return The string representation of the current enum value
             */
            @Override
            public String toString() {
                return name;
            }

            /**
             * Get the stored name for this enum value (for database reference)
             *
             * @return The stored name for this enum value
             */
            @Override
            public String getStoredName() {
                return name();
            }
        }

        /**
         * Lamb Korma meal options
         */
        public enum Spice implements IMealOption {
            MILD, MED, HOT;

            //String representation of the enum value
            private String name;

            /**
             * Constructor for each Spice enum value
             */
            Spice() {
                name = StringHelper.formatEnumToString(this);
            }

            /**
             * Get the string representation of the enum value
             *
             * @return The string representation of the enum value
             */
            @Override
            public String toString() {
                return name;
            }

            /**
             * Get the stored name for this enum value (for database reference)
             *
             * @return The stored name for this enum value
             */
            @Override
            public String getStoredName() {
                return name();
            }
        }

        /**
         * Open Chicken Sandwich meal options
         */
        public enum Bread implements IMealOption {
            WHITE, RYE, WHOLEMEAL;

            //String representation of the enum value
            private String name;

            /**
             * Constructor for each Bread enum value
             */
            Bread() {
                name = StringHelper.formatEnumToString(this);
            }

            /**
             * Get the string representation of the enum value
             *
             * @return The string representation of the enum value
             */
            @Override
            public String toString() {
                return name;
            }

            /**
             * Get the stored name for this enum value (for database reference)
             *
             * @return The stored name for this enum value
             */
            @Override
            public String getStoredName() {
                return name();
            }
        }

        /**
         * Beef Noodle Salad meal options
         */
        public enum Extras implements IMealOption {
            NO_CHILLI_FLAKES, EXTRA_CHILLI_FLAKES;

            //String representation of the enum value
            private String name;

            /**
             * Constructor for each Extras enum value
             */
            Extras() {
                name = StringHelper.formatEnumToString(this);
            }

            /**
             * Get the string representation of the enum value
             *
             * @return The string representation of the enum value
             */
            @Override
            public String toString() {
                return name;
            }

            /**
             * Get the stored name for this enum value (for database reference)
             *
             * @return The stored name for this enum value
             */
            @Override
            public String getStoredName() {
                return name();
            }
        }

        //The string representation of the enum value
        private String name;

        //The image resource id accompanying the meal type
        private int image;

        //The extras enum
        private Class<? extends Enum> extrasClass;

        //The enum of the extra's values for the enum value
        private IMealOption[] extras;

        /**
         * The constructor for each meal type enum value
         *
         * @param name        The name of the meal type
         * @param image       The accompanying meal type image
         * @param extrasClass The class of the extras enum
         * @param extras      The meal option extras
         */
        Type(String name, int image, Class<? extends Enum> extrasClass, IMealOption[] extras) {
            this.name = name;
            this.image = image;
            this.extrasClass = extrasClass;
            this.extras = extras;
        }

        /**
         * Get the string representation of the enum value
         *
         * @return The string representation of the enum value
         */
        public String getName() {
            return name;
        }

        /**
         * Get the accompanying meal type image resource id
         *
         * @return The meal type image resource id
         */
        public int getImage() {
            return image;
        }

        /**
         * Get the meal type's extras class
         *
         * @return The meal type's extras class
         */
        public Class<? extends Enum> getExtrasClass() {
            return extrasClass;
        }

        /**
         * Get the meal type's extra's options
         *
         * @return The meal type's extra's
         */
        public IMealOption[] getExtras() {
            return extras;
        }

        /**
         * Get the meal type's extra's options as a string represented array
         *
         * @return The meal type's extra's as string representations
         */
        public String[] getExtrasAsStrings() {
            String[] res = new String[extras.length];
            for (int i = 0; i < res.length; i++)
                res[i] = extras[i].toString();
            return res;
        }
    }

    /**
     * Get the meal option from the stored name
     *
     * @param storedName The stored name of the meal option
     * @return The meal option owner of the stored name
     */
    public static IMealOption getMealTypeOption(String storedName) {
        try {
            return Type.Dressing.valueOf(storedName);
        } catch (IllegalArgumentException e) {
            try {
                return Type.Spice.valueOf(storedName);
            } catch (IllegalArgumentException e2) {
                try {
                    return Type.Bread.valueOf(storedName);
                } catch (IllegalArgumentException e3) {
                    try {
                        return Type.Extras.valueOf(storedName);
                    } catch (IllegalArgumentException e4) {
                    }
                }
            }
        }
        return null;
    }

    //The unique identifier for the meal
    private String uid;

    //The type of the meal
    private Type type;

    //The option selected for the meal
    private String option;

    //The color associated with the list view entry
    private int color;

    /**
     * Constructor for the meal
     */
    public Meal() {
        color = ColorHelper.generateSaturatedColor();
        resetUid();
    }

    /**
     * Set the meal type
     *
     * @param type The type of the meal
     * @return The current meal
     */
    public Meal setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * Set the meal type option
     *
     * @param option The option for the meal type
     * @return The current meal
     */
    public Meal setOption(String option) {
        this.option = option;
        return this;
    }

    /**
     * Get the unique identifier for the order
     *
     * @return The meals unique identifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Get the meal type
     *
     * @return The type of meal
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the meal type option
     *
     * @return The option for the meal type
     */
    public String getOption() {
        return option;
    }

    /**
     * Get the color associated with the meal in the list view
     *
     * @return The associated meal color
     */
    public int getColor() {
        return color;
    }

    /**
     * Reset the meals unique identifier
     *
     * @return The current meal
     */
    private Meal resetUid() {
        uid = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Returns a better string representation of the meal for the order review (i.e. Green Salad Lunch: Ranch)
     *
     * @return A better string representation of the meal
     */
    @Override
    public String toString() {
        return getType().getName() + ": " + getOption();
    }

}
