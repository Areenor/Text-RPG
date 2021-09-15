//
// Default object type
//

package Default_classes;

import Configuration_models.ItemConfig;
import Game_data.GameState;
import Services.Terminal;

import java.util.concurrent.TimeUnit;


public abstract class Item {
    protected final String _name;
    protected String _description;
    protected boolean _isRetrievable;
    protected boolean _isWinningItem = false;
    protected String _endText;

    public Item(ItemConfig config) {
        if (config == null) throw new IllegalArgumentException("The configuration is empty");
        if (config.Name.isEmpty()) throw  new IllegalArgumentException("The object name is empty");
        if (config.Description.isEmpty()) throw  new IllegalArgumentException("The object description is empty");

        _name = config.Name;
        _description = config.Description;
        _isRetrievable = config.IsRetriavable;
        _isWinningItem = config.IsWinningItem;
        if(_isWinningItem){ _endText = config.EndText; }

    }

    public String GetName() { return _name; }
    public String GetDescription() { return _description; }

    public boolean IsRetrievable() { return _isRetrievable; }
    public boolean IsWinningItem() { return _isWinningItem; }

    public void SetDescription(String description) { _description = description; }
    public void SetIsRetrievable(boolean retrievable) { _isRetrievable = retrievable; }
    public void SetIsWinningItem(boolean winningItem) { _isWinningItem = winningItem; }


    public void FinishGame(){
        Terminal.PrintLine(_endText);
        GameState.IsFinished = true;
        try { TimeUnit.SECONDS.sleep(10); } //delay closing the window so end text is readable
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Terminal.CloseTerminal();
    }
    public void Use() { }

    public void Use(Item targetItem) { }

    public void Use(NPC targetNpc) { }
}
