//
// Default consumable
//

package Default_classes;

import Configuration_models.ConsumConfig;
import Services.Terminal;
import Enumerators.StatEnum;
import Game_data.GameState;

public class Consumable extends Item{
    private int _statChange;
    private boolean _isDangerous;
    private StatEnum _affectedStat;

    public Consumable(ConsumConfig config) {
        super(config);
        _statChange = config.StatChange;
        _isDangerous = config.IsDangerous;
        _affectedStat = config.AffectedStat;
    }

    @Override
    public void Use() {
        if (!_isDangerous) { //you can use helpful consumable items on yourself to raise your stats.
            int oldStat;
            switch (_affectedStat){
                case Strength:
                    oldStat = GameState.MainCharacter.GetStrength();
                    GameState.MainCharacter.SetStrength(oldStat + _statChange);
                    break;
                case Dexterity:
                    oldStat = GameState.MainCharacter.GetDexterity();
                    GameState.MainCharacter.SetDexterity(oldStat + _statChange);
                    break;
                case Constitution:
                    oldStat = GameState.MainCharacter.GetConstitution();
                    GameState.MainCharacter.SetConstitution(oldStat + _statChange);
                    break;
                case HitPoints:
                    oldStat = GameState.MainCharacter.GetCurrentHitPoints();
                    GameState.MainCharacter.SetMaxHitPoints(oldStat + _statChange);
                    break;
                case Attack:
                    oldStat = GameState.MainCharacter.GetAttack();
                    GameState.MainCharacter.SetAttack(oldStat + _statChange);
                    break;
                case Block:
                    oldStat = GameState.MainCharacter.GetBlock();
                    GameState.MainCharacter.SetBlock(oldStat + _statChange);
                    break;
                case Stamina:
                    oldStat = GameState.MainCharacter.GetCurrentStamina();
                    GameState.MainCharacter.SetMaxStamina(oldStat + _statChange);
                    break;
                default:
                    throw new IllegalArgumentException("The configuration is empty, misses AffectedStat");
            }
            Terminal.PrintLine("You consumed the item and raised your " + _affectedStat + " with " + _statChange + ".");
            GameState.MainCharacter.RemoveFromInventory(_name);
        }
        else {
            Terminal.PrintLine("This is a consumable item is dangerous to use on yourself. Use it on someone else.\n");
        }
    }

    @Override
    public void Use(Item targetItem) {
        Terminal.PrintLine("This is a consumable item. Consumable items can only be used on yourself or someone else.\n");
    }

    @Override
    public void Use(NPC targetNpc) { //you can use dangerous consumable items on NPC's in and out of combat
        if (_isDangerous) {
            int oldStat;
            switch (_affectedStat){
                case Strength:
                    oldStat = targetNpc.GetStrength();
                    targetNpc.SetStrength(oldStat + _statChange);
                    break;
                case Dexterity:
                    oldStat = targetNpc.GetDexterity();
                    targetNpc.SetDexterity(oldStat + _statChange);
                    break;
                case Constitution:
                    oldStat = targetNpc.GetConstitution();
                    targetNpc.SetConstitution(oldStat + _statChange);
                    break;
                case HitPoints:
                    oldStat = targetNpc.GetMaxHitPoints();
                    targetNpc.SetMaxHitPoints(oldStat + _statChange);
                    break;
                case Attack:
                    oldStat = targetNpc.GetAttack();
                    targetNpc.SetAttack(oldStat + _statChange);
                    break;
                case Block:
                    oldStat = targetNpc.GetBlock();
                    targetNpc.SetBlock(oldStat + _statChange);
                    break;
                case Stamina:
                    oldStat = targetNpc.GetMaxStamina();
                    targetNpc.SetMaxStamina(oldStat + _statChange);
                    break;
                default:
                    throw new IllegalArgumentException("The configuration is empty, misses AffectedStat");
            }
            Terminal.PrintLine("You used the consumable item on " + targetNpc.GetName() + " and changed their " + _affectedStat + " with " + _statChange + ".");
            GameState.MainCharacter.RemoveFromInventory(_name);
        }
        else {
            Terminal.PrintLine("This is a consumable item that is helpful to you, use it on yourself.\n");
        }
    }
}
