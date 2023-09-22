package org.papiricoh.townyreligion;

import org.bukkit.plugin.java.JavaPlugin;
import org.papiricoh.townyreligion.object.Religion;

import java.util.ArrayList;

public final class TownyReligion extends JavaPlugin {
    private ArrayList<Religion> world_religions;

    @Override
    public void onEnable() {
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
