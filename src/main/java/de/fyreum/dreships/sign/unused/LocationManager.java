package de.fyreum.dreships.sign.unused;

import java.util.Map;

// EXPERIMENTAL CLASS
public class LocationManager {

    private final LocList<WaterLocation> locations;
    // Example path: data.locations
    private final String path;
    private boolean dataLoaded = false;

    public LocationManager(String path) {
        this.locations = new LocList<>();
        this.path = path;
    }

    public void load() {
        for (Map<?, ?> map : LoggerConfig.getConfig().getMapList(path)) {
            locations.add(new ShortLocation((Map<String, Object>) map));
        }
        dataLoaded = true;
    }

    public void save(LocList<WaterLocation> list) {

    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public LocList<WaterLocation> getLocations() {
        return locations;
    }
}
