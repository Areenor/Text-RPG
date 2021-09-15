import Default_classes.Location;
import Game_data.GameState;
import Services.Combat;
import Services.Controller;
import Services.InitiationService;
import Services.Terminal;
import org.apache.log4j.BasicConfigurator;

import java.io.FileNotFoundException;

public class Main {
    public static void main (String[] args) throws FileNotFoundException {
        BasicConfigurator.configure();
        InitiationService.InitiateLocations();

        Terminal.PrintLine("Welcome Player! ");
        InitiationService.InitiateMainCharacter(GameState.SpawnLocation);

        Location currentLocation = GameState.MainCharacter.GetCurrentLocation();
        String description = currentLocation.GetDescription();
        Terminal.PrintLine(description + "\n");

        while (!GameState.IsFinished) {
            if(!GameState.Combat){ Controller.ExecuteCommand(GameState.MainCharacter); }
            else { Combat.Run(Combat.getCombatOrder(), 0); }
        }
    }
}

