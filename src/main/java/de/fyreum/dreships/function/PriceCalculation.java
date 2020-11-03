package de.fyreum.dreships.function;

import de.fyreum.dreships.data.ConfigLocalization;
import org.bukkit.Location;

public class PriceCalculation {

    public int calculate(double distance, double distanceMultipliedPrice) {
        return (int) Math.ceil((distance * distanceMultipliedPrice) + ConfigLocalization.START_PRICE.asInt());
    }

    public int calculate(Location loc1, Location loc2, double distanceMultipliedPrice) {
        double distance = loc1.distance(loc2);
        return this.calculate(distance, distanceMultipliedPrice);
    }

    public double getDistanceMultiplier(String name) {
        if (name.equalsIgnoreCase("AIRSHIP")) {
            return (double) ConfigLocalization.AIRSHIP_PRICE_DISTANCE_MULTIPLIER.value;
        }
        if (name.equalsIgnoreCase("SHIP")) {
            return (double) ConfigLocalization.SHIP_PRICE_DISTANCE_MULTIPLIER.value;
        }
        if (name.equalsIgnoreCase("LAND")) {
            return (double) ConfigLocalization.LAND_PRICE_DISTANCE_MULTIPLIER.value;
        }
        return -1;
    }

}
