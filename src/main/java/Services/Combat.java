package Services;


import Default_classes.Character;
import Default_classes.NPC;
import Game_data.GameState;
import java.util.ArrayList;


public abstract class Combat {
    private static ArrayList<Character> combatOrder = new ArrayList<>();
    private static boolean skipTurn = false;

    public static boolean GetSkipTurn(){ return skipTurn; }
    public static ArrayList<Character> getCombatOrder(){return combatOrder;}

    public static void SetSkipTurn(boolean turn){ skipTurn = turn; }
    public static void setCombatOrder(ArrayList<Character> newOrder){combatOrder = newOrder;}

    public static void Init(boolean PlayerInitiated){
        Terminal.PrintLine("Combat starts!");
        GameState.Combat = true;
        printCombatDialoges();

        if(!PlayerInitiated){
            combatOrder.add(GameState.MainCharacter);
            combatOrder.addAll(GameState.MainCharacter.GetCurrentLocation().getEnemies());
            combatOrder.sort(Character.DexterityComparator);
        } else {
            combatOrder.addAll(GameState.MainCharacter.GetCurrentLocation().getEnemies());
            combatOrder.sort(Character.DexterityComparator);
            combatOrder.add(0,GameState.MainCharacter);
        }

        Terminal.PrintLine("Your HP is: " + GameState.MainCharacter.GetCurrentHitPoints());
        Terminal.PrintLine("Your Stamina is: " + GameState.MainCharacter.GetCurrentStamina());
    }

    public static void Run(ArrayList<Character> order, int start){
        if(order.size() == 1){
            CombatEnd();
        }
        else{
            combatFlow(order, start);
        }
    }

    public static void CombatEnd(){
        GameState.Combat = false;
        GameState.MainCharacter.ResetHealth();
        GameState.MainCharacter.ResetStamina();
        combatOrder.clear();
        Terminal.PrintLine("Combat ends!");
    }

    private static void combatFlow(ArrayList<Character> order, int start){
        for(int current = 0; current < order.size(); current++) {
            if(current < start){
                continue;
            }
            Character currentCharacter = order.get(current);
            if(currentCharacter == GameState.MainCharacter){
                playerAction();
            } else {
                if(currentCharacter.IsDead()){
                    runNewOrder(order, current);
                    break;
                }
                enemyAction(currentCharacter);
            }
        }
    }

    private static void runNewOrder(ArrayList<Character> oldOrder, int indexOfDeadNpc){
        oldOrder.remove(indexOfDeadNpc);
        Run(oldOrder, indexOfDeadNpc);
    }

    private static void playerAction(){
        Terminal.PrintLine("Its your turn!");
        GameState.MainCharacter.ResetStamina();

        while(GameState.MainCharacter.GetCurrentStamina() > 0 && GameState.Combat && !skipTurn){
            if(GameState.MainCharacter.GetCurrentLocation().getEnemies().isEmpty()) {return;}
            Terminal.PrintLine("Your Stamina is: " + GameState.MainCharacter.GetCurrentStamina());
            Controller.ExecuteCombatCommand(GameState.MainCharacter);
        }

        skipTurn = false;
    }

    private static void enemyAction(Character attacker){
        attacker.Attack(GameState.MainCharacter.GetName());
        if(GameState.Combat) {
            Terminal.PrintLine("Your HP is: " + GameState.MainCharacter.GetCurrentHitPoints());
        }
    }

    private static void printCombatDialoges(){
        for(int current = 0; current < GameState.MainCharacter.GetCurrentLocation().getEnemies().size(); current++) {
            NPC currentNPC = GameState.MainCharacter.GetCurrentLocation().getEnemies().get(current);
            Terminal.Print(currentNPC.GetName() + " : ");
            currentNPC.CombatTalk();
        }
    }
}
