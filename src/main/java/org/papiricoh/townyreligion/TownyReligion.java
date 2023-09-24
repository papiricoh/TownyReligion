package org.papiricoh.townyreligion;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.papiricoh.townyreligion.commands.ReligionCommand;
import org.papiricoh.townyreligion.object.Religion;
import org.papiricoh.townyreligion.object.god.God;
import org.papiricoh.townyreligion.parser.ReligionParser;

import java.io.File;
import java.util.ArrayList;

public final class TownyReligion extends JavaPlugin {
    private FileConfiguration config;
    public static ArrayList<Religion> religions;
    public static ArrayList<God> gods;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = getConfig();
        this.gods = ReligionParser.loadGods(config);
        this.religions = new ArrayList<>();

        File dataFolder = this.getDataFolder();
        File religionsFolder = new File(dataFolder, "religions");
        File[] files = religionsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Religion religion = ReligionParser.loadReligions(file, gods);
                    this.religions.add(religion);
                }
            }}
        else {
            this.getLogger().warning("Religions directory not created.");
        }

        ReligionCommand religionCommand = new ReligionCommand();
        this.getCommand("tr").setExecutor(religionCommand);
        this.getCommand("tr").setTabCompleter(religionCommand);
    }

    @Override
    public void onDisable() {
        for (Religion r :religions) {
            ReligionParser.saveReligion(r, this);
        }
    }

    public static Religion getReligion(Player player) {
        Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
        if(resident == null) {
            return null;
        }
        try {
            return getReligion(resident);
        } catch (NotRegisteredException e) {
            return null;
        }
    }

    private static Religion getReligion(Resident resident) throws NotRegisteredException {
        if(!resident.hasTown()) {
            return null;
        }
        for (Religion r : religions) {
            if(r.containsTown(resident.getTown())) {
                return r;
            }
        }
        return null;
    }
}
