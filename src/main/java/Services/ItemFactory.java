package Services;

import Configuration_models.*;
import Default_classes.*;
import com.alibaba.fastjson.JSON;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ItemFactory {
    private static Path storyDirPath = Paths.get(System.getProperty("user.dir"), "story");
    private static Path itemJsonDirPath = Paths.get(storyDirPath.toString(), "junk");
    private static Path consumJsonDirPath = Paths.get(storyDirPath.toString(), "consumables");
    private static Path keyitemsJsonDirPath = Paths.get(storyDirPath.toString(), "keyitems");
    private static Path equipmentJsonDirPath = Paths.get(storyDirPath.toString(), "equipment");

    public static Item GetItem(String itemName) throws FileNotFoundException {
        Path consumConfigFilePath = Paths.get(consumJsonDirPath.toString(), itemName + ".json");
        Path keyItemConfigFilePath = Paths.get(keyitemsJsonDirPath.toString(), itemName + ".json");
        Path equipmentConfigFilePath = Paths.get(equipmentJsonDirPath.toString(), itemName + ".json");
        Path itemConfigFilePath = Paths.get(itemJsonDirPath.toString(), itemName + ".json");

        if(IsExistingFile(consumConfigFilePath)) { //If the item name belongs to a consumable item file
            ConsumConfig consumConfig = ReturnNewConsumConfig(consumConfigFilePath);
            return new Consumable(consumConfig);
        }
        else if(IsExistingFile(keyItemConfigFilePath)) { //If the item name belongs to a key item file
            KeyItemConfig keyItemConfig = ReturnNewKeyItemConfig(keyItemConfigFilePath);
            return new KeyItem(keyItemConfig);
        }
        else if(IsExistingFile(equipmentConfigFilePath)) { //If the item name belongs to a equipment item file
            EquipConfig equipConfig = ReturnNewEquipConfig(equipmentConfigFilePath);
            return new Equipment(equipConfig);
        }
        else if(IsExistingFile(itemConfigFilePath)) { //If the item name belongs to a junk item file
            ItemConfig itemConfig = ReturnNewJunkItemConfig(itemConfigFilePath);
            return new Junk(itemConfig);
        }
        else { //There is no file found with the requested item name, so exception is thrown.
            throw new FileNotFoundException("item configuration file does not exist.");
        }
    }

    private static boolean IsExistingFile(Path filePath) {
        return Files.exists(filePath);
    }

    private static ConsumConfig ReturnNewConsumConfig(Path configFilePath) throws FileNotFoundException {
        String objectConfigFileContent = InitiationService.readLineByLine(configFilePath.toString());
        ConsumConfig consumConfig = JSON.parseObject(objectConfigFileContent, ConsumConfig.class);
        return consumConfig;
    }

    private static KeyItemConfig ReturnNewKeyItemConfig(Path configFilePath) throws FileNotFoundException {
        String objectConfigFileContent = InitiationService.readLineByLine(configFilePath.toString());
        KeyItemConfig keyItemConfig = JSON.parseObject(objectConfigFileContent, KeyItemConfig.class);
        return keyItemConfig;
    }

    private static EquipConfig ReturnNewEquipConfig(Path configFilePath) throws FileNotFoundException {
        String objectConfigFileContent = InitiationService.readLineByLine(configFilePath.toString());
        EquipConfig equipConfig = JSON.parseObject(objectConfigFileContent, EquipConfig.class);
        return equipConfig;
    }

    private static ItemConfig ReturnNewJunkItemConfig(Path configFilePath) throws FileNotFoundException {
        String objectConfigFileContent = InitiationService.readLineByLine(configFilePath.toString());
        ItemConfig itemConfig = JSON.parseObject(objectConfigFileContent, ItemConfig.class);
        return itemConfig;
    }
}
