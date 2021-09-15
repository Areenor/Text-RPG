//
// Default consumable
//

package Default_classes;

import Configuration_models.ItemConfig;
import Services.Terminal;

public class Junk extends Item{
    public Junk(ItemConfig config) {
        super(config);
    }

    @Override
    public void Use() { Terminal.PrintLine("This item has no use, it is junk.");}

    @Override
    public void Use(Item targetItem) { Terminal.PrintLine("This item has no use, it is junk."); }

    @Override
    public void Use(NPC targetNpc) {
        Terminal.PrintLine("This item has no use, it is junk.");
    }
}
