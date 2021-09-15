//
// configuration file for creating a location instance
//

package Configuration_models;
import java.util.*;
import Enumerators.DirectionEnum;

public class LocationConfig {
    public String Name;
    public String Description;
    public Boolean IsSpawn;
    public List<String> Items;
    public String[] Characters;
    public Map<DirectionEnum, String> AdjacentLocations = new HashMap<DirectionEnum, String>() {{
        put(DirectionEnum.north,"");
        put(DirectionEnum.east,"");
        put(DirectionEnum.south,"");
        put(DirectionEnum.west,"");
        put(DirectionEnum.up, "");
        put(DirectionEnum.down, "");
    }};
}
