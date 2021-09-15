package Default_classes;

import Enumerators.DirectionEnum;
import Game_data.GameState;
import Services.Combat;
import Services.Controller;
import Services.Terminal;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Player extends Character {
    private Location _currentLocation;
    private Equipment _weapon = null;
    private Equipment _armor = null;
    private boolean _isDodge = false;
    private boolean _isBlock = false;

    public Player(String name, int strength, int dexterity, int constitution, Location startLocation) {
        _name = name;
        _strength = strength;
        _dexterity = dexterity;
        _constitution = constitution;
        _inventory = new HashMap<String, Item>();
        _currentLocation = startLocation;
        _maxHitPoints = _constitution * 2 + (int)(BASE_HEALTH * 1.5);
        _attack = _strength * 2 + (int)(BASE_ATTACK * 1.5);
        _block = _constitution * 2 + 1;
        _maxStamina = _dexterity + BASE_STAMINA;
        _currentHitPoints = _maxHitPoints;
        _currentStamina = _maxStamina;
    }

    public Location GetCurrentLocation() { return _currentLocation; }
    public boolean GetIsDodge(){return _isDodge;}
    public boolean GetIsBlock(){return _isBlock;}

    public void SetCurrentLocation(String locationName) { _currentLocation = GameState.GetLocation(locationName); }
    public void SetCurrentLocation(Location location) { _currentLocation = location; }
    public void SetIsDodge(boolean dodge){ _isDodge = dodge;}
    public void SetIsBlock(boolean block){ _isBlock = block;}

    public void ResetResponseActions() {
        _isDodge = false;
        _isBlock = false;
    }

    public Equipment GetWeapon() { return _weapon;}
    public Equipment GetArmor() { return _armor;}
    public void SetWeapon(Equipment newWeapon) {
        if(_weapon != null) {
            SetAttack(GetAttack() - _weapon.GetAttackBonus());
            AddToInventory(_weapon);
        }
        SetAttack(GetAttack() + newWeapon.GetAttackBonus());
        RemoveFromInventory(newWeapon.GetName());
        _weapon = newWeapon;
    }
    public void SetArmor(Equipment newArmor) {
        if(_armor != null) {
            SetBlock(GetBlock() - _armor.GetBlockBonus());
            AddToInventory(_armor);
        }
        SetBlock(GetBlock() + newArmor.GetBlockBonus());
        RemoveFromInventory(newArmor.GetName());
        _armor = newArmor;
    }

    public void Use(String itemToUseName) {
        if(!_inventory.containsKey(itemToUseName)) {
            Terminal.PrintLine("You do not posses a " + itemToUseName + "\n");
            return;
        }
        if(GameState.Combat) {
            _currentStamina = _currentStamina - 1;
        }
        Item itemToUse = _inventory.get(itemToUseName);
        itemToUse.Use();
    }

    public void UseOnItem(String itemToUseName, String targetItemName) {
        if(!_inventory.containsKey(itemToUseName)) {
            Terminal.PrintLine("You do not posses a " + itemToUseName + "\n");
            return;
        }
        if(!_currentLocation.ContainsItem(targetItemName)) {
            Terminal.PrintLine("There is no " + targetItemName + " present in this location.\n");
            return;
        }
        if(GameState.Combat) {
            _currentStamina = _currentStamina - 1;
        }
        Item itemToUse = _inventory.get(itemToUseName);
        Item targetItem = _currentLocation.GetItem(targetItemName);
        itemToUse.Use(targetItem);
    }

    public void UseOnNpc(String itemToUseName, String targetNpcName) {
        if(!_inventory.containsKey(itemToUseName)) {
            Terminal.PrintLine("You do not posses a " + itemToUseName + "\n");
            return;
        }
        if(!_currentLocation.ContainsNpc(targetNpcName)) {
            Terminal.PrintLine("The character " + targetNpcName + " is not present in this location.\n");
            return;
        }
        _currentStamina = _currentStamina - 1;
        Item itemToUse = _inventory.get(itemToUseName);
        NPC targetNpc = _currentLocation.GetNpc(targetNpcName);
        if(!GameState.Combat && targetNpc.IsFightable()){
            targetNpc.SetHostility(true);
            Combat.Init(true);
        }
        itemToUse.Use(targetNpc);
    }

    public void TalkTo(String targetNpcName) {
        if (!_currentLocation.ContainsNpc(targetNpcName)) {
            Terminal.PrintLine("The character " + targetNpcName + " is not present in this location.\n");
            return;
        }

        Terminal.PrintLine("You approach " + targetNpcName + "\n");
        NPC targetNpc = _currentLocation.GetNpc(targetNpcName);
        targetNpc.Talk();
    }

    public void Take(String targetItemName) {
        if (!_currentLocation.ContainsItem(targetItemName)) {
            Terminal.PrintLine("There is no " + targetItemName + " present in this location.\n");
            return;
        }

        Item targetItem = _currentLocation.GetItem(targetItemName);
        if(!targetItem.IsRetrievable()) {
            Terminal.PrintLine("The " + targetItemName + " can't be picked up.\n");
            return;
        }
        _currentLocation.RemoveItem(targetItemName);
        AddToInventory(targetItem);
        Terminal.PrintLine("You took the " + targetItemName + "\n");
        if(targetItem.IsWinningItem()){
            targetItem.FinishGame();
        }
    }

    public void Move(DirectionEnum direction) {
        String adjacentLocationName = _currentLocation.GetAdjacentLocation(direction);
        if (adjacentLocationName == null || adjacentLocationName.isEmpty()) {
            Terminal.PrintLine("There is nothing in this direction.\n");
            return;
        }


        _currentLocation = GameState.GetLocation(adjacentLocationName);
        Terminal.PrintLine("\n" + _currentLocation.GetDescription() + "\n");

        if(!_currentLocation.getEnemies().isEmpty()) {Combat.Init(false);}
    }

    @Override
    public void Die() {
        Combat.CombatEnd();
        Terminal.PrintLine("Time for respawn!");
        Respawn();
    }

    @Override
    public void Attack(String targetCharacterName) {
        NPC target = GameState.MainCharacter.GetCurrentLocation().GetNpc(targetCharacterName);
        if(!target.IsFightable()){
            Terminal.PrintLine("You can`t attack this character");
            return;
        }
        if(!GameState.Combat){
            target.SetHostility(true);
            Combat.Init(true);
        }
        _currentStamina = _currentStamina - 1;
        DealDamage(target, false, false);
        if(target.IsDead()){
            target.Die();
        } else {
            target.PrintCodition();
        }
    }

    public void ResponseAction(){
        if(_currentStamina > 0){
            Controller.ExecuteResponseCommand();
        }
    }

    public void PrintInventory() {
        String inventoryItems = String.join(", ", _inventory.keySet());
        Terminal.PrintLine("You posses the following items:\n" + inventoryItems);
    }

    private void Respawn(){
        _currentLocation = GameState.GetLocation(GameState.SpawnLocation);
        Terminal.PrintLine(GameState.MainCharacter.GetCurrentLocation().GetDescription() + "\n");
    }
}