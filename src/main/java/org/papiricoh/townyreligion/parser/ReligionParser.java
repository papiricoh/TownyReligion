package org.papiricoh.townyreligion.parser;

import org.bukkit.plugin.java.JavaPlugin;
import org.papiricoh.townyreligion.object.Religion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReligionParser {
    public static void saveReligion(Religion religion, JavaPlugin plugin) {
        File religionFile = new File(plugin.getDataFolder() + "/religions", religion.getUuid() + ".txt");
        try (FileWriter writer = new FileWriter(religionFile)) {
            writer.write("Name: " + religion.getName() + "\n");
            writer.write("UUID: " + religion.getUuid() + "\n");
            writer.write("Father_religion_uuid: " + religion.getFather_religion().getUuid() + "\n");
            writer.write("Founding_town_uuid: " + religion.getFoundingTown().getUUID() + "\n");
            writer.write("God: " + religion.getMain_god() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
