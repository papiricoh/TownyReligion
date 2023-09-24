package org.papiricoh.townyreligion.parser;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.papiricoh.townyreligion.object.Religion;
import org.papiricoh.townyreligion.object.god.God;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class ReligionParser {

    public static boolean deleteReligionFile(String fileName, JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();

        File religionFile = new File(dataFolder + File.separator + "religions", fileName);

        if (religionFile.exists()) {
            return religionFile.delete();
        }

        return false;
    }

    public static ArrayList<God> loadGods(FileConfiguration config) {
        ArrayList<God> gods = new ArrayList<>();

        ConfigurationSection godsSection = config.getConfigurationSection("gods");
        if (godsSection != null) {
            for (String godName : godsSection.getKeys(false)) {
                ConfigurationSection godSection = godsSection.getConfigurationSection(godName);

                ConfigurationSection potionEffectSection = godSection.getConfigurationSection("potionEffect");
                PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(potionEffectSection.getString("type")), potionEffectSection.getInt("duration"), potionEffectSection.getInt("intensity"));

                Material material = Material.valueOf(godSection.getString("material"));
                Material block = Material.valueOf(godSection.getString("block"));

                God god = new God(godName, potionEffect, material, block);
                gods.add(god);
            }
        }

        return gods;
    }

    public static Religion loadReligions(File file, ArrayList<God> gods){
        UUID uuid = null;
        String name = null;
        UUID foundingTownUuid = null;
        God god = null;
        ArrayList<Town> towns = null;
        Block altar = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("UUID: ")) {
                    uuid = UUID.fromString(line.substring("UUID: ".length()).replace("\n", "").replace("\r", ""));
                } else if (line.startsWith("Name: ")) {
                    name = line.substring("Name: ".length()).replace("\n", "").replace("\r", "");
                } else if (line.startsWith("Founding_town_uuid: ")) {
                    foundingTownUuid = UUID.fromString(line.substring("Founding_town_uuid: ".length()).replace("\n", "").replace("\r", ""));
                } else if (line.startsWith("Towns: ")) {
                    towns = parseTowns(line.substring("Towns: ".length()).replace("\n", "").replace("\r", ""));
                } else if (line.startsWith("God: ")) {
                    god = toGod(line.substring("God: ".length()).replace("\n", "").replace("\r", ""), gods);
                } else if (line.startsWith("Altar: ")) {
                    altar = parseBlock(line.substring("Altar: ".length()).replace("\n", "").replace("\r", ""));
                }
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Religion religion = new Religion(uuid, name, foundingTownUuid, towns, god, altar);
        return religion;
    }

    private static Block parseBlock(String substring) {
        if(substring.equals("")) {
            return null;
        }
        String[] segments = substring.split(" | ");
        World world = Bukkit.getWorld(segments[0]);
        if(world == null) {
            return null;
        }
        Block block = world.getBlockAt(Integer.parseInt(segments[1]), Integer.parseInt(segments[2]), Integer.parseInt(segments[3]));
        if(block != null) {
            return block;
        }
        return null;
    }

    private static ArrayList<Town> parseTowns(String substring) {
        if(substring.equals("")) {
            return null;
        }
        ArrayList<Town> towns = new ArrayList<>();
        String[] segments = substring.split(" | ");
        for (String s: segments) {
            Town t = TownyAPI.getInstance().getTown(UUID.fromString(s));
            if(t != null) {
                towns.add(t);
            }
        }
        return towns;
    }

    private static God toGod(String god_name, ArrayList<God> gods) {
        for (God g : gods) {
            if(g.getName().equals(god_name)) {
                return g;
            }
        }
        return null;
    }

    public static void saveReligion(Religion religion, JavaPlugin plugin) {
        File religionsFolder = new File(plugin.getDataFolder(), "/religions");
        if (!religionsFolder.exists()) {
            religionsFolder.mkdirs();
        }
        File religionFile = new File(religionsFolder, religion.getUuid() + ".txt");
        try (FileWriter writer = new FileWriter(religionFile)) {
            writer.write("UUID: " + religion.getUuid().toString() + "\n");
            writer.write("Name: " + religion.getName() + "\n");
            writer.write("Founding_town_uuid: " + religion.getFoundingTown().getUUID().toString() + "\n");
            writer.write("God: " + religion.getMain_god().getName() + "\n");
            writer.write(transcribeTowns(religion.getTowns()) + "\n");
            writer.write(transcribeBlock(religion.getAltar()) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String transcribeBlock(Chest altar) {
        String string = "Altar: ";
        if(altar == null) {
            return "Altar: ";
        }
        string += altar.getWorld().getName() + " | ";
        string += altar.getX() + " | ";
        string += altar.getY() + " | ";
        string += altar.getZ() + " | ";
        return string.substring(0, string.length() - 3);
    }

    private static String transcribeTowns(ArrayList<Town> towns) {
        String string = "Towns: ";
        if(towns.isEmpty()) {
            return "Towns: ";
        }
        for (Town t : towns) {
            string += t.getUUID() + " | ";
        }
        return string.substring(0, string.length() - 3);
    }
}
