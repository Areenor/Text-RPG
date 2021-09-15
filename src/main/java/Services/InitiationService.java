package Services;

import Configuration_models.*;
import Default_classes.*;
import Game_data.GameState;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class InitiationService {
    private static Path storyDirPath = Paths.get(System.getProperty("user.dir"), "story");
    private static Path locationJsonDirPath = Paths.get(storyDirPath.toString(), "locations");
    private static Path characterJsonDirPath = Paths.get(storyDirPath.toString(), "characters");

    public static void InitiateMainCharacter(String startingLocationName) {
        int pointCount = 3;
        String strengthInput = "";
        String dexterityInput = "";
        String constitutionInput = "";

        String nameInput = Terminal.Read("Please enter the name of your character \n");
        String mainCharacterName = nameInput;
        Terminal.Print("There are three game statistics of you character: Strength, Dexterity and Constitution." +
                "All of them will directly influence the performance of your character in combat. You have 3 points to split between them.\n");

        while(pointCount != 0){
            strengthInput = Terminal.Read("Strength:");
            dexterityInput = Terminal.Read("Dexterity:");
            constitutionInput = Terminal.Read("Constitution:");
            pointCount = pointCount - Integer.parseInt(constitutionInput) - Integer.parseInt(dexterityInput) - Integer.parseInt(strengthInput);
            if(pointCount < 0) {
                pointCount = 3;
                Terminal.Print("Too many points assigned, please assign only " + pointCount + " points\n");
            }
            else if(pointCount > 0) {
                pointCount = 3;
                Terminal.Print("Not enough points assigned, please assign only " + pointCount + " points\n");
            }
        }

        Location startingLocation = GameState.GetLocation(startingLocationName);
        GameState.MainCharacter = new Player(mainCharacterName, Integer.parseInt(strengthInput), Integer.parseInt(dexterityInput), Integer.parseInt(constitutionInput), startingLocation);
        Terminal.Print("Greetings, " + GameState.MainCharacter.GetName() + "\n");
    }

    public static HashMap<String, Item> InitiateCharacterInventory(List<String> inventoryItems) throws FileNotFoundException {
        HashMap<String, Item> inventory = new HashMap<String, Item>();
        if (inventoryItems != null)
            for (String itemName : inventoryItems )
            {
                Item item = ItemFactory.GetItem(itemName);
                inventory.put(itemName, item);
            }
        return inventory;
    }

    public static void InitiateLocations() throws FileNotFoundException {
        List<Location> locations = new ArrayList<Location>();
        File[] locationConfigFiles = locationJsonDirPath.toFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

        assert locationConfigFiles != null;
        for (File locationConfigFile : locationConfigFiles) {
            String locationName = locationConfigFile.getName().replace(".json", "");
            GameState.Locations.put(locationName, InitiateLocation(locationConfigFile.getPath()));
        }
    }

    public static Location InitiateLocation(String locationConfigFilePath) throws FileNotFoundException {
        String locationConfigFileContent = readLineByLine(locationConfigFilePath);
        LocationConfig locationConfig = JSON.parseObject(locationConfigFileContent, LocationConfig.class);
        return new Location(locationConfig);
    }

    public static NPC InitiateCharacter(String characterName) throws FileNotFoundException {
        Path characterConfigFilePath = Paths.get(characterJsonDirPath.toString(), characterName + ".json");
        String characterConfigFileContent = readLineByLine(characterConfigFilePath.toString());
        NPCConfig characterConfig = JSON.parseObject(characterConfigFileContent, NPCConfig.class);
        return new NPC(characterConfig);
    }

    public static String readLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
