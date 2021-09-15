package Default_classes;

import Game_data.GameState;
import Services.Terminal;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Character {

    protected final int BASE_HEALTH = 5;
    protected final int BASE_ATTACK = 1;
    protected final int BASE_STAMINA = 1;

    protected String _name;
    protected int _strength;
    protected int _dexterity;
    protected int _constitution;
    protected int _maxHitPoints;
    protected int _currentHitPoints;
    protected int _attack;
    protected int _block;
    protected int _maxStamina;
    protected int _currentStamina;
    protected Map<String, Item> _inventory = new HashMap<>();

    public String GetName() { return _name; }
    public int GetStrength(){ return _strength; }
    public int GetDexterity(){ return _dexterity; }
    public int GetConstitution() { return _constitution; }
    public int GetMaxHitPoints() { return _maxHitPoints; }
    public int GetCurrentHitPoints() { return _currentHitPoints; }
    public int GetAttack()  { return _attack; }
    public int GetBlock()  { return _block; }
    public int GetMaxStamina() { return _currentStamina; }
    public int GetCurrentStamina() { return _currentStamina; }
    public Item GetItem(String itemName) { return _inventory.get(itemName); }
    public Boolean HasItem(String itemName) { return _inventory.containsKey(itemName); }

    public void SetStrength(int strength){ _strength = strength; }
    public void SetDexterity(int dexterity){ _dexterity = dexterity; }
    public void SetConstitution(int constitution) { _constitution = constitution; }
    public void SetMaxHitPoints(int hitPoints) { _maxHitPoints = hitPoints; }
    public void SetCurrentHitPoints(int currentHitPoints) { _currentHitPoints = currentHitPoints; }
    public void SetAttack(int attack)  { _attack = attack; }
    public void SetBlock(int block)  { _block = block; }
    public void SetMaxStamina(int stamina) { _maxStamina = stamina; }
    public void SetCurrentStamina(int currentStamina) { _currentStamina = currentStamina;};

    public boolean IsDead() { return _currentHitPoints <= 0; }

    public void ResetHealth() { _currentHitPoints = _maxHitPoints; }
    public void ResetStamina() { _currentStamina = _maxStamina; }

    public static Comparator<Character> DexterityComparator = (Character character1, Character character2) -> {
        int characterDexterity1 = character1.GetDexterity();
        int characterDexterity2 = character2.GetDexterity();

        //descending order
        return characterDexterity2 - characterDexterity1;

    };

    public void AddToInventory(Item item) { _inventory.put(item.GetName(), item); }
    public void RemoveFromInventory(String itemName) { _inventory.remove(itemName); }

    public void Die(){ }

    public void Attack(String targetCharacterName){ }

    public void DealDamage(Character target, boolean dodge, boolean block){
       int dealtDamage = _attack;
       if(block){
           dealtDamage = dealtDamage - target.GetBlock();
       } else if(dodge){
           int chance = new Random().nextInt(11) + target.GetDexterity(); //50% chance to dodge + dexterity bonus
           if(chance > 5){
               Terminal.Print("Success! ");
               dealtDamage = 0;
           }
       }

        GameState.MainCharacter.ResetResponseActions();

        target.SetCurrentHitPoints(target.GetCurrentHitPoints() - dealtDamage);
        Terminal.PrintLine(_name + " strikes " + target.GetName() + " with " + dealtDamage + " points of damage!");
    }
}
