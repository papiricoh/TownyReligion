package org.papiricoh.townyreligion.listeners;
import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.papiricoh.townyreligion.TownyReligion;
import org.papiricoh.townyreligion.object.Religion;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusScreenListener implements Listener {

    @EventHandler
    public void onTownStatusScreen(TownStatusScreenEvent event) {
        Town town = event.getTown();
        for (Religion r : TownyReligion.religions) {
            if(r.containsTown(town)) {
                event.getStatusScreen().addComponentOf("religion", Component.newline().append(Component.text("Religion: " + ChatColor.GREEN + r.getName() + ChatColor.WHITE  + " God: " + ChatColor.GOLD + r.getMain_god().getName() + ChatColor.WHITE )));
            }
        }

    }

    @EventHandler
    public void onNationStatus(NationStatusScreenEvent event) {
        Nation nation = event.getNation();
        List<Town> towns = nation.getTowns();
        Map<String, Integer> religions_towns = new HashMap<>();
        for (Religion r: TownyReligion.religions) {
            int number_of_towns = 0;
            for (Town t: towns) {
                if(r.containsTown(t)) {
                    number_of_towns++;
                }
            }
            if(number_of_towns != 0) {
                religions_towns.put(r.getName(), number_of_towns);
            }
        }
        if(religions_towns.size() < towns.size()) {
            int to_add = towns.size() - religions_towns.size();
            religions_towns.put("Atheism", to_add);
        }
        DecimalFormat df = new DecimalFormat("###.#");
        String to_string = "Religions: ";
        for (String st: religions_towns.keySet()) {
            to_string += st + ": " + ChatColor.AQUA + df.format(((double) religions_towns.get(st) /(double)  towns.size()) * 100) + "% " + ChatColor.WHITE;
        }
        event.getStatusScreen().addComponentOf("religion", Component.newline().append(Component.text(to_string)));
    }

}
