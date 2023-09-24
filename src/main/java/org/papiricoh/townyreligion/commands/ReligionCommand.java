package org.papiricoh.townyreligion.commands;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.papiricoh.townyreligion.TownyReligion;
import org.papiricoh.townyreligion.object.Religion;
import org.papiricoh.townyreligion.object.god.God;

import java.util.ArrayList;
import java.util.List;

public class ReligionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is only player command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Use /tr help to display available commands.");
            return true;
        }

        if ("create".equalsIgnoreCase(args[0])) {
            if (args.length < 2) {
                player.sendMessage("Please, set god and religion name.");
                return true;
            }
            String godName = args[1];
            God god = getGod(godName);
            if(god == null) {
                player.sendMessage("This god doesn't exists.");
                return true;
            }

            String religionName = args[2];
            if(TownyReligion.getReligion(player) != null) {
                player.sendMessage("You don't have permissions or you have already a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if (isMayor) {
                try {
                    Religion religion = new Religion(religionName, TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown(), god);
                    TownyReligion.religions.add(religion);
                    player.sendMessage("Religion " + religionName + " created!");
                    return true;
                } catch (NotRegisteredException e) {
                    player.sendMessage("You don't have a town.");
                    return true;
                }
            }

        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("create");

        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            for (God g : TownyReligion.gods) {
                suggestions.add(g.getName());
            }
        }

        return suggestions;
    }

    private God getGod(String godName) {
        for (God g : TownyReligion.gods) {
            if(g.getName().equals(godName)) {
                return g;
            }
        }
        return null;
    }

}
