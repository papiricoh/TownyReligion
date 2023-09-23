package org.papiricoh.townyreligion;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.papiricoh.townyreligion.object.Religion;
import org.papiricoh.townyreligion.object.god.God;
import org.papiricoh.townyreligion.parser.ReligionParser;

import java.util.ArrayList;

public final class TownyReligion extends JavaPlugin {
    private ArrayList<Religion> world_religions;
    private FileConfiguration config;
    private static ArrayList<Religion> religions;
    private static ArrayList<God> gods;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        gods = ReligionParser.loadGods(config);
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
