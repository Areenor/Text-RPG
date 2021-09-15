package Default_classes;

import Configuration_models.NPCConfig;
import Game_data.GameState;
import Services.InitiationService;
import Services.Terminal;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class NPC extends Character {
    private String _description;
    private String _dialogue;
    private String _combatDialogue;
    private boolean _isHostile = false;
    private boolean _isFightable;

    public NPC(NPCConfig config) throws FileNotFoundException {
        if (config == null) throw new IllegalArgumentException("The configuration is empty");
        if (config.Name.isEmpty()) throw  new IllegalArgumentException("The character name is empty");
        if (config.Description.isEmpty()) throw  new IllegalArgumentException("The character description is empty");

        _name = config.Name;
        _description = config.Description;
        _dialogue = config.Dialogue;
        _strength = config.Strength;
        _dexterity = config.Dexterity;
        _constitution = config.Constitution;
        _isFightable = config.IsFightable;
        if(_isFightable){
            _isHostile = config.IsHostile;
            _combatDialogue = config.CombatDialogue;
        }
        _maxHitPoints = _constitution * 2 + BASE_HEALTH;
        _attack = _strength * 2 + BASE_ATTACK;
        _maxStamina = _dexterity + BASE_STAMINA;
        _inventory = InitiationService.InitiateCharacterInventory(config.Inventory);
        _currentHitPoints = _maxHitPoints;
        _currentStamina = _maxStamina;
    }

    public String GetDescription(){ return _description; }
    public String GetDialogue() { return _dialogue; }
    public String GetCombatDialogue() { return _combatDialogue; }

    public boolean IsHostile() { return _isHostile; }
    public boolean IsFightable() { return _isFightable; }


    public void SetFightability (boolean newFightability) { _isFightable = newFightability; }
    public void SetHostility (boolean newHostility) { _isHostile = newHostility; }
    public void SetDescription(String description){ _description = description; }
    public void SetDialogue(String dialogue) { _dialogue = dialogue; }
    public void SetCombatDialogue(String dialogue) { _combatDialogue = dialogue; }

    public void Talk() {
        if (_dialogue == null || _dialogue.isEmpty()) {
            Terminal.PrintLine(_name + " has nothing to say to you.");
        }
        Terminal.PrintLine(_dialogue + "\n");
    }

    public void CombatTalk() {
        if (_combatDialogue == null || _combatDialogue.isEmpty()) {
            return;
        }
        Terminal.PrintLine(_combatDialogue);
    }
    public void PrintCodition(){
        if(_currentHitPoints <= 2){ Terminal.PrintLine(_name + " looks almost dead"); }
        else if(_currentHitPoints <= 4){ Terminal.PrintLine(_name + " looks damaged, but is still standing"); }
        else{ Terminal.PrintLine(_name + " looks fully alive"); }
    }
    @Override
    public void Attack(String targetCharacterName) {
        Terminal.PrintLine(_name + " is attacking " + targetCharacterName +"!");
        GameState.MainCharacter.ResponseAction();
        DealDamage(GameState.MainCharacter, GameState.MainCharacter.GetIsDodge(), GameState.MainCharacter.GetIsBlock());
        if(GameState.MainCharacter.IsDead()){
            GameState.MainCharacter.Die();
        }
    }

    @Override
    public void Die() {
        Terminal.PrintLine(_name + " is dead!");
        DropInventory(GameState.MainCharacter.GetCurrentLocation());
        GameState.MainCharacter.GetCurrentLocation().RemoveNpc(_name);
    }

    public void DropInventory(Location dropLocation) {

        ArrayList<String> inventoryItemNames = new ArrayList<String>();
        for(String itemName : _inventory.keySet()) {
            inventoryItemNames.add(itemName);
        }
        for (String itemName : inventoryItemNames) {
            Item item = GetItem(itemName);
            RemoveFromInventory(itemName);
            dropLocation.AddItem(item);
        }
    }
}