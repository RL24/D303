package ucol.a1599116.tuckbox.util;

/**
 * String helper utility
 * Used for everything string related
 */
public class StringHelper {

    //Extras string constants
    public static final String NEW_ORDER = "NEW_ORDER",
            ORDER = "ORDER",
            ORDER_ID = "ORDER_ID";

    /**
     * Capitalize the first letter of a given string
     *
     * @param input The given string to capitalize
     * @return The capitalized input string
     */
    public static String capitalize(String input) {
        return String.format("%s%s", input.substring(0, 1).toUpperCase(), input.substring(1));
    }

    /**
     * Format a given enum name
     *
     * @param e The input enum
     * @return The input enum name formatted
     */
    public static String formatEnumToString(Enum e) {
        String input = e.name().toLowerCase();
        String[] parts = input.split("_");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            output.append(capitalize(parts[i]));
            if (i < parts.length - 1)
                output.append(" ");
        }
        return output.toString();
    }

}
