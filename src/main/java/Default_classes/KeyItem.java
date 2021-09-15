//
// Default key Item
//

package Default_classes;

import Configuration_models.KeyItemConfig;
import Enumerators.DirectionEnum;
import Game_data.GameState;
import Services.Terminal;

import java.util.HashMap;
import java.util.Map;

public class KeyItem extends Item{

    private Map<String, String> _newTargetDescriptions = new HashMap<String, String>();
    private Map<String, DirectionEnum> _unlockedDirections = new HashMap<String, DirectionEnum>();
    private Map<String, String> _unlockedLocationNames = new HashMap<String, String>();
    private Map<String, String> _npcItemExchange = new HashMap<String, String>();

    public KeyItem(KeyItemConfig config) {
        super(config);

        _newTargetDescriptions = config.NewTargetDescriptions;
        _unlockedDirections = config.UnlockedDirections;
        _unlockedLocationNames = config.UnlockedLocationNames;
        _npcItemExchange = config.NPCItemExchange;
    }

    @Override
    public void Use() {
        Terminal.PrintLine("This is a key item. Key items cannot be used on yourself, only on other items or characters in your location.\n");
    }

    @Override
    public void Use(Item targetItem) { //Using a key item on a target item changes the target item's description and unlocks access to a new location.
        String targetItemName = targetItem.GetName();
        if(IsCorrectTargetItem(targetItemName)) {
            SetTargetItemDescription(targetItem, targetItemName);

            GiveAccessNewLocation(targetItemName);
            Terminal.PrintLine("You successfully used " + _name + " on " + targetItem.GetName() + ".\n");

            _newTargetDescriptions.remove(targetItemName);
            _unlockedDirections.remove(targetItemName);
            _unlockedDirections.remove(targetItemName);
        }
        else {
            Terminal.PrintLine("This is not the intended target for this key item. Try again on a different target.\n");
        }
    }

    @Override
    public void Use(NPC targetNpc) { //If a key item is used on a target NPC, the key item swapped with a different item in the NPC's inventory.
        String targetNPCName = targetNpc.GetName();
        if(IsCorrectTargetNPC(targetNPCName)) {
            String wantedItemName = _npcItemExchange.get(targetNPCName);
            if(targetNpc.HasItem(wantedItemName)) {
                Item wantedItem = targetNpc.GetItem(wantedItemName);

                GameState.MainCharacter.AddToInventory(wantedItem);
                GameState.MainCharacter.RemoveFromInventory(_name);

                targetNpc.RemoveFromInventory(wantedItemName);
                targetNpc.AddToInventory(this);

                _npcItemExchange.remove(targetNPCName);
                Terminal.PrintLine("You gave " + _name + " to " + targetNpc.GetName() + " and got " + wantedItemName + " in return.\n");
            }
            else {
                Terminal.PrintLine("This NPC does not have the item needed for the exchange.\n");
            }
        }
        else {
            Terminal.PrintLine("This is not the intended target for this key item. Try again on a different target.\n");
        }
    }

    private boolean IsCorrectTargetItem(String itemName) {
        return _newTargetDescriptions.containsKey(itemName);
    }

    private boolean IsCorrectTargetNPC(String npcName) {
        return _npcItemExchange.containsKey(npcName);
    }

    private void SetTargetItemDescription(Item targetItem, String targetItemName) {
        targetItem.SetDescription(_newTargetDescriptions.get(targetItemName));
    }

    private void GiveAccessNewLocation(String targetItemName) {
        Location currentLocation = GameState.MainCharacter.GetCurrentLocation();
        currentLocation.AddAdjacentLocation(_unlockedDirections.get(targetItemName),_unlockedLocationNames.get(targetItemName));
    }
}
