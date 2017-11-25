package ucol.a1599116.tuckbox.util;

import android.support.v4.graphics.ColorUtils;

import java.util.Random;

/**
 * Color helper utility
 * Used for everything color related (i.e. blending, transparency change, color generation etc.)
 */
public class ColorHelper {

    //Randomization for color generation
    private static final Random RANDOM = new Random();

    /**
     * Generate a random saturated color
     *
     * @return A random saturated color
     */
    public static int generateSaturatedColor() {
        return ColorUtils.HSLToColor(new float[]{RANDOM.nextFloat() * 360, 0.8f, .7f});
    }

}
