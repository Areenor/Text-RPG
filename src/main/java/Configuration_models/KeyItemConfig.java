//
// configuration file for creating an object instance
//
package Configuration_models;

import Enumerators.DirectionEnum;

import java.util.HashMap;
import java.util.Map;

public class KeyItemConfig extends ItemConfig {
    public Map<String, String> NewTargetDescriptions = new HashMap<String, String>();
    public Map<String, DirectionEnum> UnlockedDirections = new HashMap<String, DirectionEnum>();
    public Map<String, String> UnlockedLocationNames = new HashMap<String, String>();
    public Map<String, String> NPCItemExchange = new HashMap<String, String>();
}
