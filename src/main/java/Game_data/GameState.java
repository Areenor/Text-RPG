package Game_data;

import Default_classes.Player;
import Default_classes.Location;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    public static boolean Combat = false;
    public static boolean IsFinished = false;
    public static String SpawnLocation;
    public static Player MainCharacter;
    public static Map<String, Location> Locations = new HashMap<>();

    public static Location GetLocation(String locationName) {
        if (Locations.containsKey(locationName))
            return Locations.get(locationName);
        return null;
    }
}
