package Default_classes;

import Configuration_models.LocationConfig;
import Game_data.GameState;
import Services.InitiationService;
import Enumerators.DirectionEnum;
import Services.ItemFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  Location {
    private final String _name;
    private String _baseDescription;
    private Map<String, Item> _items = new HashMap<String, Item>();
    private Map<String, NPC> _NPCs = new HashMap<String, NPC>();
    private Map<DirectionEnum, String> _adjacentLocations = new HashMap<DirectionEnum, String>();

    public Location(LocationConfig config) throws FileNotFoundException {
        if (config == null) throw new IllegalArgumentException("The configuration is empty");
        if (config.Name.isEmpty()) throw new IllegalArgumentException("The location name is empty");
        if (config.Description.isEmpty()) throw new IllegalArgumentException("The location description is empty");

        _name = config.Name;
        _baseDescription = config.Description;
        _adjacentLocations = config.AdjacentLocations;
        for (String character : config.Characters) {
            _NPCs.put(character, InitiationService.InitiateCharacter(character));
        }


        for (String items : config.Items) {
            _items.put(items, ItemFactory.GetItem(items));
        }

        boolean isSpawn = false;
        if(config.IsSpawn != null){
            isSpawn = config.IsSpawn;
        }
        if(isSpawn){
            GameState.SpawnLocation = _name;
        }

    }

    public String GetName() { return _name; }
    public String GetDescription() { return (_baseDescription + "\n" + GetItemDescriptions() + "\n" + GetNpcDescriptions()); }
    public String GetAdjacentLocation(DirectionEnum direction) { return _adjacentLocations.get(direction); }
    public Item GetItem(String ItemName) { return _items.get(ItemName); }
    public NPC GetNpc(String NpcName) { return _NPCs.get(NpcName); }
    public Map<String, NPC> GetAllNpc() {return _NPCs;}
    public List<NPC> getEnemies(){
        List<NPC> allEnemies = new ArrayList<NPC>();
        List<NPC> allNPC = new ArrayList<NPC>(GetAllNpc().values());
        for(NPC current: allNPC){
            if(current.IsHostile()){ allEnemies.add(current); }
        }
        return allEnemies;
    }

    public void SetBaseDescription(String description) { _baseDescription = description; }
    public void SetAdjacentLocation(DirectionEnum direction, String locationName) { _adjacentLocations.replace(direction, locationName); }
    public void AddAdjacentLocation(DirectionEnum direction, String locationName) { _adjacentLocations.put(direction, locationName); }
    public void SetNpc(String NpcName, NPC npc) { _NPCs.replace(NpcName, npc); }
    public void SetItem(String ItemName, Item item) { _items.replace(ItemName, item); }

    public boolean ContainsNpc(String NpcName) { return _NPCs.containsKey(NpcName); }
    public void AddNpc(NPC npc) { _NPCs.put(npc.GetName(), npc); }
    public void RemoveNpc(String npcName) { _NPCs.remove(npcName); }

    public boolean ContainsItem(String ItemName) { return _items.containsKey(ItemName); }
    public void AddItem(Item item){ _items.put(item.GetName(), item);  }
    public void RemoveItem(String itemName) { _items.remove(itemName); }

    private String GetItemDescriptions() {
        StringBuilder itemDescriptions = new StringBuilder();
        for(Item item : _items.values()) {
            itemDescriptions.append(item.GetDescription());
        }
        return itemDescriptions.toString();
    }

    private String GetNpcDescriptions() {
        StringBuilder NpcDescriptions = new StringBuilder();
        for(NPC NPC: _NPCs.values()) {
            NpcDescriptions.append(NPC.GetDescription());
        }
        return NpcDescriptions.toString();
    }
}
