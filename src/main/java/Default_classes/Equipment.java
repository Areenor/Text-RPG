//
// Equipment class
//

package Default_classes;

import Configuration_models.EquipConfig;
import Enumerators.EquipmentTypeEnum;
import Game_data.GameState;
import Services.Terminal;


public class Equipment extends Item{
    private int _blockBonus;
    private int _attackBonus;
    private EquipmentTypeEnum _type;

    public Equipment(EquipConfig config) {
        super(config);
        _blockBonus = config.BlockBonus;
        _attackBonus = config.AttackBonus;
        _type = config.Type;
    }

    public int GetAttackBonus() {
        return _attackBonus;
    }
    public int GetBlockBonus() {
        return _blockBonus;
    }

    @Override
    public void Use() {
        switch (_type){
            case Weapon:
                GameState.MainCharacter.SetWeapon(this);
                break;
            case Armor:
                GameState.MainCharacter.SetArmor(this);
                break;
            default:
                throw new IllegalArgumentException("The configuration is empty, misses EquipmentType");
        }
        Terminal.PrintLine("You equipped the " + _name + " of type " + _type);
    }

    @Override
    public void Use(Item targetItem) {
        Terminal.PrintLine("This is a piece of equipment. Equipment can only be used on yourself.");
    }

    @Override
    public void Use(NPC targetNpc) {
        Terminal.PrintLine("This is a piece of equipment. Equipment can only be used on yourself.");
    }
}
