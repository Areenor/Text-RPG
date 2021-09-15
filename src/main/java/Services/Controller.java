package Services;

import Default_classes.Item;
import Default_classes.Location;
import Default_classes.NPC;
import Default_classes.Player;
import Game_data.GameState;
import Enumerators.DirectionEnum;

public abstract class Controller {
    public static void ExecuteCommand(Player player) {
        String userInput = Terminal.Read();
        if (userInput == null || userInput.isEmpty())
            return;

        String[] args = userInput.split(" ");
        String command = args[0];

        switch (command) {
            case "examine":
                Location playerLocation = player.GetCurrentLocation();
                Examine(args, playerLocation);
                break;
            case "talk":
                Talk(args, player);
                break;
            case "take":
                Take(args, player);
                break;
            case "move":
                Move(args, player);
                break;
            case "use":
                Use(args, player);
                break;
            case "attack":
                Attack(args, player);
                break;
            case "inventory":
                player.PrintInventory();
                break;
            case "commands":
                PrintUsableCommands();
                break;
            case "exit":
            case "quit":
                GameState.IsFinished = true;
                Terminal.CloseTerminal();
                break;
            default:
                Terminal.PrintLine("Unknown command.\n");
        }
    }

    public static void PrintUsableCommands() {
        if(!GameState.Combat) {
            Terminal.PrintLine("Examine, talk to, take, move, use, attack, skip, inventory, exit, quit.");
        } else {
            Terminal.PrintLine("use, attack, inventory, skip, exit, quit.");
        }
    }

    public static void ExecuteCombatCommand(Player player) {
        String userInput = Terminal.Read();
        if (userInput == null || userInput.isEmpty())
            return;

        String[] args = userInput.split(" ");
        String command = args[0];

        switch (command) {
            case "use":
                Use(args, player);
                break;
            case "attack":
                Attack(args, player);
                break;
            case "commands":
                PrintUsableCommands();
                break;
            case "inventory":
                player.PrintInventory();
                break;
            case "skip":
                Combat.SetSkipTurn(true);
                return;
            case "exit":
            case "quit":
                Combat.CombatEnd();
                GameState.IsFinished = true;
                Terminal.CloseTerminal();
                break;
            default:
                Terminal.PrintLine("Unknown command.\n");
        }
    }

    public static void ExecuteResponseCommand(){
        Terminal.PrintLine("Your Stamina is: " + GameState.MainCharacter.GetCurrentStamina());
        Terminal.PrintLine("You can either dodge, block or do nothing and skip");
        while(true) {
            String userInput = Terminal.Read();
            if (userInput == null || userInput.isEmpty()) {
                Terminal.PrintLine("You decided to do nothing\n");
                return;
            }

            String[] args = userInput.split(" ");
            String command = args[0];

            switch (command) {
                case "block":
                    GameState.MainCharacter.SetCurrentStamina(GameState.MainCharacter.GetCurrentStamina() - 1);
                    GameState.MainCharacter.SetIsBlock(true);
                    return;
                case "dodge":
                    GameState.MainCharacter.SetCurrentStamina(GameState.MainCharacter.GetCurrentStamina() - 1);
                    GameState.MainCharacter.SetIsDodge(true);
                    return;
                case "skip":
                    Terminal.PrintLine("You decided to do nothing\n");
                    return;
                default:
                    Terminal.PrintLine("Wrong command, Please try again:");
            }
        }
    }

    private static void Examine(String[] args, Location playerLocation) {
        String description = "";
        Location currentLocation = GameState.MainCharacter.GetCurrentLocation();

        if (args.length == 1) {
            description = currentLocation.GetDescription();
        }
        else if (playerLocation.ContainsNpc(args[1])) {
            NPC examinedNPC = currentLocation.GetNpc(args[1]);
            description = examinedNPC.GetDescription();
        }
        else if (playerLocation.ContainsItem(args[1])) {
            Item examinedItem = currentLocation.GetItem(args[1]);
            description = examinedItem.GetDescription();
        }

        if (description.equals("")) {
            Terminal.PrintLine("No such thing is available to be examined.\n");
            return;
        }
        Terminal.PrintLine(description);
    }

    private static void Talk(String[] args, Player player) {
        if (args.length < 3 || !args[1].equals("to")) {
            Terminal.PrintLine("Unknown command.");
            return;
        }
        if (args.length > 3) {
            Terminal.PrintLine("Too many arguments.\n");
            return;
        }
        player.TalkTo(args[2]);
    }

    private static void Take(String[] args, Player player) {
        if (args.length < 2) {
            Terminal.PrintLine("Please specify an item.\n");
            return;
        }
        if (args.length > 2) {
            Terminal.PrintLine("Too many arguments.\n");
            return;
        }
        player.Take(args[1]);
    }

    private static void Move(String[] args, Player player) {
        if (args.length < 2) {
            Terminal.PrintLine("Please specify a direction.\n");
            return;
        }
        if (args.length > 2) {
            Terminal.PrintLine("Too many arguments.\n");
            return;
        }

        DirectionEnum direction;
        try {
            direction = DirectionEnum.valueOf(args[1]);
        } catch (IllegalArgumentException ex) {
            Terminal.PrintLine("Unknown direction.\n");
            return;
        }

        player.Move(direction);
    }

    private static void Use(String[] args, Player player) {
        if (args.length < 2) {
            Terminal.PrintLine("Please specify an item to use.\n");
            return;
        }
        if (args.length > 3) {
            Terminal.PrintLine("Too many arguments.\n");
            return;
        }

        String itemToUseName = args[1];

        if (args.length == 2) {
            player.Use(itemToUseName);
            return;
        }

        String target = args[2];

        if (player.GetCurrentLocation().ContainsNpc(target)) {
            player.UseOnNpc(itemToUseName, args[2]);
            return;
        }
        else if (player.GetCurrentLocation().ContainsItem(target)) {
            player.UseOnItem(itemToUseName, args[2]);
            return;
        }
        Terminal.PrintLine("No such item or character in the current location.\n");
    }

    private static void Attack(String[] args, Player player) {
        if (args.length < 2) {
            Terminal.PrintLine("Please specify a character.\n");
            return;
        }
        if (args.length > 2) {
            Terminal.PrintLine("Too many arguments.\n");
            return;
        }
        if(!GameState.MainCharacter.GetCurrentLocation().ContainsNpc(args[1])){
            Terminal.PrintLine("There is no such NPC on the location\n");
            return;
        }
        player.Attack(args[1]);
    }
}