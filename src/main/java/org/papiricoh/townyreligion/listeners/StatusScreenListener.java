package org.papiricoh.townyreligion.listeners;
import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.papiricoh.townyreligion.TownyReligion;
import org.papiricoh.townyreligion.object.Religion;

public class StatusScreenListener implements Listener {

    @EventHandler
    public void onTownStatusScreen(TownStatusScreenEvent event) {
        Town town = event.getTown();
        for (Religion r : TownyReligion.religions) {
            if(r.containsTown(town)) {
                event.getStatusScreen().addComponentOf("religion", Component.newline().append(Component.text(r.getName() + " God: " + r.getMain_god().getName())));
            }
        }

    }

}
